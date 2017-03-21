package com.micro_gis.microgistracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TimeZone;

public class RequestActivity extends AppCompatActivity implements View.OnClickListener {

    EditText accaunt, key, interval, url, group, timestamp;
    static String icon;
    SharedPreferences sharedpreferences;
    ArrayList<ImageButton> imageButtons = new ArrayList<>();
    Spinner TZone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        accaunt =(EditText) findViewById(R.id.accaunt);
        key =(EditText) findViewById(R.id.req_key);
        interval =(EditText) findViewById(R.id.req_interval);
        url =(EditText) findViewById(R.id.req_url);
        group =(EditText) findViewById(R.id.req_group);
        Button save = (Button)findViewById(R.id.req_save);
        icon="bus_";

        sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);

        accaunt.setText(sharedpreferences.getString("accaunt", ""));
        key.setText(sharedpreferences.getString("keyreq", ""));
        interval.setText(sharedpreferences.getString("intervalreq", "10"));
        url.setText(sharedpreferences.getString("urlreq", ""));
        group.setText(sharedpreferences.getString("group", ""));


        ImageButton one =(ImageButton) findViewById(R.id.imageOne);
        ImageButton two =(ImageButton) findViewById(R.id.imageTwo);
        ImageButton three =(ImageButton) findViewById(R.id.imageThree);
        ImageButton four =(ImageButton) findViewById(R.id.imageFour);
        ImageButton fife =(ImageButton) findViewById(R.id.imageFife);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        fife.setOnClickListener(this);

        imageButtons.add(one);
        imageButtons.add(two);
        imageButtons.add(three);
        imageButtons.add(four);
        imageButtons.add(fife);

        assert save != null;


        ArrayAdapter <CharSequence> adapter =
                new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        String[]TZ = new String[24];
        ArrayList<String> TZ1 = new ArrayList<String>();
        for(int i =0 ; i<25;i++){
            TZ1.add("0");
        }
        for(int i = 0; i < 13; i++) {
//            if(!(TZ1.contains(TimeZone.getTimeZone(TZ[i]).getDisplayName()))) {
//                TZ1.add(TimeZone.getTimeZone(TZ[i]).getDisplayName());
//            }
            TZ1.set(i,"GMT+"+i);
            TZ1.set(12+i,"GMT-"+i);
        }
        for(int i = 0; i < TZ1.size(); i++) {
            adapter.add(TZ1.get(i));
        }
        final Spinner TZone = (Spinner)findViewById(R.id.spinner);
        TZone.setAdapter(adapter);
        int k=2;
        for (int i =0; i<TZ1.size();i++){
            if(TZ1.get(i).equals(sharedpreferences.getString("tzone", "GMT+2"))){
                k=i;
            }
        }
        TZone.setSelection(k);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = TZone.getSelectedItem().toString();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("accaunt", accaunt.getText().toString());
                editor.putString("keyreq", key.getText().toString());
                editor.putString("intervalreq", interval.getText().toString());
                editor.putString("tzone", text);
                editor.putString("urlreq", url.getText().toString());
                editor.putString("group", group.getText().toString());
                editor.apply();
                finish();
            }
        });
        //        for(int i = 0; i < TZ1.size(); i++) {
//            if(TZ1.get(i).equals(TimeZone.getDefault().getDisplayName())) {
//                TZone.setSelection(i);
//            }
//        }
    }


    @Override
    public void onBackPressed() {

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("accaunt", accaunt.getText().toString());
        editor.putString("keyreq", key.getText().toString());
        editor.putString("intervalreq", interval.getText().toString());
        editor.putString("urlreq", url.getText().toString());
        editor.putString("group", group.getText().toString());
        editor.apply();
        finish();
    }


    @Override
    public void onClick(View v) {
        SharedPreferences.Editor editor = sharedpreferences.edit();


        switch (v.getId()) {
            case R.id.imageOne:
                for (ImageButton b: imageButtons) {
                    if(b.getId()==R.id.imageOne){
                        b.setBackgroundColor(Color.rgb(159, 207,248));
                    }else{
                        b.setBackgroundColor(Color.rgb(255,255,255));
                    }
                }
                editor.putString("iconkey", "bus_");
                editor.apply();

                break;

            case R.id.imageTwo:
                for (ImageButton b: imageButtons) {
                    if(b.getId()==R.id.imageTwo){
                        b.setBackgroundColor(Color.rgb(159, 207,248));
                    }else{
                        b.setBackgroundColor(Color.rgb(255,255,255));
                    }
                }
                editor.putString("iconkey", "truck_");
                editor.apply();
                break;

            case R.id.imageThree:
                for (ImageButton b: imageButtons) {
                    if(b.getId()==R.id.imageThree){
                        b.setBackgroundColor(Color.rgb(159, 207,248));
                    }else{
                        b.setBackgroundColor(Color.rgb(255,255,255));
                    }
                }
                editor.putString("iconkey", "car_");
                editor.apply();
                break;

            case R.id.imageFour:
                for (ImageButton b: imageButtons) {
                    if(b.getId()==R.id.imageFour){
                        b.setBackgroundColor(Color.rgb(159, 207,248));
                    }else{
                        b.setBackgroundColor(Color.rgb(255,255,255));
                    }
                }
                editor.putString("iconkey", "worker_");
                editor.apply();

                break;

            case R.id.imageFife:
                for (ImageButton b: imageButtons) {
                    if(b.getId()==R.id.imageFife){
                        b.setBackgroundColor(Color.rgb(159, 207,248));
                    }else{
                        b.setBackgroundColor(Color.rgb(255,255,255));
                    }
                }
                editor.putString("iconkey", "tractor_");
                editor.apply();

                break;
            default:
                break;
    }
    }
}
