package com.imax.ipt.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class HomeWatcherReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = HomeWatcherReceiver.class.getSimpleName();
    private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
    private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    private static final String SYSTEM_DIALOG_REASON_LOCK = "lock";
    private static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
                Log.i(LOG_TAG, "homekey");
//                IPTService.stop(context);
                ConnecteService.stop(context);
            } else if (SYSTEM_DIALOG_REASON_RECENT_APPS.equals(reason)) {
                Log.i(LOG_TAG, "long press home key or activity switch");

            } else if (SYSTEM_DIALOG_REASON_LOCK.equals(reason)) {

                Log.i(LOG_TAG, "lock");
            } else if (SYSTEM_DIALOG_REASON_ASSIST.equals(reason)) {
                Log.i(LOG_TAG, "assist");
            }

        }
    }

}