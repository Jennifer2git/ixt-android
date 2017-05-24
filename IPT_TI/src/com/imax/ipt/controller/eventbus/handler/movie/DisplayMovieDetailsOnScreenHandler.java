package com.imax.ipt.controller.eventbus.handler.movie;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class DisplayMovieDetailsOnScreenHandler extends Handler {
    public static final String TAG = "DisplayMovieDetailsOnScreenHandler";
    public static final String METHOD_NAME = "displayMovieDetailsOnScreen";

    private String id;

    public DisplayMovieDetailsOnScreenHandler(String id) {
        super();
        this.id = id;
    }

    /**
     * [JsonRpcMethod("displayMovieBrowseOnScreen", Idempotent = true)]
     * [JsonRpcHelp("Update the theatre screen to display movie selections")]
     * ResultGeneric DisplayMovieBrowseOnScreen(int startIndex, int genreId, Guid
     * actorId, Guid directorId, int[] years, int bluray, int favorite, int imax,
     * int threeD, string searchString, int orderByOptions);
     */
    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(id);
        return list;
    }

    /**
     *
     */
    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
    }

    /**
     *
     */
    @Override
    public Request getRequest() {
        return new Request(DISPLAY_MOVIE_DETAILS_ON_SCREEN, METHOD_NAME, getParameters(), this);
    }
}
