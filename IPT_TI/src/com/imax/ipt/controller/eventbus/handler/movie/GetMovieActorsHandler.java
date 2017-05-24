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

public class GetMovieActorsHandler extends Handler {

    public static final String TAG = "GetMovieActorsResponseEvent";
    public static final String METHOD_NAME = "getMovieActors";

    private String movieId = DEFAULT_GUID;
    //   private String searchStringLastName;
//   private String searchStringFirstName;
    private int mTotalRecordsAvailable;

    private ArrayList<Actor> mActors;

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
    public Request getRequest() {
        return new Request(GET_ACTORS, METHOD_NAME, getParameters(), this);
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            JSONArray jsonArrayPersons = result.getJSONArray("persons");

            mActors = new ArrayList<Actor>(jsonArrayPersons.length());

            mTotalRecordsAvailable = result.getInt("totalRecordsAvailable");

            Gson gson = new Gson();
            for (int i = 0; i < jsonArrayPersons.length(); i++) {
                JSONObject jsonObjectPerson = jsonArrayPersons.getJSONObject(i);
                JSONArray jsonCoverPaths = jsonObjectPerson.getJSONArray("coverArtPaths");
                String[] coverPaths = new String[jsonCoverPaths.length()];
                for (int j = 0; j < jsonCoverPaths.length(); j++) {
                    coverPaths[j] = jsonCoverPaths.getString(j);
                }
                Actor person = gson.fromJson(jsonObjectPerson.toString(), Actor.class);
                if (coverPaths != null && coverPaths.length > 0) {
                    person.setCoverArtPaths(coverPaths);
                    person.setCoverArtPath(coverPaths[0]);
                }
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
    public int getTotalRecordsAvailable() {
        return mTotalRecordsAvailable;
    }

    public void setTotalRecordsAvailable(int mTotalRecordsAvailable) {
        this.mTotalRecordsAvailable = mTotalRecordsAvailable;
    }

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

    public ArrayList<Actor> getActors() {
        return mActors;
    }

    public void setmActors(ArrayList<Actor> mActors) {
        this.mActors = mActors;
    }

}
