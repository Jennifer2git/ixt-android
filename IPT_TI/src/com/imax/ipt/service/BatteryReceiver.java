package com.imax.ipt.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

public class BatteryReceiver extends BroadcastReceiver {
    public final static String TAG = "BatteryReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
      /*
       * Karl: For some reason, I cannot get ACTION_BATTERY_LOW and ACTION_BATTERY_OKAY to be fired from the system
       *    current implementation will be to receive ACTION_BATTERY_CHANGED, and calculate battery level manually
       *    this method is just a workaround and is NOT the preferred method
       */
        //TODO NEED TO FIX THIS WORKAROUND
        if (intent.getAction().equals(Intent.ACTION_BATTERY_LOW)) {
//            Intent iptServiceIntent = new Intent(context, IPTService.class).setAction(IPTService.ACTION_BATTERY_LOW);
            Intent iptServiceIntent = new Intent(context, ConnecteService.class)
                    .setAction(ConnecteService.ACTION_BATTERY_LOW);
            context.startService(iptServiceIntent);
        } else if (intent.getAction().equals(Intent.ACTION_BATTERY_OKAY)) {
//            Log.i(TAG, "Battery action:" + intent.getAction());
//            Intent iptServiceIntent = new Intent(context, IPTService.class).setAction(IPTService.ACTION_BATTERY_OKAY);
//            context.startService(iptServiceIntent);
        } else if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {

            // query the initial battery status
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, ifilter);

            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            float batteryPercentage = level / (float) scale;
//            Log.d(TAG, "Battery(%) -> " + batteryPercentage);

            if (batteryPercentage <= 0.15f) {
//                Intent iptServiceIntent = new Intent(context, IPTService.class).setAction(IPTService.ACTION_BATTERY_LOW);
                Intent iptServiceIntent = new Intent(context, ConnecteService.class).setAction(ConnecteService.ACTION_BATTERY_LOW);
                context.startService(iptServiceIntent);
            } else {
//                Intent iptServiceIntent = new Intent(context, IPTService.class).setAction(IPTService.ACTION_BATTERY_OKAY);
//                Intent iptServiceIntent = new Intent(context, ConnecteService.class).setAction(ConnecteService.ACTION_BATTERY_OKAY);
//                context.startService(iptServiceIntent);
            }
        } else {
//            Log.i(TAG, "Battery action not handled:" + intent.getAction());
        }
    }

}
