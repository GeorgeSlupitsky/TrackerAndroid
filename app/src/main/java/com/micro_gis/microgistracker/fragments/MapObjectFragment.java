package com.micro_gis.microgistracker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.micro_gis.microgistracker.R;
import com.micro_gis.microgistracker.WebAppInterface;
import com.micro_gis.microgistracker.models.rest.Device;
import com.micro_gis.microgistracker.models.rest.GeoZone;
import com.micro_gis.microgistracker.models.rest.RequestObjectMoving;
import com.micro_gis.microgistracker.models.rest.ResponseObjectMoving;
import com.micro_gis.microgistracker.retrofit.API;
import com.micro_gis.microgistracker.retrofit.APIController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.internal.zzip.runOnUiThread;

/**
 * Created by User9 on 02.03.2018.
 */

public class MapObjectFragment extends Fragment {

    private static API api;
    private WebView webView;

    private Boolean isLabelEnabled;

    private String description;
    private String organization;
    private Double lat;
    private Double lng;
    private Double speed;
    private Integer heading;
    private Long event;
    private String brand;
    private String color;
    private String icon;
    private String id;
    private Integer altitude;
    private Integer satCount;
    private Double hdop;
    private Double fuelExpense;
    private Double fuelLevel;
    private String account;
    private String key;
    private String url;
    private String interval;
    private Boolean geocoder;
    private String date;

    private SharedPreferences sharedPreferences;

    private Handler handler = new Handler();

