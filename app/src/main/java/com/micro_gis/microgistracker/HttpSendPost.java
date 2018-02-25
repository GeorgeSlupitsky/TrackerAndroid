package com.micro_gis.microgistracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by oleg on 26.05.16.
 */
public class HttpSendPost {
    private AsyncTask<Void, Void, Void> async_cient;
    private String server, port;
    DBHelper dbHelper;
    public static int packetSend;
    public static int errorPacket;
    public static String timeLastSend ="";
    public HttpSendPost(String server, String port, DBHelper dbHelper){
        this.server=server;
        this.port=port;
        this.dbHelper =dbHelper;

    }
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }

    }

    public void send(final String imei, final String gprmc, final String gpgga) {
        async_cient = new AsyncTask<Void, Void, Void>() {
            String rmc=gprmc;
            String gga=gpgga;
            String devimei = imei;
            @Override
            protected void onPreExecute(){
            }

            @Override
            protected Void doInBackground(Void... params) {
                try{
                ContentValues cv = new ContentValues();
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String nmea = "acct=" + devimei + "&" + rmc + gga;

                HttpClient httpClient = new DefaultHttpClient();
                    if (server.startsWith("http://")){
                        server=server.substring(6);
                    }
                HttpPost httpPost = new HttpPost("http://" + server + ":" + port);


                if(isInternetAvailable()) {
                    if (server.startsWith("http://")){
                        server=server.substring(6);
                    }
                        try{
                        Cursor c = db.query("mytable", null, null, null, null, null, null);
                        if (c.moveToFirst()) {
                            int nmeaColIndex = c.getColumnIndex("nmea");
                            while (c.moveToNext()) {
                                // Encoding data
                                try {
                                    httpPost.setEntity(new StringEntity(c.getString(nmeaColIndex), HTTP.UTF_8));
                                    packetSend++;
                                    timeLastSend = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());;
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }

                                // making request
                                try {
                                    HttpResponse response = httpClient.execute(httpPost);
                                    // write response to log
                                    Log.d("Http Post Response:", response.toString());
                                } catch (ClientProtocolException e) {
                                    // Log exception
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    // Log exception
                                    e.printStackTrace();
                                }

                            }
                            int clearCount = db.delete("mytable", null, null);
                            errorPacket=0;
                        } else {
                            c.close();
                          }
                        } catch (Exception e){
                            Log.i("Exception", e.getMessage());
                        }


                    // Encoding data
                    try {
                        httpPost.setEntity(new StringEntity(nmea, HTTP.UTF_8));
                        packetSend++;
                        timeLastSend = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());;
                    } catch (UnsupportedEncodingException e) {

                        e.printStackTrace();
                    }

                    // making request
                    try {
                        HttpResponse response = httpClient.execute(httpPost);
                        // write response to log
                        Log.d("Http Post Response:", response.toString());
                    } catch (ClientProtocolException e) {
                        // Log exception
                        e.printStackTrace();
                    } catch (IOException e) {
                        // Log exception
                        e.printStackTrace();
                    }
                }else{
                    errorPacket++;
                    cv.put("nmea", nmea);
                    db.insert("mytable", null, cv);
                }
                }
                catch (Exception e){
                    Log.i("EEEEEEEEEEEE", e.getMessage());
                }
                finally {
                    dbHelper.close();
                }
                return null;

            }

            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
            }
        };

        if (Build.VERSION.SDK_INT >= 11)
            async_cient.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else async_cient.execute();
    }


}
