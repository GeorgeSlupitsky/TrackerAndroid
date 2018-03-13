package com.micro_gis.microgistracker.components;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.micro_gis.microgistracker.DBHelper;
import com.micro_gis.microgistracker.modules.DBHelperModule;
import com.micro_gis.microgistracker.modules.GsonModule;
import com.micro_gis.microgistracker.modules.SharedPreferencesModule;
import com.micro_gis.microgistracker.scopes.MicroGisApplicationScope;

import dagger.Component;

/**
 * Created by User9 on 12.03.2018.
 */

@MicroGisApplicationScope
@Component(modules = {SharedPreferencesModule.class, DBHelperModule.class, GsonModule.class})
public interface MicroGisComponent {

    SharedPreferences getSharedPreferences();
    DBHelper getDBHelper();
    Gson getGson();

}
