package com.micro_gis.microgistracker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.micro_gis.microgistracker.R;
import com.micro_gis.microgistracker.adapters.InfoObjectCustomAdapter;
import com.micro_gis.microgistracker.models.rest.Device;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User9 on 05.03.2018.
 */

public class InfoObjectFragment extends Fragment {

    private final String ATTRIBUTE_NAME_KEY = "key";
    private final String ATTRIBUTE_NAME_VALUE = "value";

    private ArrayList<Map<String, String>> data;

    private ListView listView;

    private SharedPreferences sharedPreferences;

    private String interval;

    private Handler handler = new Handler();

    private String[] from = {ATTRIBUTE_NAME_KEY, ATTRIBUTE_NAME_VALUE};

    private InfoObjectCustomAdapter infoObjectCustomAdapter;

    private String deviceJSON;

    private LinkedTreeMap<String, String> tempData;

    Runnable request = new Runnable() {
        @Override
        public void run() {
            data = new ArrayList<>();

            deviceJSON = sharedPreferences.getString("deviceJSON", "");

            Gson gson = new Gson();
            Device device = gson.fromJson(deviceJSON, Device.class);

            createTempData(device);

            for (String key: tempData.keySet()){
                Map<String, String> m = new HashMap<>();

                m.put(ATTRIBUTE_NAME_KEY, key);
                m.put(ATTRIBUTE_NAME_VALUE, tempData.get(key));

                data.add(m);
            }

            infoObjectCustomAdapter = new InfoObjectCustomAdapter(getContext(), R.layout.custom_adapter_object_info, data, from);

            listView.setAdapter(infoObjectCustomAdapter);

            handler.postDelayed(this, 1000* Long.parseLong(interval));
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_info_object, container, false);

        if (getActivity() != null){
            sharedPreferences = getActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        }

        deviceJSON = sharedPreferences.getString("deviceJSON", "");

        interval = sharedPreferences.getString("interval", "");

        data = new ArrayList<>();

        Gson gson = new Gson();
        Device device = gson.fromJson(deviceJSON, Device.class);

        createTempData(device);

        for (String key: tempData.keySet()){
            Map<String, String> m = new HashMap<>();

            m.put(ATTRIBUTE_NAME_KEY, key);
            m.put(ATTRIBUTE_NAME_VALUE, tempData.get(key));

            data.add(m);
        }


        listView = rootView.findViewById(R.id.listViewInfoObject);

        infoObjectCustomAdapter = new InfoObjectCustomAdapter(getContext(), R.layout.custom_adapter_object_info, data, from);

        listView.setAdapter(infoObjectCustomAdapter);

        handler.post(request);


        return rootView;
    }

    @Override
    public void onDestroyView() {
        handler.removeCallbacks(request);
        super.onDestroyView();
    }

    private void createTempData(Device device){
        tempData = new LinkedTreeMap<>();

        if (device.getDriverName() != null){
            tempData.put(getString(R.string.driver), device.getDriverName());
        }

        if (device.getTrailer() != null){
            tempData.put(getString(R.string.trailer), device.getTrailer());
        }

        if (device.getAddress() != null){
            tempData.put(getString(R.string.addressObjInfo), device.getAddress());
        }

        tempData.put(getString(R.string.plate), device.getPlate());
        tempData.put(getString(R.string.brand), device.getBrand());
        tempData.put(getString(R.string.company), device.getOrganization());

        Long event = device.getEvent();
        Date time = new java.util.Date(event *1000);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);

        tempData.put(getString(R.string.lastData), date);
        tempData.put(getString(R.string.speed), String.valueOf(device.getSpeed()));
        tempData.put(getString(R.string.altitude), String.valueOf(device.getAltitude()));
        tempData.put(getString(R.string.satCount), String.valueOf(device.getSatCount()));
        tempData.put("HDOP", String.valueOf(device.getHdop()));
        tempData.put(getString(R.string.fuelLevel), String.valueOf(device.getFuelLevel()));
        tempData.put(getString(R.string.fuelExpense), String.valueOf(device.getFuelExpense()));
    }
}
