package com.imax.ipt.controller.eventbus.handler.push;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.iptevent.EventBus;

public class ScreenShareButtonVisibilityChangedEvent extends PushHandler {

    public static final String TAG = "ScreenShareButtonVisibilityChangedEvent";
    private boolean visible;

    @Override
    public void execute(EventBus eventBus, String json) {
        try {
            JSONObject rootObject = new JSONObject(json);
            JSONArray result = rootObject.getJSONArray("params");

            visible = result.getBoolean(0);

            eventBus.postSticky(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public boolean isVisible() {
        return visible;
    }

}
