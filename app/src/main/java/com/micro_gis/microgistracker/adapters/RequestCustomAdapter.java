package com.micro_gis.microgistracker.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.micro_gis.microgistracker.DBHelper;
import com.micro_gis.microgistracker.R;
import com.micro_gis.microgistracker.activities.AddRequestGroupActivity;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by User3 on 19.02.2018.
 */

public class RequestCustomAdapter extends ArrayAdapter<Map<String, Object>>{

    private Context context;
    private ArrayList<Map<String, Object>> data;
    private int layoutResourceId;
    private String[] mFrom;
    private SharedPreferences sharedPreferences;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public RequestCustomAdapter(Context context, int layoutResourceId, String[] mFrom, ArrayList<Map<String, Object>> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
        this.mFrom = mFrom;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        sharedPreferences = context.getSharedPreferences("mypref", Context.MODE_PRIVATE);
        View row = convertView;
        RequestHolder holder = new RequestHolder();

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.custom_adapter_request_group, null);
        }

        holder.tvText = (TextView) row.findViewById(R.id.tvText);
        holder.deleteGroup = (Button) row.findViewById(R.id.deleteGroup);
        holder.editGroup = (Button) row.findViewById(R.id.edit_group);

        String text = (String) data.get(position).get(mFrom[1]);
        holder.tvText.setText(text);
        final RequestHolder finalHolder = holder;
        final int pos = position;

        holder.editGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddRequestGroupActivity.class);
                Integer id = (Integer)data.get(pos).get(mFrom[0]);
                intent.putExtra("id", id+"");
                context.startActivity(intent);
            }
        });

        holder.deleteGroup.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    finalHolder.deleteGroup.setBackgroundResource(R.drawable.delete_btn);
                } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    finalHolder.deleteGroup.setBackgroundResource(R.drawable.delete_btn_black);
                }
                return false;
            }

        });

        holder.deleteGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                alertDialog.setTitle(context.getString(R.string.delete_group));

                alertDialog.setMessage(context.getString(R.string.delete)+" "+context.getString(R.string.group)+" \""+data.get(pos).get(mFrom[1])+"\"?");

                alertDialog.setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                        dbHelper = new DBHelper(context.getApplicationContext());
                        db = dbHelper.getWritableDatabase();

                        Integer id = (Integer)data.get(pos).get(mFrom[0]);

                        try
                        {
                            db.delete("requestgroup", "id = ?", new String[]{Integer.toString(id)});
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                        finally
                        {
                            db.close();
                        }
                        data.remove(pos);
                        sharedPreferences.edit().putInt("groupId", 999999999).apply();
                        notifyDataSetChanged();
                    }

                });

                // On pressing the No button
                alertDialog.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();
            }
        });

        if (position % 2 == 0) {
            row.setBackgroundColor(Color.parseColor("#f0efef"));
        } else {
            row.setBackgroundColor(Color.WHITE);
        }

        return row;
    }

    static class RequestHolder {
        TextView tvText;
        Button deleteGroup;
        Button editGroup;
    }
}
