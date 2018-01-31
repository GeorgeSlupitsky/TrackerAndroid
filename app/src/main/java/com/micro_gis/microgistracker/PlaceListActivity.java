package com.micro_gis.microgistracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;

public class PlaceListActivity extends AppCompatActivity {

    ListView placeList;
    MarkerCustomAdapter markersCustomAdapter;
    ArrayList<Marker> markers = new ArrayList<Marker>();
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("markers", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do{
                byte[] blob = cursor.getBlob(cursor.getColumnIndex("data"));

                String json = new String(blob);
                Gson gson = new Gson();
                Marker marker = gson.fromJson(json, new TypeToken<Marker>() {
                }.getType());
                markers.add(marker);

            }while (cursor.moveToNext());
        }
        markersCustomAdapter = new MarkerCustomAdapter(PlaceListActivity.this, R.layout.place,
                markers);
        TextView textView = (TextView) findViewById(R.id.spisok_pust);

        if(markers.size()>0){
            textView.setVisibility(View.GONE);
        }else {
            textView.setVisibility(View.VISIBLE);

        }
        placeList = (ListView) findViewById(R.id.listPlaces);
        placeList.setItemsCanFocus(false);
        placeList.setAdapter(markersCustomAdapter);

        placeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {

                    if(v.findViewById(R.id.marker_des).getVisibility()==View.GONE){
                        v.findViewById(R.id.marker_des).setVisibility(View.VISIBLE);
                    }else{
                        v.findViewById(R.id.marker_des).setVisibility(View.GONE);
                    }

            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        markersCustomAdapter.notifyDataSetChanged();
    }
}
