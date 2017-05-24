package com.imax.ipt.controller.eventbus.handler.push;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.iptevent.EventBus;

public class MusicAlbumMetadataAvailableEvent extends PushHandler {

    public static final String TAG = "MusicAlbumMetadataAvailableEvent";

    private String[] albumTitles;
    private String[] coverArtPaths;

    @Override
    public void execute(EventBus eventBus, String json) {


        Log.d(TAG, json);
        try {
            JSONObject rootObject = new JSONObject(json);
            JSONArray params = rootObject.getJSONArray("params");

            JSONArray albumTitleJsonArray = params.getJSONArray(0);
            JSONArray coverArtPathJsonArray = params.getJSONArray(1);


            if (albumTitleJsonArray.isNull(0) || (albumTitleJsonArray.length() == 0)) {
                albumTitles = new String[0];
                coverArtPaths = new String[0];
            } else {
                albumTitles = new String[albumTitleJsonArray.length()];
                coverArtPaths = new String[coverArtPathJsonArray.length()];

                for (int i = 0; i < albumTitleJsonArray.length(); i++) {
                    albumTitles[i] = albumTitleJsonArray.getString(i);
                    coverArtPaths[i] = coverArtPathJsonArray.getString(i);
//               JSONObject jsonObjectMovie = albumTitleJsonArray.getJSONObject(i);
//               String mov = jsonObjectMovie.toString();
//               Movie movie = new Gson().fromJson(mov, Movie.class);
//               mMoviesMeta.add(movie);
                }
            }

            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Getters and Setters
     */
    public String[] getAlbumTitles() {
        return albumTitles;
    }

    public String[] getCoverArtPaths() {
        return coverArtPaths;
    }
}
