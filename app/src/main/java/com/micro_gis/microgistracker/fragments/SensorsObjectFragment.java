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
import com.micro_gis.microgistracker.adapters.SensorsCustomAdapter;
import com.micro_gis.microgistracker.models.rest.Device;
import com.micro_gis.microgistracker.models.rest.Sensor;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.math3.util.Precision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by User9 on 05.03.2018.
 */

public class SensorsObjectFragment extends Fragment {

    private final String ATTRIBUTE_NAME_KEY = "key";
    private final String ATTRIBUTE_NAME_VALUE = "value";
    private final String ATTRIBUTE_NAME_MEASURE = "measure";

    private String[] from = {ATTRIBUTE_NAME_KEY, ATTRIBUTE_NAME_VALUE, ATTRIBUTE_NAME_MEASURE};

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

        List <Sensor> sensors = device.getSensors();

        if (sensors != null){
            for (Sensor sensor: sensors){
                Map<String, String> m = new HashMap<>();

                m.put(ATTRIBUTE_NAME_KEY, sensor.getDescription());
                if (NumberUtils.isParsable(sensor.getValue())){
                    m.put(ATTRIBUTE_NAME_VALUE, String.valueOf(Precision.round(Double.parseDouble(sensor.getValue()), 2)));
                } else {
                    m.put(ATTRIBUTE_NAME_VALUE, sensor.getValue());
                }
                m.put(ATTRIBUTE_NAME_MEASURE, sensor.getUnitMeasure());

                data.add(m);
            }
        }

        ListView listView = rootView.findViewById(R.id.listViewSensorsObject);

        SensorsCustomAdapter sensorsCustomAdapter = new SensorsCustomAdapter(getContext(), R.layout.custom_adapter_object_info, data, from);

        listView.setAdapter(sensorsCustomAdapter);

        return rootView;
    }
}
