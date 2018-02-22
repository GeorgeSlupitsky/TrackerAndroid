package com.micro_gis.microgistracker;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class PlaceListActivity extends AppCompatActivity {

    ListView placeList;
    MarkerCustomAdapter markersCustomAdapter;
    ArrayList<Marker> markers = new ArrayList<Marker>();
    DBHelper dbHelper;
    SharedPreferences preferences;
    private int markersCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);

        final Button back = (Button) findViewById(R.id.back_buttonPL);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        preferences = getSharedPreferences("mypref", MODE_PRIVATE);
        markersCount = preferences.getInt("markersCount", 0);

        TextView title = findViewById(R.id.toolbar_title);
        title.setText(getString(R.string.markers) + " (" + markersCount + ")");

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

            Comparator<Marker> comparator = new Comparator<Marker>() {
                public int compare(Marker m1, Marker m2) {

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

            Collections.sort(markers, comparator);
        }

        markersCustomAdapter = new MarkerCustomAdapter(PlaceListActivity.this, R.layout.place,
                markers);

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

        if (markers.size() == 0){
            ViewGroup viewGroup = (ViewGroup) placeList.getParent();
            int index = viewGroup.indexOfChild(placeList);
            viewGroup.removeView(placeList);

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
