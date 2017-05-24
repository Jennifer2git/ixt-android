package com.imax.ipt.controller.eventbus.handler.input;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.ipt.controller.eventbus.handler.input.SetVideoModeHandler.VideoMode;
import com.imax.ipt.controller.eventbus.handler.input.SetZoomModeHandler.ZoomMode;
import com.imax.ipt.model.DeviceType;
import com.imax.iptevent.EventBus;

public class GetZoomModeHandler extends Handler {
    public static final String TAG = "GetZoomModeHandler";
    public static String METHOD_NAME = "getZoomMode";

    private ZoomMode zoomMode;

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
            this.zoomMode = ZoomMode.getZoomMode(result.getInt("intValue"));

            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public Request getRequest() {
        return new Request(GET_ZOOM_MODE, METHOD_NAME, getParameters(), this);
    }

    public ZoomMode getZoomMode() {
        return zoomMode;
    }
}
