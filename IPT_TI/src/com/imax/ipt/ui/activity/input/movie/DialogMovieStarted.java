package com.imax.ipt.ui.activity.input.movie;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import com.imax.ipt.R;

/**
 * Created by yanli on 2015/10/12.
 */
public class DialogMovieStarted extends ProgressDialog {

    public DialogMovieStarted(Context context, int theme){
        super(context, theme);
    }
    public DialogMovieStarted(Context context){
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_movie_started);
    }

    @Override
    public void setProgressStyle(int style) {
        super.setProgressStyle(style);
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
    }
}
