package com.imax.ipt.controller.eventbus.handler.push;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.iptevent.EventBus;

public class PcabCalibrationStateChangedEvent extends PushHandler {
    public static final String TAG = "PcabCalibrationStateChangedEvent";
    private int pcabState;//0������
    private int progress;//У׼����

    @Override
    public void execute(EventBus eventBus, String json) {
        Log.d(TAG, json);

        try {
            JSONObject rootObject = new JSONObject(json);
            JSONArray result = rootObject.getJSONArray("params");
            Log.d(TAG, "result=" + result);
            this.pcabState = result.getInt(0);
            this.progress = result.getInt(1);
            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

    }

    public int getPcabState() {
        return pcabState;
    }

    public int getProgress() {
        return progress;
    }

}
