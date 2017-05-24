package com.imax.ipt.ui.activity.menu;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.ui.activity.LobbyActivity;
import com.imax.ipt.ui.activity.input.MenuInputsFragment;
import com.imax.ipt.ui.activity.room.MenuRoomFragment;
import com.imax.ipt.ui.activity.settings.MenuSettingsFragment;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.iptevent.EventBus;

/**
 * @author Rodrigo Lopez
 */
public class MenuMasterFragment extends Fragment implements OnClickListener {
    public static String TAG = "MasterMenuFragment";

    private View mViewMasterMenuLayout;
    private View mViewMasterAnimation;

    /**
     * Master Options
     */
    private ImageButton mBtnMasterMenu;
    private ImageButton mBtnImax;
    private IPTTextView mIPTTextViewMenu;
    private ImageButton mBtnMasterMenuActive;
    private ImageView mImgMenuBackgroud;
    private ImageButton mBtnPower;
    private Button mBtnZoom;

    private EventBus mEventBus;

    private Boolean mLongClick = false;

    private long downTime = 0;
    private long upTime = 0;
    private Handler mHandler = new Handler();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mEventBus = IPT.getInstance().getEventBus(); //TODO change by controller
        String menuName;
        Bundle bundle = getArguments();
        if(bundle != null){
            menuName = (String)bundle.get("menu_name");
        }else{
            menuName ="";
        }


        mViewMasterMenuLayout = inflater.inflate(R.layout.fragment_master_menu, null);
        mBtnMasterMenu = (ImageButton) mViewMasterMenuLayout.findViewById(R.id.btnMenuMaster);
        mBtnImax = (ImageButton) mViewMasterMenuLayout.findViewById(R.id.btn_nav_Imax);
//        mBtnMasterMenuActive = (ImageButton) mViewMasterMenuLayout.findViewById(R.id.btnMenuMasterActive);

//        mImgMenuBackgroud = (ImageView) mViewMasterMenuLayout.findViewById(R.id.imgBackground);
//        MenuShapeView menuView = new MenuShapeView(getActivity().getBaseContext());
//        menuView.setAlpha(200);
//        mImgMenuBackgroud.setImageDrawable(menuView);

        mIPTTextViewMenu = (IPTTextView) mViewMasterMenuLayout.findViewById(R.id.txtMenu);
        mIPTTextViewMenu.setText(menuName);
        mBtnMasterMenu.setOnClickListener(this);
        mBtnImax.setOnClickListener(this);

        if(menuName.equals(getResources().getString(R.string.menu_name_game))) {
            mBtnZoom = (Button) mViewMasterMenuLayout.findViewById(R.id.btnZoom);
            mBtnZoom.setVisibility(View.VISIBLE);
            mBtnZoom.setOnClickListener(this);
        }




//        this.addOptionFragments();

        //this.mEventBus = IPT.getInstance().getEventBus();
        //this.mEventBus.register(this);
        //  this.sendGetActiveDeviceTypesRequest();

        return mViewMasterMenuLayout;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //this.mEventBus.unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     *
     */
    private void addOptionFragments() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        MenuInputsFragment inputsFragment = new MenuInputsFragment();
        MenuRoomFragment roomFragment = new MenuRoomFragment();
        MenuSettingsFragment settingsFragment = new MenuSettingsFragment();

        transaction.add(R.id.fragmentRooms, roomFragment);
        transaction.add(R.id.fragmentInputs, inputsFragment);
        transaction.add(R.id.fragmentSettings, settingsFragment);
        transaction.commit();
    }

//   private void sendGetActiveDeviceTypesRequest()
//   {
//      mEventBus.post(new GetActiveDeviceTypesRequestEvent());   
//   }

    /**
     *
     */
    private void showMenu() {

        switch (mViewMasterAnimation.getVisibility()) {
            case View.VISIBLE:
                mViewMasterAnimation.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_master_menu_disappear));
                mViewMasterAnimation.setVisibility(View.INVISIBLE);
                mBtnMasterMenu.setVisibility(View.VISIBLE);
                mIPTTextViewMenu.setVisibility(View.VISIBLE);
                break;
            case View.INVISIBLE:
                mViewMasterAnimation.setVisibility(View.VISIBLE);
                mViewMasterAnimation.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_master_menu_appear));
                mBtnMasterMenu.setVisibility(View.INVISIBLE);
                mIPTTextViewMenu.setVisibility(View.INVISIBLE);
                break;
            default:
                Log.d(TAG, "Visibility is gone");
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_nav_Imax:
            case R.id.btnMenuMaster:
                LobbyActivity.fire(getActivity());
                break;
            case R.id.btnZoom:

        }
    }


//   /**
//    * ##### Server Interaction *
//    */
//   
//   public void onEvent(GetActiveDeviceTypesResponseEvent activeDeviceTypesResponseEvent)
//   {
//      DeviceType[] deviceTypes = activeDeviceTypesResponseEvent.getDeviceTypes();
//      for (DeviceType deviceType : deviceTypes)
//      {
//         Log.d(TAG, deviceType.getDeviceKind());
//      }
//   }


}
