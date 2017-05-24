package com.imax.ipt.ui.activity.input;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.push.AudioMuteChangedEvent;
import com.imax.ipt.controller.eventbus.handler.remote.GetMuteHandler;
import com.imax.ipt.controller.eventbus.handler.remote.GetVolumeHandler;
import com.imax.ipt.controller.eventbus.handler.remote.SetMuteHandler;
import com.imax.ipt.controller.eventbus.handler.remote.SetVolumenHandler;
import com.imax.ipt.ui.activity.media.MediaRemote;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.widget.verticalseekbar.VerticalSeekBar;

public class RemoteControlUnavailalbleActivity extends MediaRemote implements OnClickListener {
    public static final String TITLE = "TITLE";
    public static final String INPUT_ID = "INPUT_ID";

    private VerticalSeekBar mSeekBarVolumen;
    private ImageButton mBtnMute;
    private IPTTextView txtTitle;

    private String mTitle;

    private boolean mute = false;
    private boolean mVolumenStart = false;

//   private EventBus mEventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_control_unavailable);

//      mEventBus = IPT.getInstance().getEventBus();
//      mEventBus.register(this);


        this.txtTitle = (IPTTextView) findViewById(R.id.txtTitle);

        this.mBtnMultiview = (ImageButton) findViewById(R.id.btnMultiview);
        this.mBtnMultiview.setOnClickListener(this);

        mBtnVideoMode = (Button) findViewById(R.id.btnVideoMode);
        mBtnVideoMode.setOnClickListener(this);

        this.addMenuFragment();
        this.init();

        findViewById(R.id.btnBack).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                // MovieLibraryActivity.fireToFront(TVRemoteActivity.this);
            }
        });

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

//      mEventBus.unregister(this);
    }

    /**
     * @param context
     */
    public static void fire(Context context, int inputId, String title) {
        Intent intent = new Intent(context, RemoteControlUnavailalbleActivity.class);
        intent.putExtra(INPUT_ID, inputId);
        intent.putExtra(TITLE, title);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void init() {
        this.inputId = getIntent().getIntExtra(INPUT_ID, 0);
        this.mTitle = getIntent().getStringExtra(TITLE);
        this.txtTitle.setText(getResources().getString(R.string.now_playing) + " " + mTitle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnVolMute:
                if (!mute) {
                    mEventBus.post(new SetMuteHandler(true).getRequest());
                    mute = true;
                    mBtnMute.setBackground(getResources().getDrawable(R.drawable.ipt_gui_asset_vol_mute_active));
                } else {
                    mute = false;
                    mEventBus.post(new SetMuteHandler(false).getRequest());
                    mBtnMute.setBackground(getResources().getDrawable(R.drawable.ipt_gui_asset_vol_mute_inactive));
                }
                break;

            default:
                super.onClick(v);
                break;
        }
    }


    private void setVolume(int progress) {
        mEventBus.post(new SetVolumenHandler(progress).getRequest());
    }


    protected void sendRequestGetVolume() {
        mEventBus.post(new GetVolumeHandler().getRequest());
    }

    protected void sendRequestGetMuteVolume() {
        mEventBus.post(new GetMuteHandler().getRequest());
    }

    /**
     * @param getMuteResponseEvent
     */
    public void onEvent(GetMuteHandler getMuteResponseEvent) {
        final boolean mute = getMuteResponseEvent.isMute();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBtnMute.setPressed(mute);
            }
        });
    }

    /**
     * @param
     */
    public void onEvent(AudioMuteChangedEvent audioMuteChangedEvent) {
        final boolean mute = audioMuteChangedEvent.ismMute();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBtnMute.setPressed(mute);
            }
        });
    }

    /**
     * @param getVolumeEventResponse
     */
    public void onEvent(GetVolumeHandler getVolumeEventResponse) {
        final double volume = getVolumeEventResponse.getVolume();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSeekBarVolumen.setProgress((int) volume);
            }
        });
    }
}
