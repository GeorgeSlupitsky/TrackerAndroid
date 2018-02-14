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

    private static final int CM_EDIT_ID = 1;
    private static final int CM_DELETE_ID = 2;

    final String ATTRIBUTE_NAME_ID = "id";
    final String ATTRIBUTE_NAME_TEXT = "text";
    final String ATTRIBUTE_NAME_IMAGE = "image";

    final String LOG_TAG = "myLogs";

    ListView lvSimple;
    SimpleAdapter sAdapter;
    ArrayList<Map<String, Object>> data;
    Button addGroup;

    DBHelper dbHelper;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        dbHelper = new DBHelper(this);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        addGroup = (Button) findViewById(R.id.addGroup);

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

            String[] from = { ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_IMAGE };

            int[] to = { R.id.tvText, R.id.ivImg };

            sAdapter = new SimpleAdapter(this, data, R.layout.group, from, to);

            lvSimple = (ListView) findViewById(R.id.lvSimple);
            lvSimple.setAdapter(sAdapter);

            registerForContextMenu(lvSimple);

        } else
            Log.d(LOG_TAG, "0 rows");
            c.close();

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
                int iconIndex = c.getColumnIndex("icon");

                Map<String, Object> m = new HashMap<>();

                m.put(ATTRIBUTE_NAME_ID, c.getInt(idColIndex));
                m.put(ATTRIBUTE_NAME_TEXT, c.getString(nameColIndex));

                data.add(m);
            } while (c.moveToNext());


            String[] from = { ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_IMAGE };

            int[] to = { R.id.tvText, R.id.ivImg };

            sAdapter = new SimpleAdapter(this, data, R.layout.group, from, to);

            lvSimple = (ListView) findViewById(R.id.lvSimple);
            lvSimple.setAdapter(sAdapter);

            registerForContextMenu(lvSimple);

        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_EDIT_ID, 0, R.string.edit);
        menu.add(1, CM_DELETE_ID, 1, R.string.delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CM_EDIT_ID){
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            Integer id = (Integer)data.get(acmi.position).get("id");

            Intent intent = new Intent(this, RequestAddActivity.class);

            intent.putExtra("id", id+"");

            startActivity(intent);
        }
        if (item.getItemId() == CM_DELETE_ID) {
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            Integer id = (Integer)data.get(acmi.position).get("id");

            db.delete("requestgroup", "id=?", new String[]{Integer.toString(id)});

            data.remove(acmi.position);
            sAdapter.notifyDataSetChanged();
            return true;
        }
        return super.onContextItemSelected(item);
    }


}
