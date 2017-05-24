package com.imax.ipt.controller.eventbus.handler.remote;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class GetAudioFocusHandler extends Handler {
    public static final String TAG = "GetAudioFocusHandler";
    public static final String METHOD_NAME = "getAudioFocus";

    @Override
    public List<Object> getParameters() {
        return null;
    }

    private int inputId;

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            inputId = result.getInt("pipMode");
            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public Request getRequest() {
        Request request = new Request();
        request.setMethod(METHOD_NAME);
        request.setId(GET_AUDIO_FOCUS);
        request.setParams(getParameters());
        return request;
    }

    /***
     *
     */
    public int getInputId() {
        return inputId;
    }

    public void setInputId(int inputId) {
        this.inputId = inputId;
    }
}
