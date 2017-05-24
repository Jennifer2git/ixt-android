package com.imax.ipt.controller.eventbus.handler.settings.maintenance;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class PingHandler extends Handler {
    public static final String TAG = "PingHandler";
    public static final String METHOD_NAME = "ping";

    public PingHandler() {
    }

    @Override
    public List<Object> getParameters() {
        return new ArrayList<Object>();
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);

        eventBus.post(this);
    }

    /**
     * Getters and Setters
     *
     * @return
     */

    @Override
    public Request getRequest() {
        return new Request(PING, METHOD_NAME, getParameters(), this);
    }

}
