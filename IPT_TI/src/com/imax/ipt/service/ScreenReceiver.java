package com.imax.ipt.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver {

    private static final String TAG = "ScreenReceiver";

    public static boolean wasScreenOn = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.d(TAG, "Action Screen OFF");
            wasScreenOn = false;

//            IPTService.actionScreenOff(context);
            ConnecteService.actionScreenOff(context);

        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.d(TAG, "Action Screen ON");
            wasScreenOn = true;

//            IPTService.actionScreenOn(context);
            ConnecteService.actionScreenOn(context);
        }
    }

}
