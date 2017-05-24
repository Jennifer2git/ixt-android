package com.imax.ipt.controller.eventbus.handler.media;

import android.util.Log;
import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.ipt.model.Movie;
import com.imax.iptevent.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoadMediaHandler extends Handler {

    public static final String TAG = "LoadMediaHandler";

    private static final String METHOD_NAME = "loadMedia";


    private Movie movie;
    private int mSelectedMetadataIndex;
    private int stopPlayingMedia;

    private int errorCode;

    public LoadMediaHandler(Movie movie, int mSelectedMetadataIndex, int stopPlayingMedia) {
        super();
        this.movie = movie;
        this.mSelectedMetadataIndex = mSelectedMetadataIndex;
        this.stopPlayingMedia = stopPlayingMedia;
    }

    // LoadMedia(TmsMovie mediaMetadata, int selectedMetadataIndex);
    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(movie);
        list.add(mSelectedMetadataIndex);
        list.add(stopPlayingMedia);
        Log.d(TAG,"call getParameters movie"  );
        return list;
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG,sbResult);
        Log.d(TAG,"call LoadMediaHandler onCreateEvent");

        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            errorCode = result.getInt("intValue");
            Log.d(TAG,"the json result is:" + result);
            eventBus.post(this); // move to here. test:1.repeate 2. set one right data, one error data.
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }


    }

    @Override
    public Request getRequest() {
        return new Request(LOAD_MEDIA, METHOD_NAME, getParameters(), this);
    }

    /**
     * Getters and Setters
     */
    public int getErrorCode() {
        return errorCode;
    }

}
