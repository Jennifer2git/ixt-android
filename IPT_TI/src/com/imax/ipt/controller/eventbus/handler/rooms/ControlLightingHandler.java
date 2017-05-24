package com.imax.ipt.controller.eventbus.handler.rooms;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

import java.util.ArrayList;
import java.util.List;

public class ControlLightingHandler extends Handler {

    public static final String TAG = "ControlLightingHandler";
    public static final String METHOD_NAME = "controlLighting";
    private int lightingCmd;

    //0,Ϊʧ�ܣ�1Ϊ�ɹ�
    private int response;


    public ControlLightingHandler(int lightingCmd) {
        super();
        this.lightingCmd = lightingCmd;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(lightingCmd);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {

    }


    @Override
    public Request getRequest() {
        return new Request(CONTROL_LIGHTING, METHOD_NAME, getParameters(), this);
    }

    public int getLightingCmd() {
        return lightingCmd;
    }

    public int getResponse() {
        return response;
    }

    public void setLightingCmd(int lightingCmd) {
        this.lightingCmd = lightingCmd;
    }

}
