package com.imax.ipt.controller.eventbus.handler.settings.maintenance;

import android.util.Log;
import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetVersionHandler extends Handler {
    public static final String TAG = "GetVersionHandler";
    private static String METHOD_NAME = "getVersionHandler";

    private String pc;
    private String amx;
    private String android;
    private String sn;
    private String hardware;

    public GetVersionHandler() {
        super();
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        return list;
    }


    // / return powerState (integer)
    // / 1 = On
    // / 2 = Powering Off
    // / 3 = Off
    // / 4 = Powering On

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);

        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            Log.d(TAG,"the result from server is :" + result);
            pc = result.getString("PC");
            amx = result.getString("AMX");
            android = result.getString("Android");
            sn = result.getString("SN");
            hardware = result.getString("Hardware");
            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public Request getRequest() {
        return new Request(GET_VERSION, METHOD_NAME,
                getParameters(), this);
    }

    public String getPCVersion() {
        return pc;
    }

    public String getAndroidVersion() {
        return android;
    }

    public String getAMXVersion() {
        return amx;
    }

    public String getSNVersion() {
        return sn;
    }public String getHardwareVersion() {
        return hardware;
    }

}
