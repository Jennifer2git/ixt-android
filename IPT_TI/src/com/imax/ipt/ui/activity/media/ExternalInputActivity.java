package com.imax.ipt.ui.activity.media;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.common.Constants;
import com.imax.ipt.controller.eventbus.handler.push.AudioMuteChangedEvent;
import com.imax.ipt.controller.eventbus.handler.remote.ExecuteRemoteControlHandler;
import com.imax.ipt.ui.activity.LobbyActivity;
import com.imax.ipt.ui.activity.input.DialogZoomFragment;
import com.imax.ipt.ui.activity.input.InputActivity;
import com.imax.ipt.ui.activity.media.MediaRemote.ExecuteRemoteControl;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable.DeviceKind;
import com.imax.ipt.ui.viewmodel.MenuLibraryElement;
import com.imax.iptevent.EventBus;

import java.util.ArrayList;
import java.util.List;

public class ExternalInputActivity extends InputActivity implements OnClickListener{

    private List<MenuLibraryElement> mMenuOptions = new ArrayList<MenuLibraryElement>();

    private Button mbtnZoom;
    private Button mbtnMaster;
    //    protected VerticalSeekBar mSeekBarVolumen;
//    protected SeekBar mSeekBarVolumen;
//    protected ImageButton mBtnMute;
//    protected ImageButton volupButton;
//    protected ImageButton voldownButton;
    protected EventBus mEventBus;
    private boolean mVolumenStart = false;
    private boolean mute = false;
    private static int up = 0;
    private static int down = 0;
    private IPTTextView mTxtTitle;
    private String mTitle;
    private String guid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_external);
        this.addMenuFragment();
//      this.addMenuLibraryFragment(getResources().getString(R.string.external_input), mMenuOptions,520);
        init1();
        this.init();

    }

    protected void init() {

        this.mTitle = getIntent().getStringExtra(MediaRemote.MEDIA_TITLE);
        this.guid = getIntent().getStringExtra(MediaRemote.MEDIA_ID);
        this.mTxtTitle = (IPTTextView) findViewById(R.id.txtTitle);
        this.mTxtTitle.setTypeface(Typeface.createFromAsset(getAssets(), Constants.FONT_LIGHT_PATH));
        this.mTxtTitle.setText(mTitle.toUpperCase());

        addMenuFragment();
        this.mGridLayout = (GridLayout) findViewById(R.id.gridLayout);
        this.setupView(mInputController.getInputs(DeviceKind.Extender));
        MenuLibraryElement menuConsole = new MenuLibraryElement(1, getResources().getString(R.string.console), null, null);
        mMenuOptions.add(menuConsole);


//        this.mSeekBarVolumen = (VerticalSeekBar) findViewById(R.id.seekBarVolume);
//        this.mSeekBarVolumen = (SeekBar) findViewById(R.id.seekBarVolume);
//        this.mSeekBarVolumen.setOnSeekBarChangeListener(this);
//        this.mSeekBarVolumen.setMax(Constants.MAX_VOLUME_VALUE);
//        this.sendRequestGetVolume();

//        this.mBtnMute = (ImageButton) findViewById(R.id.btnVolMute);
//        this.mBtnMute.setOnClickListener(this);
//        this.sendRequestGetMuteVolume();

//        volupButton = (ImageButton) findViewById(R.id.volumeUp);
//        voldownButton = (ImageButton) findViewById(R.id.volumeDown);
//        volupButton.setOnClickListener(this);
//        voldownButton.setOnClickListener(this);

        mbtnZoom = (Button) findViewById(R.id.btnZoom);
        mbtnZoom.setOnClickListener(this);
        mbtnMaster = (Button) findViewById(R.id.btnMenuMaster);
        mbtnMaster.setOnClickListener(this);

    }

    private void init1() {
        mEventBus = IPT.getInstance().getEventBus();
        mEventBus.register(this);
    }

    /**
     * @param
     */
//    public static void fire(Context mContext) {
    public static void fire(Context context, String guid, String title, int inputId) {

        Intent intent = new Intent(context, ExternalInputActivity.class);
        intent.putExtra(MediaRemote.MEDIA_ID, guid);
        intent.putExtra(MediaRemote.MEDIA_TITLE, title);
//        intent.putExtra(INPUT_ID, inputId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
    }

    /**
     * #SERVER INTERACTION
     */
//    protected void sendRequestGetVolume() {
//        mEventBus.post(new GetVolumeHandler().getRequest());
//    }
//
//    /**
//     *
//     */
//    protected void sendRequestGetMuteVolume() {
//        mEventBus.post(new GetMuteHandler().getRequest());
//    }
//
//    /**
//     * @param progress
//     */
//    private void setVolumen(int progress) {
//        mEventBus.post(new SetVolumenHandler(progress).getRequest());
//    }

