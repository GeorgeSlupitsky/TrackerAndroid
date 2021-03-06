package com.micro_gis.microgistracker.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.micro_gis.microgistracker.Communicator;
import com.micro_gis.microgistracker.Power;
import com.micro_gis.microgistracker.R;
import com.micro_gis.microgistracker.fragments.ContentObjectFragment;
import com.micro_gis.microgistracker.fragments.MapObjectFragment;
import com.micro_gis.microgistracker.models.rest.Account;
import com.micro_gis.microgistracker.models.rest.Device;
import com.micro_gis.microgistracker.models.rest.RequestGroupsMoving;
import com.micro_gis.microgistracker.models.rest.RequestObjectMoving;
import com.micro_gis.microgistracker.models.rest.ResponseGroupsMoving;
import com.micro_gis.microgistracker.models.rest.ResponseObjectMoving;
import com.micro_gis.microgistracker.models.rest.ResponseStatuses;
import com.micro_gis.microgistracker.retrofit.API;
import com.micro_gis.microgistracker.retrofit.APIController;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User3 on 19.02.2018.
 */

public class ObjectDetailInfoActivity extends FragmentActivity implements Communicator{

    private static API api;

    private SharedPreferences sharedPreferences;

    private String id;
    private String url;
    private String accaunt;
    private String key;
    private String interval;
    private String group;
    private boolean isGeocoderEnabled;
    private boolean isLabelEnabled;
    private boolean changeLabelsOnDriversName;
    private boolean drawLine;
    private boolean buttonsOfControl;

    private MapObjectFragment mapObjectFragment;
    private ContentObjectFragment contentObjectFragment;

    private ImageView downArrow;
    private ImageView upArrow;

    private boolean isNormal;

    private Handler handler = new Handler();

    Runnable checkCharging = new Runnable() {
        @Override
        public void run() {
            if (Power.isConnected(ObjectDetailInfoActivity.this)){
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
        setContentView(R.layout.activity_object_detail_info);

        final Button back = (Button) findViewById(R.id.back_buttonObject);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final RequestGroupsMoving requestGroupsMoving = new RequestGroupsMoving();
                requestGroupsMoving.setKey(key);
                Account account = new Account();
                account.setAccount(accaunt);
                account.setUseGeocoder(isGeocoderEnabled);
                List <String> grups = new ArrayList<>();
                grups.add(group);
                account.setGroups(grups);
                List<Account> accounts = new ArrayList<>();
                accounts.add(account);
                requestGroupsMoving.setAccounts(accounts);

                api.responseGroupsMoving(requestGroupsMoving).enqueue(new Callback<ResponseGroupsMoving>() {
                    @Override
                    public void onResponse(Call<ResponseGroupsMoving> call, Response<ResponseGroupsMoving> response) {
                        ResponseGroupsMoving responseGroupsMoving = response.body();

                        Gson gson = new Gson();
                        String json = gson.toJson(responseGroupsMoving);

                        sharedPreferences.edit().putString("groupObjects", json).apply();
                    }

                    @Override
                    public void onFailure(Call<ResponseGroupsMoving> call, Throwable t) {

                    }
                });

                finish();
            }
        });

        TextView toolbarTitleObject = (TextView) findViewById(R.id.toolbar_titleObject);
        FrameLayout mapObject = (FrameLayout) findViewById(R.id.objectMap);
        FrameLayout contentObject = (FrameLayout) findViewById(R.id.objectContent);

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

        Intent intent = getIntent();

        id = intent.getStringExtra("id");
        url = sharedPreferences.getString("url", "");
        accaunt = sharedPreferences.getString("account", "");
        key = sharedPreferences.getString("key", "");
        interval = sharedPreferences.getString("interval", "");
        isGeocoderEnabled = sharedPreferences.getBoolean("geocoder", false);
        isLabelEnabled = sharedPreferences.getBoolean("label", true);
        changeLabelsOnDriversName = sharedPreferences.getBoolean("changeLabels", false);
        drawLine = sharedPreferences.getBoolean("drawLine", true);
        buttonsOfControl = sharedPreferences.getBoolean("buttonsOfControl", true);
        group = sharedPreferences.getString("group", "");

        String description = intent.getStringExtra("description");

        toolbarTitleObject.setText(description);

        api = APIController.getApi(url);

        final RequestObjectMoving requestObjectMoving = new RequestObjectMoving();
        requestObjectMoving.setAccount(accaunt);
        requestObjectMoving.setKey(key);
        requestObjectMoving.setId(id);
        requestObjectMoving.setUseGeocoder(isGeocoderEnabled);

