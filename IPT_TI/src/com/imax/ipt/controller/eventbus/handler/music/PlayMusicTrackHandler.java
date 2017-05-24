package com.imax.ipt.controller.eventbus.handler.music;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class PlayMusicTrackHandler extends Handler {
    public static final String TAG = "PlayMusicTrackHandler";
    public static String METHOD_NAME = "playMusicTrack";

    private String uuid;

    public PlayMusicTrackHandler(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(uuid);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);

    }

    @Override
    public Request getRequest() {
        return new Request(PLAY_TRACK, METHOD_NAME, getParameters(), this);
    }

}
