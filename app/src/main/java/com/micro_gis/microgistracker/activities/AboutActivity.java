package com.micro_gis.microgistracker.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.micro_gis.microgistracker.R;

/**
 * Created by User9 on 29.03.2018.
 */

public class AboutActivity extends AppCompatActivity{

    private Button back;
    private TextView url;
    private TextView build;
    private TextView support;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        back = (Button) findViewById(R.id.back_buttonAbout);
        url = (TextView) findViewById(R.id.author_site);
        build = (TextView) findViewById(R.id.build_version);
        support = (TextView) findViewById(R.id.support);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getString(R.string.microgis_site)));
                startActivity(i);
            }
        });

        String version = null;

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (version != null){
            build.setText(version);
        }

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, "support@micro-gis.com");

                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

    }
}
