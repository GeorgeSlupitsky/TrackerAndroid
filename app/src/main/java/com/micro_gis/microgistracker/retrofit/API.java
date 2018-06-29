package com.micro_gis.microgistracker.retrofit;

import com.micro_gis.microgistracker.models.rest.RequestCheckPassword;
import com.micro_gis.microgistracker.models.rest.RequestDetailTrip;
import com.micro_gis.microgistracker.models.rest.RequestDeviceStatus;
import com.micro_gis.microgistracker.models.rest.RequestDriverEvents;
import com.micro_gis.microgistracker.models.rest.RequestGroupsMoving;
import com.micro_gis.microgistracker.models.rest.RequestObjectMoving;
import com.micro_gis.microgistracker.models.rest.RequestObjectTrip;
import com.micro_gis.microgistracker.models.rest.RequestRoute;
import com.micro_gis.microgistracker.models.rest.RequestSaveStatus;
import com.micro_gis.microgistracker.models.rest.RequestStatusCode;
import com.micro_gis.microgistracker.models.rest.RequestVoyageStatus;
import com.micro_gis.microgistracker.models.rest.ResponseCheckPassword;
import com.micro_gis.microgistracker.models.rest.ResponseDetailTrip;
import com.micro_gis.microgistracker.models.rest.ResponseDeviceStatus;
import com.micro_gis.microgistracker.models.rest.ResponseDriverEvents;
import com.micro_gis.microgistracker.models.rest.ResponseGroupsMoving;
import com.micro_gis.microgistracker.models.rest.ResponseObjectMoving;
import com.micro_gis.microgistracker.models.rest.ResponseObjectTrip;
import com.micro_gis.microgistracker.models.rest.ResponseRoute;
import com.micro_gis.microgistracker.models.rest.ResponseSaveStatus;
import com.micro_gis.microgistracker.models.rest.ResponseStatusCode;
import com.micro_gis.microgistracker.models.rest.ResponseVoyageStatus;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by postp on 24.02.2018.
 */

public interface API {

    @POST("/api/moving")
    Call<ResponseGroupsMoving> responseGroupsMoving(@Body RequestGroupsMoving requestGroupsMoving);

    @POST("/api/object")
    Call<ResponseObjectMoving> responseObjectsMoving(@Body RequestObjectMoving requestObjectsMoving);

    @POST("/api/trips")
    Call<ResponseObjectTrip> responseObjectsTrip(@Body RequestObjectTrip requestObjectsTrip);

    @POST("/api/detailTrip")
    Call<ResponseDetailTrip> responseDetailTrip(@Body RequestDetailTrip requestDetailTrip);

    @POST("/api/getStatusCode")
    Call<ResponseStatusCode> responseStatusCode(@Body RequestStatusCode requestStatusCode);

    @POST("/api/deviceStatus")
    Call<ResponseDeviceStatus> responseDeviceStatus(@Body RequestDeviceStatus requestDeviceStatus);

    @POST("/api/saveStatus")
    Call<ResponseSaveStatus> saveStatus(@Body RequestSaveStatus requestSaveStatus);

    @POST("/api/driverEvents")
    Call<ResponseDriverEvents> responseDriverEvents(@Body RequestDriverEvents requestDriverEvents);

    @POST("/api/route")
    Call<ResponseRoute> responseRoute(@Body RequestRoute requestRoute);

    @POST("/api/checkPassword")
    Call<ResponseCheckPassword> responseCheckPassword(@Body RequestCheckPassword requestCheckPassword);

    @POST("/api/voyageStatus")
    Call<ResponseVoyageStatus> responseVoyageStatus(@Body RequestVoyageStatus requestVoyageStatus);
}
