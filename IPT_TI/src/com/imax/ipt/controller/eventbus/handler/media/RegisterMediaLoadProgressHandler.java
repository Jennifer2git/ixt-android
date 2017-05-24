package com.imax.ipt.controller.eventbus.handler.media;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class RegisterMediaLoadProgressHandler extends Handler {
    public static final String TAG = "RegisterMediaLoadProgressHandler";
    public static final String METHOD_NAME = "registerMediaLoadProgress";

    public boolean register = false;

    /**
     * @param register
     */
    public RegisterMediaLoadProgressHandler(boolean register) {
        super();
        this.register = register;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(register);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
    }

    @Override
    public Request getRequest() {
        return new Request(REGISTER_MEDIA_PROGRESS, METHOD_NAME, getParameters(), this);
    }

}
