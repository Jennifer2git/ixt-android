package com.imax.ipt.controller.eventbus.handler.media;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class SetLanguageHandler extends Handler {
    public static final String TAG = "SetLanguageHandler";
    public static String METHOD_NAME = "setLanguage";

    /**
     * 0,中文，1是英文。
     */
    private int type = 0;

    public SetLanguageHandler(int type) {
        this.type = type;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(type);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
    }


    @Override
    public Request getRequest() {
        return new Request(SET_LANGUAGE, METHOD_NAME, getParameters(), this);
    }

}
