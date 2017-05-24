package com.imax.ipt.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import com.imax.ipt.R;
import com.imax.ipt.apk.update.util.RemoteUpdateUtil;
import com.imax.ipt.apk.update.util.UpdateChecker;
import com.imax.ipt.common.Constants;

/**
 * Created by yanli on 2015/9/30.
 */
public class UpdateCheckActivity extends Activity {
    private static final String TAG = UpdateCheckActivity.class.getSimpleName();

    //    public static final String TAG_DIALOG_FRAGMENT_SOFTWARE_UPDATE_DIALOG_FRAGMENT = "SoftwareUpdateDialogFragment";
//    public static final String KEY_PREFERENCE_LATEST_VERSION_CODE_PROMPTED_FOR_UPGRADE = "latestVersionCodePromptedForUpgrade";
    private static UpdateChecker checker;
    private static int remoteVersionCode = 0;
    private static boolean mForceUpdate = false;
//        Button btnYes;
//        Button btnNo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_check_update);
            Log.d(TAG, "cll onCreate " );
//        mThread.start();
        checker = new UpdateChecker(UpdateCheckActivity.this);
        mForceUpdate = getIntent().getBooleanExtra("force_update",false);
        remoteVersionCode = getIntent().getIntExtra("remote_version_code",0);
//        showUpdateDialog();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    Thread mThread = new Thread(new Runnable() {
        @Override
        public void run() {
            boolean forceUpdate;
            checker = new UpdateChecker(UpdateCheckActivity.this);
            remoteVersionCode = checker.checkForUpdateByVersionCode(Constants.URL_VERSION);//todo too long time
            mForceUpdate = checker.isForceUpdate();
            if(!checker.isUpdateAvailable()){
                startActivity(new Intent(UpdateCheckActivity.this, WelcomeActivity.class));
                return;
            }
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(UpdateCheckActivity.this.getApplicationContext());
            int latestVersionCodePrompted = prefs.getInt(RemoteUpdateUtil.KEY_PREFERENCE_LATEST_VERSION_CODE_PROMPTED_FOR_UPGRADE,0);

            if (remoteVersionCode <= latestVersionCodePrompted) {
                startActivity(new Intent(UpdateCheckActivity.this, WelcomeActivity.class));
                return;
            }

            Log.d(TAG, "cll here update awailable " + ", forceupdate: " + mForceUpdate);
            showUpdateDialog();

        }
    });

    public void showUpdateDialog() {

        SoftwareUpdateDialogFragment dialog = new SoftwareUpdateDialogFragment();
        dialog.setCancelable(false);
        dialog.show(this.getFragmentManager(), RemoteUpdateUtil.TAG_DIALOG_FRAGMENT_SOFTWARE_UPDATE_DIALOG_FRAGMENT);

    }


    public static class SoftwareUpdateDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            if (mForceUpdate) {
                builder.setMessage(getString(R.string.dialog_message_software_update_force))
                        .setTitle(getString(R.string.dialog_title_software_update_available))
                        .setPositiveButton(getString(R.string.response_ok), new DialogInterface.OnClickListener() {

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
                                Log.w(TAG, "Software update declined");

                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                                prefs.edit().putInt(RemoteUpdateUtil.KEY_PREFERENCE_LATEST_VERSION_CODE_PROMPTED_FOR_UPGRADE, remoteVersionCode).commit();
//                                int version = prefs.getInt(RemoteUpdateUtil.KEY_PREFERENCE_LATEST_VERSION_CODE_PROMPTED_FOR_UPGRADE,0);
//                                Log.w(TAG, "Software update declined the last version is " + version + ", remoteVersionCode " + remoteVersionCode);
                                Intent welcomeIntent = new Intent(getActivity(), WelcomeActivity.class);
                                getActivity().startActivity(welcomeIntent);
                                getActivity().finish();
                            }
                        });
            }
            return builder.create();
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);
        }
    }
}
