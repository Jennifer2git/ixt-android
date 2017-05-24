package com.imax.ipt.ui.activity.input.movie;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.movie.GetMovieLitesHandler;
import com.imax.ipt.model.Media;
import com.imax.ipt.model.MovieLite;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.widget.gridview.GridAdapter;
import com.imax.ipt.ui.widget.gridview.TwoWayAdapterView;
import com.imax.ipt.ui.widget.gridview.TwoWayAdapterView.OnItemClickListener;
import com.imax.ipt.ui.widget.gridview.TwoWayGridWidget;
import com.imax.iptevent.EventBus;

import java.util.ArrayList;
import java.util.List;

public class DialogMovieActorsFragment extends DialogFragment implements OnItemClickListener {
    public static String ACTOR_ID = "actor_id";
    public static String ACTOR_NAME = "actor_name";

    private EventBus mEventBus;
    private TwoWayGridWidget mGridView;
    private String guid;
    private ImageButton mBtnClose;
    private ArrayList<Media> mMoviesActors = new ArrayList<Media>();
    private GridAdapter mGridAdapter;
    private IPTTextView mTxtMediaTitle;

    @Override
    public View onCreateView(LayoutInflater mInflater, ViewGroup container, Bundle savedInstanceState) {
        View mViewDialogFragment = mInflater.inflate(R.layout.fragment_dialog_movie_actor, null);

        Dialog d = getDialog();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());
        lp.width = 1618;
        lp.height = 1300;
//        lp.x = 200;
//        lp.y = -180;
//        d.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        d.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.catalogue_details_card_bg2));
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        d.show();
        d.getWindow().setAttributes(lp);
        setStyle(STYLE_NO_FRAME, 0);

        this.init(mViewDialogFragment);
        return mViewDialogFragment;
    }

    private void init(View view) {
        Bundle bundle = getArguments();
        guid = bundle.getString(ACTOR_ID);
        String title = bundle.getString(ACTOR_NAME);
        this.mTxtMediaTitle = (IPTTextView) view.findViewById(R.id.txtMediaTitle);
        this.mTxtMediaTitle.setText(title);
        this.mEventBus = IPT.getInstance().getEventBus();
        this.mEventBus.register(this);
//        this.mGridAdapter = new GridAdapter(getActivity().getApplicationContext(), R.layout.item_movie_library, mMoviesActors, R.drawable.ipt_gui_asset_new_movie_icon, 115, 175, 5, 0);// R.drawable.catalogue_noposter_bg, 160, 240, 0, 2
        this.mGridAdapter = new GridAdapter(getActivity().getApplicationContext(), R.layout.item_movie_library, mMoviesActors, R.drawable.catalogue_noposter_bg, 160, 240, 5, 0);// R.drawable.catalogue_noposter_bg, 160, 240, 0, 2

        mGridView = (TwoWayGridWidget) view.findViewById(R.id.gridview);
        mGridView.setOnItemClickListener(this);
        mGridView.setAdapter(mGridAdapter);

        this.getMovieLite();

        mBtnClose = (ImageButton) view.findViewById(R.id.btnClose);
        mBtnClose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void getMovieLite() {

        GetMovieLitesHandler getMovieLiteEvent = new GetMovieLitesHandler();
        getMovieLiteEvent.setStartIndex(0);
        getMovieLiteEvent.setCount(1000);
        getMovieLiteEvent.setActorId(guid);
        this.mEventBus.post(getMovieLiteEvent.getRequest());
    }

    public void onEvent(GetMovieLitesHandler getMovieLiteEvent) {

        final List<MovieLite> movieLites = getMovieLiteEvent.getMovieLites(); //
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGridAdapter.addAll(movieLites);
                mGridAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
    }

    @Override
    public void onItemClick(TwoWayAdapterView<?> parent, View view, int position, long id) {
        FragmentManager fm = getFragmentManager();
        Bundle bundle = new Bundle();
        Media media = mMoviesActors.get(position);
        bundle.putString(DialogMovieFragment.MOVIE_ID, media.getId());
        DialogMovieFragment mDialogMediaLibraryFragment = new DialogMovieFragment();
        mDialogMediaLibraryFragment.setArguments(bundle);
        mDialogMediaLibraryFragment.show(fm, "fragment_movie");

    }
}
