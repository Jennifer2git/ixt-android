package com.imax.ipt.controller.eventbus.handler.input;

import android.util.Log;
import com.google.gson.Gson;
import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.ipt.model.DeviceType;
import com.imax.iptevent.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
 */
public class GetActiveDeviceTypesByLanguageHandler extends Handler {
    public static final String TAG = GetActiveDeviceTypesByLanguageHandler.class.getSimpleName();
    public static String METHOD_NAME = "getActiveDeviceTypesByLanguage";
    private DeviceType[] deviceTypes;
    private int type; // 0-cn, 1--EN

    public GetActiveDeviceTypesByLanguageHandler(int type) {
        this.type = type;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(type);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);

        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            JSONArray jsonArrayGenres = result.getJSONArray("deviceTypes");
            deviceTypes = new DeviceType[jsonArrayGenres.length()];
            Gson gson = new Gson();
            for (int i = 0; i < jsonArrayGenres.length(); i++) {
                JSONObject jsonObjectGenre = jsonArrayGenres.getJSONObject(i);
                DeviceType deviceType = gson.fromJson(jsonObjectGenre.toString(), DeviceType.class);
                if(deviceType.getDeviceKind() != null){
                deviceTypes[i] = deviceType;
                }
            }
            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

    }

    @Override
    public Request getRequest() {
        Request request = new Request();
        request.setMethod(METHOD_NAME);
        request.setId(GET_DEVICES_TYPES);
        request.setParams(getParameters());
        request.setHandler(this);
        return request;
    }

    /**
     * @return
     */
    public DeviceType[] getDeviceTypes() {
        return deviceTypes;
    }

//   /**
//    * 
//    * @param deviceTypes
//    */
//   public void setDeviceTypes(DeviceType[] deviceTypes)
//   {
//      this.deviceTypes = deviceTypes;
//   }

}
