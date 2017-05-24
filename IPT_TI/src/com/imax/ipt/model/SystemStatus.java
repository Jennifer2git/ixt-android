package com.imax.ipt.model;

public class SystemStatus {
    private int BYTE_POWER_OUTAGE = 0x01;
    private int BYTE_FIRE_ALARM = 0x02;

    private int status;

    public SystemStatus() {

    }

    public SystemStatus(int status) {
        this.status = status;
    }

    public int getStatusValue() {
        return status;
    }

    public boolean isUpsOnBatterySignaled() {
        return ((status & BYTE_POWER_OUTAGE) == BYTE_POWER_OUTAGE);
    }

    public boolean isFireAlarmSignaled() {
        return ((status & BYTE_FIRE_ALARM) == BYTE_FIRE_ALARM);
    }
}
