package com.imax.ipt.controller.eventbus.handler.music;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class GetMusicAlphaListingIndexHandler extends Handler {
    public static String TAG = "GetMusicAlphaListingIndexHandler";
    public static String METHOD_NAME = "getMusicAlbumAlphaIndex";
    private String mSearchLetterAlpha;
    private int index = 0;


    public GetMusicAlphaListingIndexHandler(String mSearchLetterAlpha) {
        super();
        this.mSearchLetterAlpha = mSearchLetterAlpha;
    }

    /**
     * string alpha, int genreId, Guid actorId, Guid directorId, int[] years, int
     * bluray, int favorite, int imax, int threeD, string searchString);
     */
    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(getSearchLetterAlpha());
        list.add(getMusicArtistId());
        list.add(getGenreId());
        list.add(getSearchString());
        list.add(getFavorite());
        return list;
    }


    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            int position = result.getInt("intValue");
            setIndex(position);
            eventBus.post(this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getSearchLetterAlpha() {
        return mSearchLetterAlpha;
    }

    public void setSearchLetterAlpha(String mSearchLetterAlpha) {
        this.mSearchLetterAlpha = mSearchLetterAlpha;
    }

    @Override
    public Request getRequest() {
        return new Request(GET_ALPHA_MUSIC_LISTING_INDEX, METHOD_NAME, getParameters(), this);
    }

}
