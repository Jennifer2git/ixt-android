package com.imax.ipt.controller.eventbus.handler;

import com.imax.ipt.conector.api.Request;
import com.imax.iptevent.EventBus;

import java.util.List;

public abstract class Handler {
    public static final int DEFAULT_INT_VALUE = 0;

    public static final String DEFAULT_GUID = "00000000-0000-0000-0000-000000000000";

    public static final int GET_MOVIES_LITE = 1;

    public static final int GET_ALPHA_LISTING = 2;

    public static final int GET_MOVIE = 3;

    public static final int PLAY_MOVIE = 4;

    public static final int GET_GENRES = 5;

    public static final int GET_VOLUME = 6;

    public static final int SET_VOLUME = 7;

    public static final int GET_MUTE = 8;

    public static final int SET_MUTE = 9;

    public static final int GET_DEVICES_TYPES = 10;

    public static final int GET_PIP_MODE = 11;

    public static final int SET_PIP_MODE = 12;

    public static final int GET_AUDIO_FOCUS = 13;

    public static final int SET_AUDIO_FOCUS = 14;

    public static final int GET_SELECTED_INPUT = 15;

    public static final int SET_SELECTED_INPUT = 16;

    public static final int GET_INPUT_BY_KIND = 17;

    public static final int SET_NOW_PLAYING = 18;

    public static final int GET_ACTORS = 20;

    public static final int GET_DIRECTORS = 21;

    public static final int GET_FAVOURITES = 22;

    public static final int GET_MOVIE_YEARS = 23;

    public static final int EXECUTE_REMOTE = 24;

    public static final int GET_MUSIC_ALBUMS = 25;

    public static final int GET_MUSIC_ALBUM = 26;

    public static final int PLAY_TRACK = 27;

    // Power Commands
    public static final int GET_SYSTEM_POWER_STATE = 28;

    public static final int SWITCH_SYSTEM_POWER = 29;

    public static final int SWITCH_POWER_SOCKET = 30;

    // MEDIA
    public static final int DISPLAY_SCREEN_SHARE = 31;

    public static final int IR_PULSE = 32;

    public static final int GET_MUSIC_ARTIST = 34;

    public static final int UPDATE_MOVIE = 36;

    // MOVIE
    public static final int GET_PLAYING_MOVIE = 37;

    // CURTAINS
    public static final int GET_STATE_CURTAINS = 33;
    public static final int MOVE_CURTAINS = 35;
    public static final int IS_CURTAIN_AVALAIBLE = 38;

    // LIGTHING
    public static final int GET_LIGHTING_PRESET = 39;
    public static final int SELECT_LIGHTING_PRESET = 40;
    public static final int ADJUST_LIGHT_LEVEL = 41;

    // Movie Library
    public static final int GET_ALPHA_MOVIE_LISTING_INDEX = 44;
    public static final int GET_ALPHA_MOVIE_ACTOR_LISTING_INDEX = 45;

    public static final int GET_ACTORS_AUTOCOMPLETE = 46;
    public static final int GET_DIRECTORS_AUTOCOMPLETE = 47;

    // Theatre
    public static final int DISPLAY_SCREEN_SHARE_BUTTON = 48;
    public static final int SCREEN_SHARE_BUTTON = 49;
    public static final int DISPLAY_MOVIE_BROWSE_ON_SCREEN = 50;
    public static final int DISPLAY_MOVIE_DETAILS_ON_SCREEN = 57;


    // Maintenance
    public static final int MAINTENANCE_LOGIN = 51;

    // Climate
    public static final int GET_HVAC_STATES = 42;
    public static final int ADJUST_CURRENT_SET_POINT = 43;
    public static final int SET_FAN_STATE = 52;

    //Preferences Lighting
    public static final int GET_LIGHTING_EVENTS = 53;

    //Autocomplete

    public static final int GET_MOVIES_LITE_AUTOCOMPLETE = 54;
    public static final int GET_MUSIC_ALBUMS_AUTOCOMPLETE = 55;

    //Music
    public static final int GET_ALPHA_MUSIC_LISTING_INDEX = 56;

    public static final int GET_ALPHA_MOVIE_DIRECTOR_LISTING_INDEX = 58;

    //Media Loading
    public static final int LOAD_MEDIA = 59;

    //Registration
    public static final int REGISTER_MEDIA_PROGRESS = 60;

    //getSecurityCameraLocations
    public static final int GET_SECURITY_CAM_LOCATION = 61;

