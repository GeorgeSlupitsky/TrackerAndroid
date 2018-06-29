package com.micro_gis.microgistracker.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.micro_gis.microgistracker.Power;
import com.micro_gis.microgistracker.R;
import com.micro_gis.microgistracker.models.rest.RequestVoyageStatus;
import com.micro_gis.microgistracker.models.rest.ResponseStatuses;
import com.micro_gis.microgistracker.models.rest.ResponseVoyageStatus;
import com.micro_gis.microgistracker.models.rest.VoyageDetail;
import com.micro_gis.microgistracker.retrofit.API;
import com.micro_gis.microgistracker.retrofit.APIController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverScheduleActivity extends AppCompatActivity {

    private static API api;

    private SharedPreferences sharedPreferences;
    private Handler handler = new Handler();
    private String url;
    private Integer login;
    private Long voyageId;
    private String password;

    private RelativeLayout rlDriverSchedule;
    private LinearLayout llNextPoint;
    private LinearLayout llCurrentPoint;

    private TextView tvNextPoint;
    private TextView tvNextPointTime;
    private TextView tvDeltaT;
    private TextView tvPreviousPoint;
    private TextView tvPreviousPointTime;

    private boolean noVoyage = false;
    private RequestVoyageStatus requestVoyageStatus;

    Runnable checkCharging = new Runnable() {
        @Override
        public void run() {
            if (Power.isConnected(DriverScheduleActivity.this)){
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
            api.responseVoyageStatus(requestVoyageStatus).enqueue(new Callback<ResponseVoyageStatus>() {
                @Override
                public void onResponse(Call<ResponseVoyageStatus> call, Response<ResponseVoyageStatus> response) {
                    ResponseVoyageStatus responseVoyageStatus = response.body();
                    if (responseVoyageStatus != null){
                        if (responseVoyageStatus.getStatus().equals(ResponseStatuses.SUCCESS.toString())){
                            if (noVoyage){
                                llCurrentPoint.setVisibility(View.VISIBLE);
                                llNextPoint.setVisibility(View.VISIBLE);
                                tvDeltaT.setVisibility(View.VISIBLE);
                                noVoyage = false;
                            }

                            VoyageDetail voyageDetail = responseVoyageStatus.getVoyageDetail();

                            String color = voyageDetail.getColor();

                            switch (color){
                                case "grey":
                                    rlDriverSchedule.setBackgroundColor(Color.parseColor("#85adad"));
                                    break;
                                case "blue":
                                    rlDriverSchedule.setBackgroundColor(Color.parseColor("#0066ff"));
                                    break;
                                case "orange":
                                    rlDriverSchedule.setBackgroundColor(Color.parseColor("#ff9100"));
                                    break;
                                case "green":
                                    rlDriverSchedule.setBackgroundColor(Color.parseColor("#33cc33"));
                                    break;
                                default:
                                    rlDriverSchedule.setBackgroundColor(Color.WHITE);
                                    break;
                            }

                            tvNextPoint.setText(voyageDetail.getNextPointString());
                            tvPreviousPoint.setText(voyageDetail.getCurrentPointString());
                            tvNextPointTime.setText(voyageDetail.getNextPointArrivalString());
                            tvPreviousPointTime.setText(voyageDetail.getCurrentPointDepartureString());
                            tvDeltaT.setText(voyageDetail.getTimelinessString());
                        }
                    } else {
                        rlDriverSchedule.setBackgroundColor(Color.WHITE);
                        llCurrentPoint.setVisibility(View.INVISIBLE);
                        llNextPoint.setVisibility(View.INVISIBLE);
                        tvDeltaT.setVisibility(View.INVISIBLE);
                        noVoyage = true;
                    }
                }

                @Override
                public void onFailure(Call<ResponseVoyageStatus> call, Throwable t) {
                    rlDriverSchedule.setBackgroundColor(Color.WHITE);
                    llCurrentPoint.setVisibility(View.INVISIBLE);
                    llNextPoint.setVisibility(View.INVISIBLE);
                    tvDeltaT.setVisibility(View.INVISIBLE);
                    noVoyage = true;
                }
            });

            handler.postDelayed(this, 5000);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_driver_schedule);

        rlDriverSchedule = (RelativeLayout) findViewById(R.id.rlDriverSchedule);
        llNextPoint = (LinearLayout) findViewById(R.id.llNextPoint);
        llCurrentPoint = (LinearLayout) findViewById(R.id.llCurrentPoint);

        tvNextPoint = (TextView) findViewById(R.id.tvNextPoint);
        tvNextPointTime = (TextView) findViewById(R.id.tvNextPointTime);
        tvDeltaT = (TextView) findViewById(R.id.tvDeltaT);
        tvPreviousPoint = (TextView) findViewById(R.id.tvPreviousPoint);
        tvPreviousPointTime = (TextView) findViewById(R.id.tvPreviousPointTime);

        sharedPreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);

        login = sharedPreferences.getInt("driverLogin", 0);
        password = sharedPreferences.getString("driverPassword", "");
        voyageId = sharedPreferences.getLong("voyageId", 0);

        url = sharedPreferences.getString("url", "http://track.micro-gis.com/api/");

        requestVoyageStatus = new RequestVoyageStatus();
        requestVoyageStatus.setLogin(login);
        requestVoyageStatus.setPassword(password);
        requestVoyageStatus.setVoyageId(voyageId);

        api = APIController.getApi(url);

        api.responseVoyageStatus(requestVoyageStatus).enqueue(new Callback<ResponseVoyageStatus>() {
            @Override
            public void onResponse(Call<ResponseVoyageStatus> call, Response<ResponseVoyageStatus> response) {
                ResponseVoyageStatus responseVoyageStatus = response.body();
                if (responseVoyageStatus != null){
                    if (responseVoyageStatus.getStatus().equals(ResponseStatuses.SUCCESS.toString())){
                        if (noVoyage){
                            llCurrentPoint.setVisibility(View.VISIBLE);
                            llNextPoint.setVisibility(View.VISIBLE);
                            tvDeltaT.setVisibility(View.VISIBLE);
                            noVoyage = false;
                        }

                        VoyageDetail voyageDetail = responseVoyageStatus.getVoyageDetail();

                        String color = voyageDetail.getColor();

                        switch (color){
                            case "grey":
                                rlDriverSchedule.setBackgroundColor(Color.parseColor("#85adad"));
                                break;
                            case "blue":
                                rlDriverSchedule.setBackgroundColor(Color.parseColor("#0066ff"));
                                break;
                            case "orange":
                                rlDriverSchedule.setBackgroundColor(Color.parseColor("#ff9100"));
                                break;
                            case "green":
                                rlDriverSchedule.setBackgroundColor(Color.parseColor("#33cc33"));
                                break;
                            default:
                                rlDriverSchedule.setBackgroundColor(Color.WHITE);
                                break;
                        }

                        tvNextPoint.setText(voyageDetail.getNextPointString());
                        tvPreviousPoint.setText(voyageDetail.getCurrentPointString());
                        tvNextPointTime.setText(voyageDetail.getNextPointArrivalString());
                        tvPreviousPointTime.setText(voyageDetail.getCurrentPointDepartureString());
                        tvDeltaT.setText(voyageDetail.getTimelinessString());
                    }
                } else {
                    rlDriverSchedule.setBackgroundColor(Color.WHITE);
                    llCurrentPoint.setVisibility(View.INVISIBLE);
                    llNextPoint.setVisibility(View.INVISIBLE);
                    tvDeltaT.setVisibility(View.INVISIBLE);
                    noVoyage = true;
                }
            }

            @Override
            public void onFailure(Call<ResponseVoyageStatus> call, Throwable t) {
                rlDriverSchedule.setBackgroundColor(Color.WHITE);
                llCurrentPoint.setVisibility(View.INVISIBLE);
                llNextPoint.setVisibility(View.INVISIBLE);
                tvDeltaT.setVisibility(View.INVISIBLE);
                noVoyage = true;
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
