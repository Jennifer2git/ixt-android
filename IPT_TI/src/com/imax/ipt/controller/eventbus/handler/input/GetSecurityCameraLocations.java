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
import com.imax.ipt.model.SecurityCameraLocation;
import com.imax.iptevent.EventBus;

public class GetSecurityCameraLocations extends Handler {

    public static final String TAG = "GetSecurityCameraLocations";
    public static String METHOD_NAME = "getSecurityCameraLocations";

    private List<SecurityCameraLocation> mSecurityCamLocations;
    private int selectedId;

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            JSONArray jsonArrayLocations = result.getJSONArray("locations");
            mSecurityCamLocations = new ArrayList<SecurityCameraLocation>();
            Gson gson = new Gson();
            for (int i = 0; i < jsonArrayLocations.length(); i++) {
                JSONObject jsonObjectLocation = jsonArrayLocations.getJSONObject(i);
                SecurityCameraLocation securityCameraLocation = gson.fromJson(jsonObjectLocation.toString(), SecurityCameraLocation.class);
                mSecurityCamLocations.add(securityCameraLocation);
            }
            selectedId = result.getInt("selectedId");

            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public Request getRequest() {
        return new Request(GET_SECURITY_CAM_LOCATION, METHOD_NAME, getParameters(), this);
    }

    public List<SecurityCameraLocation> getmSecurityCamLocations() {
        return mSecurityCamLocations;
    }

    public int getSelectedId() {
        return selectedId;
    }
}
