package com.micro_gis.microgistracker.components;

import com.micro_gis.microgistracker.activities.ObjectsActivity;
import com.micro_gis.microgistracker.modules.ObjectsActivityModule;
import com.micro_gis.microgistracker.scopes.ObjectsActivityScope;

import dagger.Component;

/**
 * Created by User9 on 13.03.2018.
 */

@Component(dependencies = MicroGisComponent.class, modules = ObjectsActivityModule.class)
@ObjectsActivityScope
public interface ObjectsActivityComponent {

    void injectObjectsActivity(ObjectsActivity objectsActivity);

}
