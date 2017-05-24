package com.imax.ipt.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;
import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.common.PowerState;
import com.imax.ipt.controller.eventbus.handler.ui.MediaMenuLibraryEvent.MenuEvent;
import com.imax.ipt.model.Input;
import com.imax.ipt.model.Media;
import com.imax.ipt.service.ConnecteService;
import com.imax.ipt.service.HomeWatcherReceiver;
import com.imax.ipt.service.IPTService;
import com.imax.ipt.ui.activity.media.AddMediaActivity;
import com.imax.ipt.ui.activity.menu.MenuLibraryFragment;
import com.imax.ipt.ui.activity.menu.MenuMasterFragment;
import com.imax.ipt.ui.activity.power.SystemCountUpDownActivity;
import com.imax.ipt.ui.activity.power.SystemOffActivity;
import com.imax.ipt.ui.fragments.*;
import com.imax.ipt.ui.viewmodel.MenuLibraryElement;
import com.imax.ipt.ui.widget.picker.Picker;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressLint("NewApi")
public abstract class BaseActivity extends FragmentActivity {
    private static final String TAG = "BaseActivity";
    protected static boolean update = false;

    protected AutoCompleteFragment mAutoCompleteFragment;
    private NowPlayingFragment mPlayingFragment;
    private SystemStatusReceiver systemStatusReceiver;

    private ExecutorService executorService = Executors
            .newSingleThreadExecutor();

    private Picker mPicker;
//   private MediaNowPlayingFragment mMediaPlayingFragment;

//   private FrameLayout layoutContent;

