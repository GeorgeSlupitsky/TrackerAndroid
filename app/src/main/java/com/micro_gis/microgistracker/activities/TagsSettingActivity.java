package com.micro_gis.microgistracker.activities;

import android.annotation.TargetApi;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TabHost;

import com.micro_gis.microgistracker.Power;
import com.micro_gis.microgistracker.R;

public class TagsSettingActivity extends TabActivity {

    private SharedPreferences sharedPreferences;
    private Handler handler = new Handler();

    Runnable checkCharging = new Runnable() {
        @Override
        public void run() {
            if (Power.isConnected(TagsSettingActivity.this)){
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } else {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
            handler.postDelayed(this, 3000);
        }
    };

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags_setting);
        final Button back = (Button) findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);
        String screenActivity = sharedPreferences.getString("screenActivity", "normal");

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

        TabHost tabHost = getTabHost();
        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator(getString(R.string.server_setting_title));
        tabSpec.setContent(new Intent(this, SettingActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag4");
        tabSpec.setIndicator(getString(R.string.server_monitoring));
        tabSpec.setContent(new Intent(this, RequestGroupsActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setIndicator(getString(R.string.filters));
        tabSpec.setContent(new Intent(this, FiltersActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator(getString(R.string.sensor_name_settings));
        tabSpec.setContent(new Intent(this, SettingSensorsActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag5");
        tabSpec.setIndicator(getString(R.string.additional_settings));
        tabSpec.setContent(new Intent(this, AdditionalSettingsActivity.class));
        tabHost.addTab(tabSpec);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(checkCharging);
    }

}
