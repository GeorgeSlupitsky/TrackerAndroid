package com.micro_gis.microgistracker.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.micro_gis.microgistracker.Communicator;
import com.micro_gis.microgistracker.R;
import com.micro_gis.microgistracker.adapters.StatusCodeCustomAdapter;
import com.micro_gis.microgistracker.adapters.TripsCustomAdapter;
import com.micro_gis.microgistracker.models.rest.RequestObjectTrip;
import com.micro_gis.microgistracker.models.rest.RequestStatusCode;
import com.micro_gis.microgistracker.models.rest.ResponseObjectTrip;
import com.micro_gis.microgistracker.models.rest.ResponseStatusCode;
import com.micro_gis.microgistracker.models.rest.ResponseStatuses;
import com.micro_gis.microgistracker.models.rest.StatusCode;
import com.micro_gis.microgistracker.models.rest.Trip;
import com.micro_gis.microgistracker.retrofit.API;
import com.micro_gis.microgistracker.retrofit.APIController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User9 on 15.03.2018.
 */

public class StatusCodeFragment extends Fragment {

    final String ATTRIBUTE_NAME_TIME = "time";
    final String ATTRIBUTE_NAME_STATUS = "status";
    final String ATTRIBUTE_NAME_LAT = "lat";
    final String ATTRIBUTE_NAME_LNG = "lng";
    final String ATTRIBUTE_NAME_TEXT = "text";

    private static API api;

    private StatusCodeCustomAdapter statusCodeCustomAdapter;

    private TextView currentDay;
    private TextView noStatusCodes;
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
    private String idDevice;
    private String key;

    private ArrayList<Map<String, Object>> data;

    private String [] mFrom = {ATTRIBUTE_NAME_TIME, ATTRIBUTE_NAME_STATUS, ATTRIBUTE_NAME_LAT, ATTRIBUTE_NAME_LNG, ATTRIBUTE_NAME_TEXT};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_status_code_object, container, false);

        if (getArguments() != null){
            url = getArguments().getString("url");
            account = getArguments().getString("account");
            idDevice = getArguments().getString("id");
            key = getArguments().getString("key");
        }

        api = APIController.getApi(url);

        prevDay = (Button) rootView.findViewById(R.id.status_code_back);
        nextDay = (Button) rootView.findViewById(R.id.status_code_forward);

        currentDay = (TextView) rootView.findViewById(R.id.status_code_date);
        noStatusCodes = (TextView) rootView.findViewById(R.id.tvNoStatusCodes);

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

                statusCodeRequest(from, till);

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

                statusCodeRequest(from, till);

                if (anotherDate.equals(currentDate)){
                    nextDay.setVisibility(View.INVISIBLE);
                }
            }
        });

        nextDay.setVisibility(View.INVISIBLE);

        listView = (ListView) rootView.findViewById(R.id.listViewStatusCodesObject);

        statusCodeRequest(from, till);

        return rootView;
    }

    private void statusCodeRequest (long from, long till){
        RequestStatusCode requestStatusCode = new RequestStatusCode();

        requestStatusCode.setId(idDevice);
        requestStatusCode.setAccount(account);
        requestStatusCode.setKey(key);
        requestStatusCode.setDateFrom(from);
        requestStatusCode.setDateTo(till);

        api.responseStatusCode(requestStatusCode).enqueue(new Callback<ResponseStatusCode>() {
            @Override
            public void onResponse(Call<ResponseStatusCode> call, Response<ResponseStatusCode> response) {
                ResponseStatusCode responseStatusCode = response.body();

                if (responseStatusCode != null){
                    if (responseStatusCode.getStatus().equals(ResponseStatuses.SUCCESS.toString())){
                        noStatusCodes.setText("");

                        data = new ArrayList<>();

                        List<StatusCode> statusCodes = responseStatusCode.getStatusCodes();

                        for (StatusCode statusCode: statusCodes){
                            Map<String, Object> m = new HashMap<>();

                            m.put(ATTRIBUTE_NAME_TIME, statusCode.getTimestamp());
                            m.put(ATTRIBUTE_NAME_STATUS, statusCode.getStatusCode());
                            m.put(ATTRIBUTE_NAME_LAT, statusCode.getLat());
                            m.put(ATTRIBUTE_NAME_LNG, statusCode.getLng());
                            m.put(ATTRIBUTE_NAME_TEXT, statusCode.getText());

                            data.add(m);
                        }

                        statusCodeCustomAdapter = new StatusCodeCustomAdapter(getContext(), R.layout.custom_adapter_status_code, data, mFrom);

                        listView.setAdapter(statusCodeCustomAdapter);

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseStatusCode> call, Throwable t) {

            }
        });
    }
}