    /**
     *
     */
    public void addMenuFragment(String menuName) {
        Bundle bundle = new Bundle();
        bundle.putString("menu_name", menuName);
        MenuMasterFragment mMasterMenuFragment = new MenuMasterFragment();
        mMasterMenuFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.masterMenuLayout, mMasterMenuFragment);
        transaction.commitAllowingStateLoss();

    }

    public void addMenuFragment() {
        addMenuFragment("");
    }

    public void addNavgationMenuFragment() {
        NavgationMenuFragment mNavigationMenuFragment = new NavgationMenuFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.masterMenuLayout, mNavigationMenuFragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.recover();
    }

    @Override
    protected void onResume() {
//        Log.d(TAG, "cll onResume");
        systemStatusReceiver = new SystemStatusReceiver();
        IntentFilter intentFilter = new IntentFilter(IPTService.BROADCAST_ACTION_STATUS_NOTIFICATION);
        intentFilter.addAction(IPTService.BROADCAST_ACTION_CONNECTION_LOST_IPT_SERVER);
        intentFilter.addAction(IPTService.BROADCAST_ACTION_SYSTEM_POWER_ON);
        intentFilter.addAction(IPTService.BROADCAST_ACTION_SYSTEM_POWERING_ON);
        intentFilter.addAction(IPTService.BROADCAST_ACTION_SYSTEM_POWER_OFF);
        intentFilter.addAction(IPTService.BROADCAST_ACTION_SYSTEM_POWERING_OFF);
        intentFilter.addAction(IPTService.BROADCAST_ACTION_MOVIE_METADATA_AVAILALBLE);
        intentFilter.addAction(IPTService.BROADCAST_ACTION_MUSIC_METADATA_AVAILALBLE);

        LocalBroadcastManager.getInstance(this).registerReceiver(systemStatusReceiver, intentFilter);

        addVolumeControlFragment();
        super.onResume();
        // retrieve the latest system state
        //    - system power state, system/tablet status

//        Intent iptServiceIntent = new Intent(this, IPTService.class).setAction(IPTService.ACTION_ACTIVITY_ONRESUME);
//        startService(iptServiceIntent);

//        Intent iptServiceIntent = new Intent(this, ConnecteService.class).setAction(IPTService.ACTION_ACTIVITY_ONRESUME);
//        startService(iptServiceIntent);

    }



    private static HomeWatcherReceiver mHomeKeyReceiver = null;

    private static void registerHomeKeyReceiver(Context context) {
        Log.i(TAG, "registerHomeKeyReceiver");
        mHomeKeyReceiver = new HomeWatcherReceiver();
        final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.registerReceiver(mHomeKeyReceiver, homeFilter);
    }

    private static void unregisterHomeKeyReceiver(Context context) {
        Log.i(TAG, "unregisterHomeKeyReceiver");
        if (null != mHomeKeyReceiver) {
            context.unregisterReceiver(mHomeKeyReceiver);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(systemStatusReceiver);
    }


    @Override
    protected void onDestroy() {
//        Log.d(TAG, "onDestroy=" + isTopActivity(this) + isDestroyed());
//        if (!isTopActivity(this)) {
//            IPTService.stop(this);
//        }
        if (!executorService.isShutdown()) {
            executorService.shutdownNow();
        }
        if (toast != null) {
            toast.cancel();
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(TAG, " cll ignore the back btn.");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected static boolean isTopActivity(Activity activity) {
        String packageName = "com.imax.ipt";
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            if (packageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /***
     * movie sort: title,,,
     */
    protected MenuLibraryFragment mMenuLibraryFragment;
    protected MovieSortFragment mMovieSortFragment;


    public void addMenuLibraryFragment(String title, List<MenuLibraryElement> mMenuOptions, int width) {

        mMenuLibraryFragment = new MenuLibraryFragment();
        mMenuLibraryFragment.setmTitle(title);
        mMenuLibraryFragment.setWidth(width);
        mMenuLibraryFragment.setmMenuOptions(mMenuOptions);
//        mMovieSortFragment = new MovieSortFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.menuLibraryFragment, mMenuLibraryFragment);
//        transaction.add(R.id.menuLibraryFragment, mMovieSortFragment);
        transaction.commitAllowingStateLoss();
    }

    /**
     * @param mMenuEvent for search icon
     */
    public void addAutoCompleteFragment(MenuEvent mMenuEvent) {
        mAutoCompleteFragment = new AutoCompleteFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(AutoCompleteFragment.MENU_EVENT_ID, mMenuEvent.ordinal());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mAutoCompleteFragment.setArguments(bundle);
        transaction.add(R.id.searchFragment, mAutoCompleteFragment);
        transaction.commitAllowingStateLoss();
    }

//   /**
//    * 
//    */
//   public void setAutoCompleteEvent(MenuEvent mMenuEvent)
//   {
//      if (mAutoCompleteFragment != null)
//      {
////         mAutoCompleteFragment.setmMenuEvent(mMenuEvent);
//      }
//   }

    /**
     * a,b,c,d,,,,z
     */
    public void addPickerFragment(int padding, int textSize) {
        Bundle bundle = new Bundle();
        Picker mPicker = new Picker();
        bundle.putInt(Picker.PADDING, padding);
        bundle.putInt(Picker.TEXTSIZE, textSize);
        mPicker.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.alphabetPickerFragment, mPicker);
        transaction.commitAllowingStateLoss();
    }

    /**
     *
     */
    public void addNowPlayingFragment(Media media) {
        if (media == null) {

            return;
        }

        mPlayingFragment = new NowPlayingFragment();
        mPlayingFragment.setMedia(media);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nowplayingFragment, mPlayingFragment);
        transaction.commitAllowingStateLoss();

    }

    public void addNowPlayingFragment(Input input) {
        if (input != null) {
            switch (input.getDeviceKind()) {
                case Movie:
                case Music:
                    // PlayingMovie and PlayingMusic event will provide playing media details
                    // changing of these 2 sources can be ignored
                    break;

                case NotSet: {
                    if (mPlayingFragment != null) {
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.remove(mPlayingFragment);
                        transaction.commitAllowingStateLoss();
                    }
                    break;
                }

                default: {
                    mPlayingFragment = new NowPlayingFragment();
                    mPlayingFragment.setDeviceType(input);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.nowplayingFragment, mPlayingFragment);
                    transaction.commitAllowingStateLoss();
                    break;
                }
            }
        }
    }

    /**
     *
     */
    public void setOptionForPickerFragment(String[] options, int padding, int textSize) {
        //edit by watershao
//	   String[] options1 = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        String options1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        mPicker = new Picker();
        Bundle bundle = new Bundle();
        bundle.putInt(Picker.PADDING, padding);
        bundle.putInt(Picker.TEXTSIZE, textSize);/* padding and textsize are fix on xml not used.*/
        /* begain add to hide the abcd,,,z sort but not hide year */
        for (String option : options) {
            if (options1.contains(option)) {
                bundle.putStringArray(Picker.KEY_OPTIONS, null);
                break;
            } else {
                bundle.putStringArray(Picker.KEY_OPTIONS, options);
                break;
            }

        }
//        bundle.putStringArray(Picker.KEY_OPTIONS, options);
        /* end add to hide the abcd,,,z sort but not hide year */

        mPicker.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.alphabetPickerFragment, mPicker);
        transaction.commitAllowingStateLoss();
    }

    public void removeOptionForPickerFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mPicker != null && mPicker.isAdded()) {
            transaction.remove(mPicker);
        }
        transaction.commitAllowingStateLoss();

    }

    public void removeMediaFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//      if (mMediaPlayingFragment != null && mMediaPlayingFragment.isAdded())
