package com.imax.ipt.ui.activity.input.music;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.music.DisplayMusicAlbumDetailsOnScreenHandler;
import com.imax.ipt.controller.eventbus.handler.music.GetMusicAlbumHandler;
import com.imax.ipt.controller.eventbus.handler.music.PlayMusicAlbumHandler;
import com.imax.ipt.controller.eventbus.handler.music.PlayMusicTrackHandler;
import com.imax.ipt.controller.eventbus.handler.music.SetMusicAlbumFavoriteStatusHandler;
import com.imax.ipt.model.MusicAlbum;
import com.imax.ipt.model.MusicTrack;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.util.ImageUtil;
import com.imax.ipt.ui.viewmodel.RemoteControlUtil;
import com.imax.iptevent.EventBus;

public class DialogMusicFragment extends DialogFragment implements OnClickListener {
    public static String MUSIC_ALBUM_ID = "musicAlbumId";

    private ImageButton mBtnPlayMedia;
    private ImageButton mBtnClose;
    private ImageView mImgPoster;
    private ImageButton mBtnFavorite;

    private IPTTextView txtMediaTitle;
    private IPTTextView txtArtist;
    //   private IPTTextView txtYear;
    private IPTTextView txtGenre;
    private LinearLayout layoutTracks;

    private LoaderImage loaderImage;
    private EventBus mEventBus;

    private MusicAlbum mMusicAlbum;

    @Override
    public View onCreateView(LayoutInflater mInflater, ViewGroup container, Bundle savedInstanceState) {
        View mViewDialogFragment = mInflater.inflate(R.layout.fragment_dialog_music_description, null);

        Dialog d = getDialog();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());
        lp.width = 1618;
        lp.height = 1300;
        lp.x = 200;
        lp.y = -180;
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        d.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        d.show();
        d.getWindow().setAttributes(lp);

        this.init(mViewDialogFragment);
        return mViewDialogFragment;
    }

    private void init(View view) {
        this.mEventBus = IPT.getInstance().getEventBus();
        this.mEventBus.register(this);
        txtMediaTitle = (IPTTextView) view.findViewById(R.id.txtMediaTitle);
        txtArtist = (IPTTextView) view.findViewById(R.id.txtArtist);
        txtGenre = (IPTTextView) view.findViewById(R.id.txtGenre);
//      txtYear = (IPTTextView) view.findViewById(R.id.txtYear);
        layoutTracks = (LinearLayout) view.findViewById(R.id.layouTracks);

        mImgPoster = (ImageView) view.findViewById(R.id.imageHighResolution);
        mBtnPlayMedia = (ImageButton) view.findViewById(R.id.btnPlayMedia);
        mBtnPlayMedia.setOnClickListener(this);
        mBtnClose = (ImageButton) view.findViewById(R.id.btnClose);
        mBtnClose.setOnClickListener(this);

        mBtnFavorite = (ImageButton) view.findViewById(R.id.btnFavourite);
        mBtnFavorite.setOnClickListener(this);

        String musicAlbumId = getArguments().getString(MUSIC_ALBUM_ID);
        this.getMusicAlbum(musicAlbumId);
    }

    /**
     * @param getMovieResponseEvent
     */
    public void build(MusicAlbum musicAlbum) {
        txtMediaTitle.setText(musicAlbum.getName());
        loaderImage = new LoaderImage();
//      loaderImage.execute(musicAlbum.getCoverArtPath());
        loaderImage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, musicAlbum.getCoverArtPath());
        mBtnFavorite.setSelected(musicAlbum.isFavorite());
        mBtnFavorite.setPressed(musicAlbum.isFavorite());
        buildTracks(musicAlbum.getTracks());
    }

    private void buildTracks(MusicTrack[] tracks) {
        String artist = "";
        String genre = "";
        if (tracks != null) {
            //added by watershao
            if (layoutTracks != null) {
                layoutTracks.removeAllViews();
            }
            int i = 1;
            for (MusicTrack musicTrack : tracks) {
                artist = musicTrack.getArtist().getFirstName() + " " + musicTrack.getArtist().getLastName();
                genre = musicTrack.getGenre().getName();
                IPTTextView textViewmusicTrack = new IPTTextView(getActivity());
                textViewmusicTrack.setText(i + ".  " + musicTrack.getTitle());
                textViewmusicTrack.setTextSize(16);
                textViewmusicTrack.setTextColor(Color.WHITE);
                textViewmusicTrack.setSingleLine(true);
                textViewmusicTrack.setEllipsize(TruncateAt.END);
                textViewmusicTrack.setTag(musicTrack);
                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                llp.setMargins(0, 40, 0, 0); // llp.setMargins(left, top, right,
                // bottom);
                textViewmusicTrack.setLayoutParams(llp);
                textViewmusicTrack.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MusicTrack musicTrack = (MusicTrack) v.getTag();
                        mEventBus.post(new PlayMusicTrackHandler(musicTrack.getId()).getRequest());

                        RemoteControlUtil.openMusicRemoteControl(getActivity(), mMusicAlbum, musicTrack);
                    }
                });
                layoutTracks.addView(textViewmusicTrack);
                i++;
            }
            txtArtist.setText(artist);
            txtGenre.setText(genre);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPlayMedia:
                if (mMusicAlbum != null && mMusicAlbum.getTracks() != null && mMusicAlbum.getTracks().length > 0) {
//            mEventBus.post(new PlayMusicTrackHandler(mMusicAlbum.getTracks()[0].getId()).getRequest());
                    mEventBus.post(new PlayMusicAlbumHandler(mMusicAlbum.getId()).getRequest());

                    RemoteControlUtil.openMusicRemoteControl(getActivity(), mMusicAlbum, mMusicAlbum.getTracks()[0]);
                }
                break;

            case R.id.btnClose:
                this.dismiss();
                if (loaderImage != null) {
                    loaderImage.cancel(true);
                }
                break;

            case R.id.btnFavourite:
                if (mMusicAlbum != null) {

                    mEventBus.post(new SetMusicAlbumFavoriteStatusHandler(mMusicAlbum.getId(), !mMusicAlbum.isFavorite()).getRequest());
                }
                break;

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loaderImage != null) {
            loaderImage.cancel(true);
        }
        mEventBus.unregister(this);
    }

    /**
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

            if (result != null && isAdded()) {
                BitmapDrawable background = new BitmapDrawable(getResources(), result);
                mImgPoster.setImageDrawable(background);
            }
        }

    }

    /**
     * Server Interaction
     */

    private void getMusicAlbum(String musicAlbumId) {
        this.mEventBus.post(new GetMusicAlbumHandler(musicAlbumId, true).getRequest());
    }

    /**
     * @param getMusicAlbumHandler
     */
    public void onEvent(GetMusicAlbumHandler getMusicAlbumHandler) {
        this.mMusicAlbum = getMusicAlbumHandler.getMusicAlbum();
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                build(mMusicAlbum);
            }
        });

        // Display music album details on theatre screen
        mEventBus.post(new DisplayMusicAlbumDetailsOnScreenHandler(getMusicAlbumHandler.getMusicAlbum().getId()).getRequest());
    }

    public void onEvent(SetMusicAlbumFavoriteStatusHandler setMusicAlbumFavoriteStatusHandler) {
        mMusicAlbum.setFavorite(setMusicAlbumFavoriteStatusHandler.isFavorited());
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mBtnFavorite.setSelected(mMusicAlbum.isFavorite());
                mBtnFavorite.setPressed(mMusicAlbum.isFavorite());
            }
        });
    }
}
