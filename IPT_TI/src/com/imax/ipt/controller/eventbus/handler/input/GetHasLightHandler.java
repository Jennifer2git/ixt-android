package com.imax.ipt.controller.eventbus.handler.input;

import android.util.Log;
import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetHasLightHandler extends Handler {
    public static final String TAG = GetHasLightHandler.class.getSimpleName();
    public static final String METHOD_NAME = "hasLight";

    private boolean hasLighting = false;

    @Override
    public List<Object> getParameters() {
        return new ArrayList<Object>();
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            hasLighting = result.getBoolean("hasLight");
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
    public boolean getHasLighting() {
        return hasLighting;
    }

    @Override
    public Request getRequest() {
        return new Request(GET_HAS_LIGHT, METHOD_NAME, getParameters(), this);
    }

}
