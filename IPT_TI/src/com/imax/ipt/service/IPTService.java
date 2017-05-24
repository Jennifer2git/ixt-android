package com.imax.ipt.service;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.content.*;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.*;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.WindowManager;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.apk.update.util.RemoteUpdateUtil;
import com.imax.ipt.apk.update.util.UpdateChecker;
import com.imax.ipt.common.Constants;
import com.imax.ipt.conector.Client;
import com.imax.ipt.conector.ClientCallback;
import com.imax.ipt.conector.api.Request;
import com.imax.ipt.conector.api.Response;
import com.imax.ipt.controller.eventbus.handler.input.GetNowPlayingInputHandler;
import com.imax.ipt.controller.eventbus.handler.power.AutoPowerControlHandler;
import com.imax.ipt.controller.eventbus.handler.power.GetSystemPowerStateHandler;
import com.imax.ipt.controller.eventbus.handler.push.*;
import com.imax.ipt.controller.eventbus.handler.rooms.GetCurtainStateHandler;
import com.imax.ipt.controller.eventbus.handler.settings.maintenance.MaintenanceLoginHandler;
import com.imax.ipt.controller.eventbus.handler.settings.maintenance.PingHandler;
import com.imax.ipt.model.Movie;
import com.imax.ipt.model.SystemStatus;
import com.imax.ipt.model.TabletStatus;
import com.imax.ipt.ui.activity.DownloadActivity;
import com.imax.ipt.ui.activity.media.AddMediaActivity;
import com.imax.ipt.util.GSONIgnoredFieldUtil;
import com.imax.iptevent.EventBus;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class IPTService extends Service implements ClientCallback {
    private static final String ACTION_PING = "com.imax.ipt.service.PING";
    private static final String ACTION_SCREEN_ON = "com.imax.ipt.service.SCREEN_ON";
    private static final String ACTION_SCREEN_OFF = "com.imax.ipt.service.SCREEN_OFF";

    public static final String ACTION_ACTIVITY_ONRESUME = "com.imax.ipt.service.ACTIVITY_ONSUME";
    public static final String ACTION_BATTERY_LOW = "com.imax.ipt.service.BATTERY_LOW";
    public static final String ACTION_BATTERY_OKAY = "com.imax.ipt.service.BATTERY_OKAY";
    public static final String ACTION_CONNECTION_CONNECTED_IPT_SERVER = "com.imax.ipt.service.CONNECTED_IPT_SERVER";
    public static final String ACTION_CONNECTION_NOT_CONNECTED_IPT_SERVER = "com.imax.ipt.service.NOT_CONNECTED_IPT_SERVER";
    public static final String ACTION_CONNECTION_CONNECTED_WIFI = "com.imax.ipt.service.CONNECTED_IPT_WIFI";
    public static final String ACTION_CONNECTION_NOT_CONNECTED_WIFI = "com.imax.ipt.service.NOT_CONNECTED_IPT_WIFI";

    public static final String BROADCAST_ACTION_STATUS_NOTIFICATION = "com.imax.ipt.service.BROADCAST_ACTION_STATUS_NOTIFICATION";
    public static final String BROADCAST_ACTION_CONNECTION_LOST_IPT_SERVER = "com.imax.ipt.service.BROADCAST_ACTION_CONNECTION_LOST_IPT_SERVER";
    public static final String BROADCAST_ACTION_SYSTEM_POWER_ON = "com.imax.ipt.service.BROADCAST_ACTION_SYSTEM_POWER_ON";
    public static final String BROADCAST_ACTION_SYSTEM_POWERING_ON = "com.imax.ipt.service.BROADCAST_ACTION_SYSTEM_POWERING_ON";
    public static final String BROADCAST_ACTION_SYSTEM_POWER_OFF = "com.imax.ipt.service.BROADCAST_ACTION_SYSTEM_POWER_OFF";
    public static final String BROADCAST_ACTION_SYSTEM_POWERING_OFF = "com.imax.ipt.service.BROADCAST_ACTION_SYSTEM_POWERING_OFF";
    public static final String BROADCAST_ACTION_2HP_SYSTEM_POWERING_OFF = "com.imax.ihp.receivers.IPT_POWER_OFF";

    public static final String BROADCAST_ACTION_MOVIE_METADATA_AVAILALBLE = "com.imax.ipt.service.BROADCAST_ACTION_MOVIE_METADATA_AVAILABLE";
    public static final String BROADCAST_ACTION_MUSIC_METADATA_AVAILALBLE = "com.imax.ipt.service.BROADCAST_ACTION_MUSIC_METADATA_AVAILABLE";

    public static final String BROADCAST_ACTION_HOME_PREMIERE = "com.imax.ipt.service.BROADCAST_HOME_PREMIERE";

    public static final String BROADCAST_ACTION_APP_UPDATE_AVAILALBLE = "com.imax.ipt.service.BROADCAST_ACTION_APP_UPDATE_AVAILABLE";

    public static final String EXTENDED_DATA_SYSTEM_STATUS = "com.imax.ipt.service.EXTENDED_DATA_SYSTEM_STATUS";
    public static final String EXTENDED_DATA_TABLET_STATUS = "com.imax.ipt.service.EXTENDED_DATA_TABLET_STATUS";

    // TODO: make the SSID name configurable in Maintenance section
    public static final String NETWORK_SSID = "\"" + "IMAX" + "\"";
    public static final String NETWORK_PASS = "IMAXIPT4u";
//    public static final String NETWORK_SSID = "\"" + "TCL-IMAX" + "\"";
//   public static final String NETWORK_PASS = "tclimax20140403";

    private static final int UPDATE_AVAILIBALE = 1;

    private static UpdateChecker checker;
    private static int remoteVersionCode = 0;
    private static boolean mForceUpdate = false;
    private static int ALIVE = 1;
    private static int NOT_ALIVE = 0;

    public enum PowerState {
        Unknown(0), On(1), PoweringOff(2), Off(3), PoweringOn(4);

        private int value;

        PowerState(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static PowerState getPowerState(int value) {
            PowerState[] values = values();
            for (PowerState powerState : values) {
                if (powerState.value == value) return powerState;
            }
            throw new IllegalArgumentException("PowerState does not support");
        }
    }

    public static final String TAG = "IPTService";

    private static final ConcurrentHashMap<PushNotificationEnum, Class<? extends PushHandler>> pushHandlers = new ConcurrentHashMap<PushNotificationEnum, Class<? extends PushHandler>>();

    static {
        pushHandlers.put(PushNotificationEnum.playingMovieChanged, PlayingMovieChangedEvent.class);
        pushHandlers.put(PushNotificationEnum.playingMusicTrackChanged, PlayingMusicTrackChangedEvent.class);
        pushHandlers.put(PushNotificationEnum.pipModeChanged, PipModeChangedEvent.class);
        pushHandlers.put(PushNotificationEnum.audioFocusInputIdChanged, AudioFocusInputIdChangedEvent.class);
        pushHandlers.put(PushNotificationEnum.selectedInputChanged, SelectedInputChangedEvent.class);
        pushHandlers.put(PushNotificationEnum.systemPowerProgressChanged, SystemPowerProgressChangedEvent.class);
        pushHandlers.put(PushNotificationEnum.maintenanceLoginSessionExpired, MaintenanceLoginSessionExpiredEvent.class);
        pushHandlers.put(PushNotificationEnum.lightLevelChanged, LightLevelChangedEvent.class);
//        pushHandlers.put(PushNotificationEnum.lightLevelChanged, LightLevelChangedEvent.class);
        pushHandlers.put(PushNotificationEnum.setpointTemperatureChanged, SetpointTemperatureChangedEvent.class);
        pushHandlers.put(PushNotificationEnum.movieMetadataAvailable, MovieMetadataAvailableEvent.class);
        pushHandlers.put(PushNotificationEnum.musicAlbumMetadataAvailable, MusicAlbumMetadataAvailableEvent.class);
        pushHandlers.put(PushNotificationEnum.actualTemperatureChanged, ActualTemperatureChangedEvent.class);
        pushHandlers.put(PushNotificationEnum.movieAdded, MovieAddedEvent.class);
        pushHandlers.put(PushNotificationEnum.movieDeleted, MovieDeletedEvent.class);//added by jennifer 20150724
        pushHandlers.put(PushNotificationEnum.movieStarted, MovieStartedEvent.class);//added by jennifer 20150724
        pushHandlers.put(PushNotificationEnum.musicTrackAdded, MusicTrackAddedEvent.class);
        pushHandlers.put(PushNotificationEnum.audioMuteChanged, AudioMuteChangedEvent.class);
        pushHandlers.put(PushNotificationEnum.systemPowerStateChanged, SystemPowerStateChangedEvent.class);
        pushHandlers.put(PushNotificationEnum.mediaLoadProgressChanged, MediaLoadProgressChangedEvent.class);
        pushHandlers.put(PushNotificationEnum.videoModeChanged, VideoModeChangedEvent.class);
        pushHandlers.put(PushNotificationEnum.screenAspectRatioChanged, ScreenAspectRatioChangedEvent.class);
        pushHandlers.put(PushNotificationEnum.curtainStateChanged, CurtainStateChangedEvent.class);
        pushHandlers.put(PushNotificationEnum.selectedLightingPresetChanged, SelectedLightingPresetChangedEvent.class);
        pushHandlers.put(PushNotificationEnum.fanStateChanged, FanStateChangedEvent.class);
        pushHandlers.put(PushNotificationEnum.hvacSystemFaultStateChanged, HvacSystemFaultStateChangedEvent.class);
        pushHandlers.put(PushNotificationEnum.selectedSecutiyCameraLocationChanged, SelectedSecutiyCameraLocationChangedEvent.class);
        pushHandlers.put(PushNotificationEnum.screenShareButtonVisibilityChanged, ScreenShareButtonVisibilityChangedEvent.class);
        pushHandlers.put(PushNotificationEnum.nowPlayingChanged, NowPlayingChangedEvent.class);
        pushHandlers.put(PushNotificationEnum.powerSocketStateChanged, PowerSocketStateChangedEvent.class);
        pushHandlers.put(PushNotificationEnum.systemStatusNotification, SystemStatusNotificationEvent.class);
        pushHandlers.put(PushNotificationEnum.requestClientLog, RequestClientLogEvent.class);

        pushHandlers.put(PushNotificationEnum.pcabCalibrationStateChanged, PcabCalibrationStateChangedEvent.class);
        pushHandlers.put(PushNotificationEnum.upsStateChanged, UpsStateChangedEvent.class);
        pushHandlers.put(PushNotificationEnum.generalNotification, GeneralNotificationEvent.class);
//        pushHandlers.put(PushNotificationEnum.setInputName, SetInputNameHandler.class);

    }

    // public static final int ON = 1;
    // public static final int POWERING_OFF = 2;
    // public static final int OFF = 3;
    // public static final int POWERING_ON = 4;

    private Client mClient;
    private EventBus mEventBus;
    private SparseArray<Request> sparseArray = new SparseArray<Request>();
    private Gson gson;
    private BroadcastReceiver mScreenReceiver;
    private BatteryReceiver batteryReceiver;
    private PingBroadcastReceiver pingBroadcastReceiver;
    private HomePremiereReceiver mHomePremiereReceiver;
    private SystemStatus systemStatus = new SystemStatus();
    private static TabletStatus tabletStatus = new TabletStatus();

    private void sendRequest(Request objRequest) {
        if (mClient == null) {
            Log.w(TAG, "Request not sent, client not yet initialized: " + objRequest.toString());
            return;
        }

        Gson gson = new GsonBuilder().setExclusionStrategies(new GSONIgnoredFieldUtil()).create();
        String request = gson.toJson(objRequest);
        Log.d(TAG, request);
        mClient.sendRequest(request);
    }

    /**
     * @param request
     */
    public void onEventAsync(Request request) {
        sparseArray.put(request.getId(), request);
        this.sendRequest(request);
    }

    /**
     *
     */
    @Override
    public void messageReceived(String message) {
        Log.d("post-response", "msg=" + message);
        Response response = gson.fromJson(message, Response.class);
        Request request = null;
        if ((request = sparseArray.get(response.getId())) != null) {
            request.getHandler().onCreateEvent(mEventBus, message);
        } else {
            Log.d(TAG, "Push Notification ::" + message);
            if (response.getMethod() != null) {
                Class<? extends PushHandler> clazzPushHandler = null;

                PushNotificationEnum pushNotificationEnum = null;
                try {
                    pushNotificationEnum = PushNotificationEnum.valueOf(response.getMethod());
                } catch (IllegalArgumentException e) {
                    // push notification is not registered
                    Log.w(TAG, "Push notification is not recognized: " + response.getMethod());
                    return;
                }

                if ((clazzPushHandler = pushHandlers.get(pushNotificationEnum)) != null) {
                    PushHandler handler;
                    try {
                        handler = clazzPushHandler.newInstance();
                        try {
                            handler.execute(mEventBus, message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (InstantiationException e) {
                        Log.e(TAG, e.getMessage(), e);
                    } catch (IllegalAccessException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
            }
        }
    }

    /**
     *
     */
    @Override
    public void exceptionCaught(Throwable cause) {
        Log.e(TAG, "cll exceptionCaught=" + cause.getMessage(), cause);
        tabletStatus.setIptServerNotConnected(true);
        refreshStatusNotification();

        // display the connecting screen
        Intent iptServerConnectionLostIntent = new Intent(BROADCAST_ACTION_CONNECTION_LOST_IPT_SERVER);
        LocalBroadcastManager.getInstance(this).sendBroadcast(iptServerConnectionLostIntent);

        if (mClient != null) {
            mClient.cancel();
            mClient = null;
        }

        // wait 10 second before re-connecting
        //TODO this retry may need move to error catch.
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        connect();


        // // TODO Allow just one time .
        // Intent intentService = new Intent(getBaseContext(), IPTService.class);
        // PendingIntent intent =
        // PendingIntent.getService(getApplicationContext(), 0, intentService, 0);
        // AlarmManager mgr = (AlarmManager)
        // getSystemService(Context.ALARM_SERVICE);
        // mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 10000, intent);
        // stopSelf();
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "cll onCreate");
        super.onCreate();
        mEventBus = IPT.getInstance().getEventBus();
        mEventBus.register(this);
        gson = new Gson();
//        this.connect(); //here remove this fuction to ConnecteService.

        pingBroadcastReceiver = new PingBroadcastReceiver(getApplicationContext());

        // broadcast listener for listenering to screen off/on events
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mScreenReceiver = new ScreenReceiver();
        registerReceiver(mScreenReceiver, filter);

        batteryReceiver = new BatteryReceiver();
        IntentFilter inf = new IntentFilter();
//        inf.addAction(Intent.ACTION_BATTERY_OKAY);
        inf.addAction(Intent.ACTION_BATTERY_CHANGED);
        inf.addAction(Intent.ACTION_BATTERY_LOW);
        registerReceiver(batteryReceiver, inf);

        Settings.System.putInt(getContentResolver(), Settings.Global.WIFI_SLEEP_POLICY, Settings.Global.WIFI_SLEEP_POLICY_NEVER);
        checkInitialBatteryLevel();
        checkWifiConnectionStatus();

        Intent logManagementServiceIntent = new Intent(this, LogManagementService.class);
        startService(logManagementServiceIntent);

//        registerHomeKeyReceiver(this);

    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mEventBus.post(new GetNowPlayingInputHandler().getRequest());
            Log.d(TAG, "cll handleMessage is live or not =" + msg.what);
//            screenOn(0);
        }

    };

    private void heartbeat() {
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Message msg = mHandler.obtainMessage();
                if (mClient != null) {
                    if (mClient.isAlive()) {
                        msg.what = ALIVE;
                    }
                }
                msg.what = NOT_ALIVE;
                msg.sendToTarget();
                Log.d(TAG, "cll heartbeat");
            }
        }, 3000);

    }


    public void checkInitialBatteryLevel() {
        // query the initial battery status
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPercentage = level / (float) scale;
        Log.d(TAG, "Battery (startup %) -> " + batteryPercentage);

        if (batteryPercentage <= 0.15f) {
            tabletStatus.setBatteryLow(true);
            refreshStatusNotification();
        }
    }

    public void checkWifiConnectionStatus() {
        ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected()) {
            tabletStatus.setIptWifiNotConnected(true);
            refreshStatusNotification();
        } else {
            WifiReceiver.checkConnectedWifiSSID(this);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.w(TAG, "IPT Running on low memory");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "onStartCommand");
        if (intent == null) {
            return START_STICKY;
        }


        Log.d(TAG, " cll onStartCommand action=" + intent.getAction());
        init();

        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();

            if (action.equals(ACTION_ACTIVITY_ONRESUME)) {

                Log.d(TAG, "onStartCommand action systemStatus =" + systemStatus.getStatusValue());
                if (systemStatus != null) {
                    refreshStatusNotification();
                }

                if (systemStatus.getStatusValue() == 0) {

//                    iptSystemPowerOn();
                Log.d(TAG, "onStartCommand action systemStatus = 0");
                }

//        IntentFilter intentFilterHP = new IntentFilter(IPTService.BROADCAST_ACTION_STATUS_NOTIFICATION);
//        intentFilterHP.addAction(IPTService.BROADCAST_ACTION_HOME_PREMIERE);
//        mHomePremiereReceiver = new HomePremiereReceiver();
//        registerReceiver(mHomePremiereReceiver, intentFilterHP);
//        Log.d(TAG, "cll registerReceiver " );


            } else if (action.equals(ACTION_SCREEN_ON)) {
                // stop PingBroadcastReceiver when the tablet is being used and
                // active
                pingBroadcastReceiver.stop();

                // get the latest system power state
//            getSystemPowerState();
            } else if (action.equals(ACTION_SCREEN_OFF)) {
                // start PingBroadcastReceiver to allow the socket connection to
                // stay connected,
                // or detected disconnection
                pingBroadcastReceiver.start(60);
            } else if (action.equals(ACTION_PING)) {
                // Ping the server
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        pingIptServer();
                    }

                }).start();
            } else if (action.equals(ACTION_BATTERY_LOW)) {
                tabletStatus.setBatteryLow(true);
                refreshStatusNotification();
            } else if (action.equals(ACTION_BATTERY_OKAY)) {
//                tabletStatus.setBatteryLow(false);
//                refreshStatusNotification();
            } else if (action.equals(ACTION_CONNECTION_CONNECTED_WIFI)) {
                tabletStatus.setIptWifiNotConnected(false);
                refreshStatusNotification();

            } else if (action.equals(ACTION_CONNECTION_NOT_CONNECTED_WIFI)) {
                // display the connecting (welcome) screen)
                Intent iptServerConnectionLostIntent = new Intent(IPTService.BROADCAST_ACTION_CONNECTION_LOST_IPT_SERVER);
                LocalBroadcastManager.getInstance(this).sendBroadcast(iptServerConnectionLostIntent);

                tabletStatus.setIptWifiNotConnected(true);
                refreshStatusNotification();

            } else if (action.equals(ACTION_CONNECTION_NOT_CONNECTED_IPT_SERVER)) {
                Intent iptServerConnectionLostIntent = new Intent(IPTService.BROADCAST_ACTION_CONNECTION_LOST_IPT_SERVER);
                LocalBroadcastManager.getInstance(this).sendBroadcast(iptServerConnectionLostIntent);

                tabletStatus.setIptServerNotConnected(true);
                refreshStatusNotification();

            }
        }

        return START_STICKY;
    }


    public static boolean getWifiConnect() {
        return !tabletStatus.isIptWifiNotConnected();
    }

    private void refreshStatusNotification() {
        Intent statusIntent = new Intent(BROADCAST_ACTION_STATUS_NOTIFICATION).putExtra(EXTENDED_DATA_SYSTEM_STATUS, systemStatus.getStatusValue()).putExtra(EXTENDED_DATA_TABLET_STATUS, tabletStatus.toByte());
        LocalBroadcastManager.getInstance(this).sendBroadcast(statusIntent);
    }

    public static void actionPing(Context context) {
        Intent i = new Intent(context, IPTService.class);
        i.setAction(ACTION_PING);
        context.startService(i);

    }

    public static void actionScreenOn(Context context) {
        Intent i = new Intent(context, IPTService.class);
        i.setAction(ACTION_SCREEN_ON);
        context.startService(i);
    }

    public static void actionScreenOff(Context context) {
        Intent i = new Intent(context, IPTService.class);
        i.setAction(ACTION_SCREEN_OFF);
        context.startService(i);
    }


    private void init() {
        IPT.getInstance().getIPTContext().put(IPT.MAINTENANCE_LOGIN, MaintenanceLoginHandler.MAINTENANCE_RESPONSE_BAD);
    }

    public static void fire(Context context) {
        Intent intent = new Intent(context, IPTService.class);
        context.startService(intent);
    }

    public static void stop(Context context) {
        Log.d(TAG, "cll stop");

        Intent intent = new Intent(context, IPTService.class);
        context.stopService(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "cll onDestroy");
        super.onDestroy();
//        unregisterHomeKeyReceiver(this);
        mEventBus.unregister(this);
        new Thread(new Runnable() {

            @Override
            public void run() {
                if (mClient != null) {
                    mClient.cancel();
                }
            }
        }).start();

        unregisterReceiver(mScreenReceiver);
        unregisterReceiver(batteryReceiver);
//        unregisterReceiver(mHomePremiereReceiver);
//        mHomePremiereReceiver = null;

        pingBroadcastReceiver.stop();
        pingBroadcastReceiver = null;
    }

    private void connect() {
//        if (tabletStatus.isIptWifiNotConnected()) {
////         connectIptWifiNetwork();
//        }

        /*add to distory bad connection */
        if (mClient != null) {
            mClient.cancel();
            mClient = null;
        }
        mClient = new Client(this);
        mClient.connect();


    }

    Timer mUpdateTimer = new Timer();
    TimerTask mUpdateTimerTask = new TimerTask() {
        @Override
        public void run() {
            checker = new UpdateChecker(IPTService.this);
            remoteVersionCode = checker.checkForUpdateByVersionCode(Constants.URL_VERSION);
            mForceUpdate = checker.isForceUpdate();
            if (!checker.isUpdateAvailable()) {
                return;
            }

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(IPTService.this);
            int latestVersionCodePrompted = prefs.getInt(RemoteUpdateUtil.KEY_PREFERENCE_LATEST_VERSION_CODE_PROMPTED_FOR_UPGRADE, 0);
            if (remoteVersionCode <= latestVersionCodePrompted) {
                return;
            }
            Log.d(TAG, "cll here update awailable " + ", forceupdate: " + mForceUpdate);
            Message msg = new Message();
            msg.what = UPDATE_AVAILIBALE;
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.UPDATE_FORCE_UPDATE, mForceUpdate);
            bundle.putInt(Constants.UPDATE_REMOTE_VERSION_CODE, remoteVersionCode);
            msg.setData(bundle);
            mUpdateHandler.sendMessage(msg);

        }
    };

    Handler mUpdateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case UPDATE_AVAILIBALE:
                    Bundle bundle = msg.getData();
                    showUpdateAppDialog(bundle.getBoolean(Constants.UPDATE_FORCE_UPDATE), bundle.getInt(Constants.UPDATE_REMOTE_VERSION_CODE));
                    break;
            }
            super.handleMessage(msg);

        }
    };
