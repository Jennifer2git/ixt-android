package com.imax.ipt.controller.eventbus.handler.push;

import android.util.Log;
import com.imax.ipt.common.PowerState;
import com.imax.iptevent.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SystemPowerStateChangedEvent extends PushHandler {
    public static final String TAG = SystemPowerStateChangedEvent.class.getSimpleName();
    private PowerState mPowerState;

    @Override
    public void execute(EventBus eventBus, String json) {
        Log.d(TAG, json);

        try {
            JSONObject rootObject = new JSONObject(json);
            JSONArray result = rootObject.getJSONArray("params");
            this.mPowerState = PowerState.getPowerState(result.getInt(0));
            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

    }

    public PowerState getmPowerState() {
        return mPowerState;
    }

}
