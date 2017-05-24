package com.imax.ipt.model;

public class TabletStatus {
    private final static byte BYTE_BATTERY_LOW = 0x01;
    private final static byte BYTE_IPT_WIFI_NOT_CONNECTED = 0x02;
    private final static byte BYTE_IPT_SERVER_NOT_CONNECTED = 0x04;

    private boolean batteryLow;
    private boolean iptWifiNotConnected;
    private boolean iptServerNotConnected;

    public TabletStatus() {
    }

    public static TabletStatus newInstance(byte rawByte) {
        TabletStatus status = new TabletStatus();
        status.setBatteryLow((rawByte & BYTE_BATTERY_LOW) == BYTE_BATTERY_LOW);
        status.setIptWifiNotConnected((rawByte & BYTE_IPT_WIFI_NOT_CONNECTED) == BYTE_IPT_WIFI_NOT_CONNECTED);
        status.setIptServerNotConnected((rawByte & BYTE_IPT_SERVER_NOT_CONNECTED) == BYTE_IPT_SERVER_NOT_CONNECTED);

        return status;
    }

    public boolean isBatteryLow() {
        return batteryLow;
    }

    public void setBatteryLow(boolean batteryLow) {
        this.batteryLow = batteryLow;
    }

    public boolean isIptWifiNotConnected() {
        return iptWifiNotConnected;
    }

    public void setIptWifiNotConnected(boolean iptWifiNotConnected) {
        this.iptWifiNotConnected = iptWifiNotConnected;
    }

    public boolean isIptServerNotConnected() {
        return iptServerNotConnected;
    }

    public void setIptServerNotConnected(boolean iptServerNotConnected) {
        this.iptServerNotConnected = iptServerNotConnected;
    }

    public byte toByte() {
        Byte rawByte = (byte) ((batteryLow ? BYTE_BATTERY_LOW : 0) |
                (iptWifiNotConnected ? BYTE_IPT_WIFI_NOT_CONNECTED : 0) |
                (iptServerNotConnected ? BYTE_IPT_SERVER_NOT_CONNECTED : 0));
        return rawByte;
    }
}
