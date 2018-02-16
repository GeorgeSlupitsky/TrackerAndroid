package com.micro_gis.microgistracker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by User3 on 15.02.2018.
 */

public class GroupsCustomAdapter extends BaseAdapter {
    Context context;
    private int layoutResourceId;
    private String[] mFrom;
    private List<Map<String, Object>> data;
    SharedPreferences preferences;

    private int selectedPosition = 0;

    public GroupsCustomAdapter(Context context, String[] from,
                               List<Map<String, Object>> data) {
        this.context = context;
        this.mFrom = from;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        preferences = context.getSharedPreferences("mypref", Context.MODE_PRIVATE);
        Integer savedPosition = preferences.getInt("position", 999999999);

        if (savedPosition != 999999999){
            selectedPosition = savedPosition;
        }

        final String[] from = mFrom;
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            v = inflater.inflate(R.layout.group_custom_adapter, null);
            RadioButton r = (RadioButton)v.findViewById(R.id.radiobutton);
        }
        TextView tv = (TextView)v.findViewById(R.id.textviewGroup);
        tv.setText(String.valueOf(data.get(position).get(from[1])));
        RadioButton r = (RadioButton)v.findViewById(R.id.radiobutton);
        r.setChecked(position == selectedPosition);
        r.setTag(position);
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = (Integer)view.getTag();
                preferences.edit().putInt("groupId", (Integer)data.get(selectedPosition).get(from[0])).apply();
                preferences.edit().putInt("position", selectedPosition).apply();
                notifyDataSetChanged();
            }
        });
        tv.setTag(position);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = (Integer)view.getTag();
                preferences.edit().putInt("groupId", (Integer)data.get(selectedPosition).get(from[0])).apply();
                preferences.edit().putInt("position", selectedPosition).apply();
                notifyDataSetChanged();
            }
        });
        return v;
    }
}
