package com.imax.ipt.controller.eventbus.handler.music;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class DisplayMusicAlbumBrowseOnScreenHandler extends Handler {
    public static final String TAG = "DisplayMusicAlbumBrowseOnScreenHandler";
    public static final String METHOD_NAME = "displayMusicAlbumBrowseOnScreen";

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(getStartIndex());
        list.add(getMusicArtistId());
        list.add(getGenreId());
        list.add(getSearchString());
        list.add(getFavorite());
        return list;
    }

    /**
     *
     */
    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
    }

    /**
     *
     */
    @Override
    public Request getRequest() {
        return new Request(DISPLAY_MUSIC_ALBUM_BROWSE_ON_SCREEN, METHOD_NAME, getParameters(), this);
    }
}
