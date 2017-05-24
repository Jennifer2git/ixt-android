package com.imax.ipt.controller.eventbus.handler.push;


public enum PushNotificationEnum {
    selectedInputChanged("selectedInputChanged"),
    nowPlayingChanged("nowPlayingChanged"),
    audioFocusInputIdChanged("audioFocusInputIdChanged"),
    pipModeChanged("pipModeChanged"),
    playingMusicTrackChanged("playingMusicTrackChanged"),
    movieMetadataAvailable("movieMetadataAvailable"),
    musicAlbumMetadataAvailable("musicAlbumMetadataAvailable"),
    mediaLoadProgressChanged("mediaLoadProgressChanged"),
    movieAdded("movieAdded"),
    movieDeleted("movieDeleted"),
    movieStarted("movieStarted"),
    musicTrackAdded("musicTrackAdded"),
    musicTrackDeleted("musicTrackDeleted"),
    audioMuteChanged("audioMuteChanged"),
    audioVolumeChanged("audioVolumeChanged"),
    systemPowerStateChanged("systemPowerStateChanged"),
    systemPowerProgressChanged("systemPowerProgressChanged"),
    powerSocketStateChanged("powerSocketStateChanged"),
    screenShareButtonVisibilityChanged("screenShareButtonVisibilityChanged"),
    maintenanceLoginSessionExpired("maintenanceLoginSessionExpired"),
    projectorLampStateChanged("projectorLampStateChanged"),
    selectedLightingPresetChanged("selectedLightingPresetChanged"),
    lightLevelChanged("lightLevelChanged"),
    curtainStateChanged("curtainStateChanged"),
    setpointTemperatureChanged("setpointTemperatureChanged"),
    actualTemperatureChanged("actualTemperatureChanged"),
    fanStateChanged("fanStateChanged"),
    hvacSystemFaultStateChanged("hvacSystemFaultStateChanged"),
    playingMovieChanged("playingMovieChanged"),
    adjustLightLevel("adjustLightLevel"),
    selectedSecutiyCameraLocationChanged("selectedSecutiyCameraLocationChanged"),
    videoModeChanged("videoModeChanged"),
    screenAspectRatioChanged("screenAspectRatioChanged"),
    systemStatusNotification("systemStatusNotification"),
    requestClientLog("requestClientLog"),

    pcabCalibrationStateChanged("pcabCalibrationStateChanged"),
    upsStateChanged("upsStateChanged"),
    //    setInputName("setInputName"),
    threeDChanged("threeDChanged"),
//    focusAjust("focusAjust"),
    generalNotification("generalNotification");

    private String methodName;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    private PushNotificationEnum(String methodName) {
        this.methodName = methodName;
    }

    public static PushNotificationEnum getPushRequest(String methodName) {
        PushNotificationEnum[] values = values();
        for (int i = 0; i < values.length; i++) {
            String method = values[i].getMethodName();
            if (method.equalsIgnoreCase(methodName)) {
                return values[i];
            }
        }
        throw new UnsupportedOperationException("The following push notification is unsupported :" + methodName);
    }
}
