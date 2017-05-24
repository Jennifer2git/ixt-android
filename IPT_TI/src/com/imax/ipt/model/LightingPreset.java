package com.imax.ipt.model;

import android.view.KeyEvent.DispatcherState;

public class LightingPreset {
    private int id;
    private String displayName;

    public LightingPreset(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
