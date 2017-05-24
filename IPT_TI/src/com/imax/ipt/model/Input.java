package com.imax.ipt.model;

import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable.DeviceKind;

public class Input {
    private int id;
    private DeviceKind deviceKind;
    private String displayName;
    private boolean irSupported;
    private boolean active;

    public boolean isIrSupported() {
        return irSupported;
    }

    public void setIrSupported(boolean irSupported) {
        this.irSupported = irSupported;
    }

    public DeviceKind getDeviceKind() {
        return deviceKind;
    }

    public void setDeviceKind(DeviceKind deviceKind) {
        this.deviceKind = deviceKind;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
