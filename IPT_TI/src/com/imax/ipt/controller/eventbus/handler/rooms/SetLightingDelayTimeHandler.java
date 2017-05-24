package com.imax.ipt.controller.eventbus.handler.rooms;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanli on 2015/10/21.
 * for set lighting delay time
 */
public class SetLightingDelayTimeHandler extends Handler {

    public static final String TAG = "SetLightingDelayTimeHandler";
    public static final String METHOD_NAME = "setLightingDelayTime";
    private int lightingDelayTime = 5;
    private int response = 0; //0-ok, 1-error.

    public SetLightingDelayTimeHandler(int lightingDelayTime){
        super();
        this.lightingDelayTime = lightingDelayTime;
    }
    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(lightingDelayTime);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        //do nothing.

    }

    @Override
    public Request getRequest() {
        return new Request(SET_LIGHTING_DELAY_TIME,METHOD_NAME,getParameters(),this);
    }

    public int getResponse(){
        return response;
    }
    public void setResponse(int response){
        this.response = response;
    }
}
