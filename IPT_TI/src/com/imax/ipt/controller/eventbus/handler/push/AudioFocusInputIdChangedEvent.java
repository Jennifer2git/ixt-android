package com.imax.ipt.controller.eventbus.handler.push;

import com.imax.iptevent.EventBus;

public class AudioFocusInputIdChangedEvent extends PushHandler {

    @Override
    public void execute(EventBus eventBus, String json) {
        eventBus.post(this);
    }

}
