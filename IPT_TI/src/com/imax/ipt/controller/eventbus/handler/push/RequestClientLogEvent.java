package com.imax.ipt.controller.eventbus.handler.push;

import android.util.Log;

import com.imax.iptevent.EventBus;

public class RequestClientLogEvent extends PushHandler {

    public static final String TAG = "RequestClientLogEvent";

    @Override
    public void execute(EventBus eventBus, String json) {
        eventBus.post(this);
    }
}
