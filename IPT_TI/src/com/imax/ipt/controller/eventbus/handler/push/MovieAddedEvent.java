package com.imax.ipt.controller.eventbus.handler.push;

import android.util.Log;
import com.google.gson.Gson;
import com.imax.ipt.model.Movie;
import com.imax.iptevent.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieAddedEvent extends PushHandler {
    public static final String TAG = "MovieAddedEvent";

    @Override
    public void execute(EventBus eventBus, String json) {
        Log.d(TAG, "MovieAddedEvent execute json ¡êo" + json);
        eventBus.post(this);
    }

}
