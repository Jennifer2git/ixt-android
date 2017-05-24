package com.imax.ipt.controller.eventbus.handler.music;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.ipt.model.MediaType;
import com.imax.ipt.model.MusicAlbum;
import com.imax.iptevent.EventBus;

public class GetPlayingMusicTrackHandler extends Handler {

    private static final String TAG = "GetPlayingMusicTrackHandler";
    private static final String METHOD_NAME = "getPlayingMusicTrack";

    private MusicAlbum mMusicAlbum;

    @Override
    public List<Object> getParameters() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        Log.d(TAG, sbResult);
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");

            mMusicAlbum = new Gson().fromJson(result.getJSONObject("musicAlbum").toString(), MusicAlbum.class);
            mMusicAlbum.setMediaType(MediaType.ALBUMS);

            eventBus.post(this);

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public Request getRequest() {
        return new Request(GET_PLAYING_MUSIC_TRACK, METHOD_NAME, getParameters(), this);
    }

    public MusicAlbum getmMusicAlbum() {
        return mMusicAlbum;
    }
}
