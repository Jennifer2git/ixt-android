package com.imax.ipt.ui.activity.media;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.common.Constants;
import com.imax.ipt.controller.eventbus.handler.push.AudioMuteChangedEvent;
import com.imax.ipt.controller.eventbus.handler.remote.*;
import com.imax.ipt.ui.activity.BaseActivity;
import com.imax.ipt.ui.activity.LobbyActivity;
import com.imax.ipt.ui.activity.input.DialogNumberFragment;
import com.imax.ipt.ui.activity.input.DialogVideoViewFragment;
import com.imax.ipt.ui.activity.input.DialogZoomFragment;
import com.imax.ipt.ui.activity.settings.multiview.MultiViewActivity;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.util.VibrateUtil;
import com.imax.ipt.ui.widget.verticalseekbar.VerticalSeekBar;
import com.imax.iptevent.EventBus;

public class MediaRemote extends BaseActivity implements OnSeekBarChangeListener, OnClickListener, VerticalSeekBar.OnSeekBarChangeListener {
    public static final int SIMPLE = 0;
    public static final int FULL = 1;

    protected int inputId;
    private boolean isPause = false;
    private boolean isStop = false;
    private boolean mute = false;
    private boolean play = true;
    protected int mode = SIMPLE;
    protected IPTTextView txtTitle;
    protected String mTitle;

    public enum ExecuteRemoteControl {
        PlayPause, Play, Stop, Pause, NextTrack, PreviousTrack, FastForward, Rewind, PreviousMenu, BackMenu, RootMenu,
        PopupMenu, Audio, Subtitles, NetVideo, Display, Zoom, Enter, DirectionUp, DirectionDown, DirectionLeft,
        DirectionRight, Options, Key0, Key1, Key2, Key3, Key4, Key5, Key6, Key7, Key8, Key9,
    }

    public static final String MEDIA_ID = "mediaId";
    public static final String MEDIA_TITLE = "titleId";

    public static final String MUSIC_TRACK_ID = "musicTrackId";
    public static final String MUSIC_ALBUM_ID = "musicAlbumId";

    public static final String MUSIC_ARTIST = "musicArtist";
    public static final String MUSIC_ALBUM = "musicAlbum";
    public static final String MUSIC_COVER = "musicCover";

    //    protected VerticalSeekBar mSeekBarVolumen;
    protected SeekBar mSeekBarVolumen;
    protected ImageButton mBtnMute;
    protected ImageButton mBtnPlayPause;
    protected ImageButton mBtnPlay;
    protected ImageButton mBtnPause;
    protected ImageButton mBtnRew;
    protected ImageButton mBtnFF;
    protected ImageButton mBtnAudioLanguage;
    protected ImageButton mBtnSubtitles;
    protected ImageButton mBtnPopUpMenu;
    protected ImageButton mBtnPreviousMenu;
    protected ImageButton mBtnRootMenu;
    protected ImageButton mBtnOk;
    protected Button mBtnOk2;
    protected ImageButton mBtnCloseControl;
    protected ImageButton mBtnUp;
    protected ImageButton mBtnDown;
    protected ImageButton mBtnLeft;
    protected ImageButton mBtnRigth;
    protected ImageButton mBtnStop;

    protected ImageButton mBtnPrevious;
    protected ImageButton mBtnNext;

    protected ImageButton mBtnMultiview;
    protected Button mBtnVideoMode;

    protected TextView txtOk;

    private static int up = 0;
    private static int down = 0;
    protected ImageButton volupButton;
    protected ImageButton voldownButton;

    private DialogVideoViewFragment mDialogVideoModeFragment;
    private DialogZoomFragment mDialogZoomFragment;
    private DialogNumberFragment mDialogNumFragment;

    protected EventBus mEventBus;

/*   private int rew = 0;
   private int ff = 0;*/

