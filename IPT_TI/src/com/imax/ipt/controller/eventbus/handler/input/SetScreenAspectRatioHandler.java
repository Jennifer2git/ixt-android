package com.imax.ipt.controller.eventbus.handler.input;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class SetScreenAspectRatioHandler extends Handler {
    public static final String TAG = "SetScreenAspectRatioHandler";
    public static String METHOD_NAME = "setScreenAspectRatio";

    public enum ScreenAspectRatio {
        Default(0),
        Hd(1),
        Scope(2);

        private int value;

        private ScreenAspectRatio(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static ScreenAspectRatio getScreenAspectRatio(int value) {
            ScreenAspectRatio[] values = values();
            for (ScreenAspectRatio aspectRatio : values) {
                if (aspectRatio.value == value)
                    return aspectRatio;
            }
            throw new IllegalArgumentException("ScreenAspectRatio not support");
        }
    }

    private ScreenAspectRatio aspectRatio;

    public SetScreenAspectRatioHandler(ScreenAspectRatio aspectRatio) {
        super();
        this.aspectRatio = aspectRatio;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(aspectRatio.getValue());
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        eventBus.post(this);
    }

    @Override
    public Request getRequest() {
        return new Request(SET_SCREEN_ASPECT_RATIO, METHOD_NAME, getParameters(), this);
    }

}
