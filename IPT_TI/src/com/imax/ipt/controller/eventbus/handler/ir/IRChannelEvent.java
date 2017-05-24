package com.imax.ipt.controller.eventbus.handler.ir;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class IRChannelEvent extends Handler {
    public static String TAG = "IRChannelEvent";
    private static final String METHOD_NAME = "irChannel";

    private int mInputId;
    private int mChannel;

    public IRChannelEvent(int mInputId, int mChannel) {
        super();
        this.mInputId = mInputId;
        this.mChannel = mChannel;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(mInputId);
        list.add(mChannel);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
    }

    @Override
    public Request getRequest() {
        return new Request(IR_CHANNEL, METHOD_NAME, getParameters(), this);
    }

}
