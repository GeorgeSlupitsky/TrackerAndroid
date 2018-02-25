package com.micro_gis.microgistracker.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.micro_gis.microgistracker.activities.MicroGisActivity;
import com.micro_gis.microgistracker.models.database.Track;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by oleg on 09.06.16.
 */
public class TrackCustomAdapter extends ArrayAdapter<Track> {
    private LineChartData chartdata,chartdata1;

    private Context context;
    private int layoutResourceId;
    private ArrayList<Track> data = new ArrayList<Track>();
    private DBHelper dbHelper;
    private Track track;
    private SQLiteDatabase db;
    private int p;

    public TrackCustomAdapter(Context context, int layoutResourceId,
                             ArrayList<Track> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View row = convertView;
        TrackHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new TrackHolder();
            holder.trackName = (TextView) row.findViewById(R.id.trackName);
            holder.trackTime = (TextView) row.findViewById(R.id.timeTrack);
            holder.sensorsOnTrack = (TextView) row.findViewById(R.id.sensorOnTrack);
            holder.pointsOnTrack = (TextView) row.findViewById(R.id.pontsOntrack);
            holder.maxSpeed = (TextView) row.findViewById(R.id.maxSpeed);
            holder.averageSpeed = (TextView) row.findViewById(R.id.averageSpeed);
            holder.createTrack = (Button) row.findViewById(R.id.createTrack);
            holder.deleteTrack = (Button) row.findViewById(R.id.deleteTrack);
            holder.chart = (LineChartView) row.findViewById(R.id.chart);
            holder.chart1 = (LineChartView) row.findViewById(R.id.chart1);
            holder.trackLengh = (TextView) row.findViewById(R.id.tracklenght);
            holder.averageAltitude =(TextView)row.findViewById(R.id.averageAltitude);
            holder.maxAltitude =(TextView)row.findViewById(R.id.maxAltitude);
            row.setTag(holder);
        } else {
            holder = (TrackHolder) row.getTag();
        }
        p=position;
        track = data.get(position);
        holder.trackName.setText(track.getName());
        holder.averageSpeed.setText(track.getAverageSpeed());
        holder.maxSpeed.setText(track.getMaxSpeed());
        holder.pointsOnTrack.setText(""+track.getPointsOnTrack());
        holder.sensorsOnTrack.setText(""+track.getSensors().size());
        holder.trackTime.setText(track.getTime());
        holder.trackLengh.setText("    "+track.getTrackLenght()+" m ");
        holder.maxAltitude.setText(""+track.getMaxAltitude());
        holder.averageAltitude.setText(""+track.getAverageAltitude());


        float tempoRange = Float.parseFloat(track.getMaxSpeed().split(" ")[0])+10;

        float range = Float.parseFloat(track.getTrackLenght().toString())+10;
        //chart
        Line line;
        List<PointValue> values3;
        List<Line> lines = new ArrayList<Line>();
        values3 = new ArrayList<>();
        values3 = new ArrayList<PointValue>();
        if(track.getChartPoits()!=null) {
            for (int i = 0; i < track.getChartPoits().size(); ++i) {

                values3.add(new PointValue(track.getChartPoits().get(i).y, track.getChartPoits().get(i).x));
            }
        }
        line = new Line(values3);
        line.setColor(Color.GRAY);
        line.setHasPoints(false);
        line.setFilled(true);
        line.setStrokeWidth(1);
        lines.add(line);
        line = new Line(values3);
        line.setColor(Color.GRAY);
        line.setHasPoints(false);
        line.setFilled(true);
        line.setStrokeWidth(1);
        lines.add(line);

        chartdata = new LineChartData(lines);
        List<AxisValue> values1 = new ArrayList<>();
        for(float i = 0; i < range; i += 50.0f){
            AxisValue value2 = new AxisValue(i);
            values1.add(value2);
        }
        Axis distanceAxis = new Axis();
        distanceAxis.setName("Distance, m");
        distanceAxis.setValues(values1);
        distanceAxis.setTextColor(ChartUtils.COLOR_GREEN);
        distanceAxis.setMaxLabelChars(4);
        distanceAxis.setFormatter(new SimpleAxisValueFormatter().setAppendedText("".toCharArray()));
        distanceAxis.setHasLines(true);
        distanceAxis.setHasTiltedLabels(true);
        chartdata.setAxisXBottom(distanceAxis);




        List<AxisValue> axisValues = new ArrayList<>();
        for (float i = 0; i < tempoRange; i += 5.0f) {

            axisValues.add(new AxisValue(tempoRange-i).setLabel(String.valueOf(tempoRange - i)));
        }

        Axis tempoAxis = new Axis(axisValues).setName("Speed [km/h]").setHasLines(true).setMaxLabelChars(4)
                .setTextColor(ChartUtils.COLOR_GREEN);
        chartdata.setAxisYLeft(tempoAxis);


