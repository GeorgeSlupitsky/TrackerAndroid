package com.micro_gis.microgistracker;

import android.app.Activity;
import android.app.Application;

import com.micro_gis.microgistracker.components.DaggerMicroGisComponent;
import com.micro_gis.microgistracker.components.MicroGisComponent;
import com.micro_gis.microgistracker.modules.ContextModule;

/**
 * Created by User9 on 13.03.2018.
 */

public class MicroGisApplication extends Application {

    private MicroGisComponent microGisComponent;

    public static MicroGisApplication get(Activity activity){
        return (MicroGisApplication) activity.getApplication();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        microGisComponent = DaggerMicroGisComponent.builder().contextModule(new ContextModule(this)).build();

    }

    public MicroGisComponent getMicroGisComponent() {
        return microGisComponent;
    }
}
