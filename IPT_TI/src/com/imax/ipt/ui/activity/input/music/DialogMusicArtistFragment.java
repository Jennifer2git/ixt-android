package com.imax.ipt.ui.activity.input.music;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.music.GetMusicArtistAlbumsHandler;
import com.imax.ipt.model.Media;
import com.imax.ipt.model.MusicAlbum;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.widget.gridview.GridAdapter;
import com.imax.ipt.ui.widget.gridview.TwoWayAdapterView;
import com.imax.ipt.ui.widget.gridview.TwoWayAdapterView.OnItemClickListener;
import com.imax.ipt.ui.widget.gridview.TwoWayGridWidget;
import com.imax.iptevent.EventBus;

public class DialogMusicArtistFragment extends DialogFragment implements OnItemClickListener {
    public static String ARTIST_ID = "artist_id";
    public static String ARTIST_NAME = "artist_name";

    private EventBus mEventBus;
    private TwoWayGridWidget mGridView;
    private String guid;
    private ImageButton mBtnClose;
    private ArrayList<Media> mMedias = new ArrayList<Media>();
    private GridAdapter mGridAdapter;
    private IPTTextView mTxtMediaTitle;

    @Override
    public View onCreateView(LayoutInflater mInflater, ViewGroup container, Bundle savedInstanceState) {
        View mViewDialogFragment = mInflater.inflate(R.layout.fragment_dialog_music_artist, null);

        Dialog d = getDialog();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());
        lp.width = 1618;
        lp.height = 1300;
        lp.x = 200;
        lp.y = -180;
        d.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        d.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        d.show();
        d.getWindow().setAttributes(lp);
        setStyle(STYLE_NO_FRAME, 0);

        this.init(mViewDialogFragment);
        return mViewDialogFragment;
    }

    private void init(View view) {
        Bundle bundle = getArguments();
        guid = bundle.getString(ARTIST_ID);
        String title = bundle.getString(ARTIST_NAME);
        this.mTxtMediaTitle = (IPTTextView) view.findViewById(R.id.txtMediaTitle);
        this.mTxtMediaTitle.setText(title);
        this.mEventBus = IPT.getInstance().getEventBus();
        this.mEventBus.register(this);
        this.mGridAdapter = new GridAdapter(getActivity().getApplicationContext(), R.layout.item_music_library, mMedias, R.drawable.ipt_gui_asset_album_cover_default, 175, 175, 5, 0);

        mGridView = (TwoWayGridWidget) view.findViewById(R.id.gridview);
        mGridView.setOnItemClickListener(this);
        mGridView.setAdapter(mGridAdapter);

        this.getArtistAlbums();

        mBtnClose = (ImageButton) view.findViewById(R.id.btnClose);
        mBtnClose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void getArtistAlbums() {
        GetMusicArtistAlbumsHandler getMusicArtistAlbumsHandler = new GetMusicArtistAlbumsHandler();
        getMusicArtistAlbumsHandler.setStartIndex(0);
        getMusicArtistAlbumsHandler.setCount(1000);
        getMusicArtistAlbumsHandler.setMusicArtistId(guid);
        this.mEventBus.post(getMusicArtistAlbumsHandler.getRequest());
    }

    public void onEvent(GetMusicArtistAlbumsHandler getMusicArtistAlbumsHandler) {
        final List<MusicAlbum> musicAlbums = getMusicArtistAlbumsHandler.getMusicAlbums();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGridAdapter.addAll(musicAlbums);
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
        Media media = mMedias.get(position);
        bundle = new Bundle();
        DialogMusicFragment mDialogMusicFragment = new DialogMusicFragment();
        bundle.putString(DialogMusicFragment.MUSIC_ALBUM_ID, media.getId());
        mDialogMusicFragment.setArguments(bundle);
        mDialogMusicFragment.show(fm, "fragment_music_dialog");

    }
}
