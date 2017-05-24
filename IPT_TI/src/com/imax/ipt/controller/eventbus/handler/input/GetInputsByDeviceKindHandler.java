package com.imax.ipt.controller.eventbus.handler.input;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.ipt.model.DeviceType;
import com.imax.ipt.model.Input;
import com.imax.iptevent.EventBus;

public class GetInputsByDeviceKindHandler extends Handler {
    public static final String TAG = "GetInputsByDeviceKindHandler";
    public static final String METHOD_NAME = "getInputsByDeviceKind";

    private String deviceKind;
    private Input[] inputs;

    public GetInputsByDeviceKindHandler(String deviceKind) {
        super();
        this.deviceKind = deviceKind;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(deviceKind);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            JSONArray jsonArrayGenres = result.getJSONArray("inputs");
            inputs = new Input[jsonArrayGenres.length()];
            Gson gson = new Gson();
            for (int i = 0; i < jsonArrayGenres.length(); i++) {
                JSONObject jsonObjectGenre = jsonArrayGenres.getJSONObject(i);
                Input input = gson.fromJson(jsonObjectGenre.toString(), Input.class);
                inputs[i] = input;
            }
            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public Input[] getInputs() {
        return inputs;
    }

    @Override
    public Request getRequest() {
        return new Request(GET_INPUT_BY_KIND, METHOD_NAME, getParameters(), this);
    }
}
