package com.micro_gis.microgistracker.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.micro_gis.microgistracker.DBHelper;
import com.micro_gis.microgistracker.Power;
import com.micro_gis.microgistracker.models.database.Marker;
import com.micro_gis.microgistracker.R;

import java.util.ArrayList;

public class AddMarkerActivity extends AppCompatActivity implements View.OnClickListener{
    private DBHelper dbHelper;
    private String name;
    private static String url, shadowUrl;
    private ArrayList<ImageButton> imageButtons = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private Handler handler = new Handler();

    Runnable checkCharging = new Runnable() {
        @Override
        public void run() {
            if (Power.isConnected(AddMarkerActivity.this)){
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
        setContentView(R.layout.activity_add_marker);
        final Button back = (Button) findViewById(R.id.back_buttonAP);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button button =(Button)findViewById(R.id.add_marker);
        dbHelper = new DBHelper(this);

        sharedPreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);

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

        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues cv = new ContentValues();
        final EditText getname =(EditText)findViewById(R.id.get_name_place);
        final EditText des =(EditText)findViewById(R.id.marker_description);

        ImageButton f1_b_2x =(ImageButton) findViewById(R.id.f1_b_2x);
        ImageButton f1_r_2x =(ImageButton) findViewById(R.id.f1_r_2x);
        ImageButton f1_y_2x =(ImageButton) findViewById(R.id.f1_y_2x);
        ImageButton f1_g_2x =(ImageButton) findViewById(R.id.f1_g_2x);
        ImageButton f2_b_2x =(ImageButton) findViewById(R.id.f2_b_2x);
        ImageButton f2_r_2x =(ImageButton) findViewById(R.id.f2_r_2x);
        ImageButton f2_y_2x =(ImageButton) findViewById(R.id.f2_y_2x);
        ImageButton f2_g_2x =(ImageButton) findViewById(R.id.f2_g_2x);
        ImageButton f3_b_2x =(ImageButton) findViewById(R.id.f3_b_2x);
        ImageButton f3_r_2x =(ImageButton) findViewById(R.id.f3_r_2x);
        ImageButton f3_y_2x =(ImageButton) findViewById(R.id.f3_y_2x);
        ImageButton f3_g_2x =(ImageButton) findViewById(R.id.f3_g_2x);
        ImageButton f4_b_2x =(ImageButton) findViewById(R.id.f4_b_2x);
        ImageButton f4_r_2x =(ImageButton) findViewById(R.id.f4_r_2x);
        ImageButton f4_y_2x =(ImageButton) findViewById(R.id.f4_y_2x);
        ImageButton f4_g_2x =(ImageButton) findViewById(R.id.f4_g_2x);
        ImageButton p_b_2x =(ImageButton) findViewById(R.id.p_b_2x);
        ImageButton p_r_2x =(ImageButton) findViewById(R.id.p_r_2x);
        ImageButton p_y_2x =(ImageButton) findViewById(R.id.p_y_2x);
        ImageButton p_g_2x =(ImageButton) findViewById(R.id.p_g_2x);
        f1_b_2x.setOnClickListener(this);
        f1_r_2x.setOnClickListener(this);
        f1_y_2x.setOnClickListener(this);
        f1_g_2x.setOnClickListener(this);
        f2_b_2x.setOnClickListener(this);
        f2_r_2x.setOnClickListener(this);
        f2_y_2x.setOnClickListener(this);
        f2_g_2x.setOnClickListener(this);
        f3_b_2x.setOnClickListener(this);
        f3_r_2x.setOnClickListener(this);
        f3_y_2x.setOnClickListener(this);
        f3_g_2x.setOnClickListener(this);
        f4_b_2x.setOnClickListener(this);
        f4_r_2x.setOnClickListener(this);
        f4_y_2x.setOnClickListener(this);
        f4_g_2x.setOnClickListener(this);
        p_b_2x.setOnClickListener(this);
        p_r_2x.setOnClickListener(this);
        p_y_2x.setOnClickListener(this);
        p_g_2x.setOnClickListener(this);


        imageButtons.add(f1_b_2x);
        imageButtons.add(f1_r_2x);
        imageButtons.add(f1_y_2x);
        imageButtons.add(f1_g_2x);
        imageButtons.add(f2_b_2x);
        imageButtons.add(f2_r_2x);
        imageButtons.add(f2_y_2x);
        imageButtons.add(f2_g_2x);
        imageButtons.add(f3_b_2x);
        imageButtons.add(f3_r_2x);
        imageButtons.add(f3_y_2x);
        imageButtons.add(f3_g_2x);
        imageButtons.add(f4_b_2x);
        imageButtons.add(f4_r_2x);
        imageButtons.add(f4_y_2x);
        imageButtons.add(f4_g_2x);
        imageButtons.add(p_b_2x);
        imageButtons.add(p_r_2x);
        imageButtons.add(p_y_2x);
        imageButtons.add(p_g_2x);
        url="file:///android_res/drawable/f1_b_2x.png";
        shadowUrl="file:///android_res/drawable/f1_s_2x.png";
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                String cor = getIntent().getStringExtra("latlng");
                final Marker marker = new Marker();
                        name = getname.getText().toString();
                        marker.setName(name);
                        marker.setUrl(url);
                        marker.setShadowUrl(shadowUrl);
                        marker.setLatlng(cor);
                        marker.setDescription(des.getText().toString());
                        Gson gson = new Gson();
                        cv.put("latlng",cor);
                        cv.put("data", gson.toJson(marker).getBytes());
                        db.insert("markers", null, cv);
//                        MicroGisActivity.myWebView.loadUrl("javascript:savePlace("+cor+","+ name + ");");
                        MicroGisActivity.myWebView.loadUrl("javascript: \n" +
                        "var FlIcon = L.Icon.Default.extend({\n" +
                        "            options: {\n" +
                        "            \t    iconUrl: '"+url+"',\n" +
                        "            \t      shadowUrl: '"+shadowUrl+"'" +
                        "}" +
                        "         });\n" +
                        "         var flIcon = new FlIcon();\n" +
                        "\n" +
                        "  var popup = L.popup()\n" +
                        "    .setContent('"+name+"');        var custom_adapter_marker = L.marker("+cor+", {icon: flIcon}).bindPopup(popup).addTo(map);");

                MicroGisActivity.myWebView.loadUrl("javascript:map.removeLayer(mitka);");
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.f1_b_2x:
                for (ImageButton b: imageButtons) {
                    if(b.getId()==R.id.f1_b_2x){
                        b.setBackgroundColor(Color.rgb(159, 207,248));
                    }else{
                        b.setBackgroundColor(Color.rgb(255,255,255));
                    }
                }
                url="file:///android_res/drawable/f1_b_2x.png";
                shadowUrl="file:///android_res/drawable/f1_s_2x.png";
                break;

            case R.id.f1_g_2x:
                for (ImageButton b: imageButtons) {
                    if(b.getId()==R.id.f1_g_2x){
                        b.setBackgroundColor(Color.rgb(159, 207,248));
                    }else{
                        b.setBackgroundColor(Color.rgb(255,255,255));
                    }
                }
                url="file:///android_res/drawable/f1_g_2x.png";
                shadowUrl="file:///android_res/drawable/f1_s_2x.png";
                break;

            case R.id.f1_r_2x:
                for (ImageButton b: imageButtons) {
                    if(b.getId()==R.id.f1_r_2x){
                        b.setBackgroundColor(Color.rgb(159, 207,248));
                    }else{
                        b.setBackgroundColor(Color.rgb(255,255,255));
                    }
                }
                url="file:///android_res/drawable/f1_r_2x.png";
                shadowUrl="file:///android_res/drawable/f1_s_2x.png";
                break;

            case R.id.f1_y_2x:
                for (ImageButton b: imageButtons) {
                    if(b.getId()==R.id.f1_y_2x){
                        b.setBackgroundColor(Color.rgb(159, 207,248));
                    }else{
                        b.setBackgroundColor(Color.rgb(255,255,255));
                    }
                }
                url="file:///android_res/drawable/f1_y_2x.png";
                shadowUrl="file:///android_res/drawable/f1_s_2x.png";

                break;

            case R.id.f2_b_2x:
                for (ImageButton b: imageButtons) {
                    if(b.getId()==R.id.f2_b_2x){
                        b.setBackgroundColor(Color.rgb(159, 207,248));
                    }else{
                        b.setBackgroundColor(Color.rgb(255,255,255));
                    }
                }
                url="file:///android_res/drawable/f2_b_2x.png";
                shadowUrl="file:///android_res/drawable/f2_s_2x.png";
                break;

            case R.id.f2_g_2x:
                for (ImageButton b: imageButtons) {
                    if(b.getId()==R.id.f2_g_2x){
                        b.setBackgroundColor(Color.rgb(159, 207,248));
                    }else{
                        b.setBackgroundColor(Color.rgb(255,255,255));
                    }
                }

                url="file:///android_res/drawable/f2_g_2x.png";
                shadowUrl="file:///android_res/drawable/f2_s_2x.png";
                break;

            case R.id.f2_r_2x:

                for (ImageButton b: imageButtons) {
                    if(b.getId()==R.id.f2_r_2x){
                        b.setBackgroundColor(Color.rgb(159, 207,248));
                    }else{
                        b.setBackgroundColor(Color.rgb(255,255,255));
                    }
                }
                url="file:///android_res/drawable/f2_r_2x.png";
                shadowUrl="file:///android_res/drawable/f2_s_2x.png";
                break;
            case R.id.f2_y_2x:
                for (ImageButton b: imageButtons) {
                    if(b.getId()==R.id.f2_y_2x){
                        b.setBackgroundColor(Color.rgb(159, 207,248));
                    }else{
                        b.setBackgroundColor(Color.rgb(255,255,255));
                    }
                }

                url="file:///android_res/drawable/f2_y_2x.png";
                shadowUrl="file:///android_res/drawable/f2_s_2x.png";
                break;
            case R.id.f3_b_2x:
                for (ImageButton b: imageButtons) {
                    if(b.getId()==R.id.f3_b_2x){
                        b.setBackgroundColor(Color.rgb(159, 207,248));
                    }else{
                        b.setBackgroundColor(Color.rgb(255,255,255));
                    }
                }
                url="file:///android_res/drawable/f3_b_2x.png";
                shadowUrl="file:///android_res/drawable/f3_s_2x.png";
                break;

            case R.id.f3_g_2x:
                for (ImageButton b: imageButtons) {
                    if(b.getId()==R.id.f3_g_2x){
                        b.setBackgroundColor(Color.rgb(159, 207,248));
                    }else{
                        b.setBackgroundColor(Color.rgb(255,255,255));
                    }
                }

                url="file:///android_res/drawable/f3_g_2x.png";
                shadowUrl="file:///android_res/drawable/f3_s_2x.png";
                break;

            case R.id.f3_r_2x:
                for (ImageButton b: imageButtons) {
                    if(b.getId()==R.id.f3_r_2x){
                        b.setBackgroundColor(Color.rgb(159, 207,248));
                    }else{
                        b.setBackgroundColor(Color.rgb(255,255,255));
                    }
                }

                url="file:///android_res/drawable/f3_r_2x.png";
                shadowUrl="file:///android_res/drawable/f3_s_2x.png";
                break;
            case R.id.f3_y_2x:
                for (ImageButton b: imageButtons) {
                    if(b.getId()==R.id.f3_y_2x){
                        b.setBackgroundColor(Color.rgb(159, 207,248));
                    }else{
                        b.setBackgroundColor(Color.rgb(255,255,255));
                    }
                }
                url="file:///android_res/drawable/f3_y_2x.png";
                shadowUrl="file:///android_res/drawable/f3_s_2x.png";
                break;
            case R.id.f4_b_2x:
                for (ImageButton b: imageButtons) {
                    if(b.getId()==R.id.f4_b_2x){
                        b.setBackgroundColor(Color.rgb(159, 207,248));
                    }else{
                        b.setBackgroundColor(Color.rgb(255,255,255));
                    }
                }
                url="file:///android_res/drawable/f4_b_2x.png";
                shadowUrl="file:///android_res/drawable/f4_s_2x.png";
                break;

            case R.id.f4_g_2x:
                for (ImageButton b: imageButtons) {
                    if(b.getId()==R.id.f4_g_2x){
                        b.setBackgroundColor(Color.rgb(159, 207,248));
                    }else{
                        b.setBackgroundColor(Color.rgb(255,255,255));
                    }
                }

                url="file:///android_res/drawable/f4_g_2x.png";
                shadowUrl="file:///android_res/drawable/f4_s_2x.png";
                break;

            case R.id.f4_r_2x:
                for (ImageButton b: imageButtons) {
                    if(b.getId()==R.id.f4_r_2x){
                        b.setBackgroundColor(Color.rgb(159, 207,248));
                    }else{
                        b.setBackgroundColor(Color.rgb(255,255,255));
                    }
                }

                url="file:///android_res/drawable/f4_r_2x.png";
                shadowUrl="file:///android_res/drawable/f4_s_2x.png";
                break;
            case R.id.f4_y_2x:
                for (ImageButton b: imageButtons) {
                    if(b.getId()==R.id.f4_y_2x){
                        b.setBackgroundColor(Color.rgb(159, 207,248));
                    }else{
                        b.setBackgroundColor(Color.rgb(255,255,255));
                    }
                }
                url="file:///android_res/drawable/f4_y_2x.png";
                shadowUrl="file:///android_res/drawable/f4_s_2x.png";
                break;
            case R.id.p_b_2x:
                for (ImageButton b: imageButtons) {
                    if(b.getId()==R.id.p_b_2x){
                        b.setBackgroundColor(Color.rgb(159, 207,248));
                    }else{
                        b.setBackgroundColor(Color.rgb(255,255,255));
                    }
                }
                url="file:///android_res/drawable/p_b_2x.png";
                shadowUrl="file:///android_res/drawable/p_s_2x.png";
                break;

            case R.id.p_g_2x:
                for (ImageButton b: imageButtons) {
                    if(b.getId()==R.id.p_g_2x){
                        b.setBackgroundColor(Color.rgb(159, 207,248));
                    }else{
                        b.setBackgroundColor(Color.rgb(255,255,255));
                    }
                }

                url="file:///android_res/drawable/p_g_2x.png";
                shadowUrl="file:///android_res/drawable/p_s_2x.png";
                break;

            case R.id.p_r_2x:
                for (ImageButton b: imageButtons) {
                    if(b.getId()==R.id.p_r_2x){
                        b.setBackgroundColor(Color.rgb(159, 207,248));
                    }else{
                        b.setBackgroundColor(Color.rgb(255,255,255));
                    }
                }

                url="file:///android_res/drawable/p_r_2x.png";
                shadowUrl="file:///android_res/drawable/p_s_2x.png";
                break;
            case R.id.p_y_2x:
                for (ImageButton b: imageButtons) {
                    if(b.getId()==R.id.p_y_2x){
                        b.setBackgroundColor(Color.rgb(159, 207,248));
                    }else{
                        b.setBackgroundColor(Color.rgb(255,255,255));
                    }
                }
                url="file:///android_res/drawable/p_y_2x.png";
                shadowUrl="file:///android_res/drawable/p_s_2x.png";
                break;

            default:
                break;
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(checkCharging);
    }
}
