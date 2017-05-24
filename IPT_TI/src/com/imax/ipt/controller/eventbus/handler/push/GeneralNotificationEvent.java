package com.imax.ipt.controller.eventbus.handler.push;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.iptevent.EventBus;

public class GeneralNotificationEvent extends PushHandler {

    public static final String TAG = "GeneralNotificationEvent";

    //0,代表提示关机，
    private int mState;

    @Override
    public void execute(EventBus eventBus, String json) {
        try {
            JSONObject rootObject = new JSONObject(json);
            JSONArray result = rootObject.getJSONArray("params");
            mState = result.getInt(0);
            eventBus.postSticky(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }


    public int getState() {
        return mState;
    }
}
