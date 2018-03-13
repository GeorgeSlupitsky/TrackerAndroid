package com.micro_gis.microgistracker.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.micro_gis.microgistracker.scopes.MicroGisApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by User9 on 13.03.2018.
 */

@Module
public class GsonModule {

    @MicroGisApplicationScope
    @Provides
    public Gson gson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

}
