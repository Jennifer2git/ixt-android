package com.imax.ipt.controller.eventbus.handler.music;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class DisplayMusicAlbumDetailsOnScreenHandler extends Handler {
    public static final String TAG = "DisplayMusicAlbumDetailsOnScreenHandler";
    public static final String METHOD_NAME = "displayMusicAlbumDetailsOnScreen";

    private String id;

    public DisplayMusicAlbumDetailsOnScreenHandler(String id) {
        super();
        this.id = id;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(id);
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
        return new Request(DISPLAY_MUSIC_ALBUM_DETAILS_ON_SCREEN, METHOD_NAME, getParameters(), this);
    }
}
