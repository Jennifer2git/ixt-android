package com.imax.ipt.controller.eventbus.handler.music;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class SetMusicAlbumFavoriteStatusHandler extends Handler {
    public static final String TAG = "SetMusicAlbumFavoriteStatusHandler";
    public static String METHOD_NAME = "setMusicAlbumFavoriteStatus";

    private String musicAlbumId;
    private boolean isFavorited;

    public SetMusicAlbumFavoriteStatusHandler(String musicAlbumId, boolean isFavorited) {
        super();
        this.musicAlbumId = musicAlbumId;
        this.isFavorited = isFavorited;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(musicAlbumId);
        list.add(isFavorited);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        eventBus.post(this);
    }

    @Override
    public Request getRequest() {
        return new Request(SET_MUSIC_ALBUM_FAVORITE_STATUS, METHOD_NAME, getParameters(), this);
    }

    public boolean isFavorited() {
        return isFavorited;
    }
}
