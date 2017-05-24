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
import com.imax.ipt.ui.activity.input.tv.ChannelPresetFragment;
import com.imax.iptevent.EventBus;

public class AddInputChannelPresetHandler extends Handler {
    public static final String TAG = "AddInputChannelPresetHandler";
    public static final String METHOD_NAME = "addInputChannelPreset";

    private int inputId;
    private int channel;
    private String displayName;

    private int presetId;

    public AddInputChannelPresetHandler(int inputId, int channel, String displayName) {
        this.inputId = inputId;
        this.channel = channel;
        this.displayName = displayName;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(inputId);
        list.add(channel);
        list.add(displayName);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            presetId = result.getInt("intValue");

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
    public int getPresetId() {
        return presetId;
    }

    @Override
    public Request getRequest() {
        return new Request(ADD_INPUT_CHANNEL_PRESET, METHOD_NAME, getParameters(), this);
    }

}
