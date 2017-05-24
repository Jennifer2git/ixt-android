package com.imax.ipt.ui.util;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.imax.ipt.R;

public class FactoryDeviceTypeDrawable {

    public static final String TAG = "FDeviceTypeDrawable";


    public enum DeviceKind {
        Movie("Movie"), Music("Music"), Imax("Imax"),OnlineMovie("OnlineMovie"),
        Oppo("Oppo"),Himedia("Himedia"),Kaleidescape("Kaleidescape"),Bestv("Bestv"),
        Game("Game"),Karaoke("Karaoke"), Extender("Extender"),Mymovie("Mymovie"),Gdc("Gdc"),
        Security("Security"),NotSet("NotSet"), ALL("NotSet"),Zaxel("Zaxel") ;

        private String deviceKind = "";

        private DeviceKind(String deviceKind) {
            this.deviceKind = deviceKind;
        }

        public static DeviceKind getDeviceKindEnum(String deviceKind) {
            DeviceKind[] values = values();
            for (int i = 0; i < values.length; i++) {
                String device = values[i].getDeviceKind();
                if (device.equalsIgnoreCase(deviceKind)) {
                    return values[i];
                }
            }
            return NotSet;
        }

        public String getDeviceKind() {
            return deviceKind;
        }

        public void setDeviceKind(String deviceKind) {
            this.deviceKind = deviceKind;
        }
    }

    public static Drawable createDeviceTypeDrawable(Resources resources, DeviceKind deviceKind) {
        switch (deviceKind) {
            case Movie:
                return resources.getDrawable(R.drawable.selector_movie_button);
            case Imax:
                return resources.getDrawable(R.drawable.selector_menu_input_imax_button);
            case Game:
                return resources.getDrawable(R.drawable.selector_gaming_button);
            case Kaleidescape:
            case Himedia:
            case Bestv:
            case Oppo:
                return resources.getDrawable(R.drawable.selector_menu_input_oppo_button);
            case OnlineMovie:
            case Mymovie:
                return resources.getDrawable(R.drawable.selector_menu_input_online_movie_button);
            case NotSet:
                return resources.getDrawable(android.R.color.transparent);
            case Security:
                return resources.getDrawable(R.drawable.selector_security_camera_button);
            case Karaoke:
                return resources.getDrawable(R.drawable.selector_menu_input_karaoke_button);
            case Music:
                return resources.getDrawable(R.drawable.selector_menu_input_music_button);
//            case MediaPlayer:
//                return resources.getDrawable(R.drawable.selector_movie_button);
            case Gdc:
                return resources.getDrawable(R.drawable.selector_menu_input_gdc_button);
            case Extender:
                return resources.getDrawable(R.drawable.selector_menu_input_external_input_button);
            case Zaxel:
                return resources.getDrawable(R.drawable.selector_menu_input_zaxel_button);
            default:
                Log.d(TAG, "Default case Device Kind ");
                break;
        }
        return null;
    }
}
