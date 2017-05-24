package com.imax.ipt.controller.eventbus.handler.settings.maintenance;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class SwitchProjectorLampHandler extends Handler {
    public static final String TAG = "SwitchProjectorLampHandler";
    private static String METHOD_NAME = "switchProjectorLamp";
    private int mPosition;
    private boolean mOn;
    private int mResponse;

    public SwitchProjectorLampHandler(int position, boolean on) {
        mPosition = position;
        mOn = on;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(mPosition);
        list.add(mOn);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            mResponse = result.getInt("response");

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        eventBus.post(this);
    }

    @Override
    public Request getRequest() {
        return new Request(SWITCH_PROJECTOR_LAMP, METHOD_NAME, getParameters(),
                this);
    }

    public int getResponse() {
        return mResponse;
    }
}
