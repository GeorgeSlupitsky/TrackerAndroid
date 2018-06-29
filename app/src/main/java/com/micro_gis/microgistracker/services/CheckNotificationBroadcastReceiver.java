package com.micro_gis.microgistracker.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CheckNotificationBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, CheckNotificationService.class));
    }
}
