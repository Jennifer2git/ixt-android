package com.imax.ipt.controller.eventbus.handler.power;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class GetPowerSocketStateHandler extends Handler {
    public static final String TAG = "GetPowerSocketStateHandler";
    private static String METHOD_NAME = "getPowerSocketState";
    private Integer[] mPowerSockets;
    private Integer[] mPowerStates;

    public GetPowerSocketStateHandler() {
        super();
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        return list;
    }

    public Integer[] getPowerSockets() {
        return mPowerSockets;
    }

    public Integer[] getPowerStates() {
        return mPowerStates;
    }

    // / return powerState (integer)
    // / 1 = On
    // / 2 = Powering Off
    // / 3 = Off
    // / 4 = Powering On

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);

        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            JSONArray powerSockets = result.getJSONArray("powerSockets");
            mPowerSockets = new Integer[powerSockets.length()];
            for (int i = 0; i < powerSockets.length(); i++) {
                mPowerSockets[i] = powerSockets.getInt(i);
            }
            JSONArray powerStates = result.getJSONArray("powerStates");
            mPowerStates = new Integer[powerStates.length()];
            for (int i = 0; i < powerStates.length(); i++) {
                mPowerStates[i] = powerStates.getInt(i);
            }
            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public Request getRequest() {
        return new Request(GET_POWER_SOCKET_STATE, METHOD_NAME,
                getParameters(), this);
    }

}
