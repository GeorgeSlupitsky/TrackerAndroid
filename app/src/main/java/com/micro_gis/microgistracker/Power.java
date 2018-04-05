package com.micro_gis.microgistracker;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;

/**
 * Created by User9 on 05.04.2018.
 */

public class Power {

    public static boolean isConnected(Context context) {
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        return plugged == BatteryManager.BATTERY_PLUGGED_AC ||
                plugged == BatteryManager.BATTERY_PLUGGED_USB ||
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 &&
                        plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS);
    }

}
