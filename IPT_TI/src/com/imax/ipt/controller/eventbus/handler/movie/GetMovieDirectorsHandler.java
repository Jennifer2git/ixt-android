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
import com.imax.ipt.model.Director;
import com.imax.ipt.model.MediaType;
import com.imax.ipt.model.Person;
import com.imax.iptevent.EventBus;

public class GetMovieDirectorsHandler extends Handler {
    public static final String TAG = "GetMovieDirectorsHandler";
    private ArrayList<Person> persons;
    public static final String METHOD_NAME = "getMovieDirectors";
    private String movieId;
    //   private String searchStringLastName;
//   private String searchStringFirstName;
    private int mTotalRecordsAvailable;

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

//   public String getSearchStringLastName()
//   {
//      return searchStringLastName;
//   }
//
//   public void setSearchStringLastName(String searchStringLastName)
//   {
//      this.searchStringLastName = searchStringLastName;
//   }
//
//   public String getSearchStringFirstName()
//   {
//      return searchStringFirstName;
//   }
//
//   public void setSearchStringFirstName(String searchStringFirstName)
//   {
//      this.searchStringFirstName = searchStringFirstName;
//   }

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(getStartIndex());
        list.add(getCount());
        list.add(getMovieId());
        list.add(getSearchString());
        list.add(getSearchString());
//      list.add(getSearchStringLastName());
//      list.add(getSearchStringFirstName());
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            mTotalRecordsAvailable = result.getInt("totalRecordsAvailable");
            JSONArray jsonArrayPersons = result.getJSONArray("persons");
            persons = new ArrayList<Person>(jsonArrayPersons.length());
            Gson gson = new Gson();
            for (int i = 0; i < jsonArrayPersons.length(); i++) {
                JSONObject jsonPerson = jsonArrayPersons.getJSONObject(i);
                JSONArray jsonCoverPaths = jsonPerson.getJSONArray("coverArtPaths");

                String[] coverPaths = new String[jsonCoverPaths.length()];
                for (int j = 0; j < jsonCoverPaths.length(); j++) {
                    coverPaths[j] = jsonCoverPaths.getString(j);
                }
                Director person = gson.fromJson(jsonPerson.toString(), Director.class);
                if (coverPaths != null && coverPaths.length > 0) {
                    person.setCoverArtPaths(coverPaths);
                    person.setCoverArtPath(coverPaths[0]);
                }
                person.setMediaType(MediaType.DIRECTOR);
                person.setTitle(person.getFirstName() + " " + person.getLastName());
                persons.add(person);
            }
            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public int getTotalRecordsAvailable() {
        return mTotalRecordsAvailable;
    }

    public void setmTotalRecordsAvailable(int mTotalRecordsAvailable) {
        this.mTotalRecordsAvailable = mTotalRecordsAvailable;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }

    @Override
    public Request getRequest() {
        return new Request(GET_DIRECTORS, METHOD_NAME, getParameters(), this);
    }

}
