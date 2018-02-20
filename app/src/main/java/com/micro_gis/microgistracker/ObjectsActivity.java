package com.micro_gis.microgistracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User3 on 15.02.2018.
 */

public class ObjectsActivity extends AppCompatActivity {

    private static final String NOT_CONNECTED = "Not connected";

    final String ATTRIBUTE_NAME_ID = "id";
    final String ATTRIBUTE_NAME_TEXT = "text";
    final String ATTRIBUTE_NAME_IMAGE = "image";
    final String ATTRIBUTE_NAME_STATUS = "status";
    final String ATTRIBUTE_NAME_COLOR = "color";

    ObjectCustomAdapter objectCustomAdapter;
    SharedPreferences sharedPreferences;
    ArrayList<Map<String, Object>> data;
    String objects;
    TextView noObjects;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objects);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        noObjects = (TextView) findViewById(R.id.tvNoObjects);

        sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);
        objects = sharedPreferences.getString("groupObjects", "empty");

        if (objects.equals(NOT_CONNECTED)){
            noObjects.setText(getString(R.string.not_connected));
        } else if(objects.equals("empty")){
            noObjects.setText(getString(R.string.empty_object_list));
        } else {
            try {
                data = new ArrayList<>();

                JSONObject obj  = new JSONObject(objects);
                JSONArray arr = obj.getJSONArray("devices");

                for (int i = 0; i < arr.length(); i++) {
                    String icon = null;

                    Map<String, Object> m = new HashMap<>();

                    try {
                        icon = arr.getJSONObject(i).getString("icon");
                    } catch (Exception e) {
                        icon = "car_sedan";
                    }
                    String description = arr.getJSONObject(i).getString("description");
                    String color = arr.getJSONObject(i).getString("color");
                    String status = "Status";
                    Integer id = i;

                    m.put(ATTRIBUTE_NAME_ID, id);
                    m.put(ATTRIBUTE_NAME_TEXT, description);
                    m.put(ATTRIBUTE_NAME_STATUS, status);
                    m.put(ATTRIBUTE_NAME_IMAGE, icon);
                    m.put(ATTRIBUTE_NAME_COLOR, color);

                    data.add(m);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            String[] from = { ATTRIBUTE_NAME_ID, ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_STATUS, ATTRIBUTE_NAME_IMAGE, ATTRIBUTE_NAME_COLOR};

            objectCustomAdapter = new ObjectCustomAdapter(this, R.layout.object, data, from);

            listView = (ListView) findViewById(R.id.lvObjects);

            listView.setAdapter(objectCustomAdapter);
        }

    }
}
