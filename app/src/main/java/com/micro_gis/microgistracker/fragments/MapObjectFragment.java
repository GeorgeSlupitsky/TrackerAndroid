package com.micro_gis.microgistracker.fragments;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.micro_gis.microgistracker.R;
import com.micro_gis.microgistracker.WebAppInterface;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.google.android.gms.internal.zzip.runOnUiThread;

/**
 * Created by User9 on 02.03.2018.
 */

public class MapObjectFragment extends Fragment {

    WebView webView;
    TextView driverName;
    TextView trailerName;
    TextView objectDate;
    TextView geozone;
    ImageView imageViewObject;
    ImageView imageViewStatus;
    ImageView imageViewDriver;
    ImageView imageViewTrailer;
    ImageView imageViewWiFi;
    ImageView imageViewLowFlor;

    String description;
    String driver;
    String trailer;
    String address;
    String organization;
    String plate;
    Double lat;
    Double lng;
    Double speed;
    Long event;
    Integer heading;
    String brand;
    String color;
    String icon;
    String id;
    Integer statusCode;
    Boolean wifi;
    Boolean isLowFlor;
    Integer altitude;
    Integer satCount;
    Double hdop;
    Double fuelExpense;
    Double fuelLevel;
    String account;
    String key;
    String url;
    Boolean geocoder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_map_object, container, false);


        webView = (WebView) rootView.findViewById(R.id.webviewMapOdject);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl("file:///android_asset/indexObjectMap.html");
                WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true);

            }
        });

        webView.addJavascriptInterface(new WebAppInterface(rootView.getContext()), "Android");

        driverName = (TextView) rootView.findViewById(R.id.driverNameF);
        trailerName = (TextView) rootView.findViewById(R.id.trailerNameF);
        objectDate = (TextView) rootView.findViewById(R.id.objectDateF);
        geozone = (TextView) rootView.findViewById(R.id.addressF);

        imageViewObject = (ImageView) rootView.findViewById(R.id.imageViewObjectF);
        imageViewStatus = (ImageView) rootView.findViewById(R.id.imageViewStatusF);
        imageViewDriver = (ImageView) rootView.findViewById(R.id.imageViewDriverF);
        imageViewTrailer = (ImageView) rootView.findViewById(R.id.imageViewTrailerF);
        imageViewWiFi = (ImageView) rootView.findViewById(R.id.imageViewWiFiF);
        imageViewLowFlor = (ImageView) rootView.findViewById(R.id.imageViewLowFlorF);

        description = getArguments().getString("description");
        driver = getArguments().getString("driverName");

        if (driver == null){
            driver = "empty";
        }

        trailer = getArguments().getString("trailer");

        if (trailer == null){
            trailer = "empty";
        }

        address = getArguments().getString("address");

        if (address == null){
            address = "---";
        }

        organization = getArguments().getString("organization");
        plate = getArguments().getString("plate");
        lat = getArguments().getDouble("lat");
        lng = getArguments().getDouble("lng");
        speed = getArguments().getDouble("speed");
        event = getArguments().getLong("event");
        heading = getArguments().getInt("heading");
        brand = getArguments().getString("brand");
        color = getArguments().getString("color");
        icon = getArguments().getString("icon");

        if (icon == null){
            icon = "car_sedan";
        }

        String image = icon + "_" + color;

        id = getArguments().getString("id");
        statusCode = getArguments().getInt("statusCode");
        wifi = getArguments().getBoolean("wifi");
        isLowFlor = getArguments().getBoolean("lowFlor");
        altitude = getArguments().getInt("altitude");
        satCount = getArguments().getInt("satCount");
        hdop = getArguments().getDouble("hdop");
        fuelExpense = getArguments().getDouble("fuelExpense");
        fuelLevel = getArguments().getDouble("fuelLevel");
        account = getArguments().getString("account");
        key = getArguments().getString("key");
        url = getArguments().getString("url");
        geocoder = getArguments().getBoolean("geocoder");

        String statusIcon = null;

        switch (statusCode){
            case 61714:
                statusIcon = "device_moving";
                break;
            case 61715:
                statusIcon = "device_stop";
                break;
            case 63601:
                statusIcon = "device_towing";
                break;
            case 62144:
                statusIcon = "device_parking";
                break;
        }

        Resources resources = container.getContext().getResources();

        final int resourceId = resources.getIdentifier(image, "drawable",
                container.getContext().getPackageName());

        imageViewObject.setImageResource(resourceId);

        Date time = new java.util.Date(event*1000);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
        objectDate.setText(date);

        if (statusIcon != null){
            final int statusResourceId = resources.getIdentifier(statusIcon, "drawable",
                    container.getContext().getPackageName());
            imageViewStatus.setImageResource(statusResourceId);
        }

        String driverIcon;

        if (driver.equals("empty")){
            driverIcon = "driver_grey";
            driverName.setText("");
        } else {
            driverIcon = "driver_green";
            driverName.setText(driver);
        }

        final int driverResourceId = resources.getIdentifier(driverIcon, "drawable",
                container.getContext().getPackageName());

        imageViewDriver.setImageResource(driverResourceId);

        String trailerIcon;

        if (trailer.equals("empty")){
            trailerIcon = "trailer_grey";
            trailerName.setText("");
        } else {
            trailerIcon = "trailer_green";
            trailerName.setText(trailer);
        }

        final int trailerResourceId = resources.getIdentifier(trailerIcon, "drawable",
                container.getContext().getPackageName());
        imageViewTrailer.setImageResource(trailerResourceId);

        String wifiIcon;

        if (!wifi){
            wifiIcon = "wifi_grey";
        } else {
            wifiIcon = "wifi_green";
        }

        final int wifiResourceId = resources.getIdentifier(wifiIcon, "drawable",
                container.getContext().getPackageName());
        imageViewWiFi.setImageResource(wifiResourceId);

        String lowFlorIcon;

        if (!isLowFlor){
            lowFlorIcon = "lowflor_grey";
        } else {
            lowFlorIcon = "lowflor_green";
        }

        final int lowFlorResourceId = resources.getIdentifier(lowFlorIcon, "drawable",
                container.getContext().getPackageName());
        imageViewLowFlor.setImageResource(lowFlorResourceId);

        if (address.equals("---")){
            geozone.setText("");
        } else {
            geozone.setText(address);
        }


        return rootView;
    }
}
