package com.micro_gis.microgistracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by User3 on 15.02.2018.
 */

public class ObjectsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objects);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }
}
