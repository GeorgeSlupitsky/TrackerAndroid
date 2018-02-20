package com.micro_gis.microgistracker;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

public class MicroGisActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener {
    HttpSendPost httpPost;
    public static Location mLastLocation;
    //    private GoogleApiClient mGoogleApiClient;
//    private LocationRequest mLocationRequest;
    Double lat, lon;
    String responseJson;
    LocationManager mLocationManager;
    static String gprmc, gpgga, imeis, lastValidGprms, lastValidGpgga, server, port;
    static int time, distance, angle, pointsOnTrack, timerCount;
    String bestProvider, hronTime, timeStart, timeStop;
    ArrayList<AVLData> lisAvldata = new ArrayList<>();
    Handler handler = new Handler();
    DBHelper dbHelper;
    Chronometer mChronometer;
    SharedPreferences sharedpreferences;
    static int addddd;
    static Track track;
    static ArrayList<PressedSensor> sensors;
    static Vibrator vibrator;
    static Double currectLat, currectLon;
    static Location mPreviousLocation;
    static WebView myWebView;
    boolean isStart, isSend, isEnabl =false;
    boolean isAdd;
    TextView signalGps, speedOnTrack, lenghtTrack;
    int lenght, fullLenght;
    ArrayList<Points> pointsList = new ArrayList<>();
    ArrayList<Point> chartPoits = new ArrayList<>();
    ArrayList<Point> altitudeChart = new ArrayList<>();
    ArrayList<Double> points = new ArrayList<>();
    String accaunt, key, interval, url, group, timestamp;
    Button sendToserver;
    long firstStart = 0;
    static HttpAsyncTask httpAsyncTask;
    boolean isRun;
    private int groupId;



    public static String POST(JSONObject jsonObject,String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            if (!url.startsWith("http://")){
                url="http://"+url;
            }
            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d(" ", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            isRun = true;

            try {


                JSONObject form = new JSONObject();
                JSONArray accounts = new JSONArray();

                JSONObject acc = new JSONObject();
                JSONArray grups = new JSONArray();
                grups.put(group);

                acc.put("account", accaunt);
                acc.put("groups",grups);
                accounts.put(acc);
                form.put("key", key);
                form.put("accounts",accounts);

                Log.i("$$$$$$$$$$$$", form.toString());

                return POST(form,urls[0]);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {


            try {
            if(result==null){
                Toast toast = Toast.makeText(getApplicationContext(),
                        getString(R.string.server_monitoring_message_corect), Toast.LENGTH_LONG);
                toast.show();
            }
            JSONObject obj  = new JSONObject(result);
            sharedpreferences.edit().putString("groupObjects", result).apply();
                String status = obj.getString("status");
                if(status.equalsIgnoreCase("WARNING")){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            getString(R.string.server_monitoring_message_corect), Toast.LENGTH_LONG);
                    toast.show();
                }
                JSONArray arr = obj.getJSONArray("devices");
                for (int i = 0; i < arr.length(); i++)
                {
                    String icon = null;
                    try{
                        icon = arr.getJSONObject(i).getString("icon");
                    } catch (Exception e){
                        icon = "car_sedan";
                    }
                    String color = arr.getJSONObject(i).getString("color");
                    String lat = arr.getJSONObject(i).getString("lat");
                    String lng = arr.getJSONObject(i).getString("lng");
                    String description = arr.getJSONObject(i).getString("description");
                    String organization = arr.getJSONObject(i).getString("organization");
                    String speed = arr.getJSONObject(i).getString("speed");
                    String brand = arr.getJSONObject(i).getString("brand");
                    String event = arr.getJSONObject(i).getString("event");
                    String heading = arr.getJSONObject(i).getString("heading");
                    java.util.Date time = new java.util.Date(Long.parseLong(event)*1000);
                    String timel =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
                    String descriptionStr = getString(R.string.descriptionObj);
                    String brandStr = getString(R.string.brand);
                    String companyStr = getString(R.string.company);
                    String lastDataStr = getString(R.string.lastData);
                    String speedStr = getString(R.string.speed);
                    String html = descriptionStr + ": " +description+
                            " <br/>" + brandStr + ": " +brand+
                            " <br/>" + companyStr + ": " +organization+
                            " <br/>" + lastDataStr + ": " +timel+
                            " <br/>" + speedStr + ": "+speed;

                    String[] DIRS = {"north","north-east","east","south-east","south","south-west","west","north-west"};

                    int[] ANCOR_X = {31, 36, 32, 41, 30, 28, 31, 22};
                    int[] ANCOR_Y = {30, 30, 32, 45, 38, 42, 32, 23};

                            int dirNdx = (int) (Math.floor(Integer.parseInt(heading) / 45) % 8);
                            String dirIconName = DIRS[dirNdx];
                            int ancX = ANCOR_X[dirNdx];
                            int ancY = ANCOR_Y[dirNdx];
                    if(Double.parseDouble(speed)>0){
                        myWebView.loadUrl("javascript: "+
                                "var arrow"+i+";"+
                                "var myIcon = new L.icon({\n" +
                                "iconUrl: 'file:///android_asset/images/"+dirIconName+".png',\n" +
                                "iconSize: [44,44],\n" +
                                "shadowUrl: null,\n" +
                                "shadowSize: null,\n" +
                                "iconAnchor: ["+ancX+", "+ancY+"],\n" +
                                "popupAnchor: [0, 0]\n" +
                                "});\n" +
                                "if(typeof(arrow"+i+")==='undefined')\n" +
                                " {\n" +
                                " arrow"+i+" = new L.marker(["+lat+","+lng+"], {icon: myIcon}).addTo(map);\n" +
                                "}else{\n" +
                                "arrow"+i+".setIcon(myIcon);"+
                                "arrow"+i+".setLatLng(["+lat+", "+lng+"]).addTo(map);\n" +
                                "}\n");
                    }else{
                        myWebView.loadUrl("javascript: "+
                                "var arrow"+i+";"+
                                "var myIcon = new L.icon({\n" +
                                "iconUrl: 'file:///android_asset/images/empty.png',\n" +
                                "iconSize: [44,44],\n" +
                                "shadowUrl: null,\n" +
                                "shadowSize: null,\n" +
                                "iconAnchor: ["+ancX+", "+ancY+"],\n" +
                                "popupAnchor: [0, 0]\n" +
                                "});\n"+
                                "if(typeof(arrow"+i+")==='undefined')\n" +
                                " {\n" +
                                " arrow"+i+" = new L.marker(["+lat+","+lng+"], {icon: myIcon}).addTo(map);\n" +
                                "}else{\n" +
                                "arrow"+i+".setIcon(myIcon);"+
                                "arrow"+i+".setLatLng(["+lat+", "+lng+"]).addTo(map);\n" +
                                "}\n");
                    }

                    myWebView.loadUrl("javascript: var bus"+i+ ";" +
                    "var BusIcon = L.Icon.Default.extend({options: {iconUrl: 'file:///android_asset/images/deviceIcons/"+
                           icon+"_"+color+".png',iconSize:     [32, 32],\n" +
                            "    shadowSize:   [0, 0]} });" +
                            "var busIcon = new BusIcon();" +
                            "if(typeof(bus"+i+")==='undefined')\n" +
                            " {\n" +
                            "bus"+i+" = new L.marker([" + lat+","+lng + "], {icon: busIcon}).addTo(map);\n" +
                            " }\n" +
                            " else\n" +
                            " {\n" +
                            "bus"+i+".setIcon(busIcon);" +
                            "  bus"+i+".setLatLng([" + lat+","+lng + "]).addTo(map);\n" +
                            " }\n" +
                            "  bus"+i+".bindPopup(\""+html+"\");\n"
                           );

                }

            } catch (JSONException e) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        getString(R.string.server_monitoring_message_corect), Toast.LENGTH_LONG);
                toast.show();
                e.printStackTrace();
            }
        }
    }

