package com.micro_gis.microgistracker.modules;


import com.micro_gis.microgistracker.WebAppInterface;
import com.micro_gis.microgistracker.activities.MicroGisActivity;
import com.micro_gis.microgistracker.scopes.MicroGisActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by User9 on 13.03.2018.
 */

@Module
public class MicroGisActivityModule {

    private MicroGisActivity microGisActivity;

    public MicroGisActivityModule(MicroGisActivity microGisActivity) {
        this.microGisActivity = microGisActivity;
    }

    @Provides
    @MicroGisActivityScope
    public WebAppInterface webAppInterface(){
        return new WebAppInterface(microGisActivity);
    }
}
