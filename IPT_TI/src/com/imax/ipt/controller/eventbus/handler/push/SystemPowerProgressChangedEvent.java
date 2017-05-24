package com.imax.ipt.controller.eventbus.handler.push;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.iptevent.EventBus;

public class SystemPowerProgressChangedEvent extends PushHandler {
    public static final String TAG = "SystemPowerProgressChangedEvent";

    private int mPercentCompleted = 0;
    private int mEstimatedSecondsRemaining = 0;
    private int mError = 0;

    /**
     * int percentCompleted, int estimatedSecondsRemaining, int errorCode
     */
    @Override
    public void execute(EventBus eventBus, String json) {
        try {
            JSONObject rootObject = new JSONObject(json);
            JSONArray result = rootObject.getJSONArray("params");
            mPercentCompleted = result.getInt(0);
            mEstimatedSecondsRemaining = result.getInt(1);
            mError = result.getInt(2);
            eventBus.postSticky(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Getters and Setters
     */

    public int getmPercentCompleted() {
        return mPercentCompleted;
    }

    public void setmPercentCompleted(int mPercentCompleted) {
        this.mPercentCompleted = mPercentCompleted;
    }

    public int getmEstimatedSecondsRemaining() {
        return mEstimatedSecondsRemaining;
    }

    public void setmEstimatedSecondsRemaining(int mEstimatedSecondsRemaining) {
        this.mEstimatedSecondsRemaining = mEstimatedSecondsRemaining;
    }

    public int getmError() {
        return mError;
    }

    public void setmError(int mError) {
        this.mError = mError;
    }
}
