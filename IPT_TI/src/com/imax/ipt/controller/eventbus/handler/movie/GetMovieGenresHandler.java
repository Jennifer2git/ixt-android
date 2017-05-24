package com.imax.ipt.controller.eventbus.handler.movie;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.ipt.model.Genre;
import com.imax.iptevent.EventBus;

public class GetMovieGenresHandler extends Handler {
    public static final String TAG = "GetMovieGenresHandler";

    private String guid;
    private Genre[] genres;

    public static final String METHOD_NAME = "getMovieGenres";

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(getStartIndex());
        list.add(Integer.MAX_VALUE);
        list.add(guid);
        list.add(getSearchString());
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            JSONArray jsonArrayGenres = result.getJSONArray("genres");
            genres = new Genre[jsonArrayGenres.length()];
            Gson gson = new Gson();
            for (int i = 0; i < jsonArrayGenres.length(); i++) {
                JSONObject jsonObjectGenre = jsonArrayGenres.getJSONObject(i);
                Genre genre = gson.fromJson(jsonObjectGenre.toString(), Genre.class);
                genres[i] = genre;
            }
            ;
            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }


    @Override
    public Request getRequest() {
        return new Request(GET_GENRES, METHOD_NAME, getParameters(), this);
    }

    /**
     * Getters and Setters
     */
    public Genre[] getGenres() {
        return genres;
    }

    public void setGenres(Genre[] genres) {
        this.genres = genres;
    }
}
