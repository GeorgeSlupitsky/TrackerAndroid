package com.micro_gis.microgistracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.util.ArrayList;
import java.util.Collections;

public class TrackListActivity extends AppCompatActivity {

    ListView trackList;
    TrackCustomAdapter trackCustomAdapter;
    ArrayList<Track> trackArray = new ArrayList<Track>();
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_list);
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


        TextView textView = (TextView) findViewById(R.id.spisok_pust1);

        if(trackArray.size()>0){
            textView.setVisibility(View.GONE);
        }else {
            textView.setVisibility(View.VISIBLE);

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
        trackList = (ListView) findViewById(R.id.listView);
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
}
