package com.micro_gis.microgistracker.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.micro_gis.microgistracker.R;
import com.micro_gis.microgistracker.adapters.TripsCustomAdapter;
import com.micro_gis.microgistracker.models.rest.RequestObjectTrip;
import com.micro_gis.microgistracker.models.rest.ResponseObjectMoving;
import com.micro_gis.microgistracker.models.rest.ResponseObjectTrip;
import com.micro_gis.microgistracker.models.rest.ResponseStatuses;
import com.micro_gis.microgistracker.models.rest.Trip;
import com.micro_gis.microgistracker.retrofit.API;
import com.micro_gis.microgistracker.retrofit.APIController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User9 on 06.03.2018.
 */

public class TripObjectFragment extends Fragment {

    final String ATTRIBUTE_NAME_TIME = "time";
    final String ATTRIBUTE_NAME_STATUS = "status";
    final String ATTRIBUTE_NAME_DURATION = "duration";
    final String ATTRIBUTE_NAME_DISTANCE = "distance";
    final String ATTRIBUTE_NAME_SPEED = "speed";
    final String ATTRIBUTE_NAME_START_ADDRESS = "start address";
    final String ATTRIBUTE_NAME_FINISH_ADDRESS = "finish address";

    private static API api;

    private TripsCustomAdapter tripsCustomAdapter;

    private TextView currentDay;
    private TextView noTrips;
    private ListView listView;

    private Button prevDay;
    private Button nextDay;

    private Calendar currentDate;
    private Calendar anotherDate;
    private Calendar tillDate;

    private long from;
    private long till;

    private String url;
    private String account;
    private String id;
    private String key;
    private boolean geocoder;

    private String [] mFrom = { ATTRIBUTE_NAME_TIME, ATTRIBUTE_NAME_STATUS, ATTRIBUTE_NAME_DURATION, ATTRIBUTE_NAME_DISTANCE, ATTRIBUTE_NAME_SPEED, ATTRIBUTE_NAME_START_ADDRESS, ATTRIBUTE_NAME_FINISH_ADDRESS};;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_trip_object, container, false);

        if (getArguments() != null){
            url = getArguments().getString("url");
            account = getArguments().getString("account");
            id = getArguments().getString("id");
            key = getArguments().getString("key");
            geocoder = getArguments().getBoolean("geocoder");
        }

        api = APIController.getApi(url);

        prevDay = (Button) rootView.findViewById(R.id.trip_back);
        nextDay = (Button) rootView.findViewById(R.id.trip_forward);

        currentDay = (TextView) rootView.findViewById(R.id.trip_date);
        noTrips = (TextView) rootView.findViewById(R.id.tvNoTrips);

        currentDate = Calendar.getInstance();

        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);

        from = currentDate.getTimeInMillis() / 1000;

        tillDate = Calendar.getInstance();

        tillDate.set(Calendar.HOUR_OF_DAY, 0);
        tillDate.set(Calendar.MINUTE, 0);
        tillDate.set(Calendar.SECOND, 0);
        tillDate.set(Calendar.MILLISECOND, 0);
        tillDate.add(Calendar.DATE, 1);

        till = tillDate.getTimeInMillis() / 1000;

        anotherDate = Calendar.getInstance();

        anotherDate.set(Calendar.HOUR_OF_DAY, 0);
        anotherDate.set(Calendar.MINUTE, 0);
        anotherDate.set(Calendar.SECOND, 0);
        anotherDate.set(Calendar.MILLISECOND, 0);

        final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = df.format(currentDate.getTime());

        currentDay.setText(formattedDate);

        prevDay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                anotherDate.add(Calendar.DATE, -1);
                tillDate.add(Calendar.DATE, -1);

                from = anotherDate.getTimeInMillis() / 1000;
                till = tillDate.getTimeInMillis() / 1000;

                String formattedDate = df.format(anotherDate.getTime());

                tripRequest(from, till);

                currentDay.setText(formattedDate);

                nextDay.setVisibility(View.VISIBLE);
            }
        });

        nextDay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                anotherDate.add(Calendar.DATE, 1);
                tillDate.add(Calendar.DATE, 1);
                String formattedDate = df.format(anotherDate.getTime());

                from = anotherDate.getTimeInMillis() / 1000;
                till = tillDate.getTimeInMillis() / 1000;

                currentDay.setText(formattedDate);

                tripRequest(from, till);

                if (anotherDate.equals(currentDate)){
                    nextDay.setVisibility(View.INVISIBLE);
                }
            }
        });

        nextDay.setVisibility(View.INVISIBLE);

        listView = (ListView) rootView.findViewById(R.id.listViewTripsObject);

        tripRequest(from, till);

        return rootView;
    }

    private void tripRequest (long from, long till){
        RequestObjectTrip requestObjectTrip = new RequestObjectTrip();

        requestObjectTrip.setId(id);
        requestObjectTrip.setAccount(account);
        requestObjectTrip.setKey(key);
        requestObjectTrip.setUseGeocoder(geocoder);
        requestObjectTrip.setDateFrom(from);
        requestObjectTrip.setDateTo(till);

        api.responseObjectsTrip(requestObjectTrip).enqueue(new Callback<ResponseObjectTrip>() {

            @Override
            public void onResponse(Call<ResponseObjectTrip> call, Response<ResponseObjectTrip> response) {
                ResponseObjectTrip responseObjectTrip = response.body();

                if (responseObjectTrip != null){
                    if (responseObjectTrip.getStatus().equals(ResponseStatuses.SUCCESS.toString())){
                        noTrips.setText("");

                        ArrayList<Map<String, Object>> data = new ArrayList<>();

                        List <Trip> trips = responseObjectTrip.getTrips();

                        for (Trip trip: trips){
                            Map<String, Object> m = new HashMap<>();

                            m.put(ATTRIBUTE_NAME_TIME, trip.getStartDate());
                            m.put(ATTRIBUTE_NAME_STATUS, trip.getStatus());
                            m.put(ATTRIBUTE_NAME_DURATION, trip.getDuration());
                            m.put(ATTRIBUTE_NAME_DISTANCE, trip.getDistance());
                            m.put(ATTRIBUTE_NAME_SPEED, trip.getSpeed());
                            m.put(ATTRIBUTE_NAME_START_ADDRESS, trip.getStartAddress());
                            m.put(ATTRIBUTE_NAME_FINISH_ADDRESS, trip.getEndAddress());

                            data.add(m);
                        }

                        tripsCustomAdapter = new TripsCustomAdapter(getContext(), R.layout.custom_adapter_trips, data, mFrom);

                        listView.setAdapter(tripsCustomAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseObjectTrip> call, Throwable t) {

            }
        });
    }
}