        api.responseObjectsMoving(requestObjectMoving).enqueue(new Callback<ResponseObjectMoving>() {
            @Override
            public void onResponse(Call<ResponseObjectMoving> call, Response<ResponseObjectMoving> response) {
                ResponseObjectMoving responseObjectMoving = response.body();

                if (responseObjectMoving != null){
                    if (responseObjectMoving.getStatus().equalsIgnoreCase(ResponseStatuses.WARNING.toString())){
                        List<String> warnings = responseObjectMoving.getWarnings();
                        if (warnings.get(0).contains(ResponseStatuses.WARNING_DOES_NOT_HAVE_ACCESS_TO_THE_DEVICE.toString())) {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    getString(R.string.warning_does_not_have_acces_to_device), Toast.LENGTH_LONG);
                            toast.show();
                            finish();
                        }
                    } else if (responseObjectMoving.getStatus().equalsIgnoreCase(ResponseStatuses.DEVICE_ID_IS_NOT_VALID.toString())){
                        Toast toast = Toast.makeText(getApplicationContext(),
                                getString(R.string.device_id_is_not_valid), Toast.LENGTH_LONG);
                        toast.show();
                        finish();
                    } else if (responseObjectMoving.getStatus().equalsIgnoreCase(ResponseStatuses.SUCCESS.toString())){
                        mapObjectFragment = new MapObjectFragment();
                        contentObjectFragment = new ContentObjectFragment();

                        Device device = responseObjectMoving.getDevice();
                        Bundle bundleMap = new Bundle();
                        bundleMap.putString("description", device.getDescription());
                        bundleMap.putString("organization", device.getOrganization());
                        bundleMap.putDouble("lat", device.getLat());
                        bundleMap.putDouble("lng", device.getLng());
                        bundleMap.putDouble("speed", device.getSpeed());
                        bundleMap.putLong("event", device.getEvent());
                        bundleMap.putInt("heading", device.getHeading());
                        bundleMap.putString("brand", device.getBrand());
                        bundleMap.putString("color", device.getColor());
                        bundleMap.putString("icon", device.getIcon());
                        bundleMap.putString("id", id);
                        bundleMap.putInt("altitude", device.getAltitude());
                        bundleMap.putInt("satCount", device.getSatCount());
                        bundleMap.putDouble("hdop", device.getHdop());
                        bundleMap.putDouble("fuelExpense", device.getFuelExpense());
                        bundleMap.putDouble("fuelLevel", device.getFuelLevel());
                        bundleMap.putString("account", accaunt);
                        bundleMap.putString("key", key);
                        bundleMap.putString("url", url);
                        bundleMap.putString("interval", interval);
                        bundleMap.putBoolean("geocoder", isGeocoderEnabled);
                        bundleMap.putBoolean("label", isLabelEnabled);
                        bundleMap.putBoolean("changeLabels", changeLabelsOnDriversName);
                        bundleMap.putBoolean("drawLine", drawLine);
                        bundleMap.putBoolean("buttonsOfControl", buttonsOfControl);

                        Gson gson = new Gson();

                        mapObjectFragment.setArguments(bundleMap);

                        Bundle bundleContent = new Bundle();
                        bundleContent.putString("id", id);
                        bundleContent.putString("account", accaunt);
                        bundleContent.putString("key", key);
                        bundleContent.putString("url", url);
                        bundleContent.putBoolean("geocoder", isGeocoderEnabled);
                        bundleContent.putString("color", device.getColor());
                        bundleContent.putString("icon", device.getIcon());

                        contentObjectFragment.setArguments(bundleContent);

                        String deviceJSON = gson.toJson(device);

                        sharedPreferences.edit().putString("deviceJSON", deviceJSON).apply();

                        FragmentTransaction transaction =
                                getSupportFragmentManager().beginTransaction();

                        transaction.add(R.id.objectMap, mapObjectFragment);
                        transaction.add(R.id.objectContent, contentObjectFragment);

                        transaction.commitAllowingStateLoss();
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            getString(R.string.status_error), Toast.LENGTH_LONG);
                    toast.show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseObjectMoving> call, Throwable t) {
                finish();
            }
        });

        downArrow = findViewById(R.id.arrowDownA);
        upArrow = findViewById(R.id.arrowUpA);

        isNormal = true;

        downArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNormal){
                    contentObject.setVisibility(View.GONE);
                    downArrow.setVisibility(View.INVISIBLE);
                    isNormal = false;
                } else {
                    isNormal = true;
                    upArrow.setVisibility(View.VISIBLE);
                    mapObject.setVisibility(View.VISIBLE);
                }
            }
        });

        upArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNormal){
                    isNormal = true;
                    downArrow.setVisibility(View.VISIBLE);
                    contentObject.setVisibility(View.VISIBLE);
                } else {
                    mapObject.setVisibility(View.GONE);
                    upArrow.setVisibility(View.INVISIBLE);
                    isNormal = false;
                }
            }
        });


    }

    @Override
    public void event(String account, String key, String id, Long dateFrom, Long dateTo, String duration) {
        mapObjectFragment.drawTrack(account, key, id, dateFrom, dateTo, duration);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(checkCharging);
    }
}
