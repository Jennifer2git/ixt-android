package com.imax.ipt.controller.eventbus.handler.music;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class DisplayMusicArtistBrowseOnScreenHandler extends Handler {
    public static final String TAG = "DisplayMusicArtistBrowseOnScreenHandler";
    public static final String METHOD_NAME = "displayMusicArtistBrowseOnScreen";

    public DisplayMusicArtistBrowseOnScreenHandler() {
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(getStartIndex());
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
        return new Request(DISPLAY_MUSIC_ARTIST_BROWSE_ON_SCREEN, METHOD_NAME, getParameters(), this);
    }

}
