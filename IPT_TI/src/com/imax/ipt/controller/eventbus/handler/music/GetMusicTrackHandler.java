package com.imax.ipt.controller.eventbus.handler.music;

import java.util.ArrayList;
import java.util.List;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.iptevent.EventBus;


public class GetMusicTrackHandler extends Handler {
    public static String METHOD_NAME = "getMovieLites";


    @Override
    public List<Object> getParameters() {
        List<Object> list = new ArrayList<Object>();
        list.add(getStartIndex());
        list.add(getCount());
        list.add(getGenreId());
        list.add(getActorId());
        list.add(getDirectorId());
        list.add(getYears());
        list.add(getBluray());
        list.add(getFavorite());
        list.add(getImax());
        list.add(getThreeD());
        list.add(getSearchString());
        list.add(getOrderByOptions());
        return list;
    }


    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {

    }


    @Override
    public Request getRequest() {
        return null;
    }
}
