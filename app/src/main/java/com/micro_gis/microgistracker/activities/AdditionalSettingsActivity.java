package com.micro_gis.microgistracker.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.micro_gis.microgistracker.Power;
import com.micro_gis.microgistracker.R;

/**
 * Created by User3 on 23.02.2018.
 */

public class AdditionalSettingsActivity extends AppCompatActivity {

    private SharedPreferences sharedpreferences;
    private CheckBox labelParamsCheckBox;
    private CheckBox clusterParamsCheckBox;
    private CheckBox geocoderParamsCheckBox;
    private CheckBox navigationParamsCheckBox;
    private CheckBox changeLabelsCheckBox;
    private CheckBox drawLineCheckBox;
    private CheckBox buttonsOfControlCheckBox;
    private RadioGroup radioGroup;
    private RadioButton saAlways;
    private RadioButton saWhileCharging;
    private RadioButton saNormal;
    private Button save;

    private boolean label;
    private boolean cluster;
    private boolean geocoder;
    private boolean navigation;
    private boolean changeLabels;
    private boolean drawLine;
    private boolean buttonsOfControl;
    private String screenActivity;

    private Handler handler = new Handler();

    Runnable checkCharging = new Runnable() {
        @Override
        public void run() {
            if (Power.isConnected(AdditionalSettingsActivity.this)){
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
        setContentView(R.layout.activity_additional_settings);

        sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);

        labelParamsCheckBox = (CheckBox) findViewById(R.id.labelParamsCheckBox);
        clusterParamsCheckBox = (CheckBox) findViewById(R.id.clusterParamsCheckBox);
        geocoderParamsCheckBox = (CheckBox) findViewById(R.id.geocoderParamsCheckBox);
        navigationParamsCheckBox = (CheckBox) findViewById(R.id.navigationParamsCheckBox);
        changeLabelsCheckBox = (CheckBox) findViewById(R.id.changeLabels);
        drawLineCheckBox = (CheckBox) findViewById(R.id.drawLine);
        buttonsOfControlCheckBox = (CheckBox) findViewById(R.id.buttonsOfControl);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        saAlways = (RadioButton) findViewById(R.id.sa_always);
        saWhileCharging = (RadioButton) findViewById(R.id.sa_while_charging);
        saNormal = (RadioButton) findViewById(R.id.sa_normal);
        save = (Button) findViewById(R.id.saveAddSettings);

        labelParamsCheckBox.setChecked(sharedpreferences.getBoolean("label", true));
        clusterParamsCheckBox.setChecked(sharedpreferences.getBoolean("cluster", true));
        geocoderParamsCheckBox.setChecked(sharedpreferences.getBoolean("geocoder", false));
        navigationParamsCheckBox.setChecked(sharedpreferences.getBoolean("navigation", true));
        changeLabelsCheckBox.setChecked(sharedpreferences.getBoolean("changeLabels", false));
        drawLineCheckBox.setChecked(sharedpreferences.getBoolean("drawLine", true));
        buttonsOfControlCheckBox.setChecked(sharedpreferences.getBoolean("buttonsOfControl", true));

        screenActivity = sharedpreferences.getString("screenActivity", "normal");

        switch (screenActivity) {
            case "normal":
                saNormal.setChecked(true);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                handler.removeCallbacks(checkCharging);
                break;
            case "always":
                saAlways.setChecked(true);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                handler.removeCallbacks(checkCharging);
                break;
            case "while_charging":
                saWhileCharging.setChecked(true);
                handler.post(checkCharging);
                break;
        }

        label = labelParamsCheckBox.isChecked();

        cluster = clusterParamsCheckBox.isChecked();

        geocoder = geocoderParamsCheckBox.isChecked();

        navigation = navigationParamsCheckBox.isChecked();

        changeLabels = changeLabelsCheckBox.isChecked();

        drawLine = drawLineCheckBox.isChecked();

        buttonsOfControl = buttonsOfControlCheckBox.isChecked();

        labelParamsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                label = labelParamsCheckBox.isChecked();
            }
        });

        clusterParamsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cluster = clusterParamsCheckBox.isChecked();
            }
        });

        geocoderParamsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geocoder = geocoderParamsCheckBox.isChecked();
            }
        });

        navigationParamsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation = navigationParamsCheckBox.isChecked();
            }
        });

        changeLabelsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLabels = changeLabelsCheckBox.isChecked();
            }
        });

        drawLineCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawLine = drawLineCheckBox.isChecked();
            }
        });

        buttonsOfControlCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsOfControl = buttonsOfControlCheckBox.isChecked();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                switch (radioButtonID){
                    case R.id.sa_normal:
                        sharedpreferences.edit().putString("screenActivity", "normal").apply();
                        break;
                    case R.id.sa_always:
                        sharedpreferences.edit().putString("screenActivity", "always").apply();
                        break;
                    case R.id.sa_while_charging:
                        sharedpreferences.edit().putString("screenActivity", "while_charging").apply();
                        break;
                }
                sharedpreferences.edit().putBoolean("label", label).apply();
                sharedpreferences.edit().putBoolean("cluster", cluster).apply();
                sharedpreferences.edit().putBoolean("geocoder", geocoder).apply();
                sharedpreferences.edit().putBoolean("navigation", navigation).apply();
                sharedpreferences.edit().putBoolean("changeLabels", changeLabels).apply();
                sharedpreferences.edit().putBoolean("drawLine", drawLine).apply();
                sharedpreferences.edit().putBoolean("buttonsOfControl", buttonsOfControl).apply();
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(checkCharging);
    }
}
