package com.imax.ipt.controller.eventbus.handler.movie;

import android.util.Log;
import com.google.gson.Gson;
import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.ipt.model.Movie;
import com.imax.iptevent.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetMovieHandler extends Handler {
    public static final String TAG = "GetMovieHandler";
    public static String METHOD_NAME = "getMovie";

    private String guid;
    private Movie movie;

    public GetMovieHandler(String guid) {
        this.guid = guid;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(guid);
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            JSONObject jsonObjectMovie = result.getJSONObject("movie");
            String mov = jsonObjectMovie.toString();
            movie = new Gson().fromJson(mov, Movie.class);
            this.setMovie(movie);
            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public Request getRequest() {
        return new Request(GET_MOVIE, METHOD_NAME, getParameters(), this);
    }

    /**
     *
     */
    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
