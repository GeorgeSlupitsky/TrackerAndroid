package com.micro_gis.microgistracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by oleg on 30.06.16.
 */
public class MarkerCustomAdapter extends ArrayAdapter<Marker> {
    Context context;
    int layoutResourceId;
    ArrayList<Marker> data = new ArrayList<Marker>();
    DBHelper dbHelper;
    Marker marker;
    SQLiteDatabase db;
    int p;
    public MarkerCustomAdapter(Context context, int layoutResourceId,
                              ArrayList<Marker> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View row = convertView;
        MarkersHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new MarkersHolder();
            holder.markerName = (TextView) row.findViewById(R.id.placeName);
            holder.description = (TextView) row.findViewById(R.id.placeDescription);
            holder.deletePlace = (Button) row.findViewById(R.id.deletePlace);
            holder.mrker_des =(TextView) row.findViewById(R.id.marker_des);
            holder.editplace=(Button) row.findViewById(R.id.edit_place);
            holder.editDes=(EditText)row.findViewById(R.id.editdes);
            row.setTag(holder);
        } else {
            holder = (MarkersHolder) row.getTag();
        }
        p=position;
        marker = data.get(position);
        holder.markerName.setText(marker.getName());
        holder.description.setText(marker.getLatlng());
        holder.mrker_des.setText(marker.getDescription());
        holder.editDes.setText(marker.getDescription());
        final MarkersHolder finalHolder = holder;
        final MarkersHolder finalHolder1 = holder;

        holder.editplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditPlaceActivity.class);
                EditPlaceActivity.marker = data.get(position);
                context.startActivity(intent);

//                if(finalHolder1.editDes.getVisibility()==View.GONE){
//                    finalHolder1.editDes.setVisibility(View.VISIBLE);
//                    finalHolder1.editDes.setText(finalHolder1.mrker_des.getText().toString());
//                    finalHolder1.mrker_des.setVisibility(View.GONE);
//                }else{
//                    finalHolder1.mrker_des.setVisibility(View.VISIBLE);
//                    finalHolder1.mrker_des.setText(finalHolder1.editDes.getText().toString());
//                    finalHolder1.editDes.setVisibility(View.GONE);
//
//                }
            }
        });
        holder.deletePlace.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    finalHolder.deletePlace.setBackgroundResource(R.drawable.delete_btn);
                } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    finalHolder.deletePlace.setBackgroundResource(R.drawable.delete_btn_black);
                }
                return false;
            }

        });
        holder.deletePlace.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // Setting Dialog Title
                alertDialog.setTitle(context.getString(R.string.deletig_marker));

                // Setting Dialog Message
                alertDialog.setMessage(context.getString(R.string.delete)+" \""+data.get(position).getName()+"\" "+context.getString(R.string.marker)+"?");

                // On pressing the Yes button.
                alertDialog.setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                        dbHelper = new DBHelper(context.getApplicationContext());
                        db = dbHelper.getWritableDatabase();

                        try
                        {
                            db.delete("markers", "latlng = ?", new String[]{new String(data.get(position).getLatlng())});
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                        finally
                        {
                            db.close();
                        }
                        data.remove(position);
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
        return row;

    }

    static class MarkersHolder {
        TextView markerName;
        TextView description;
        TextView mrker_des;
        Button deletePlace;
        Button editplace;
        EditText editDes;
    }

}
