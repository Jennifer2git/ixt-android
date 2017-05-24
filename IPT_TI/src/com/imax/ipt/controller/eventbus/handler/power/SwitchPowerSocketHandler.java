package com.imax.ipt.controller.eventbus.handler.power;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class SwitchPowerSocketHandler extends Handler {
    public static final String TAG = "SwitchSystemPowerHandler";
    private static String METHOD_NAME = "switchPowerSocket";

    private boolean mOnState;
    private int mPowerSocket;
    private int mPowerStatus;

    public SwitchPowerSocketHandler(int powerSocket, boolean onState) {
        super();
        this.mOnState = onState;
        this.mPowerSocket = powerSocket;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(mPowerSocket);
        list.add(mOnState);
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
        this.mPowerStatus = 0;
    }

    @Override
    public Request getRequest() {
        return new Request(SWITCH_POWER_SOCKET, METHOD_NAME, getParameters(), this);
    }

}