//    Thread updateThread = new Thread(new Runnable() {
//        @Override
//        public void run() {
//            checker = new UpdateChecker(IPTService.this);
//            remoteVersionCode = checker.checkForUpdateByVersionCode(Constants.URL_VERSION);
//            mForceUpdate = checker.isForceUpdate();
//            if (!checker.isUpdateAvailable()) {
//                return;
//            }
//            // TODO why this error ? with service thread?
//
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(IPTService.this);
//            int latestVersionCodePrompted = prefs.getInt(RemoteUpdateUtil.KEY_PREFERENCE_LATEST_VERSION_CODE_PROMPTED_FOR_UPGRADE, 0);
//            if (remoteVersionCode <= latestVersionCodePrompted) {
//                return;
//            }
//            Log.d(TAG, "cll here update awailable " + ", forceupdate: " + mForceUpdate);
//            Message msg = new Message();
//            msg.what = UPDATE_AVAILIBALE;
//            Bundle bundle = new Bundle();
//            bundle.putBoolean(Constants.UPDATE_FORCE_UPDATE,mForceUpdate);
//            bundle.putInt(Constants.UPDATE_REMOTE_VERSION_CODE,remoteVersionCode);
//            msg.setData(bundle);
//            mUpdateHandler.sendMessage(msg);
//
//            Log.e(TAG, "update check timerThread after 120s only check one times");
//
//        }
//    });


    @Override
    public void isConnected() {
        // get all the inputs and store them in memory -- this is good for performance.
        Log.d(TAG, "cll isConnected");
//        this.mEventBus.post(new GetActiveDeviceTypesHandler().getRequest());//remove this event by jennifer

//        mUpdateTimer.schedule(mUpdateTimerTask, Constants.DELAY_TIME);

        /* getSystemPowerState after GetActiveDeviceTypes is returned
         so that the menu can have data to populate */
//      getSystemPowerState();
//        getCurtainState();

        tabletStatus.setIptServerNotConnected(false);
        refreshStatusNotification();

    }

    /**
     * now not use connect IMAX autoly
     */
    private void connectIptWifiNetwork() {
        boolean networkConfigured = false;

        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            Log.e(TAG, "Wifi is NOT ENABLED, connection to IPT server will not succeeded");
            wifiManager.setWifiEnabled(true);
            boolean enable = false;
            while (!enable) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (wifiManager.isWifiEnabled()) {
                    enable = true;
                }
            }

            //         return;
        }

        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration i : list) {
            if (i.SSID.equals(NETWORK_SSID)) {
                networkConfigured = true;
            }
        }

        if (!networkConfigured) {
            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = NETWORK_SSID;
            conf.hiddenSSID = true;
            conf.status = WifiConfiguration.Status.ENABLED;
//            conf.preSharedKey = "\"" + NETWORK_PASS + "\"";


            // Add to Android wifi settings
            int res = wifiManager.addNetwork(conf);
            boolean b = wifiManager.enableNetwork(res, true);
            boolean es = wifiManager.saveConfiguration();
        }

        for (WifiConfiguration i : list) {
            if (i.SSID != null && i.SSID.equals(NETWORK_SSID)) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                boolean reconnectionSucceeded = wifiManager.reconnect();
                break;
            }
        }
    }

    /**
     * @param powerState
     */
    private void switchPowerState(PowerState powerState) {
        Log.i(TAG, "PowerState changed: " + powerState.toString());
        Intent iptSystemPowerStateChangedIntent;

        switch (powerState) {
            case On://todo:when not stop service powerstate on
                iptSystemPowerStateChangedIntent = new Intent(BROADCAST_ACTION_SYSTEM_POWER_ON);
                LocalBroadcastManager.getInstance(this).sendBroadcast(iptSystemPowerStateChangedIntent);
                break;
            case Off:
                iptSystemPowerStateChangedIntent = new Intent(BROADCAST_ACTION_SYSTEM_POWER_OFF);
                LocalBroadcastManager.getInstance(this).sendBroadcast(iptSystemPowerStateChangedIntent);
                break;
            case PoweringOff:
                iptSystemPowerStateChangedIntent = new Intent(BROADCAST_ACTION_SYSTEM_POWERING_OFF);
                LocalBroadcastManager.getInstance(this).sendBroadcast(iptSystemPowerStateChangedIntent);
                break;
            case PoweringOn:
                iptSystemPowerStateChangedIntent = new Intent(BROADCAST_ACTION_SYSTEM_POWERING_ON);
                LocalBroadcastManager.getInstance(this).sendBroadcast(iptSystemPowerStateChangedIntent);
                break;
            default:
                Log.d(TAG, "System power status case is  not supported :" + powerState);
                Toast.makeText(this, "System Power State is not setup yet .", Toast.LENGTH_LONG).show();
                break;
        }

    }

    /**
     * Server interaction
     */
    /**
     * Request
     */
    private void getCurtainState() {
        this.mEventBus.post(new GetCurtainStateHandler().getRequest());
    }

    /**
     * Reponse
     *
     * @param getCurtainStateHandler
     */
    public void onEvent(GetCurtainStateHandler getCurtainStateHandler) {
        // IPT.getInstance().getIPTContext().put(IPT.STATE_CURTAINS,
        // getCurtainStateHandler.getStateCurtain());
        IPT.getInstance().getIPTContext().put(IPT.STATE_CURTAINS, getCurtainStateHandler.getCurtainState());
    }

    public void onEvent(final CurtainStateChangedEvent curtainStateChangedEvent) {
        Log.d(TAG, "Changing IPT Curtain State: " + curtainStateChangedEvent.getCurtainState());
        IPT.getInstance().getIPTContext().put(IPT.STATE_CURTAINS, curtainStateChangedEvent.getCurtainState());
    }

    private void getSystemPowerState() {
        Log.d(TAG, "getSystemPowerState :::::");
        GetSystemPowerStateHandler getSystemPowerStateHandler = new GetSystemPowerStateHandler();
        this.mEventBus.post(getSystemPowerStateHandler.getRequest());
    }

    /**
     * @param getSystemPowerStateHandler
     */
