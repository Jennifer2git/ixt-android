package com.imax.ipt.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.imax.ipt.controller.GlobalController;

public class SystemLanguageChangeReceiver extends BroadcastReceiver {

    private static final String TAG = SystemLanguageChangeReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {
            Log.d(TAG, "onReceive language change broadcast " + Intent.ACTION_LOCALE_CHANGED );
            GlobalController.getInstance().setSystemLanguage();
            GlobalController.getInstance().getActiveDeviceByLanguage();
        }
    }

}
