package com.micro_gis.microgistracker.modules;

import com.micro_gis.microgistracker.activities.ObjectsActivity;
import com.micro_gis.microgistracker.adapters.ObjectsCustomAdapter;
import com.micro_gis.microgistracker.scopes.ObjectsActivityScope;

import java.util.ArrayList;
import java.util.Map;

import dagger.Module;
import dagger.Provides;

/**
 * Created by User9 on 13.03.2018.
 */

@Module
public class ObjectsActivityModule {

    private ObjectsActivity objectsActivity;
    private ArrayList<Map<String, Object>> data;
    private int layoutResourceId;
    private String[] mFrom;


    public ObjectsActivityModule(ObjectsActivity objectsActivity, int resource, ArrayList<Map<String, Object>> data, String[] mFrom) {
        this.objectsActivity = objectsActivity;
        this.data = data;
        this.layoutResourceId = resource;
        this.mFrom = mFrom;
    }

    @Provides
    @ObjectsActivityScope
    public ObjectsCustomAdapter objectsCustomAdapter(){
        return new ObjectsCustomAdapter(objectsActivity, layoutResourceId, data, mFrom);
    }

}
