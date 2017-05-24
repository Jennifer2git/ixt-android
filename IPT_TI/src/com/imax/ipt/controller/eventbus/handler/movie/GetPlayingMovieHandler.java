package com.imax.ipt.controller.eventbus.handler.movie;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.ipt.model.MediaType;
import com.imax.ipt.model.MovieLite;
import com.imax.iptevent.EventBus;


public class GetPlayingMovieHandler extends Handler {

    private static final String TAG = "GetPlayingMovieHandler";
    private static final String METHOD_NAME = "getPlayingMovie";

    private MovieLite movieLite;

    @Override
    public List<Object> getParameters() {
        return new ArrayList<Object>();
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            JSONObject jsonMovieLite = result.getJSONObject("movieLite");

            movieLite = new MovieLite();
            movieLite.setId(jsonMovieLite.getString("id"));
            movieLite.setTitle(jsonMovieLite.getString("title"));
            movieLite.setCoverArtPath(jsonMovieLite.getString("coverArtPath"));
            movieLite.setLoading(jsonMovieLite.getBoolean("loading"));
            movieLite.setMediaType(MediaType.MOVIE);

            eventBus.post(this);

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public Request getRequest() {
        return new Request(GET_PLAYING_MOVIE, METHOD_NAME, getParameters(), this);
    }

    public MovieLite getMovieLite() {
        return movieLite;
    }
}
