package com.imax.ipt.controller.eventbus.handler.settings.maintenance;

import android.util.Log;
import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

import java.util.ArrayList;
import java.util.List;

public class UpdateCheckHandler extends Handler {
    public static final String TAG = UpdateCheckHandler.class.getSimpleName();
    public static final String METHOD_NAME = "update";

    public UpdateCheckHandler() {
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
        return new Request(UPDATE, METHOD_NAME, getParameters(), this);
    }

}
