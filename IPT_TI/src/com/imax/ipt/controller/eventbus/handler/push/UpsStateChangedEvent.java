package com.imax.ipt.controller.eventbus.handler.push;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.iptevent.EventBus;

public class UpsStateChangedEvent extends PushHandler {

    public static final String TAG = "UpsStateChangedEvent";

    //0,代表电源问题，1代表电源系统问题,-1->normal 2-> fire alarm
    private int mUpsState;

    @Override
    public void execute(EventBus eventBus, String json) {
        try {
            JSONObject rootObject = new JSONObject(json);
            JSONArray result = rootObject.getJSONArray("params");
            mUpsState = result.getInt(0);
            eventBus.postSticky(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }


    public int getPowerState() {
        return mUpsState;
    }
}
