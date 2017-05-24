package com.imax.ipt.controller.settings;

import com.imax.ipt.controller.BaseController;
import com.imax.ipt.controller.GlobalController;
import com.imax.ipt.controller.eventbus.handler.input.SetNowPlayingInputHandler;
import com.imax.ipt.controller.eventbus.handler.movie.GetPlayingMovieHandler;
import com.imax.ipt.controller.eventbus.handler.push.AudioFocusInputIdChangedEvent;
import com.imax.ipt.controller.eventbus.handler.push.PipModeChangedEvent;
import com.imax.ipt.controller.eventbus.handler.push.SelectedInputChangedEvent;
import com.imax.ipt.controller.eventbus.handler.remote.SetAudioFocusHandler;
import com.imax.ipt.controller.eventbus.handler.settings.multiview.GetPipModeHandler;
import com.imax.ipt.controller.eventbus.handler.settings.multiview.SetPipModeHandler;
import com.imax.ipt.controller.eventbus.handler.ui.SetupMultiviewEvent.MultiView;
import com.imax.ipt.model.Input;
import com.imax.ipt.model.Media;
import com.imax.ipt.model.MusicAlbum;
import com.imax.ipt.model.MusicTrack;
import com.imax.ipt.ui.activity.input.movie.MovieLibraryActivity;
import com.imax.ipt.ui.activity.input.movie.MovieRemoteFullActivity;
import com.imax.ipt.ui.activity.input.music.MusicLibraryActivity;
import com.imax.ipt.ui.activity.settings.multiview.MultiViewActivity;
import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable.DeviceKind;
import com.imax.ipt.ui.viewmodel.RemoteControlUtil;
import com.imax.iptevent.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MultiViewController extends BaseController {
    private EventBus mEventBus;
    private List<Input> mInputs = new ArrayList<Input>();
    private int audioFocusOutputIndex;
    private MultiViewActivity mMultiViewActivity;

    public MultiViewController(MultiViewActivity multiViewActivity) {
        this.mEventBus = getEventBus();
        this.mEventBus.register(this);
        this.mMultiViewActivity = multiViewActivity;
        this.init();
    }

    protected void init() {
        this.sendGetPIPMode();
    }

    @Override
    public void onDestroy() {
        this.mEventBus.unregister(this);
    }

    /**
     * ##### Server Interaction *
     */
    public void GetPlayingMovie() {
        mEventBus.post(new GetPlayingMovieHandler().getRequest());
    }

    private void sendGetPIPMode() {
        mEventBus.post(new GetPipModeHandler(true).getRequest());
    }

    public void onEvent(final GetPipModeHandler getPipModeResponseEvent) {
        this.mMultiViewActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final MultiView multiView = MultiView.getMultiView(getPipModeResponseEvent.getPipMode());
                mInputs = getPipModeResponseEvent.getInputs();
                audioFocusOutputIndex = getPipModeResponseEvent.getAudioFocusOutputIndex();
                mMultiViewActivity.buildMultiview(multiView, mInputs, audioFocusOutputIndex);

            }
        });
    }

    public void setPipMode(MultiView pipMode) {
        this.mEventBus.post(new SetPipModeHandler((byte) pipMode.getPipMode()).getRequest());
    }

    public void setAudioFocus(int inputId) {
        this.mEventBus.post(new SetAudioFocusHandler(inputId).getRequest());
    }

    public void OpenInputSelectionDialog(int outputIndex) {
        mMultiViewActivity.showDialogSelectionInput(outputIndex);
    }

    public void setSelectedInput(int outputIndex, int inputId) {
        // TODO: command to be sent

    }

    public void setFullScreen(int inputId) {
        this.mEventBus.post(new SetPipModeHandler((byte) MultiView.NotSet.getPipMode()).getRequest());
        this.mEventBus.post(new SetNowPlayingInputHandler(inputId).getRequest());
    }

    public void openRemoteControl(int inputId, String title, DeviceKind deviceKind, boolean irSupported) {
        Input nowPlaying = GlobalController.getInstance().getNowPlaying();
        DeviceKind playingDeviceKind = nowPlaying.getDeviceKind();
        int id = nowPlaying.getId();
        switch (deviceKind) {
            case Game:
            case Oppo:
            case Zaxel:

            case OnlineMovie:
                RemoteControlUtil.openRemoteControl(mMultiViewActivity, inputId, title, deviceKind, irSupported);
                break;

            case Movie:
            case Imax:
                if (playingDeviceKind == DeviceKind.Movie || playingDeviceKind == DeviceKind.Imax) {
                    Media media = GlobalController.getInstance().getCurrentMovieMedia();
                    if (media != null) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("movie_guid",media.getId());
//                        bundle.putString("movie_title",media.getTitle());
//                        bundle.putString("movie_cover_art",media.getCoverArtPath());
//                        bundle.putString("movie_rating",media.getRating());
//                        bundle.putInt("movie_year",media.getYear());
//                        bundle.putParcelableArray("movie_genre",media.getGenre());
//                        MovieRemoteFullActivity.fire(mMultiViewActivity, (com.imax.ipt.model.Movie)media, inputId);
                        MovieRemoteFullActivity.fire(mMultiViewActivity, media.getId(),media.getTitle(), inputId);
                        return;
                    }
                }
                MovieLibraryActivity.fireToFront(mMultiViewActivity);
                break;

            case Music:
                if (playingDeviceKind == DeviceKind.Music) {
                    MusicAlbum musicAlbum = GlobalController.getInstance().getCurrentMusicTrack();
                    if (musicAlbum != null) {
                        MusicTrack[] musicTracks = musicAlbum.getTracks();
                        if (musicTracks != null && musicTracks.length > 0) {
                            RemoteControlUtil.openMusicRemoteControl(mMultiViewActivity, musicAlbum, musicTracks[0]);
                            return;
                        }
                    }
                }
                MusicLibraryActivity.fire(mMultiViewActivity);
                break;
        }
    }
   
