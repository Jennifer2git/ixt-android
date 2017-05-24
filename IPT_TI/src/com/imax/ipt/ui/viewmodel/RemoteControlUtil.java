package com.imax.ipt.ui.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.imax.ipt.model.MusicAlbum;
import com.imax.ipt.model.MusicTrack;
import com.imax.ipt.ui.activity.input.RemoteControlUnavailalbleActivity;
import com.imax.ipt.ui.activity.input.movie.MovieRemoteFullActivity;
import com.imax.ipt.ui.activity.input.music.MusicRemoteActivity;
import com.imax.ipt.ui.activity.input.music.MusicRemoteFullActivity;
import com.imax.ipt.ui.activity.media.PrimaRemoteActivity;
import com.imax.ipt.ui.activity.media.SonyRemoteFullActivity;
import com.imax.ipt.ui.activity.media.ZaxelRemoteFullActivity;
import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable.DeviceKind;

public class RemoteControlUtil {
    public static final String TAG = "RemoteControlUtil";

    public static void openRemoteControl(Context context, int inputId, String title, DeviceKind deviceKind, boolean irSupported) {
        openRemoteControl(context, inputId, null, title, deviceKind, irSupported);
    }

    public static void openRemoteControl(Context context, int inputId, String guid, String title, DeviceKind deviceKind, boolean irSupported) {
        // if external device does not support IR control,
        //    display a screen with only volume control
        Log.d(TAG, "inputId = " + inputId + title);

        if (!irSupported) {
            RemoteControlUnavailalbleActivity.fire(context, inputId, title);
            return;
        }
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
//        boolean isControlComplexityFull = prefs.getBoolean(IRControl.PREFERENCE_DEFAULT_CUSTOM_INPUT_CONTROL_COMPLEXITY_FULL, Boolean.FALSE);
        switch (deviceKind) {
            case Game:
                break;
            case Imax:
                PrimaRemoteActivity.fire(context, null, title, inputId);
                break;
            case Movie:
                MovieRemoteFullActivity.fire(context, guid, title, inputId);//why guid is null?
                break;
            case Music:

                break;
            case OnlineMovie:
                SonyRemoteFullActivity.fire(context, null, title, inputId);

                break;

            case Karaoke:
//                KaraokeActivity.fire(context);
                break;
            case Gdc:
                break;
            case Extender:
//                ExternalInputActivity.fire(context);
                break;
            case Security:
//    	  OppoRemoteFullActivity.fire(context, null, title, inputId);
                break;
            case Zaxel:
                ZaxelRemoteFullActivity.fire(context, title);
                break;
            default:
                Log.d(TAG, "This device is unsupported for input " + deviceKind.getDeviceKind());
                break;
        }
    }

    public static void openMusicRemoteControl(Context context, MusicAlbum musicAlbum, MusicTrack musicTrack) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        boolean isControlComplexityFull = prefs.getBoolean(MusicRemoteActivity.PREFERENCE_DEFAULT_MUSIC_CONTROL_COMPLEXITY_FULL, Boolean.FALSE);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        boolean bMode = sp.getBoolean("default_mode", true);
        isControlComplexityFull = bMode;
        if (isControlComplexityFull) {
            MusicRemoteFullActivity.fire(context, musicAlbum.getId(), musicTrack.getId(), musicTrack.getTitle(),
                    musicTrack.getArtist().fullName(), musicAlbum.getName(), musicAlbum.getCoverArtPath());
        } else {
            MusicRemoteActivity.fire(context, musicAlbum.getId(), musicTrack.getId(), musicTrack.getTitle(),
                    musicTrack.getArtist().fullName(),
                    musicAlbum.getName(), musicAlbum.getCoverArtPath());
        }
    }
}
