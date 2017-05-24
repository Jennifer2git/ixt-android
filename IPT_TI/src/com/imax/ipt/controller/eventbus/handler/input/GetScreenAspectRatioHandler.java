package com.imax.ipt.controller.eventbus.handler.input;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.ipt.controller.eventbus.handler.input.SetScreenAspectRatioHandler.ScreenAspectRatio;
import com.imax.iptevent.EventBus;

public class GetScreenAspectRatioHandler extends Handler {
    public static final String TAG = "GetScreenAspectRatioHandler";
    public static String METHOD_NAME = "getScreenAspectRatio";

    private ScreenAspectRatio aspectRatio;

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        try {
            Log.d(TAG, sbResult);
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            aspectRatio = ScreenAspectRatio.getScreenAspectRatio(result.getInt("intValue"));

            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public Request getRequest() {
        return new Request(GET_SCREEN_ASPECT_RATIO, METHOD_NAME, getParameters(), this);
    }

    public ScreenAspectRatio getAspectRatio() {
        return aspectRatio;
    }
}
