package com.micro_gis.microgistracker.components;

import com.micro_gis.microgistracker.activities.MicroGisActivity;
import com.micro_gis.microgistracker.modules.APIModule;
import com.micro_gis.microgistracker.modules.MicroGisActivityModule;
import com.micro_gis.microgistracker.scopes.MicroGisActivityScope;

import dagger.Component;

/**
 * Created by User9 on 13.03.2018.
 */

@Component (dependencies = MicroGisComponent.class, modules = {MicroGisActivityModule.class, APIModule.class})
@MicroGisActivityScope
public interface MicroGisActivityComponent {

    void injectMicroGisActivity(MicroGisActivity microGisActivity);

}