    private boolean mVolumenStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
    }

    private void init() {
        mEventBus = IPT.getInstance().getEventBus();
        mEventBus.register(this);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//        Log.d("cll", "onProgressChanged=" + mSeekBarVolumen.getProgress() + ",+" + progress + ",user=" + fromUser);

        if (!mVolumenStart) {
            mVolumenStart = true;
            return;
        }
        if (!fromUser) {
//            mSeekBarVolumen.setProgress(progress);
            return;
        } else {
            if (progress > Constants.SCROLL_MAX_VOLUME_VALUE) {
//                mSeekBarVolumen.setProgress(Constants.SCROLL_MAX_VOLUME_VALUE);
                progress = Constants.SCROLL_MAX_VOLUME_VALUE;
            }
            setVolumen(progress);
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
//        Log.d("water", "start volume=" + mSeekBarVolumen.getProgress());
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
//        Log.d("water", "stop volume=" + mSeekBarVolumen.getProgress());
    }


    @Override
    public void onProgressChanged(VerticalSeekBar VerticalSeekBar, int progress, boolean fromUser) {
        Log.d("CLL", "CLL onProgressChanged call");

//        if (!mVolumenStart) {
//            mVolumenStart = true;
//            return;
//        }
//
//        if (fromUser) {
//            if (progress > Constants.SCROLL_MAX_VOLUME_VALUE) {
//                mSeekBarVolumen.setProgress(Constants.SCROLL_MAX_VOLUME_VALUE);
//                setVolumen(Constants.SCROLL_MAX_VOLUME_VALUE);
//                return;
//            }
//        } else {
////            mSeekBarVolumen.setProgress(progress);
//            return;
//        }
//
//        if (mute) {
//            mute = false;
////            mBtnMute.setBackground(getResources().getDrawable(R.drawable.ipt_gui_asset_vol_mute_inactive));
//            mBtnMute.setBackground(getResources().getDrawable(R.drawable.catalogue_mute_volume_icn));
//            /** begen remove set volume seekbar
//             int progressOld = mSeekBarVolumen.getProgress();
//             int offset = mSeekBarVolumen.getThumbOffset();
//             mSeekBarVolumen.setThumb(getResources().getDrawable(R.drawable.ipt_gui_asset_vol_knob_active_down));
//             mSeekBarVolumen.setThumbOffset(offset);
//             mSeekBarVolumen.setProgress(progressOld);
//             /** end remove set volume seekbar */
//        }
//
//        setVolumen(progress);
    }

    @Override
    public void onStartTrackingTouch(VerticalSeekBar VerticalSeekBar) {
    }

    @Override
    public void onStopTrackingTouch(VerticalSeekBar VerticalSeekBar) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

//            case R.id.volumeUp:
//
////                mBtnMute.setBackground(getResources().getDrawable(R.drawable.ipt_gui_asset_vol_mute_inactive));
//                mBtnMute.setImageDrawable(getResources().getDrawable(R.drawable.catalogue_mute_volume_icn));
//                mute = false;
//                mSeekBarVolumen.setEnabled(true);
//
//                int progressOld1 = mSeekBarVolumen.getProgress();
//                int offset1 = mSeekBarVolumen.getThumbOffset();
////                mSeekBarVolumen.setThumb(getResources().getDrawable(R.drawable.ipt_gui_asset_vol_knob_active_down));
////                mSeekBarVolumen.setThumb(getResources().getDrawable(R.drawable.ipt_gui_asset_vol_knob_active_down));
//                mSeekBarVolumen.setThumbOffset(offset1);
//
//                if (progressOld1 + 1 > Constants.MAX_VOLUME_VALUE) {
//                    progressOld1 = Constants.MAX_VOLUME_VALUE;
//                    mSeekBarVolumen.setProgress(progressOld1);
//                    setVolumen(-1);//add
//                    return;
//                } else {
//                    if (up == 0) {
//                        up = 1;
//                        mSeekBarVolumen.setProgress(progressOld1);
//                        setVolumen(-1);
//                        return;
//
//                    } else {
//                        up = 0;
//                    }
//                    progressOld1 = progressOld1 + 1;
//                }
//                mSeekBarVolumen.setProgress(progressOld1);
//                setVolumen(-1);
//                break;
//            case R.id.volumeDown:
////                mBtnMute.setBackground(getResources().getDrawable(R.drawable.ipt_gui_asset_vol_mute_inactive));
//                mBtnMute.setImageDrawable(getResources().getDrawable(R.drawable.catalogue_mute_volume_icn));
//                mute = false;
//                mSeekBarVolumen.setEnabled(true);
//
//                int progressOld2 = mSeekBarVolumen.getProgress();
//                int offset2 = mSeekBarVolumen.getThumbOffset();
////                mSeekBarVolumen.setThumb(getResources().getDrawable(R.drawable.ipt_gui_asset_vol_knob_active_down));
//                mSeekBarVolumen.setThumbOffset(offset2);
//
//                if (progressOld2 - 1 < 0) {
//                    progressOld2 = 0;
//                    mSeekBarVolumen.setProgress(progressOld2);
//                    return;
//
//                } else {
//                    if (down == 0) {
//                        down = 1;
//                        mSeekBarVolumen.setProgress(progressOld2);
//                        setVolumen(-2);
//                        return;
//
//                    } else {
//                        down = 0;
//                    }
//                    progressOld2 = progressOld2 - 1;
//                }
//                mSeekBarVolumen.setProgress(progressOld2);
//                setVolumen(-2);
//                break;
//            case R.id.btnVolMute:
//                if (!mute) {
//                    mEventBus.post(new SetMuteHandler(true).getRequest());
//                    mute = true;
////                    mBtnMute.setBackground(getResources().getDrawable(R.drawable.ipt_gui_asset_vol_mute_active));
//                    mBtnMute.setImageDrawable(getResources().getDrawable(R.drawable.catalogue_mute_volume_active_icn));
//                    int progress = mSeekBarVolumen.getProgress();
//                    int offset = mSeekBarVolumen.getThumbOffset();
////                    mSeekBarVolumen.setThumb(getResources().getDrawable(R.drawable.ipt_gui_asset_vol_knob_muted_down));
//                    mSeekBarVolumen.setThumbOffset(offset);
//                    mSeekBarVolumen.setProgress(progress);
//                    mSeekBarVolumen.invalidate();
//                    mSeekBarVolumen.setEnabled(false);
//                } else {
//                    mute = false;
//                    mEventBus.post(new SetMuteHandler(false).getRequest());
////                    mBtnMute.setBackground(getResources().getDrawable(R.drawable.ipt_gui_asset_vol_mute_inactive));
//                    mBtnMute.setImageDrawable(getResources().getDrawable(R.drawable.catalogue_mute_volume_icn));
//                    int progress = mSeekBarVolumen.getProgress();
//                    int offset = mSeekBarVolumen.getThumbOffset();
////                    mSeekBarVolumen.setThumb(getResources().getDrawable(R.drawable.ipt_gui_asset_vol_knob_active_down));
//                    mSeekBarVolumen.setThumbOffset(offset);
//                    mSeekBarVolumen.setProgress(progress);
//                    mSeekBarVolumen.setEnabled(true);
//                }
//                break;

            case R.id.btnPlay:
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.Play.toString()).getRequest());
//                if (!play){// now is pause
////                    mBtnPlay.setImageDrawable(getResources().getDrawable(R.drawable.player_play_notactive_btn));
//                    mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.Pause.toString()).getRequest());
//
//                }else{// now is playing
////                    mBtnPlay.setImageDrawable(getResources().getDrawable(R.drawable.player_pause_active_btn));
//                    mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.PlayPause.toString()).getRequest());
//
//                }
                play = !play;

                if (mBtnStop != null) {
                    mBtnStop.setImageDrawable(getResources().getDrawable(R.drawable.selector_remote_full_stop_button));
                }

                break;

            case R.id.btnStop:
