package com.imax.ipt.controller.eventbus.handler.input;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class SetVideoModeHandler extends Handler {
    public static final String TAG = "SetVideoModeHandler";
    public static String METHOD_NAME = "setVideoMode";

    public enum VideoMode {
        TwoD(0),
        ThreeDFp(1),
        ThreeDSs(2),
        ThreeDTb(3);

        private int value;

        private VideoMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static VideoMode getVideoMode(int value) {
            VideoMode[] values = values();
            for (VideoMode videoMode : values) {
                if (videoMode.value == value)
                    return videoMode;
            }
            throw new IllegalArgumentException("VideoMode does not support");
        }
    }

    private VideoMode videoMode;
    private int inputId;

    public SetVideoModeHandler(VideoMode videoMode, int inputId) {
        super();
        this.videoMode = videoMode;
        this.inputId = inputId;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(videoMode.getValue());
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
        return new Request(SET_VIDEO_MODE, METHOD_NAME, getParameters(), this);
    }

}