//    /**
//     * @param getMuteResponseEvent
//     */
//    public void onEvent(GetMuteHandler getMuteResponseEvent) {
//        mute = getMuteResponseEvent.isMute();
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
////                mBtnMute.setPressed(mute);
//                if(mute){
////                    mSeekBarVolumen.setEnabled(false);
//                }
//
//            }
//        });
//    }

    /**
     * @param audioMuteChangedEvent
     */
    public void onEvent(AudioMuteChangedEvent audioMuteChangedEvent) {
        final boolean mute = audioMuteChangedEvent.ismMute();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                mBtnMute.setPressed(mute);
            }
        });
    }

    /**
     * @param
     */
//    public void onEvent(GetVolumeHandler getVolumeEventResponse) {
//        final double volume = getVolumeEventResponse.getVolume();
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mSeekBarVolumen.setProgress((int) volume);
//            }
//        });
//    }

//    @Override
//    public void onProgressChanged(VerticalSeekBar VerticalSeekBar, int progress,
//                                  boolean fromUser) {
//        if (!mVolumenStart) {
//            mVolumenStart = true;
//            return;
//        }
//
//
//        if (fromUser) {
//            if (progress > Constants.SCROLL_MAX_VOLUME_VALUE) {
//                mSeekBarVolumen.setProgress(Constants.SCROLL_MAX_VOLUME_VALUE);
//                setVolumen(Constants.SCROLL_MAX_VOLUME_VALUE);
//                return;
//            }
//        } else {
//
//            mSeekBarVolumen.setProgress(progress);
//            return;
//        }
//
//
//        if (mute) {
//            mute = false;
//            mBtnMute.setBackground(getResources().getDrawable(R.drawable.ipt_gui_asset_vol_mute_inactive));
//            int progressOld = mSeekBarVolumen.getProgress();
//            int offset = mSeekBarVolumen.getThumbOffset();
//            mSeekBarVolumen.setThumb(getResources().getDrawable(R.drawable.ipt_gui_asset_vol_knob_active_down));
//            mSeekBarVolumen.setThumbOffset(offset);
//            mSeekBarVolumen.setProgress(progressOld);
//
//        }
//
//        setVolumen(progress);
//    }

//    @Override
//    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//        if (!mVolumenStart) {
//            mVolumenStart = true;
//            return;
//        }
//        if(!fromUser){
//            mSeekBarVolumen.setProgress(progress);
//            return;
//        }else{
//            if (progress > Constants.SCROLL_MAX_VOLUME_VALUE) {
//                mSeekBarVolumen.setProgress(Constants.SCROLL_MAX_VOLUME_VALUE);
//                progress = Constants.SCROLL_MAX_VOLUME_VALUE;
//            }
//            setVolumen(progress);
//        }
//
//    }



//    @Override
//    public void onStartTrackingTouch(VerticalSeekBar VerticalSeekBar) {
//
//    }
//
//    @Override
//    public void onStopTrackingTouch(VerticalSeekBar VerticalSeekBar) {
//
//    }

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
//                mSeekBarVolumen.setThumbOffset(offset1);
//
//                if (progressOld1 + 1 > Constants.MAX_VOLUME_VALUE) {
//                    progressOld1 = Constants.MAX_VOLUME_VALUE;
//                    mSeekBarVolumen.setProgress(progressOld1);
//                    setVolumen(-1);
//                    return;
//
//                } else {
//
//                    if (up == 0) {
//                        up = 1;
//                        mSeekBarVolumen.setProgress(progressOld1);
//                        setVolumen(-1);
//                        return;
//
//                    } else {
//                        up = 0;
////			  			touch = true;
//                    }
//
//                    progressOld1 = progressOld1 + 1;
//
//                }
//
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
////			  			touch = true;
//                    }
//
//                    progressOld2 = progressOld2 - 1;
//                }
//
//                mSeekBarVolumen.setProgress(progressOld2);
//
//                setVolumen(-2);
//                break;
//
//
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
//
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
//
//                }
//                break;
            case R.id.btnZoom:
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.Zoom.toString()).getRequest());
                showDialogZoom();
                break;
            case R.id.btnMenuMaster:
                LobbyActivity.fire(this);
        }
    }

    private void showDialogZoom() {
        FragmentManager fm = getSupportFragmentManager();
        DialogZoomFragment mDialogZoomFragment = DialogZoomFragment.newInstance(mInputController.getInputs(DeviceKind.Extender).get(0).getId());
        mDialogZoomFragment.show(fm, "fragment_dialog_zoom");
    }
}
