package com.imax.ipt.controller.eventbus.handler.ir;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class IRPulseEvent extends Handler {
    public IRPulseEvent(int mInputId, int mCode) {
        super();
        this.mInputId = mInputId;
        this.mCode = mCode;
    }

    public static String TAG = "IRPulseEvent";
    private static final String METHOD_NAME = "irPulse";

    private int mInputId;
    private int mCode;

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(mInputId);
        list.add(mCode);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
    }

    @Override
    public Request getRequest() {
        return new Request(IR_PULSE, METHOD_NAME, getParameters(), this);
    }

}
