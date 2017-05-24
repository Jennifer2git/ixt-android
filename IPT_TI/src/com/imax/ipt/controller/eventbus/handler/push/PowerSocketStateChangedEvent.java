package com.imax.ipt.controller.eventbus.handler.push;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.iptevent.EventBus;

public class PowerSocketStateChangedEvent extends PushHandler {

    public static final String TAG = "PowerSocketStateChangedEvent";
    private int mPowerSocket;
    private int mPowerState;

    @Override
    public void execute(EventBus eventBus, String json) {
        try {
            JSONObject rootObject = new JSONObject(json);
            JSONArray result = rootObject.getJSONArray("params");
            mPowerSocket = result.getInt(0);
            mPowerState = result.getInt(1);
            eventBus.postSticky(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public int getPowerSocket() {
        return mPowerSocket;
    }

    public int getPowerState() {
        return mPowerState;
    }
}
