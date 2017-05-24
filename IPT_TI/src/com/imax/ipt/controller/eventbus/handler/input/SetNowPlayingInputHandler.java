package com.imax.ipt.controller.eventbus.handler.input;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class SetNowPlayingInputHandler extends Handler {
    public static final String TAG = "SetNowPlayingInputHandler";
    public static final String METHOD_NAME = "setNowPlayingInput";
    private int inputId;

    public SetNowPlayingInputHandler(int inputId) {
        super();
        this.inputId = inputId;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(inputId);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);

    }

    @Override
    public Request getRequest() {
        return new Request(SET_NOW_PLAYING, METHOD_NAME, getParameters(), this);
    }

}