//    public void onEventMainThread(GetSystemPowerStateHandler getSystemPowerStateHandler) {
//        Log.d(TAG, "onEventMainThread GetSystemPowerStateHandler:::::");
//        this.switchPowerState(getSystemPowerStateHandler.getPowerState());
//    }

    public void onEvent(MovieMetadataAvailableEvent movieMetadataAvailableEvent) {
        // mEventBus.postSticky(movieMetadataAvailableEvent.getmMoviesMeta());
//      AddMediaActivity.fire(getApplicationContext(), (ArrayList<Movie>) movieMetadataAvailableEvent.getmMoviesMeta());

        Intent movieMetadataAvailalbeIntent = new Intent(BROADCAST_ACTION_MOVIE_METADATA_AVAILALBLE);
        movieMetadataAvailalbeIntent.putExtra(AddMediaActivity.KEY_METADATA_MOVIE, (ArrayList<Movie>) movieMetadataAvailableEvent.getmMoviesMeta());
        LocalBroadcastManager.getInstance(this).sendBroadcast(movieMetadataAvailalbeIntent);
    }

    public void onEvent(MusicAlbumMetadataAvailableEvent musicAlbumMetadataAvailableEvent) {
        // mEventBus.postSticky(musicAlbumMetadataAvailableEvent.getmMoviesMeta());
//      AddMediaActivity.fire(getApplicationContext(), musicAlbumMetadataAvailableEvent.getAlbumTitles(), musicAlbumMetadataAvailableEvent.getCoverArtPaths());

        Intent musicMetadataAvailalbeIntent = new Intent(BROADCAST_ACTION_MUSIC_METADATA_AVAILALBLE);
        musicMetadataAvailalbeIntent.putExtra(AddMediaActivity.KEY_METADATA_MUSIC_ALBUM_TILES, musicAlbumMetadataAvailableEvent.getAlbumTitles());
        musicMetadataAvailalbeIntent.putExtra(AddMediaActivity.KEY_METADATA_MUSIC_ALBUM_COVERARTPATHS, musicAlbumMetadataAvailableEvent.getCoverArtPaths());
        LocalBroadcastManager.getInstance(this).sendBroadcast(musicMetadataAvailalbeIntent);
    }


    /**
     * @param systemPowerStateChangedEvent
     */
