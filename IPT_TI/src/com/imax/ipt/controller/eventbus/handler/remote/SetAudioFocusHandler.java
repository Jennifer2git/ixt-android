package com.imax.ipt.controller.eventbus.handler.remote;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class SetAudioFocusHandler extends Handler {
    public static final String TAG = "SetAudioFocusHandler";

    public static String METHOD_NAME = "setAudioFocus";

    private Integer inputId;

    public SetAudioFocusHandler(Integer inputId) {
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
        eventBus.post(this);
    }

    @Override
    public Request getRequest() {
        return new Request(SET_AUDIO_FOCUS, METHOD_NAME, getParameters(), this);
    }

}
