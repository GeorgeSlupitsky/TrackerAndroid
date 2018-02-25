package com.micro_gis.microgistracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Key;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

/**
 * Created by User3 on 19.02.2018.
 */

public class ObjectCustomAdapter extends ArrayAdapter<Map<String, Object>> {

    Context context;
    ArrayList<Map<String, Object>> data;
    ArrayList<Map<String, Object>> filterData = null;
    int layoutResourceId;
    private String[] mFrom;

    public ObjectCustomAdapter(Context context, int resource, ArrayList<Map<String, Object>> data, String[] mFrom) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
        this.filterData = new ArrayList<Map<String, Object>>();
        this.filterData.addAll(data);
        this.layoutResourceId = resource;
        this.mFrom = mFrom;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ObjectHolder holder = new ObjectHolder();

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.object, null);
        }

        holder.objectDescription = (TextView) row.findViewById(R.id.objectDescription);
        holder.driverName = (TextView) row.findViewById(R.id.driverName);
        holder.trailerName = (TextView) row.findViewById(R.id.trailerName);
        holder.imageViewObject = (ImageView) row.findViewById(R.id.imageViewObject);
        holder.imageViewStatus = (ImageView) row.findViewById(R.id.imageViewStatus);
        holder.objectDate = (TextView) row.findViewById(R.id.objectDate);
        holder.imageViewDriver = (ImageView) row.findViewById(R.id.imageViewDriver);
        holder.imageViewTrailer = (ImageView) row.findViewById(R.id.imageViewTrailer);
        holder.imageViewWiFi = (ImageView) row.findViewById(R.id.imageViewWiFi);
        holder.imageViewLowFlor = (ImageView) row.findViewById(R.id.imageViewLowFlor);
        holder.geozone = (TextView) row.findViewById(R.id.geozone);

        Integer id = (Integer) data.get(position).get(mFrom[0]);
        String text = (String) data.get(position).get(mFrom[1]);
        Integer status = (Integer) data.get(position).get(mFrom[2]);
        String image = (String) data.get(position).get(mFrom[3]);
        String color = (String) data.get(position).get(mFrom[4]);
        String date = (String) data.get(position).get(mFrom[5]);
        String driver = (String) data.get(position).get(mFrom[6]);
        String trailer = (String) data.get(position).get(mFrom[7]);
        Boolean wifi = (Boolean) data.get(position).get(mFrom[8]);
        Boolean lowFlor = (Boolean) data.get(position).get(mFrom[9]);
        String address = (String) data.get(position).get(mFrom[10]);

        final Integer index = id;

        holder.objectDescription.setText(text);

        String icon = image + "_" + color;

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
            holder.imageViewStatus.setImageResource(statusResourceId);
        }

        final int resourceId = resources.getIdentifier(icon, "drawable",
                context.getPackageName());

        holder.imageViewObject.setImageResource(resourceId);
        holder.objectDate.setText(date);

        String driverIcon;

        if (driver.equals("empty")){
            driverIcon = "driver_grey";
            holder.driverName.setText("");
        } else {
            driverIcon = "driver_green";
            holder.driverName.setText(driver);
        }

        final int driverResourceId = resources.getIdentifier(driverIcon, "drawable",
                context.getPackageName());
        holder.imageViewDriver.setImageResource(driverResourceId);

        String trailerIcon;

        if (trailer.equals("empty")){
            trailerIcon = "trailer_grey";
            holder.trailerName.setText("");
        } else {
            trailerIcon = "trailer_green";
            holder.trailerName.setText(trailer);
        }

        final int trailerResourceId = resources.getIdentifier(trailerIcon, "drawable",
                context.getPackageName());
        holder.imageViewTrailer.setImageResource(trailerResourceId);

        String wifiIcon;

        if (!wifi){
            wifiIcon = "wifi_grey";
        } else {
            wifiIcon = "wifi_green";
        }

        final int wifiResourceId = resources.getIdentifier(wifiIcon, "drawable",
                context.getPackageName());
        holder.imageViewWiFi.setImageResource(wifiResourceId);

        String lowFlorIcon;

        if (!lowFlor){
            lowFlorIcon = "lowflor_grey";
        } else {
            lowFlorIcon = "lowflor_green";
        }

        final int lowFlorResourceId = resources.getIdentifier(lowFlorIcon, "drawable",
                context.getPackageName());
        holder.imageViewLowFlor.setImageResource(lowFlorResourceId);

        if (address.equals("---")){
            holder.geozone.setText("");
        } else {
            holder.geozone.setText(address);
        }

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ObjectDetailInfoActivity.class);
                intent.putExtra("id", index+"");
                context.startActivity(intent);

            }
        });

        return row;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        data.clear();
        if (charText.length() == 0) {
            data.addAll(filterData);
        }
        else
        {
            for (Map<String, Object> map : filterData) {
                String name = (String) map.get(mFrom[1]);
                if (name.toLowerCase(Locale.getDefault()).contains(charText)) {
                    data.add(map);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class ObjectHolder{
        TextView objectDescription;
        TextView driverName;
        TextView trailerName;
        ImageView imageViewObject;
        ImageView imageViewStatus;
        ImageView imageViewDriver;
        ImageView imageViewTrailer;
        ImageView imageViewWiFi;
        ImageView imageViewLowFlor;
        TextView objectDate;
        TextView geozone;
    }
}
