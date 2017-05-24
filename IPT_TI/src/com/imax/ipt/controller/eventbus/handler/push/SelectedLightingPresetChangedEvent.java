package com.imax.ipt.controller.eventbus.handler.push;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.ipt.controller.eventbus.handler.rooms.GetCurtainStateHandler.CurtainState;
import com.imax.iptevent.EventBus;

public class SelectedLightingPresetChangedEvent extends PushHandler {

    public static final String TAG = "SelectedLightingPresetChangedEvent";
    private int lightingPresetId;

    @Override
    public void execute(EventBus eventBus, String json) {
        try {
            JSONObject rootObject = new JSONObject(json);
            JSONArray result = rootObject.getJSONArray("params");

            lightingPresetId = result.getInt(0);

            eventBus.postSticky(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public int getLightingPresetId() {
        return lightingPresetId;
    }

}