//    Runnable nav = new Runnable() {
//        @Override
//        public void run() {
//            navigation(getLastKnownLocation(), (int)getLastKnownLocation().getBearing());
//            handler.postDelayed(this, 1000L);
//        }
//    };

    Runnable runnable = new Runnable() {
        public void run() {
            try {

                AVLData avlData = parse(gprmc, gpgga);
                if (gpgga ==null& Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(mLastLocation!=null) {
                        avlData = parseAvl(mLastLocation);
                    }
                }
                httpPost = new HttpSendPost(server, port, dbHelper);

                if (avlData.getLongitude() != 0.0) {
                    if (points.size() == 0) {
                        points.add(avlData.getLatitude());
                        points.add(avlData.getLongitude());
                        points.add(0.0);
                    }

                    if (angle != 0 & avlData.getHeading() != points.get(2) & Math.abs((int) avlData.getHeading() - points.get(2).intValue()) >= angle) {
                        points.set(2, (double) avlData.getHeading());
                        try {
                            if(isStart){
                                if(gpgga!=null) {
                                    httpPost.send(imeis, gprmc, gpgga + "$Sensor=0:0,1:0,2:0,3:0,4:0,5:0,6:0");
                                }else {
                                    httpPost.send(imeis, getNMEARMC(mLastLocation), getNMEAGGA(mLastLocation) + "$Sensor=0:0,1:0,2:0,3:0,4:0,5:0,6:0");

                                }}
                        }catch (Exception e){
                            if(isStart) {

                                httpPost.send(imeis, getNMEARMC(mLastLocation), getNMEAGGA(mLastLocation) + "$Sensor=0:0,1:0,2:0,3:0,4:0,5:0,6:0");
                            }
                        }
                    }

                    Location loc1 = new Location("");
                    loc1.setLatitude(points.get(0));
                    loc1.setLongitude(points.get(1));
                    Location loc2 = new Location("");
                    loc2.setLatitude(avlData.getLatitude());
                    loc2.setLongitude(avlData.getLongitude());
                    int distanceInMeters = (int) loc1.distanceTo(loc2);

                    if (distance != 0 & avlData.getLatitude() != points.get(0) & Math.abs(distanceInMeters - distance) >= distance) {
                        points.set(0, avlData.getLatitude());
                        points.set(1, avlData.getLongitude());

                        try {
                            if(isStart){

                                if(gpgga!=null) {
                                    httpPost.send(imeis, gprmc, gpgga + "$Sensor=0:0,1:0,2:0,3:0,4:0,5:0,6:0");
                                }else {
                                    httpPost.send(imeis, getNMEARMC(mLastLocation), getNMEAGGA(mLastLocation) + "$Sensor=0:0,1:0,2:0,3:0,4:0,5:0,6:0");

                                }}
                        }catch (Exception e){
                            if(isStart) {
                                httpPost.send(imeis, getNMEARMC(mLastLocation), getNMEAGGA(mLastLocation) + "$Sensor=0:0,1:0,2:0,3:0,4:0,5:0,6:0");
                            }
                        }
                    }


                }
            } catch (Exception x) {
                Log.i("run Exeption", x.getMessage());
            }

            handler.postDelayed(this, 1000);

        }
    };
    Runnable requst = new Runnable() {
        @Override
        public void run() {
//            {
//                "accounts": [
//
//                {
//                    "account": "7box@ukr.net",
//                        "groups": [
//                    "nm_50_dzh.vashingtona-rjasne-2"
//                    ]
//                }
//                ],
//                "key": "6FD653E1C66232E2C78C983BFA624"
//            }

            try {
                httpAsyncTask = new HttpAsyncTask();
                httpAsyncTask.execute(url);
            }catch (Exception e){
                Toast toast = Toast.makeText(getApplicationContext(),
                        getString(R.string.server_monitoring_message_corect), Toast.LENGTH_LONG);
                toast.show();
            }

            handler.postDelayed(this, 1000*Long.parseLong(interval));

        }
    };

    Runnable changingTime = new Runnable() {
        @Override
        public void run() {

            AVLData avlData = parse(gprmc, gpgga);
            if (gprmc==null & Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                mLastLocation = getLastKnownLocation();
                if(mLastLocation!=null) {
                    avlData = parseAvl(mLastLocation);
                }
            }
            if (avlData.getLongitude() != 0.0) {

                httpPost = new HttpSendPost(server, port, dbHelper);

                try {
//                    if(isStart) {

                        if (gpgga != null) {
                            httpPost.send(imeis, gprmc, gpgga + "$Sensor=0:0,1:0,2:0,3:0,4:0,5:0,6:0");
                        } else {
                            httpPost.send(imeis, getNMEARMC(mLastLocation), getNMEAGGA(mLastLocation) + "$Sensor=0:0,1:0,2:0,3:0,4:0,5:0,6:0");

//                        }
                    }
                }catch (Exception e){
//                    if(isStart) {
                        httpPost.send(imeis, getNMEARMC(mLastLocation), getNMEAGGA(mLastLocation) + "$Sensor=0:0,1:0,2:0,3:0,4:0,5:0,6:0");
//                    }
                }
            }
            handler.postDelayed(this, time * 1000);

        }
    };



    Runnable r = new Runnable() {
        public void run() {

            try {
                timerCount++;
                AVLData avlData = parse(gprmc, gpgga);
                if (gpgga ==null & Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mLastLocation = getLastKnownLocation();
                    if (mLastLocation != null) {
                        avlData = parseAvl(mLastLocation);
                    }
                }

                if (avlData.getLongitude() == 0) {
                    signalGps.setText("  Waiting GPS");
                    signalGps.setTextColor(Color.RED);

                } else {
                    signalGps.setText("  GPS good");
                    signalGps.setTextColor(Color.GREEN);
                }
                int sat = Integer.parseInt(sharedpreferences.getString("minSatelites", "3"));
                int minspeed = Integer.parseInt(sharedpreferences.getString("minSpeed", "3"));
                int maxspeed = Integer.parseInt(sharedpreferences.getString("maxSpeed", "160"));

                if (avlData.getLongitude() != 0.0 & avlData.getSpeed() >= minspeed & avlData.getSpeed() <= maxspeed) {
                    if (points.size() == 0) {
                        points.add(avlData.getLatitude());
                        points.add(avlData.getLongitude());
                        points.add(0.0);

                    }
                    isStart = true;
                    if (time != 0 && timerCount % time == 0 || time != 0 && timerCount - time >= 0) {
                        timerCount = 0;
                        pointsList.add(new Points("" + avlData.getLatitude(), "" + avlData.getLongitude()));
                        pointsOnTrack++;
                        lisAvldata.add(avlData);
                    }
                    if (angle != 0 & avlData.getHeading() != points.get(2) & Math.abs((int) avlData.getHeading() - points.get(2).intValue()) >= angle) {
                        points.set(2, (double) avlData.getHeading());
                        pointsList.add(new Points("" + avlData.getLatitude(), "" + avlData.getLongitude()));
                        pointsOnTrack++;
                        lisAvldata.add(avlData);
                    }

                    Location loc1 = new Location("");
                    loc1.setLatitude(points.get(0));
                    loc1.setLongitude(points.get(1));
                    Location loc2 = new Location("");
                    loc2.setLatitude(avlData.getLatitude());
                    loc2.setLongitude(avlData.getLongitude());
                    float bearing = loc1.bearingTo(loc2);
                    int distanceInMeters = (int) loc1.distanceTo(loc2);

                    if (distance != 0 & avlData.getLatitude() != points.get(0) & Math.abs(distanceInMeters - distance) >= distance) {
                        points.set(0, avlData.getLatitude());
                        points.set(1, avlData.getLongitude());
                        pointsList.add(new Points("" + avlData.getLatitude(), "" + avlData.getLongitude()));
                        pointsOnTrack++;
                        lisAvldata.add(avlData);
                    }

                    if (pointsList.size() > 2) {
                        lenght = 0;
                        for (int i = 0; i < pointsList.size() - 1; i++) {
                            Location location1 = new Location("");
                            location1.setLatitude(Double.parseDouble(pointsList.get(i).lat));
                            location1.setLongitude(Double.parseDouble(pointsList.get(i).lon));
                            Location location2 = new Location("");
                            location2.setLatitude(Double.parseDouble(pointsList.get(i + 1).lat));
                            location2.setLongitude(Double.parseDouble(pointsList.get(i + 1).lon));
                            int meters = (int) location1.distanceTo(location2);
                            lenght += meters;
                        }
                        lenghtTrack.setText(lenght + " m");
                        if (avlData.getSpeed() != 0) {
                            chartPoits.add(new Point(avlData.getSpeed(), lenght));
                        }
                        if (avlData.getAltitude() != 0) {
                            altitudeChart.add(new Point(avlData.getAltitude(), lenght));
                        }
                    }
                    speedOnTrack.setText(avlData.getSpeed() + " km/h");
                    trackInMap(lisAvldata, bearing);
                } else {
                    isStart = false;
                    timerCount = 0;
                }


                currectLat = avlData.getLatitude();
                currectLon = avlData.getLongitude();

            } catch (Exception x) {
                Log.i("run Exeption", x.getMessage());
            }
            fullLenght = lenght;
            handler.postDelayed(this, 1000);
            onResume();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_micro_gis);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (mLocationManager != null){
            if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(myIntent);
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.GONE);
        Button menu = (Button) findViewById(R.id.menuBtn);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        dbHelper = new DBHelper(this);
        signalGps = (TextView) findViewById(R.id.gpsSignal);
        speedOnTrack = (TextView) findViewById(R.id.speedOnTrack);
        lenghtTrack = (TextView) findViewById(R.id.lenghtTrack);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE
                    },
                    1);

        }
        addddd = 0;
