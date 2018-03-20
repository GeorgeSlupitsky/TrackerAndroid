package com.micro_gis.microgistracker.activities;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.micro_gis.microgistracker.models.database.AVLData;
import com.micro_gis.microgistracker.DBHelper;
import com.micro_gis.microgistracker.HttpSendPost;
import com.micro_gis.microgistracker.R;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private TextView imei, latitude, longitude, satellites, speed, altitude, HDOP, direction, statusSignal, packets, errorPacket, timeSending;
    private boolean isStart;
    Handler handler = new Handler();
    static int time, distance, angel;
    static String gprmc, gpgga, imeis, lastValidGprms, lastValidGpgga, server, port;
    ArrayList<Double> points;
    LocationManager LM;
    HttpSendPost httpPost;
    DBHelper dbHelper;
    static Vibrator vibrator;
    SharedPreferences sharedpreferences;
    LocationManager mLocationManager;
    public static Location mLastLocation;



    Runnable changingTime = new Runnable() {
        @Override
        public void run() {

            AVLData avlData = parse(gprmc, gpgga);
            if (gprmc==null & Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                mLastLocation =getLastKnownLocation();
                if(mLastLocation!=null) {
                    avlData = parseAvl(mLastLocation);
                }
          }
            if (avlData.getLongitude() != 0.0) {


                updateUI(avlData);
            }
            handler.postDelayed(this, 2000);

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button back = (Button) findViewById(R.id.back_buttonINFO);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        points = new ArrayList<>();
        dbHelper = new DBHelper(this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //Buttons

//        buildGoogleApiClient();
        //TextViev
        latitude = (TextView) findViewById(R.id.latitude);
        longitude = (TextView) findViewById(R.id.longitude);
        speed = (TextView) findViewById(R.id.speed);
        satellites = (TextView) findViewById(R.id.satellites);
        altitude = (TextView) findViewById(R.id.altitude);
        direction = (TextView) findViewById(R.id.direction);
        HDOP = (TextView) findViewById(R.id.textView2);
        imei = (TextView) findViewById(R.id.imei);
        statusSignal = (TextView) findViewById(R.id.signalStatus);
        packets = (TextView) findViewById(R.id.sendedPacket);
        errorPacket = (TextView) findViewById(R.id.badSanded);
        timeSending = (TextView) findViewById(R.id.timeLastSend);
//        gprmcT=(TextView)findViewById(R.id.gprmc);

        //get imei
        imeis = MicroGisActivity.imeis;
        if (imeis != null){
            imei.append(imeis);
        }

        //NMEA listener
        LM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        ((LocationManager) getSystemService(Context.LOCATION_SERVICE)).requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
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

        LM.addNmeaListener(new GpsStatus.NmeaListener() {
            public void onNmeaReceived(long timestamp, String nmea) {
                if (nmea.startsWith("$GPGGA")) {
                    sendGpgga(nmea);
                }
                if (nmea.startsWith("$GPRMC")) {
                    sendGprmc(nmea);
                    if (!nmea.split(",")[2].equalsIgnoreCase("V")) {
                        lastValidGprms = nmea;
                        lastValidGpgga = gpgga;
                    }
                }

            }
        });


        sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        server = sharedpreferences.getString("serverKey", "");
        port = sharedpreferences.getString("portKey", "");
        time = Integer.parseInt(sharedpreferences.getString("periodKey", "0"));
        angel = Integer.parseInt(sharedpreferences.getString("angleKey", "0"));
        distance = Integer.parseInt(sharedpreferences.getString("distanceKey", "0"));


        try {
            AVLData avlData = parse(gprmc, gpgga);
            updateUI(avlData);
            handler.post(changingTime);

        } catch (Exception e) {

        }

    }


    String sendGpgga(String msg) {
        gpgga = msg;
        return gpgga;
    }

    String sendGprmc(String msg) {
        gprmc = msg;
        return gprmc;
    }
    public AVLData parseAvl(Location location) {
        AVLData avlData = new AVLData();
        avlData.setSpeed((int) location.getSpeed());
        avlData.setLatitude(location.getLatitude());
        avlData.setLongitude(location.getLongitude());
        avlData.setHeading(location.getBearing());
        avlData.setAltitude((int) location.getAltitude());
        avlData.setHdop(0);
        return avlData;
    }
    static String getNmea() {
        return gprmc + gpgga;
    }


    @Override
    public void onStart() {

        try {
            AVLData avlData = parse(gprmc, gpgga);
            updateUI(avlData);


                handler.post(changingTime);


        }catch (Exception e){

        }


//        mGoogleApiClient.connect();
        super.onStart();

    }
    @Override
    public void onResume() {
        super.onResume();

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, MicroGisActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification n = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            n = new Notification.Builder(this)
                    .setContentTitle("MicroGIS Tracker")
                    .setContentText(isStart ? "Receiving..." : "Waiting GPS")
                    .setSmallIcon(isStart ? R.mipmap.ic_launcher : R.drawable.ic_launcher_black)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true).build();
        }

        n.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(12, n);


    }

    @Override
    public void onBackPressed() {
        if (isStart) {
            moveTaskToBack(true);

        } else {
            moveTaskToBack(false);
            super.onBackPressed();
        }
    }

    private void updateUI(AVLData avl) {
//        AVLData avl =parse(gprmc,gpgga);
//        gprmcT.setText(gprmc);
        latitude.setText("Latitude: " + avl.getLatitude());
        longitude.setText("Longitude: " + avl.getLongitude());
        speed.setText("Speed: " + avl.getSpeed() + " Km/h");
        satellites.setText("Satellites: " + avl.getSatellites());
        HDOP.setText("HDOP: " + avl.getHdop());
        direction.setText("Direction: " + (int) MicroGisActivity.getAngle(MicroGisActivity.mPreviousLocation.getLatitude(),
                MicroGisActivity.mPreviousLocation.getLongitude(), MicroGisActivity.mLastLocation.getLatitude(), MicroGisActivity.mLastLocation.getLongitude()) + "°");
        altitude.setText("Altitude: " + avl.getAltitude());
        if (avl.getLongitude() == 0) {
            statusSignal.setText("No GPS signal!");
            statusSignal.setTextColor(Color.RED);
        } else {
            statusSignal.setText("GPS signal is good!");
            statusSignal.setTextColor(Color.GREEN);
        }
        packets.setText("Packets: " + HttpSendPost.packetSend);
        errorPacket.setText("Bad packets: " + HttpSendPost.errorPacket);
        timeSending.setText("Time last send: " + HttpSendPost.timeLastSend);
    }
    @Override
    public  void  onDestroy(){
//        mGoogleApiClient.disconnect();


            handler.removeCallbacks(changingTime);

        super.onDestroy();
    }

    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Log.i(providers.toString(), "                 ");
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

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
                if(gprmc==null){
                    avl = parseAvl(getLastKnownLocation());
                    return avl;
                }
            //GPGGA=142436.000,4948.793629,N,02403.732753,E,1,6,1.96,351.018,M,36.946,M,,*53
            String[] gpggaArr = gpgga.split(",");

            String[] gprs = gprmc.split(",");
            boolean validGPS = gprs[2].equalsIgnoreCase("A");

            double latitude = BigDecimal.valueOf(validGPS ? _parseLatitude(gprs[3], gprs[4]) : 0.0).setScale(7, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            double longitude = BigDecimal.valueOf(validGPS ? _parseLongitude(gprs[5], gprs[6]) : 0.0).setScale(7, BigDecimal.ROUND_HALF_DOWN).doubleValue();

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
            return d.equals("s") ? -lat : lat;
        } else {
            return 90.0; // invalid latitude
        }
    }


    private double _parseLongitude(String s, String d) {
        Double _lon = Double.parseDouble(s);
        if (_lon < 99999.0) {
            double lon = (double) (_lon.longValue() / 100L); // _lon is always positive here
            lon += (_lon - (lon * 100.0)) / 60.0;
            return d.equals("W") ? -lon : lon;
        } else {
            return 180.0; // invalid longitude
        }
    }

    @Override
    public void onLocationChanged(Location location) {
            mLastLocation = location;
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

    void setLoc(){
        mLastLocation =mLastLocation;
    }

    public static String getNMEAGGA(final Location loc) {
        StringBuilder sbGPGGA = new StringBuilder();

        char cNorthSouth = loc.getLatitude() >= 0 ? 'N' : 's';
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


    public static String getCorrectPosition(double degree) {
        double val = degree - (int) degree;
        val *= 60;

        val = (int) degree * 100 + val;
        return locFormat.format(Math.abs(val));
    }


    public static String getNMEARMC(final Location loc) {
        // $GPRMC,053117.000,V,4812.7084,N,01619.3522,E,0.14,237.29,070311,,,N*76
        StringBuilder sbGPRMC = new StringBuilder();

        char cNorthSouth = loc.getLatitude() >= 0 ? 'N' : 's';
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
