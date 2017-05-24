package com.imax.ipt.controller.eventbus.handler.rooms;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.ipt.model.LightingPreset;
import com.imax.iptevent.EventBus;

public class GetLightingPresetsHandler extends Handler {
    public static final String TAG = "GetLightingPresetsHandler";
    public static final String METHOD_NAME = "getLightingPresets";

    private List<LightingPreset> lightingPresets;
    private int activePresetId;

    @Override
    public List<Object> getParameters() {
        return new ArrayList<Object>();
    }

    /**
     * {"id":39,"result":{"lightingPresets":[{"id":4,"displayName":"Movie"},{"id"
     * :6,"displayName":"Gaming"},{"id":7,"displayName":"Lounge"},{"id":8,
     * "displayName"
     * :"Party"},{"id":9,"displayName":"Full"},{"id":10,"displayName"
     * :"Low"}],"activePresetId":4,"response":0}}
     */
    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            JSONArray jsonArrayLightingPresets = result.getJSONArray("lightingPresets");
            lightingPresets = new ArrayList<LightingPreset>(jsonArrayLightingPresets.length());
            for (int i = 0; i < jsonArrayLightingPresets.length(); i++) {
                JSONObject jsonLightingPreset = jsonArrayLightingPresets.getJSONObject(i);
                LightingPreset lightingPreset = new LightingPreset(jsonLightingPreset.getInt("id"), jsonLightingPreset.getString("displayName"));
                lightingPresets.add(lightingPreset);
            }
            activePresetId = result.getInt("activePresetId");
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
    public List<LightingPreset> getLightingPresets() {
        return lightingPresets;
    }

    public void setLightingPresets(List<LightingPreset> lightingPresets) {
        this.lightingPresets = lightingPresets;
    }

    public int getActivePresetId() {
        return activePresetId;
    }

    @Override
    public Request getRequest() {
        return new Request(GET_LIGHTING_PRESET, METHOD_NAME, getParameters(), this);
    }

}
