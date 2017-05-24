package com.imax.ipt.controller.eventbus.handler.settings.multiview;

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

public class GetPipModeHandler extends Handler {
    public static final String TAG = "GetPipModeHandler";
    public static String METHOD_NAME = "getPipMode";
    private boolean withSelectedInputs = false;
    private int pipMode;
    private int audioFocusOutputIndex;
    private ArrayList<Input> inputs;

    public GetPipModeHandler(boolean withSelectedInputs) {
        super();
        this.withSelectedInputs = withSelectedInputs;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(withSelectedInputs);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        try {
            Log.d(TAG, sbResult);
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            this.pipMode = result.getInt("pipMode");
            this.audioFocusOutputIndex = result.getInt("audioFocusOutputIndex");
            JSONArray jsonArrayinputs = result.getJSONArray("inputs");
            inputs = new ArrayList<Input>(jsonArrayinputs.length());
            for (int i = 0; i < jsonArrayinputs.length(); i++) {
                JSONObject jsonObjectInput = jsonArrayinputs.getJSONObject(i);
                Input deviceType = new Gson().fromJson(jsonObjectInput.toString(), Input.class);
                inputs.add(deviceType);
            }
            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public Request getRequest() {
        return new Request(GET_PIP_MODE, METHOD_NAME, getParameters(), this);
    }

    /**
     * Getters and setters
     ***/

    public int getPipMode() {
        return pipMode;
    }

    public ArrayList<Input> getInputs() {
        return inputs;
    }

    public void setInputs(ArrayList<Input> inputs) {
        this.inputs = inputs;
    }

    public void setPipMode(int pipMode) {
        this.pipMode = pipMode;
    }

    public boolean isWithSelectedInputs() {
        return withSelectedInputs;
    }

    public void setWithSelectedInputs(boolean withSelectedInputs) {
        this.withSelectedInputs = withSelectedInputs;
    }

    public int getAudioFocusOutputIndex() {
        return audioFocusOutputIndex;
    }

    public void setAudioFocusOutputIndex(int audioFocusOutputIndex) {
        this.audioFocusOutputIndex = audioFocusOutputIndex;
    }
}
