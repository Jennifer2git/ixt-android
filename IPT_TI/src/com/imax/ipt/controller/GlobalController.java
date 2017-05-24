package com.imax.ipt.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import com.imax.ipt.IPT;
import com.imax.ipt.common.Constants;
import com.imax.ipt.common.PowerState;
import com.imax.ipt.controller.eventbus.handler.input.*;
import com.imax.ipt.controller.eventbus.handler.media.GetZaxelHandler;
import com.imax.ipt.controller.eventbus.handler.media.SetLanguageHandler;
import com.imax.ipt.controller.eventbus.handler.movie.GetPlayingMovieHandler;
import com.imax.ipt.controller.eventbus.handler.music.GetPlayingMusicTrackHandler;
import com.imax.ipt.controller.eventbus.handler.power.GetSystemPowerStateHandler;
import com.imax.ipt.controller.eventbus.handler.push.NowPlayingChangedEvent;
import com.imax.ipt.controller.eventbus.handler.push.PlayingMovieChangedEvent;
import com.imax.ipt.controller.eventbus.handler.push.PlayingMusicTrackChangedEvent;
import com.imax.ipt.controller.eventbus.handler.remote.SetMuteHandler;
import com.imax.ipt.controller.eventbus.handler.remote.SetVolumenHandler;
import com.imax.ipt.controller.eventbus.handler.rooms.ControlLightingHandler;
import com.imax.ipt.model.DeviceType;
import com.imax.ipt.model.Input;
import com.imax.ipt.model.Media;
import com.imax.ipt.model.MusicAlbum;
import com.imax.ipt.service.ConnecteService;
import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable.DeviceKind;
import com.imax.ipt.ui.util.VibrateUtil;
import com.imax.iptevent.EventBus;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalController {

    private static final String TAG = GlobalController.class.getSimpleName();
    private static GlobalController instance = new GlobalController();

    public static final int Language_CN = 0;//0-cn 1-en
    public static final int Language_EN = 1;//0-cn 1-en
    public static int systemLanguage;//0-cn 1-en

    private EventBus mEventBus;
    private Input mNowPlaying;
    private MusicAlbum mCurrentMusicTrack;
    private Media mCurrentMovieMedia;
    private Context mContext;

    private boolean mHasLighting = false;

    private GlobalController() {
        mEventBus = IPT.getInstance().getEventBus();
        mContext = IPT.getInstance().getApplicationContext();
    }

    public static GlobalController getInstance() {
        return instance;
    }

    public void serverConnected() {
        IPT.setConnect2Server(true);
        mEventBus.register(this);
        setSystemLanguage();
        getActiveDeviceByLanguage();
        mEventBus.post(new GetHasLightHandler().getRequest());

    }

    public void setSystemLanguage() {
        if (IPT.getInstance().getResources().getConfiguration().locale.getCountry().equals("CN")) {
            mEventBus.post(new SetLanguageHandler(Language_CN).getRequest());
        } else {
            mEventBus.post(new SetLanguageHandler(Language_EN).getRequest());
        }
    }

    public void getActiveDeviceByLanguage() {
        if (IPT.getInstance().getResources().getConfiguration().locale.getCountry().equals("CN")) {
            getActiveDeviceByLanguage(Language_CN);
        } else {
            getActiveDeviceByLanguage(Language_EN);
        }
    }

    private void getActiveDeviceByLanguage(int type) {
        mEventBus.post(new GetActiveDeviceTypesByLanguageHandler(type).getRequest());
    }

    public void serverDisconnected() {
        mEventBus.unregister(this);
        // clear cache
        mNowPlaying = null;
        mCurrentMusicTrack = null;
        inputsByDeviceKind.clear();
        IPT.setConnect2Server(false);
    }

    public boolean getHasLighting() {
        mHasLighting = mContext.getSharedPreferences("config", Context.MODE_PRIVATE)
                .getBoolean("hasLighting", false);
        return mHasLighting;
    }

    public void setLightsOn() {
        mEventBus.post(new ControlLightingHandler(Constants.LIGHTING_CONTROL_POWER_ON).getRequest());
        VibrateUtil.vibrate(mContext, 100);
    }

    public void setLightsOff() {
        mEventBus.post(new ControlLightingHandler(Constants.LIGHTING_CONTROL_POWER_OFF).getRequest());
        VibrateUtil.vibrate(mContext, 100);
    }

    public void set2DMode() {
        mEventBus.post(new GetZaxelHandler(101, 0, "").getRequest());// use GetZaxelHandler 101->2d 102->3d
    }

    public void set3DMode() {
        mEventBus.post(new GetZaxelHandler(102, 0, "").getRequest());// use GetZaxelHandler 101->2d 102->3d
    }


    public void onEvent(GetHasLightHandler getHasLightHandler) {
        mHasLighting = getHasLightHandler.getHasLighting();
        setConfig2Preference(Constants.CONFIG_KEY_LIGHT, mHasLighting);
    }

    public Map<DeviceKind, Vector<Input>> inputsByDeviceKind = new ConcurrentHashMap<DeviceKind, Vector<Input>>();

    public void onEvent(GetActiveDeviceTypesByLanguageHandler getActiveDeviceTypesByLanguageHandler) {
        Vector<DeviceType> deviceTypes = new Vector<DeviceType>();
        if (getActiveDeviceTypesByLanguageHandler.getDeviceTypes() == null) {
            Log.e(TAG, "get active device type error!");
            return;
        }

        inputsByDeviceKind.clear();
        DeviceType[] activeDeviceTypes = getActiveDeviceTypesByLanguageHandler.getDeviceTypes();
        for (int i = activeDeviceTypes.length - 1; i >= 0; i--) {
            if (activeDeviceTypes[i] != null) {
                deviceTypes.add(activeDeviceTypes[i]);
                inputsByDeviceKind.put(activeDeviceTypes[i].getDeviceKind(), new Vector<Input>());
            }
        }
        getSystemPowerState();
        setConfig2Preference(Constants.CONFIG_KEY_ACTIVE_DEVICE, deviceTypes);
        this.mEventBus.post(new GetInputsByDeviceKindHandler_ForIptServiceOnly(DeviceKind.ALL.getDeviceKind()).getRequest());
    }

    // note replace this with ByLanguage.
//    public void onEvent(GetActiveDeviceTypesHandler getActiveDeviceTypesHandler) {
//        Vector<DeviceType> deviceTypes = new Vector<DeviceType>();
//        if (getActiveDeviceTypesHandler.getDeviceTypes() == null) {
//            Log.e(TAG, "get active device type error!");
//            return;
//        }
//        inputsByDeviceKind.clear();
//        DeviceType[] activeDeviceTypes = getActiveDeviceTypesHandler.getDeviceTypes();
//        for (int i = activeDeviceTypes.length - 1; i >= 0; i--) {
//            if (activeDeviceTypes[i] != null) {
//                deviceTypes.add(activeDeviceTypes[i]);
//                inputsByDeviceKind.put(activeDeviceTypes[i].getDeviceKind(), new Vector<Input>());
//            }
//        }
//
//        getSystemPowerState();
//        setConfig2Preference(Constants.CONFIG_KEY_ACTIVE_DEVICE, deviceTypes);
//        this.mEventBus.post(new GetInputsByDeviceKindHandler_ForIptServiceOnly(DeviceKind.ALL.getDeviceKind()).getRequest());
//
//    }


    private void setConfig2Preference(String key, Object object) {
        SharedPreferences sp = mContext.getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (key.equals(Constants.CONFIG_KEY_LIGHT)) {
            editor.putBoolean(key, (Boolean) object);
        } else if (key.equals(Constants.CONFIG_KEY_ACTIVE_DEVICE)) {
            if (sp.contains(key)) {
                editor.remove(key);
                editor.commit();
            }
            editor.putString(key, transfer2Jason(object));
        }
        editor.commit();
    }

    private String transfer2Jason(Object deviceTypes) {
        Gson gson = new Gson();
        String strJason = gson.toJson(deviceTypes);
        return strJason;
    }

    public Object readConfigFromPreference(String key) {
        SharedPreferences sp = mContext.getSharedPreferences("config", Context.MODE_PRIVATE);

        if (key.equals(Constants.CONFIG_KEY_LIGHT)) {
            return sp.getBoolean(key, false);
        } else if (key.equals(Constants.CONFIG_KEY_ACTIVE_DEVICE)) {
            return getDevicesFromGson(sp.getString(key, null));
        } else {
            return null;
        }
    }

    private Object getDevicesFromGson(String strJson) {
        Vector<DeviceType> deviceTypes = new Vector<>();
        if (strJson == null) {
            return null;
        }
        try {
            JSONArray jsonArrayDeviceTypes = new JSONArray(strJson);
            Gson gson = new Gson();
            for (int i = 0; i < jsonArrayDeviceTypes.length(); i++) {
                DeviceType deviceType = gson.fromJson(jsonArrayDeviceTypes.get(i).toString(), DeviceType.class);
                if (deviceType.getDeviceKind() != null) {
                    deviceTypes.add(i, deviceType);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return deviceTypes;
    }

    public void onEvent(final GetInputsByDeviceKindHandler_ForIptServiceOnly getInputsByDeviceKindHandler) {
        Input[] allInputs = getInputsByDeviceKindHandler.getInputs();
        for (Input input : allInputs) {
            if (inputsByDeviceKind.containsKey(input.getDeviceKind())) {
                inputsByDeviceKind.get(input.getDeviceKind()).add(input);
            }
        }
        IPT.getInstance().getIPTContext().put(IPT.INPUTS_BY_DEVICE_KIND, inputsByDeviceKind);
    }

    private void onEvent(GetSystemPowerStateHandler getSystemPowerStateHandler) {
        this.switchPowerState(getSystemPowerStateHandler.getPowerState());
    }

    private void getSystemPowerState() {
        GetSystemPowerStateHandler getSystemPowerStateHandler = new GetSystemPowerStateHandler();
        this.mEventBus.post(getSystemPowerStateHandler.getRequest());
    }


    public void setVolume(int progress) {
        mEventBus.post(new SetVolumenHandler(progress).getRequest());
    }

    public void setMute(Boolean mute) {
        mEventBus.post(new SetMuteHandler(mute).getRequest());
        VibrateUtil.vibrate(mContext, 100);
    }

    public void onEventMainThread(GetNowPlayingInputHandler handler) {
        mNowPlaying = handler.getNowPlayingInput();
    }

    public void onEventMainThread(NowPlayingChangedEvent handler) {
        mNowPlaying = handler.getInput();
        if (mNowPlaying.getDeviceKind() == DeviceKind.NotSet) {
            // playing movie / music has stopped
            mCurrentMovieMedia = null;
            mCurrentMusicTrack = null;
        }
    }

    public void onEventMainThread(GetPlayingMovieHandler handler) {
        mCurrentMovieMedia = handler.getMovieLite();
    }

    public void onEventMainThread(GetPlayingMusicTrackHandler handler) {
        mCurrentMusicTrack = handler.getmMusicAlbum();
    }

    public void onEventMainThread(PlayingMovieChangedEvent handler) {
        mCurrentMovieMedia = handler.getMedia();
    }

    public void onEventMainThread(PlayingMusicTrackChangedEvent handler) {
        mCurrentMusicTrack = handler.getmMusicAlbum();
    }

    public Input getNowPlaying() {
        return mNowPlaying;
    }

    public MusicAlbum getCurrentMusicTrack() {
        return mCurrentMusicTrack;
    }

    public Media getCurrentMovieMedia() {
        return mCurrentMovieMedia;
    }

    /**
     * @param powerState
     */
    private void switchPowerState(PowerState powerState) {
        Intent iptSystemPowerStateChangedIntent;

        switch (powerState) {
            case On:
                iptSystemPowerStateChangedIntent = new Intent(ConnecteService.BROADCAST_ACTION_SYSTEM_POWER_ON);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(iptSystemPowerStateChangedIntent);
                break;
            case Off:
                iptSystemPowerStateChangedIntent = new Intent(ConnecteService.BROADCAST_ACTION_SYSTEM_POWER_OFF);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(iptSystemPowerStateChangedIntent);
                break;
            case PoweringOff:
                iptSystemPowerStateChangedIntent = new Intent(ConnecteService.BROADCAST_ACTION_SYSTEM_POWERING_OFF);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(iptSystemPowerStateChangedIntent);
                break;
            case PoweringOn:
                iptSystemPowerStateChangedIntent = new Intent(ConnecteService.BROADCAST_ACTION_SYSTEM_POWERING_ON);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(iptSystemPowerStateChangedIntent);
            case Unknown:
                // TODO how to response unknown
                Log.d(TAG, "System power status Unknown :" + powerState);
                break;
            default:
                Toast.makeText(mContext, "System Power State is not setup yet .", Toast.LENGTH_LONG).show();
                break;
        }

    }


}
