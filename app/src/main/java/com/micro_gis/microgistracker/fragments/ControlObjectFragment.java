package com.micro_gis.microgistracker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.micro_gis.microgistracker.R;
import com.micro_gis.microgistracker.adapters.ControlCustomAdapter;
import com.micro_gis.microgistracker.models.rest.DeviceStatus;
import com.micro_gis.microgistracker.models.rest.RequestDeviceStatus;
import com.micro_gis.microgistracker.models.rest.RequestSaveStatus;
import com.micro_gis.microgistracker.models.rest.ResponseDeviceStatus;
import com.micro_gis.microgistracker.models.rest.ResponseSaveStatus;
import com.micro_gis.microgistracker.models.rest.ResponseStatuses;
import com.micro_gis.microgistracker.retrofit.API;
import com.micro_gis.microgistracker.retrofit.APIController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User9 on 16.03.2018.
 */

public class ControlObjectFragment extends Fragment{

    final String ATTRIBUTE_NAME_ID = "time";
    final String ATTRIBUTE_NAME_STATUS = "status";
    final String ATTRIBUTE_NAME_COLOR = "color";
    final String ATTRIBUTE_NAME_COLOR_HEX = "color hex";
    final String ATTRIBUTE_NAME_ICON = "icon";

    private static API api;
    private Button saveStatus;
    private TextView noControl;
    private ListView listView;
    private TextView status;
    private TextView statusStr;

    private String url;
    private String account;
    private String idDevice;
    private String key;

    private String icon;

    private ArrayList<Map<String, Object>> data;

    private ControlCustomAdapter controlCustomAdapter;

    private String [] mFrom = {ATTRIBUTE_NAME_ID, ATTRIBUTE_NAME_STATUS, ATTRIBUTE_NAME_COLOR, ATTRIBUTE_NAME_COLOR_HEX, ATTRIBUTE_NAME_ICON};

    private Integer statusId;
    private String colorHex;
    private String textStatus;

    private Integer statusIdChoose;
    private String colorHexChoose;
    private String textStatusChoose;

    private List<DeviceStatus> dataList;

    private static int save = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_control_object, container, false);

        if (getArguments() != null){
            url = getArguments().getString("url");
            account = getArguments().getString("account");
            idDevice = getArguments().getString("id");
            key = getArguments().getString("key");
            icon = getArguments().getString("icon");
        }

        api = APIController.getApi(url);

        createAdapter();

        saveStatus = (Button) rootView.findViewById(R.id.buttonSaveControl);
        noControl = (TextView) rootView.findViewById(R.id.noControl);
        listView = (ListView) rootView.findViewById(R.id.lvControl) ;
        status = (TextView) rootView.findViewById(R.id.tvStatus);
        statusStr = (TextView) rootView.findViewById(R.id.tvStatusStr);

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (save != -1 && save != position){
                    parent.getChildAt(save).setBackgroundColor(Color.TRANSPARENT);
                }

                if (save == -1){
                    colorHex = (String) data.get(position).get(ATTRIBUTE_NAME_COLOR_HEX);
                    statusId = (Integer) data.get(position).get(ATTRIBUTE_NAME_ID);
                    textStatus = (String) data.get(position).get(ATTRIBUTE_NAME_STATUS);

                    parent.getChildAt(position).setBackgroundColor(Color.parseColor(colorHex));

                    save = position;

                    listView.setItemChecked(position, true);
                } else if (save == position){
                    parent.getChildAt(save).setBackgroundColor(Color.TRANSPARENT);
                    save = -1;
                    listView.setItemChecked(position, false);
                } else if (save != position){
                    colorHex = (String) data.get(position).get(ATTRIBUTE_NAME_COLOR_HEX);
                    statusId = (Integer) data.get(position).get(ATTRIBUTE_NAME_ID);
                    textStatus = (String) data.get(position).get(ATTRIBUTE_NAME_STATUS);

                    parent.getChildAt(position).setBackgroundColor(Color.parseColor(colorHex));

                    save = position;

                    listView.setItemChecked(position, true);
                }


//                TextView tv = (TextView) ((LinearLayout) view).getChildAt(1);
//                tv.setBackgroundColor(Color.parseColor(colorHex));

            }
        });

        saveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listView.getCheckedItemCount() == 1){
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

                                    status.setBackgroundColor(Color.parseColor(colorHex));
                                    status.setText(textStatus);

                                    createAdapter();

                                    save = -1;
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

        return rootView;
    }

    private void createAdapter(){
        RequestDeviceStatus requestDeviceStatus = new RequestDeviceStatus();
        requestDeviceStatus.setAccount(account);
        requestDeviceStatus.setId(Integer.valueOf(idDevice));
        requestDeviceStatus.setKey(key);

        api.responseDeviceStatus(requestDeviceStatus).enqueue(new Callback<ResponseDeviceStatus>() {
            @Override
            public void onResponse(Call<ResponseDeviceStatus> call, Response<ResponseDeviceStatus> response) {
                ResponseDeviceStatus responseDeviceStatus = response.body();

                if (responseDeviceStatus != null){
                    dataList = responseDeviceStatus.getDeviceStatuses();

                    if (dataList != null){

                        noControl.setVisibility(View.GONE);

                        data = new ArrayList<>();

                        for (DeviceStatus deviceStatus: dataList){

                            if (!deviceStatus.isEnabled()){
                                Map<String, Object> m = new HashMap<>();

                                m.put(ATTRIBUTE_NAME_ID, deviceStatus.getId());
                                m.put(ATTRIBUTE_NAME_STATUS, deviceStatus.getStatus());
                                m.put(ATTRIBUTE_NAME_COLOR, deviceStatus.getColor());
                                m.put(ATTRIBUTE_NAME_COLOR_HEX, deviceStatus.getColorHEX());
                                m.put(ATTRIBUTE_NAME_ICON, icon);

                                data.add(m);
                            } else {
                                status.setBackgroundColor(Color.parseColor(deviceStatus.getColorHEX()));
                                status.setText(deviceStatus.getStatus());
                            }
                        }

                        controlCustomAdapter = new ControlCustomAdapter(getContext(), R.layout.custom_adapter_control, data, mFrom);

                        listView.setAdapter(controlCustomAdapter);

                    } else {
                        noControl.setText(getContext().getString(R.string.noStatuses));
                        saveStatus.setVisibility(View.GONE);
                        listView.setVisibility(View.GONE);
                        status.setVisibility(View.GONE);
                        statusStr.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDeviceStatus> call, Throwable t) {
            }
        });
    }
}
