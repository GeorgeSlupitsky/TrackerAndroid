package com.micro_gis.microgistracker;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TrackListActivity extends AppCompatActivity {

    ListView trackList;
    TrackCustomAdapter trackCustomAdapter;
    ArrayList<Track> trackArray = new ArrayList<Track>();
    DBHelper dbHelper;
    SharedPreferences preferences;
    private int tracksCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_list);

        final Button back = (Button) findViewById(R.id.back_buttonTL);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        trackList = (ListView) findViewById(R.id.listView);

        preferences = getSharedPreferences("mypref", MODE_PRIVATE);
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

            Comparator<Track> comparator = new Comparator<Track>() {
                public int compare(Track m1, Track m2) {

                    String m1Name = m1.getName();
                    String m2Name = m2.getName();

                    String name1 = m1Name.replaceAll("\\d", "");
                    String name2 = m2Name.replaceAll("\\d", "");

                    if (name1.equalsIgnoreCase(name2)){
                        return extractInt(m1Name) - (extractInt(m2Name));
                    }

                    return m1Name.compareTo(m2Name);
                }

                int extractInt(String s) {
                    String num = s.replaceAll("\\D", "");
                    return num.isEmpty() ? 0 : Integer.parseInt(num);
                }
            };

            Collections.sort(trackArray, comparator);

        }

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
        trackCustomAdapter = new TrackCustomAdapter(TrackListActivity.this, R.layout.row,
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
}