//      {
//         transaction.remove(mMediaPlayingFragment);
//      }
        if (mPlayingFragment != null && mPlayingFragment.isAdded()) {
            transaction.remove(mPlayingFragment);
        }
        transaction.commitAllowingStateLoss();

    }

    private StatusNotificationFragment mStatusNotificationFragment;

    protected void addStatusNotificationFragment(int systemStatusValue, byte tabletStatusValue) {
        Log.d(TAG, String.format("CLL SYSTEM STATUS IS " + systemStatusValue + "tabletstatus " + tabletStatusValue));
        if (systemStatusValue == 0 && tabletStatusValue == 0) {
            if (mStatusNotificationFragment != null) {
                // systemStatus all cleared
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.remove(mStatusNotificationFragment);
                if (!isFinishing() && !isDestroyed()) {
                    try {
                        transaction.commitAllowingStateLoss();
                    } catch (IllegalStateException e) {
                        Log.w(TAG, "statusNotifiaction", e);
                    }
                }
            }
        } else {
            if (findViewById(R.id.statusNotificationLayout) != null) {
                mStatusNotificationFragment = StatusNotificationFragment.newInstance(systemStatusValue, tabletStatusValue);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.statusNotificationLayout, mStatusNotificationFragment);
                if (!isFinishing() && !isDestroyed()) {
                    try {
                        transaction.commitAllowingStateLoss();
                    } catch (IllegalStateException e) {
                        Log.w(TAG, "statusNotifiaction", e);
                    }
                }
            }
        }

    }

    protected void addVolumeControlFragment() {
        if (findViewById(R.id.fragment_volume_control) != null) {
            VolumeControlFragment volumeControlFragment = new VolumeControlFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_volume_control, volumeControlFragment);
            transaction.commitAllowingStateLoss();
        }
    }


    /**
     *
     */
    private void recover() {
        // Thread.setDefaultUncaughtExceptionHandler(new
        // Thread.UncaughtExceptionHandler()
        // {
        // @Override
        // public void uncaughtException(Thread paramThread, Throwable
        // paramThrowable)
        // {
        // PendingIntent intent =
        // PendingIntent.getActivity(IPT.getInstance().getBaseContext(), 0,new
        // Intent(getIntent()), getIntent().getFlags());
        // AlarmManager mgr = (AlarmManager)
        // getSystemService(Context.ALARM_SERVICE);
        // mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, intent);
        // System.exit(0);
        // }
        // });
    }

    private static Toast toast = null;
    private long time = 0;

    private void iptServerConnectionLost() {
        Log.w(TAG, "iptServerConnectionLost!!!!!!!!!!!!1");

        //TODO optimize handling the exception of server connection lost.!!!
        Intent welcomeActivityIntent = new Intent(this, WelcomeActivity.class);
        welcomeActivityIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(welcomeActivityIntent);

        if (time > 20) { //try 5 times
            time = 0;
        } else {
            time = time + 1;
            return;
        }
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 300);
        TextView textView = new TextView(getApplicationContext());
        textView.setBackgroundResource(R.drawable.global_notification_bg2);
//        Drawable drawable = getResources().getDrawable(R.drawable.toast_prompt_left);
        Drawable drawable = getResources().getDrawable(R.drawable.global_warning_icn);

        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//        drawable.setBounds(0, 0,150,150);
        textView.setCompoundDrawables(drawable, null, null, null);

        textView.setTextColor(Color.WHITE);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);

