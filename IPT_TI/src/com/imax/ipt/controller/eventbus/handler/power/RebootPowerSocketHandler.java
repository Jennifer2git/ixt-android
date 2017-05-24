package com.imax.ipt.controller.eventbus.handler.power;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class RebootPowerSocketHandler extends Handler {
    public static final String TAG = "RebootPowerSocketHandler";
    private static String METHOD_NAME = "rebootPowerSocket";

    private int mPowerSocket;

    public RebootPowerSocketHandler(int powerSocket) {
        super();
        this.mPowerSocket = powerSocket;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(mPowerSocket);
        return list;
    }

    // / return powerState (integer)
    // / 1 = On
    // / 2 = Powering Off
    // / 3 = Off
    // / 4 = Powering On

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        eventBus.post(this);
    }

    @Override
    public Request getRequest() {
        return new Request(REBOOT_POWER_SOCKET, METHOD_NAME, getParameters(), this);
    }
}
