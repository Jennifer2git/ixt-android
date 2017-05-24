package com.imax.ipt.controller.eventbus.handler.settings.maintenance;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class GetPcabCalibrationStatusHandler extends Handler {
    public static final String TAG = "getPcabCalibrationTime";
    private static String METHOD_NAME = "getPcabCalibrationStatus";

    //0正常，1，正在校准中，2，未知
    private int pcabStatus;
    private String lastTime;
    private String nextTime;

    public GetPcabCalibrationStatusHandler() {

    }

    @Override
    public List<Object> getParameters() {
        return null;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            pcabStatus = result.getInt("pcabStatus");
            lastTime = result.getString("lastTime");
            nextTime = result.getString("nextTime");

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        eventBus.post(this);
    }

    @Override
    public Request getRequest() {
        return new Request(GET_PCAB_CALIBRATION_STATUS, METHOD_NAME,
                getParameters(), this);
    }

    public int getPcabStatus() {
        return pcabStatus;
    }

    public String getLastTime() {
        return lastTime;
    }

    public String getNextTime() {
        return nextTime;
    }

}
