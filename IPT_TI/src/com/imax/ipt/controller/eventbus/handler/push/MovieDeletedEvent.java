package com.imax.ipt.controller.eventbus.handler.push;

import com.imax.iptevent.EventBus;

public class MovieDeletedEvent extends PushHandler {
    public static final String TAG = "MovieDeletedEvent";
    public void execute(EventBus eventBus, String json)
    {
        eventBus.post(this);
    }
}
