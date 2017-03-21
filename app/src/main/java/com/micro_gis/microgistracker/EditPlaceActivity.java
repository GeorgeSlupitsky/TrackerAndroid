package com.micro_gis.microgistracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;

import java.util.ArrayList;

public class EditPlaceActivity extends AppCompatActivity implements View.OnClickListener {
    static Marker marker;
    static String url, shadowUrl;
    ArrayList<ImageButton> imageButtons = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_place);
        Button save =(Button)findViewById(R.id.add_marker_edit);
        final EditText des = (EditText)findViewById(R.id.marker_description_edit);
        final EditText name =(EditText)findViewById(R.id.get_name_place_edit);
        des.setText(marker.getDescription());
        name.setText(marker.getName());
        url=marker.getUrl();
        shadowUrl=marker.getShadowUrl();
        DBHelper dbHelper = new DBHelper(this);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues cv = new ContentValues();
        ImageButton f1_b_2x =(ImageButton) findViewById(R.id.f1_b_2x1);
        ImageButton f1_r_2x =(ImageButton) findViewById(R.id.f1_r_2x1);
        ImageButton f1_y_2x =(ImageButton) findViewById(R.id.f1_y_2x1);
        ImageButton f1_g_2x =(ImageButton) findViewById(R.id.f1_g_2x1);
        ImageButton f2_b_2x =(ImageButton) findViewById(R.id.f2_b_2x1);
        ImageButton f2_r_2x =(ImageButton) findViewById(R.id.f2_r_2x1);
        ImageButton f2_y_2x =(ImageButton) findViewById(R.id.f2_y_2x1);
        ImageButton f2_g_2x =(ImageButton) findViewById(R.id.f2_g_2x1);
        ImageButton f3_b_2x =(ImageButton) findViewById(R.id.f3_b_2x1);
        ImageButton f3_r_2x =(ImageButton) findViewById(R.id.f3_r_2x1);
        ImageButton f3_y_2x =(ImageButton) findViewById(R.id.f3_y_2x1);
        ImageButton f3_g_2x =(ImageButton) findViewById(R.id.f3_g_2x1);
        ImageButton f4_b_2x =(ImageButton) findViewById(R.id.f4_b_2x1);
        ImageButton f4_r_2x =(ImageButton) findViewById(R.id.f4_r_2x1);
        ImageButton f4_y_2x =(ImageButton) findViewById(R.id.f4_y_2x1);
        ImageButton f4_g_2x =(ImageButton) findViewById(R.id.f4_g_2x1);
        ImageButton p_b_2x =(ImageButton) findViewById(R.id.p_b_2x1);
        ImageButton p_r_2x =(ImageButton) findViewById(R.id.p_r_2x1);
        ImageButton p_y_2x =(ImageButton) findViewById(R.id.p_y_2x1);
        ImageButton p_g_2x =(ImageButton) findViewById(R.id.p_g_2x1);
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
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marker.setName(name.getText().toString());
                marker.setDescription(des.getText().toString());
                marker.setShadowUrl(shadowUrl);
                marker.setUrl(url);
                Gson gson = new Gson();
                cv.put("latlng",marker.getLatlng());
                cv.put("data", gson.toJson(marker).getBytes());
                String[] args = new String[]{marker.getLatlng()};
                db.update("markers",cv, "latlng = ?", args);
                finish();
            }
        });

    }

    @Override
    public void onClick(View v) {
        {
            switch (v.getId()) {
                case R.id.f1_b_2x1:
                    for (ImageButton b: imageButtons) {
                        if(b.getId()==R.id.f1_b_2x1){
                            b.setBackgroundColor(Color.rgb(159, 207,248));
                        }else{
                            b.setBackgroundColor(Color.rgb(255,255,255));
                        }
                    }
                    url="file:///android_res/drawable/f1_b_2x.png";
                    shadowUrl="file:///android_res/drawable/f1_s_2x.png";
                    break;

                case R.id.f1_g_2x1:
                    for (ImageButton b: imageButtons) {
                        if(b.getId()==R.id.f1_g_2x1){
                            b.setBackgroundColor(Color.rgb(159, 207,248));
                        }else{
                            b.setBackgroundColor(Color.rgb(255,255,255));
                        }
                    }
                    url="file:///android_res/drawable/f1_g_2x.png";
                    shadowUrl="file:///android_res/drawable/f1_s_2x.png";
                    break;

                case R.id.f1_r_2x1:
                    for (ImageButton b: imageButtons) {
                        if(b.getId()==R.id.f1_r_2x1){
                            b.setBackgroundColor(Color.rgb(159, 207,248));
                        }else{
                            b.setBackgroundColor(Color.rgb(255,255,255));
                        }
                    }
                    url="file:///android_res/drawable/f1_r_2x.png";
                    shadowUrl="file:///android_res/drawable/f1_s_2x.png";
                    break;

                case R.id.f1_y_2x1:
                    for (ImageButton b: imageButtons) {
                        if(b.getId()==R.id.f1_y_2x1){
                            b.setBackgroundColor(Color.rgb(159, 207,248));
                        }else{
                            b.setBackgroundColor(Color.rgb(255,255,255));
                        }
                    }
                    url="file:///android_res/drawable/f1_y_2x.png";
                    shadowUrl="file:///android_res/drawable/f1_s_2x.png";

                    break;

                case R.id.f2_b_2x1:
                    for (ImageButton b: imageButtons) {
                        if(b.getId()==R.id.f2_b_2x1){
                            b.setBackgroundColor(Color.rgb(159, 207,248));
                        }else{
                            b.setBackgroundColor(Color.rgb(255,255,255));
                        }
                    }
                    url="file:///android_res/drawable/f2_b_2x.png";
                    shadowUrl="file:///android_res/drawable/f2_s_2x.png";
                    break;

                case R.id.f2_g_2x1:
                    for (ImageButton b: imageButtons) {
                        if(b.getId()==R.id.f2_g_2x1){
                            b.setBackgroundColor(Color.rgb(159, 207,248));
                        }else{
                            b.setBackgroundColor(Color.rgb(255,255,255));
                        }
                    }

                    url="file:///android_res/drawable/f2_g_2x.png";
                    shadowUrl="file:///android_res/drawable/f2_s_2x.png";
                    break;

                case R.id.f2_r_2x1:

                    for (ImageButton b: imageButtons) {
                        if(b.getId()==R.id.f2_r_2x1){
                            b.setBackgroundColor(Color.rgb(159, 207,248));
                        }else{
                            b.setBackgroundColor(Color.rgb(255,255,255));
                        }
                    }
                    url="file:///android_res/drawable/f2_r_2x.png";
                    shadowUrl="file:///android_res/drawable/f2_s_2x.png";
                    break;
                case R.id.f2_y_2x1:
                    for (ImageButton b: imageButtons) {
                        if(b.getId()==R.id.f2_y_2x1){
                            b.setBackgroundColor(Color.rgb(159, 207,248));
                        }else{
                            b.setBackgroundColor(Color.rgb(255,255,255));
                        }
                    }

                    url="file:///android_res/drawable/f2_y_2x.png";
                    shadowUrl="file:///android_res/drawable/f2_s_2x.png";
                    break;
                case R.id.f3_b_2x1:
                    for (ImageButton b: imageButtons) {
                        if(b.getId()==R.id.f3_b_2x1){
                            b.setBackgroundColor(Color.rgb(159, 207,248));
                        }else{
                            b.setBackgroundColor(Color.rgb(255,255,255));
                        }
                    }
                    url="file:///android_res/drawable/f3_b_2x.png";
                    shadowUrl="file:///android_res/drawable/f3_s_2x.png";
                    break;

                case R.id.f3_g_2x1:
                    for (ImageButton b: imageButtons) {
                        if(b.getId()==R.id.f3_g_2x1){
                            b.setBackgroundColor(Color.rgb(159, 207,248));
                        }else{
                            b.setBackgroundColor(Color.rgb(255,255,255));
                        }
                    }

                    url="file:///android_res/drawable/f3_g_2x.png";
                    shadowUrl="file:///android_res/drawable/f3_s_2x.png";
                    break;

                case R.id.f3_r_2x1:
                    for (ImageButton b: imageButtons) {
                        if(b.getId()==R.id.f3_r_2x1){
                            b.setBackgroundColor(Color.rgb(159, 207,248));
                        }else{
                            b.setBackgroundColor(Color.rgb(255,255,255));
                        }
                    }

                    url="file:///android_res/drawable/f3_r_2x.png";
                    shadowUrl="file:///android_res/drawable/f3_s_2x.png";
                    break;
                case R.id.f3_y_2x1:
                    for (ImageButton b: imageButtons) {
                        if(b.getId()==R.id.f3_y_2x1){
                            b.setBackgroundColor(Color.rgb(159, 207,248));
                        }else{
                            b.setBackgroundColor(Color.rgb(255,255,255));
                        }
                    }
                    url="file:///android_res/drawable/f3_y_2x.png";
                    shadowUrl="file:///android_res/drawable/f3_s_2x.png";
                    break;
                case R.id.f4_b_2x1:
                    for (ImageButton b: imageButtons) {
                        if(b.getId()==R.id.f4_b_2x1){
                            b.setBackgroundColor(Color.rgb(159, 207,248));
                        }else{
                            b.setBackgroundColor(Color.rgb(255,255,255));
                        }
                    }
                    url="file:///android_res/drawable/f4_b_2x.png";
                    shadowUrl="file:///android_res/drawable/f4_s_2x.png";
                    break;

                case R.id.f4_g_2x1:
                    for (ImageButton b: imageButtons) {
                        if(b.getId()==R.id.f4_g_2x1){
                            b.setBackgroundColor(Color.rgb(159, 207,248));
                        }else{
                            b.setBackgroundColor(Color.rgb(255,255,255));
                        }
                    }

                    url="file:///android_res/drawable/f4_g_2x.png";
                    shadowUrl="file:///android_res/drawable/f4_s_2x.png";
                    break;

                case R.id.f4_r_2x1:
                    for (ImageButton b: imageButtons) {
                        if(b.getId()==R.id.f4_r_2x1){
                            b.setBackgroundColor(Color.rgb(159, 207,248));
                        }else{
                            b.setBackgroundColor(Color.rgb(255,255,255));
                        }
                    }

                    url="file:///android_res/drawable/f4_r_2x.png";
                    shadowUrl="file:///android_res/drawable/f4_s_2x.png";
                    break;
                case R.id.f4_y_2x1:
                    for (ImageButton b: imageButtons) {
                        if(b.getId()==R.id.f4_y_2x1){
                            b.setBackgroundColor(Color.rgb(159, 207,248));
                        }else{
                            b.setBackgroundColor(Color.rgb(255,255,255));
                        }
                    }
                    url="file:///android_res/drawable/f4_y_2x.png";
                    shadowUrl="file:///android_res/drawable/f4_s_2x.png";
                    break;
                case R.id.p_b_2x1:
                    for (ImageButton b: imageButtons) {
                        if(b.getId()==R.id.p_b_2x1){
                            b.setBackgroundColor(Color.rgb(159, 207,248));
                        }else{
                            b.setBackgroundColor(Color.rgb(255,255,255));
                        }
                    }
                    url="file:///android_res/drawable/p_b_2x.png";
                    shadowUrl="file:///android_res/drawable/p_s_2x.png";
                    break;

                case R.id.p_g_2x1:
                    for (ImageButton b: imageButtons) {
                        if(b.getId()==R.id.p_g_2x1){
                            b.setBackgroundColor(Color.rgb(159, 207,248));
                        }else{
                            b.setBackgroundColor(Color.rgb(255,255,255));
                        }
                    }

                    url="file:///android_res/drawable/p_g_2x.png";
                    shadowUrl="file:///android_res/drawable/p_s_2x.png";
                    break;

                case R.id.p_r_2x1:
                    for (ImageButton b: imageButtons) {
                        if(b.getId()==R.id.p_r_2x1){
                            b.setBackgroundColor(Color.rgb(159, 207,248));
                        }else{
                            b.setBackgroundColor(Color.rgb(255,255,255));
                        }
                    }

                    url="file:///android_res/drawable/p_r_2x.png";
                    shadowUrl="file:///android_res/drawable/p_s_2x.png";
                    break;
                case R.id.p_y_2x1:
                    for (ImageButton b: imageButtons) {
                        if(b.getId()==R.id.p_y_2x1){
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
    }
}