//        buildGoogleApiClient();
        sensors = new ArrayList<>();
        sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        server = sharedpreferences.getString("serverKey", "");
        port = sharedpreferences.getString("portKey", "");
        time = Integer.parseInt(sharedpreferences.getString("periodKey", "1"));
        angle = Integer.parseInt(sharedpreferences.getString("angleKey", "0"));
        distance = Integer.parseInt(sharedpreferences.getString("distanceKey", "0"));

        TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (checkIMEIPermission()) {
            try {
                imeis = mngr.getDeviceId();
            } catch (Exception e) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    imeis = mngr.getDeviceId(0);
                }
            }
        }

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        class myWebClient extends WebViewClient {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        }
    /*WebView */
        myWebView = (WebView) findViewById(R.id.webview);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myWebView.setWebViewClient(new myWebClient());
                myWebView.loadUrl("file:///android_asset/index.html");
                WebSettings webSettings = myWebView.getSettings();
                webSettings.setJavaScriptEnabled(true);

            }
        });


        myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");


        final Button clean = (Button) findViewById(R.id.cleanlayers);
        assert clean != null;
        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearMap();
                getMarkers();
                if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    navigation(getLastKnownLocation(), (int) getAngle(mPreviousLocation.getLatitude(), mPreviousLocation.getLongitude(), mLastLocation.getLatitude(), mLastLocation.getLongitude()));
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            getString(R.string.enable_gps), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        sharedpreferences.edit().putString("groupObjects", "Not connected").apply();

        sendToserver = (Button) findViewById(R.id.request_server);

        assert sendToserver != null;
        sendToserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRun) {
                    if (groupId != 999999999){
                        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                            navigation(getLastKnownLocation(), (int) getAngle(mPreviousLocation.getLatitude(), mPreviousLocation.getLongitude(), mLastLocation.getLatitude(), mLastLocation.getLongitude()));
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    getString(R.string.enable_gps), Toast.LENGTH_LONG);
                            toast.show();
                        }

                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        String selection = "id = ?";
                        String[] selectionArgs = new String[] {String.valueOf(groupId)};

                        Cursor c = db.query("requestgroup", null, selection, selectionArgs, null, null, null);

                        if (c.moveToFirst()) {
                            int accountColIndex = c.getColumnIndex("account");
                            int keyColIndex = c.getColumnIndex("keyString");
                            int urlColIndex = c.getColumnIndex("url");
                            int requestIntervalColIndex = c.getColumnIndex("requestInterval");
                            int groupsColIndex = c.getColumnIndex("groups");

                            accaunt = c.getString(accountColIndex);
                            key = c.getString(keyColIndex);
                            interval = c.getString(requestIntervalColIndex);
                            url = c.getString(urlColIndex);
                            group = c.getString(groupsColIndex);

                            handler.post(requst);

                            sendToserver.setBackgroundResource(R.drawable.connect);
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    getString(R.string.server_monitoring_message_corect), Toast.LENGTH_LONG);
                            toast.show();
                        }

                        c.close();
                    } else {
                        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                            navigation(getLastKnownLocation(), (int) getAngle(mPreviousLocation.getLatitude(), mPreviousLocation.getLongitude(), mLastLocation.getLatitude(), mLastLocation.getLongitude()));
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    getString(R.string.enable_gps), Toast.LENGTH_LONG);
                            toast.show();
                        }

                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        Cursor c = db.query("requestgroup", null, null, null, null, null, null);

                        if (c.moveToFirst()){
                            int accountColIndex = c.getColumnIndex("account");
                            int keyColIndex = c.getColumnIndex("keyString");
                            int urlColIndex = c.getColumnIndex("url");
                            int requestIntervalColIndex = c.getColumnIndex("requestInterval");
                            int groupsColIndex = c.getColumnIndex("groups");

                            accaunt = c.getString(accountColIndex);
                            key = c.getString(keyColIndex);
                            interval = c.getString(requestIntervalColIndex);
                            url = c.getString(urlColIndex);
                            group = c.getString(groupsColIndex);

                            handler.post(requst);

                            sendToserver.setBackgroundResource(R.drawable.connect);
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(),
                            getString(R.string.please_add_server_monitoring_settings), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                            sendToserver.setBackgroundResource(R.drawable.disconnect);
                        }
                    }
                } else {
                    isRun = false;
                    handler.removeCallbacks(requst);
                    sharedpreferences.edit().putString("groupObjects", "Not connected").apply();
                    sendToserver.setBackgroundResource(R.drawable.disconnect);
                    clearMap();
                    getMarkers();
                    if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                        navigation(getLastKnownLocation(), (int) getAngle(mPreviousLocation.getLatitude(), mPreviousLocation.getLongitude(), mLastLocation.getLatitude(), mLastLocation.getLongitude()));
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                getString(R.string.enable_gps), Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        });


        final Button start = (Button) findViewById(R.id.start);
        assert start != null;
        start.setBackgroundResource(R.drawable.stop);
        start.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            getString(R.string.enable_gps), Toast.LENGTH_LONG);
                    toast.show();
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    return;
                }
                if (sharedpreferences.getString("serverKey", "").equals("") || sharedpreferences.getString("portKey", "").equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            getString(R.string.please_add_server_settings), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                    return;
                }
                try {
                    if (!isEnabl) {

                        if (time != 0) {
                            handler.post(changingTime);
                        }
                        handler.post(runnable);
                        start.setBackgroundResource(R.drawable.resive);
                        isEnabl = true;


                    } else {
                        handler.removeCallbacks(changingTime);
                        handler.removeCallbacks(runnable);
                        start.setBackgroundResource(R.drawable.stop);
                        isEnabl = false;

                    }

                } catch (Exception e) {

                }

            }
        });


        clean.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    clean.setBackgroundResource(R.drawable.clean);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    clean.setBackgroundResource(R.drawable.clean_a);
                }
                return false;
            }
        });

        final Button fab = (Button) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    Location myLocation = getLastKnownLocation();
                    mLastLocation = getLastKnownLocation();


                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    setLocationOnMap("" + mLastLocation.getLatitude(), "" + mLastLocation.getLongitude());
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            getString(R.string.enable_gps), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        fab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    fab.setBackgroundResource(R.drawable.gps_2x);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    fab.setBackgroundResource(R.drawable.gps_a_2x);
                }
                return false;
            }
        });

        lisAvldata = new ArrayList<AVLData>();
        handler.post(r);
        timeStart = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            myWebView.evaluateJavascript("(function() { return onMapClick(e); })();", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String s) {

                }
            });
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        ((LocationManager) getSystemService(Context.LOCATION_SERVICE)).requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location loc) {

            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLocationManager.addNmeaListener(new GpsStatus.NmeaListener() {
            public void onNmeaReceived(long timestamp, String nmea) {
                if (nmea.startsWith("$GPGGA")) {
                    sendGpgga(nmea);
                }
                if (nmea.startsWith("$GPRMC")) {
                    sendGprmc(nmea);
                }

            }
        });
    }

    void getMarkers(){
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query("markers", null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    byte[] blob = cursor.getBlob(cursor.getColumnIndex("data"));
//                WebView webView = (WebView) findViewById(R.id.webview);
                    String json = new String(blob);
                    Gson gson = new Gson();
                    Marker marker = gson.fromJson(json, new TypeToken<Marker>() {
                    }.getType());
                    myWebView.loadUrl("javascript: \n" +
                            "var RedIcon = L.Icon.Default.extend({\n" +
                            "            options: {\n" +
                            "            \t    iconUrl: '" + marker.getUrl() + "',\n" +
                            "            \t      shadowUrl:'" + marker.getShadowUrl() + "'" +
                            "}" +
                            "         });\n" +
                            "         var redIcon = new RedIcon();\n" +
                            "\n" +
                            "  var popup = L.popup()\n" +
                            "    .setContent('" + marker.getName() + "'); var place = L.marker(" + marker.getLatlng() + ", {icon: redIcon}).bindPopup(popup).addTo(map);");


                } while (cursor.moveToNext());
                db.close();
                addddd++;
            }
