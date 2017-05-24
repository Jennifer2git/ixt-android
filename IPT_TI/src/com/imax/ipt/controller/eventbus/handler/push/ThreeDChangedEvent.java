package com.imax.ipt.controller.eventbus.handler.push;

import android.util.Log;
import com.imax.iptevent.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ThreeDChangedEvent extends PushHandler {

    public static final String TAG = "ThreeDChangedEvent";

//    1: 3d, 0: 2d
    private int m3DState;

    @Override
    public void execute(EventBus eventBus, String json) {
        try {
            JSONObject rootObject = new JSONObject(json);
            JSONArray result = rootObject.getJSONArray("params");
            m3DState = result.getInt(0);
            eventBus.postSticky(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }


    public int get3DState() {
        return m3DState;
    }
}
