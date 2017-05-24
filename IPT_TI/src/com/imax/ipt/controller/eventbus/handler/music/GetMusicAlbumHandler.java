package com.imax.ipt.controller.eventbus.handler.music;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.ipt.model.MusicAlbum;
import com.imax.iptevent.EventBus;

public class GetMusicAlbumHandler extends Handler {
    public static final String TAG = "GetMusicAlbumHandler";
    private static String METHOD_NAME = "getMusicAlbum";

    private String albumId;
    private boolean withTracks = true;

    private MusicAlbum musicAlbum;

    public GetMusicAlbumHandler(String albumId, boolean withTracks) {
        super();
        this.albumId = albumId;
        this.withTracks = withTracks;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(albumId);
        list.add(withTracks);
        return list;
    }

    /**
     * { "id":26, "result":{ "musicAlbum":{ "id":3, "name":"Clumsy",
     * "coverArtPath":
     * "http://172.31.10.130/ShowCenter/Temp/Thumb/ACA_3362459559_20120905114959_255x255.jpg"
     * , "tracks":[ { "id":1071, "title":"Superman's Dead", "albumId":3,
     * "artist":{ "id":3, "lastName":"Our Lady", "firstName":"Peace" }, "genre":{
     * "id":2, "name":"Alternative" } }, { "id":1072,
     * "title":"Automatic Flowers", "albumId":3, "artist":{ "id":3,
     * "lastName":"Our Lady", "firstName":"Peace" }, "genre":{ "id":2,
     * "name":"Alternative" } }, { "id":1073, "title":"Carnival", "albumId":3,
     * "artist":{ "id":3, "lastName":"Our Lady", "firstName":"Peace" }, "genre":{
     * "id":2, "name":"Alternative" } }, { "id":1074, "title":"Big Dumb Rocket",
     * "albumId":3, "artist":{ "id":3, "lastName":"Our Lady", "firstName":"Peace"
     * }, "genre":{ "id":2, "name":"Alternative" } }, { "id":1075,
     * "title":"4 A.M.", "albumId":3, "artist":{ "id":3, "lastName":"Our Lady",
     * "firstName":"Peace" }, "genre":{ "id":2, "name":"Alternative" } }, {
     * "id":1076, "title":"Shaking", "albumId":3, "artist":{ "id":3,
     * "lastName":"Our Lady", "firstName":"Peace" }, "genre":{ "id":2,
     * "name":"Alternative" } }, { "id":1077, "title":"Clumsy", "albumId":3,
     * "artist":{ "id":3, "lastName":"Our Lady", "firstName":"Peace" }, "genre":{
     * "id":2, "name":"Alternative" } }, { "id":1078, "title":"Hello Oskar",
     * "albumId":3, "artist":{ "id":3, "lastName":"Our Lady", "firstName":"Peace"
     * }, "genre":{ "id":2, "name":"Alternative" } }, { "id":1079,
     * "title":"Let You Down", "albumId":3, "artist":{ "id":3,
     * "lastName":"Our Lady", "firstName":"Peace" }, "genre":{ "id":2,
     * "name":"Alternative" } }, { "id":1080, "title":"The Story of 100 Aisles",
     * "albumId":3, "artist":{ "id":3, "lastName":"Our Lady", "firstName":"Peace"
     * }, "genre":{ "id":2, "name":"Alternative" } }, { "id":1081,
     * "title":"Car Crash", "albumId":3, "artist":{ "id":3,
     * "lastName":"Our Lady", "firstName":"Peace" }, "genre":{ "id":2,
     * "name":"Alternative" } } ] }, "response":0 } }
     */
    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);

        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            JSONObject jsonMusicAlbum = result.getJSONObject("musicAlbum");
            Gson gson = new Gson();
            this.musicAlbum = gson.fromJson(jsonMusicAlbum.toString(), MusicAlbum.class);
            eventBus.post(this);

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public MusicAlbum getMusicAlbum() {
        return musicAlbum;
    }

    public void setMusicAlbum(MusicAlbum musicAlbum) {
        this.musicAlbum = musicAlbum;
    }

    @Override
    public Request getRequest() {
        return new Request(GET_MUSIC_ALBUM, METHOD_NAME, getParameters(), this);
    }
}
