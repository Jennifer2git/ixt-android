package com.imax.ipt.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.imax.ipt.ui.activity.WelcomeActivity;

public class IPTBootReceiver extends BroadcastReceiver {

    public static String TAG = IPTBootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive starting WelcomeActivity" + intent.getAction());

        Intent intentWelcome = new Intent(context, WelcomeActivity.class);
        intentWelcome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentWelcome);
    }

}
