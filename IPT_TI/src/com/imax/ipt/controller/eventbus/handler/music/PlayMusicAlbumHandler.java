package com.imax.ipt.controller.eventbus.handler.music;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class PlayMusicAlbumHandler extends Handler {
    public static final String TAG = "PlayMusicAlbumHandler";
    public static String METHOD_NAME = "playMusicAlbum";

    private String uuid;

    public PlayMusicAlbumHandler(String uuid) {
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
        return new Request(PLAY_ALBUM, METHOD_NAME, getParameters(), this);
    }

}
