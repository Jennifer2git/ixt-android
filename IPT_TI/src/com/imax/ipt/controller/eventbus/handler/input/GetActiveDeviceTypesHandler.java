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
 * This command is meant to be only called when the system connects (after system powered ON) at the IPTService
 *    if this command is needed anywhere else, please create a new class to avoid EventBus posting the response to IPTSErvice
 */
public class GetActiveDeviceTypesHandler extends Handler {
    public static final String TAG = GetActiveDeviceTypesHandler.class.getSimpleName();
    public static String METHOD_NAME = "getActiveDeviceTypes";
    private DeviceType[] deviceTypes;

    @Override
    public List<Object> getParameters() {
        return new ArrayList<Object>();
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
                if (deviceType.getDeviceKind() != null) {
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
