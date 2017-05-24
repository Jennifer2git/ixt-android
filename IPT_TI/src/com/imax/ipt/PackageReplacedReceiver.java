package com.imax.ipt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PackageReplacedReceiver extends BroadcastReceiver {
    private final static String TAG = "PackageReplacedReceiver";

    public PackageReplacedReceiver() {
        Log.i(TAG, "PackageReplacedReceiver started");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "IPT package replaced..." + intent.getAction());

        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
//      throw new UnsupportedOperationException("Not yet implemented");
    }
}
