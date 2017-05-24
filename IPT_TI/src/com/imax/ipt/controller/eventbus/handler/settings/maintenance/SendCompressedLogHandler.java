package com.imax.ipt.controller.eventbus.handler.settings.maintenance;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class SendCompressedLogHandler extends Handler {
    public static final String TAG = "SendCompressedLogHandler";
    public static String METHOD_NAME = "sendCompressedLog";

    private String fileName;
    private String encoded64String;

    public SendCompressedLogHandler(String fileName, String encoded64String) {
        super();
        this.fileName = fileName;
        this.encoded64String = encoded64String;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(fileName);
        list.add(encoded64String);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        eventBus.post(this);
    }

    @Override
    public Request getRequest() {
        return new Request(SEND_COMPRESSED_LOG, METHOD_NAME, getParameters(), this);
    }

}
