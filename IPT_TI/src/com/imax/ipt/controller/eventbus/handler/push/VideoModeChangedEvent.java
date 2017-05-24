package com.imax.ipt.controller.eventbus.handler.push;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.ipt.controller.eventbus.handler.input.SetVideoModeHandler.VideoMode;
import com.imax.iptevent.EventBus;

public class VideoModeChangedEvent extends PushHandler {

    public static final String TAG = "VideoModeChangedEvent";
    private VideoMode videoMode;

//	   TwoD(0),
//	   ThreeDFp(1),
//	   ThreeDSs(2),
//	   ThreeDTb(3);

    @Override
    public void execute(EventBus eventBus, String json) {
        try {
            JSONObject rootObject = new JSONObject(json);
            JSONArray result = rootObject.getJSONArray("params");
            videoMode = VideoMode.getVideoMode(result.getInt(0));
            eventBus.postSticky(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public VideoMode getVideoMode() {
        return videoMode;
    }
}
