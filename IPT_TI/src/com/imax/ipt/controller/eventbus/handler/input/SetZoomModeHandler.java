package com.imax.ipt.controller.eventbus.handler.input;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class SetZoomModeHandler extends Handler {
    public static final String TAG = "SetZoomModeHandler";
    public static String METHOD_NAME = "setZoomMode";

    public enum ZoomMode {
        Normal(0),
        Wide(1);

        private int value;

        private ZoomMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static ZoomMode getZoomMode(int value) {
            ZoomMode[] values = values();
            for (ZoomMode videoMode : values) {
                if (videoMode.value == value)
                    return videoMode;
            }
            throw new IllegalArgumentException("VideoMode does not support");
        }
    }

    private ZoomMode zoomMode;
    private int inputId;

    public SetZoomModeHandler(ZoomMode zoomMode, int inputId) {
        super();
        this.zoomMode = zoomMode;
        this.inputId = inputId;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(zoomMode.getValue());
        list.add(inputId);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        eventBus.post(this);
    }

    @Override
    public Request getRequest() {
        return new Request(SET_ZOOM_MODE, METHOD_NAME, getParameters(), this);
    }

}
