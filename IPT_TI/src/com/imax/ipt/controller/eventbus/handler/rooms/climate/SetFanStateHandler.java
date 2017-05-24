package com.imax.ipt.controller.eventbus.handler.rooms.climate;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class SetFanStateHandler extends Handler {
    public static final String TAG = "SetFanStateHandler";
    public static String METHOD_NAME = "setFanState";
    private int mFanState;

    public static int FAN_STATE_ON = 0;
    public static int FAN_STATE_AUTO = 1;

    public SetFanStateHandler(int mFanState) {
        super();
        this.mFanState = mFanState;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(mFanState);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        eventBus.post(this);
    }

    @Override
    public Request getRequest() {
        return new Request(SET_FAN_STATE, METHOD_NAME, getParameters(), this);
    }

    /**
     * Getters and Setters
     *
     * @return
     */
    public int getmFanState() {
        return mFanState;
    }

    public void setmFanState(int mFanState) {
        this.mFanState = mFanState;
    }

}
