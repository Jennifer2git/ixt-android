package com.imax.ipt.controller.eventbus.handler.movie;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class GetMovieYearIndexHandler extends Handler {
    public static final String TAG = "GetMovieYearIndexHandler";
    public static String METHOD_NAME = "getMovieYearIndex";

    private int selectedYear;
    private int index;

    public GetMovieYearIndexHandler(int selectedYear) {
        this.selectedYear = selectedYear;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(selectedYear);
        list.add(getGenreId());
        list.add(getActorId());
        list.add(getDirectorId());
        list.add(getYears());
        list.add(getBluray());
        list.add(getFavorite());
        list.add(getImax());
        list.add(getThreeD());
        list.add(getSearchString());
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        try {
            Log.d(TAG, sbResult);
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            index = result.getInt("intValue");

            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public Request getRequest() {
        return new Request(GET_MOVIE_YEAR_INDEX, METHOD_NAME, getParameters(), this);
    }

    public int getIndex() {
        return index;
    }
}
