package com.imax.ipt.controller.eventbus.handler.power;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class GetSystemPowerProgressHandler extends Handler {
    public static final String TAG = "GetSystemPowerProgressHandler";
    private static String METHOD_NAME = "getSystemPowerProgress";

    private int mPercentCompleted = 0;
    private int mEstimatedSecondsRemaining = 0;
    private int mError = 0;

    public GetSystemPowerProgressHandler() {
        super();
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);

        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            mPercentCompleted = result.getInt("percentCompleted");
            mEstimatedSecondsRemaining = result.getInt("estimatedSecondsRemaining");
            mError = result.getInt("errorCode");
            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public Request getRequest() {
        return new Request(GET_SYSTEM_POWER_PROGRESS, METHOD_NAME, getParameters(), this);
    }

    /**
     * Getters and Setters
     */
    public int getmPercentCompleted() {
        return mPercentCompleted;
    }

    public int getmEstimatedSecondsRemaining() {
        return mEstimatedSecondsRemaining;
    }

    public int getmError() {
        return mError;
    }
}
