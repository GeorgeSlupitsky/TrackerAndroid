package com.micro_gis.microgistracker.activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
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
import com.micro_gis.microgistracker.services.CheckNotificationService;


public class DriverActivityTags extends TabActivity{

    private SharedPreferences sharedPreferences;
    private Handler handler = new Handler();
    private Button logout;
    private Button back;

    Runnable checkCharging = new Runnable() {
        @Override
        public void run() {
            if (Power.isConnected(DriverActivityTags.this)){
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
        setContentView(R.layout.activity_driver_tags);

        sharedPreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);

        back = (Button) findViewById(R.id.back_buttonDriver);
        logout = (Button) findViewById(R.id.logout);

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
        tabSpec.setIndicator(getString(R.string.events));
        tabSpec.setContent(new Intent(this, DriverNotificationActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator(getString(R.string.route));
        tabSpec.setContent(new Intent(this, DriverRouteActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setIndicator(getString(R.string.schedule));
        tabSpec.setContent(new Intent(this, DriverScheduleActivity.class));
        tabHost.addTab(tabSpec);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DriverActivityTags.this);

                alertDialog.setTitle(R.string.logout_driver);

                alertDialog.setMessage(getString(R.string.logout));

                alertDialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        sharedPreferences.edit().putBoolean("loginIn", false).apply();
                        sharedPreferences.edit().putInt("driverLogin", 0).apply();
                        sharedPreferences.edit().putString("driverPassword", "").apply();
                        sharedPreferences.edit().putBoolean("rememberMe", false).apply();
                        stopService(new Intent(DriverActivityTags.this, CheckNotificationService.class));
                        Intent intent = new Intent(DriverActivityTags.this, DriverLoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                });

                // On pressing the No button
                alertDialog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(checkCharging);

        if (!sharedPreferences.getBoolean("rememberMe", true)){
            stopService(new Intent(DriverActivityTags.this, CheckNotificationService.class));
        }

    }
}
