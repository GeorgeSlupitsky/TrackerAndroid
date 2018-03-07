package com.micro_gis.microgistracker.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.micro_gis.microgistracker.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by User9 on 07.03.2018.
 */

public class SensorsCustomAdapter extends ArrayAdapter<Map<String, String>> {

    private Context context;
    private ArrayList<Map<String, String>> data;
    private int layoutResourceId;
    private String[] mFrom;

    public SensorsCustomAdapter(Context context, int resource, ArrayList<Map<String, String>> data, String[] mFrom) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
        this.layoutResourceId = resource;
        this.mFrom = mFrom;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        InfoObjectCustomAdapter.InfoObjectHolder holder = new InfoObjectCustomAdapter.InfoObjectHolder();

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.custom_adapter_object_info, null);
        }

        holder.textViewKey = (TextView) row.findViewById(R.id.textViewKey);
        holder.textViewValue = (TextView) row.findViewById(R.id.textViewValue);

        String key =  data.get(position).get(mFrom[0]);
        String value = data.get(position).get(mFrom[1]);
        String measure = data.get(position).get(mFrom[2]);

        String unitMeasure = null;
        switch (measure){
            case "VALUE_LITERS":
                unitMeasure = getContext().getString(R.string.litres);
                break;
            case "VALUE_CELSIUM":
                unitMeasure = getContext().getString(R.string.celsium);
                break;
            case "VALUE_RPM":
                unitMeasure = getContext().getString(R.string.rpm);
                break;
            case "VALUE_VOLTAGE":
                unitMeasure = getContext().getString(R.string.volts);
                break;
            case "VALUE_KILOMETERS":
                unitMeasure = getContext().getString(R.string.Km);
                break;
            case "VALUE_HOURS":
                unitMeasure = getContext().getString(R.string.Hour);
                break;
            case "VALUE_PERCENT":
                unitMeasure = "%";
                break;
            case "VALUE_ACCELEROMETER":
                unitMeasure = getContext().getString(R.string.acceleration);
                break;
        }

        if (unitMeasure != null){
            holder.textViewValue.setText(value + " " + unitMeasure);
        } else {
            holder.textViewValue.setText(value);
        }

        holder.textViewKey.setText(key);

        return row;
    }

    static class InfoObjectHolder {
        TextView textViewKey;
        TextView textViewValue;
    }

}
