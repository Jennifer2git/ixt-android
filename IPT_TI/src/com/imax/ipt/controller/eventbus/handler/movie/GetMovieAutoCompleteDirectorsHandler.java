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
import com.imax.ipt.model.MediaType;
import com.imax.ipt.model.Person;
import com.imax.iptevent.EventBus;

public class GetMovieAutoCompleteDirectorsHandler extends Handler {
    public static final String TAG = "GetMovieAutoCompleteDirectorsHandler";
    private ArrayList<Person> persons;
    public static final String METHOD_NAME = "getMovieDirectors";
    private String movieId;
    private String searchStringLastName;
    private String searchStringFirstName;

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
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            JSONArray jsonArrayPersons = result.getJSONArray("persons");
            persons = new ArrayList<Person>(jsonArrayPersons.length());
            Gson gson = new Gson();
            for (int i = 0; i < jsonArrayPersons.length(); i++) {
                JSONObject jsonObjectGenre = jsonArrayPersons.getJSONObject(i);
                Person person = gson.fromJson(jsonObjectGenre.toString(), Person.class);
                person.setMediaType(MediaType.DIRECTOR);
                person.setCoverArtPath("");
                person.setTitle(person.fullName());
                persons.add(person);
            }
            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }


    @Override
    public Request getRequest() {
        return new Request(GET_DIRECTORS_AUTOCOMPLETE, METHOD_NAME, getParameters(), this);
    }

}