//    public void onEvent(SystemPowerStateChangedEvent systemPowerStateChangedEvent) {
//        Log.d(TAG, "power state=" + systemPowerStateChangedEvent.getmPowerState());
//        this.switchPowerState(systemPowerStateChangedEvent.getmPowerState());
//    }

    /**
     * @param maintenanceLoginSessionExpiredEvent
     */
    public void onEvent(MaintenanceLoginSessionExpiredEvent maintenanceLoginSessionExpiredEvent) {
        IPT.getInstance().getIPTContext().put(IPT.MAINTENANCE_LOGIN, MaintenanceLoginHandler.MAINTENANCE_RESPONSE_BAD);
    }

//    public void onEvent(GetNowPlayingInputHandler getNowPlayingInputHandler) {
//        Input nowPlaying = getNowPlayingInputHandler.getNowPlayingInput();
//        Log.d("test", "iptService getNowPlayingInput=" + nowPlaying.getDeviceKind());
//        switch (nowPlaying.getDeviceKind()) {
//            case MediaPlayer:
//            case Movie:
//                // retrieve the playing movie
//                mEventBus.post(new GetPlayingMovieHandler().getRequest());
//                break;
//
//            case Music:
//                mEventBus.post(new GetPlayingMusicTrackHandler().getRequest());
//                break;
//
//            // default:
//            // mMediaLibraryActivity.addNowPlayingFragment(nowPlaying);
//            // break;
//        }
//    }

    private Object syncPingObject = new Object();

    public void pingIptServer() {
        // send the Ping message to the Server
        mEventBus.post(new PingHandler().getRequest());

        // wait for the proper response
        synchronized (syncPingObject) {
            try {
                Log.d(TAG, "syncPingObject Wait...");
                syncPingObject.wait(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "syncPingObject Wait DONE");

        PingBroadcastReceiver.unlock();

    }

    public void onEvent(PingHandler pingHandler) {
        Log.d(TAG, "onEvent PingHandler received");
        synchronized (syncPingObject) {
            syncPingObject.notifyAll();
        }
    }


    public void onEvent(SystemStatusNotificationEvent systemStatusNotificationEvent) {
        systemStatus = systemStatusNotificationEvent.getStatus();
        Intent statusIntent = new Intent(BROADCAST_ACTION_STATUS_NOTIFICATION)
                .putExtra(EXTENDED_DATA_SYSTEM_STATUS, systemStatusNotificationEvent.getStatus().getStatusValue())
                .putExtra(EXTENDED_DATA_TABLET_STATUS, tabletStatus.toByte());
        LocalBroadcastManager.getInstance(this).sendBroadcast(statusIntent);
    }

//    private Map<DeviceKind, Vector<Input>> inputsByDeviceKind = new ConcurrentHashMap<DeviceKind, Vector<Input>>();

//    public void onEvent(final GetActiveDeviceTypesHandler getActiveDeviceTypesHandler) {
//
//        Log.d(TAG, "onEvent GetActiveDeviceTypesHandler");
//        Vector<DeviceType> deviceTypes = new Vector<DeviceType>();
//        inputsByDeviceKind.clear();
//        if (getActiveDeviceTypesHandler.getDeviceTypes() == null) {
//            Log.e(TAG, "get active device type error!");
//            return;
//
//        }
//        DeviceType[] activeDeviceTypes = getActiveDeviceTypesHandler.getDeviceTypes();
//
//        for (int i = activeDeviceTypes.length - 1; i >= 0; i--) {
//            deviceTypes.add(activeDeviceTypes[i]);
//            if (activeDeviceTypes[i] == null) {
//                continue;
//            }
//            inputsByDeviceKind.put(activeDeviceTypes[i].getDeviceKind(), new Vector<Input>());
//        }
//
////        for (DeviceType deviceType : getActiveDeviceTypesHandler.getDeviceTypes()) {
////            deviceTypes.add(deviceType);
////            inputsByDeviceKind.put(deviceType.getDeviceKind(), new Vector<Input>());
////        }
//
////        mUpdateTimer.cancel();
//
//        this.mEventBus.post(new GetInputsByDeviceKindHandler_ForIptServiceOnly(DeviceKind.ALL.getDeviceKind()).getRequest());
//        IPT.getInstance().getIPTContext().put(IPT.ACTIVE_DEVICE_TYPES, deviceTypes);
//
//        getSystemPowerState();
//    }

//    // todo why need this event -- just for search input by devicekind. no other action.
//    public void onEvent(final GetInputsByDeviceKindHandler_ForIptServiceOnly getInputsByDeviceKindHandler) {
//        Input[] allInputs = getInputsByDeviceKindHandler.getInputs();
//        for (Input input : allInputs) {
////            if(input ==null){
////                continue;
////            }
//            if (inputsByDeviceKind.containsKey(input.getDeviceKind())) {
//                inputsByDeviceKind.get(input.getDeviceKind()).add(input);
//            }
//        }
//
//        IPT.getInstance().getIPTContext().put(IPT.INPUTS_BY_DEVICE_KIND, inputsByDeviceKind);
//    }


//    public void onEventMainThread(UpsStateChangedEvent ups) {
//        showDialogUps(ups.getPowerState());
//    }

    private AlertDialog dialog;
    static private AlertDialog dialogUpdate;

    public void showUpdateAppDialog(Boolean forceUpdate, int remoteVersionCode) {

        Log.d(TAG, "cll showUpdateAppDialog");
        Builder builder = new Builder(getApplicationContext());

        if (forceUpdate) {
            builder.setMessage(getString(R.string.dialog_message_software_update_force))

                    .setTitle(getString(R.string.dialog_title_software_update_available))
                    .setPositiveButton(getString(R.string.response_ok), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "force update setPositiveButton");
                            Intent intent = new Intent(getApplicationContext(), DownloadActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }
                    });

        } else {

            Log.d(TAG, "cll showUpdateAppDialog option");
            builder.setMessage(getString(R.string.dialog_message_software_update_available))
                    .setTitle(getString(R.string.dialog_title_software_update_available))
                    .setPositiveButton(getString(R.string.response_yes), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(getApplicationContext(), DownloadActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            Log.d(TAG, "update setPositiveButton");
                        }
                    })
                    .setNegativeButton(getString(R.string.response_no), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "Software update declined setNegativeButton");

                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            prefs.edit().putInt(RemoteUpdateUtil.KEY_PREFERENCE_LATEST_VERSION_CODE_PROMPTED_FOR_UPGRADE, remoteVersionCode).commit();
                            int version = prefs.getInt(RemoteUpdateUtil.KEY_PREFERENCE_LATEST_VERSION_CODE_PROMPTED_FOR_UPGRADE, 0);
                            Log.w(TAG, "Software update declined the last version is " + version + ", remoteVersionCode " + remoteVersionCode);
                        }
                    });
        }

        dialogUpdate = builder.create();
        dialogUpdate.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialogUpdate.setCancelable(false);
        dialogUpdate.setCanceledOnTouchOutside(false);
        dialogUpdate.show();
    }

    public void showDialogUps(int type) {
        if (type == -1 && dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            return;
        }

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            return;
        }

        Builder builder = new Builder(getApplicationContext());
        dialog = builder.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.setTitle(R.string.prompt_warning);
        if (type == 0) {
            dialog.setMessage(getResources().getString(R.string.prompt_ac_poweroff));
        }
        if (type == 1) {
            dialog.setMessage(getResources().getString(R.string.prompt_ac_system_problem));
        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        if (type == 0 || type == 1) {
            dialog.show();
            Log.d(TAG, "cll send cmd to HP to power off");

            Intent intent = new Intent();
            intent.setAction(IPTService.BROADCAST_ACTION_2HP_SYSTEM_POWERING_OFF);
            intent.putExtra("IPT_SYSTEM_STATE", "powering_off");
            sendBroadcast(intent);
        }
    }

    public void onEventMainThread(GeneralNotificationEvent general) {
//	   Toast.makeText(getApplicationContext(), "state="+ups.getPowerState(), Toast.LENGTH_LONG).show();
//	   showDialogShutdown(general.getState());
        screenOn(general.getState());
    }

    private KeyguardLock kl;
    private WakeLock wl;

    private void screenOn(int type) {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //��ȡ��Դ����������
        wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");

        //��ȡPowerManager.WakeLock����,����Ĳ���|��ʾͬʱ��������ֵ,������LogCat���õ�Tag
        wl.acquire();
        //������Ļ

        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        //�õ�����������������
        kl = km.newKeyguardLock("unLock");
        //������LogCat���õ�Tag
        kl.disableKeyguard();
        //����

//	    try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//
//			e.printStackTrace();
//		}
        showDialogShutdown(type);

//       kl.reenableKeyguard();  
        //���������Զ�����
//       wl.release();  
        //�ͷ�
    }

    private static AlertDialog shutdownDialog;
    private MyCount mc = new MyCount(30000, 1000);

    /**
     * ��ʾ�ر�ϵͳ
     *
     * @param type
     */
    public void showDialogShutdown(int type) {
        if (type == -1 && shutdownDialog != null && shutdownDialog.isShowing()) {
            shutdownDialog.dismiss();
            return;
        }

        if (shutdownDialog != null && shutdownDialog.isShowing()) {
            shutdownDialog.dismiss();
        }

        Builder builder = new Builder(getApplicationContext());


//		builder.setCancelable(false);
        shutdownDialog = builder.create();
        shutdownDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        shutdownDialog.setTitle(R.string.prompt_general);
        if (type == 0) {
            shutdownDialog.setMessage(getResources().getString(R.string.prompt_shutdown));
        }

        shutdownDialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getText(R.string.zaxel_ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                mEventBus.post(new AutoPowerControlHandler(true).getRequest());
                mc.cancel();
                dialog.cancel();
            }
        });

        shutdownDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getText(R.string.zaxel_cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mEventBus.post(new AutoPowerControlHandler(false).getRequest());
                mc.cancel();
                dialog.cancel();
            }
        });

//		shutdownDialog.setCancelable(false);
        shutdownDialog.setCanceledOnTouchOutside(false);
        if (type == 0 || type == 1) {
            shutdownDialog.show();
            mc.start();
        }
    }

    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            if (shutdownDialog != null && shutdownDialog.isShowing()) {
                shutdownDialog.setMessage(getResources().getString(R.string.prompt_shutdown) + " (0)");
                mEventBus.post(new AutoPowerControlHandler(true).getRequest());
                shutdownDialog.cancel();
                mc.cancel();

                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                kl.reenableKeyguard();
                wl.release();
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (shutdownDialog != null && shutdownDialog.isShowing()) {

                shutdownDialog.setMessage(getResources().getString(R.string.prompt_shutdown) + " (" + millisUntilFinished / 1000 + ")");
            }

        }
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
}

