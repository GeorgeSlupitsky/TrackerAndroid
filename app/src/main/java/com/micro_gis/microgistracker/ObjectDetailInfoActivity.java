package com.micro_gis.microgistracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by User3 on 19.02.2018.
 */

public class ObjectDetailInfoActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_detail_info);

        final Button back = (Button) findViewById(R.id.back_buttonObject);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        textView = (TextView) findViewById(R.id.ObjectId);

        Intent intent = getIntent();

        String id = intent.getStringExtra("id");

        textView.setText(id);

    }

}
