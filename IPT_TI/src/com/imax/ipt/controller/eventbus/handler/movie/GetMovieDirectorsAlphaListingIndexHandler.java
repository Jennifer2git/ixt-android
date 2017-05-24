package com.imax.ipt.controller.eventbus.handler.movie;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class GetMovieDirectorsAlphaListingIndexHandler extends Handler {
    public static String TAG = "GetMovieAlphaListingIndexHandler";
    public static String METHOD_NAME = "getMovieDirectorAlphaIndex";
    private String mSearchLetterAlpha;
    private int index = 0;
    private String movieId;

    public GetMovieDirectorsAlphaListingIndexHandler(String mSearchLetterAlpha) {
        this.mSearchLetterAlpha = mSearchLetterAlpha;
    }

    /**
     * string alpha, Guid movieId, string searchStringLastName, string
     * searchStringFirstName
     */
    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(getSearchLetterAlpha());
        list.add(getMovieId());
        list.add(getSearchString());
        list.add(getSearchString());
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

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    @Override
    public Request getRequest() {
        return new Request(GET_ALPHA_MOVIE_DIRECTOR_LISTING_INDEX, METHOD_NAME, getParameters(), this);
    }

}
