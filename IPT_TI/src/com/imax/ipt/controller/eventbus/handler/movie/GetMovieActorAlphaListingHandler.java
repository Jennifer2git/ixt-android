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

public class GetMovieActorAlphaListingHandler extends Handler {
    public static final String TAG = "GetMovieActorAlphaListingHandler";
    public static String METHOD_NAME = "getMovieActorAlphaListings";

    private String searchString;

    public GetMovieActorAlphaListingHandler(String searchString) {
        this.searchString = searchString;
    }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();

        list.add(DEFAULT_GUID);
        list.add(searchString);
        list.add(searchString);

//      list.add(getGenreId());
//      list.add(getActorId());
//      list.add(getDirectorId());
//      list.add(getYears());
//      list.add(getBluray());
//      list.add(getFavorite());
//      list.add(getImax());
//      list.add(getThreeD());
//      list.add(getSearchString());
        return list;
    }

    private String[] options;

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            JSONArray jsonArrayLetters = result.getJSONArray("alphaListings");

            String[] letters = new String[jsonArrayLetters.length()];
            for (int i = 0; i < jsonArrayLetters.length(); i++) {
                String letter = jsonArrayLetters.getString(i);
                letters[i] = letter;
            }
            this.setOptions(letters);
            eventBus.post(this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Request getRequest() {
        return new Request(GET_MOVIE_ACTOR_ALPHA_LISTING, METHOD_NAME, getParameters(), this);
    }

}
