package com.imax.ipt.controller.eventbus.handler.remote;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class ExecuteRemoteControlHandler extends Handler {
    public static final String TAG = "ExecuteRemoteControlHandler";
    public static final String METHOD_NAME = "executeRemoteControl";

    private String command;

    public ExecuteRemoteControlHandler(String command) {
        super();
        this.command = command;
    }

    @Override
    public List<Object> getParameters() {
        ArrayList<Object> list = new ArrayList<Object>();
        list.add(command);
        return list;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
    }

    @Override
    public Request getRequest() {
        return new Request(EXECUTE_REMOTE, METHOD_NAME, getParameters(), this);
    }

}
