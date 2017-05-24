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
import com.imax.ipt.model.Actor;
import com.imax.ipt.model.MediaType;
import com.imax.iptevent.EventBus;

public class GetAutoCompleteActorsHandler extends Handler {

    public static final String TAG = "GetMovieActorsResponseEvent";
    public static final String METHOD_NAME = "getMovieActors";

    private String movieId = DEFAULT_GUID;
    private String searchStringLastName;
    private String searchStringFirstName;
    private ArrayList<Actor> mActors;

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(getStartIndex());
        list.add(getCount());
        list.add(getMovieId());
        list.add(getSearchStringLastName());
        list.add(getSearchStringFirstName());
        return list;
    }

    @Override
    public Request getRequest() {
        return new Request(GET_ACTORS_AUTOCOMPLETE, METHOD_NAME, getParameters(), this);
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            JSONArray jsonArrayPersons = result.getJSONArray("persons");
            mActors = new ArrayList<Actor>(jsonArrayPersons.length());
            Gson gson = new Gson();
            for (int i = 0; i < jsonArrayPersons.length(); i++) {
                JSONObject jsonObjectGenre = jsonArrayPersons.getJSONObject(i);
                Actor person = gson.fromJson(jsonObjectGenre.toString(), Actor.class);
                person.setMediaType(MediaType.ACTOR);
                person.setTitle(person.getFirstName() + " " + person.getLastName());
                mActors.add(person);
            }
            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Getters and Setters
     */

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getSearchStringLastName() {
        return searchStringLastName;
    }

    public void setSearchStringLastName(String searchStringLastName) {
        this.searchStringLastName = searchStringLastName;
    }

    public String getSearchStringFirstName() {
        return searchStringFirstName;
    }

    public void setSearchStringFirstName(String searchStringFirstName) {
        this.searchStringFirstName = searchStringFirstName;
    }

    public ArrayList<Actor> getmActors() {
        return mActors;
    }

    public void setmActors(ArrayList<Actor> mActors) {
        this.mActors = mActors;
    }

}
