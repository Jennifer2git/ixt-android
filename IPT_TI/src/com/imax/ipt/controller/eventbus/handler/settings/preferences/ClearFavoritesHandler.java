package com.imax.ipt.controller.eventbus.handler.settings.preferences;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class ClearFavoritesHandler extends Handler {
    public static final String TAG = "ClearFavoritesHandler";
    public static final String METHOD_NAME = "clearFavorites";

    public static final int FAVORITE_TYPE_MOVIE = 1;
    public static final int FAVORITE_TYPE_MUSIC_ALBUM = 2;
    public static final int FAVORITE_TYPE_CHANNEL_PRESET = 3;

    private int favoriteType;

    public ClearFavoritesHandler(int favoriteType) {
        this.favoriteType = favoriteType;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(favoriteType);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);

        eventBus.post(this);
    }

    /**
     * Getters and Setters
     *
     * @return
     */

    @Override
    public Request getRequest() {
        return new Request(CLEAR_FAVORITES, METHOD_NAME, getParameters(), this);
    }

}
