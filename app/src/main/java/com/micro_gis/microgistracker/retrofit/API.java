package com.micro_gis.microgistracker.retrofit;

import com.micro_gis.microgistracker.models.RequestGroupsMoving;
import com.micro_gis.microgistracker.models.ResponseGroupsMoving;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by postp on 24.02.2018.
 */

public interface API {

    @POST("/api/moving")
    Call<ResponseGroupsMoving> responseGroupsMoving(@Body RequestGroupsMoving requestGroupsMoving);

}
