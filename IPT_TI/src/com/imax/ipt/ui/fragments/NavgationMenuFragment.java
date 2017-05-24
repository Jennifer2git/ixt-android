package com.imax.ipt.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.remote.ExecuteRemoteControlHandler;
import com.imax.ipt.ui.activity.LobbyActivity;
import com.imax.ipt.ui.activity.input.movie.MovieLibraryActivity;
import com.imax.ipt.ui.activity.input.movie.MozaxRemoteControlActivity;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.iptevent.EventBus;

/**
 * Created by yanli on 2015/11/13.
 */
public class NavgationMenuFragment extends Fragment implements View.OnClickListener {
    public static String TAG = "NavgationMenuFragment";

    private View mViewMasterMenuLayout;
    private Activity mContainerActivity;
    /**
     * movie title,
     */
    private ImageButton mBtnMasterMenu;
    private ImageButton mBtnImax;
    private ImageButton mBtnBack;
    private ImageButton mBtnRemoteControl;
    private ImageButton mBtnPopUp;
    private ImageButton mBtnMovieMenu;

    private IPTTextView mIPTTextViewMenu;
    private ImageButton mBtnMasterMenuActive;
    private ImageView mImgMenuBackgroud;
    private ImageButton mBtnPower;
    private EventBus mEventBus;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContainerActivity = activity;

    }

    public Activity getContainerActivity(){
        return this.mContainerActivity ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mEventBus = IPT.getInstance().getEventBus();
//        mViewMasterMenuLayout = inflater.inflate(R.layout.fragment_master_menu, null);
        mViewMasterMenuLayout = inflater.inflate(R.layout.fragment_navi_menu, null);
        initUI(inflater);
        return mViewMasterMenuLayout;
    }

    private void initUI(LayoutInflater inflater){
        mBtnMasterMenu = (ImageButton) mViewMasterMenuLayout.findViewById(R.id.btnMenuMaster);
        mBtnImax = (ImageButton) mViewMasterMenuLayout.findViewById(R.id.btn_nav_Imax);
        mBtnMasterMenu.setOnClickListener(this);
        mBtnImax.setOnClickListener(this);
        mBtnBack = (ImageButton) mViewMasterMenuLayout.findViewById(R.id.btnBack);
        mBtnBack.setVisibility(View.GONE);
//        mBtnBack.setOnClickListener(this);
        mBtnPopUp = (ImageButton) mViewMasterMenuLayout.findViewById(R.id.btnPopUpMenu);
        mBtnPopUp.setOnClickListener(this);
        mBtnMovieMenu = (ImageButton) mViewMasterMenuLayout.findViewById(R.id.btnMovieMenu);
        mBtnRemoteControl = (ImageButton) mViewMasterMenuLayout.findViewById(R.id.btn_remote);
        mBtnRemoteControl.setOnClickListener(this);

        if(mContainerActivity.getClass().getSimpleName().equals(MovieLibraryActivity.class.getSimpleName())){
            LinearLayout ll_control_menu = (LinearLayout)mViewMasterMenuLayout.findViewById(R.id.ll_control_menu);
            ll_control_menu.setVisibility(View.INVISIBLE);
            mBtnPopUp.setVisibility(View.INVISIBLE);
            mBtnMovieMenu.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnMenuMaster:
            case R.id.btn_nav_Imax:
                LobbyActivity.fire(getActivity());
                break;
            case R.id.btnBack:
//                if (v.getContext().getClass().getSimpleName().equals(MovieLibraryActivity.class.getSimpleName())){
//                    break;
//                }
//                MovieLibraryActivity.fire(getActivity());
                break;
            case R.id.btnPopUpMenu:
                break;
            case R.id.btn_remote2:
            case R.id.btn_remote:
            case R.id.btnPreviousMenu:
          /*ExecuteRemoteControlHandler need method string name. */
                this.mEventBus.post(new ExecuteRemoteControlHandler("PreviousMenu").getRequest());
                MozaxRemoteControlActivity.fire(getActivity(), "guid", "", 1);
                break;
            default:

        }

    }



}
