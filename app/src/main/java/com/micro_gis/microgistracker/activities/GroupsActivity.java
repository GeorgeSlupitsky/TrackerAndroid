package com.micro_gis.microgistracker.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.micro_gis.microgistracker.DBHelper;
import com.micro_gis.microgistracker.Power;
import com.micro_gis.microgistracker.adapters.GroupsCustomAdapter;
import com.micro_gis.microgistracker.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User3 on 15.02.2018.
 */

public class GroupsActivity extends AppCompatActivity{

    private final String ATTRIBUTE_NAME_ID = "id";
    private final String ATTRIBUTE_NAME_TEXT = "text";

    private final String LOG_TAG = "myLogs";

    private ListView lvSimple;
    private TextView textView;
    private ArrayList<Map<String, Object>> data;
    private GroupsCustomAdapter groupsCustomAdapter;
    private DBHelper dbHelper;
    private RadioButton r;
    private SharedPreferences preferences;
    private int groupsCount;
    private Handler handler = new Handler();

    Runnable checkCharging = new Runnable() {
        @Override
        public void run() {
            if (Power.isConnected(GroupsActivity.this)){
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
        setContentView(R.layout.activity_groups);

        final Button back = (Button) findViewById(R.id.back_buttonGroups);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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

        groupsCount = preferences.getInt("groupsCount", 0);

        TextView title = findViewById(R.id.toolbar_title);
        title.setText(getString(R.string.objectGroups) + " (" + groupsCount + ")");

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

            Comparator<Map<String, Object>> mapComparator = new Comparator<Map<String, Object>>() {
                public int compare(Map<String, Object> m1, Map<String, Object> m2) {

                    String m1Name = (String) m1.get(ATTRIBUTE_NAME_TEXT);
                    String m2Name = (String) m2.get(ATTRIBUTE_NAME_TEXT);

                    if (!m1Name.matches("[0-9]+") || !m2Name.matches("[0-9]+")){
                        String name1 = m1Name.replaceAll("\\d", "");
                        String name2 = m2Name.replaceAll("\\d", "");

                        if (name1.equalsIgnoreCase(name2)){
                            return extractInt(m1Name) - (extractInt(m2Name));
                        }
                    }

                    return m1Name.compareTo(m2Name);
                }

                int extractInt(String s) {
                    String num = s.replaceAll("\\D", "");
                    return num.isEmpty() ? 0 : Integer.parseInt(num);
                }
            };

            Collections.sort(data, mapComparator);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(checkCharging);
    }
}
