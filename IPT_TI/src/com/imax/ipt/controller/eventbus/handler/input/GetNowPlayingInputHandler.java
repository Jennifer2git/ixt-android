package com.imax.ipt.controller.eventbus.handler.input;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.ipt.model.DeviceType;
import com.imax.ipt.model.Input;
import com.imax.ipt.model.LightingPreset;
import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable.DeviceKind;
import com.imax.iptevent.EventBus;

public class GetNowPlayingInputHandler extends Handler {
    public static final String TAG = "GetNowPlayingInputHandler";
    public static final String METHOD_NAME = "getNowPlayingInput";

    private Input nowPlayingInput;

    @Override
    public List<Object> getParameters() {
        return new ArrayList<Object>();
    }

    /**
     * {"id":39,"result":{"lightingPresets":[{"id":4,"displayName":"Movie"},{"id"
     * :6,"displayName":"Gaming"},{"id":7,"displayName":"Lounge"},{"id":8,
     * "displayName"
     * :"Party"},{"id":9,"displayName":"Full"},{"id":10,"displayName"
     * :"Low"}],"activePresetId":4,"response":0}}
     */
    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");

            JSONObject jsonNowPlaying = result.getJSONObject("input");
            nowPlayingInput = new Input();
            nowPlayingInput.setId(jsonNowPlaying.getInt("id"));
            nowPlayingInput.setDisplayName(jsonNowPlaying.getString("displayName"));
            nowPlayingInput.setDeviceKind(DeviceKind.getDeviceKindEnum(jsonNowPlaying.getString("deviceKind")));
            nowPlayingInput.setActive(jsonNowPlaying.getBoolean("active"));
            nowPlayingInput.setIrSupported(jsonNowPlaying.getBoolean("irSupported"));
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        eventBus.post(this);
    }

    /**
     * Getters and Setters
     *
     * @return
     */
    public Input getNowPlayingInput() {
        return nowPlayingInput;
    }

    @Override
    public Request getRequest() {
        return new Request(GET_NOW_PLAYING_INPUT, METHOD_NAME, getParameters(), this);
    }

}