/*   *//**
     * UIInteraction
     *//*
   public void onEvent(SetupMultiviewEvent setupMultiviewEvent)
   {
      final MultiView multiView = setupMultiviewEvent.getMultiView();
      this.mEventBus.post(new SetPipModeHandler((byte) multiView.getPipMode()).getRequest());

      mMultiViewActivity.runOnUiThread(new Runnable() {
         @Override
         public void run()
         {
            mMultiViewActivity.buildMultiview(multiView, mDeviceTypes);
         }
      });
   }*/

//   public void onEvent(SetSelectedInputHandler setSelectedInputResponseEvent)
//   {
//     // this.sendGetPIPMode();onEvent(SetSelectedInputHandler setSelectedInputResponseEvent)
//   }
//
//   public void onEvent(SetAudioFocusHandler setAudioFocusResponseEvent)
//   {
//     // this.sendGetPIPMode();
//   }

//   /**
//    * 
//    * @param setPipModeHandleronEvent(SetSelectedInputHandler setSelectedInputResponseEvent)
//    */onEvent(SetSelectedInputHandler setSelectedInputResponseEvent)
//   public void onEvent(onEvent(SetSelectedInputHandler setSelectedInputResponseEvent)SetPipModeHandler setPipModeHandler)
//   {
//      //this.sendGetPIPMode();
//   }

    /**
     * @param
     */
    public void onEvent(PipModeChangedEvent pipModeChangedEvent) {
        this.sendGetPIPMode();
    }

    /**
     * @param setPipModeHandler
     */
    public void onEvent(AudioFocusInputIdChangedEvent setPipModeHandler) {
        this.sendGetPIPMode();
    }

    public void onEvent(SelectedInputChangedEvent setPipModeHandler) {
        this.sendGetPIPMode();
    }

    /**
     * Getters and Setters
     */

    public int getAudioFocusOutputIndex() {
        return audioFocusOutputIndex;
    }

    public List<Input> getmInputs() {
        return mInputs;
    }

    public void setmInputs(List<Input> mInputs) {
        this.mInputs = mInputs;
    }

    public void setAudioFocusOutputIndex(int audioFocusOutputIndex) {
        this.audioFocusOutputIndex = audioFocusOutputIndex;
    }

    public EventBus getmEventBus() {
        return mEventBus;
    }

    public void setmEventBus(EventBus mEventBus) {
        this.mEventBus = mEventBus;
    }

}
