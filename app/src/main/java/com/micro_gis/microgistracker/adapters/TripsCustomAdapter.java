package com.micro_gis.microgistracker.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.micro_gis.microgistracker.R;

import org.apache.commons.math3.util.Precision;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by User9 on 07.03.2018.
 */

public class TripsCustomAdapter extends ArrayAdapter<Map<String, Object>> {

    private Context context;
    private ArrayList<Map<String, Object>> data;
    private int layoutResourceId;
    private String[] mFrom;

    public TripsCustomAdapter(Context context, int resource, ArrayList<Map<String, Object>> data, String[] mFrom) {
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
            row = inflater.inflate(R.layout.custom_adapter_trips, null);
        }

        TripHolder holder = new TripHolder();

        holder.timeTrip = (TextView) row.findViewById(R.id.timeTrip);
        holder.durationTrip = (TextView) row.findViewById(R.id.durationTrip);
        holder.distanceTrip = (TextView) row.findViewById(R.id.distanceTrip);
        holder.speedTrip = (TextView) row.findViewById(R.id.speedTrip);
        holder.addressStartTrip = (TextView) row.findViewById(R.id.addressStartTrip);
        holder.addressFinishTrip = (TextView) row.findViewById(R.id.addressFinishTrip);
        holder.imageViewStatusTrip = (ImageView) row.findViewById(R.id.imageViewStatusTrip);
        holder.imageViewStart = (ImageView) row.findViewById(R.id.imageViewStart);
        holder.imageViewFinish = (ImageView) row.findViewById(R.id.imageViewFinish);

        String time = (String) data.get(position).get(mFrom[0]);
        holder.timeTrip.setText(time);

        String duration = (String) data.get(position).get(mFrom[2]);

        String[] split = duration.split(":");

        String textDuration;

        if (!split[0].equals("0")){
            textDuration = split[0] + " " + getContext().getString(R.string.Hour) + " " + split [1] + " " + getContext().getString(R.string.Min);
        } else {
            if (split[1].startsWith("0")){
                split[1] = split[1].replaceFirst("0", "");
            }

            textDuration = split[1] + " " + getContext().getString(R.string.Min);
        }
        holder.durationTrip.setText(textDuration);

        Double distance = Precision.round((Double) data.get(position).get(mFrom[3]), 2);

        if (distance == 0){
            holder.distanceTrip.setText("");
        } else {
            holder.distanceTrip.setText(String.valueOf(distance) + " " + getContext().getString(R.string.Km));
        }

        Double speed = (Double) data.get(position).get(mFrom[4]);

        if (speed == 0){
            holder.speedTrip.setText("");
        } else {
            holder.speedTrip.setText(String.valueOf(speed) + " " + getContext().getString(R.string.KmH));
        }

        holder.imageViewStart.setVisibility(View.INVISIBLE);
        holder.imageViewFinish.setVisibility(View.INVISIBLE);

        String startAddress = (String) data.get(position).get(mFrom[5]);
        if (startAddress != null){
            holder.addressStartTrip.setText(startAddress);
            holder.imageViewStart.setVisibility(View.VISIBLE);
        } else {
            holder.addressStartTrip.setText("");
            holder.imageViewStart.setVisibility(View.INVISIBLE);
        }

        holder.imageViewFinish.setVisibility(View.INVISIBLE);

        String finishAddress = (String) data.get(position).get(mFrom[6]);
        if (finishAddress != null){
            holder.addressFinishTrip.setText(finishAddress);
            holder.imageViewFinish.setVisibility(View.VISIBLE);
        } else {
            holder.addressFinishTrip.setText("");
            holder.imageViewFinish.setVisibility(View.INVISIBLE);
        }

        Integer status = (Integer) data.get(position).get(mFrom[1]);

        String statusIcon = null;

        switch (status){
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

        Resources resources = context.getResources();

        if (statusIcon != null){
            final int statusResourceId = resources.getIdentifier(statusIcon, "drawable",
                    context.getPackageName());
            holder.imageViewStatusTrip.setImageResource(statusResourceId);
        }

        return row;
    }

    static class TripHolder{
        TextView timeTrip;
        TextView durationTrip;
        TextView distanceTrip;
        TextView speedTrip;
        TextView addressStartTrip;
        TextView addressFinishTrip;
        ImageView imageViewStatusTrip;
        ImageView imageViewStart;
        ImageView imageViewFinish;
    }
}
