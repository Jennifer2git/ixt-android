package com.imax.ipt.controller.eventbus.handler.push;

import android.util.Log;

import com.imax.iptevent.EventBus;

public class MusicTrackAddedEvent extends PushHandler {
    private static final String TAG = "MusicTrackAddedEvent";

    @Override
    public void execute(EventBus eventBus, String json) {
        Log.d(TAG, "new track added");
        eventBus.post(this);
    }

}
