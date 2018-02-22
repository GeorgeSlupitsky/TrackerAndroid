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
        holder.imageViewObject = (ImageView) row.findViewById(R.id.imageViewObject);
        holder.imageViewStatus = (ImageView) row.findViewById(R.id.imageViewStatus);
        holder.objectDate = (TextView) row.findViewById(R.id.objectDate);

        Integer id = (Integer) data.get(position).get(mFrom[0]);
        String text = (String) data.get(position).get(mFrom[1]);
        String status = (String) data.get(position).get(mFrom[2]);
        String image = (String) data.get(position).get(mFrom[3]);
        String color = (String) data.get(position).get(mFrom[4]);
        String date = (String) data.get(position).get(mFrom[5]);

        String icon = image + "_" + color;

        final Integer index = id;

        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(icon, "drawable",
                context.getPackageName());

        String statusIcon = null;

        switch (status){
            case "61714":
                statusIcon = "device_moving";
                break;
            case "61715":
                statusIcon = "device_stop";
                break;
            case "63601":
                statusIcon = "device_towing";
                break;
            case "62144":
                statusIcon = "device_parking";
                break;
        }

        if (statusIcon != null){
            final int statusResourceId = resources.getIdentifier(statusIcon, "drawable",
                    context.getPackageName());
            holder.imageViewStatus.setImageResource(statusResourceId);
        }

        holder.objectDescription.setText(text);
        holder.imageViewObject.setImageResource(resourceId);
        holder.objectDate.setText(date);

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
        ImageView imageViewObject;
        ImageView imageViewStatus;
        ImageView imageView1;
        ImageView imageView2;
        ImageView imageView3;
        ImageView imageView4;
        TextView objectDate;
    }
}
