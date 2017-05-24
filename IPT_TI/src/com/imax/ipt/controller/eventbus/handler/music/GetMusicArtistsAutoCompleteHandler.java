package com.imax.ipt.controller.eventbus.handler.music;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.ipt.model.Actor;
import com.imax.ipt.model.MediaType;
import com.imax.ipt.model.MusicAlbum;
import com.imax.iptevent.EventBus;

public class GetMusicArtistsAutoCompleteHandler extends Handler {
    public static final String TAG = "GetMusicArtistsHandler";
    public static String METHOD_NAME = "getMusicArtists";
    public int totalRecordsAvailable;
    //   private ArrayList<MusicAlbum> musicAlbums;
    private ArrayList<Actor> persons;

    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(getStartIndex());
        list.add(getCount());
//      list.add(getMusicArtistId());
//      list.add(getGenreId());
        list.add(getSearchString());
        list.add(getSearchString());
//      list.add(getFavorite());
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

//      Log.d(TAG, sbResult);
//      try
//      {
//         JSONObject rootObject = new JSONObject(sbResult);
//         JSONObject result = rootObject.getJSONObject("result");
//         JSONArray jsonArrayMusicAlbums = result.getJSONArray("musicAlbums");
//         int totalRecordsAvailable = result.getInt("totalRecordsAvailable");
//         Gson gson = new Gson();
//         ArrayList<MusicAlbum> musicAlbums = new ArrayList<MusicAlbum>(jsonArrayMusicAlbums.length());
//         for (int i = 0; i < jsonArrayMusicAlbums.length(); i++)
//         {
//            JSONObject jsonMusicAlbum = jsonArrayMusicAlbums.getJSONObject(i);
//            MusicAlbum musicAlbum = gson.fromJson(jsonMusicAlbum.toString(), MusicAlbum.class);
//            musicAlbum.setMediaType(MediaType.ALBUMS);
//            musicAlbum.setTitle(musicAlbum.getName());
//            musicAlbum.setCoverArtPath(UUID.randomUUID().toString());
//            musicAlbums.add(musicAlbum);
//         }
//
//         this.setMusicAlbums(musicAlbums);
//         this.setTotalRecordsAvailable(totalRecordsAvailable);
//         eventBus.post(this);
//
//      } catch (JSONException e)
//      {
//         Log.e(TAG, e.getMessage());
//      }

    }

    @Override
    public Request getRequest() {
        return new Request(GET_MUSIC_ARTIST_AUTO_COMPLETE, METHOD_NAME, getParameters(), this);
    }

    /**
     * Getters and setters
     ***/
    public int getTotalRecordsAvailable() {
        return totalRecordsAvailable;
    }

    public void setTotalRecordsAvailable(int totalRecordsAvailable) {
        this.totalRecordsAvailable = totalRecordsAvailable;
    }

//   public ArrayList<MusicAlbum> getMusicAlbums()
//   {
//      return musicAlbums;
//   }
//
//   public void setMusicAlbums(ArrayList<MusicAlbum> musicAlbums)
//   {
//      this.musicAlbums = musicAlbums;
//   }

    public ArrayList<Actor> getMusicArtists() {
        return persons;
    }
}
