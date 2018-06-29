package com.micro_gis.microgistracker.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.micro_gis.microgistracker.Power;
import com.micro_gis.microgistracker.R;
import com.micro_gis.microgistracker.models.rest.RequestCheckPassword;
import com.micro_gis.microgistracker.models.rest.ResponseCheckPassword;
import com.micro_gis.microgistracker.models.rest.ResponseStatuses;
import com.micro_gis.microgistracker.retrofit.API;
import com.micro_gis.microgistracker.retrofit.APIController;
import com.micro_gis.microgistracker.services.CheckNotificationService;

import org.apache.commons.lang3.math.NumberUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverLoginActivity extends AppCompatActivity {

    private static API api;

    private String url;
    private EditText login;
    private EditText password;
    private CheckBox rememberMe;
    private Button enter;
    private Button back;
    private SharedPreferences sharedPreferences;
    private Handler handler = new Handler();

    Runnable checkCharging = new Runnable() {
        @Override
        public void run() {
            if (Power.isConnected(DriverLoginActivity.this)){
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
        setContentView(R.layout.activity_driver_login);

        back = (Button) findViewById(R.id.back_buttonDriverLogin);
        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        rememberMe = (CheckBox) findViewById(R.id.rememberMe);
        enter = (Button) findViewById(R.id.loginButton);

        sharedPreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean("rememberMe", false)){
            Intent intent = new Intent(DriverLoginActivity.this, DriverActivityTags.class);
            startActivity(intent);
            finish();
        }

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

        url = sharedPreferences.getString("url", "http://track.micro-gis.com/api/");

        api = APIController.getApi(url);


        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NumberUtils.isParsable(login.getText().toString())){

                    RequestCheckPassword requestCheckPassword = new RequestCheckPassword();
                    requestCheckPassword.setLogin(Integer.parseInt(login.getText().toString()));
                    requestCheckPassword.setPassword(password.getText().toString());

                    api.responseCheckPassword(requestCheckPassword).enqueue(new Callback<ResponseCheckPassword>() {
                        @Override
                        public void onResponse(Call<ResponseCheckPassword> call, Response<ResponseCheckPassword> response) {
                            ResponseCheckPassword responseCheckPassword = response.body();

                            if (responseCheckPassword != null){
                                if (responseCheckPassword.getStatus().equals(ResponseStatuses.INCORRECT_PASSWORD.toString())){
                                    Toast toast = Toast.makeText(DriverLoginActivity.this,
                                            getString(R.string.incorrect_password), Toast.LENGTH_LONG);
                                    toast.show();
                                } else if (responseCheckPassword.getStatus().equals(ResponseStatuses.SUCCESS.toString())){
                                    if (rememberMe.isChecked()){
                                        sharedPreferences.edit().putBoolean("rememberMe", true).apply();
                                    }
                                    sharedPreferences.edit().putBoolean("loginIn", true).apply();
                                    sharedPreferences.edit().putInt("driverLogin", Integer.parseInt(login.getText().toString())).apply();
                                    sharedPreferences.edit().putString("driverPassword", password.getText().toString()).apply();

                                    Intent intent = new Intent(DriverLoginActivity.this, DriverActivityTags.class);
                                    startActivity(intent);

                                    Intent service = new Intent(DriverLoginActivity.this, CheckNotificationService.class);
                                    service.putExtra("driverLogin", Integer.parseInt(login.getText().toString()));
                                    service.putExtra("driverPassword", password.getText().toString());
                                    startService(service);
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseCheckPassword> call, Throwable t) {

                        }
                    });
                } else {
                    Toast toast = Toast.makeText(DriverLoginActivity.this,
                            getString(R.string.login_empty), Toast.LENGTH_LONG);
                    toast.show();
                }
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
    }
}
