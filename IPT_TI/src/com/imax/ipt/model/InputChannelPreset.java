package com.imax.ipt.model;

public class InputChannelPreset {
    private int presetId;
    private int channel;
    private String displayName;
    private boolean isSelected;

    public InputChannelPreset() {
        presetId = 0;
        channel = 0;
        displayName = "";
    }

    public InputChannelPreset(InputChannelPreset preset) {
        presetId = preset.getPresetId();
        channel = preset.getChannel();
        displayName = preset.getDisplayName();
    }

    public int getPresetId() {
        return presetId;
    }

    public void setPresetId(int presetId) {
        this.presetId = presetId;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
