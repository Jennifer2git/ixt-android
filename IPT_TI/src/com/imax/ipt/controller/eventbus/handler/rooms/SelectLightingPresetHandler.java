package com.imax.ipt.controller.eventbus.handler.rooms;

import java.util.ArrayList;
import java.util.List;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class SelectLightingPresetHandler extends Handler {

    public static final String TAG = "SelectLightingPresetHandler";
    public static final String METHOD_NAME = "selectLightingPreset";
    private int lightingPresetId;


    public SelectLightingPresetHandler(int lightingPresetId) {
        super();
        this.lightingPresetId = lightingPresetId;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(lightingPresetId);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {

    }

    @Override
    public Request getRequest() {
        return new Request(SELECT_LIGHTING_PRESET, METHOD_NAME, getParameters(), this);
    }

    public int getLightingPresetId() {
        return lightingPresetId;
    }

    public void setLightingPresetId(int lightingPresetId) {
        this.lightingPresetId = lightingPresetId;
    }

}
