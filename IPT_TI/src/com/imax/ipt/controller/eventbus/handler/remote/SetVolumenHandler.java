package com.imax.ipt.controller.eventbus.handler.remote;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class SetVolumenHandler extends Handler {
    public static final String TAG = "SetVolumenHandler";
    public static String METHOD_NAME = "setVolume";
    private double volume;

    public SetVolumenHandler(double volume) {
        this.volume = volume;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(volume);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
    }

    @Override
    public Request getRequest() {
        return new Request(SET_VOLUME, METHOD_NAME, getParameters(), this);
    }
    /** Getters and setters ***/
}
