package com.imax.ipt.controller.eventbus.handler.ui;

import com.imax.ipt.controller.eventbus.handler.ui.MediaMenuLibraryEvent.MenuEvent;
import com.imax.ipt.model.Media;

public class AutoCompleteEvent {
    //private Media mMedia;
    private String searchString;

    public String getSearchString() {
        return searchString;
    }

    private MenuEvent mMenuEvent;

//   public AutoCompleteEvent(Media mMedia, MenuEvent menuEvent)
//   {
//      super();
//      this.mMedia = mMedia;
//      this.mMenuEvent = menuEvent;
//   }

    public AutoCompleteEvent(String searchString, MenuEvent menuEvent) {
        super();
        this.searchString = searchString;
        this.mMenuEvent = menuEvent;
    }

    public MenuEvent getmMenuEvent() {
        return mMenuEvent;
    }

    public void setmMenuEvent(MenuEvent mMenuEvent) {
        this.mMenuEvent = mMenuEvent;
    }

//   public Media getmMedia()
//   {
//      return mMedia;
//   }
//
//   public void setmMedia(Media mMedia)
//   {
//      this.mMedia = mMedia;
//   }


}
