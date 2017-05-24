package com.imax.ipt.controller.eventbus.handler.media;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.ipt.model.MediaType;
import com.imax.ipt.model.MovieLite;
import com.imax.iptevent.EventBus;


public class GetZaxelHandler extends Handler {
    public static final String TAG = "GetZaxelHandler";
    public static String METHOD_NAME = "getZaxel";
    public int response;
    private String content;

    private int cmd;
    /**
     * 参数个数，0代表不带参数，1，代表带参数
     */
    private int paramCount;
    /**
     * 如果参数个数为0，则参数为空
     */
    private String param;


    public GetZaxelHandler(int cmd, int paramCount, String param) {
        super();
        this.cmd = cmd;
        this.paramCount = paramCount;
        this.param = param;
    }


    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(cmd);
        list.add(paramCount);
        list.add(param);
        return list;
    }


    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            int response = result.getInt("response");
            String content = result.getString("content");

            this.response = response;
            this.content = content;
            eventBus.post(this);

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public Request getRequest() {
        return new Request(GET_ZAXEL, METHOD_NAME, getParameters(), this);
    }


    public int getResponse() {
        return response;
    }

    public String getContent() {
        return content;
    }
}
