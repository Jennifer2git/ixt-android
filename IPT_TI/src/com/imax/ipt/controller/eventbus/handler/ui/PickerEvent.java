package com.imax.ipt.controller.eventbus.handler.ui;

public class PickerEvent {
    public PickerEvent(String selection) {
        super();
        this.selection = selection;
    }

    private String selection;

    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

}
