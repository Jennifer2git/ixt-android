package com.imax.ipt.controller.eventbus.handler.push;

public class SystemPowerProgressEvent {
    private int percentCompleted;
    private int estimatedSecondsRemaining;
    private int errorCode;

    public SystemPowerProgressEvent(int percentCompleted, int estimatedSecondsRemaining, int errorCode) {
        super();
        this.percentCompleted = percentCompleted;
        this.estimatedSecondsRemaining = estimatedSecondsRemaining;
        this.errorCode = errorCode;
    }

    public int getPercentCompleted() {
        return percentCompleted;
    }

    public void setPercentCompleted(int percentCompleted) {
        this.percentCompleted = percentCompleted;
    }

    public int getEstimatedSecondsRemaining() {
        return estimatedSecondsRemaining;
    }

    public void setEstimatedSecondsRemaining(int estimatedSecondsRemaining) {
        this.estimatedSecondsRemaining = estimatedSecondsRemaining;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }


}
