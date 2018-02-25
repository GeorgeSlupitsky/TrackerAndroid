package com.micro_gis.microgistracker.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.micro_gis.microgistracker.R;

public class SettingActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    Button ok;
    Switch switchOn;
    EditText server,time,port,angle,distance;
    TextView sensorName;
    public static final String APP_PREFERENCES = "mypref";
    public static final String APP_PREFERENCES_SERVER = "serverKey";
    public static final String APP_PREFERENCES_PORT = "portKey";
    public static final String APP_PREFERENCES_PERIOD = "periodKey";
    public static final String APP_PREFERENCES_ANGLE = "angleKey";
    public static final String APP_PREFERENCES_DISTANCE = "distanceKey";
    public static final String APP_PREFERENCES_SWITCH = "switchKey";

    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ok =(Button)findViewById(R.id.okButton);
        server=(EditText)findViewById(R.id.server);
        time=(EditText) findViewById(R.id.period);
        port=(EditText)findViewById(R.id.port);
        angle=(EditText)findViewById(R.id.angle);
        distance=(EditText)findViewById(R.id.distance);
        switchOn =(Switch)findViewById(R.id.switch1);



        sharedpreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        server.setText(sharedpreferences.getString(APP_PREFERENCES_SERVER, ""));
        port.setText(sharedpreferences.getString(APP_PREFERENCES_PORT, ""));
        time.setText(sharedpreferences.getString(APP_PREFERENCES_PERIOD, "0"));
        angle.setText(sharedpreferences.getString(APP_PREFERENCES_ANGLE, "0"));
        distance.setText(sharedpreferences.getString(APP_PREFERENCES_DISTANCE, "0"));

        ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(APP_PREFERENCES_SERVER, server.getText().toString());
                editor.putString(APP_PREFERENCES_PORT, port.getText().toString());
                editor.putString(APP_PREFERENCES_ANGLE, angle.getText().toString());
                editor.putString(APP_PREFERENCES_DISTANCE, distance.getText().toString());
                editor.putString(APP_PREFERENCES_PERIOD, time.getText().toString());
                editor.commit();
                finish();
            }
        });
        linearLayout  = (LinearLayout) findViewById(R.id.serverSettingLayout);
        String isCheck = sharedpreferences.getString(APP_PREFERENCES_SWITCH, "false");
        if(!Boolean.parseBoolean(isCheck)){
            for (int j = 0; j < linearLayout.getChildCount(); j++) {
                View child = linearLayout.getChildAt(j);
                child.setEnabled(false);
            }
        }
        switchOn.setChecked(Boolean.parseBoolean(isCheck));
        switchOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isChecked){
                    for (int i = 0; i < linearLayout.getChildCount(); i++) {
                        View child = linearLayout.getChildAt(i);
                        child.setEnabled(false);
                    }

                }
                else {
                    for (int i = 0; i < linearLayout.getChildCount(); i++) {
                        View child = linearLayout.getChildAt(i);
                        child.setEnabled(true);
                    }

                }
                sharedpreferences.edit().putString(APP_PREFERENCES_SWITCH, String.valueOf(switchOn.isChecked())).commit();

            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        if(!Boolean.parseBoolean(sharedpreferences.getString(APP_PREFERENCES_SWITCH, "false"))){
            for (int j = 0; j < linearLayout.getChildCount(); j++) {
                View child = linearLayout.getChildAt(j);
                child.setEnabled(false);
            }
        }
        switchOn.setChecked(Boolean.parseBoolean(sharedpreferences.getString(APP_PREFERENCES_SWITCH, "false")));

//        linearLayout  = (LinearLayout) findViewById(R.id.serverSettingLayout);
//        if(!sharedpreferences.getBoolean(APP_PREFERENCES_SWITCH, false)){
//            for (int j = 0; j < linearLayout.getChildCount(); j++) {
//                View child = linearLayout.getChildAt(j);
//                child.setEnabled(false);
//            }
//        }
//        switchOn.setChecked(sharedpreferences.getBoolean(APP_PREFERENCES_SWITCH, false));
    }
    @Override
    public void onBackPressed() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(APP_PREFERENCES_SERVER, server.getText().toString());
        editor.putString(APP_PREFERENCES_PORT, port.getText().toString());
        editor.putString(APP_PREFERENCES_ANGLE, angle.getText().toString());
        editor.putString(APP_PREFERENCES_DISTANCE, distance.getText().toString());
        editor.putString(APP_PREFERENCES_PERIOD, time.getText().toString());
        editor.commit();
        finish();
    }

}