package com.micro_gis.microgistracker;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User3 on 15.02.2018.
 */

public class GroupsActivity extends AppCompatActivity{

    final String ATTRIBUTE_NAME_ID = "id";
    final String ATTRIBUTE_NAME_TEXT = "text";

    final String LOG_TAG = "myLogs";

    ListView lvSimple;
    TextView textView;
    ArrayList<Map<String, Object>> data;
    GroupsCustomAdapter groupsCustomAdapter;
    DBHelper dbHelper;
    RadioButton r;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        preferences = getPreferences(MODE_PRIVATE);

        dbHelper = new DBHelper(this);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.query("requestgroup", null, null, null, null, null, null);

        data = new ArrayList<Map<String, Object>>();

        if (c.moveToFirst()) {
            do {
                int idColIndex = c.getColumnIndex("id");
                int nameColIndex = c.getColumnIndex("groupname");

                Map<String, Object> m = new HashMap<>();

                m.put(ATTRIBUTE_NAME_ID, c.getInt(idColIndex));
                m.put(ATTRIBUTE_NAME_TEXT, c.getString(nameColIndex));

                data.add(m);

            } while (c.moveToNext());

            String[] from = { ATTRIBUTE_NAME_ID, ATTRIBUTE_NAME_TEXT};

            groupsCustomAdapter = new GroupsCustomAdapter(this, from, data);

            lvSimple = (ListView) findViewById(R.id.lvGroups);

            lvSimple.setAdapter(groupsCustomAdapter);

        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();

        textView = (TextView) findViewById(R.id.no_groups);

        if (!data.isEmpty()){
            textView.setText("");
        }
    }
}
