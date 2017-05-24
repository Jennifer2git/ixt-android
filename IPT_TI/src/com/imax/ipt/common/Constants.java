package com.imax.ipt.common;

public final class Constants {
    private Constants() {
    }

    public static final String CONTROL_PC_HOST = "172.31.1.10";
//    public static final String CONTROL_PC_HOST = "172.28.50.19";

    /*
    http://172.29.16.31:8088/
    http://172.28.50.10:8088/

1.      Connect IPT NOC WIFI with password IMAXIPT4u
2.      Remote desktop connect to 172.29.16.31
2.      Remote desktop connect to 10.0.1.101:8088
3.      ID:  ipc\iptadmin
4.      Pwd: bIMAXm4u
git@github.com:mrchenhao/mercafly-android.git
    */

    public static String URL_VERSION = "http://" + CONTROL_PC_HOST + ":8088/PAD/version.xml";
    public static String URL_APK = "http://" + CONTROL_PC_HOST + ":8088/PAD/IPT.apk";

    public static final String FONT_HAIRLINE_PATH ="fonts/Montserrat-Hairline.otf";
    public static final String FONT_LIGHT_PATH ="fonts/Montserrat-Light.otf";
    public static final String FONT_REGULAR_PATH ="fonts/Montserrat-Regular.otf";
    public static final String FONT_BOLD_PATH ="fonts/Montserrat-Bold.otf";
    public static final String FONT_BLACK_PATH ="fonts/Montserrat-Black.otf";

    public static final String IPT_PACKAGE_NAME ="com.imax.ipt";

    public static final String IMAX_PACKAGE_NAME ="com.imax.ihp.external";
    public static final String UPDATE_REMOTE_VERSION_CODE ="remote_version_code";
    public static final String UPDATE_FORCE_UPDATE ="force_update";

    public static final String CONFIG_KEY_LIGHT ="has_light";
    public static final String CONFIG_KEY_ACTIVE_DEVICE ="active_device";

    public static final int PROJECTOR_POSITION_LEFT = 0;
    public static final int PROJECTOR_POSITION_RIGHT = 1;
    /**
     * #1���п�
     * #2��CISCO
     * #3��AMX
     * #4��5��ͶӰ��
     * #6��MOZAEX
     * #7: PRIMA
     * #8: SONY4K
     */
    public static final int POWERSOCKET_MOAZAEX = 6;
    public static final int POWERSOCKET_SONY = 8;
    public static final int POWERSOCKET_HOLLYWOOD = 7;
    public static final int POWERSOCKET_LEFTPROJECTOR = 4;// project1
    public static final int POWERSOCKET_RIGHTPROJECTOR = 5;//project2

    public static final int POWERSTATE_NOTSET = 0;
    public static final int POWERSTATE_ON = 1;
    public static final int POWERSTATE_POWERINGOFF = 2;
    public static final int POWERSTATE_OFF = 3;
    public static final int POWERSTATE_POWERINGON = 4;

//    public static final int MAX_VOLUME_VALUE = 172;// 86-- 6db
    public static final int MAX_VOLUME_VALUE = 86;// 86-- 6db
//    public static final int SCROLL_MAX_VOLUME_VALUE = 148;// 74-- -6db
//    public static final int SCROLL_MAX_VOLUME_VALUE = 142;// 71-- -9db
    public static final int SCROLL_MAX_VOLUME_VALUE = 71;// 71-- -9db
//    public static final int DEFAULT_VOLUME_VALUE = 108;// -- -26db  HOW TO COUNT THIS 0->98 -80->18db.
    public static final int DEFAULT_VOLUME_VALUE = 54;// -- -26db  HOW TO COUNT THIS 0->98 -80->18db.


    public static final int LIGHTING_CONTROL_POWER_ON = 4;// power on
    public static final int LIGHTING_CONTROL_POWER_OFF = 8;// power off
    public static final int LIGHTING_CONTROL_LIGHTNESS_UP = 2;// LIGHTNESS UP
    public static final int LIGHTING_CONTROL_LIGHTNESS_DOWN = 3;// LIGHTNES DOWN
    public static final int LIGHTING_CONTROL_COLOR_WHITE = 1;// COLOR WHITE
    public static final int LIGHTING_CONTROL_COLOR_RED = 5;// COLOR RED
    public static final int LIGHTING_CONTROL_COLOR_GREEN = 6;// COLOR GREEN
    public static final int LIGHTING_CONTROL_COLOR_BLUE = 7;// COLOR BLUE
    public static final int LIGHTING_CONTROL_COLOR_YELLOW = 13;// COLOR YELLOW
    public static final int LIGHTING_CONTROL_FULL_COLOR = 16;// full color ->color gradient
    public static final int LIGHTING_CONTROL_COLOR_SELECTION = 20;//color gradient fix


    public static final int LIGHTING_CONTROL_ON_SCREEN = 27;
    public static final int LIGHTING_CONTROL_ON_TOP = 28;
    public static final int LIGHTING_CONTROL_ON_WALL = 29;
    public static final int LIGHTING_CONTROL_ON_FOOT = 30;

    public static final int LIGHTING_CONTROL_OFF_SCREEN = 35;
    public static final int LIGHTING_CONTROL_OFF_TOP = 36;
    public static final int LIGHTING_CONTROL_OFF_WALL = 37;
    public static final int LIGHTING_CONTROL_OFF_FOOT = 38;

    public static final int LIGHTING_MODE_RESET = 39;
    public static final int LIGHTING_MODE_SAVE = 24;
    public static final int LIGHTING_MODE_SAVE_MOVIE = 34;
    public static final int LIGHTING_MODE_SAVE_KARAOKE = 21;//
    public static final int LIGHTING_MODE_SAVE_GAME = 22;//
    public static final int LIGHTING_MODE_SAVE_PAUSE = 32;//
    public static final int LIGHTING_MODE_SAVE_WELCOME = 31;//
    public static final int LIGHTING_MODE_SAVE_LIGHTING = 23;//

    public static final int LIGHTING_MODE_MOVIE = 61;// Movie mode
    public static final int LIGHTING_MODE_KARAOKE = 62;// Karaoke mode
    public static final int LIGHTING_MODE_GAME = 63;// Game mode
    public static final int LIGHTING_MODE_PAUSE = 64;// Pause mode
    public static final int LIGHTING_MODE_WELCOME = 65;// Welcome mode
    public static final int LIGHTING_MODE_LIGHTING = 66;// Lighting mode


    public static final int DEVICE_ID_MOVIE = 2;
    public static final int DEVICE_ID_MUSIC = 2;
    public static final int DEVICE_ID_IMAX = 3;
    public static final int DEVICE_ID_GAME = 4;
    public static final int DEVICE_ID_ONLINE_MOVIE = 5;
    public static final int DEVICE_ID_KARAOKE = 6;
    public static final int DEVICE_ID_ZAXEL = 7;
    public static final int DEVICE_ID_EXTENDER = 8;
    public static final int DEVICE_ID_OPPO = 9;
    public static final int DEVICE_ID_HIMEDIA = 10;
    public static final int DEVICE_ID_KALEIDESCOPE = 11;
    public static final int DEVICE_ID_BESTV = 12;
    public static final int DEVICE_ID_Mymovie = 13;
    public static final int DEVICE_ID_GDC = 14;

    public static final int DELAY_TIME = 600000;//10mins

}

