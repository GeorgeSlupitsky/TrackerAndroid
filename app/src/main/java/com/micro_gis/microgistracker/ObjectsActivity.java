package com.micro_gis.microgistracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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
    final String ATTRIBUTE_NAME_DATE = "date";
    final String ATTRIBUTE_NAME_DRIVER = "driver";
    final String ATTRIBUTE_NAME_TRAILER = "trailer";
    final String ATTRIBUTE_NAME_WIFI = "wifi";
    final String ATTRIBUTE_NAME_LOW_FLOR = "lowFlor";
    final String ATTRIBUTE_NAME_ADDRESS = "address";

    ObjectCustomAdapter objectCustomAdapter;
    SharedPreferences sharedPreferences;
    ArrayList<Map<String, Object>> data;
    String objects;
    TextView noObjects;
    ListView listView;
    Button clearSearch;
    int objectsCount;
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objects);

        final Button back = (Button) findViewById(R.id.back_buttonObjects);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        search = (EditText) findViewById(R.id.inputSearch);
        noObjects = (TextView) findViewById(R.id.tvNoObjects);
        clearSearch = (Button) findViewById(R.id.clearSearch);

        sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);
        objects = sharedPreferences.getString("groupObjects", "empty");
        objectsCount = sharedPreferences.getInt("objectsCount", 0);

        TextView title = findViewById(R.id.toolbar_title);
        title.setText(getString(R.string.objectsGroup) + " (" + objectsCount + ")");

        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
            }
        });

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
                    String status = arr.getJSONObject(i).getString("statusCode");
                    String event = arr.getJSONObject(i).getString("event");
                    Date time = new java.util.Date(Long.parseLong(event)*1000);
                    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
                    Integer id = Integer.parseInt(arr.getJSONObject(i).getString("id"));
                    String driver;

                    try{
                        driver = arr.getJSONObject(i).getString("driverName");
                    } catch (Exception e){
                        driver = "empty";
                    }

                    String trailer;

                    try {
                        trailer = arr.getJSONObject(i).getString("trailer");
                    } catch (Exception e){
                        trailer = "empty";
                    }

                    String wifi = arr.getJSONObject(i).getString("wifi");
                    String lowFlor = arr.getJSONObject(i).getString("lowFlor");

                    String address;

                    try {
                        address = arr.getJSONObject(i).getString("address");
                    } catch (Exception e){
                        address = "---";
                    }


                    m.put(ATTRIBUTE_NAME_ID, id);
                    m.put(ATTRIBUTE_NAME_TEXT, description);
                    m.put(ATTRIBUTE_NAME_STATUS, status);
                    m.put(ATTRIBUTE_NAME_IMAGE, icon);
                    m.put(ATTRIBUTE_NAME_COLOR, color);
                    m.put(ATTRIBUTE_NAME_DATE, date);
                    m.put(ATTRIBUTE_NAME_DRIVER, driver);
                    m.put(ATTRIBUTE_NAME_TRAILER, trailer);
                    m.put(ATTRIBUTE_NAME_WIFI, wifi);
                    m.put(ATTRIBUTE_NAME_LOW_FLOR, lowFlor);
                    m.put(ATTRIBUTE_NAME_ADDRESS, address);

                    data.add(m);
                }

                Comparator<Map<String, Object>> mapComparator = new Comparator<Map<String, Object>>() {
                    public int compare(Map<String, Object> m1, Map<String, Object> m2) {

                        String m1Name = (String) m1.get(ATTRIBUTE_NAME_TEXT);
                        String m2Name = (String) m2.get(ATTRIBUTE_NAME_TEXT);

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

                Collections.sort(data, mapComparator);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            String[] from = { ATTRIBUTE_NAME_ID, ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_STATUS, ATTRIBUTE_NAME_IMAGE, ATTRIBUTE_NAME_COLOR, ATTRIBUTE_NAME_DATE,
                    ATTRIBUTE_NAME_DRIVER, ATTRIBUTE_NAME_TRAILER, ATTRIBUTE_NAME_WIFI, ATTRIBUTE_NAME_LOW_FLOR, ATTRIBUTE_NAME_ADDRESS};

            objectCustomAdapter = new ObjectCustomAdapter(this, R.layout.object, data, from);

            listView = (ListView) findViewById(R.id.lvObjects);

            listView.setAdapter(objectCustomAdapter);

            search.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    String text = search.getText().toString().toLowerCase(Locale.getDefault());
                    objectCustomAdapter.filter(text);
                }
            });

        }

    }
}
