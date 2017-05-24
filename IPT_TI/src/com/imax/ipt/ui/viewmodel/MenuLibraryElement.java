package com.imax.ipt.ui.viewmodel;

import java.util.List;

import com.imax.ipt.controller.eventbus.handler.ui.MediaMenuLibraryEvent.MenuEvent;

/**
 * @author Rodrigo Lopez
 */
public class MenuLibraryElement {
    private int id;

    private String name;

    private MenuEvent event;

    private List<MenuLibraryElement> subMenu;

    public MenuLibraryElement(int id, String name, List<MenuLibraryElement> subMenu, MenuEvent event) {
        super();
        this.id = id;
        this.name = name;
        this.subMenu = subMenu;
        this.event = event;
    }

    /**
     * @return
     */
    public boolean isHasItems() {
        if (getSubMenus() == null) {
            return false;
        }
        if (getSubMenus().isEmpty()) {
            return false;
        }
        return true;
    }

    public MenuEvent getEvent() {
        return event;
    }

    public void setEvent(MenuEvent event) {
        this.event = event;
    }

    /**
     * @return
     */
    public List<MenuLibraryElement> getSubMenus() {
        return subMenu;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

}