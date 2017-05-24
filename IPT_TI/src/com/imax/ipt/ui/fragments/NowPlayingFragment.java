package com.imax.ipt.ui.fragments;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.common.Constants;
import com.imax.ipt.controller.eventbus.handler.push.AudioMuteChangedEvent;
import com.imax.ipt.controller.eventbus.handler.remote.*;
import com.imax.ipt.model.*;
import com.imax.ipt.ui.activity.input.gaming.GamingActivity;
import com.imax.ipt.ui.activity.input.gdc.GdcActivity;
import com.imax.ipt.ui.activity.input.karaoke.KaraokeActivity;
import com.imax.ipt.ui.activity.input.security.SecurityCamActivity;
import com.imax.ipt.ui.activity.media.ExternalInputActivity;
import com.imax.ipt.ui.activity.media.MediaRemote;
import com.imax.ipt.ui.activity.media.OppoRemoteFullActivity;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable.DeviceKind;
import com.imax.ipt.ui.util.ImageUtil;
import com.imax.ipt.ui.viewmodel.RemoteControlUtil;
import com.imax.iptevent.EventBus;

public class NowPlayingFragment extends Fragment implements SeekBar.OnSeekBarChangeListener, OnClickListener {
    public static final String TAG = "NowPlayingFragment";

    private ImageView mImagePoster;
    private IPTTextView mTxtTitle;
    private IPTTextView mTxtSubtitle1;
    private IPTTextView mTxtSubtitle2;
    private LoaderImage mLoaderImg;
    private DeviceKind deviceKind;

    private Media media;
    private Input input;

    private ImageButton mBtnMute;
    private ImageButton mVolumeUpBtn;
    private ImageButton mVolumeDownBtn;
    private ImageButton mBtnPlayPause;
    private SeekBar mSeekBarVolumen;
    private boolean mute = false;
    private static int up = 0;
    private static int down = 0;
    private boolean play = true;

