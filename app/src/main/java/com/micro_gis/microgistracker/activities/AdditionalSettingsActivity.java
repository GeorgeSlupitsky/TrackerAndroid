package com.micro_gis.microgistracker.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

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
    private Button save;

    private boolean label;
    private boolean cluster;
    private boolean geocoder;
    private boolean navigation;
    private boolean changeLabels;
    private boolean drawLine;
    private boolean buttonsOfControl;

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
        save = (Button) findViewById(R.id.saveAddSettings);

        labelParamsCheckBox.setChecked(sharedpreferences.getBoolean("label", true));
        clusterParamsCheckBox.setChecked(sharedpreferences.getBoolean("cluster", true));
        geocoderParamsCheckBox.setChecked(sharedpreferences.getBoolean("geocoder", false));
        navigationParamsCheckBox.setChecked(sharedpreferences.getBoolean("navigation", true));
        changeLabelsCheckBox.setChecked(sharedpreferences.getBoolean("changeLabels", false));
        drawLineCheckBox.setChecked(sharedpreferences.getBoolean("drawLine", true));
        buttonsOfControlCheckBox.setChecked(sharedpreferences.getBoolean("buttonsOfControl", true));

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
                if (labelParamsCheckBox.isChecked()){
                    label = true;
                } else {
                    label = false;
                }
            }
        });

        clusterParamsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clusterParamsCheckBox.isChecked()){
                    cluster = true;
                } else {
                    cluster = false;
                }
            }
        });

        geocoderParamsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (geocoderParamsCheckBox.isChecked()){
                    geocoder = true;
                } else {
                    geocoder = false;
                }
            }
        });

        navigationParamsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navigationParamsCheckBox.isChecked()){
                    navigation = true;
                } else {
                    navigation = false;
                }
            }
        });

        changeLabelsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changeLabelsCheckBox.isChecked()){
                    changeLabels = true;
                } else {
                    changeLabels = false;
                }
            }
        });

        drawLineCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawLineCheckBox.isChecked()){
                    drawLine = true;
                } else {
                    drawLine = false;
                }
            }
        });

        buttonsOfControlCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonsOfControlCheckBox.isChecked()){
                    buttonsOfControl = true;
                } else {
                    buttonsOfControl = false;
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
}
