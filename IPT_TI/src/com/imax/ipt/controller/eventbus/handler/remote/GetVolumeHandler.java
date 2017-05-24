package com.imax.ipt.controller.eventbus.handler.remote;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class GetVolumeHandler extends Handler {
    public static String TAG = "GetVolumeHandler";
    public static String METHOD_NAME = "getVolume";

    private double volume;

    @Override
    public List<Object> getParameters() {
        return new ArrayList<Object>();
    }


    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            volume = result.getDouble("intValue");
            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public Request getRequest() {
        return new Request(GET_VOLUME, METHOD_NAME, getParameters(), this);
    }

    /**
     * Getters and setters
     ***/
    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }
}
