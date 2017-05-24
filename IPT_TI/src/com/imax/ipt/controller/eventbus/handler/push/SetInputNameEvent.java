package com.imax.ipt.controller.eventbus.handler.push;

import android.util.Log;
import com.imax.iptevent.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SetInputNameEvent extends PushHandler {
    public static final String TAG = "SetInputNameEvent";


    @Override
    public void execute(EventBus eventBus, String json) throws JSONException {
        JSONObject rootObject = new JSONObject(json);
        JSONArray result = rootObject.getJSONArray("params");
//        input = new Gson().fromJson(result.getString(0), Input.class);
        Log.d(TAG, "SetInputNameEvent execute json ¡êo" + json);
        eventBus.post(this);
    }

}
