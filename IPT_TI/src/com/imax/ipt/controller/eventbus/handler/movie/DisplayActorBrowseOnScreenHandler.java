package com.imax.ipt.controller.eventbus.handler.movie;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class DisplayActorBrowseOnScreenHandler extends Handler {
    public static final String TAG = "DisplayActorBrowseOnScreenHandler";
    public static final String METHOD_NAME = "displayActorBrowseOnScreen";

    private String mMovieId;

    public DisplayActorBrowseOnScreenHandler(String movieId) {
        this.mMovieId = movieId;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(getStartIndex());
        list.add(mMovieId);
        list.add(getSearchString());
        list.add(getSearchString());
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
    }

    @Override
    public Request getRequest() {
        return new Request(DISPLAY_ACTOR_BROWSE_ON_SCREEN, METHOD_NAME, getParameters(), this);
    }

}
