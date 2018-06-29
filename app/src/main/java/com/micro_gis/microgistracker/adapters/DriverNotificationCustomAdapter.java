package com.micro_gis.microgistracker.adapters;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.micro_gis.microgistracker.DBHelper;
import com.micro_gis.microgistracker.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class DriverNotificationCustomAdapter extends ArrayAdapter<Map<String, Object>> {

    private Context context;
    private ArrayList<Map<String, Object>> data;
    private int layoutResourceId;
    private String[] mFrom;
    private DBHelper dbHelper;

    public DriverNotificationCustomAdapter(Context context, int resource, ArrayList<Map<String, Object>> data, String[] mFrom) {
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
        DriverNotificationHolder holder = new DriverNotificationHolder();

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.custom_adapter_driver_notification, null);
        }

        holder.tvNotificationTime = (TextView) row.findViewById(R.id.tvNotificationTime);
        holder.tvNotificationText = (TextView) row.findViewById(R.id.tvNotificationText);

        Long time = (Long) data.get(position).get(mFrom[0]);

        String messageTime = new SimpleDateFormat("yyyy-MM-dd\nHH:mm").format(new Date(time * 1000));

        String message = (String) data.get(position).get(mFrom[1]);

        holder.tvNotificationTime.setText(messageTime);
        holder.tvNotificationText.setText(message);

        if ((Integer)data.get(position).get(mFrom[3]) == 0) {
            holder.tvNotificationTime.setTypeface(null, Typeface.BOLD);
            holder.tvNotificationText.setTypeface(null, Typeface.BOLD);

            dbHelper = new DBHelper(getContext());

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            Integer idInt = (Integer) data.get(position).get(mFrom[2]);
            String id = String.valueOf(idInt);

            ContentValues cv = new ContentValues();
            cv.put("time", time);
            cv.put("message", message);
            cv.put("isSeen", 1);

            db.update("messages", cv, "id = ?", new String[] { id });
        }


        return row;
    }

    static class DriverNotificationHolder {
        TextView tvNotificationTime;
        TextView tvNotificationText;
    }
}
