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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by User9 on 15.03.2018.
 */

public class StatusCodeCustomAdapter extends ArrayAdapter<Map<String, Object>> {

    private Context context;
    private ArrayList<Map<String, Object>> data;
    private int layoutResourceId;
    private String[] mFrom;

    public StatusCodeCustomAdapter(Context context, int resource, ArrayList<Map<String, Object>> data, String[] mFrom) {
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
        StatusCodeCustomAdapter.StatusCodeObjectHolder holder = new StatusCodeCustomAdapter.StatusCodeObjectHolder();

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.custom_adapter_status_code, null);
        }

        holder.textViewDate = (TextView) row.findViewById(R.id.textViewDate);
        holder.textViewText = (TextView) row.findViewById(R.id.textViewText);

        Long time = (Long) data.get(position).get(mFrom[0]);
        Integer status = (Integer) data.get(position).get(mFrom[1]);
        String text = (String) data.get(position).get(mFrom[4]);

        Date date = new Date(time * 1000);
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        String dateFormatted = formatter.format(date);

        holder.textViewDate.setText(dateFormatted);

        String value = "";

        switch (status){
            case 65537:
                value = getContext().getString(R.string.fuellingManual);
                break;
            case 65538:
                value = getContext().getString(R.string.drainManual);
                break;
            case 65539:
                value = getContext().getString(R.string.fuellingAuto);
                break;
            case 65540:
                value = getContext().getString(R.string.drainAuto);
                break;
            case 65553:
                value = getContext().getString(R.string.worksessionEndManual);
                break;
            case 65554:
                value = getContext().getString(R.string.worksessionStartManual);
                break;
            case 65555:
                value = getContext().getString(R.string.worksessionEndVoyage);
                break;
            case 65556:
                value = getContext().getString(R.string.worksessionStartVoyage);
                break;
            case 65557:
                value = getContext().getString(R.string.worksessionEndRfid);
                break;
            case 65558:
                value = getContext().getString(R.string.worksessionStartRfid);
                break;
            case 65559:
                value = getContext().getString(R.string.temperatureInNorm);
                break;
            case 65560:
                value = getContext().getString(R.string.temperatureOutNorm);
                break;
            case 65568:
                value = getContext().getString(R.string.arrivalZone) + " " + text;
                break;
            case 65569:
                value = getContext().getString(R.string.departureZone) + " " + text;
                break;
            case 65570:
                value = getContext().getString(R.string.speedZone);
                break;
            case 65585:
                value = getContext().getString(R.string.serviceDayOverdue);
                break;
            case 65586:
                value = getContext().getString(R.string.arrivalZone) + " " + text;
                break;
            case 65580:
                value = getContext().getString(R.string.serviceDayApproximation);
                break;
            case 65581:
                value = getContext().getString(R.string.serviceDayOverdue);
                break;
            case 65582:
                value = getContext().getString(R.string.serviceDayApproximation);
                break;
            case 65583:
                value = getContext().getString(R.string.serviceDayOverdue);
                break;
            case 65584:
                value = getContext().getString(R.string.serviceDayApproximation);
                break;
            case 65620:
                value = getContext().getString(R.string.speedOutOfRange);
                break;
            case 65621:
                value = getContext().getString(R.string.speedInTheRange);
                break;
            case 65622:
                value = getContext().getString(R.string.maxSpeed);
                break;
            case 65623:
                value = getContext().getString(R.string.loseCommunication);
                break;
            case 65624:
                value = getContext().getString(R.string.loseCoordinates);
                break;
            case 65625:
                value = getContext().getString(R.string.sensorChangeOn);
                break;
            case 65626:
                value = getContext().getString(R.string.sensorChangeOff);
                break;
            case 65627:
                value = getContext().getString(R.string.sensorValueInTheRange);
                break;
            case 65628:
                value = getContext().getString(R.string.sensorValueOutOfRange);
                break;
        }


        holder.textViewText.setText(value);

        return row;
    }

    static class StatusCodeObjectHolder {
        TextView textViewDate;
        TextView textViewText;
    }

}
