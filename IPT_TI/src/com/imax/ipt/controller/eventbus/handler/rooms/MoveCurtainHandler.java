package com.imax.ipt.controller.eventbus.handler.rooms;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class MoveCurtainHandler extends Handler {
    public static final String TAG = "MoveCurtainHandler";
    public static final String METHOD_NAME = "moveCurtain";

    private boolean open;

    public MoveCurtainHandler(boolean open) {
        super();
        this.open = open;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(isOpen());
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
    }

    @Override
    public Request getRequest() {
        return new Request(MOVE_CURTAINS, METHOD_NAME, getParameters(), this);
    }

    /**
     * Getters and Setters
     *
     * @return
     */
    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

}