    public static final int SET_VIDEO_MODE = 101;
    public static final int GET_VIDEO_MODE = 102;
    public static final int GET_MOVIE_ACTOR_ALPHA_LISTING = 103;
    public static final int GET_MUSIC_ALBUM_ALPHA_LISTING = 104;
    public static final int GET_MUSIC_ARTIST_ALPHA_LISTING = 105;
    public static final int GET_MUSIC_ARTIST_ALPHA_INDEX = 106;
    public static final int DISPLAY_ACTOR_BROWSE_ON_SCREEN = 107;
    public static final int DISPLAY_DIRECTOR_BROWSE_ON_SCREEN = 108;
    public static final int SET_SCREEN_ASPECT_RATIO = 109;
    public static final int GET_SCREEN_ASPECT_RATIO = 110;
    public static final int GET_MUSIC_ARTIST_AUTO_COMPLETE = 111;
    public static final int SET_MUSIC_ALBUM_FAVORITE_STATUS = 112;
    public static final int GET_MOVIE_YEAR_INDEX = 113;
    public static final int GET_MOVIE_DIRECTOR_ALPHA_LISTING = 114;
    public static final int GET_LIGHT_LEVEL = 115;
    public static final int SELECT_SECURITY_CAMERA_LOCATION = 116;
    public static final int DISPLAY_MUSIC_ALBUM_BROWSE_ON_SCREEN = 117;
    public static final int DISPLAY_MUSIC_ARTIST_BROWSE_ON_SCREEN = 118;
    public static final int DISPLAY_MUSIC_ALBUM_DETAILS_ON_SCREEN = 119;
    public static final int GET_NOW_PLAYING_INPUT = 120;
    public static final int GET_PLAYING_MUSIC_TRACK = 121;
    public static final int CLEAR_FAVORITES = 122;
    public static final int GET_POWER_SOCKET_STATE = 123;
    public static final int GET_INPUT_CHANNEL_PRESETS_FOR_INPUT = 124;
    public static final int IR_CHANNEL = 125;
    public static final int EDIT_INPUT_CHANNEL_PRESET = 126;
    public static final int ADD_INPUT_CHANNEL_PRESET = 127;
    public static final int DELETE_INPUT_CHANNEL_PRESET = 128;
    public static final int PING = 129;
    public static final int UPDATE = 214;
    public static final int GET_SYSTEM_POWER_PROGRESS = 130;
    public static final int GET_INPUT_BY_KIND_FOR_IPT_SERVICE_ONLY = 131;
    public static final int SEND_COMPRESSED_LOG = 132;

    public static final int SWITCH_PROJECTOR_LAMP = 150;
    public static final int GET_PROJECTOR_LAMP_STATUS = 151;
    public static final int REBOOT_POWER_SOCKET = 152;

    //added by water
    public static final int GET_PCAB_CALIBRATION_STATUS = 201;
    public static final int SWITCH_PCAB_CALIBRATION = 202;
    public static final int SET_ZOOM_MODE = 203;
    public static final int GET_ZOOM_MODE = 204;

    public static final int PLAY_ALBUM = 205;
    public static final int UPDATE_MOVIE_FAVORITE = 206;

    public static final int RESET_PROJECTOR_LAMP_TIME = 207;//����ͶӰ�ǵƹ�ʱ��
    public static final int GET_VERSION = 208;//����ͶӰ�ǵƹ�ʱ��

    public static final int CONTROL_LIGHTING = 209;//���Ƶƹ�����

    public static final int GET_ZAXEL = 210;//set zaxel
    public static final int SET_LANGUAGE = 211;//��������

    public static final int AUTO_POWER_CONTROL = 212;//�Զ����Ƶ�Դ

    public static final int SET_LIGHTING_DELAY_TIME = 213;//set lighting delay time cmd.
    public static final int GET_HAS_LIGHT = 214;//set lighting delay time cmd.
    public static final int SET_INPUT_NAME = 216;//set display name of input divice.
    public static final int CONTROL_FOCUS = 215;//set display name of input divice.

    public abstract List<Object> getParameters();

    private Integer startIndex = DEFAULT_INT_VALUE;
    private Integer count = DEFAULT_INT_VALUE;
    private Integer genreId = DEFAULT_INT_VALUE;
    private String actorId = DEFAULT_GUID;
    private String directorId = DEFAULT_GUID;
    private Integer[] years = {};
    private Integer bluray = DEFAULT_INT_VALUE;
    private Integer favorite = DEFAULT_INT_VALUE;
    private Integer imax = DEFAULT_INT_VALUE;
    private Integer threeD = DEFAULT_INT_VALUE;
    private String searchString = "";
    private Integer orderByOptions = DEFAULT_INT_VALUE;
    private String musicArtistId = DEFAULT_GUID;

    private boolean isAutoComplete = false;

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getGenreId() {
        return genreId;
    }

    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public String getDirectorId() {
        return directorId;
    }

    public void setDirectorId(String directorId) {
        this.directorId = directorId;
    }

    public Integer[] getYears() {
        return years;
    }

    public void setYears(Integer[] years) {

        this.years = years;

    }

    public Integer getBluray() {
        return bluray;
    }

    public void setBluray(Integer bluray) {
        this.bluray = bluray;
    }

    public Integer getFavorite() {
        return favorite;
    }

    public void setFavorite(Integer favorite) {
        this.favorite = favorite;
    }

    public Integer getImax() {
        return imax;
    }

    public void setImax(Integer imax) {
        this.imax = imax;
    }

    public Integer getThreeD() {
        return threeD;
    }

    public void setThreeD(Integer threeD) {
        this.threeD = threeD;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public Integer getOrderByOptions() {
        return orderByOptions;
    }

    public void setOrderByOptions(int orderByOptions) {
        this.orderByOptions = orderByOptions;
    }

    public String getMusicArtistId() {
        return musicArtistId;
    }

    public void setMusicArtistId(String musicArtistId) {
        this.musicArtistId = musicArtistId;
    }

    public boolean isAutoComplete() {
        return isAutoComplete;
    }

    public void setAutoComplete(boolean isAutcomplete) {
        this.isAutoComplete = isAutcomplete;
    }

    public abstract void onCreateEvent(EventBus eventBus, String sbResult);

    public abstract Request getRequest();

}
