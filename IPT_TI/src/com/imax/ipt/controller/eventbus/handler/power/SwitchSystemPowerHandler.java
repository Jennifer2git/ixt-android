package com.imax.ipt.controller.eventbus.handler.power;

import android.util.Log;
import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SwitchSystemPowerHandler extends Handler {
    public static final String TAG = SwitchSystemPowerHandler.class.getSimpleName();
    private static String METHOD_NAME = "switchSystemPower";

    private boolean mOnState;
    private int state;


    public SwitchSystemPowerHandler(boolean onState) {
        super();
        this.mOnState = onState;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(mOnState);
        return list;
    }

    // / return powerState (integer)
    // / 1 = On
    // / 2 = Powering Off
    // / 3 = Off
    // / 4 = Powering On

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            state = result.getInt("intValue");
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        eventBus.post(this);
    }

    @Override
    public Request getRequest() {
        return new Request(SWITCH_SYSTEM_POWER, METHOD_NAME, getParameters(), this);
    }

    /**
     * Getters and Setters
     *
     * @return
     */
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
