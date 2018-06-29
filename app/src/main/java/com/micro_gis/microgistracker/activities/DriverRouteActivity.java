package com.micro_gis.microgistracker.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ListView;

import com.micro_gis.microgistracker.Power;
import com.micro_gis.microgistracker.R;
import com.micro_gis.microgistracker.adapters.DriverRouteCustomAdapter;
import com.micro_gis.microgistracker.models.rest.Point;
import com.micro_gis.microgistracker.models.rest.RequestRoute;
import com.micro_gis.microgistracker.models.rest.ResponseRoute;
import com.micro_gis.microgistracker.models.rest.ResponseStatuses;
import com.micro_gis.microgistracker.retrofit.API;
import com.micro_gis.microgistracker.retrofit.APIController;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverRouteActivity extends AppCompatActivity {

    private static API api;

    private String url;
    private Integer login;
    private String password;
    private Long voyageId;

    private SharedPreferences sharedPreferences;
    private Handler handler = new Handler();
    private DriverRouteCustomAdapter driverRouteCustomAdapter;

    private ListView listView;

    private RequestRoute requestRoute;

    Runnable checkCharging = new Runnable() {
        @Override
        public void run() {
            if (Power.isConnected(DriverRouteActivity.this)){
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } else {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
            handler.postDelayed(this, 3000);
        }
    };

    Runnable request = new Runnable() {
        @Override
        public void run() {
            api.responseRoute(requestRoute).enqueue(new Callback<ResponseRoute>() {
                @Override
                public void onResponse(Call<ResponseRoute> call, Response<ResponseRoute> response) {
                    ResponseRoute responseRoute = response.body();
                    if (responseRoute != null){
                        if (responseRoute.getStatus().equals(ResponseStatuses.SUCCESS.toString())){
                            Map<String, Point> pointMap = responseRoute.getPoints();
                            Point currentPoint = pointMap.get(String.valueOf(responseRoute.getLastPoint()));
                            listView.setSelection(responseRoute.getLastPoint() + currentPoint.getCircle() + 1);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseRoute> call, Throwable t) {
                }
            });

            handler.postDelayed(this, 5000);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_driver_route);
        listView = (ListView) findViewById(R.id.lvDriverRoute);

        sharedPreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);

        login = sharedPreferences.getInt("driverLogin", 0);
        password = sharedPreferences.getString("driverPassword", "");
        voyageId = sharedPreferences.getLong("voyageId", 0);

        url = sharedPreferences.getString("url", "http://track.micro-gis.com/api/");

        requestRoute = new RequestRoute();
        requestRoute.setLogin(login);
        requestRoute.setPassword(password);
        requestRoute.setVoyageId(voyageId);

        api = APIController.getApi(url);

        api.responseRoute(requestRoute).enqueue(new Callback<ResponseRoute>() {
            @Override
            public void onResponse(Call<ResponseRoute> call, Response<ResponseRoute> response) {
                ResponseRoute responseRoute = response.body();
                if (responseRoute != null){
                    if (responseRoute.getStatus().equals(ResponseStatuses.SUCCESS.toString())){
                        driverRouteCustomAdapter = new DriverRouteCustomAdapter(DriverRouteActivity.this);

                        Map<String, Point> pointMap = responseRoute.getPoints();

                        int circle = 0;

                        for (Point point: pointMap.values()){
                            if (circle == point.getCircle()){
                                int circleForSetting = point.getCircle() + 1;
                                driverRouteCustomAdapter.addSeparatorItem(getString(R.string.circle) + " " + circleForSetting);
                                circle++;
                            }
                            driverRouteCustomAdapter.addItem(point);
                        }

                        listView.setAdapter(driverRouteCustomAdapter);
                        Point currentPoint = pointMap.get(String.valueOf(responseRoute.getLastPoint()));
                        listView.setSelection(responseRoute.getLastPoint() + currentPoint.getCircle() + 1);

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseRoute> call, Throwable t) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
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

        handler.post(request);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(checkCharging);
        handler.removeCallbacks(request);
    }
}
