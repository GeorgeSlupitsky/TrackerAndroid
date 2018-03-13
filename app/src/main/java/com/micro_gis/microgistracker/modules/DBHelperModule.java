package com.micro_gis.microgistracker.modules;

import android.content.Context;

import com.micro_gis.microgistracker.DBHelper;
import com.micro_gis.microgistracker.scopes.MicroGisApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by User9 on 13.03.2018.
 */

@Module (includes = ContextModule.class)
public class DBHelperModule {

    @MicroGisApplicationScope
    @Provides
    public DBHelper dbHelper(Context context){
        return new DBHelper(context);
    }

}
