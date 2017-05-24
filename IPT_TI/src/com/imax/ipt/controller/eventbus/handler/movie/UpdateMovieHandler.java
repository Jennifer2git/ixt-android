package com.imax.ipt.controller.eventbus.handler.movie;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.ipt.model.Movie;
import com.imax.iptevent.EventBus;

public class UpdateMovieHandler extends Handler {

    public static String TAG = "UpdateMovieHandler";
    private static final String METHOD_NAME = "updateMovie";

    private Movie mMovie = new Movie();

    public UpdateMovieHandler(Movie mMovie) {
        super();
        this.mMovie = mMovie;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
//      list.add(getMovie());

        list.add(mMovie.getId());
        list.add(mMovie.isFavorite());
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        eventBus.post(this);
    }

    @Override
    public Request getRequest() {
        return new Request(UPDATE_MOVIE, METHOD_NAME, getParameters(), this);
    }

    /**
     * Getters and Setters
     *
     * @return
     */
    public Movie getMovie() {
        return mMovie;
    }

    public void setMovie(Movie mMovie) {
        this.mMovie = mMovie;
    }

}
