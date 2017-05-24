package com.imax.ipt.controller.eventbus.handler.ui;

import com.imax.ipt.ui.viewmodel.MenuLibraryElement;

public class MediaMenuLibraryEvent {
    private MenuEvent event;
    private MenuLibraryElement menu;

    public static enum MenuEvent {
        TITLES, GENRES_MOVIE, GENRES_MUSIC, ACTORS, DIRECTORS, YEARS,
        FAVOURITES_MUSIC, FAVOURITES_MOVIE, SHOW_GENRES, ARTISTS, ALBUMS,
        SETTINGS, GENERAL, LIGHTING, PREF_FAVOURITES, VERSION, PROJECTOR,
        REBOOT_DEVICES, LOGIN, DEMO, FOCUS;

        public static MenuEvent valueOf(int ordinal) {
            MenuEvent[] events = values();
            int i = 0;
            for (MenuEvent menuEvent : events) {
                if (i == ordinal) {
                    return menuEvent;
                }
                i++;
            }
            throw new IllegalArgumentException("This value is not allowed for ordinal " + ordinal);
        }

    }

    public MediaMenuLibraryEvent(MenuEvent event, MenuLibraryElement menu) {
        super();
        this.event = event;
        this.menu = menu;
    }


    public MenuLibraryElement getMenu() {
        return menu;
    }

    public void setMenu(MenuLibraryElement menu) {
        this.menu = menu;
    }

    public MenuEvent getEvent() {
        return event;
    }

    public void setEvent(MenuEvent event) {
        this.event = event;
    }

}
