package com.micro_gis.microgistracker.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.micro_gis.microgistracker.R;

/**
 * Created by User3 on 23.02.2018.
 */

public class AdditionalSettingsActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    CheckBox labelParamsCheckBox;
    CheckBox clusterParamsCheckBox;
    CheckBox geocoderParamsCheckBox;
    CheckBox navigationParamsCheckBox;
    CheckBox changeLabelsCheckBox;
    CheckBox drawLineCheckBox;
    CheckBox buttonsOfControlCheckBox;

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

        labelParamsCheckBox.setChecked(sharedpreferences.getBoolean("label", true));
        clusterParamsCheckBox.setChecked(sharedpreferences.getBoolean("cluster", true));
        geocoderParamsCheckBox.setChecked(sharedpreferences.getBoolean("geocoder", false));
        navigationParamsCheckBox.setChecked(sharedpreferences.getBoolean("navigation", true));
        changeLabelsCheckBox.setChecked(sharedpreferences.getBoolean("changeLabels", false));
        drawLineCheckBox.setChecked(sharedpreferences.getBoolean("drawLine", true));
        buttonsOfControlCheckBox.setChecked(sharedpreferences.getBoolean("buttonsOfControl", true));

        labelParamsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (labelParamsCheckBox.isChecked()){
                    sharedpreferences.edit().putBoolean("label", true).apply();
                } else {
                    sharedpreferences.edit().putBoolean("label", false).apply();
                }
            }
        });

        clusterParamsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clusterParamsCheckBox.isChecked()){
                    sharedpreferences.edit().putBoolean("cluster", true).apply();
                } else {
                    sharedpreferences.edit().putBoolean("cluster", false).apply();
                }
            }
        });

        geocoderParamsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (geocoderParamsCheckBox.isChecked()){
                    sharedpreferences.edit().putBoolean("geocoder", true).apply();
                } else {
                    sharedpreferences.edit().putBoolean("geocoder", false).apply();
                }
            }
        });

        navigationParamsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navigationParamsCheckBox.isChecked()){
                    sharedpreferences.edit().putBoolean("navigation", true).apply();
                } else {
                    sharedpreferences.edit().putBoolean("navigation", false).apply();
                }
            }
        });

        changeLabelsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changeLabelsCheckBox.isChecked()){
                    sharedpreferences.edit().putBoolean("changeLabels", true).apply();
                } else {
                    sharedpreferences.edit().putBoolean("changeLabels", false).apply();
                }
            }
        });

        drawLineCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawLineCheckBox.isChecked()){
                    sharedpreferences.edit().putBoolean("drawLine", true).apply();
                } else {
                    sharedpreferences.edit().putBoolean("drawLine", false).apply();
                }
            }
        });

        buttonsOfControlCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonsOfControlCheckBox.isChecked()){
                    sharedpreferences.edit().putBoolean("buttonsOfControl", true).apply();
                } else {
                    sharedpreferences.edit().putBoolean("buttonsOfControl", false).apply();
                }
            }
        });

    }
}
