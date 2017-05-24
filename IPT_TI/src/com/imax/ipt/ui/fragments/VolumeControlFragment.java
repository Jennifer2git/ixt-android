package com.imax.ipt.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.common.Constants;
import com.imax.ipt.controller.GlobalController;
import com.imax.ipt.controller.eventbus.handler.push.AudioMuteChangedEvent;
import com.imax.ipt.controller.eventbus.handler.remote.GetMuteHandler;
import com.imax.ipt.controller.eventbus.handler.remote.GetVolumeHandler;
import com.imax.ipt.controller.eventbus.handler.remote.SetMuteHandler;
import com.imax.ipt.ui.activity.LobbyActivity;
import com.imax.ipt.ui.util.VibrateUtil;
import com.imax.iptevent.EventBus;

public class VolumeControlFragment extends Fragment implements SeekBar.OnSeekBarChangeListener, OnClickListener {
    public static final String TAG = "VolumeControlFragment";

    private ImageButton mBtnMute;
    private ImageButton mVolumeUpBtn;
    private ImageButton mVolumeDownBtn;
    private SeekBar mSeekBarVolumen;
    private TextView txtVolumeIndicator;
    private boolean mute = false;

    private boolean mVolumenStart = false;
    protected EventBus mEventBus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mVolumeControl = inflater.inflate(R.layout.fragment_volume_control, null);
        this.init(mVolumeControl);
        txtVolumeIndicator = (TextView) mVolumeControl.findViewById(R.id.txtVolumeIndicator);

        return mVolumeControl;
    }


    private void init(View view) {
        mEventBus = IPT.getInstance().getEventBus();
        mEventBus.register(this);

        mVolumeUpBtn = (ImageButton) view.findViewById(R.id.volumeUp);
        mVolumeDownBtn = (ImageButton) view.findViewById(R.id.volumeDown);
        mBtnMute = (ImageButton) view.findViewById(R.id.btnVolMute);
        mSeekBarVolumen = (SeekBar) view.findViewById(R.id.seekBarVolume);
        mVolumeUpBtn.setOnClickListener(this);
        mVolumeDownBtn.setOnClickListener(this);
        mSeekBarVolumen.setOnSeekBarChangeListener(this);

        mEventBus.post(new GetVolumeHandler().getRequest());
        mEventBus.post(new GetMuteHandler().getRequest());

        this.mSeekBarVolumen.setMax(Constants.MAX_VOLUME_VALUE);
        mBtnMute.setOnClickListener(this);

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

        LobbyActivity.mVolume = progress;

//        setIndicator(progress); // NOTE: remove show volume indicator.

        if (!fromUser) {
            mSeekBarVolumen.setProgress(progress);
            return;
        } else {
            if (progress > Constants.SCROLL_MAX_VOLUME_VALUE) {
                mSeekBarVolumen.setProgress(Constants.SCROLL_MAX_VOLUME_VALUE);
                progress = Constants.SCROLL_MAX_VOLUME_VALUE;
            }
            GlobalController.getInstance().setVolume(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        GlobalController.getInstance().setVolume(seekBar.getProgress());
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
        LobbyActivity.mMute = mute;

        if (mute) {
            mBtnMute.setImageDrawable(getResources().getDrawable(R.drawable.catalogue_mute_volume_active_icn));
            mSeekBarVolumen.setEnabled(false);
        } else {
            mBtnMute.setImageDrawable(getResources().getDrawable(R.drawable.catalogue_mute_volume_icn));
            mSeekBarVolumen.setEnabled(true);
        }
    }

    public void onEventMainThread(GetVolumeHandler getVolumeEventResponse) {
        final double volume = getVolumeEventResponse.getVolume();
        mSeekBarVolumen.setProgress((int) volume);
        LobbyActivity.mVolume = (int) volume;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.volumeUp:
                mBtnMute.setImageDrawable(getResources().getDrawable(R.drawable.catalogue_mute_volume_icn));
                mute = false;
                mSeekBarVolumen.setEnabled(true);

                int progressOld1 = mSeekBarVolumen.getProgress();
                int offset1 = mSeekBarVolumen.getThumbOffset();
                mSeekBarVolumen.setThumbOffset(offset1);

                if (progressOld1 + 1 > Constants.MAX_VOLUME_VALUE) {
                    progressOld1 = Constants.MAX_VOLUME_VALUE;
                } else {
                    progressOld1 = progressOld1 + 1;
                }

                mSeekBarVolumen.setProgress(progressOld1);
                GlobalController.getInstance().setVolume(-1);
                VibrateUtil.vibrate(getActivity(), 100);
                LobbyActivity.mVolume = progressOld1;
                LobbyActivity.mMute = mute;
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
                } else {
                    progressOld2 = progressOld2 - 1;
                }
                mSeekBarVolumen.setProgress(progressOld2);
                GlobalController.getInstance().setVolume(-2);
                VibrateUtil.vibrate(getActivity(), 100);
                LobbyActivity.mVolume = progressOld2;
                LobbyActivity.mMute = mute;

                break;
            case R.id.btnVolMute:
                if (!mute) {
                    mute = true;
                    mBtnMute.setImageDrawable(getResources().getDrawable(R.drawable.catalogue_mute_volume_active_icn));
                    mSeekBarVolumen.invalidate();
                } else {
                    mute = false;
                    mBtnMute.setImageDrawable(getResources().getDrawable(R.drawable.catalogue_mute_volume_icn));
                }
                    mEventBus.post(new SetMuteHandler(mute).getRequest());
                    mSeekBarVolumen.setEnabled(!mute);

                LobbyActivity.mMute = mute;
                break;
        }

    }


    private void setIndicator(int progress) {
        String volume = "";
        String signal = "-";
        int mod = 0;
        if (0 < progress && progress < 160) {
            progress = 160 - progress;

        } else if (160 < progress && progress <= Constants.MAX_VOLUME_VALUE) {
            progress = progress - 160;
            signal = "+";

        } else if (progress == 160) {
            progress = 0;
            signal = " ";
        } else if (progress == 0) {
            txtVolumeIndicator.setText("---.-" + " dB");
            return;
        }
        if ((progress % 2) == 1) {
            progress = progress / 2;
            mod = 5;
        } else {
            progress = progress / 2;
            mod = 0;
        }
        volume = signal + progress + "." + mod;
        txtVolumeIndicator.setText(volume + " dB");
    }

}
