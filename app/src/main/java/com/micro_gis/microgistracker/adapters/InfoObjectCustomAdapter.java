package com.micro_gis.microgistracker.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.micro_gis.microgistracker.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by User9 on 05.03.2018.
 */

public class InfoObjectCustomAdapter extends ArrayAdapter<Map<String, String>> {

    private Context context;
    private ArrayList<Map<String, String>> data;
    private int layoutResourceId;
    private String[] mFrom;

    public InfoObjectCustomAdapter(Context context, int resource, ArrayList<Map<String, String>> data, String[] mFrom) {
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
        InfoObjectHolder holder = new InfoObjectHolder();

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.custom_adapter_object_info, null);
        }

        holder.textViewKey = (TextView) row.findViewById(R.id.textViewKey);
        holder.textViewValue = (TextView) row.findViewById(R.id.textViewValue);

        String key =  data.get(position).get(mFrom[0]);
        String value = data.get(position).get(mFrom[1]);

        holder.textViewKey.setText(key);
        holder.textViewValue.setText(value);

        if (position % 2 == 0) {
            row.setBackgroundColor(Color.parseColor("#f0efef"));
        } else {
            row.setBackgroundColor(Color.WHITE);
        }

        return row;
    }

    static class InfoObjectHolder {
        TextView textViewKey;
        TextView textViewValue;
    }

}
