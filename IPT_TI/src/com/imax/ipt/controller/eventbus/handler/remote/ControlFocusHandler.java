package com.imax.ipt.controller.eventbus.handler.remote;

import android.util.Log;
import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ControlFocusHandler extends Handler {
    public static String TAG = "ControlFocusHandler";
    public static String METHOD_NAME = "controlFocus";

//    private String cmd_LeftFocusStart = "LeftFocusStart";
//    private String cmd_LeftFocusFar = "LeftFocusFar";
//    private String cmd_LeftFocusNear = "LeftFocusNear";
//    private String cmd_RightFocusStart = "RightFocusStart";
//    private String cmd_RightFocusFar = "RightFocusFar";
//    private String cmd_RightFocusNear = "RightFocusNear";
    private String cmdFocus = "";
    private int focusState ;
//    private String cmd_AllFocusEnd = "AllFocusEnd";


    public ControlFocusHandler(String cmdFocus) {
        this.cmdFocus = cmdFocus;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(cmdFocus);
        return list;
    }


    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            focusState = result.getInt("intValue");
            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public Request getRequest() {
        return new Request(CONTROL_FOCUS, METHOD_NAME, getParameters(), this);
    }

    /**
     * Getters and setters
     ***/
    public double getFocusState() {
        return focusState;
    }

    public void setFocusCMD (String cmdFocus) {
        this.cmdFocus = cmdFocus;
    }
}
