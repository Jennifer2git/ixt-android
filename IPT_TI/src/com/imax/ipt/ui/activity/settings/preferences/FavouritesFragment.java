package com.imax.ipt.ui.activity.settings.preferences;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.imax.ipt.R;
import com.imax.ipt.controller.settings.PreferencesController;
import com.imax.ipt.ui.util.VibrateUtil;


public class FavouritesFragment extends Fragment implements OnClickListener {
    private View mFavouritesLayout;

    private PreferencesController preferencesController;

    private ImageButton btnClearFavoriteMovies;
    private ImageButton btnClearFavoriteMusic;
    private Button btnClearTvChannels;

    private TextView tvFavourite;
    private TextView tvMine;
    private TextView tvLable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getResources().getConfiguration().locale.getCountry().equals("CN")) {

            mFavouritesLayout = inflater.inflate(R.layout.fragment_pref_favourites_cn, null);
        } else {

            mFavouritesLayout = inflater.inflate(R.layout.fragment_pref_favourites_en, null);
        }

        btnClearFavoriteMovies = (ImageButton) mFavouritesLayout.findViewById(R.id.btnClearFavoriteMovies);
        btnClearFavoriteMovies.setOnClickListener(this);

        btnClearFavoriteMusic = (ImageButton) mFavouritesLayout.findViewById(R.id.btnClearFavoriteMusic);
        btnClearFavoriteMusic.setOnClickListener(this);

        btnClearTvChannels = (Button) mFavouritesLayout.findViewById(R.id.btnClearTvChannels);
        btnClearTvChannels.setOnClickListener(this);

        tvLable = (TextView) mFavouritesLayout.findViewById(R.id.lable_name);

        tvFavourite = (TextView) mFavouritesLayout.findViewById(R.id.lable_favourite);
        tvFavourite.setOnClickListener(this);
        tvMine = (TextView) mFavouritesLayout.findViewById(R.id.lable_mine);
        tvMine.setOnClickListener(this);

        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean mode = sp.getBoolean("default_favourite", true);
        if (mode) {
            tvLable.setText(tvFavourite.getText());
        } else {
            tvLable.setText(tvMine.getText());
        }

        return mFavouritesLayout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setPreferencesController(PreferencesController preferencesController) {
        this.preferencesController = preferencesController;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnClearFavoriteMovies:
                preferencesController.ClearMovieFavorites();
                break;
            case R.id.btnClearFavoriteMusic:
                preferencesController.ClearMusicAlbumFavorites();
                break;
            case R.id.btnClearTvChannels:
                preferencesController.ClearTvChannelFavorites();
                break;
            case R.id.lable_favourite:
                tvLable.setText(tvFavourite.getText());
                final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                Editor editor = sp.edit();
                editor.putBoolean("default_favourite", true);
                editor.apply();
                break;
            case R.id.lable_mine:
                tvLable.setText(tvMine.getText());
                final SharedPreferences sp1 = PreferenceManager.getDefaultSharedPreferences(getActivity());
                Editor editor1 = sp1.edit();
                editor1.putBoolean("default_favourite", false);
                editor1.apply();
                break;
            default:
                //do nothing.
        }

        VibrateUtil.vibrate(getActivity(), 100);

    }

}
