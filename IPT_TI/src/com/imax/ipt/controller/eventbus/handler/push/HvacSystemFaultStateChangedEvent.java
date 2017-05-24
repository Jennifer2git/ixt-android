package com.imax.ipt.controller.eventbus.handler.push;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.iptevent.EventBus;

public class HvacSystemFaultStateChangedEvent extends PushHandler {

    public static final String TAG = "HvacSystemFaultStateChangedEvent";

    private boolean fault;
    private String faultDescription;

    @Override
    public void execute(EventBus eventBus, String json) {
        try {
            JSONObject rootObject = new JSONObject(json);
            JSONArray result = rootObject.getJSONArray("params");

            fault = result.getBoolean(0);
            faultDescription = result.getString(1);

            eventBus.postSticky(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public boolean isFault() {
        return fault;
    }

    public String getFaultDescription() {
        return faultDescription;
    }
}
