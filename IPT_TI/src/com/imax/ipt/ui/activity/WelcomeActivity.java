package com.imax.ipt.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.content.PermissionChecker;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import com.imax.ipt.R;
import com.imax.ipt.apk.update.util.RemoteUpdateUtil;
import com.imax.ipt.apk.update.util.UpdateChecker;
import com.imax.ipt.common.Constants;
import com.imax.ipt.service.ConnecteService;

public class WelcomeActivity extends BaseActivity {
    public static final String TAG = WelcomeActivity.class.getSimpleName();
    private static final int UPDATE_AVAILIBALE = 1;

    private static UpdateChecker checker;
    private static int remoteVersionCode = 0;
    private static boolean mForceUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_welcome);
        if(mUpdateThread.isAlive()){
            return;
        }
        mUpdateThread.start();
        super.onCreate(savedInstanceState);
//        getDex();
//        checkAutoRunPermission();
    }


    protected void onResume() {
        Intent connectServiceIntent = new Intent(this, ConnecteService.class).setAction(ConnecteService.ACTION_ACTIVITY_ONRESUME);
        startService(connectServiceIntent);
        super.onResume();
    }

    Thread mUpdateThread = new Thread(new Runnable() {
        @Override
        public void run() {
            checker = new UpdateChecker(WelcomeActivity.this);
            remoteVersionCode = checker.checkForUpdateByVersionCode(Constants.URL_VERSION);
            mForceUpdate = checker.isForceUpdate();
            if (!checker.isUpdateAvailable()) {
                return;
            }
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WelcomeActivity.this.getApplicationContext());
            int latestVersionCodePrompted = prefs.getInt(RemoteUpdateUtil.KEY_PREFERENCE_LATEST_VERSION_CODE_PROMPTED_FOR_UPGRADE, 0);

            if (remoteVersionCode <= latestVersionCodePrompted) {
                return;
            }
            Message msg = new Message();
            msg.what = UPDATE_AVAILIBALE;
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.UPDATE_FORCE_UPDATE,mForceUpdate);
            bundle.putInt(Constants.UPDATE_REMOTE_VERSION_CODE,remoteVersionCode);
            msg.setData(bundle);
            mUpdateHandler.sendMessage(msg);
        }
    });

   Handler mUpdateHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case UPDATE_AVAILIBALE:
                    Bundle bundle = msg.getData();
                    showUpdateAppDialog(getApplicationContext(), bundle.getBoolean(Constants.UPDATE_FORCE_UPDATE), bundle.getInt(Constants.UPDATE_REMOTE_VERSION_CODE));
                    break;
            }
            super.handleMessage(msg);

        }
    };



    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    private ServiceConnection mServiceConnetion = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            WelcomeBindService.MyBinder binder = (WelcomeBindService.MyBinder)service;
//            WelcomeBindService bindService = binder.getService();
//            //TODO start to connection
//
//
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//        Log.d(TAG, "cll onServiceDisconnected welcomeactivity");
//
//        }
//    };
//    private void bindService(){
//        Intent intent = new Intent(this,WelcomeBindService.class);
//        bindService(intent, mServiceConnetion, BIND_AUTO_CREATE);
//
//    }
//
//    private void unBindService(){
//        Log.d(TAG, "cll unBindService welcomeactivity");
//        unbindService(mServiceConnetion);
//    }


    static private AlertDialog dialogUpdate;

    public void showUpdateAppDialog(Context context, Boolean forceUpdate, int remoteVersionCode) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (forceUpdate) {
            builder.setMessage(getString(R.string.dialog_message_software_update_force))
                    .setTitle(getString(R.string.dialog_title_software_update_available))
                    .setPositiveButton(getString(R.string.response_ok), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(context, DownloadActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            dialogUpdate = null;
                        }
                    });

        } else {
            builder.setMessage(getString(R.string.dialog_message_software_update_available))
                    .setTitle(getString(R.string.dialog_title_software_update_available))
                    .setPositiveButton(getString(R.string.response_yes), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(context, DownloadActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            dialogUpdate = null;
                        }
                    })
                    .setNegativeButton(getString(R.string.response_no), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            prefs.edit().putInt(RemoteUpdateUtil.KEY_PREFERENCE_LATEST_VERSION_CODE_PROMPTED_FOR_UPGRADE, remoteVersionCode).commit();
                            int version = prefs.getInt(RemoteUpdateUtil.KEY_PREFERENCE_LATEST_VERSION_CODE_PROMPTED_FOR_UPGRADE, 0);
                            Log.w(TAG, "Software update declined the last version is " + version + ", remoteVersionCode " + remoteVersionCode);
                            dialogUpdate = null;
                        }
                    });
        }

        if(dialogUpdate != null){
            return;
        }
        dialogUpdate = builder.create();
        dialogUpdate.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialogUpdate.setCancelable(false);
        dialogUpdate.setCanceledOnTouchOutside(false);
        dialogUpdate.show();
    }

    private void checkAutoRunPermission(){
        int permission = PermissionChecker.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS);
        int perm2 = PermissionChecker.checkSelfPermission(this, Manifest.permission.RECEIVE_BOOT_COMPLETED);
        int perm3 = PermissionChecker.checkSelfPermission(this, Manifest.permission_group.APP_INFO);
// 0 0 -1 4.4
// -1 0 -1 honor phone

        Log.d(TAG,"get app permission 1 = " + permission + " receive boot " + perm2 + perm3);
    }

    private void getDex(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        float density  = dm.density;      // ��Ļ�ܶȣ����ر�����0.75/1.0/1.5/2.0��
        int densityDPI = dm.densityDpi;     // ��Ļ�ܶȣ�ÿ�����أ�120/160/240/320��
        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;

        Log.d(TAG + "  DisplayMetrics", "xdpi=" + xdpi + "; ydpi=" + ydpi);
        Log.d(TAG + "  DisplayMetrics", "density=" + density + "; densityDPI=" + densityDPI);

        int screenWidthDip = dm.widthPixels;        // ��Ļ��dip���磺320dip��
        int screenHeightDip = dm.heightPixels;
        Log.d(TAG + "  DisplayMetrics", "screenWidthDip=" + screenWidthDip + "; screenHeightDip=" + screenHeightDip);

        int screenWidth  = (int)(dm.widthPixels * density + 0.5f);      // ��Ļ��px���磺480px��
        int screenHeight = (int)(dm.heightPixels * density + 0.5f);
        Log.d(TAG + "  DisplayMetrics", "screenWidth=" + screenWidth + "; screenHeight=" + screenHeight);

    }

}
