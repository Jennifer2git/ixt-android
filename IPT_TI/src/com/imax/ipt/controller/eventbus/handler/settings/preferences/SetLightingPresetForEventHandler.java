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

public class SetLightingPresetForEventHandler extends Handler {
    public static final String TAG = "SetLightingPresetForEventEventsHandler";
    public static final String METHOD_NAME = "setLightingPresetForEvent";

    private int eventId;
    private int presetId;

    public SetLightingPresetForEventHandler(int eventId, int presetId) {
        this.eventId = eventId;
        this.presetId = presetId;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(eventId);
        list.add(presetId);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
//      try
//      {
//         JSONObject rootObject = new JSONObject(sbResult);
//         JSONObject result = rootObject.getJSONObject("result");
//         JSONArray jsonArrayLightingEvents = result.getJSONArray("lightingEvents");
//         lightingEvents = new ArrayList<LightingEvent>(jsonArrayLightingEvents.length());
//         for (int i = 0; i < jsonArrayLightingEvents.length(); i++)
//         {
//            JSONObject jsonLightingEvent = jsonArrayLightingEvents.getJSONObject(i);
//            LightingEvent lightingEvent = new LightingEvent();
//            lightingEvent.setId(jsonLightingEvent.getInt("id"));
//            lightingEvent.setDisplayName(jsonLightingEvent.getString("displayName"));
//            lightingEvents.add(lightingEvent);
//         }
//         JSONArray jsonArrayLightingPresets = result.getJSONArray("lightingPresets");
//         lightingPresets = new ArrayList<LightingPreset>(jsonArrayLightingPresets.length());
//         for (int i = 0; i < jsonArrayLightingPresets.length(); i++)
//         {
//            JSONObject jsonLightingPreset = jsonArrayLightingPresets.getJSONObject(i);
//            LightingPreset lightingPreset = new LightingPreset();
//            lightingPreset.setId(jsonLightingPreset.getInt("id"));
//            lightingPreset.setDisplayName(jsonLightingPreset.getString("displayName"));
//            lightingPresets.add(lightingPreset);
//         }
////         activePresetId = result.getInt("activePresetId");
//         eventBus.post(this);
//      } catch (JSONException e)
//      {
//         Log.e(TAG, e.getMessage());
//      }
        eventBus.post(this);
    }

    /**
     * Getters and Setters
     *
     * @return
     */

    @Override
    public Request getRequest() {
        return new Request(GET_LIGHTING_EVENTS, METHOD_NAME, getParameters(), this);
    }

}
