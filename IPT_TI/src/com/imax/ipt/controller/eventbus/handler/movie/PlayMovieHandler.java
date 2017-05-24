package com.imax.ipt.controller.eventbus.handler.movie;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class PlayMovieHandler extends Handler {
    public static final String TAG = "PlayMovieHandler";
    public static String METHOD_NAME = "playMovie";

    private String guid;

    public PlayMovieHandler(String guid) {
        this.guid = guid;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(guid);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);

    }

    @Override
    public Request getRequest() {
        return new Request(PLAY_MOVIE, METHOD_NAME, getParameters(), this);
    }

}