//            navigation(getLastKnownLocation(), (int)getLastKnownLocation().getBearing());
        } catch (Throwable e) {
            Log.e("r exeption", e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }


    @Override
    public void onDestroy() {
        addddd = 0;
        mChronometer.stop();
        handler.removeCallbacks(requst);
        if (pointsList.size() >= 2) {
            hronTime = mChronometer.getText().toString();
            timeStop = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            ContentValues cv = new ContentValues();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String name = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
            cv.put("name", name);
            Gson gson = new Gson();
            Track track = new Track();
            track.setTime(hronTime);
            track.setName(name);
            track.setTimeStart(timeStart);
            track.setTimeStop(timeStop);
            track.setPoints(pointsList);
            track.setChartPoits(chartPoits);
            track.setAltitudeChart(altitudeChart);
            ArrayList<Integer> speedList = new ArrayList<>();
            ArrayList<Integer> altitudeList = new ArrayList<>();
            String length;
            int speed = 0, altitude = 0;
            for (AVLData avlData : lisAvldata) {
                speedList.add(avlData.getSpeed());
                altitudeList.add(avlData.getAltitude());
                speed += avlData.getSpeed();
                altitude += avlData.getAltitude();
            }
            Collections.sort(speedList);
            Collections.sort(altitudeList);
            if (altitudeList.size() != 0) {
                track.setMaxAltitude(String.valueOf(altitudeList.get(altitudeList.size() - 1)) + " m");
                track.setAverageSpeed(String.valueOf(speed / speedList.size()) + " km/h");
            }
            if (speedList.size() != 0) {
                track.setMaxSpeed(String.valueOf(speedList.get(speedList.size() - 1)) + " km/h");
                track.setAverageAltitude(String.valueOf(altitude / altitudeList.size()) + " m");
            }

            track.setAvlDataList(lisAvldata);
            track.setSensors(sensors);
            track.setPointsOnTrack(pointsOnTrack);
            track.setTrackLenght(String.valueOf(fullLenght));
            cv.put("track", gson.toJson(track).getBytes());
            db.insert("trackdata", null, cv);
        }
        handler.removeCallbacks(r);
//        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.infoTracking) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.sensors) {
            Intent intent = new Intent(this, ControlActivity.class);
            startActivityForResult(intent, 1);
        } else if (id == R.id.groups) {
            Intent intent = new Intent(this, GroupsActivity.class);
            startActivityForResult(intent, 1);
        } else if (id == R.id.objects) {
            Intent intent = new Intent(this, ObjectsActivity.class);
            startActivityForResult(intent, 1);
        } else if (id == R.id.setting) {
            Intent intent = new Intent(this, TagsSettingActivity.class);
            startActivityForResult(intent, 1);
        } else if (id == R.id.places) {
            Intent intent = new Intent(this, PlaceListActivity.class);
            startActivityForResult(intent, 1);
        } else if (id == R.id.tracks) {
            Intent intent = new Intent(this, TrackListActivity.class);
            startActivityForResult(intent, 1);
        }  else if (id == R.id.exit) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            // Setting Dialog Title
            alertDialog.setTitle(getString(R.string.exit));

            // Setting Dialog Message
            alertDialog.setMessage(getString(R.string.closing_app_msg));

            // On pressing the Yes button.
            alertDialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.cancel(12);
                    MicroGisActivity.this.finish();
                }

            });

            // On pressing the No button
            alertDialog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setLocationOnMap(String lat, String lon) {
        WebView webView = (WebView) findViewById(R.id.webview);
        if (getString(R.string.you_are_here).startsWith("You")) {
            webView.loadUrl("javascript:toYourLoc(" + lat + "," + lon + /*","+getString(R.string.you_are_here).toString()+*/");");
        } else if (getString(R.string.you_are_here).startsWith("Ви")) {
            webView.loadUrl("javascript:toYourLoc1(" + lat + "," + lon + ");");
        } else {
            webView.loadUrl("javascript:toYourLoc2(" + lat + "," + lon + ");");
        }
    }


