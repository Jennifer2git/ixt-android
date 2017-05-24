package com.imax.ipt.ui.activity.media.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imax.ipt.R;
import com.imax.ipt.model.Movie;

public class MetaFragment extends Fragment {

    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private Movie mMovie;


    /**
     * @param message
     * @return
     */
    public static final MetaFragment newInstance(String message) {
        MetaFragment f = new MetaFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, message);
        f.setArguments(bdl);
        return f;
    }

    /**
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String message = getArguments().getString(EXTRA_MESSAGE);
        View v = inflater.inflate(R.layout.fragment_add_meta_library, container, false);
        // TextView messageTextView = (TextView) v.findViewById(R.id.textView);
        //messageTextView.setText(message);
        return v;
    }

    /**
     * Getters and Setters
     */
    public Movie getmMovie() {
        return mMovie;
    }

    public void setmMovie(Movie mMovie) {
        this.mMovie = mMovie;
    }


}