//         if (mode == SIMPLE)
//         {
//            mBtnPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.selector_remote_pause_button));
//            mBtnRew.setImageDrawable(getResources().getDrawable(R.drawable.selector_remote_rew_button));
//            mBtnFF.setImageDrawable(getResources().getDrawable(R.drawable.selector_remote_ff_button));
//         }
//         else
//         {
//            mBtnPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.selector_remote_full_pause_button));
//            mBtnRew.setImageDrawable(getResources().getDrawable(R.drawable.selector_remote_full_rew_menu_button));
//            mBtnFF.setImageDrawable(getResources().getDrawable(R.drawable.selector_remote_full_ff_button));
//         }
                isStop = !isStop;
//                mBtnStop.setImageDrawable(getResources().getDrawable(R.drawable.player_stop_active_btn));
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.Stop.toString()).getRequest());
                play = true;
//                mBtnPlay.setImageDrawable(getResources().getDrawable(R.drawable.selector_remote_full_play_button));
                mBtnPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.selector_remote_full_play_button));
                break;
            case R.id.btnPause:
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.Pause.toString()).getRequest());
                break;

            case R.id.btnPlayPause:
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.PlayPause.toString()).getRequest());
//                if (!play){// now is pause
////                    mBtnPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.player_play_notactive_btn));
////                    mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.Pause.toString()).getRequest());
//
//                }else{// now is playing
////                    mBtnPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.player_pause_active_btn));
////                    mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.PlayPause.toString()).getRequest());
//
//                }
                play = !play;
                if (mBtnStop != null) {
                    mBtnStop.setImageDrawable(getResources().getDrawable(R.drawable.selector_remote_full_stop_button));
                }
                break;

            case R.id.btnRew:
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.Rewind.toString()).getRequest());
                break;

            case R.id.btnFF:

                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.FastForward.toString()).getRequest());
         /*
         ff++;
         switch (ff)
         {
         case 1:
            if (mode == SIMPLE)
            {
               mBtnFF.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_ff1_btn_simple_active));
            }
            elsew
            {
               mBtnFF.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_ff1_btn_full_active));
            }
            mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.FastForward.toString()).getRequest());
            break;
         case 2:
            if (mode == SIMPLE)
            {
               mBtnFF.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_ff2_btn_simple_active));
            }
            else
            {
               mBtnFF.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_ff2_btn_full_active));
            }
            mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.FastForward.toString()).getRequest());
            break;
         case 3:
            ff = 0;
            if (mode == SIMPLE)
            {
               mBtnFF.setImageDrawable(getResources().getDrawable(R.drawable.selector_remote_ff_button));
            }
            else
            {
               mBtnFF.setImageDrawable(getResources().getDrawable(R.drawable.selector_remote_full_ff_button));
            }
            mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.FastForward.toString()).getRequest());
            break;
         }*/
                break;

            case R.id.btnAudioLanguage:
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.Audio.toString()).getRequest());
                break;

            case R.id.btnSubtitles:
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.Subtitles.toString()).getRequest());
                break;

            case R.id.btnPopUpMenu:
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.PopupMenu.toString()).getRequest());
                break;

            case R.id.btn_remote:
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.PreviousMenu.toString()).getRequest());
//                MozaxRemoteControlActivity.fire(this, "guid", "", 1);
            case R.id.btnPreviousMenu:
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.PreviousMenu.toString()).getRequest());
                break;

            case R.id.btnMovieMenu:
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.RootMenu.toString()).getRequest());
                break;

