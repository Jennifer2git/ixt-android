package com.imax.ipt.controller.eventbus.handler.media;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class ScreenShareHandler extends Handler {
    public static final String TAG = "DisplayScreenShareButton";
    public static String METHOD_NAME = "screenShare";

    private boolean mShareButton;

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);

    }

    public boolean isShareButton() {
        return mShareButton;
    }

    public void setShareButton(boolean mShareButton) {
        this.mShareButton = mShareButton;
    }

    @Override
    public Request getRequest() {
        return new Request(SCREEN_SHARE_BUTTON, METHOD_NAME, getParameters(), this);
    }

}