        holder.chart.setLineChartData(chartdata);

        Viewport v = holder.chart.getMaximumViewport();
        v.set(v.left, tempoRange, v.right, 0);
        holder.chart.setMaximumViewport(v);
        holder.chart.setCurrentViewport(v);

//chart1

        float altitude = Float.parseFloat(track.getMaxAltitude().split(" ")[0])+50;

        Line line1;
        List<PointValue> value3;
        List<Line> lines1 = new ArrayList<Line>();

        values3 = new ArrayList<PointValue>();
        if(track.getChartPoits()!=null) {
            for (int i = 1; i < track.getChartPoits().size()-1; i++) {

                values3.add(new PointValue(track.getAltitudeChart().get(i).y, track.getAltitudeChart().get(i).x));
            }
        }
        line1 = new Line(values3);
        line1.setColor(Color.GRAY);
        line1.setHasPoints(false);
        line1.setFilled(true);
        line1.setStrokeWidth(1);
        lines1.add(line1);
        line1 = new Line(values3);
        line1.setColor(Color.GRAY);
        line1.setHasPoints(false);
        line1.setFilled(true);
        line1.setStrokeWidth(1);
        lines1.add(line1);

        chartdata1 = new LineChartData(lines1);
        List<AxisValue> values2 = new ArrayList<>();
        for(float i = 0; i < range; i += 50.0f){
            AxisValue value2 = new AxisValue(i);
            values2.add(value2);
        }
        Axis distanceAxis1 = new Axis();
        distanceAxis1.setName("Distance, m");
        distanceAxis1.setValues(values2);
        distanceAxis1.setTextColor(ChartUtils.COLOR_GREEN);
        distanceAxis1.setMaxLabelChars(4);
        distanceAxis1.setFormatter(new SimpleAxisValueFormatter().setAppendedText("".toCharArray()));
        distanceAxis1.setHasLines(true);
        distanceAxis1.setHasTiltedLabels(true);
        chartdata1.setAxisXBottom(distanceAxis1);




        List<AxisValue> axisValues1 = new ArrayList<>();
        for (float i = 0; i < altitude; i += 50.0f) {

            axisValues1.add(new AxisValue(altitude-i).setLabel(String.valueOf(altitude - i)));
        }

        Axis tempoAxis1 = new Axis(axisValues1).setName("Altitude, m ").setHasLines(true).setMaxLabelChars(4)
                .setTextColor(ChartUtils.COLOR_GREEN);
        chartdata1.setAxisYLeft(tempoAxis1);


        holder.chart1.setLineChartData(chartdata1);

        Viewport v1 = holder.chart1.getMaximumViewport();
        v1.set(v1.left, altitude, v1.right, 0);
        holder.chart1.setMaximumViewport(v1);
        holder.chart1.setCurrentViewport(v1);




        holder.createTrack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                track = data.get(position);
                Activity activity = (Activity) context;
                Intent startIntent = new Intent(context,MicroGisActivity.class);
//                MicroGisActivity.track =track;

//                MicroGisActivity.track =track;
                startIntent.putExtra("trackpoits",track.getPoints().toString());
                startIntent.putExtra("start", track.getPoints().get(0).toString());
                startIntent.putExtra("end", track.getPoints().get(track.getPoints().size()-1).toString());
                MicroGisActivity.sensors=track.getSensors();

                ((Activity) context).setResult(Activity.RESULT_OK, startIntent);
                ((Activity) context).finish();
            }

        });

        final TrackHolder finalHolder = holder;
        holder.deleteTrack.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    finalHolder.deleteTrack.setBackgroundResource(R.drawable.delete_btn);
                } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    finalHolder.deleteTrack.setBackgroundResource(R.drawable.delete_btn_black);
                }
                return false;
            }

        });

        holder.deleteTrack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // Setting Dialog Title
                alertDialog.setTitle(context.getString(R.string.deleting_track));

                // Setting Dialog Message
                alertDialog.setMessage(context.getString(R.string.delete)+ " \""+track.getName()+"\" "+context.getString(R.string.track)+"?");

                // On pressing the Yes button.
                alertDialog.setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                        dbHelper = new DBHelper(context.getApplicationContext());
                        db = dbHelper.getWritableDatabase();

                        try
                        {
                            db.delete("trackdata", "name = ?", new String[]{new String(track.getName().toString())});
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

    static class TrackHolder {
        TextView trackName;
        TextView trackTime;
        TextView trackLengh;
        TextView sensorsOnTrack;
        TextView pointsOnTrack;
        TextView maxSpeed;
        TextView averageSpeed;
        Button createTrack;
        Button deleteTrack;
        LineChartView chart;
        LineChartView chart1;
        TextView averageAltitude;
        TextView maxAltitude;
    }


}
