package com.imax.ipt.controller.eventbus.handler.push;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.ipt.controller.eventbus.handler.input.SetVideoModeHandler.VideoMode;
import com.imax.ipt.model.SystemStatus;
import com.imax.iptevent.EventBus;

public class SystemStatusNotificationEvent extends PushHandler {

    public static final String TAG = "SystemStatusNotificationEvent";

    private SystemStatus status;

    @Override
    public void execute(EventBus eventBus, String json) {
        try {
            JSONObject rootObject = new JSONObject(json);
            JSONArray result = rootObject.getJSONArray("params");
            int statusValue = result.getInt(0);
            status = new SystemStatus(statusValue);
            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public SystemStatus getStatus() {
        return status;
    }
}
