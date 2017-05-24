package com.imax.ipt.controller.eventbus.handler.rooms.climate;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class AdjustCurrentSetPointHandler extends Handler {
    private static final String TAG = "AdjustCurrentSetPointHandler";
    private static final String METHOD_NAME = "adjustCurrentSetpoint";

    private boolean up;


    public AdjustCurrentSetPointHandler(boolean up) {
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
        return new Request(ADJUST_CURRENT_SET_POINT, METHOD_NAME, getParameters(), this);
    }

}
