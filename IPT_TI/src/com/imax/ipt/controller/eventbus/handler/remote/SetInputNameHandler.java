package com.imax.ipt.controller.eventbus.handler.remote;

import android.util.Log;
import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable;
import com.imax.iptevent.EventBus;

import java.util.ArrayList;
import java.util.List;

public class SetInputNameHandler extends Handler {
    public static final String TAG = "SetInputNameHandler";
    public static final String METHOD_NAME = "setInputName";

    private FactoryDeviceTypeDrawable.DeviceKind deviceKind;
    private String displayName;

    public SetInputNameHandler(FactoryDeviceTypeDrawable.DeviceKind deviceKind,String displayName) {
        super();
        this.deviceKind = deviceKind;
        this.displayName = displayName;

    }

    @Override
    public List<Object> getParameters() {
        ArrayList<Object> arrayList = new ArrayList<Object>();
        arrayList.add(deviceKind);
        arrayList.add(displayName);
        Log.d(TAG, "cll set device kind  " + deviceKind.toString() + displayName);
        return arrayList;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
    }

    @Override
    public Request getRequest() {
        return new Request(SET_INPUT_NAME, METHOD_NAME, getParameters(), this);
    }

    /**
     * Getters and setters
     ***/


    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
