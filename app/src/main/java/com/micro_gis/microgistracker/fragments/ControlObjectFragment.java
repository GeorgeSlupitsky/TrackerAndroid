package com.micro_gis.microgistracker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.micro_gis.microgistracker.R;
import com.micro_gis.microgistracker.models.rest.DeviceStatus;
import com.micro_gis.microgistracker.models.rest.RequestDeviceStatus;
import com.micro_gis.microgistracker.models.rest.RequestSaveStatus;
import com.micro_gis.microgistracker.models.rest.ResponseDeviceStatus;
import com.micro_gis.microgistracker.models.rest.ResponseSaveStatus;
import com.micro_gis.microgistracker.models.rest.ResponseStatuses;
import com.micro_gis.microgistracker.retrofit.API;
import com.micro_gis.microgistracker.retrofit.APIController;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User9 on 16.03.2018.
 */

public class ControlObjectFragment extends Fragment{

    private static API api;

    private AppCompatSpinner spinner;
    private Button saveStatus;
    private TextView noControl;

    private String url;
    private String account;
    private String idDevice;
    private String key;

    private Integer statusId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_control_object, container, false);

        if (getArguments() != null){
            url = getArguments().getString("url");
            account = getArguments().getString("account");
            idDevice = getArguments().getString("id");
            key = getArguments().getString("key");
        }

        api = APIController.getApi(url);

        RequestDeviceStatus requestDeviceStatus = new RequestDeviceStatus();
        requestDeviceStatus.setAccount(account);
        requestDeviceStatus.setId(Integer.valueOf(idDevice));
        requestDeviceStatus.setKey(key);

        spinner = (AppCompatSpinner) rootView.findViewById(R.id.spinnerControl);
        saveStatus = (Button) rootView.findViewById(R.id.buttonSaveControl);
        noControl = (TextView) rootView.findViewById(R.id.noControl);

        saveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statusId != null){
                    RequestSaveStatus requestSaveStatus = new RequestSaveStatus();

                    requestSaveStatus.setId(Integer.valueOf(idDevice));
                    requestSaveStatus.setAccount(account);
                    requestSaveStatus.setKey(key);
                    requestSaveStatus.setStatusId(statusId);

                    api.saveStatus(requestSaveStatus).enqueue(new Callback<ResponseSaveStatus>() {
                        @Override
                        public void onResponse(Call<ResponseSaveStatus> call, Response<ResponseSaveStatus> response) {
                            ResponseSaveStatus responseSaveStatus = response.body();

                            if (responseSaveStatus != null){
                                if (response.body().getStatus().equals(ResponseStatuses.SUCCESS.toString())){
                                    Toast toast = Toast.makeText(rootView.getContext(),
                                            getString(R.string.statusChanged), Toast.LENGTH_LONG);
                                    toast.show();
                                } else {
                                    Toast toast = Toast.makeText(rootView.getContext(),
                                            getString(R.string.statusError), Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseSaveStatus> call, Throwable t) {

                        }
                    });

                } else {
                    Toast toast = Toast.makeText(rootView.getContext(),
                            getString(R.string.chooseStatus), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        api.responseDeviceStatus(requestDeviceStatus).enqueue(new Callback<ResponseDeviceStatus>() {
            @Override
            public void onResponse(Call<ResponseDeviceStatus> call, Response<ResponseDeviceStatus> response) {
                ResponseDeviceStatus responseDeviceStatus = response.body();

                if (responseDeviceStatus != null){
                    List<DeviceStatus> dataList = responseDeviceStatus.getDeviceStatuses();

                    if (dataList != null){

                        noControl.setVisibility(View.GONE);

                        String [] data = new String[dataList.size()];

                        Integer selectedStatus = null;

                        for (DeviceStatus deviceStatus: dataList){
                            data[dataList.indexOf(deviceStatus)] = deviceStatus.getStatus();

                            if (deviceStatus.isEnabled()){
                                selectedStatus = dataList.indexOf(deviceStatus);
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_spinner_item, data);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinner.setAdapter(adapter);

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                statusId = dataList.get(position).getId();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        if (selectedStatus != null){
                            spinner.setSelection(selectedStatus);
                        }

                    } else {
                        noControl.setText(getContext().getString(R.string.noStatuses));
                        spinner.setVisibility(View.GONE);
                        saveStatus.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDeviceStatus> call, Throwable t) {

            }
        });

        return rootView;
    }
}
