package com.imax.ipt.controller.eventbus.handler.settings.maintenance;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class ResetProjectorLampTimeHandler extends Handler {
    public static final String TAG = "ResetProjectorLampTimeHandler";
    private static String METHOD_NAME = "resetProjectorLampTime";
    private int mPosition;
    private int mResponse;

    public ResetProjectorLampTimeHandler(int position) {
        mPosition = position;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(mPosition);
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
        return new Request(RESET_PROJECTOR_LAMP_TIME, METHOD_NAME, getParameters(),
                this);
    }

    public int getResponse() {
        return mResponse;
    }
}
