package com.imax.ipt.controller.eventbus.handler.settings.maintenance;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class GetProjectorLampStatusHandler extends Handler {
    public static final String TAG = "GetProjectorLampStatus";
    private static String METHOD_NAME = "getProjectorLampStatus";

    private int mLeftLampHourLife;
    private int mLeftLampHourUsed;
    private boolean mLeftLampOn;
    private int mRightLampHourLife;
    private int mRightLampHourUsed;
    private boolean mRightLampOn;
    private int mResponse;

    public GetProjectorLampStatusHandler() {

    }

    @Override
    public List<Object> getParameters() {
        return null;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            mResponse = result.getInt("response");
//			mLeftLampHourLife = result.getInt("leftLampHourLife");
            mLeftLampHourUsed = result.getInt("leftLampHourUsed");
//			mLeftLampOn = result.getBoolean("leftLampOn");
//			mRightLampHourLife = result.getInt("rightLampHourLife");
            mRightLampHourUsed = result.getInt("rightLampHourUsed");
//			mRightLampOn = result.getBoolean("rightLampOn");

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        eventBus.post(this);
    }

    @Override
    public Request getRequest() {
        return new Request(GET_PROJECTOR_LAMP_STATUS, METHOD_NAME,
                getParameters(), this);
    }

    public int getLeftLampHourUsed() {
        return mLeftLampHourUsed;
    }

    public int getLeftLampHourLife() {
        return mLeftLampHourLife;
    }

    public boolean isLeftLampOn() {
        return mLeftLampOn;
    }

    public int getRightLampHourLife() {
        return mRightLampHourLife;
    }

    public int getRightLampHourUsed() {
        return mRightLampHourUsed;
    }

    public boolean isRightLampOn() {
        return mRightLampOn;
    }

    public int getResponse() {
        return mResponse;
    }
}
