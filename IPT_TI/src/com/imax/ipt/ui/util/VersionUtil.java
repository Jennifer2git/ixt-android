package com.imax.ipt.ui.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.widget.Toast;

public class VersionUtil {

    public static final String TAG = "VersionUtil";

    public static void showVersion(Context context) {

        PackageManager manager = context.getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
//         Toast.makeText(context, "PackageName = " + info.packageName + "\nVersionCode = " + info.versionCode + "\nVersionName = " + info.versionName, Toast.LENGTH_LONG).show();
        } catch (NameNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        }

    }

}
