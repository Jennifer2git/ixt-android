package com.imax.ipt.controller.eventbus.handler.remote;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.ipt.model.InputChannelPreset;
import com.imax.ipt.model.LightingEvent;
import com.imax.ipt.model.LightingPreset;
import com.imax.iptevent.EventBus;

public class GetInputChannelPresetsForInputHandler extends Handler {
    public static final String TAG = "GetInputChannelPresetsForInputHandler";
    public static final String METHOD_NAME = "getInputChannelPresetsForInput";

    private int inputId;
    private InputChannelPreset[] inputChannelPresets;

    public GetInputChannelPresetsForInputHandler(int inputId) {
        this.inputId = inputId;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(inputId);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");

            JSONArray jsonArrayPresets = result.getJSONArray("presets");
            inputChannelPresets = new InputChannelPreset[jsonArrayPresets.length()];
            Gson gson = new Gson();
            for (int i = 0; i < inputChannelPresets.length; i++) {
                JSONObject jsonPreset = jsonArrayPresets.getJSONObject(i);
                inputChannelPresets[i] = gson.fromJson(jsonPreset.toString(), InputChannelPreset.class);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        eventBus.post(this);
    }

    /**
     * Getters and Setters
     *
     * @return
     */
    public InputChannelPreset[] getInputChannelPresets() {
        return inputChannelPresets;
    }

    @Override
    public Request getRequest() {
        return new Request(GET_INPUT_CHANNEL_PRESETS_FOR_INPUT, METHOD_NAME, getParameters(), this);
    }

}
