package com.imax.ipt.controller.eventbus.handler.rooms;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class AdjustLightLevelHandler extends Handler {
    public static final String TAG = "AdjustLightLevelHandler";
    public static final String METHOD_NAME = "adjustLightLevel";

    private boolean up;

    public AdjustLightLevelHandler(boolean up) {
        super();
        this.up = up;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(up);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
    }

    @Override
    public Request getRequest() {
        return new Request(ADJUST_LIGHT_LEVEL, METHOD_NAME, getParameters(), this);
    }

}
