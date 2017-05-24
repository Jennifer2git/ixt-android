package com.imax.ipt.controller.eventbus.handler.settings.preferences;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.ipt.model.LightingEvent;
import com.imax.ipt.model.LightingPreset;
import com.imax.iptevent.EventBus;

public class GetLightingEventsHandler extends Handler {
    public static final String TAG = "GetLightingEventsHandler";
    public static final String METHOD_NAME = "getLightingEvents";

    private List<LightingEvent> lightingEvents;
    private List<LightingPreset> lightingPresets;
//   private int activePresetId;

    @Override
    public List<Object> getParameters() {
        return new ArrayList<Object>();
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            JSONArray jsonArrayLightingEvents = result.getJSONArray("lightingEvents");
            lightingEvents = new ArrayList<LightingEvent>(jsonArrayLightingEvents.length());
            for (int i = 0; i < jsonArrayLightingEvents.length(); i++) {
                JSONObject jsonLightingEvent = jsonArrayLightingEvents.getJSONObject(i);
                LightingEvent lightingEvent = new LightingEvent();
                lightingEvent.setId(jsonLightingEvent.getInt("id"));
                lightingEvent.setDisplayName(jsonLightingEvent.getString("displayName"));
                lightingEvents.add(lightingEvent);
            }
            JSONArray jsonArrayLightingPresets = result.getJSONArray("lightingPresets");
            lightingPresets = new ArrayList<LightingPreset>(jsonArrayLightingPresets.length());
            for (int i = 0; i < jsonArrayLightingPresets.length(); i++) {
                JSONObject jsonLightingPreset = jsonArrayLightingPresets.getJSONObject(i);
                LightingPreset lightingPreset = new LightingPreset(jsonLightingPreset.getInt("id"), jsonLightingPreset.getString("displayName"));
                lightingPresets.add(lightingPreset);
            }
//         activePresetId = result.getInt("activePresetId");
            eventBus.post(this);
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
    public LightingPreset[] getLightingPresets() {
        LightingPreset[] presets = new LightingPreset[lightingPresets.size()];
        lightingPresets.toArray(presets);
        return presets;
    }

    public LightingEvent[] getLightingEvents() {
        LightingEvent[] events = new LightingEvent[lightingPresets.size()];
        lightingEvents.toArray(events);
        return events;
    }
//
//   public void setLightingPresets(List<LightingPreset> lightingPresets)
//   {
//      this.lightingPresets = lightingPresets;
//   }
//   
//   public int getActivePresetId()
//   {
//      return activePresetId;
//   }

    @Override
    public Request getRequest() {
        return new Request(GET_LIGHTING_EVENTS, METHOD_NAME, getParameters(), this);
    }

}
