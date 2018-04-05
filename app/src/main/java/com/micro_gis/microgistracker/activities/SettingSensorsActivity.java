package com.micro_gis.microgistracker.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.micro_gis.microgistracker.Power;
import com.micro_gis.microgistracker.R;

public class SettingSensorsActivity extends AppCompatActivity {
    private SharedPreferences sharedpreferences;
    public static String sensor0,sensor1,sensor2,sensor3,sensor4,sensor5;
    private EditText s0,s1,s2,s3,s4,s5;
    private Button save;
    public static final String SENSOR_PREFERENCES = "sensorpref";
    public static final String S0 = "s0";
    public static final String S1 = "s1";
    public static final String S2 = "s2";
    public static final String S3 = "s3";
    public static final String S4 = "s4";
    public static final String S5 = "s5";

    private Handler handler = new Handler();

    Runnable checkCharging = new Runnable() {
        @Override
        public void run() {
            if (Power.isConnected(SettingSensorsActivity.this)){
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } else {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
            handler.postDelayed(this, 3000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_sensors);
        s0=(EditText) findViewById(R.id.sensor_0);
        s1=(EditText) findViewById(R.id.sensor_1);
        s2=(EditText) findViewById(R.id.sensor_2);
        s3=(EditText) findViewById(R.id.sensor_3);
        s4=(EditText) findViewById(R.id.sensor_4);
        s5=(EditText) findViewById(R.id.sensor_5);
        save=(Button) findViewById(R.id.save);

        sharedpreferences = getSharedPreferences(SENSOR_PREFERENCES, Context.MODE_PRIVATE);

        String screenActivity = sharedpreferences.getString("screenActivity", "normal");

        switch (screenActivity) {
            case "normal":
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                handler.removeCallbacks(checkCharging);
                break;
            case "always":
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                handler.removeCallbacks(checkCharging);
                break;
            case "while_charging":
                handler.post(checkCharging);
                break;
        }

        s0.setText(sharedpreferences.getString(S0, ""));
        s1.setText(sharedpreferences.getString(S1, ""));
        s2.setText(sharedpreferences.getString(S2, ""));
        s3.setText(sharedpreferences.getString(S3, ""));
        s4.setText(sharedpreferences.getString(S4, ""));
        s5.setText(sharedpreferences.getString(S5, ""));

        save.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();

            }

        });

    }
    @Override
    public void onBackPressed() {
        save();
    }

    public void save(){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(S0, s0.getText().toString());
        editor.putString(S1, s1.getText().toString());
        editor.putString(S2, s2.getText().toString());
        editor.putString(S3, s3.getText().toString());
        editor.putString(S4, s4.getText().toString());
        editor.putString(S5, s5.getText().toString());
        editor.commit();
        sensor0=s0.getText().toString();
        sensor1=s1.getText().toString();
        sensor2=s2.getText().toString();
        sensor3=s3.getText().toString();
        sensor4=s4.getText().toString();
        sensor5=s5.getText().toString();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(checkCharging);
    }
}
