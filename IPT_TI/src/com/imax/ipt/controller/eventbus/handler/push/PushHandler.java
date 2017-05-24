package com.imax.ipt.controller.eventbus.handler.push;

import com.imax.iptevent.EventBus;
import org.json.JSONException;

public abstract class PushHandler {
    public abstract void execute(EventBus eventBus, String json) throws JSONException;
}
