package com.imax.ipt.controller.eventbus.handler.push;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.iptevent.EventBus;

public class SetpointTemperatureChangedEvent extends PushHandler {

    public static final String TAG = "SetpointTemperatureChangedEvent";

    private int setPoint;
    private int unit;

    @Override
    public void execute(EventBus eventBus, String json) {
        try {
            JSONObject rootObject = new JSONObject(json);
            JSONArray result = rootObject.getJSONArray("params");
            if (result != null && result.length() > 0) {
                setPoint = result.getInt(0);
                unit = result.getInt(1);
            }
            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public int getSetPoint() {
        return setPoint;
    }

    public void setSetPoint(int setPoint) {
        this.setPoint = setPoint;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

}
