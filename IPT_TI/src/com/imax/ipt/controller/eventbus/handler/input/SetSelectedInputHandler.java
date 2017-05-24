package com.imax.ipt.controller.eventbus.handler.input;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class SetSelectedInputHandler extends Handler {
    public static final String TAG = "SetSelectedInputHandler";
    public static String METHOD_NAME = "setSelectedInput";

    private int outputIndex;
    private int inputId;

    public SetSelectedInputHandler(int outputIndex, int inputId) {
        super();
        this.outputIndex = outputIndex;
        this.inputId = inputId;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(outputIndex);
        list.add(inputId);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        eventBus.post(this);
    }

    @Override
    public Request getRequest() {
        return new Request(SET_SELECTED_INPUT, METHOD_NAME, getParameters(), this);
    }

}
