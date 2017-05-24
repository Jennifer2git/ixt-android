package com.imax.ipt.controller.eventbus.handler.push;

import com.imax.iptevent.EventBus;

/**
 * Created by yanli on 2015/10/12.
 */
public class MovieStartedEvent extends PushHandler {
        public static final String TAG = MovieStartedEvent.class.getSimpleName();

        @Override
        public void execute(EventBus eventBus, String json) {
            eventBus.post(this);
        }

}