//            case R.id.btnCloseControl:
//                if(mBtnOk2 != null){
//                    mBtnOk2.setVisibility(View.VISIBLE);
//                    mBtnCloseControl.setVisibility(View.VISIBLE);
//                }
//                break;
            case R.id.txtOK:
            case R.id.btnOk2:
            case R.id.btnOk:
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.Enter.toString()).getRequest());
                break;

            case R.id.btnUp:
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.DirectionUp.toString()).getRequest());
//                if(mBtnOk2 != null){
//                    mBtnOk2.setVisibility(View.VISIBLE);
//                    mBtnCloseControl.setVisibility(View.VISIBLE);
//
//                }
                break;

            case R.id.btnDown:
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.DirectionDown.toString()).getRequest());
//                if(mBtnOk2 != null){
//                    mBtnOk2.setVisibility(View.VISIBLE);
//                    mBtnCloseControl.setVisibility(View.VISIBLE);
//
//                }

                break;

            case R.id.btnLeft:
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.DirectionLeft.toString()).getRequest());
//                if(mBtnOk2 != null){
//                    mBtnOk2.setVisibility(View.VISIBLE);
//                    mBtnCloseControl.setVisibility(View.VISIBLE);
//
//                }

                break;

            case R.id.btnRigth:
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.DirectionRight.toString()).getRequest());
//                if(mBtnOk2 != null){
//                mBtnOk2.setVisibility(View.VISIBLE);
//                mBtnCloseControl.setVisibility(View.VISIBLE);
//
//                }
                break;

            case R.id.btnPrev:
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.PreviousTrack.toString()).getRequest());
                break;
            case R.id.btnNext:
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.NextTrack.toString()).getRequest());
                break;
            case R.id.btnMultiview:
                MultiViewActivity.fire(getApplicationContext());
                break;
            case R.id.btnVideoMode:
                showDialogVideoMode();
                break;
            case R.id.btnNetVideo://for sony 4k audio control
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.Display.toString()).getRequest());
                break;
            case R.id.btnOptionMenu:
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.RootMenu.toString()).getRequest());
                break;
            case R.id.btnOptionsMenu:
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.Options.toString()).getRequest());
                break;
            case R.id.btnMasterMenu:
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.PopupMenu.toString()).getRequest());
                break;
            case R.id.btnBackMenu:
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.BackMenu.toString()).getRequest());
                break;
            case R.id.btnnum:
                showDialogNum();
                break;

            case R.id.btnZoom:
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.Zoom.toString()).getRequest());
                showDialogZoom();
                break;
            case R.id.btn_nav_Imax:
            case R.id.btnMenuMaster:
                LobbyActivity.fire(getApplicationContext());
        }

        VibrateUtil.vibrate(getApplicationContext(), 10);
    }


    private void showDialogZoom() {
        FragmentManager fm = getSupportFragmentManager();
        mDialogZoomFragment = DialogZoomFragment.newInstance(inputId);
        mDialogZoomFragment.show(fm, "fragment_dialog_zoom");
    }

    private void showDialogNum() {
        FragmentManager fm = getSupportFragmentManager();
        mDialogNumFragment = DialogNumberFragment.newInstance(inputId);
        mDialogNumFragment.show(fm, "fragment_dialog_num");
    }

    private void showDialogVideoMode() {
        FragmentManager fm = getSupportFragmentManager();
        mDialogVideoModeFragment = DialogVideoViewFragment.newInstance(inputId);
        mDialogVideoModeFragment.show(fm, "fragment_dialog_multiview");
    }

    // TODO change this for a Controller.

    /**
     * #SERVER INTERACTION
     */
    protected void sendRequestGetVolume() {
        mEventBus.post(new GetVolumeHandler().getRequest());
    }

    /**
     *
     */
    protected void sendRequestGetMuteVolume() {
        mEventBus.post(new GetMuteHandler().getRequest());
    }

    /**
     * @param progress
     */
    private void setVolumen(int progress) {
        mEventBus.post(new SetVolumenHandler(progress).getRequest());
    }

    /**
     * @param getMuteResponseEvent
     */
    public void onEvent(GetMuteHandler getMuteResponseEvent) {
//        if (getMuteResponseEvent == null) {
//            return;
//        }
//        mute = getMuteResponseEvent.isMute();
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mBtnMute.setPressed(mute);
//                if (mute) {
//                    mSeekBarVolumen.setEnabled(false);
//                }
//            }
//        });
    }

    /**
     * @param
     */
    public void onEvent(AudioMuteChangedEvent audioMuteChangedEvent) {
//        final boolean mute = audioMuteChangedEvent.ismMute();
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mBtnMute.setPressed(mute);
//            }
//        });
    }

    /**
     * @param getVolumeEventResponse
     */
    public void onEvent(GetVolumeHandler getVolumeEventResponse) {
//        final double volume = getVolumeEventResponse.getVolume();
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mSeekBarVolumen.setProgress((int) volume);
//            }
//        });
    }


}