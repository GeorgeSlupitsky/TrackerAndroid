package com.micro_gis.microgistracker.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.micro_gis.microgistracker.R;
import com.micro_gis.microgistracker.fragments.ContentObjectFragment;
import com.micro_gis.microgistracker.fragments.InfoObjectFragment;
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

public class ObjectDetailInfoActivity extends FragmentActivity {

    private static API api;

    public static String jsonInfo;

    private SharedPreferences sharedPreferences;

    private String id;
    private String url;
    private String accaunt;
    private String key;
    private String interval;
    private String group;
    private boolean isGeocoderEnabled;
    private boolean isLabelEnabled;

    private MapObjectFragment mapObjectFragment;
    private ContentObjectFragment contentObjectFragment;

    private ImageView downArrow;
    private ImageView upArrow;

    private boolean isNormal;

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

        sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);

        Intent intent = getIntent();

        id = intent.getStringExtra("id");
        url = sharedPreferences.getString("url", "");
        accaunt = sharedPreferences.getString("account", "");
        key = sharedPreferences.getString("key", "");
        interval = sharedPreferences.getString("interval", "");
        isGeocoderEnabled = sharedPreferences.getBoolean("geocoder", false);
        isLabelEnabled = sharedPreferences.getBoolean("label", true);
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

                assert responseObjectMoving != null;

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

                    mapObjectFragment.setArguments(bundleMap);

                    Gson gson = new Gson();

                    String deviceJSON = gson.toJson(device);

                    sharedPreferences.edit().putString("deviceJSON", deviceJSON).apply();

//                    Bundle bundleInfo = new Bundle();
//                    bundleInfo.putString("description", device.getDescription());
//                    bundleInfo.putString("organization", device.getOrganization());
//                    bundleInfo.putString("lat", String.valueOf(device.getLat()));
//                    bundleInfo.putString("lng", String.valueOf(device.getLng()));
//                    bundleInfo.putString("speed", String.valueOf(device.getSpeed()));
//                    bundleInfo.putString("event", String.valueOf(device.getEvent()));
//                    bundleInfo.putString("heading", String.valueOf(device.getHeading()));
//                    bundleInfo.putString("brand", String.valueOf(device.getBrand()));
//                    bundleInfo.putString("color", String.valueOf(device.getColor()));
//                    bundleInfo.putString("icon", device.getIcon());
//                    bundleInfo.putString("plate", device.getPlate());
//                    bundleInfo.putString("altitude", String.valueOf(device.getAltitude()));
//                    bundleInfo.putString("satCount", String.valueOf(device.getSatCount()));
//                    bundleInfo.putString("hdop", String.valueOf(device.getHdop()));
//                    bundleInfo.putString("fuelExpense", String.valueOf(device.getFuelExpense()));
//                    bundleInfo.putString("fuelLevel", String.valueOf(device.getFuelLevel()));
//                    bundleInfo.putString("statusCode", String.valueOf(device.getStatusCode()));
//                    bundleInfo.putString("wifi", String.valueOf(device.isWifi()));
//                    bundleInfo.putString("lowFlor", String.valueOf(device.isLowFlor()));
//                    bundleInfo.putString("driverName", device.getDriverName());
//                    bundleInfo.putString("trailer", device.getTrailer());
//                    bundleInfo.putString("address", device.getAddress());
//
//                    contentObjectFragment.setArguments(bundleInfo);

                    FragmentTransaction transaction =
                            getSupportFragmentManager().beginTransaction();

                    transaction.add(R.id.objectMap, mapObjectFragment);
                    transaction.add(R.id.objectContent, contentObjectFragment);

                    transaction.commitAllowingStateLoss();
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
                    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                    int height = displayMetrics.heightPixels;
                    LinearLayout linearLayout = mapObjectFragment.getView().findViewById(R.id.webViewLL);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height - 155);
                    linearLayout.setLayoutParams(lp);
                    isNormal = false;
                    v.setVisibility(View.INVISIBLE);
                } else {
                    FragmentManager fm = getSupportFragmentManager();
                    fm.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .show(mapObjectFragment).commit();
                    isNormal = true;
                    upArrow.setVisibility(View.VISIBLE);
                }
            }
        });

        upArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNormal){
                    float density = getResources().getDisplayMetrics().density;
                    float height = 225 * density;
                    LinearLayout linearLayout = mapObjectFragment.getView().findViewById(R.id.webViewLL);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)height);
                    linearLayout.setLayoutParams(lp);
                    isNormal = true;
                    downArrow.setVisibility(View.VISIBLE);
                } else {
                    FragmentManager fm = getSupportFragmentManager();
                    fm.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .hide(mapObjectFragment).commit();
                    isNormal = false;
                    v.setVisibility(View.INVISIBLE);
                }
            }
        });


    }
}
