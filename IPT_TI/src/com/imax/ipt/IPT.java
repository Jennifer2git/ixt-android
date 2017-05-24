package com.imax.ipt;

import android.app.Application;
import android.os.StrictMode;
import android.util.Log;
import com.imax.iptevent.EventBus;

import java.util.concurrent.ConcurrentHashMap;

public class IPT extends Application {

    public static final String STATE_CURTAINS = "STATE_CURTAINS";
    public static final String POWER_STATUS = "POWER_STATUS";
    public static final String SHARE_BUTTON = "SHARE_BUTTON";
    public static final String MAINTENANCE_LOGIN = "MAINTENANCE_LOGIN";

    public static final String ACTIVE_DEVICE_TYPES = "ACTIVE_DEVICE_KINDS";
    public static final String INPUTS_BY_DEVICE_KIND = "INPUTS_BY_DEVICE_KIND";

    private static boolean connected = false;
    private static boolean powerOn = false;

    private static IPT instance;
    private EventBus eventBus = new EventBus();

    private ConcurrentHashMap<Object, Object> mIPTContext = new ConcurrentHashMap<Object, Object>();
    private boolean DEVELOPER_MODE = true;

    @Override
    public void onCreate() {
        super.onCreate();

        if (DEVELOPER_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().detectAll().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        }
        instance = this;
    }

    public static IPT getInstance() {
        return instance;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public ConcurrentHashMap<Object, Object> getIPTContext() {
        return mIPTContext;
    }

    public static boolean isConnect2Server() {
        Log.d("IPT","is connecte 2 server ?" + connected );
        return connected;
    }

    public static void setConnect2Server(boolean connect2Server) {
        IPT.connected = connect2Server;
        Log.d("IPT","set connecte 2 server " + connect2Server );
    }

    public static boolean isPowerOn() {
        return powerOn;
    }

    public static void setPowerOn(boolean powerOn) {
        IPT.powerOn = powerOn;
    }

    public void setmIPTContext(ConcurrentHashMap<Object, Object> mIPTContext) {
        this.mIPTContext = mIPTContext;
    }
}
