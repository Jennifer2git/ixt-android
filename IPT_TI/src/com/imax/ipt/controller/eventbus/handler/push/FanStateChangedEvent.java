package com.imax.ipt.controller.eventbus.handler.push;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.iptevent.EventBus;

public class FanStateChangedEvent extends PushHandler {
    public static final String TAG = "FanStateChangedEvent";
    private FanState fanState;

    public enum FanState {
        Unknown(-1),
        On(0),
        Auto(1);

        private int value;

        private FanState(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static FanState getFanState(int value) {
            FanState[] values = values();
            for (FanState fanState : values) {
                if (fanState.value == value)
                    return fanState;
            }
            throw new IllegalArgumentException("FanState does not support");
        }
    }

    @Override
    public void execute(EventBus eventBus, String json) {
        try {
            JSONObject rootObject = new JSONObject(json);
            JSONArray result = rootObject.getJSONArray("params");
            fanState = FanState.getFanState(result.getInt(0));
            eventBus.postSticky(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public FanState getFanState() {
        return fanState;
    }

}
