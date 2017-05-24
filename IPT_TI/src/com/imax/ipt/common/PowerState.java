package com.imax.ipt.common;

/**
 * Created by yanli on 2016/12/8.
 */
public enum PowerState {
    Unknown(0), On(1), PoweringOff(2), Off(3), PoweringOn(4);

    private int value;

    PowerState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static PowerState getPowerState(int value) {
        PowerState[] values = values();
        for (PowerState powerState : values) {
            if (powerState.value == value) return powerState;
        }
        throw new IllegalArgumentException("PowerState does not support");
    }
}