    Runnable requst = new Runnable() {
        @Override
        public void run() {
            api = APIController.getApi(url);

            final RequestObjectMoving requestObjectMoving = new RequestObjectMoving();
            requestObjectMoving.setAccount(account);
            requestObjectMoving.setKey(key);
            requestObjectMoving.setId(id);
            requestObjectMoving.setUseGeocoder(geocoder);

            api.responseObjectsMoving(requestObjectMoving).enqueue(new Callback<ResponseObjectMoving>() {
                @Override
                public void onResponse(Call<ResponseObjectMoving> call, Response<ResponseObjectMoving> response) {
                    ResponseObjectMoving responseObjectMoving = response.body();

                    assert responseObjectMoving != null;
                    Device device = responseObjectMoving.getDevice();

                    Gson gson = new Gson();

                    String deviceJSON = gson.toJson(device);

                    sharedPreferences.edit().putString("deviceJSON", deviceJSON).apply();

                    description = device.getDescription();
                    brand = device.getBrand();
                    organization = device.getOrganization();
                    event = device.getEvent();
                    Date time = new java.util.Date(event *1000);
                    date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
                    speed = device.getSpeed();
                    altitude = device.getAltitude();
                    satCount = device.getSatCount();
                    hdop = device.getHdop();
                    fuelLevel = device.getFuelLevel();
                    fuelExpense = device.getFuelExpense();
                    heading = device.getHeading();
                    color = device.getColor();
                    lat = device.getLat();
                    lng = device.getLng();

                    String descriptionStr = getString(R.string.descriptionObj);
                    String brandStr = getString(R.string.brand);
                    String companyStr = getString(R.string.company);
                    String lastDataStr = getString(R.string.lastData);
                    String speedStr = getString(R.string.speed);
                    String altitudeStr = getString(R.string.altitude);
                    String satCountStr = getString(R.string.satCount);
                    String hdopStr = "HDOP";
                    String fuelLevelStr = getString(R.string.fuelLevel);
                    String fuelExpenseStr = getString(R.string.fuelExpense);

                    String html = descriptionStr + ": " + description +
                            " <br/>" + brandStr + ": " + brand +
                            " <br/>" + companyStr + ": " + organization +
                            " <br/>" + lastDataStr + ": " + date +
                            " <br/>" + speedStr + ": " + speed +
                            " <br/>" + altitudeStr + ": " + altitude +
                            " <br/>" + satCountStr + ": " + satCount +
                            " <br/>" + hdopStr + ": " + hdop +
                            " <br/>" + fuelLevelStr + ": " + fuelLevel +
                            " <br/>" + fuelExpenseStr + ": " + fuelExpense;

                    String[] DIRS = {"north","north-east","east","south-east","south","south-west","west","north-west"};

                    int[] ANCOR_X = {20, 20, 17, 25, 20, 20, 27, 25};
                    int[] ANCOR_Y = {20, 20, 20, 25, 25, 25, 20, 15};

                    int dirNdx = (int) (Math.floor(heading / 45) % 8);
                    String dirIconName = DIRS[dirNdx];
                    int ancX = ANCOR_X[dirNdx];
                    int ancY = ANCOR_Y[dirNdx];

                    webView.loadUrl("javascript: " +
                            "map.panTo(new L.LatLng(" + lat + ", " + lng +"));\n" +
                            "var isLabelEnabled = " + isLabelEnabled + ";\n" +
                            "var speed = " + speed + ";\n" +
                            "var busIcon = L.Icon.Default.extend({options: \n" +
                                "{iconUrl: 'file:///android_asset/images/deviceIcons/" + icon + "_" + color + ".png',\n" +
                                "iconSize: [32, 32],\n" +
                                "iconAnchor: [16, 16],\n" +
                                "shadowSize: [0, 0],\n" +
                                "popupAnchor: [0, -10],\n" +
                                "tooltipAnchor: [16, 0]} });\n" +
                            "var arrow" + id + ";\n" +
                            "if (speed > 0){\n" +
                                "var arrowIcon = new L.icon({\n" +
                                    "iconUrl: 'file:///android_asset/images/" + dirIconName + ".png',\n" +
                                    "iconSize: [44,44],\n" +
                                    "shadowUrl: null,\n" +
                                    "shadowSize: null,\n" +
                                    "iconAnchor: [" + ancX + ", " + ancY + "],\n" +
                                    "popupAnchor: [0, -10]\n" +
                                "});\n" +
                                "if (typeof(arrow" + id + ") === 'undefined'){\n" +
                                    "arrow" + id + " = new L.marker([" + lat + ", " + lng + "], {icon: arrowIcon});\n" +
                                    "arrow" + id + ".bindPopup(\"" + html + "\");"+
                                "} else {\n" +
                                    "arrow" + id + ".setIcon(arrowIcon);\n" +
                                    "arrow" + id + ".bindPopup(\"" + html + "\");"+
                                    "arrow" + id + ".setLatLng([" + lat + ", " + lng + "]);\n" +
                                "}\n" +
                            "} else {\n" +
                                "var arrowIcon = new L.icon({\n" +
                                    "iconUrl: 'file:///android_asset/images/empty.png',\n" +
                                    "iconSize: [44, 44],\n" +
                                    "shadowUrl: null,\n" +
                                    "shadowSize: null,\n" +
                                    "iconAnchor: [" + ancX + ", " + ancY + "],\n" +
                                    "popupAnchor: [0, 0]\n" +
                                "});\n"+
                                "if (typeof(arrow" + id + ")==='undefined'){\n" +
                                    "arrow" +  id + " = new L.marker([" + lat + ", " + lng + "], {icon: arrowIcon});\n" +
                                    "arrow" + id + ".bindPopup(\"" + html + "\");"+
                                    "arrow" + id + ".typeMarker = 'arrow';\n" +
                                "} else {\n" +
                                    "arrow" + id + ".setIcon(arrowIcon);"+
                                    "arrow" + id + ".bindPopup(\"" + html + "\");"+
                                    "arrow" + id + ".setLatLng([" + lat + ", " + lng + "]);\n" +
                                "}\n" +
                            "}\n" +
                            "var icon = new busIcon();\n" +
                            "if (typeof(bus" + id + ") === 'undefined'){\n" +
                                "if (isLabelEnabled){\n" +
                                    "bus" + id + " = new L.marker([" + lat + ", " + lng + "], {icon: icon})" +
                                        ".bindTooltip(\"" + description + "\", {permanent: true})" +
                                        ".bindPopup(\"" + html + "\");\n" +
                                "} else {\n" +
                                    "bus" + id + " = new L.marker([" + lat + ", " + lng + "], {icon: icon})" +
                                        ".bindPopup(\"" + html + "\");\n" +
                                "}\n" +
                            "} else {\n" +
                                "bus" + id + ".setIcon(icon);\n" +
                                "bus" + id + ".setLatLng([" + lat + ", " + lng + "]);\n" +
                                "bus" + id + ".unbindTooltip();\n" +
                                    "if (isLabelEnabled){\n" +
                                        "bus" + id + ".bindTooltip(\"" + description + "\", {permanent: true});" +
                                    "}\n" +
                                "bus" + id + ".bindPopup(\"" + html + "\");\n" +
                            "}\n" +
                            "bus" + id + ".addTo(map);\n" +
                            "arrow" + id + ".addTo(map);");

                    List<GeoZone> geoZones = device.getGeoZones();

                    if (geoZones != null){

                        Collections.sort(geoZones, (GeoZone g1, GeoZone g2) -> g1.getPriority() - g2.getPriority());
                        Collections.reverse(geoZones);

                        GeoZone geoZone = geoZones.get(0);

                        webView.loadUrl("javascript: " +
                        "map.eachLayer(function(layer) {\n" +
                                "if (layer.typeMarker == 'geozone'){\n" +
                                    "map.removeLayer(layer);\n" +
                                "}\n" +
                            "});\n" +
                        "var geozone = geomToWkt('" + geoZone.getGeom() + "').toObject({\n" +
                                "color: '" + geoZone.getColor() + "'\n" +
                                "});\n" +
                        "geozone.bindPopup('" + geoZone.getName() + "');\n" +
                        "geozone.typeMarker = 'geozone';\n" +
                        "map.addLayer(geozone);");
                    } else {
                        webView.loadUrl("javascript: " +
                        "map.eachLayer(function(layer) {\n" +
                            "if (layer.typeMarker == 'geozone'){\n" +
                                "map.removeLayer(layer);\n" +
                            "}\n" +
                        "});\n");
                    }
                }

                @Override
                public void onFailure(Call<ResponseObjectMoving> call, Throwable t) {

                }
            });
            handler.postDelayed(this, 1000*Long.parseLong(interval));
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_map_object, container, false);

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

        sharedPreferences = getActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE);

        webView.addJavascriptInterface(new WebAppInterface(rootView.getContext()), "Android");

        description = getArguments().getString("description");

        organization = getArguments().getString("organization");
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

        id = getArguments().getString("id");
        altitude = getArguments().getInt("altitude");
        satCount = getArguments().getInt("satCount");
        hdop = getArguments().getDouble("hdop");
        fuelExpense = getArguments().getDouble("fuelExpense");
        fuelLevel = getArguments().getDouble("fuelLevel");
        account = getArguments().getString("account");
        key = getArguments().getString("key");
        url = getArguments().getString("url");
        geocoder = getArguments().getBoolean("geocoder");
        isLabelEnabled = getArguments().getBoolean("label");
        interval = getArguments().getString("interval");

        handler.post(requst);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(requst);
    }

}
