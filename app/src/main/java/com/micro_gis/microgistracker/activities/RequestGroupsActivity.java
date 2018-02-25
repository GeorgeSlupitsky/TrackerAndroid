package com.micro_gis.microgistracker.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.micro_gis.microgistracker.DBHelper;
import com.micro_gis.microgistracker.R;
import com.micro_gis.microgistracker.adapters.RequestCustomAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class RequestGroupsActivity extends AppCompatActivity {

    final String ATTRIBUTE_NAME_ID = "id";
    final String ATTRIBUTE_NAME_TEXT = "text";

    final String LOG_TAG = "myLogs";

    ListView lvSimple;
    RequestCustomAdapter requestCustomAdapter;
    ArrayList<Map<String, Object>> data;
    Button addGroup;
    View footerView;
    boolean addFooter;
    boolean addListView;

    DBHelper dbHelper;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_groups);

        addFooter = false;
        addListView = true;

        dbHelper = new DBHelper(this);

        LayoutInflater layoutInflater = getLayoutInflater();

        footerView = layoutInflater.inflate(R.layout.footer_activity_request, null);

        addGroup = (Button) footerView.findViewById(R.id.footerAddGroup);

        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RequestGroupsActivity.this, AddRequestGroupActivity.class);
                startActivity(i);
            }
        });

        lvSimple = (ListView) findViewById(R.id.lvSimple);

        lvSimple.addFooterView(footerView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.query("requestgroup", null, null, null, null, null, null);

        data = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                int idColIndex = c.getColumnIndex("id");
                int nameColIndex = c.getColumnIndex("groupname");

                Map<String, Object> m = new HashMap<>();

                m.put(ATTRIBUTE_NAME_ID, c.getInt(idColIndex));
                m.put(ATTRIBUTE_NAME_TEXT, c.getString(nameColIndex));

                data.add(m);
            } while (c.moveToNext());

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

            String[] from = { ATTRIBUTE_NAME_ID, ATTRIBUTE_NAME_TEXT };

            requestCustomAdapter = new RequestCustomAdapter(this, R.layout.custom_adapter_request_group, from, data);

            lvSimple.setAdapter(requestCustomAdapter);

        } else {
            String[] from = { ATTRIBUTE_NAME_ID, ATTRIBUTE_NAME_TEXT };
            requestCustomAdapter = new RequestCustomAdapter(this, R.layout.custom_adapter_request_group, from, data);
            lvSimple.setAdapter(requestCustomAdapter);
        }

        c.close();
    }
}
