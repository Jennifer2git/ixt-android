package com.imax.ipt.controller.settings;

import com.imax.ipt.controller.BaseController;
import com.imax.ipt.controller.eventbus.handler.rooms.GetLightingPresetsHandler;
import com.imax.ipt.controller.eventbus.handler.ui.MediaMenuLibraryEvent;
import com.imax.ipt.controller.eventbus.handler.ui.MediaMenuLibraryEvent.MenuEvent;
import com.imax.ipt.ui.activity.settings.preferences.PreferencesActivity;
import com.imax.ipt.controller.eventbus.handler.settings.preferences.ClearFavoritesHandler;
import com.imax.ipt.controller.eventbus.handler.settings.preferences.GetLightingEventsHandler;
import com.imax.iptevent.EventBus;

public class PreferencesController extends BaseController {
    private EventBus mEventBus;
    private PreferencesActivity mPreferencesActivity;
    private static String TAG = "PreferencesController";

    public PreferencesController(PreferencesActivity preferencesActivity) {
        this.mPreferencesActivity = preferencesActivity;
        this.mEventBus = getEventBus();
        this.mEventBus.register(this);
        this.getLightingPresetsHandler();
    }

    private void getLightingPresetsHandler() {
        this.mEventBus.post(new GetLightingPresetsHandler().getRequest());
    }

    public void GetLightingEventsHandler() {
        this.mEventBus.post(new GetLightingEventsHandler().getRequest());
    }

    public void ClearMovieFavorites() {
        this.mEventBus.post(new ClearFavoritesHandler(ClearFavoritesHandler.FAVORITE_TYPE_MOVIE).getRequest());
    }

    public void ClearMusicAlbumFavorites() {
        this.mEventBus.post(new ClearFavoritesHandler(ClearFavoritesHandler.FAVORITE_TYPE_MUSIC_ALBUM).getRequest());
    }

    public void ClearTvChannelFavorites() {
        this.mEventBus.post(new ClearFavoritesHandler(ClearFavoritesHandler.FAVORITE_TYPE_CHANNEL_PRESET).getRequest());
    }

    /**
     *
     */
    @Override
    public void onDestroy() {
        this.mEventBus.unregister(this);
    }

    //public void onEventMainThread(MediaMenuLibraryEvent menuMovieLibraryEvent)
    public void onEvent(final MediaMenuLibraryEvent menuMovieLibraryEvent) {
        final MenuEvent mCurrentEvent = menuMovieLibraryEvent.getEvent();

        mPreferencesActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mPreferencesActivity.showFragment(mCurrentEvent);

            }
        });
//      mPreferencesActivity.showFragment(mCurrentEvent);
    }

    public void onEvent(GetLightingEventsHandler getLightingEventsHandler) {
        //getLightingEventsHandler.
        mPreferencesActivity.populateLightingEvents(getLightingEventsHandler.getLightingEvents(), getLightingEventsHandler.getLightingPresets());
    }
}
