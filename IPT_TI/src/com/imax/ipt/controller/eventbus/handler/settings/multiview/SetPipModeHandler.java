package com.imax.ipt.controller.eventbus.handler.settings.multiview;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class SetPipModeHandler extends Handler {
    public static final String TAG = "SetPipModeHandler";
    public static String METHOD_NAME = "setPipMode";

    private byte pipMode;

    public SetPipModeHandler(byte pipMode) {
        super();
        this.pipMode = pipMode;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(pipMode);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        eventBus.post(this);
    }

    @Override
    public Request getRequest() {
        return new Request(SET_PIP_MODE, METHOD_NAME, getParameters(), this);
    }

}
