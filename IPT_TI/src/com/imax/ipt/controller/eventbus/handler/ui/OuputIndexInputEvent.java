package com.imax.ipt.controller.eventbus.handler.ui;

public class OuputIndexInputEvent {
    private int inputId;
    private int outputId;

    public OuputIndexInputEvent(int inputId, int outputId) {
        super();
        this.inputId = inputId;
        this.outputId = outputId;
    }

    public int getInputId() {
        return inputId;
    }

    public void setInputId(int inputId) {
        this.inputId = inputId;
    }

    public int getOutputId() {
        return outputId;
    }

    public void setOutputId(int outputId) {
        this.outputId = outputId;
    }
}
