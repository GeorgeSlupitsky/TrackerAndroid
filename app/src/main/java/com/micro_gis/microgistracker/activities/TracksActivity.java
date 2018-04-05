package com.micro_gis.microgistracker.activities;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.micro_gis.microgistracker.DBHelper;
import com.micro_gis.microgistracker.Power;
import com.micro_gis.microgistracker.R;
import com.micro_gis.microgistracker.models.database.Track;
import com.micro_gis.microgistracker.adapters.TrackCustomAdapter;


import java.util.ArrayList;
import java.util.Collections;

public class TracksActivity extends AppCompatActivity {

    private ListView trackList;
    private TrackCustomAdapter trackCustomAdapter;
    private ArrayList<Track> trackArray = new ArrayList<Track>();
    private DBHelper dbHelper;
    private SharedPreferences preferences;
    private int tracksCount;
    private Handler handler = new Handler();

    Runnable checkCharging = new Runnable() {
        @Override
        public void run() {
            if (Power.isConnected(TracksActivity.this)){
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } else {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
            handler.postDelayed(this, 3000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks);

        final Button back = (Button) findViewById(R.id.back_buttonTL);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        trackList = (ListView) findViewById(R.id.listView);

        preferences = getSharedPreferences("mypref", MODE_PRIVATE);

        String screenActivity = preferences.getString("screenActivity", "normal");

        switch (screenActivity) {
            case "normal":
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                handler.removeCallbacks(checkCharging);
                break;
            case "always":
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                handler.removeCallbacks(checkCharging);
                break;
            case "while_charging":
                handler.post(checkCharging);
                break;
        }

        tracksCount = preferences.getInt("tracksCount", 0);

        TextView title = findViewById(R.id.toolbar_title);
        title.setText(getString(R.string.tracksToolbar) + " (" + tracksCount + ")");

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("trackdata", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
           do{ byte[] blob = cursor.getBlob(cursor.getColumnIndex("track"));

            String json = new String(blob);
            Gson gson = new Gson();
            Track track = gson.fromJson(json, new TypeToken<Track>() {
            }.getType());

               trackArray.add(track);
            }while (cursor.moveToNext());

        }

        cursor.close();
        db.close();

        Collections.reverse(trackArray);
        if(trackArray.size()>25){
            int delta = trackArray.size()-25;
            Collections.reverse(trackArray);
            for(int i =0; i<delta;i++){

                try
                {
                    db.delete("trackdata", "name = ?", new String[] {trackArray.get(25+i).getName() });
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    db.close();
                }
            }
            Collections.reverse(trackArray);
        }
        trackCustomAdapter = new TrackCustomAdapter(TracksActivity.this, R.layout.custom_adapter_track,
                trackArray);
        trackList.setItemsCanFocus(false);
        trackList.setAdapter(trackCustomAdapter);
        trackList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {

                if( v.findViewById(R.id.descriptionLayout).getVisibility()==View.GONE){
                    v.findViewById(R.id.descriptionLayout).setVisibility(View.VISIBLE);
                    v.findViewById(R.id.altitudelayout).setVisibility(View.VISIBLE);
                    v.findViewById(R.id.chart).setVisibility(View.VISIBLE);

                }
                else{
                    v.findViewById(R.id.altitudelayout).setVisibility(View.GONE);
                    v.findViewById(R.id.descriptionLayout).setVisibility(View.GONE);
                    v.findViewById(R.id.chart).setVisibility(View.GONE);

                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (trackArray.size() == 0){
            ViewGroup viewGroup = (ViewGroup) trackList.getParent();
            int index = viewGroup.indexOfChild(trackList);
            viewGroup.removeView(trackList);

            TextView textView = new TextView(this);
            textView.setText(R.string.spisok_pust);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }

            textView.setTextSize(20);
            textView.setPadding(0,20,0,0);

            viewGroup.addView(textView, index);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(checkCharging);
    }
}
