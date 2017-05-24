package com.imax.ipt.controller.eventbus.handler.power;

import android.util.Log;
import com.imax.ipt.common.PowerState;
import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetSystemPowerStateHandler extends Handler {


    public static final String TAG = "GetSystemPowerState";
    private static String METHOD_NAME = "getSystemPowerState";

    private PowerState powerState;

    public GetSystemPowerStateHandler() {
        super();
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        return list;
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
            this.powerState = PowerState.getPowerState(result.getInt("intValue"));
            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public Request getRequest() {
        return new Request(GET_SYSTEM_POWER_STATE, METHOD_NAME, getParameters(), this);
    }

    /**
     * Getters and Setters
     */

    public PowerState getPowerState() {
        return powerState;
    }
}
