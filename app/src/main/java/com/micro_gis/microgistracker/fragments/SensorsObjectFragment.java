package com.micro_gis.microgistracker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.micro_gis.microgistracker.R;
import com.micro_gis.microgistracker.adapters.InfoObjectAdapter;
import com.micro_gis.microgistracker.models.rest.Device;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User9 on 05.03.2018.
 */

public class SensorsObjectFragment extends Fragment {

    private final String ATTRIBUTE_NAME_KEY = "key";
    private final String ATTRIBUTE_NAME_VALUE = "value";

    private String[] from = {ATTRIBUTE_NAME_KEY, ATTRIBUTE_NAME_VALUE};

    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_sensors_object, container, false);

        if (getActivity() != null){
            sharedPreferences = getActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        }

        String deviceJSON = sharedPreferences.getString("deviceJSON", "");

        Gson gson = new Gson();
        Device device = gson.fromJson(deviceJSON, Device.class);

        ArrayList<Map<String, String>> data = new ArrayList<>();

        Map <String, String> map = device.getSensors();

        for (String key: map.keySet()){
            Map<String, String> m = new HashMap<>();

            m.put(ATTRIBUTE_NAME_KEY, key);
            m.put(ATTRIBUTE_NAME_VALUE, map.get(key));

            data.add(m);
        }

        ListView listView = rootView.findViewById(R.id.listViewSensorsObject);

        InfoObjectAdapter infoObjectAdapter = new InfoObjectAdapter(getContext(), R.layout.custom_adapter_object_info, data, from);

        listView.setAdapter(infoObjectAdapter);

        return rootView;
    }
}