    private boolean mVolumenStart = false;
    protected EventBus mEventBus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mNowPlaying = inflater.inflate(R.layout.fragment_now_playing, null);
//        mNowPlaying.setOnClickListener(this);
        this.init(mNowPlaying);
        return mNowPlaying;
    }


    private void init(View view) {
        mEventBus = IPT.getInstance().getEventBus();
        mEventBus.register(this);
        mVolumeUpBtn = (ImageButton) view.findViewById(R.id.volumeUp);
        mVolumeDownBtn = (ImageButton) view.findViewById(R.id.volumeDown);
        mBtnMute = (ImageButton) view.findViewById(R.id.btnVolMute);
        mSeekBarVolumen = (SeekBar) view.findViewById(R.id.seekBarVolume);
        mBtnPlayPause = (ImageButton) view.findViewById(R.id.btnPlayPause);

        mVolumeUpBtn.setOnClickListener(this);
//        mVolumeDownBtn.setOnClickListener(this);
        mSeekBarVolumen.setOnSeekBarChangeListener(this);
        mEventBus.post(new GetVolumeHandler().getRequest());

        this.mSeekBarVolumen.setMax(Constants.MAX_VOLUME_VALUE); //98-18db, 86-6db
        mVolumeDownBtn.setVisibility(View.GONE);

        mEventBus.post(new GetMuteHandler().getRequest());
        mBtnMute.setOnClickListener(this);

        mBtnPlayPause.setOnClickListener(this);

        mImagePoster = (ImageView) view.findViewById(R.id.imgPoster);
        mImagePoster.setClickable(true);
        mImagePoster.setOnClickListener(this);

        TextView txtPlaying = (IPTTextView) view.findViewById(R.id.txtNowPlaying);
        mTxtTitle = (IPTTextView) view.findViewById(R.id.txtTitle);
        Typeface face = Typeface.createFromAsset(getResources().getAssets(), Constants.FONT_LIGHT_PATH);
        txtPlaying.setTypeface(face);
        mTxtTitle.setTypeface(face);
        mTxtSubtitle1 = (IPTTextView) view.findViewById(R.id.txtSubtitle1);
        mTxtSubtitle2 = (IPTTextView) view.findViewById(R.id.txtSubtitle2);
        mTxtSubtitle1.setTypeface(face);
        mTxtSubtitle2.setTypeface(face);

        if (media == null || media.getTitle().equals("empty_media")) {
            txtPlaying.setVisibility(View.INVISIBLE);
            mImagePoster.setVisibility(View.INVISIBLE);
            return;
        }
        if (media != null) {
            if (media.getMediaType() != null) {
                switch (media.getMediaType()) {
                    case MOVIE: {
                        mImagePoster.setImageResource(R.drawable.lobby_movies_active_icn);

                        if (media.getCoverArtPath() == null) {
                        } else {

                        }
                        mLoaderImg = new LoaderImage();
                        mLoaderImg.execute(media.getCoverArtPath());
                        mTxtTitle.setText(media.getTitle());
                        deviceKind = DeviceKind.Movie;
                        break;
                    }

                    case ALBUMS: {
                        MusicAlbum musicAlbum = (MusicAlbum) media;
                        mImagePoster.setImageResource(R.drawable.lobby_music_active_icn);

                        mLoaderImg = new LoaderImage();
                        mLoaderImg.execute(musicAlbum.getCoverArtPath());
                        mTxtTitle.setText(musicAlbum.getName());
                        MusicTrack[] musicTracks = musicAlbum.getTracks();
                        if (musicTracks != null && musicTracks.length > 0) {
                            MusicTrack mMusicTrack = musicTracks[0];
                            mTxtSubtitle1.setText(mMusicTrack.getTitle());
                            Person mArtist = mMusicTrack.getArtist();
                            mTxtSubtitle2.setText(mArtist.fullName());
                        }
                        deviceKind = DeviceKind.Music;
                        break;
                    }
                    default:
                        break;
                }
            }
        } else if (input != null) {
            mTxtTitle.setText(input.getDisplayName());
            switch (input.getDeviceKind()) {
                case Game:
//                    mImagePoster.setImageResource(R.drawable.ipt_gui_asset_now_playing_gaming);
                    mImagePoster.setImageResource(R.drawable.lobby_controller_active_icn);
                    break;
                case Gdc:
                case Imax:
//                    mTxtTitle.setText(R.string.media_devices);
//                    mImagePoster.setImageResource(R.drawable.ipt_gui_asset_now_playing_hollywood);
                    mImagePoster.setImageResource(R.drawable.lobby_homepremiere_active_icn);
                    break;
                case OnlineMovie:
//                    mTxtTitle.setText(R.string.tv);
//                    mImagePoster.setImageResource(R.drawable.ipt_gui_asset_now_playing_sony);
                    mImagePoster.setImageResource(R.drawable.lobby_sony4k_active_icn);
                    break;
                case Security:
                    break;
                case Extender:
//                    mTxtTitle.setText(R.string.external_input);
                    mImagePoster.setImageResource(R.drawable.lobby_external_active_icn);
                    break;
                case Karaoke:
//                    mTxtTitle.setText(R.string.external_input_karaoke);
                    mImagePoster.setImageResource(R.drawable.lobby_karaoke_active_icn);
                    break;
                case Oppo:
                    mImagePoster.setImageResource(R.drawable.selector_menu_input_oppo_button);
            }
        }
    }

    @Override
    public void onDetach() {
        mEventBus.unregister(this);
        super.onDetach();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!mVolumenStart) {
            mVolumenStart = true;
            return;
        }

        if(!fromUser){
            mSeekBarVolumen.setProgress(progress);
            return;
        }else{
            if (progress > Constants.SCROLL_MAX_VOLUME_VALUE) {
                mSeekBarVolumen.setProgress(Constants.SCROLL_MAX_VOLUME_VALUE);
                progress = Constants.SCROLL_MAX_VOLUME_VALUE;
            }
            setVolumen(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /**
     * TODO CREATE GENERIC CLASS FOR THIS
     *
     * @author rlopez
     */
    class LoaderImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            return ImageUtil.getImage(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            if (result != null) {
                if (getActivity() != null) {
                    BitmapDrawable background = new BitmapDrawable(getActivity().getResources(), result);
                    mImagePoster.setImageDrawable(background);
                }
            }
        }

    }

    /**
     * Getters and setters
     *
     * @return
     */
//   public MusicAlbum getmMusicAlbum()
//   {
//      return mMusicAlbum;
//   }

   /*public void setmMusicAlbum(Media mMusicAlbum)
   {
      this.mMusicAlbum = (MusicAlbum) mMusicAlbum;
   }*/
    public void setMedia(Media media) {
        this.media = media;
    }

    public void setDeviceType(Input input) {
        this.input = input;
    }


    public void onEventMainThread(AudioMuteChangedEvent audioMuteChangedEvent) {
        final boolean mute = audioMuteChangedEvent.ismMute();
        mBtnMute.setPressed(mute);
    }

    public void onEventMainThread(GetMuteHandler getMuteResponseEvent) {
        if (getMuteResponseEvent == null) {
            return;
        }
        mute = getMuteResponseEvent.isMute();
        if (mute) {
            mBtnMute.setImageDrawable(getResources().getDrawable(R.drawable.catalogue_mute_volume_active_icn));
            mSeekBarVolumen.setEnabled(false);
        }else{
            mBtnMute.setImageDrawable(getResources().getDrawable(R.drawable.catalogue_mute_volume_icn));
            mSeekBarVolumen.setEnabled(true);
        }
    }

    public void onEventMainThread(GetVolumeHandler getVolumeEventResponse) {
        final double volume = getVolumeEventResponse.getVolume();
        mSeekBarVolumen.setProgress((int) volume);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtNowPlaying:
            case R.id.txtTitle:
            case R.id.txtSubtitle1:
            case R.id.txtSubtitle2:
            case R.id.imgPoster:
                switchMedia();
                break;
            case R.id.btnPlayPause:
                mEventBus.post(new ExecuteRemoteControlHandler(MediaRemote.ExecuteRemoteControl.PlayPause.toString()).getRequest());
                break;
            case R.id.volumeUp:
                mBtnMute.setImageDrawable(getResources().getDrawable(R.drawable.catalogue_mute_volume_icn));
                mute = false;
                mSeekBarVolumen.setEnabled(true);

                int progressOld1 = mSeekBarVolumen.getProgress();
                int offset1 = mSeekBarVolumen.getThumbOffset();
                mSeekBarVolumen.setThumbOffset(offset1);

                if (progressOld1 + 1 > Constants.MAX_VOLUME_VALUE) {
                    progressOld1 = Constants.MAX_VOLUME_VALUE;
                    mSeekBarVolumen.setProgress(progressOld1);
                    setVolumen(-1);//add
                    return;
                } else {
                    if (up == 0) {
                        up = 1;
                        mSeekBarVolumen.setProgress(progressOld1);
                        setVolumen(-1);
                        return;

                    } else {
                        up = 0;
                    }
                    progressOld1 = progressOld1 + 1;
                }
                mSeekBarVolumen.setProgress(progressOld1);
                setVolumen(-1);
                break;

            case R.id.volumeDown:
                mBtnMute.setImageDrawable(getResources().getDrawable(R.drawable.catalogue_mute_volume_icn));
                mute = false;
                mSeekBarVolumen.setEnabled(true);

                int progressOld2 = mSeekBarVolumen.getProgress();
                int offset2 = mSeekBarVolumen.getThumbOffset();
                mSeekBarVolumen.setThumbOffset(offset2);

                if (progressOld2 - 1 < 0) {
                    progressOld2 = 0;
                    mSeekBarVolumen.setProgress(progressOld2);
                    return;

                } else {
                    if (down == 0) {
                        down = 1;
                        mSeekBarVolumen.setProgress(progressOld2);
                        setVolumen(-2);
                        return;

                    } else {
                        down = 0;
                    }
                    progressOld2 = progressOld2 - 1;
                }
                mSeekBarVolumen.setProgress(progressOld2);
                setVolumen(-2);
                break;
            case R.id.btnVolMute:
                if (!mute) {
                    mEventBus.post(new SetMuteHandler(true).getRequest());
                    mute = true;
                    mBtnMute.setImageDrawable(getResources().getDrawable(R.drawable.catalogue_mute_volume_active_icn));
                    int progress = mSeekBarVolumen.getProgress();
                    int offset = mSeekBarVolumen.getThumbOffset();
                    mSeekBarVolumen.setThumbOffset(offset);
                    mSeekBarVolumen.setProgress(progress);
                    mSeekBarVolumen.invalidate();
                    mSeekBarVolumen.setEnabled(false);
                } else {
                    mute = false;
                    mEventBus.post(new SetMuteHandler(false).getRequest());
                    mBtnMute.setImageDrawable(getResources().getDrawable(R.drawable.catalogue_mute_volume_icn));
                    int progress = mSeekBarVolumen.getProgress();
                    int offset = mSeekBarVolumen.getThumbOffset();
                    mSeekBarVolumen.setThumbOffset(offset);
                    mSeekBarVolumen.setProgress(progress);
                    mSeekBarVolumen.setEnabled(true);
                }
                break;
        }

    }

    private void switchMedia() {
        if (media != null) {
            switch (media.getMediaType()) {
                case MOVIE: {
//                    RemoteControlUtil.openRemoteControl(getActivity(), 0, media.getTitle(), deviceKind, true);// need guid
                    RemoteControlUtil.openRemoteControl(getActivity(), Constants.DEVICE_ID_MOVIE, media.getId(), media.getTitle(), deviceKind, true);// need guid
                    break;
                }

                case ALBUMS: {
                    MusicAlbum musicAlbum = (MusicAlbum) media;
                    MusicTrack mMusicTrack = musicAlbum.getTracks()[0];
                    Person mArtist = mMusicTrack.getArtist();

                    RemoteControlUtil.openMusicRemoteControl(getActivity(), musicAlbum, mMusicTrack);
                    break;
                }
            }
        } else if (input != null) {
            switch (input.getDeviceKind()) {
                case Game:
                    GamingActivity.fire(getActivity(), null, input.getDisplayName(), input.getId());
                    break;
                case Imax:
                case OnlineMovie:
                case Zaxel:
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
//            boolean isControlComplexityFull = prefs.getBoolean(IRControl.PREFERENCE_DEFAULT_CONTROL_COMPLEXITY_FULL, Boolean.FALSE);
                    RemoteControlUtil.openRemoteControl(getActivity().getApplicationContext(), input.getId(), input.getDisplayName(), input.getDeviceKind(), input.isIrSupported());
                    break;
                case Gdc:
                    GdcActivity.fire(getActivity(), null, input.getDisplayName(), input.getId());
                    break;
                case Extender:
                    ExternalInputActivity.fire(getActivity(), null, input.getDisplayName(), input.getId());
                    break;
                case Karaoke:
                    KaraokeActivity.fire(getActivity(), null, input.getDisplayName(), input.getId());
                    break;
                case Security:
                    SecurityCamActivity.fire(getActivity());
                    break;
                case Oppo:
                    OppoRemoteFullActivity.fire(getActivity(), null, input.getDisplayName(), input.getId());
                    break;
                default:
                    break;
            }
        }

    }

    private void setVolumen(int progress) {
        mEventBus.post(new SetVolumenHandler(progress).getRequest());
    }

//   @Override
//   public boolean onTouch(View v, MotionEvent event)
//   {
//      if (event.getAction() == MotionEvent.ACTION_DOWN)
//      {
//         if (mMusicTrack != null && mMusicAlbum != null && mArtist != null)
//         {
//            MusicRemoteActivity.fire(getActivity(), mMusicAlbum.getId(), mMusicTrack.getId(), mMusicTrack.getTitle(), mArtist.fullName(), mMusicAlbum.getName(), mMusicAlbum.getCoverArtPath());
//         }
//      }
//      return false;
//   }


}
