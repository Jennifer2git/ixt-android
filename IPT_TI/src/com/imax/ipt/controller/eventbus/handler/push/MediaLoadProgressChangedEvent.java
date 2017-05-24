package com.imax.ipt.controller.eventbus.handler.push;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.iptevent.EventBus;

public class MediaLoadProgressChangedEvent extends PushHandler {
    public static final String TAG = "MediaLoadProgressChangedEvent";
    private int mPorcentage;
    private int estimatedSecondsRemaining;

    @Override
    public void execute(EventBus eventBus, String json) {
        Log.d(TAG, json);
        try {
            JSONObject rootObject = new JSONObject(json);
            JSONArray result = rootObject.getJSONArray("params");
            this.mPorcentage = result.getInt(0);
            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Getters and Setters
     *
     * @return
     */
    public int getmPorcentage() {
        return mPorcentage;
    }

    public void setmPorcentage(int mPorcentage) {
        this.mPorcentage = mPorcentage;
    }

}
