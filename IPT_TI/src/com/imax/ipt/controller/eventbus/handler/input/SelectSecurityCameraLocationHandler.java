package com.imax.ipt.controller.eventbus.handler.input;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class SelectSecurityCameraLocationHandler extends Handler {
    public static final String TAG = "SelectSecurityCameraLocationHandler";
    public static String METHOD_NAME = "selectSecurityCameraLocation";

    private int locationId;

    public SelectSecurityCameraLocationHandler(int locationId) {
        super();
        this.locationId = locationId;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(locationId);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        eventBus.post(this);
    }

    @Override
    public Request getRequest() {
        return new Request(SELECT_SECURITY_CAMERA_LOCATION, METHOD_NAME, getParameters(), this);
    }
}
