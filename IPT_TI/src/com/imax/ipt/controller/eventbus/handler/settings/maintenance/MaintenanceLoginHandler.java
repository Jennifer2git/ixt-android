package com.imax.ipt.controller.eventbus.handler.settings.maintenance;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class MaintenanceLoginHandler extends Handler {

    private static final String TAG = "MaintenanceLoginHandler";
    public static final String METHOD_NAME = "maintenanceLogin";

    private String password;
    private int mLoginResponse;

    public static final int MAINTENANCE_RESPONSE_OK = 0;
    public static final int MAINTENANCE_RESPONSE_BAD = 1;


    public MaintenanceLoginHandler(String password) {
        super();
        this.password = password;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(password);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            mLoginResponse = result.getInt("response");
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        eventBus.post(this);
    }

    @Override
    public Request getRequest() {
        return new Request(MAINTENANCE_LOGIN, METHOD_NAME, getParameters(), this);
    }

    /**
     * Getters and Setters
     */
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getmLoginResponse() {
        return mLoginResponse;
    }

    public void setmLoginResponse(int mLoginResponse) {
        this.mLoginResponse = mLoginResponse;
    }


}
