package com.micro_gis.microgistracker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.google.gson.Gson;
import com.micro_gis.microgistracker.R;
import com.micro_gis.microgistracker.WebAppInterface;
import com.micro_gis.microgistracker.models.rest.Device;
import com.micro_gis.microgistracker.models.rest.GeoZone;
import com.micro_gis.microgistracker.models.rest.PointInMap;
import com.micro_gis.microgistracker.models.rest.RequestDetailTrip;
import com.micro_gis.microgistracker.models.rest.RequestObjectMoving;
import com.micro_gis.microgistracker.models.rest.ResponseDetailTrip;
import com.micro_gis.microgistracker.models.rest.ResponseObjectMoving;
import com.micro_gis.microgistracker.models.rest.ResponseStatuses;
import com.micro_gis.microgistracker.retrofit.API;
import com.micro_gis.microgistracker.retrofit.APIController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
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
    private Button cleanLayer;

    private Boolean isLabelEnabled;
    private Boolean changeLabelsOnDriversName;
    private Boolean drawLine;
    private Boolean buttonsOfControl;

    private List<String> coordinatesForTrackLine = new ArrayList<>();

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

    private boolean trackInMap = false;

    Runnable requst = new Runnable() {
        @Override
        public void run() {
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

                    String descriptionStr = getContext().getString(R.string.descriptionObj);
                    String brandStr = getString(R.string.brand);
                    String companyStr = getString(R.string.company);
                    String lastDataStr = getString(R.string.lastData);
                    String speedStr = getString(R.string.speed);
                    String altitudeStr = getString(R.string.altitude);
                    String satCountStr = getString(R.string.satCount);
                    String hdopStr = "HDOP";
                    String fuelLevelStr = getString(R.string.fuelLevel);
                    String fuelExpenseStr = getString(R.string.fuelExpense);

                    String driverName = null;
                    boolean hasName = false;

                    if (device.getDriverName() != null){
                        driverName = device.getDriverName();
                        hasName = true;
                    }

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

                    if (!trackInMap){
                        webView.loadUrl("javascript: " +
                                "var hasName = " + hasName + ";\n" +
                                "var changeLabels = " + changeLabelsOnDriversName + ";\n" +
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
                                ".bindPopup(\"" + html + "\");\n" +
                                "if (changeLabels && hasName){\n" +
                                "bus" + id + ".bindTooltip(\"" + driverName + "\", {permanent: true})\n" +
                                "} else {\n" +
                                "bus" + id + ".bindTooltip(\"" + description + "\", {permanent: true})\n" +
                                "}\n" +
                                "bus" + id + ".typeMarker = 'car';\n" +
                                "} else {\n" +
                                "bus" + id + " = new L.marker([" + lat + ", " + lng + "], {icon: icon})" +
                                ".bindPopup(\"" + html + "\");\n" +
                                "bus" + id + ".typeMarker = 'car';\n" +
                                "}\n" +
                                "} else {\n" +
                                "bus" + id + ".setIcon(icon);\n" +
                                "bus" + id + ".setLatLng([" + lat + ", " + lng + "]);\n" +
                                "bus" + id + ".unbindTooltip();\n" +
                                "if (isLabelEnabled){\n" +
                                "if (changeLabels && hasName){\n" +
                                "bus" + id + ".bindTooltip(\"" + driverName + "\", {permanent: true})\n" +
                                "} else {\n" +
                                "bus" + id + ".bindTooltip(\"" + description + "\", {permanent: true});\n" +
                                "}\n" +
                                "}\n" +
                                "bus" + id + ".bindPopup(\"" + html + "\");\n" +
                                "}\n" +
                                "bus" + id + ".addTo(map);\n" +
                                "arrow" + id + ".addTo(map);");

                        List<GeoZone> geoZones = device.getGeoZones();

                        if (coordinatesForTrackLine.size() != 10){
                            coordinatesForTrackLine.add("[" + device.getLat() + ", " + device.getLng() + "]");
                        } else {
                            coordinatesForTrackLine.remove(0);
                            coordinatesForTrackLine.add("[" + device.getLat() + ", " + device.getLng() + "]");
                        }

                        if (drawLine){

                            String start = "[";
                            String end = "]";
                            String tempCoordinate = "";

                            Iterator iterator = coordinatesForTrackLine.iterator();

                            while (iterator.hasNext()){
                                String trailTrack = (String) iterator.next();
                                tempCoordinate = tempCoordinate + trailTrack;
                                if (iterator.hasNext()){
                                    tempCoordinate = tempCoordinate + ", ";
                                }
                            }

                            String coordinates = start + tempCoordinate + end;

                            String hexColor = String.format("#%06X", (0xFFFFFF & sharedPreferences.getInt("trackcolor", 0xffff0000)));

                            webView.loadUrl("javascript:map.eachLayer(function(layer) {\n" +
                                    "if (layer.type == 'drawLine'){\n" +
                                    "map.removeLayer(layer)\n" +
                                    "}\n" +
                                    "});");

                            webView.loadUrl("javascript: " +
                                    "drawLineTrack(" + coordinates + ");\n"
                            );

                            webView.loadUrl("javascript:drawLine.setStyle({\n" +
                                    "color: '" + hexColor +
                                    "'\n});");

                        }

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

        cleanLayer = (Button) rootView.findViewById(R.id.cleanLayersObject);

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
        drawLine = getArguments().getBoolean("drawLine");

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
        buttonsOfControl = getArguments().getBoolean("buttonsOfControl");
        changeLabelsOnDriversName = getArguments().getBoolean("changeLabels");
        interval = getArguments().getString("interval");

        api = APIController.getApi(url);

        handler.post(requst);

        cleanLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackInMap = false;
                coordinatesForTrackLine = new ArrayList<>();
                webView.loadUrl("javascript:map.removeLayer(polyline);");
                webView.loadUrl("javascript:map.removeLayer(drawLine);");
                webView.loadUrl("javascript:map.eachLayer(function(layer) {\n" +
                        "if (layer instanceof L.Marker) {\n" +
                        "if (layer.typeMarker === 'flag' || layer.typeMarker === 'parking'){\n" +
                        "map.removeLayer(layer);\n" +
                        "}\n" +
                        "}" +
                        "});");
                webView.loadUrl("javascript:map.panTo(new L.LatLng(" + lat + ", " + lng + "));\n");
            }
        });

        cleanLayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    cleanLayer.setBackgroundResource(R.drawable.clean);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    cleanLayer.setBackgroundResource(R.drawable.clean_a);
                }
                return false;
            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (buttonsOfControl){
                    webView.loadUrl("javascript:" +
                            "map.addControl(map.zoomControl);\n" +
                            "geoSearch = new L.Control.GeoSearch({\n" +
                            "    provider: new L.GeoSearch.Provider.Google()\n" +
                            "}).addTo(map);"
                    );
                } else {
                    webView.loadUrl("javascript:" +
                            "map.removeControl(map.zoomControl);\n" +
                            "map.removeControl(geoSearch);\n"
                    );
                }
            }
        }, 500);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(requst);
    }

    public void drawTrack(String account, String key, String id, Long dateFrom, Long dateTo, String duration){

        RequestDetailTrip requestDetailTrip = new RequestDetailTrip();

        requestDetailTrip.setKey(key);
        requestDetailTrip.setAccount(account);
        requestDetailTrip.setId(id);
        requestDetailTrip.setDateFrom(dateFrom);
        requestDetailTrip.setDateTo(dateTo);

        api.responseDetailTrip(requestDetailTrip).enqueue(new Callback<ResponseDetailTrip>() {
            @Override
            public void onResponse(Call<ResponseDetailTrip> call, Response<ResponseDetailTrip> response) {
                ResponseDetailTrip responseDetailTrip = response.body();

                if (responseDetailTrip != null){
                    if (responseDetailTrip.getStatus().equals(ResponseStatuses.SUCCESS.toString())){
                        List <PointInMap> points = responseDetailTrip.getPoints();

                        if (points != null && !points.isEmpty()){

                            trackInMap = true;

                            webView.loadUrl("javascript:map.removeLayer(polyline);");
                            webView.loadUrl("javascript:map.eachLayer(function(layer) {\n" +
                                    "if (layer instanceof L.Marker) {\n" +
                                    "if (layer.typeMarker === 'flag' || layer.typeMarker === 'parking'){\n" +
                                        "map.removeLayer(layer);\n" +
                                    "}\n" +
                                    "}" +
                                    "});");

                            if (points.get(0).getStatus() == 62144){

                                String[] split = duration.split(":");

                                String textDuration;

                                if (!split[0].equals("0")){
                                    textDuration = split[0] + " " + getString(R.string.Hour) + " " + split [1] + " " + getString(R.string.Min);
                                } else {
                                    if (split[1].startsWith("0")){
                                        split[1] = split[1].replaceFirst("0", "");
                                    }

                                    textDuration = split[1] + " " + getString(R.string.Min);
                                }

                                String parkingStr = getString(R.string.parking);

                                webView.loadUrl("javascript: " +
                                    "var icon = new L.icon({\n" +
                                        "iconUrl: 'file:///android_asset/images/device_parking.png',\n" +
                                        "iconSize: [25, 25],\n" +
                                        "shadowUrl: null,\n" +
                                        "shadowSize: null,\n" +
                                        "popupAnchor: [0, 0]\n" +
                                        "});\n"+
                                    "var parking = new L.Marker ([" + points.get(0).getLat() + ", " + points.get(0).getLng() +"], {icon: icon});\n" +
                                        "parking.typeMarker = 'parking';\n" +
                                        "parking.bindPopup('" + parkingStr + ": " + textDuration + "');\n" +
                                        "parking.addTo(map);\n" +
                                        "map.panTo(new L.LatLng(" + points.get(0).getLat() + ", " + points.get(0).getLng() +"));\n"
                                );

                            } else {

                                String start = "[";
                                String end = "]";
                                String tempCoordinate = "";

                                PointInMap pointInMapFirst = points.get(0);

                                String startCoordinate = "[" + pointInMapFirst.getLat() + ", " + pointInMapFirst.getLng() + "]";
                                String endCoordinate = "";

                                Iterator iterator = points.iterator();

                                while (iterator.hasNext()){
                                    PointInMap pointInMap = (PointInMap) iterator.next();
                                    String point = "[" + pointInMap.getLat() + ", " + pointInMap.getLng() + "]";
                                    tempCoordinate = tempCoordinate + point;
                                    if (iterator.hasNext()){
                                        tempCoordinate = tempCoordinate + ", ";
                                    } else {
                                        endCoordinate = "[" + pointInMap.getLat() + ", " + pointInMap.getLng() + "]";
                                    }
                                }

                                String coordinates = start + tempCoordinate + end;

                                String hexColor = String.format("#%06X", (0xFFFFFF & sharedPreferences.getInt("trackcolor", 0xffff0000)));

                                webView.loadUrl("javascript: " +
                                    "lineTrack(" + coordinates + "," + startCoordinate + "," + endCoordinate + "," + "'" + getString(R.string.startTrack) + "'" + "," + "'" + getString(R.string.finishTrack) + "'" + ");\n" +
                                        "map.fitBounds([" + startCoordinate + ", " + endCoordinate + "]);\n"

                                );

                                webView.loadUrl("javascript:polyline.setStyle({\n" +
                                            "color: '" + hexColor +
                                        "'\n});");
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDetailTrip> call, Throwable t) {

            }
        });

    }

}
