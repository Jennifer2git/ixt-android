package com.imax.ipt.controller.eventbus.handler.push;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.imax.ipt.model.Movie;
import com.imax.iptevent.EventBus;

public class MovieMetadataAvailableEvent extends PushHandler {

    public static final String TAG = "MovieMetadataAvailableEvent";
    private List<Movie> mMoviesMeta = new ArrayList<Movie>();

    @Override
    public void execute(EventBus eventBus, String json) {
        Log.d(TAG, json);
        try {
            JSONObject rootObject = new JSONObject(json);
            JSONArray params = rootObject.getJSONArray("params");

            JSONArray metadatas = params.getJSONArray(0);
            if (metadatas.isNull(0) || (metadatas.length() == 0)) {
                mMoviesMeta.add(new Movie());
            } else {
                for (int i = 0; i < metadatas.length(); i++) {
                    JSONObject jsonObjectMovie = metadatas.getJSONObject(i);
                    String mov = jsonObjectMovie.toString();
                    Movie movie = new Gson().fromJson(mov, Movie.class);
                    mMoviesMeta.add(movie);
                }
            }


//         JSONArray result = rootObject.getJSONArray("params");
//
//         if (result.length() == 0 || result.isNull(0))
//         {
//            
//         }
//         else
//         {
//            for (int i = 0; i < result.length(); i++)
//            {
//               JSONObject jsonObjectMovie = result.getJSONObject(i);
//               String mov = jsonObjectMovie.toString();
//               Movie movie = new Gson().fromJson(mov, Movie.class);
//               mMoviesMeta.add(movie);
//            }
//         }
            this.setmMoviesMeta(mMoviesMeta);
            eventBus.post(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Getters and Setters
     */

    public List<Movie> getmMoviesMeta() {
        return mMoviesMeta;
    }

    public void setmMoviesMeta(List<Movie> mMoviesMeta) {
        this.mMoviesMeta = mMoviesMeta;
    }

}
