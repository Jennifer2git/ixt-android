package com.imax.ipt.ui.activity.input.movie;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.common.Constants;
import com.imax.ipt.controller.eventbus.handler.movie.DisplayMovieDetailsOnScreenHandler;
import com.imax.ipt.controller.eventbus.handler.movie.GetMovieHandler;
import com.imax.ipt.controller.eventbus.handler.movie.PlayMovieHandler;
import com.imax.ipt.controller.eventbus.handler.movie.UpdateMovieHandler;
import com.imax.ipt.controller.inputs.InputController;
import com.imax.ipt.model.Genre;
import com.imax.ipt.model.Input;
import com.imax.ipt.model.Movie;
import com.imax.ipt.model.Person;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.util.DisplayUtil;
import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable;
import com.imax.ipt.ui.util.ImageUtil;
import com.imax.ipt.ui.util.Utils;
import com.imax.iptevent.EventBus;

import java.util.Map;
import java.util.Vector;

public class DialogMovieFragment extends DialogFragment implements OnClickListener {
    public static String TAG = "DialogMovieFragment";

    public static String MOVIE_ID = "movie_id";

    private ImageButton mBtnPlayMedia;
    private ImageButton mBtnFavorite;
    private ImageButton mBtnClose;
    private ImageView mImgPoster;

    private IPTTextView txtPlayMovie;
    private IPTTextView txtFavorite;
    private IPTTextView txtMediaTitle;
    private IPTTextView txtDirectorTitle;
    private IPTTextView txtActorTitle;
    private IPTTextView txtActors;
    private IPTTextView txtDirectors;
    private IPTTextView txtGenre;
    private IPTTextView txtSynopsis;
    private IPTTextView txtDuration;
    private IPTTextView txtRated;
    private IPTTextView txtYear;

//    private LinearLayout layoutActors;
//    private LinearLayout layoutDirectors;

    private LoaderImage loaderImage;
    private EventBus eventBus;

    private String guid;
    private Movie mMovie;

    private InputController mInputController;
    private Typeface face;

    @Override
    public View onCreateView(LayoutInflater mInflater, ViewGroup container, Bundle savedInstanceState) {
        View mViewDialogFragment = mInflater.inflate(R.layout.fragment_dialog_movie_description, null);

        Dialog d = getDialog();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());
//        lp.width = 1618;
//        lp.height = 1300;
//        lp.x = -200;
//        lp.y = -180;

//        lp.width = 810;//(380,260) -(1190,920) =(810,660)
        lp.width = DisplayUtil.dip2px(mInflater.getContext(),600);
        lp.height = DisplayUtil.dip2px(mInflater.getContext(),450);
        d.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
//        d.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.color_catalogue_bg));
        d.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.catalogue_details_card_bg2));
