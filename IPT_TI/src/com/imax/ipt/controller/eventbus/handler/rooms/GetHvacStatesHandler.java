package com.imax.ipt.controller.eventbus.handler.rooms;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.ipt.controller.eventbus.handler.push.FanStateChangedEvent.FanState;
import com.imax.iptevent.EventBus;

public class GetHvacStatesHandler extends Handler {

    public static final String TAG = "GetHvacStatesHandler";
    public static final String METHOD_NAME = "getHvacStates";

    private int mUnit;
    private int mValueSetPoint;
    private int mCurrentTemperature;
    private FanState mFanState;
    private boolean mIsFault;
    private String mDescription;

    @Override
    public List<Object> getParameters() {
        return new ArrayList<Object>();
    }

    /**
     * 07-07 03:40:59.169: D/GetHvacStatesHandler(16395):
     * {"id":40,"result":{"setpoint"
     * :{"value":65,"unit":0},"actualTemperature":{"value"
     * :77,"unit":0},"fanState"
     * :1,"isFault":true,"description":"gyhgyh","response":1}}
     */
    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            JSONObject setPoint = result.getJSONObject("setpoint");
            JSONObject actualTemperature = result.getJSONObject("actualTemperature");
            mCurrentTemperature = actualTemperature.getInt("value");
            mValueSetPoint = setPoint.getInt("value");
            mUnit = setPoint.getInt("unit");
            mFanState = FanState.getFanState(result.getInt("fanState"));
            mIsFault = result.getBoolean("isFault");
            mDescription = result.getString("description");
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        eventBus.post(this);
    }


    @Override
    public Request getRequest() {
        return new Request(GET_HVAC_STATES, METHOD_NAME, getParameters(), this);
    }

    /**
     * Getters and Setters
     */

    public int getmUnit() {
        return mUnit;
    }

    public void setmUnit(int mUnit) {
        this.mUnit = mUnit;
    }

    public int getmValueSetPoint() {
        return mValueSetPoint;
    }

    public void setmValueSetPoint(int mValueSetPoint) {
        this.mValueSetPoint = mValueSetPoint;
    }

    public int getmCurrentTemperature() {
        return mCurrentTemperature;
    }

    public void setmCurrentTemperature(int mCurrentTemperature) {
        this.mCurrentTemperature = mCurrentTemperature;
    }

    public FanState getmFanState() {
        return mFanState;
    }

    public void setmFanState(FanState mFanState) {
        this.mFanState = mFanState;
    }

    public boolean ismIsFault() {
        return mIsFault;
    }

    public void setmIsFault(boolean mIsFault) {
        this.mIsFault = mIsFault;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

}
