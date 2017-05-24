package com.imax.ipt.service;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.controller.GlobalController;
import com.imax.ipt.ui.activity.WelcomeActivity;

public class HomePremiereReceiver extends BroadcastReceiver {
    private final static String TAG = "HomePremiereReceiver";
    private final String ACTION_HP = "com.imax.ipt.service.BROADCAST_HOME_PREMIERE";
    private final String ACTION_RE = "com.imax.ipt.service.BROADCAST_RESPONSE";
    private final int RE_OK = 1;
    private final int RE_ERROR = 0; //
    private final int RE_UNKNOWN = -1; //

    private Context mContext;

    public HomePremiereReceiver() {

    }


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.v(TAG, "onReceive " + intent.getAction().toString());
        this.mContext = context;
        if (intent.getAction().equals(ACTION_HP)) {
            Bundle bundle = intent.getExtras();
            if (bundle.getString("HP_CMD") == null) {
                return;
            }

            if (!IPT.isPowerOn()) {
                showStartAppDialog(mContext);
//                sendResponse2HP(mContext,RE_ERROR);
                // TODO: if 3d notify hp to repeat send cmd？？
                return;
            }


            Log.d(TAG, "cll ? " + bundle.getString("HP_CMD"));

//            sendResponse2HP(mContext,RE_OK);

            if (bundle.getString("HP_CMD").equals("HP_MUTE")) {
                GlobalController.getInstance().setMute(bundle.getBoolean("HP_MUTE"));
            } else if (bundle.getString("HP_CMD").equals("HP_VOLUME")) {
                GlobalController.getInstance().setVolume(bundle.getInt("HP_VOLUME"));

            } else if (bundle.getString("HP_CMD").equals("HP_LIGHTING_CONTROL")) {
                if (!intent.hasExtra("HP_LIGHTING_CONTROL")) {
                    return;
                }
                //TODO : add lighting control on HP.
            } else if (bundle.getString("HP_CMD").equals("HP_2D_OR_3D")) {

                if (!intent.hasExtra("HP_2D_OR_3D")) {
                    Log.d(TAG, "cll error get the 2d/3d");
                    return;
                }

                String mode = intent.getStringExtra("HP_2D_OR_3D");
                if (mode.equals("2d")) {
                    GlobalController.getInstance().set2DMode();
                }
                if (mode.equals("3d")) {
                    GlobalController.getInstance().set3DMode();
                }
            }

        }

    }



    private void showStartAppDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context.getApplicationContext());
        builder
                .setTitle(context.getString(R.string.prompt_note))
                .setMessage(context.getString(R.string.warning_restart_app_prompt))
                .setPositiveButton(context.getString(R.string.response_ok), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Intent intent = new Intent(context, WelcomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        dialogInterface.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void sendResponse2HP(Context context, int type){
        Intent intent = new Intent();
        intent.setAction(ACTION_RE);
        Bundle bundle = new Bundle();
        bundle.putInt("ipt_response", type);// 1 -ok, 0 -power off. -1 -unknown
        intent.putExtras(bundle);
        context.sendBroadcast(intent);
    }

//    private boolean isIPTrun() {
//
//        ActivityManager mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningAppProcessInfo>
//                appProcessList = mActivityManager.getRunningAppProcesses();
//
//        for (ActivityManager.RunningAppProcessInfo appProcess : appProcessList) {
//            String[] pkgNameList = appProcess.pkgList;
//            for (int i = 0; i < pkgNameList.length; i++) {
//                String pkgName = pkgNameList[i];
//                Log.i(TAG, " i =  " + i + " packageName " + pkgName);
//                if (pkgName.equals(Constants.IPT_PACKAGE_NAME)) {
//                    Log.i(TAG, " i =  " + i + " packageName is the one looking for " + pkgName);
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
}
