package com.imax.ipt.controller.eventbus.handler.push;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.iptevent.EventBus;

public class AudioMuteChangedEvent extends PushHandler {
    public static final String TAG = "AudioMuteChangedEvent";
    private boolean mMute = false;


    /**
     * 07-29 13:01:23.012: D/AudioMuteChangedEvent(12336): {"id":0,"method":"audioMuteChanged","params":[false]}
     */
    @Override
    public void execute(EventBus eventBus, String json) {
        Log.d(TAG, json);
        try {
            JSONObject rootObject = new JSONObject(json);
            JSONArray result = rootObject.getJSONArray("params");
            mMute = result.getBoolean(0);
            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Getters and Setters
     */
    public boolean ismMute() {
        return mMute;
    }

    public void setmMute(boolean mMute) {
        this.mMute = mMute;
    }
}
