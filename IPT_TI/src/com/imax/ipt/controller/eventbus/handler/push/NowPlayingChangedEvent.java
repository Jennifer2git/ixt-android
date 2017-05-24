package com.imax.ipt.controller.eventbus.handler.push;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.imax.ipt.model.DeviceType;
import com.imax.ipt.model.Input;
import com.imax.iptevent.EventBus;

public class NowPlayingChangedEvent extends PushHandler {

    public static final String TAG = "NowPlayingChangedEvent";

    private Input input;

    /**
     * {"id":0,"method":"playingMovieChanged","params":[{"id":
     * "322c28c2-83ca-ae98-f3bc-181572282833"
     * ,"title":"2001: A Space Odyssey","coverArtPath":
     * "http://172.31.10.130:8088/image.png?id=322c28c2-83ca-ae98-f3bc-181572282833&size=Small"
     * ,"loading":false}]}
     */

    @Override
    public void execute(EventBus eventBus, String json) {
        try {
            JSONObject rootObject = new JSONObject(json);
            JSONArray result = rootObject.getJSONArray("params");
            input = new Gson().fromJson(result.getString(0), Input.class);
//         media.setMediaType(MediaType.MOVIE);
            eventBus.postSticky(this);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Getters and Setters
     */
    public Input getInput() {
        return input;
    }
//   public Media getMedia()
//   {
//      return media;
//   }
//
//   public void setMedia(Media media)
//   {
//      this.media = media;
//   }
}
