package com.micro_gis.microgistracker.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.micro_gis.microgistracker.DBHelper;
import com.micro_gis.microgistracker.R;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;

/**
 * Created by User3 on 08.02.2018.
 */

public class AddRequestGroupActivity extends AppCompatActivity {

    EditText groupName, accaunt, key, interval, url, group;
    Spinner TZone;
    DBHelper dbHelper;
    SQLiteDatabase db;
    ContentValues cv = new ContentValues();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request_group);

        groupName = (EditText) findViewById(R.id.groupName);
        accaunt =(EditText) findViewById(R.id.accaunt);
        key =(EditText) findViewById(R.id.req_key);
        interval =(EditText) findViewById(R.id.req_interval);
        url =(EditText) findViewById(R.id.req_url);
        group =(EditText) findViewById(R.id.req_group);
        Button save = (Button)findViewById(R.id.req_save);
        Button cancel = (Button) findViewById(R.id.req_cancel);
        assert save != null;


        ArrayAdapter<CharSequence> adapter =
                new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        String[]TZ = new String[24];
        ArrayList<String> TZ1 = new ArrayList<String>();
        for(int i =0 ; i<25;i++){
            TZ1.add("0");
        }
        for(int i = 0; i < 13; i++) {
            TZ1.set(i,"GMT+"+i);
            TZ1.set(12+i,"GMT-"+i);
        }
        for(int i = 0; i < TZ1.size(); i++) {
            adapter.add(TZ1.get(i));
        }
        final Spinner TZone = (Spinner)findViewById(R.id.spinner);
        TZone.setAdapter(adapter);
        int k=2;
//        for (int i =0; i<TZ1.size();i++){
//            if(TZ1.get(i).equals(sharedpreferences.getString("tzone", "GMT+2"))){
//                k=i;
//            }
//        }
        TZone.setSelection(k);

        dbHelper = new DBHelper(this);

        Intent intent = getIntent();

        String id = intent.getStringExtra("id");

        if (id != null){
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            String selectQuery = "select * from requestgroup where id = " + id;

            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst())
            {
                int nameColIndex = cursor.getColumnIndex("groupname");
                int accountColIndex = cursor.getColumnIndex("account");
                int keyColIndex = cursor.getColumnIndex("keyString");
                int urlColIndex = cursor.getColumnIndex("url");
                int requestIntervalColIndex = cursor.getColumnIndex("requestInterval");
                int groupsColIndex = cursor.getColumnIndex("groups");
                int timeZoneColIndex = cursor.getColumnIndex("timeZone");
                groupName.setText(cursor.getString(nameColIndex));
                accaunt.setText(cursor.getString(accountColIndex));
                key.setText(cursor.getString(keyColIndex));
                url.setText(cursor.getString(urlColIndex));
                interval.setText(cursor.getString(requestIntervalColIndex));
                group.setText(cursor.getString(groupsColIndex));
                String timeZone = cursor.getString(timeZoneColIndex);

                for (String s: TZ1){
                    if (timeZone.equals(s)){
                        TZone.setSelection(TZ1.indexOf(s));
                    }
                }
            }

            cursor.close();
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = groupName.getText().toString();
                String accName = accaunt.getText().toString();
                String keyStr = key.getText().toString();
                String urlStr = url.getText().toString();
                String intervalStr = interval.getText().toString();
                String groupStr = group.getText().toString();
                String timeZone = TZone.getSelectedItem().toString();

                SQLiteDatabase db = dbHelper.getWritableDatabase();

                cv.put("groupname", name);
                cv.put("account", accName);
                cv.put("keyString", keyStr);
                cv.put("url", urlStr);
                cv.put("requestInterval", intervalStr);
                cv.put("groups", groupStr);
                cv.put("timeZone", timeZone);

                if (NumberUtils.isParsable(interval.getText().toString())){
                    if (Integer.parseInt(interval.getText().toString()) >= 10){
                        Intent intent = getIntent();
                        String id = intent.getStringExtra("id");

                        if (id != null){
                            db.update("requestgroup", cv, "id = ?", new String[] { id });
                        } else {
                            db.insert("requestgroup", null, cv);
                        }

                        dbHelper.close();
                        finish();

                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                getString(R.string.no_less_interval), Toast.LENGTH_LONG);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            getString(R.string.incorrect_data), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

}
