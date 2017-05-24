package com.imax.ipt.controller.eventbus.handler.push;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.ipt.controller.eventbus.handler.input.SetScreenAspectRatioHandler.ScreenAspectRatio;
import com.imax.iptevent.EventBus;

public class ScreenAspectRatioChangedEvent extends PushHandler {

    public static final String TAG = "ScreenAspectRatioChangedEvent";
    private ScreenAspectRatio screnAspectRatio;

    @Override
    public void execute(EventBus eventBus, String json) {
        try {
            JSONObject rootObject = new JSONObject(json);
            JSONArray result = rootObject.getJSONArray("params");
            screnAspectRatio = ScreenAspectRatio.getScreenAspectRatio(result.getInt(0));
            eventBus.postSticky(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

    }

    public ScreenAspectRatio getScrenAspectRatio() {
        return screnAspectRatio;
    }
}
