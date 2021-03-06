package com.micro_gis.microgistracker.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.micro_gis.microgistracker.DBHelper;
import com.micro_gis.microgistracker.activities.DriverActivityTags;
import com.micro_gis.microgistracker.models.rest.RequestDriverEvents;
import com.micro_gis.microgistracker.models.rest.ResponseDriverEvents;
import com.micro_gis.microgistracker.models.rest.ResponseStatuses;
import com.micro_gis.microgistracker.models.rest.Voyage;
import com.micro_gis.microgistracker.retrofit.API;
import com.micro_gis.microgistracker.retrofit.APIController;
import com.micro_gis.microgistracker.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckNotificationService extends Service {

    private SharedPreferences sharedPreferences;

    private String url;
    private Integer driverLogin;
    private String driverPassword;

    private static API api;

    private Handler handler = new Handler();

    private boolean appDestroy = false;

    private DBHelper dbHelper;

    private final static String TAG = "NotificationService";

    private NotificationManager notificationManager;

    Runnable checkNotificationForDriver = new Runnable() {
        @Override
        public void run() {
            RequestDriverEvents requestDriverEvents = new RequestDriverEvents();
            requestDriverEvents.setLogin(driverLogin);
            requestDriverEvents.setPassword(driverPassword);

            api.responseDriverEvents(requestDriverEvents).enqueue(new Callback<ResponseDriverEvents>() {
                @Override
                public void onResponse(Call<ResponseDriverEvents> call, Response<ResponseDriverEvents> response) {
                    ResponseDriverEvents responseDriverEvents = response.body();

                    if (responseDriverEvents != null) {
                        if (responseDriverEvents.getStatus().equals(ResponseStatuses.SUCCESS.toString())){
                            List<Voyage> voyages = responseDriverEvents.getVoyages();

                            ContentValues cv = new ContentValues();
                            SQLiteDatabase db = dbHelper.getWritableDatabase();

                            Date nowDate = new Date();
                            Long now = nowDate.getTime() / 1000;

                            for (Voyage voyage : voyages) {
                                Long voyageId = voyage.getId();
                                String message = null;
                                if (voyage.isActive()) {
                                    Map<Long, String> activeMessages = voyage.getMessageByTime();

                                    for (Long time: activeMessages.keySet()){
                                        if (now >= time){
                                            message = activeMessages.get(time);
                                            addMessageToDB(cv, db, message, time, voyageId);
                                        }
                                    }

                                    if (now >= voyage.getDateStart()){
                                        String startTimeShift = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(voyage.getDateStart() * 1000));
                                        message = getString(R.string.shift) + " " + startTimeShift + "\n" +
                                                getString(R.string.routeDriver) + " " + voyage.getRouteName() + "\n" +
                                                getString(R.string.schedule) + " " + voyage.getScheduleName() + "\n" +
                                                getString(R.string.vehicle) + " " + voyage.getDeviceName() + "\n" +
                                                getString(R.string.begin);
                                        addMessageToDB(cv, db, message, voyage.getDateStart(), voyageId);
                                    }

                                    if (now >= voyage.getDateEnd() - 300){
                                        message = getString(R.string.tillEndShift);
                                        addMessageToDB(cv, db, message, voyage.getDateEnd() - 300, voyageId);
                                    }

                                    if (now >= voyage.getDateEnd() - 45){
                                        message = getString(R.string.endShift);
                                        addMessageToDB(cv, db, message, voyage.getDateEnd(), voyageId);
                                    }

                                } else {
                                    String startTimeShift = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(voyage.getDateStart() * 1000));
                                    if (voyage.getDateStart() < now + 86400){
                                        message = getString(R.string.newShift) + "\n" +
                                                startTimeShift + "\n" +
                                                getString(R.string.routeDriver) + " " + voyage.getRouteName() + "\n" +
                                                getString(R.string.schedule) + " " + voyage.getScheduleName() + "\n" +
                                                getString(R.string.vehicle) + " " + voyage.getDeviceName();
                                        addMessageToDB(cv, db, message, now, voyageId);
                                    }

                                    Map<Long, String> activeMessages = voyage.getMessageByTime();

                                    for (Long time: activeMessages.keySet()){
                                        if (now >= time){
                                            message = activeMessages.get(time);
                                            addMessageToDB(cv, db, message, time, voyageId);
                                        }
                                    }
                                }
                            }

                            Cursor c = db.query("messages", null, null, null, null, null, null);

                            if (c.moveToFirst()) {
                                do {
                                    int timeColIndex = c.getColumnIndex("time");
                                    int idColIndex = c.getColumnIndex("id");

                                    Integer id = c.getInt(idColIndex);
                                    Long time = c.getLong(timeColIndex);

                                    if (time < now - 86400){
                                        db.delete("messages", "id = ?", new String[]{Integer.toString(id)});
                                    }

                                } while (c.moveToNext());
                            }
                            c.close();
                            db.close();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseDriverEvents> call, Throwable t) {

                }
            });

            handler.postDelayed(this, 30000);
        }
    };

    private void showNotification(String message){
        Intent intent = new Intent(this, DriverActivityTags.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.newMassage))
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentText(message);

        Notification notification = builder.build();

        notificationManager.notify(1, notification);
    }

    private void addMessageToDB(ContentValues cv, SQLiteDatabase db, String message, Long time, Long voyageId){
        Cursor c = db.query("messages", null, null, null, null, null, null);

        boolean isInDB = false;

        if (c.moveToFirst()) {
            do {
                int messageColIndex = c.getColumnIndex("message");

                String dbMessage = c.getString(messageColIndex);

                if (dbMessage.equals(message)) {
                    isInDB = true;
                }
            } while (c.moveToNext());
        }

        if (isInDB){
            message = null;
        } else {
            showNotification(message);
        }

        if (message != null){
            cv.put("time", time);
            cv.put("message", message);
            cv.put("isSeen", 0);
            cv.put("voyageId", voyageId);
            db.insert("messages", null, cv);
        }

        c.close();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "Create Service");

        dbHelper = new DBHelper(this);

        sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);

        sharedPreferences.edit().putBoolean("appDestroy", false).apply();
        sharedPreferences.edit().putBoolean("serviceStarted", true).apply();

        url = sharedPreferences.getString("url", "http://track.micro-gis.com/api/");
        driverLogin = sharedPreferences.getInt("driverLogin", 0);
        driverPassword = sharedPreferences.getString("driverPassword", "");

        api = APIController.getApi(url);

        notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        handler.post(checkNotificationForDriver);
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "StartCommand");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        appDestroy = sharedPreferences.getBoolean("appDestroy", false);

        Log.d(TAG, String.valueOf(appDestroy));

        if (appDestroy){
            Intent broadcastIntent = new Intent("RestartNotificationService");
            sendBroadcast(broadcastIntent);
            handler.removeCallbacks(checkNotificationForDriver);
            Log.d(TAG, "Service Destroyed");
        } else {
            sharedPreferences.edit().putBoolean("serviceStarted", false).apply();
            handler.removeCallbacks(checkNotificationForDriver);
            Log.d(TAG, "Service Stopped");
        }
    }

}
