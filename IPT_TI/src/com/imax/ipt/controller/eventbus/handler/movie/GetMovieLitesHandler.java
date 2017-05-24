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


public class GetMovieLitesHandler extends Handler {
    public static final String TAG = "GetMovieLitesHandler";
    public static String METHOD_NAME = "getMovieLites";
    public int totalRecordsAvailable;
    private ArrayList<MovieLite> movieLites;


    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(getStartIndex());
        list.add(getCount());
        list.add(getGenreId());
        list.add(getActorId());
        list.add(getDirectorId());
        list.add(getYears());
        list.add(getBluray());
        list.add(getFavorite());
        list.add(getImax());
        list.add(getThreeD());
        list.add(getSearchString());
        list.add(getOrderByOptions());
        return list;
    }


    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            JSONArray jsonArrayMovies = result.getJSONArray("movieLites");
            int totalRecordsAvailable = result.getInt("totalRecordsAvailable");

            ArrayList<MovieLite> movieLites = new ArrayList<MovieLite>(jsonArrayMovies.length());
            for (int i = 0; i < jsonArrayMovies.length(); i++) {
                JSONObject jsonMovieLite = jsonArrayMovies.getJSONObject(i);
                MovieLite movieLite = new MovieLite();
                movieLite.setId(jsonMovieLite.getString("id"));
                movieLite.setTitle(jsonMovieLite.getString("title"));
                movieLite.setCoverArtPath(jsonMovieLite.getString("coverArtPath"));
                movieLite.setMediaType(MediaType.MOVIE);
                movieLites.add(movieLite);
            }

            this.setMovieLites(movieLites);
            this.setTotalRecordsAvailable(totalRecordsAvailable);
            eventBus.post(this);

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public Request getRequest() {
        return new Request(GET_MOVIES_LITE, METHOD_NAME, getParameters(), this);
    }

    /**
     * Getters and setters
     ***/
    public ArrayList<MovieLite> getMovieLites() {
        return movieLites;
    }

    public void setMovieLites(ArrayList<MovieLite> movieLites) {
        this.movieLites = movieLites;
    }

    public int getTotalRecordsAvailable() {
        return totalRecordsAvailable;
    }

    public void setTotalRecordsAvailable(int totalRecordsAvailable) {
        this.totalRecordsAvailable = totalRecordsAvailable;
    }
}