//        if (IPTService.getWifiConnect()) {
        if (ConnecteService.getWifiConnect()) {
            textView.setText(R.string.warning_pc_not_connected);// here
        } else {
            textView.setText(R.string.warning_wifi_not_connected);
        }

        toast.setView(textView);
        toast.show();


    }

    private class SystemStatusReceiver extends BroadcastReceiver {
        private static final String TAG = "SystemStatusReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();
            Log.d(TAG, "intent received action: " + intentAction);

            if (intentAction.equals(IPTService.BROADCAST_ACTION_SYSTEM_POWER_ON)) {
                iptSystemPowerOn();
            } else if (intentAction.equals(IPTService.BROADCAST_ACTION_SYSTEM_POWERING_ON)) {
                iptSystemPoweringOn();
            } else if (intentAction.equals(IPTService.BROADCAST_ACTION_SYSTEM_POWER_OFF)) {
                iptSystemPowerOff();
            } else if (intentAction.equals(IPTService.BROADCAST_ACTION_SYSTEM_POWERING_OFF)) {
                iptSystemPoweringOff();
            } else if (intentAction.equals(IPTService.BROADCAST_ACTION_STATUS_NOTIFICATION)) {
                int statusValue = intent.getIntExtra(IPTService.EXTENDED_DATA_SYSTEM_STATUS, 0);
                byte tabletValue = intent.getByteExtra(IPTService.EXTENDED_DATA_TABLET_STATUS, (byte) 0);
                addStatusNotificationFragment(statusValue, tabletValue);

            } else if (intentAction.equals(IPTService.BROADCAST_ACTION_MOVIE_METADATA_AVAILALBLE)) {
                movieMetadataAvailable(intent);
            } else if (intentAction.equals(IPTService.BROADCAST_ACTION_MUSIC_METADATA_AVAILALBLE)) {
                musicMetadataAvailable(intent);
            } else if (intentAction.equals(IPTService.BROADCAST_ACTION_CONNECTION_LOST_IPT_SERVER)) {
                iptServerConnectionLost();
            }
        }

    }


    private void iptSystemPowerOn() {
        IPT.setPowerOn(true);
        Intent intent = new Intent(this, LobbyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    private void iptSystemPoweringOn() {
        Intent intent = new Intent(this, SystemCountUpDownActivity.class);
        intent.putExtra(SystemCountUpDownActivity.POWER_STATUS, PowerState.PoweringOn.getValue());
//      intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void iptSystemPowerOff() {
        Intent intent = new Intent(this, SystemOffActivity.class);
//      intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void iptSystemPoweringOff() {
        IPT.setPowerOn(false);
        Intent intent = new Intent(this, SystemCountUpDownActivity.class);
        intent.putExtra(SystemCountUpDownActivity.POWER_STATUS, PowerState.PoweringOff.getValue());
//      intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void movieMetadataAvailable(Intent request) {
        Intent intent = new Intent(this, AddMediaActivity.class);
        if (request.getExtras() != null &&
                request.getExtras().containsKey(AddMediaActivity.KEY_METADATA_MOVIE)) {
            intent.putParcelableArrayListExtra(AddMediaActivity.KEY_METADATA_MOVIE, request.getExtras().getParcelableArrayList(AddMediaActivity.KEY_METADATA_MOVIE));
        }
        startActivity(intent);
    }

    private void musicMetadataAvailable(Intent request) {
        Intent intent = new Intent(this, AddMediaActivity.class);

        Bundle bundle = request.getExtras();
        if (bundle != null && bundle.containsKey(AddMediaActivity.KEY_METADATA_MUSIC_ALBUM_TILES) && bundle.containsKey(AddMediaActivity.KEY_METADATA_MUSIC_ALBUM_COVERARTPATHS)) {
            intent.putExtra(AddMediaActivity.KEY_METADATA_MUSIC_ALBUM_TILES, request.getExtras().getStringArray(AddMediaActivity.KEY_METADATA_MUSIC_ALBUM_TILES));
            intent.putExtra(AddMediaActivity.KEY_METADATA_MUSIC_ALBUM_COVERARTPATHS, request.getExtras().getStringArray(AddMediaActivity.KEY_METADATA_MUSIC_ALBUM_COVERARTPATHS));
        }
        startActivity(intent);
    }
}
