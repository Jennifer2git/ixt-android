package com.imax.ipt.controller.eventbus.handler.rooms;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class GetLightLevelHandler extends Handler {
    public static final String TAG = "GetLightLevelHandler";
    public static String METHOD_NAME = "getLightLevel";

    private int lightLevel;

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        try {
            Log.d(TAG, sbResult);
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            this.lightLevel = result.getInt("intValue");

            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public Request getRequest() {
        return new Request(GET_LIGHT_LEVEL, METHOD_NAME, getParameters(), this);
    }

    public int getLightLevel() {
        return lightLevel;
    }
}
