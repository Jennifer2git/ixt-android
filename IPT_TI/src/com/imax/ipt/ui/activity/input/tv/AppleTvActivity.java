package com.imax.ipt.ui.activity.input.tv;

import com.imax.ipt.R;
import com.imax.ipt.controller.remote.RemoteController;
import com.imax.ipt.controller.remote.VolumeController;
import com.imax.ipt.controller.remote.VolumeView;
import com.imax.ipt.ui.activity.BaseActivity;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.widget.verticalseekbar.VerticalSeekBar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class AppleTvActivity extends BaseActivity implements VolumeView {
    private VolumeController mVolumeController;
    private RemoteController mRemoteController;
    private IPTTextView txtTitle;
    private String mTitle;
    private int inputId;
    public static final String TITLE = "TITLE";
    public static final String INPUT_ID = "INPUT_ID";
    private static int[] onClickSources = {R.id.btnBack, R.id.btnMultiview,
            R.id.txtTvOk, R.id.btnUp, R.id.btnRigth, R.id.btnLeft,
            R.id.btnDown, R.id.btnMenu, R.id.btnPlayPause};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_apple);

        this.inputId = getIntent().getIntExtra(INPUT_ID, 0);

        mVolumeController = new VolumeController(this);
        mRemoteController = new RemoteController(this, inputId);

        for (int id : onClickSources) {
            View view = (View) findViewById(id);
            if (view != null) {
                view.setOnClickListener(mRemoteController);
            }
        }

        this.txtTitle = (IPTTextView) findViewById(R.id.txtTitle);

        this.addMenuFragment();

        this.mTitle = getIntent().getStringExtra(TITLE);
        this.txtTitle.setText(getResources().getString(R.string.now_playing)
                + " " + mTitle);

        findViewById(R.id.btnBack).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static void fire(Context context, int inputId, String title) {
        Intent intent = new Intent(context, AppleTvActivity.class);
        intent.putExtra(INPUT_ID, inputId);
        intent.putExtra(TITLE, title);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    protected void onDestroy() {
        super.onDestroy();
        mVolumeController.destroy();
        mRemoteController.destroy();
    }

    @Override
    public VerticalSeekBar getSeekBar() {
        return (VerticalSeekBar) findViewById(R.id.seekBarVolume);
    }

    @Override
    public ImageButton getMuteButton() {
        return (ImageButton) findViewById(R.id.btnVolMute);
    }
}
