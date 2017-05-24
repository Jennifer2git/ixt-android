package com.imax.ipt.controller.eventbus.handler.input;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.ipt.model.DeviceType;
import com.imax.ipt.model.Input;
import com.imax.iptevent.EventBus;

public class GetSelectedInputHandler extends Handler {
    public static final String TAG = "GetSelectedInputHandler";

    public static String METHOD_NAME = "getSelectedInput";

    private int ouputIndex;

    private Input input;

    private int outPutIndex;

    public GetSelectedInputHandler(int ouputIndex) {
        super();
        this.ouputIndex = ouputIndex;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(ouputIndex);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            JSONObject inputObject = result.getJSONObject("input");
            input = new Gson().fromJson(inputObject.toString(), Input.class);
            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public Request getRequest() {
        return new Request(GET_SELECTED_INPUT, METHOD_NAME, getParameters(), this);
    }

    /**
     * Getters and Setters
     */

    public Input getInput() {
        return input;
    }

    public int getOutPutIndex() {
        return outPutIndex;
    }

    public void setOutPutIndex(int outPutIndex) {
        this.outPutIndex = outPutIndex;
    }

}
