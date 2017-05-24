package com.imax.ipt.controller.eventbus.handler.music;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;

public class GetMusicAlphaListingHandler extends Handler {
    public static String METHOD_NAME = "getMusicAlbumAlphaListings";

    /**
     * Guid musicArtistId, int musicGenreId, string searchString, int favorite
     */
    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();

        list.add(getMusicArtistId());
        list.add(getGenreId());
        list.add(getSearchString());
        list.add(getFavorite());
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
        return new Request(GET_MUSIC_ALBUM_ALPHA_LISTING, METHOD_NAME, getParameters(), this);
    }

}
