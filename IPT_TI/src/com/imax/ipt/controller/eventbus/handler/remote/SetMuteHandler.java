package com.imax.ipt.controller.eventbus.handler.remote;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class SetMuteHandler extends Handler {
    public static final String TAG = "SetMuteHandler";
    public static final String METHOD_NAME = "setMute";

    private boolean mute;

    public SetMuteHandler(boolean mute) {
        super();
        this.mute = mute;
    }

    @Override
    public List<Object> getParameters() {
        ArrayList<Object> arrayList = new ArrayList<Object>();
        arrayList.add(mute);
        return arrayList;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
    }

    @Override
    public Request getRequest() {
        return new Request(SET_MUTE, METHOD_NAME, getParameters(), this);
    }

    /**
     * Getters and setters
     ***/

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }

}
