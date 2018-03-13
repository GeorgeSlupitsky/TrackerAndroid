package com.micro_gis.microgistracker.modules;

import android.content.Context;

import com.micro_gis.microgistracker.scopes.MicroGisApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by User9 on 13.03.2018.
 */

@Module
public class ContextModule {

    private Context context;

    public ContextModule(Context context){
        this.context = context;
    }

    @Provides
    @MicroGisApplicationScope
    public Context context(){
        return context.getApplicationContext();
    }

}
