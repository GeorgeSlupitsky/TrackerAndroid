package com.micro_gis.microgistracker.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TreeSet;

import com.micro_gis.microgistracker.R;
import com.micro_gis.microgistracker.models.rest.Point;

public class DriverRouteCustomAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

    private ArrayList<Object> mData = new ArrayList<>();
    private LayoutInflater mInflater;

    private TreeSet mSeparatorsSet = new TreeSet();

    public DriverRouteCustomAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final Point item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSeparatorItem(final String item) {
        mData.add(item);
        mSeparatorsSet.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int type = getItemViewType(position);
        System.out.println("getView " + position + " " + convertView + " type = " + type);

        if (convertView == null){
            holder = new ViewHolder();
            switch (type) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.custom_adapter_driver_route_list, null);
                    holder.routeName = (TextView)convertView.findViewById(R.id.textViewDriverRoute);
                    holder.arrivalIV = (ImageView)convertView.findViewById(R.id.ivArrival);
                    holder.departureIV = (ImageView)convertView.findViewById(R.id.ivDeparture);
                    holder.textViewArrival = (TextView)convertView.findViewById(R.id.tvArrival);
                    holder.textViewDeparture = (TextView)convertView.findViewById(R.id.tvDeparture);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.custom_adapter_driver_route_separator, null);
                    holder.circle = (TextView)convertView.findViewById(R.id.circle);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        switch (type) {
            case TYPE_ITEM:
                Point point = (Point)mData.get(position);
                holder.routeName.setText(point.getPointName());
                if (point.getArrivalString() != null){
                    holder.textViewArrival.setText(point.getArrivalString());
                } else {
                    holder.arrivalIV.setVisibility(View.GONE);
                    holder.textViewArrival.setVisibility(View.GONE);
                }
                if (point.getDepartureString() != null){
                    holder.textViewDeparture.setText(point.getDepartureString());
                } else {
                    holder.departureIV.setVisibility(View.GONE);
                    holder.textViewDeparture.setVisibility(View.GONE);
                }
                break;
            case TYPE_SEPARATOR:
                holder.circle.setText((String)mData.get(position));
                holder.circle.setTypeface(holder.circle.getTypeface(), Typeface.BOLD);
                break;
        }
        return convertView;
    }

    static class ViewHolder {
        TextView circle;
        TextView routeName;
        ImageView arrivalIV;
        ImageView departureIV;
        TextView textViewArrival;
        TextView textViewDeparture;

    }
}

