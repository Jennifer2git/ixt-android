package com.imax.ipt.controller.remote;

import android.content.res.Resources;
import android.widget.ImageButton;

import com.imax.ipt.ui.widget.verticalseekbar.VerticalSeekBar;

public interface VolumeView {
    public VerticalSeekBar getSeekBar();

    public ImageButton getMuteButton();

    public Resources getResources();
}