//    public Location getLocation() {
//        Criteria criteria = new Criteria();
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        bestProvider = locationManager.getBestProvider(criteria, false);
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Log.i("No permission", "Location per");
//            return null;
//        }
//        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
////        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
////                0, mLocationListener);
//        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//        return location;
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Intent intent = data;

                WebView webView = (WebView) findViewById(R.id.webview);
//                webView.loadUrl("javascript:map.removeControl;");
                String weight = sharedpreferences.getString("trackWidth", "3");
                String hexColor = String.format("#%06X", (0xFFFFFF & sharedpreferences.getInt("trackcolor", 0xffff0000)));
                webView.loadUrl("javascript:lineTrack(" + intent.getStringExtra("trackpoits") + "," + intent.getStringExtra("start") + "," + intent.getStringExtra("end") +
                        ");");
                webView.loadUrl("javascript:polyline.setStyle({\n" +
                        "  color: '" + hexColor +/*",'\n" +
                        " weight: "+weight +*/
                        "'\n});");

                for (PressedSensor sensor : sensors) {
                    if (sensor.number == 0) {
                        webView.loadUrl("javascript:sensorInTrack0(" + sensor.points.toString() + ",\"" + sensor.name + "\");");
                    } else if (sensor.number == 1) {
                        webView.loadUrl("javascript:sensorInTrack1(" + sensor.points.toString() + ",\"" + sensor.name + "\");");
                    } else if (sensor.number == 2) {
                        webView.loadUrl("javascript:sensorInTrack2(" + sensor.points.toString() + ",\"" + sensor.name + "\");");
                    } else if (sensor.number == 3) {
                        webView.loadUrl("javascript:sensorInTrack3(" + sensor.points.toString() + ",\"" + sensor.name + "\");");
                    } else if (sensor.number == 4) {
                        webView.loadUrl("javascript:sensorInTrack4(" + sensor.points.toString() + ",\"" + sensor.name + "\");");
                    } else if (sensor.number == 5) {
                        webView.loadUrl("javascript:sensorInTrack5(" + sensor.points.toString() + ",\"" + sensor.name + "\");");
                    } else if (sensor.number == 6) {
                        webView.loadUrl("javascript:sensorInTrackSos(" + sensor.points.toString() + ",\"" + sensor.name + "\");");
                    }
                }
