package com.imax.ipt.controller.eventbus.handler.push;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.ipt.controller.eventbus.handler.rooms.GetCurtainStateHandler.CurtainState;
import com.imax.iptevent.EventBus;

public class CurtainStateChangedEvent extends PushHandler {

    public static final String TAG = "CurtainStateChangedEvent";
    private CurtainState curtainState;

    @Override
    public void execute(EventBus eventBus, String json) {
        try {
            JSONObject rootObject = new JSONObject(json);
            JSONArray result = rootObject.getJSONArray("params");

            if (result.getBoolean(0)) {
                curtainState = CurtainState.Opened;
            } else {
                curtainState = CurtainState.Closed;
            }
            eventBus.postSticky(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public CurtainState getCurtainState() {
        return curtainState;
    }
}
