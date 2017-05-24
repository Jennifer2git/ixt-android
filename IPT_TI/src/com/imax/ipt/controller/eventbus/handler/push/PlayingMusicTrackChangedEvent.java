package com.imax.ipt.controller.eventbus.handler.push;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.imax.ipt.model.MediaType;
import com.imax.ipt.model.MusicAlbum;
import com.imax.iptevent.EventBus;

public class PlayingMusicTrackChangedEvent extends PushHandler {
    public static final String TAG = "PlayingMovieChangedEvent";

    private MusicAlbum mMusicAlbum;

    /**
     * {"id":0,"method":"playingMusicTrackChanged","params":[{"id":
     * "741541f0-d9b5-6fa9-0e81-6c2048b492b9"
     * ,"name":"City Of Angels","coverArtPath"
     * :"","loading":false,"favorite":false
     * ,"tracks":[{"id":"46b40b10-9e1d-2d5b-f934-d66e1cd23eb4"
     * ,"title":"U2   If God Will Send His Angels"
     * ,"albumId":"741541f0-d9b5-6fa9-0e81-6c2048b492b9"
     * ,"artist":{"id":"2b92f3a2-bb1e-411e-a108-79ed55caf7c0"
     * ,"lastName":"Soundtrack"
     * ,"firstName":"","coverArtPaths":[]},"genre":{"id":13
     * ,"name":"Soundtrack"}}]}]}
     */

    @Override
    public void execute(EventBus eventBus, String json) {
        try {
            JSONObject rootObject = new JSONObject(json);
            JSONArray result = rootObject.getJSONArray("params");
            mMusicAlbum = new Gson().fromJson(result.getString(0), MusicAlbum.class);
            mMusicAlbum.setMediaType(MediaType.ALBUMS);
            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public MusicAlbum getmMusicAlbum() {
        return mMusicAlbum;
    }

//   public void setmMusicAlbum(MusicAlbum mMusicAlbum)
//   {
//      this.mMusicAlbum = mMusicAlbum;
//   }


}