//                sensorInTrack(track);
            }
            if (resultCode == RESULT_CANCELED) {


            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        server = sharedpreferences.getString("serverKey", "");
        port = sharedpreferences.getString("portKey", "");
        time = Integer.parseInt(sharedpreferences.getString("periodKey", "1"));
        angle = Integer.parseInt(sharedpreferences.getString("angleKey", "0"));
        distance = Integer.parseInt(sharedpreferences.getString("distanceKey", "0"));
        int id = sharedpreferences.getInt("groupId", 999999999);

        if (isRun) {
            clearMap();
            getMarkers();

            if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                navigation(getLastKnownLocation(), (int) getAngle(mPreviousLocation.getLatitude(), mPreviousLocation.getLongitude(), mLastLocation.getLatitude(), mLastLocation.getLongitude()));
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        getString(R.string.enable_gps), Toast.LENGTH_LONG);
                toast.show();
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            String selection = "id = ?";
            String[] selectionArgs = new String[]{String.valueOf(id)};

            Cursor c = db.query("requestgroup", null, selection, selectionArgs, null, null, null);

            if (c.moveToFirst()) {
                int accountColIndex = c.getColumnIndex("account");
                int keyColIndex = c.getColumnIndex("keyString");
                int urlColIndex = c.getColumnIndex("url");
                int requestIntervalColIndex = c.getColumnIndex("requestInterval");
                int groupsColIndex = c.getColumnIndex("groups");

                accaunt = c.getString(accountColIndex);
                key = c.getString(keyColIndex);
                interval = c.getString(requestIntervalColIndex);
                url = c.getString(urlColIndex);
                group = c.getString(groupsColIndex);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        getString(R.string.please_add_server_monitoring_settings), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
                sendToserver.setBackgroundResource(R.drawable.disconnect);
            }
        }

        groupId = sharedpreferences.getInt("groupId", 999999999);

        if (isRun){
            handler.post(requst);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        server = sharedpreferences.getString("serverKey", "");
        port = sharedpreferences.getString("portKey", "");
        time = Integer.parseInt(sharedpreferences.getString("periodKey", "1"));
        angle = Integer.parseInt(sharedpreferences.getString("angleKey", "0"));
        distance = Integer.parseInt(sharedpreferences.getString("distanceKey", "0"));

        if (firstStart < 5){
            getMarkers();
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean("firststart", false);
        if (!previouslyStarted) {

            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean("firststart", Boolean.TRUE);
            edit.commit();
            Intent intent = new Intent(this, TagsSettingActivity.class);
            Toast toast = Toast.makeText(getApplicationContext(),
                    " Please, add settings!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
            startActivity(intent);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);

        if (firstStart < 10){
            firstStart++;
        }
    }


    public AVLData parseAvl(Location location) {
        AVLData avlData = new AVLData();
        avlData.setSpeed((int) location.getSpeed());
        avlData.setLatitude(location.getLatitude());
        avlData.setLongitude(location.getLongitude());
        avlData.setHeading(location.getBearing());
        avlData.setAltitude((int) location.getAltitude());
        return avlData;
    }

    public double trackLength(Track track) {
        List<AVLData> avlDataList = track.getAvlDataList();
        ArrayList<Points> points = new ArrayList<>();
        ArrayList<Double> heigth = new ArrayList<>();
        double len = 0.0;
        for (AVLData avldata : avlDataList) {
            points.add(new Points("" + avldata.getLatitude(), "" + avldata.getLongitude()));
            heigth.add(avldata.getAltitude() + 0.0);
        }

        for (int i = 0; i < points.size() - 2; i++) {
            Location location1 = new Location("lo1");
            Location location2 = new Location("lo2");
            location1.setLongitude(Math.floor(Double.parseDouble(points.get(i).lon) * 1000) / 1000);
            location1.setLatitude(Math.floor(Double.parseDouble(points.get(i).lat) * 1000) / 1000);
            location2.setLongitude(Math.floor(Double.parseDouble(points.get(i + 1).lon) * 1000) / 1000);
            location2.setLatitude(Math.floor(Double.parseDouble(points.get(i + 1).lat) * 1000) / 1000);
            double x1 = Double.parseDouble(points.get(i).lat);
            double x2 = Double.parseDouble(points.get(i + 1).lat);
            double y1 = Double.parseDouble(points.get(i).lon);
            double y2 = Double.parseDouble(points.get(i + 1).lon);
            double dist = location1.distanceTo(location2);
//        if(dist>0.5){
            len += dist;
//    }
        }

        return Math.floor(len * 100) / 100;
    }



    public void addTracktoMap(List<AVLData> list) {
        ArrayList<Points> points = new ArrayList<>();
        for (AVLData avldata : list) {
            points.add(new Points("" + avldata.getLatitude(), "" + avldata.getLongitude()));


        }

        WebView webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("javascript:lineTrack(" + points.toString() + "," + points.get(0).toString() + "," + points.get(points.size() - 1) + ");");
    }

    public void trackInMap(List<AVLData> list, float angle) {
        ArrayList<Points> points = new ArrayList<>();
        for (AVLData avldata : list) {
            points.add(new Points("" + avldata.getLatitude(), "" + avldata.getLongitude()));

        }

        WebView webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("javascript:onlineTrack(" + points.toString() + "," + points.get(0).toString() + "," + points.get(points.size() - 1).toString() + ");");
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();
        if (mLastLocation == null){
            mPreviousLocation = location;
        } else {
            mPreviousLocation = mLastLocation;
        }
        mLastLocation = location;
        setLtLn();

        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            navigation(getLastKnownLocation(), (int) getAngle(mPreviousLocation.getLatitude(), mPreviousLocation.getLongitude(), mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    getString(R.string.enable_gps), Toast.LENGTH_LONG);
            toast.show();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    String sendGpgga(String msg) {
        gpgga = msg;
        return gpgga;
    }

    String sendGprmc(String msg) {
        gprmc = msg;
        return gprmc;
    }

    static String getNmea() {
        return gprmc + gpgga;
    }

    private Location getLastKnownLocation() {
        List<String> providers = mLocationManager.getProviders(true);
        Log.i(providers.toString(), "                 ");
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return null;
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }



    //parse data
    public AVLData parse(String gprmc, String gpgga) {

        AVLData avl = new AVLData();

        try {

            //GPGGA,142436.000,4948.793629,N,02403.732753,E,1,6,1.96,351.018,M,36.946,M,,*53
            String[] gpggaArr = gpgga.split(",");

            String[] gprs = gprmc.split(",");
            boolean validGPS = gprs[2].equalsIgnoreCase("A");

            double latitude = validGPS ? _parseLatitude(gprs[3], gprs[4]) : 0.0;
            double longitude = validGPS ? _parseLongitude(gprs[5], gprs[6]) : 0.0;

            double speedKPH = validGPS ? Double.parseDouble(gprs[7]) * 1.852 : 0.0;
            if (!gprs[8].equals("")) {
                double heading = validGPS ? (gprs[8] != null ? Double.parseDouble(gprs[8]) : 0.0) : 0.0;
                avl.setHeading((int) heading);

            }

            if (!gpggaArr[7].equals("")) {
                int satellites = validGPS ? Integer.parseInt(gpggaArr[7]) : 0;
                avl.setSatellites(satellites);
            }

            if (!gpggaArr[8].equals("")) {
                double HDOP = validGPS ? Double.parseDouble(gpggaArr[8]) : 0.0;
                avl.setHdop(HDOP);

            }
            if (!gpggaArr[9].equals("")) {
                double altitude = validGPS ? Double.parseDouble(gpggaArr[9]) : 0.0;
                avl.setAltitude((int) altitude);

            }
            avl.setLatitude(latitude);
            avl.setLongitude(longitude);
            avl.setSpeed((int) speedKPH);
            return avl;

        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
            avl.setHdop(500);
        }
        return avl;
    }


    private double _parseLatitude(String s, String d) {
        Double _lat = Double.parseDouble(s);
        if (_lat < 99999.0) {
            double lat = (double) (_lat.longValue() / 100L); // _lat is always positive here
            lat += (_lat - (lat * 100.0)) / 60.0;
            return d.equals("S") ? -lat : lat;
        } else {
            return 90.0; // invalid latitude
        }
    }


    private double _parseLongitude(String s, String d) {
        Double _lon = Double.parseDouble(s);
        if (_lon < 99999.0) {
            double lon = (double) (_lon.longValue() / 100L); // _lon is always positive here
            lon += (_lon - (lon * 100.0)) / 60.0;
            return d.equals("wa") ? -lon : lon;
        } else {
            return 180.0; // invalid longitude
        }
    }

    public void navigation(Location paramLocation, Integer paramInteger)
    {
        int i = 15 * (paramInteger / 15);
        if ((i > 345) || (i < 15))
            i = 0;
        myWebView.loadUrl("javascript: var navig;var myIconNav = new L.icon({\niconUrl: 'file:///android_asset/nav/" + i + ".png'," +
                "\niconSize: [36,36],\nshadowUrl: null,\nshadowSize: null,\npopupAnchor: [0, 0]\n});\nif(typeof(navig)==='undefined')\n " +
                "{\n navig = new L.marker([" + paramLocation.getLatitude() + ", " + paramLocation.getLongitude() + "], " +
                "{icon: myIconNav}).addTo(map);\n}else{\nnavig.setIcon(myIconNav);navig.setLatLng([" + paramLocation.getLatitude() + ", " + paramLocation.getLongitude() + "]).addTo(map);\n}\n");
    }

    void setLtLn(){
        lat = lat;
        lon = lon;
    }

    private boolean checkIMEIPermission()
    {
        String permission = Manifest.permission.READ_PHONE_STATE;
        int res = getApplicationContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private void clearMap(){
        myWebView.loadUrl("javascript:map.removeLayer(polyline);");
        myWebView.loadUrl("javascript:map.eachLayer(function(layer) {\n" +
                "if (layer instanceof L.Marker) {\n" +
                "map.removeLayer(layer)\n" +
                "}\n" +
                "map.closePopup()\n"+
                "});");
    }

    public static String getNMEAGGA(final Location loc) {
        StringBuilder sbGPGGA = new StringBuilder();

        char cNorthSouth = loc.getLatitude() >= 0 ? 'N' : 'S';
        char cEastWest = loc.getLongitude() >= 0 ? 'E' : 'W';

        Date curDate = new Date();
        sbGPGGA.append("$GPGGA,");
        sbGPGGA.append(HHMMSS.format(curDate));
        sbGPGGA.append(',');
        sbGPGGA.append(getCorrectPosition(loc.getLatitude()));
        sbGPGGA.append(",");
        sbGPGGA.append(cNorthSouth);
        sbGPGGA.append(',');
        sbGPGGA.append(getCorrectPosition(loc.getLongitude()));
        sbGPGGA.append(',');
        sbGPGGA.append(cEastWest);
        sbGPGGA.append(',');
        sbGPGGA.append('1'); // quality
        sbGPGGA.append(',');
        Bundle bundle = loc.getExtras();
        int satellites = 7;
        if( bundle != null )
            satellites = bundle.getInt("satellites", 7);
        sbGPGGA.append(satellites);
        sbGPGGA.append(',');
        sbGPGGA.append(',');
        if (loc.hasAltitude())
            sbGPGGA.append(shortFormat.format(loc.getAltitude()));
        sbGPGGA.append(',');
        sbGPGGA.append('M');
        sbGPGGA.append(',');
        sbGPGGA.append(',');
        sbGPGGA.append('M');
        sbGPGGA.append(',');
        sbGPGGA.append("*");
        int checksum = getNMEAChecksum(sbGPGGA);
        sbGPGGA.append(java.lang.Integer.toHexString(checksum));
        sbGPGGA.append("\r\n");

        return sbGPGGA.toString();
    }

    public static double getAngle(double lat1, double lon1, double lat2, double lon2)
    {
        //Formulas

        //θ =   atan2(  sin(Δlong).cos(lat2),cos(lat1).sin(lat2) − sin(lat1).cos(lat2).cos(Δlong) )
        // Δlong = long2 - long1
        Log.i("angle", "Inside getAngle");
        double latitude1 = Math.toRadians(lat1);
        double longitude1 = Math.toRadians(lon1);
        double latitude2 = Math.toRadians(lat2);
        double longitude2 = Math.toRadians(lon2);


        double dlong = Math.toRadians(longitude2-longitude1);

        double y = Math.sin(dlong) * Math.cos(latitude2);
        double x = Math.cos(latitude1)*Math.sin(latitude2) - Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(dlong);
        double angle= Math.atan2(y, x);


        if (angle < 0)
            angle = Math.abs(angle);
        else
            angle = 2*Math.PI - angle;

        Log.i("angle", String.valueOf(angle)+" in radians");

        angle=Math.toDegrees(angle);
        Log.i("angle", String.valueOf(angle)+" in degrees");

        return angle;
    }

    public static String getCorrectPosition(double degree) {
        double val = degree - (int) degree;
        val *= 60;

        val = (int) degree * 100 + val;
        return locFormat.format(Math.abs(val));
    }


    public static String getNMEARMC(final Location loc) {
        // $GPRMC,053117.000,V,4812.7084,N,01619.3522,E,0.14,237.29,070311,,,N*76
        StringBuilder sbGPRMC = new StringBuilder();

        char cNorthSouth = loc.getLatitude() >= 0 ? 'N' : 'S';
        char cEastWest = loc.getLongitude() >= 0 ? 'E' : 'W';

        Date curDate = new Date();
        sbGPRMC.append("$GPRMC,");
        sbGPRMC.append(HHMMSS.format(curDate));
        sbGPRMC.append(",A,");
        sbGPRMC.append(getCorrectPosition(loc.getLatitude()));
        sbGPRMC.append(",");
        sbGPRMC.append(cNorthSouth);
        sbGPRMC.append(",");
        sbGPRMC.append(getCorrectPosition(loc.getLongitude()));
        sbGPRMC.append(',');
        sbGPRMC.append(cEastWest);
        sbGPRMC.append(',');
        sbGPRMC.append(shortFormat.format(loc.getSpeed() * 1.94));
        sbGPRMC.append(",");
        sbGPRMC.append(shortFormat.format(loc.getBearing()));
        sbGPRMC.append(",");
        sbGPRMC.append(DDMMYY.format(curDate));
        sbGPRMC.append(",,,");
        sbGPRMC.append("A");
        sbGPRMC.append("*");
        int checksum = getNMEAChecksum(sbGPRMC);
        sbGPRMC.append(java.lang.Integer.toHexString(checksum));
        // if(D) Log.v(TAG, sbGPRMC.toString());
        sbGPRMC.append("\r\n");

        return sbGPRMC.toString();
    }
    public static int getNMEAChecksum(final StringBuilder sbString) {
        int checksum = 0;

        for (int i = 0; i < sbString.length(); i++) {
            if (sbString.charAt(i) != '*' && sbString.charAt(i) != '$')
                checksum ^= sbString.charAt(i);
        }
        return checksum;
    }

    public static DecimalFormatSymbols decSymFormat = new DecimalFormatSymbols();
    static {
        decSymFormat.setDecimalSeparator('.');
    }
    public static DecimalFormat locFormat = new DecimalFormat("0000.######", decSymFormat);
    public static DecimalFormat shortFormat = new DecimalFormat("##.#", decSymFormat);

    public static SimpleDateFormat HHMMSS = new SimpleDateFormat("HHmmss.000",
            Locale.UK);

    public static SimpleDateFormat DDMMYY = new SimpleDateFormat("ddMMyy",
            Locale.UK);
    static {
        HHMMSS.setTimeZone(TimeZone.getTimeZone("GMT"));
        DDMMYY.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

}
