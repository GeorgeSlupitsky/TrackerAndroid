package com.micro_gis.microgistracker.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.micro_gis.microgistracker.DBHelper;
import com.micro_gis.microgistracker.Power;
import com.micro_gis.microgistracker.R;
import com.micro_gis.microgistracker.adapters.DriverNotificationCustomAdapter;
import com.micro_gis.microgistracker.models.rest.RequestDriverEvents;
import com.micro_gis.microgistracker.models.rest.ResponseDriverEvents;
import com.micro_gis.microgistracker.models.rest.ResponseStatuses;
import com.micro_gis.microgistracker.models.rest.Voyage;
import com.micro_gis.microgistracker.retrofit.API;
import com.micro_gis.microgistracker.retrofit.APIController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverNotificationActivity extends AppCompatActivity {

    private final String ATTRIBUTE_NAME_TIME = "time";
    private final String ATTRIBUTE_MESSAGE = "message";
    private final String ATTRIBUTE_ID = "id";
    private final String ATTRIBUTE_IS_SEEN = "is seen";
    private final String ATTRIBUTE_VOYAGE_ID = "voyage id";

    private String[] from = {ATTRIBUTE_NAME_TIME, ATTRIBUTE_MESSAGE, ATTRIBUTE_ID, ATTRIBUTE_IS_SEEN, ATTRIBUTE_VOYAGE_ID};

    private ArrayList<Map<String, Object>> data;

    private static API api;

    private DBHelper dbHelper;

    private String url;
    private Integer login;
    private String password;
    private Handler handler = new Handler();
    private ListView listView;
    private DriverNotificationCustomAdapter driverNotificationCustomAdapter;

    private SharedPreferences sharedPreferences;

    Runnable checkCharging = new Runnable() {
        @Override
        public void run() {
            if (Power.isConnected(DriverNotificationActivity.this)){
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } else {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
            handler.postDelayed(this, 3000);
        }
    };

    Runnable checkForNotification = new Runnable() {
        @Override
        public void run() {
            data = new ArrayList<>();

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            Cursor c = db.query("messages", null, null, null, null, null, null);

            if (c.moveToFirst()) {
                do {
                    int timeColIndex = c.getColumnIndex("time");
                    int messageColIndex = c.getColumnIndex("message");
                    int idColIndex = c.getColumnIndex("id");
                    int isSeenColIndex = c.getColumnIndex("isSeen");
                    int voyageIdColIndex = c.getColumnIndex("voyageId");

                    Map<String, Object> m = new HashMap<>();

                    m.put(ATTRIBUTE_NAME_TIME, c.getLong(timeColIndex));
                    m.put(ATTRIBUTE_MESSAGE, c.getString(messageColIndex));
                    m.put(ATTRIBUTE_ID, c.getInt(idColIndex));
                    m.put(ATTRIBUTE_IS_SEEN, c.getInt(isSeenColIndex));
                    m.put(ATTRIBUTE_VOYAGE_ID, c.getLong(voyageIdColIndex));

                    data.add(m);

                } while (c.moveToNext());
            }

            c.close();
            db.close();

            Collections.reverse(data);

            driverNotificationCustomAdapter = new DriverNotificationCustomAdapter(DriverNotificationActivity.this, R.layout.custom_adapter_driver_notification, data, from);

            listView.setAdapter(driverNotificationCustomAdapter);

            if (data.isEmpty()){
                handler.postDelayed(this,1000);
            } else {
                handler.postDelayed(this, 30000);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data = new ArrayList<>();

        setContentView(R.layout.activity_driver_notification);

        listView = findViewById(R.id.lvDriverNotification);

        sharedPreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);

        dbHelper = new DBHelper(this);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.query("messages", null, null, null, null, null, null);

        if (c.moveToFirst()) {
            do {
                int timeColIndex = c.getColumnIndex("time");
                int messageColIndex = c.getColumnIndex("message");
                int idColIndex = c.getColumnIndex("id");
                int isSeenColIndex = c.getColumnIndex("isSeen");
                int voyageIdColIndex = c.getColumnIndex("voyageId");

                Map<String, Object> m = new HashMap<>();

                m.put(ATTRIBUTE_NAME_TIME, c.getLong(timeColIndex));
                m.put(ATTRIBUTE_MESSAGE, c.getString(messageColIndex));
                m.put(ATTRIBUTE_ID, c.getInt(idColIndex));
                m.put(ATTRIBUTE_IS_SEEN, c.getInt(isSeenColIndex));
                m.put(ATTRIBUTE_VOYAGE_ID, c.getLong(voyageIdColIndex));

                data.add(m);
            } while (c.moveToNext());
        }

        c.close();
        db.close();

        Collections.reverse(data);

        driverNotificationCustomAdapter = new DriverNotificationCustomAdapter(this, R.layout.custom_adapter_driver_notification, data, from);

        listView.setAdapter(driverNotificationCustomAdapter);

        login = sharedPreferences.getInt("driverLogin", 0);
        password = sharedPreferences.getString("driverPassword", "");

        url = sharedPreferences.getString("url", "http://track.micro-gis.com/api/");

        RequestDriverEvents requestDriverEvents = new RequestDriverEvents();
        requestDriverEvents.setLogin(login);
        requestDriverEvents.setPassword(password);

        api = APIController.getApi(url);

        api.responseDriverEvents(requestDriverEvents).enqueue(new Callback<ResponseDriverEvents>() {
            @Override
            public void onResponse(Call<ResponseDriverEvents> call, Response<ResponseDriverEvents> response) {
                ResponseDriverEvents responseDriverEvents = response.body();

                if (responseDriverEvents != null){
                    if (responseDriverEvents.getStatus().equals(ResponseStatuses.SUCCESS.toString())){
                        List<Voyage> voyages = responseDriverEvents.getVoyages();
                        for (Voyage voyage: voyages){
                            if (voyage.isActive()){
                                sharedPreferences.edit().putLong("voyageId", voyage.getId()).apply();
                            }
                        }
                    } else if (responseDriverEvents.getStatus().equals(ResponseStatuses.NO_ACTUAL_VOYAGES.toString())){
                        Toast toast = Toast.makeText(DriverNotificationActivity.this,
                                getString(R.string.no_actual_voyages), Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDriverEvents> call, Throwable t) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        String screenActivity = sharedPreferences.getString("screenActivity", "normal");

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(checkForNotification);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(checkForNotification);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(checkCharging);
    }
}
