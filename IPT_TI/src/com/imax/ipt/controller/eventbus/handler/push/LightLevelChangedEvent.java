package com.imax.ipt.controller.eventbus.handler.push;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.iptevent.EventBus;

public class LightLevelChangedEvent extends PushHandler {
    public static final String TAG = "AdjustLightLevelEvent";
    private int mLightLevel;

    @Override
    public void execute(EventBus eventBus, String json) {
        try {
            JSONObject rootObject = new JSONObject(json);
            JSONArray result = rootObject.getJSONArray("params");
            if (result != null && result.length() > 0) {
                mLightLevel = result.getInt(0);
            }
            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Getters and Setters
     */
    public int getmLightLevel() {
        return mLightLevel;
    }

    public void setmLightLevel(int mLightLevel) {
        this.mLightLevel = mLightLevel;
    }

}
