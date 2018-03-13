package com.micro_gis.microgistracker.modules;

import android.content.Context;
import android.content.SharedPreferences;

import com.micro_gis.microgistracker.scopes.MicroGisApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by User9 on 13.03.2018.
 */

@Module (includes = ContextModule.class)
public class SharedPreferencesModule {

    @Provides
    @MicroGisApplicationScope
    public SharedPreferences sharedPreferences(Context context){
        String myPref = "mypref";
        return context.getSharedPreferences(myPref, Context.MODE_PRIVATE);
    }

}
