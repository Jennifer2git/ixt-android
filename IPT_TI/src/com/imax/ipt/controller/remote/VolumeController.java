package com.imax.ipt.controller.remote;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.common.Constants;
import com.imax.ipt.controller.eventbus.handler.push.AudioMuteChangedEvent;
import com.imax.ipt.controller.eventbus.handler.remote.GetMuteHandler;
import com.imax.ipt.controller.eventbus.handler.remote.GetVolumeHandler;
import com.imax.ipt.controller.eventbus.handler.remote.SetMuteHandler;
import com.imax.ipt.controller.eventbus.handler.remote.SetVolumenHandler;
import com.imax.ipt.ui.widget.verticalseekbar.VerticalSeekBar;
import com.imax.iptevent.EventBus;

public class VolumeController implements OnSeekBarChangeListener, VerticalSeekBar.OnSeekBarChangeListener,
        OnClickListener {
    private VolumeView mVolumeView;
    private EventBus mEventBus;
    private boolean mute = false;
    private VerticalSeekBar mSeekBarVolumen;
    private ImageButton mBtnMute;
    private boolean mVolumenStart = false;

    public VolumeController(VolumeView volumeView) {
        mVolumeView = volumeView;
        mSeekBarVolumen = volumeView.getSeekBar();
        mBtnMute = volumeView.getMuteButton();

        mEventBus = IPT.getInstance().getEventBus();
        mEventBus.register(this);

        mSeekBarVolumen.setOnSeekBarChangeListener(this);
		mSeekBarVolumen.setMax(Constants.MAX_VOLUME_VALUE);// 6db
        Log.d("VolumeController", "mSeekBarVolumen getMax:" + mSeekBarVolumen.getMax());
        this.sendRequestGetVolume();

        mBtnMute.setOnClickListener(this);
        this.sendRequestGetMuteVolume();
    }

    public void destroy() {
        mEventBus.unregister(this);
    }

    protected void sendRequestGetVolume() {
        mEventBus.post(new GetVolumeHandler().getRequest());
    }

    protected void sendRequestGetMuteVolume() {
        mEventBus.post(new GetMuteHandler().getRequest());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnVolMute:
                if (!mute) {
                    mEventBus.post(new SetMuteHandler(true).getRequest());
                    mute = true;
                    mBtnMute.setBackground(mVolumeView.getResources().getDrawable(
                            R.drawable.ipt_gui_asset_vol_mute_active));
                    int progress = mSeekBarVolumen.getProgress();
                    int offset = mSeekBarVolumen.getThumbOffset();
                    mSeekBarVolumen.setThumb(mVolumeView.getResources()
                            .getDrawable(
                                    R.drawable.ipt_gui_asset_vol_knob_muted_down));
                    mSeekBarVolumen.setThumbOffset(offset);
                    mSeekBarVolumen.setProgress(progress);
                    // mSeekBarVolumen.invalidate();
                } else {
                    mute = false;
                    mEventBus.post(new SetMuteHandler(false).getRequest());
                    mBtnMute.setBackground(mVolumeView.getResources().getDrawable(
                            R.drawable.ipt_gui_asset_vol_mute_inactive));
                    int progress = mSeekBarVolumen.getProgress();
                    int offset = mSeekBarVolumen.getThumbOffset();
                    mSeekBarVolumen.setThumb(mVolumeView.getResources()
                            .getDrawable(
                                    R.drawable.ipt_gui_asset_vol_knob_active_down));
                    mSeekBarVolumen.setThumbOffset(offset);
                    mSeekBarVolumen.setProgress(progress);
                    // mSeekBarVolumen.invalidate();
                }
                break;
        }
    }

    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        if (!mVolumenStart) {
            mVolumenStart = true;
            return;
        }
        if (mute) {
            mute = false;
            mBtnMute.setBackground(mVolumeView.getResources().getDrawable(R.drawable.ipt_gui_asset_vol_mute_inactive));
            int progressOld = mSeekBarVolumen.getProgress();
            int offset = mSeekBarVolumen.getThumbOffset();
            mSeekBarVolumen.setThumb(mVolumeView.getResources().getDrawable(R.drawable.ipt_gui_asset_vol_knob_active_down));
            mSeekBarVolumen.setThumbOffset(offset);
            mSeekBarVolumen.setProgress(progressOld);
        }
        setVolumen(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar arg0) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar arg0) {
    }

    public void onEventMainThread(GetMuteHandler getMuteResponseEvent) {
        final boolean mute = getMuteResponseEvent.isMute();
        mBtnMute.setPressed(mute);
    }

    public void onEventMainThread(AudioMuteChangedEvent audioMuteChangedEvent) {
        final boolean mute = audioMuteChangedEvent.ismMute();
        mBtnMute.setPressed(mute);
    }

    public void onEventMainThread(GetVolumeHandler getVolumeEventResponse) {
        final double volume = getVolumeEventResponse.getVolume();
        mSeekBarVolumen.setProgress((int) volume);
    }

    private void setVolumen(int progress) {
        mEventBus.post(new SetVolumenHandler(progress).getRequest());
    }

    @Override
    public void onProgressChanged(VerticalSeekBar VerticalSeekBar,
                                  int progress, boolean fromUser) {
        // TODO Auto-generated method stub
        if (!mVolumenStart) {
            mVolumenStart = true;
            return;
        }
        if (mute) {
            mute = false;
            mBtnMute.setBackground(mVolumeView.getResources().getDrawable(R.drawable.ipt_gui_asset_vol_mute_inactive));
            int progressOld = mSeekBarVolumen.getProgress();
            int offset = mSeekBarVolumen.getThumbOffset();
            mSeekBarVolumen.setThumb(mVolumeView.getResources().getDrawable(R.drawable.ipt_gui_asset_vol_knob_active_down));
            mSeekBarVolumen.setThumbOffset(offset);
            mSeekBarVolumen.setProgress(progressOld);
        }
        setVolumen(progress);
    }

    @Override
    public void onStartTrackingTouch(VerticalSeekBar VerticalSeekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(VerticalSeekBar VerticalSeekBar) {
        // TODO Auto-generated method stub

    }
}
