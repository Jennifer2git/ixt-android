package com.imax.ipt.controller.eventbus.handler.movie;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class GetMovieYearDecadesHandler extends Handler {
    public static final String TAG = "GetMovieYearDecadesHandler";
    public static final String METHOD_NAME = "getMovieYearDecades";
    private String[] options;

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
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
        Log.d(TAG, sbResult);
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            JSONArray jsonArrayYears = result.getJSONArray("intArray");

            String[] years = new String[jsonArrayYears.length()];
            for (int i = 0; i < jsonArrayYears.length(); i++) {
                String letter = jsonArrayYears.getString(i);
                years[i] = letter;
            }
            this.setOptions(years);
            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public Request getRequest() {
        return new Request(GET_MOVIE_YEARS, METHOD_NAME, getParameters(), this);
    }

    /**
     * Getters and setters
     ***/
    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

}
