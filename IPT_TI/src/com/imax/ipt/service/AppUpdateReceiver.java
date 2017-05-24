package com.imax.ipt.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AppUpdateReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AppUpdateReceiver", "cll onReceive update start UpdateCheckActivity");
//        if(intent.getAction().equals(IPTService.BROADCAST_ACTION_APP_UPDATE_AVAILALBLE)){
//            Intent iptServiceIntent = new Intent(context, IPTService.class).setAction(IPTService.BROADCAST_ACTION_APP_UPDATE_AVAILALBLE);
//            Intent intentService = new Intent().setAction(IPTService.BROADCAST_ACTION_APP_UPDATE_AVAILALBLE);
//                    context.startService(iptServiceIntent);

//        Intent intentUpdate = new Intent(context, UpdateCheckActivity.class);
//        intentUpdate.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intentUpdate);

//        }
    }

}