//        d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        d.show();
        d.getWindow().setAttributes(lp);
        setStyle(STYLE_NO_FRAME, 0);

        this.init(mViewDialogFragment);
        return mViewDialogFragment;
    }

    private void init(View view) {
        this.eventBus = IPT.getInstance().getEventBus();
        this.eventBus.register(this);
        this.sendGetMovieEvent();

        txtPlayMovie = (IPTTextView) view.findViewById(R.id.txtPlayMovie);
        txtFavorite = (IPTTextView) view.findViewById(R.id.txtFavourite);

        txtMediaTitle = (IPTTextView) view.findViewById(R.id.txtMediaTitle);
        txtDirectorTitle = (IPTTextView) view.findViewById(R.id.txtDirectorTitle);
        txtActorTitle = (IPTTextView) view.findViewById(R.id.txtActorsTitle);
        txtActors = (IPTTextView) view.findViewById(R.id.txtActors);
        txtDirectors = (IPTTextView) view.findViewById(R.id.txtDirectors);

        txtMediaTitle.setTypeface(Typeface.createFromAsset(view.getContext().getAssets(), Constants.FONT_REGULAR_PATH));
        txtDirectorTitle.setTypeface(Typeface.createFromAsset(view.getContext().getAssets(), Constants.FONT_REGULAR_PATH));
        txtActorTitle.setTypeface(Typeface.createFromAsset(view.getContext().getAssets(), Constants.FONT_REGULAR_PATH));

        txtGenre = (IPTTextView) view.findViewById(R.id.txtGenre);
        txtSynopsis = (IPTTextView) view.findViewById(R.id.txtSynopsis);
        txtDuration = (IPTTextView) view.findViewById(R.id.txtDuration);
        txtYear = (IPTTextView) view.findViewById(R.id.txtYear);
        txtRated = (IPTTextView) view.findViewById(R.id.txtRated);

//        layoutActors = (LinearLayout) view.findViewById(R.id.layoutActors);
//        layoutDirectors = (LinearLayout) view.findViewById(R.id.layoutDirectors);

        mImgPoster = (ImageView) view.findViewById(R.id.imageHighResolution);
        mBtnPlayMedia = (ImageButton) view.findViewById(R.id.btnPlayMedia);
        mBtnPlayMedia.setOnClickListener(this);
        mBtnClose = (ImageButton) view.findViewById(R.id.btnClose);
        mBtnClose.setOnClickListener(this);

        mBtnFavorite = (ImageButton) view.findViewById(R.id.btnFavourite);
        mBtnFavorite.setOnClickListener(this);
//        txtPlayMovie.setOnClickListener(this);/
//        txtFavorite.setOnClickListener(this);

    }

    /**
     *
     */
    private void sendGetMovieEvent() {
        Bundle bundle = getArguments();
        guid = bundle.getString(MOVIE_ID);
        eventBus.post(new GetMovieHandler(guid).getRequest());
    }

    /**
     * @param actors
     */
    private void buildActorsList(Person[] actors) {
        if (actors != null) {
            String actor = "";
            for (Person person : actors) {
                actor += person.getFirstName() + " " + person.getLastName() + ".  ";
            }
                txtActors.setText(actor);
        }
    }

    /**
     * @param directors
     */
    private void buildDirectorList(Person[] directors) {
        if (directors != null) {
                String director = "";
            for (Person person : directors) {
                director += person.getFirstName() + " " + person.getLastName() + ".  ";

            }
                txtDirectors.setText(director);
//            for (Person person : directors) {
//                IPTTextView textViewDirectors = new IPTTextView(getActivity());
//                textViewDirectors.setText(person.getFirstName() + " " + person.getLastName());
//                textViewDirectors.setTextSize(16);
//                textViewDirectors.setTextColor(Color.WHITE);
//                layoutDirectors.addView(textViewDirectors);
//            }
        }
    }

    /***
     * @param genres
     * @return
     */
    private String getGenre(Genre[] genres) {
        String[] options = new String[genres.length];
        int i = 0;
        for (Genre genre : genres) {
            options[i] = genre.getName();
            i++;
        }
        return Utils.join(",", options);
    }

    /**
     *
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.txtPlayMovie:
//                txtPlayMovie.setTextColor(getResources().getColor(R.color.gold));
            case R.id.btnPlayMedia:
                if (mMovie != null) {
                    eventBus.post(new PlayMovieHandler(guid).getRequest());

                    // MediaPlayer is not sent in the list
            Map<FactoryDeviceTypeDrawable.DeviceKind, Vector<Input>> inputsByDeviceKind = (Map<FactoryDeviceTypeDrawable.DeviceKind, Vector<Input>>)IPT.getInstance().getIPTContext().get(IPT.INPUTS_BY_DEVICE_KIND);
            int inputId = inputsByDeviceKind.get(FactoryDeviceTypeDrawable.DeviceKind.Movie).get(0).getId();
                    // movie input id =2;
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    boolean bMode = sp.getBoolean("default_mode", true);
//                    Log.d("cll", "the cover art path:" + movie.getCoverArtPath());

                    if (bMode) {
                        MovieRemoteFullActivity.isShowPrepareProgress = true;
//                        MovieRemoteFullActivity.fire(getActivity(), movie, inputId);
                        MovieRemoteFullActivity.fire(getActivity(), mMovie.getId(),mMovie.getTitle(), inputId);

                    } else {
//                        MovieRemoteActivity.isShowPrepareProgress = true;
//                        MovieRemoteActivity.fire(getActivity(), mMovie.getId(), mMovie.getTitle(), inputId);
                    }

                    dismiss();
                }
                break;

            case R.id.btnClose:
                this.dismiss();
                if (loaderImage != null) {
                    loaderImage.cancel(true);
                }
                break;

            case R.id.txtFavourite:
            case R.id.btnFavourite:
                updateMovie();
                break;
        }
    }

    /***
     *
     */
    public void updateMovie() {
        if(mMovie == null){
            Log.d(TAG,"couldnot get movie from center pc.");
            return;
        }
        boolean favourite = mMovie.isFavorite() ? false : true;
        mMovie.setFavorite(favourite);
        this.eventBus.post(new UpdateMovieHandler(mMovie).getRequest());
    }

    /**
     *
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loaderImage != null) {
            loaderImage.cancel(true);
        }
        eventBus.unregister(this);
    }

    /**
     * Server interaction
     */

    public void onEvent(UpdateMovieHandler updateMovieHandler) {
        Movie movie = updateMovieHandler.getMovie();
        this.updateFavourite(movie);
    }

    public void onEvent(final GetMovieHandler getMovieHandler) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
               mMovie = getMovieHandler.getMovie();
                txtMediaTitle.setText(mMovie.getTitle());
                txtGenre.setText(getGenre(mMovie.getGenre()));
                txtSynopsis.setText(mMovie.getSynopsis());
                txtYear.setText(Integer.toString(mMovie.getYear()));

                try {
                    txtDuration.setText(Integer.toString(mMovie.getTime()) + " " + getString(R.string.minutes));
                    txtRated.setText(mMovie.getRating());
                    buildActorsList(mMovie.getActors());
                    buildDirectorList(mMovie.getDirectors());
                } catch (IllegalStateException e) {
                    // Exception wil be thrown if Fragment is no longer attached to the activity
                    Log.w(TAG, "Caught exception: " + e.toString());
                }

                mBtnFavorite.setSelected(mMovie.isFavorite());
                mBtnFavorite.setPressed(mMovie.isFavorite());
                if(mMovie.isFavorite()){
                    txtFavorite.setTextColor(getResources().getColor(R.color.gold));
                }else{
                    txtFavorite.setTextColor(getResources().getColor(R.color.gray_light));
                }

                loaderImage = new LoaderImage();
//            loaderImage.execute(movie.getCoverArtPath());
                loaderImage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mMovie.getCoverArtPath());

            }
        });

        // Display movie details on theatre screen
        eventBus.post(new DisplayMovieDetailsOnScreenHandler(getMovieHandler.getMovie().getId()).getRequest());
    }

    public void updateFavourite(final Movie movie) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mBtnFavorite.setSelected(movie.isFavorite());
                mBtnFavorite.setPressed(movie.isFavorite());
                if(mMovie.isFavorite()){
                    txtFavorite.setTextColor(getResources().getColor(R.color.gold));
                }else{
                    txtFavorite.setTextColor(getResources().getColor(R.color.gray_light));
                }
            }
        });

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

            if (result != null) {
                try {
                    BitmapDrawable background = new BitmapDrawable(getResources(), result);
                    mImgPoster.setImageDrawable(background);
                } catch (IllegalStateException e) {
                    // Exception wil be thrown if Fragment is no longer attached to the activity
                    Log.w(TAG, "Caught exception: " + e.toString());
                }
            }
        }

    }
}
