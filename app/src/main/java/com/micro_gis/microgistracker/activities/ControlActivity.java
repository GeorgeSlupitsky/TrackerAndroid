package com.micro_gis.microgistracker.activities;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.micro_gis.microgistracker.DBHelper;
import com.micro_gis.microgistracker.HttpSendPost;
import com.micro_gis.microgistracker.models.database.Points;
import com.micro_gis.microgistracker.models.database.PressedSensor;
import com.micro_gis.microgistracker.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ControlActivity extends AppCompatActivity implements View.OnLongClickListener, LocationListener {

    String server, port, imei;
    HttpSendPost httpSendPost;
    DBHelper dbHelper;
    Toast toast;
    String sensor_0 = "$Sensor=0:1,1:0,2:0,3:0,4:0,5:0,6:0";
    String sensor_1 = "$Sensor=0:0,1:1,2:0,3:0,4:0,5:0,6:0";
    String sensor_2 = "$Sensor=0:0,1:0,2:1,3:0,4:0,5:0,6:0";
    String sensor_3 = "$Sensor=0:0,1:0,2:0,3:1,4:0,5:0,6:0";
    String sensor_4 = "$Sensor=0:0,1:0,2:0,3:0,4:1,5:0,6:0";
    String sensor_5 = "$Sensor=0:0,1:0,2:0,3:0,4:0,5:1,6:0";
    String sensor_sos = "$Sensor=0:0,1:0,2:0,3:0,4:0,5:0,6:1";
    Location location;
    Button button0, button1, button2, button3, button4, button5;
    LocationManager locationManager;
    boolean isSwitchCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        final Button back = (Button) findViewById(R.id.back_buttonSensors);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button0 = (Button) findViewById(R.id.button0);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        final Button sos = (Button) findViewById(R.id.sos);
//        Button sensorSetting = (Button)findViewById(R.id.sensorSetting);

        button0.setOnLongClickListener(this);
        button2.setOnLongClickListener(this);
        button3.setOnLongClickListener(this);
        button1.setOnLongClickListener(this);
        button4.setOnLongClickListener(this);
        button5.setOnLongClickListener(this);
//        sensorSetting.setOnClickListener(this);

        SharedPreferences sharedpreferences = getSharedPreferences("sensorpref", Context.MODE_PRIVATE);
        button0.setText(sharedpreferences.getString(SettingSensorsActivity.S0, "").equals("") ? "SENSOR 0" : sharedpreferences.getString(SettingSensorsActivity.S0, ""));
        button1.setText(sharedpreferences.getString(SettingSensorsActivity.S1, "").equals("") ? "SENSOR 1" : sharedpreferences.getString(SettingSensorsActivity.S1, ""));
        button2.setText(sharedpreferences.getString(SettingSensorsActivity.S2, "").equals("") ? "SENSOR 2" : sharedpreferences.getString(SettingSensorsActivity.S2, ""));
        button3.setText(sharedpreferences.getString(SettingSensorsActivity.S3, "").equals("") ? "SENSOR 3" : sharedpreferences.getString(SettingSensorsActivity.S3, ""));
        button4.setText(sharedpreferences.getString(SettingSensorsActivity.S4, "").equals("") ? "SENSOR 4" : sharedpreferences.getString(SettingSensorsActivity.S4, ""));
        button5.setText(sharedpreferences.getString(SettingSensorsActivity.S5, "").equals("") ? "SENSOR 5" : sharedpreferences.getString(SettingSensorsActivity.S5, ""));
        sos.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                try{
                if(isSwitchCheck){
                if (MicroGisActivity.getNmea()!=null && MicroGisActivity.getNmea().split(",").length>=2 && MicroGisActivity.getNmea().split(",")[2].equalsIgnoreCase("A")) {
                    httpSendPost.send(imei, "", MicroGisActivity.getNmea() + "$Sensor=0:0,1:0,2:0,3:0,4:0,5:0,6:1");
                } else {
                    sendLoc(sensor_sos);

                }}

                if(MicroGisActivity.currectLon!=0.0){
                    MicroGisActivity.sensors.add(new PressedSensor(sos.getText().toString(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), new Points("" + MicroGisActivity.currectLat, "" + MicroGisActivity.currectLon),6));

                }else {
                    MicroGisActivity.sensors.add(new PressedSensor(sos.getText().toString(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), new Points("" + location.getLatitude(), "" + location.getLongitude()),6));
                }
                }catch (Exception e){
                    Log.i("*************", ""+MicroGisActivity.mLastLocation.getLatitude());
                    MicroGisActivity.sensors.add(new PressedSensor(sos.getText().toString(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), new Points("" + MicroGisActivity.mLastLocation.getLongitude(), "" + MicroGisActivity.mLastLocation.getLongitude()),6));
                }
                MicroGisActivity.vibrator.vibrate(300);
                return true;
            }
        });

        imei = MicroGisActivity.imeis;
        SharedPreferences sharedpref = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        server = sharedpref.getString("serverKey", "");
        port = sharedpref.getString("portKey", "");
        isSwitchCheck = Boolean.parseBoolean(sharedpref.getString("switchKey", "false"));


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        dbHelper = new DBHelper(this);
        httpSendPost = new HttpSendPost(server, port, dbHelper);

        toast = Toast.makeText(getApplicationContext(),
                "No correct GPS data!", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);

    }


    @Override
    public boolean onLongClick(View v) {

        switch (v.getId()) {

            case R.id.button0:
                if(isSwitchCheck){
                if (MicroGisActivity.getNmea()!=null &&MicroGisActivity.getNmea().split(",").length>=2&& MicroGisActivity.getNmea().split(",")[2].equalsIgnoreCase("A")) {
                    httpSendPost.send(imei, MicroGisActivity.gprmc, MicroGisActivity.gpgga + "$Sensor=0:1,1:0,2:0,3:0,4:0,5:0,6:0");
                } else {
                    sendLoc(sensor_0);

                }}
                if(MicroGisActivity.currectLon!=0.0){
                    MicroGisActivity.sensors.add(new PressedSensor(button0.getText().toString(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), new Points("" + MicroGisActivity.currectLat, "" + MicroGisActivity.currectLon),0));

                }else {
                    MicroGisActivity.sensors.add(new PressedSensor(button0.getText().toString(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), new Points("" + location.getLatitude(), "" + location.getLongitude()),0));
                }
                MicroGisActivity.vibrator.vibrate(1000);
                break;

            case R.id.button1:

                if(isSwitchCheck){
                    if (MicroGisActivity.getNmea()!=null && MicroGisActivity.getNmea().split(",").length>=2 && MicroGisActivity.getNmea().split(",")[2].equalsIgnoreCase("A")) {
                    httpSendPost.send(imei, "", MicroGisActivity.getNmea() + "$Sensor=0:0,1:1,2:0,3:0,4:0,5:0,6:0");
                } else {
                    sendLoc(sensor_1);

                }}
                if(MicroGisActivity.currectLon!=0.0){
                    MicroGisActivity.sensors.add(new PressedSensor(button1.getText().toString(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), new Points("" + MicroGisActivity.currectLat, "" + MicroGisActivity.currectLon),1));

                }else {
                    MicroGisActivity.sensors.add(new PressedSensor(button1.getText().toString(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), new Points("" + location.getLatitude(), "" + location.getLongitude()),1));
                }                MicroGisActivity.vibrator.vibrate(300);

                break;

            case R.id.button2:
                if(isSwitchCheck){
                if (MicroGisActivity.getNmea()!=null &&MicroGisActivity.getNmea().split(",").length>=2&&MicroGisActivity.getNmea().split(",")[2].equalsIgnoreCase("A")) {
                    httpSendPost.send(imei, "", MicroGisActivity.getNmea() + "$Sensor=0:0,1:0,2:1,3:0,4:0,5:0,6:0");
                } else {
                    sendLoc(sensor_2);

                }}
                if(MicroGisActivity.currectLon!=0.0){
                    MicroGisActivity.sensors.add(new PressedSensor(button2.getText().toString(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), new Points("" + MicroGisActivity.currectLat, "" + MicroGisActivity.currectLon),2));

                }else {
                    MicroGisActivity.sensors.add(new PressedSensor(button2.getText().toString(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), new Points("" + location.getLatitude(), "" + location.getLongitude()),2));
                }
                MicroGisActivity.vibrator.vibrate(300);

                break;

            case R.id.button3:
                if(isSwitchCheck){
                if (MicroGisActivity.getNmea()!=null && MicroGisActivity.getNmea().split(",").length>=2 && MicroGisActivity.getNmea().split(",")[2].equalsIgnoreCase("A")) {
                    httpSendPost.send(imei, "", MicroGisActivity.getNmea() + "$Sensor=0:0,1:0,2:0,3:1,4:0,5:0,6:0");
                } else {
                    sendLoc(sensor_3);

                }}
                if(MicroGisActivity.currectLon!=0.0){
                    MicroGisActivity.sensors.add(new PressedSensor(button3.getText().toString(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), new Points("" + MicroGisActivity.currectLat, "" + MicroGisActivity.currectLon),3));

                }else {
                    MicroGisActivity.sensors.add(new PressedSensor(button3.getText().toString(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), new Points("" + location.getLatitude(), "" + location.getLongitude()),3));
                }
                MicroGisActivity.vibrator.vibrate(300);

                break;

            case R.id.button4:
                if(isSwitchCheck){
                if (MicroGisActivity.getNmea()!=null && MicroGisActivity.getNmea().split(",").length>=2 && MicroGisActivity.getNmea().split(",")[2].equalsIgnoreCase("A")) {
                    httpSendPost.send(imei, "", MicroGisActivity.getNmea() + "$Sensor=0:0,1:0,2:0,3:0,4:1,5:0,6:0");
                } else {
                    sendLoc(sensor_4);

                }}
                if(MicroGisActivity.currectLon!=0.0){
                    MicroGisActivity.sensors.add(new PressedSensor(button4.getText().toString(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), new Points("" + MicroGisActivity.currectLat, "" + MicroGisActivity.currectLon),4));

                }else {
                    MicroGisActivity.sensors.add(new PressedSensor(button4.getText().toString(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), new Points("" + location.getLatitude(), "" + location.getLongitude()),4));
                }
                MicroGisActivity.vibrator.vibrate(300);

                break;

            case R.id.button5:
                if(isSwitchCheck){
                if (MicroGisActivity.getNmea()!=null && MicroGisActivity.getNmea().split(",").length>=2 && MicroGisActivity.getNmea().split(",")[2].equalsIgnoreCase("A")) {
                    httpSendPost.send(imei, "", MicroGisActivity.getNmea() + "$Sensor=0:0,1:0,2:0,3:0,4:0,5:1,6:0");
                } else {
                    sendLoc(sensor_5);

                }}
                if(MicroGisActivity.currectLon!=0.0){
                    MicroGisActivity.sensors.add(new PressedSensor(button5.getText().toString(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), new Points("" + MicroGisActivity.currectLat, "" + MicroGisActivity.currectLon),5));

                }else {
                    MicroGisActivity.sensors.add(new PressedSensor(button5.getText().toString(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), new Points("" + location.getLatitude(), "" + location.getLongitude()),5));
                }                MicroGisActivity.vibrator.vibrate(300);

                break;

//            case R.id.info:
//                onBackPressed();
//                break;

//            case R.id.sensorSetting:
//                Intent intent = new Intent(this, SettingSensorsActivity.class);
//                startActivity(intent);

            default:
                break;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button0 = (Button) findViewById(R.id.button0);
        Button button4 = (Button) findViewById(R.id.button4);
        Button button5 = (Button) findViewById(R.id.button5);

        SharedPreferences sharedpreferences = getSharedPreferences("sensorpref", Context.MODE_PRIVATE);
        button0.setText(sharedpreferences.getString(SettingSensorsActivity.S0, "").equals("") ? "SENSOR 0" : sharedpreferences.getString(SettingSensorsActivity.S0, ""));
        button1.setText(sharedpreferences.getString(SettingSensorsActivity.S1, "").equals("") ? "SENSOR 1" : sharedpreferences.getString(SettingSensorsActivity.S1, ""));
        button2.setText(sharedpreferences.getString(SettingSensorsActivity.S2, "").equals("") ? "SENSOR 2" : sharedpreferences.getString(SettingSensorsActivity.S2, ""));
        button3.setText(sharedpreferences.getString(SettingSensorsActivity.S3, "").equals("") ? "SENSOR 3" : sharedpreferences.getString(SettingSensorsActivity.S3, ""));
        button4.setText(sharedpreferences.getString(SettingSensorsActivity.S4, "").equals("") ? "SENSOR 4" : sharedpreferences.getString(SettingSensorsActivity.S4, ""));
        button5.setText(sharedpreferences.getString(SettingSensorsActivity.S5, "").equals("") ? "SENSOR 5" : sharedpreferences.getString(SettingSensorsActivity.S5, ""));

    }

    @Override
    public void onLocationChanged(Location location) {

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

    //  acct=355056054762664&$GPRMC,144255.00,A,4948.787275,N,02403.742451,E,000.0,,020616,,,A*7D
// $GPGGA,144255.00,4948.787275,N,02403.742451,E,1,06,2.4,370.1,M,36.3,M,,*61$Sensor=0:0,1:0,2:0,3:1,4:0,5:0,6:0
    public void sendLoc(String sensor) {

        //$GPRMC,090410.23,V,,,,,,,130616,,,N*73$GPGGA,090410.23,,,,,0,00,999.9,,M,,M,,*6B
        //net,lat,lon,speed,fixtime,altitude,heading
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = getLastKnownLocation();

        String data="$net,"+location.getLatitude()+","+location.getLongitude()+","+(int)location.getSpeed()+","+(long)System.currentTimeMillis()/1000+","+(int)location.getAltitude()+","+(int)location.getBearing();
        httpSendPost.send(imei, data,"" + sensor);
    }
    private Location getLastKnownLocation() {
        LocationManager  mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
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
}