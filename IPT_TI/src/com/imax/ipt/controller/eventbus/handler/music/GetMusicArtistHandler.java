package com.imax.ipt.controller.eventbus.handler.music;

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
import com.imax.ipt.model.Person;
import com.imax.iptevent.EventBus;

public class GetMusicArtistHandler extends Handler {
    public static final String TAG = "GetMovieLiteHandler";
    public static String METHOD_NAME = "getMusicArtists";

    public int totalRecordsAvailable;
    private ArrayList<Actor> persons;
//   private String searchStringLastName;
//   private String searchStringFirstName;


    /**
     * [JsonRpcMethod("getMusicArtists", Idempotent = true)]
     * [JsonRpcHelp("Get music artists available for the filtered criteria")]
     * ResultPersons GetMusicArtists(int startIndex, int count, string
     * searchStringLastName, string searchStringFirstName);
     */

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(getStartIndex());
        list.add(getCount());
        list.add(getSearchString());
        list.add(getSearchString());
//      list.add(getSearchStringFirstName());
//      list.add(getSearchStringLastName());
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            JSONArray jsonArrayPersons = result.getJSONArray("persons");
            totalRecordsAvailable = result.getInt("totalRecordsAvailable");
            persons = new ArrayList<Actor>(jsonArrayPersons.length());
            Gson gson = new Gson();
            for (int i = 0; i < jsonArrayPersons.length(); i++) {
                JSONObject jsonObjectPerson = jsonArrayPersons.getJSONObject(i);
                JSONArray jsonCoverPaths = jsonObjectPerson.getJSONArray("coverArtPaths");
                String[] coverPaths = new String[jsonCoverPaths.length()];
                for (int j = 0; j < jsonCoverPaths.length(); j++) {
                    coverPaths[j] = jsonCoverPaths.getString(j);
                }
                Actor person = gson.fromJson(jsonObjectPerson.toString(), Actor.class);
                person.setMediaType(MediaType.ARTIST);
                if (coverPaths != null && coverPaths.length > 0) {
                    person.setCoverArtPaths(coverPaths);
                    person.setCoverArtPath(coverPaths[0]);
                }
                person.setTitle(person.getFirstName() + " " + person.getLastName());
                persons.add(person);
            }
            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public Request getRequest() {
        return new Request(GET_MUSIC_ARTIST, METHOD_NAME, getParameters(), this);
    }

    public ArrayList<Actor> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<Actor> persons) {
        this.persons = persons;
    }

    public int getTotalRecordsAvailable() {
        return totalRecordsAvailable;
    }

    public void setTotalRecordsAvailable(int totalRecordsAvailable) {
        this.totalRecordsAvailable = totalRecordsAvailable;
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
}
