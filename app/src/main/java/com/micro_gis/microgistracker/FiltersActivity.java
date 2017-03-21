package com.micro_gis.microgistracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

public class FiltersActivity extends AppCompatActivity {
    public static final String APP_PREFERENCES = "mypref";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        sharedpreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        final ColorPicker cp;
        final int[] selectedColorRGB = {Color.rgb(255, 0, 0)};
        cp = new ColorPicker(FiltersActivity.this, 255, 0, 0);
        final EditText trackWidth = (EditText) findViewById(R.id.trackWidth);
        final EditText minSpeed = (EditText) findViewById(R.id.minSpeed);
        final EditText maxSpeed = (EditText) findViewById(R.id.maxSpeed);
        final EditText minSatelites = (EditText) findViewById(R.id.minSatelites);
        Button save = (Button) findViewById(R.id.filterSave);

        assert trackWidth != null;
        assert minSpeed != null;
        assert minSatelites != null;
        assert maxSpeed != null;

        trackWidth.setText(sharedpreferences.getString("trackWidth", "3"));
        minSpeed.setText(sharedpreferences.getString("minSpeed", "3"));
        minSatelites.setText(sharedpreferences.getString("minSatelites", "3"));
        maxSpeed.setText(sharedpreferences.getString("maxSpeed", "160"));

        final Button color = (Button) findViewById(R.id.colorBtn);
        color.setBackgroundColor(sharedpreferences.getInt("trackcolor", selectedColorRGB[0]));
        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cp.show();
                Button okColor = (Button) cp.findViewById(R.id.okColorButton);
                okColor.setText(getString(R.string.save));
                okColor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int selectedColorR = cp.getRed();

                        selectedColorRGB[0] = cp.getColor();
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putInt("trackcolor", selectedColorRGB[0]);
                        editor.commit();
                        color.setBackgroundColor(selectedColorRGB[0]);
                        cp.dismiss();
                    }
                });
            }
        });

        assert save != null;
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("trackWidth", trackWidth.getText().toString());
                editor.putString("minSpeed", minSpeed.getText().toString());
                editor.putString("minSatelites", minSatelites.getText().toString());
                editor.putString("maxSpeed", maxSpeed.getText().toString());
                editor.commit();
                finish();

            }
        });
    }
}
