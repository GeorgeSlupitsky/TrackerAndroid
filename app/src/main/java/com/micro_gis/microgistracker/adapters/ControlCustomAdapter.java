package com.micro_gis.microgistracker.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.micro_gis.microgistracker.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by User9 on 20.03.2018.
 */

public class ControlCustomAdapter extends ArrayAdapter<Map<String, Object>> {

    private Context context;
    private ArrayList<Map<String, Object>> data;
    private int layoutResourceId;
    private String[] mFrom;
    private boolean isColored = false;

    public ControlCustomAdapter(Context context, int resource, ArrayList<Map<String, Object>> data, String[] mFrom) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
        this.layoutResourceId = resource;
        this.mFrom = mFrom;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.custom_adapter_control, null);
        }

        ControlHolder holder = new ControlHolder();

        holder.imageView = row.findViewById(R.id.controlIV);
        holder.tvStatus = row.findViewById(R.id.tvStatusAdapter);

        String image = data.get(position).get(mFrom[4]) + "_" + data.get(position).get(mFrom[2]);

        Resources resources = context.getResources();

        final int statusResourceId = resources.getIdentifier(image, "drawable",
                context.getPackageName());
        holder.imageView.setImageResource(statusResourceId);

        holder.tvStatus.setText((String) data.get(position).get(mFrom[1]));

        return row;
    }

    static class ControlHolder{
        TextView tvStatus;
        ImageView imageView;
    }

}
