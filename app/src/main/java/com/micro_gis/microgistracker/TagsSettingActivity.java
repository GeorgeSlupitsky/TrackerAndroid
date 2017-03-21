package com.micro_gis.microgistracker;

import android.annotation.TargetApi;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

public class TagsSettingActivity extends TabActivity {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags_setting);
        TextView text =(TextView) findViewById(R.id.toolbar_title);
        text.setTextColor(Color.WHITE);
        TabHost tabHost = getTabHost();
        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator(getString(R.string.server_setting_title));
        tabSpec.setContent(new Intent(this, SettingActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag4");
        tabSpec.setIndicator(getString(R.string.server_monitoring));
        tabSpec.setContent(new Intent(this, RequestActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setIndicator(getString(R.string.filters));
        tabSpec.setContent(new Intent(this, FiltersActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator(getString(R.string.sensor_name_settings));
        tabSpec.setContent(new Intent(this, SettingSensorsActivity.class));
        tabHost.addTab(tabSpec);


    }


}
