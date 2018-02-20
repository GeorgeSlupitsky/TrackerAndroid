package com.micro_gis.microgistracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestActivity extends AppCompatActivity {

    final String ATTRIBUTE_NAME_ID = "id";
    final String ATTRIBUTE_NAME_TEXT = "text";

    final String LOG_TAG = "myLogs";

    ListView lvSimple;
    RequestCustomAdapter requestCustomAdapter;
    ArrayList<Map<String, Object>> data;
    Button addGroup;

    DBHelper dbHelper;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        dbHelper = new DBHelper(this);

        addGroup = (Button) findViewById(R.id.addGroup);

//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//        Cursor c = db.query("requestgroup", null, null, null, null, null, null);
//
//        data = new ArrayList<Map<String, Object>>();
//
//        if (c.moveToFirst()) {
//            do {
//                int idColIndex = c.getColumnIndex("id");
//                int nameColIndex = c.getColumnIndex("groupname");
//
//                Map<String, Object> m = new HashMap<>();
//
//                m.put(ATTRIBUTE_NAME_ID, c.getInt(idColIndex));
//                m.put(ATTRIBUTE_NAME_TEXT, c.getString(nameColIndex));
//
//                data.add(m);
//
//            } while (c.moveToNext());
//
//            String[] from = { ATTRIBUTE_NAME_ID, ATTRIBUTE_NAME_TEXT };
//
//            requestCustomAdapter = new RequestCustomAdapter(this, R.layout.group, from, data);
//
//            lvSimple = (ListView) findViewById(R.id.lvSimple);
//            lvSimple.setAdapter(requestCustomAdapter);
//
//        } else
//            Log.d(LOG_TAG, "0 rows");
//            c.close();

        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RequestActivity.this, RequestAddActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

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


            String[] from = { ATTRIBUTE_NAME_ID, ATTRIBUTE_NAME_TEXT };

            requestCustomAdapter = new RequestCustomAdapter(this, R.layout.group, from, data);

            lvSimple = (ListView) findViewById(R.id.lvSimple);
            lvSimple.setAdapter(requestCustomAdapter);
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();
    }

}
