package com.imax.ipt.apk.update.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import com.imax.ipt.R;
import com.imax.ipt.common.Constants;

public class RemoteUpdateUtil {
    private static final String TAG = "RemoteUpdateUtil";

    public static final String TAG_DIALOG_FRAGMENT_SOFTWARE_UPDATE_DIALOG_FRAGMENT = "SoftwareUpdateDialogFragment";
    public static final String TAG_DIALOG_FRAGMENT_NO_SOFTWARE_UPDATE_AVAILABLE_DIALOG_FRAGMENT = "NoSoftwareUpdateAvailableDialogFragment";

    public static final String KEY_PREFERENCE_LATEST_VERSION_CODE_PROMPTED_FOR_UPGRADE = "latestVersionCodePromptedForUpgrade";

    static final int REQUEST_INSTALL = 1;

    private static UpdateChecker checker;
    private static int remoteVersionCode;
    private static boolean mForceUpdate;
    public static boolean isUpdate = false;


    public void checkRemoteUpdate(final Activity activity, final boolean isManualCheck) {
                Log.d(TAG, "Cll checkRemoteUpdate " + activity.getClass().getSimpleName());

        new Thread(new Runnable() {
            @Override
            public void run() {
                checker = new UpdateChecker(activity);
                Log.d(TAG, " cll Current App version code: " + checker.getVersionCode());
                remoteVersionCode = checker.checkForUpdateByVersionCode(Constants.URL_VERSION);
                Log.d(TAG, "cll Remote App version code: " + remoteVersionCode);

                boolean update = checker.isUpdateAvailable();
                Log.d(TAG, "update available? " + String.valueOf(update));

                if (update) {

                    // check is force update or not,if yes update, if not show dialog
                    mForceUpdate = checker.isForceUpdate();
//                    if (mForceUpdate) {
//                        activity.runOnUiThread(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                checker.downloadAndInstall(Constants.URL_APK);
//                            }
//                        });
//                        return;
//                    }

                    if (!isManualCheck) {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
                        int latestVersionCodePrompted = prefs.getInt(KEY_PREFERENCE_LATEST_VERSION_CODE_PROMPTED_FOR_UPGRADE, 0);
                        if (remoteVersionCode <= latestVersionCodePrompted) {
                            Log.i(TAG, "User has been prompted for this upgrade version: " + remoteVersionCode);
                            return;
                        }
                    }
                    // Prompt the user if they want an update
                    SoftwareUpdateDialogFragment dialog = new SoftwareUpdateDialogFragment();
                    dialog.setCancelable(false);
                    dialog.show(activity.getFragmentManager(), TAG_DIALOG_FRAGMENT_SOFTWARE_UPDATE_DIALOG_FRAGMENT);
                } else if (!update && isManualCheck) {
                    NoSoftwareUpdateAvailableDialogFragment dialog = new NoSoftwareUpdateAvailableDialogFragment();
                    dialog.show(activity.getFragmentManager(), TAG_DIALOG_FRAGMENT_NO_SOFTWARE_UPDATE_AVAILABLE_DIALOG_FRAGMENT);
                }
            }
        }).start();
    }

/*   private void performDownloadAndInstall()
   {
      parentActivity.runOnUiThread(new Runnable() {
         
         @Override
         public void run()
         {
            checker.downloadAndInstall("http://" + Client.host + "/IPT.apk");   
         }
      });               
      Log.d(TAG, "downloadAndInstall-ed");
   }*/

    public static class SoftwareUpdateDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            if(mForceUpdate){
                builder.setMessage(getString(R.string.dialog_message_software_update_force))
                        .setTitle(getString(R.string.dialog_title_software_update_available))
                        .setPositiveButton(getString(R.string.response_ok),new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        checker.downloadAndInstall(Constants.URL_APK);
                                    }
                                });
                            }
                        });
//                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
//                            @Override
//                            public void onCancel(DialogInterface dialog) {
//                                getActivity().runOnUiThread(new Runnable() {
//
//                                    @Override
//                                    public void run() {
//                                        checker.downloadAndInstall(apkUrl);
//                                    }
//                                });
//                            }
//                        });

            } else {

                builder.setMessage(getString(R.string.dialog_message_software_update_available))
                        .setTitle(getString(R.string.dialog_title_software_update_available))
                        .setPositiveButton(getString(R.string.response_yes), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        checker.downloadAndInstall(Constants.URL_APK);
                                    }
                                });
                            }
                        })
                        .setNegativeButton(getString(R.string.response_no), new DialogInterface.OnClickListener() {

                            @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                            prefs.edit().putInt(KEY_PREFERENCE_LATEST_VERSION_CODE_PROMPTED_FOR_UPGRADE, remoteVersionCode).commit();
                        }
                    });
            }
            Dialog dialog =builder.create();
            return dialog;
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);
        }
    }

    public static class NoSoftwareUpdateAvailableDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage(getString(R.string.dialog_message_no_software_update_available))
                    .setPositiveButton(getString(R.string.response_ok), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // no action needed, user should wait or press the Back button on page to proceed
                        }
                    });

            return builder.create();
        }
    }

}
