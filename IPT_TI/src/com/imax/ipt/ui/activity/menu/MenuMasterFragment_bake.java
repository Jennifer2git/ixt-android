package com.imax.ipt.ui.activity.menu;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.power.SwitchSystemPowerHandler;
import com.imax.ipt.ui.activity.input.MenuInputsFragment;
import com.imax.ipt.ui.activity.room.MenuRoomFragment;
import com.imax.ipt.ui.activity.settings.MenuSettingsFragment;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.layout.MenuShapeView;
import com.imax.iptevent.EventBus;

/**
 * @author Rodrigo Lopez
 */
public class MenuMasterFragment_bake extends Fragment implements OnClickListener {
    public static String TAG = "MasterMenuFragment";

    private View mViewMasterMenuLayout;
    private View mViewMasterAnimation;

    /**
     * Master Options
     */
    private ImageButton mBtnMasterMenu;
    private IPTTextView mIPTTextViewMenu;
    private ImageButton mBtnMasterMenuActive;
    private ImageView mImgMenuBackgroud;
    private ImageButton mBtnPower;
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

        mViewMasterMenuLayout = inflater.inflate(R.layout.fragment_master_menu_bake, null);
        mBtnMasterMenu = (ImageButton) mViewMasterMenuLayout.findViewById(R.id.btnMenuMaster);
        mBtnMasterMenuActive = (ImageButton) mViewMasterMenuLayout.findViewById(R.id.btnMenuMasterActive);

        mImgMenuBackgroud = (ImageView) mViewMasterMenuLayout.findViewById(R.id.imgBackground);
        MenuShapeView menuView = new MenuShapeView(getActivity().getBaseContext());
        menuView.setAlpha(200);
        mImgMenuBackgroud.setImageDrawable(menuView);
//      mImgMenuBackgroud.setBackgroundResource(R.drawable.ipt_gui_asset_master_menu_bg);
//      mImgMenuBackgroud.setImageAlpha(254);




        mViewMasterAnimation = mViewMasterMenuLayout.findViewById(R.id.masterMenuAnimation);
        mIPTTextViewMenu = (IPTTextView) mViewMasterMenuLayout.findViewById(R.id.txtMenu);
        mBtnMasterMenu.setOnClickListener(this);
        mBtnMasterMenuActive.setOnClickListener(this);

        mViewMasterAnimation.setOnClickListener(this);

        mBtnPower = (ImageButton) mViewMasterAnimation.findViewById(R.id.btnPowerOff);
 /*     mBtnPower.setOnLongClickListener(new OnLongClickListener() 
      {         
         @Override
         public boolean onLongClick(View v)
         {
            mEventBus.post(new SwitchSystemPowerHandler(false).getRequest()); //TODO we have to create a controller for this
            mLongClick = true;
            return false;
         }
      });*/
        final Runnable mRunnable = new Runnable() {

            @Override
            public void run() {
                mEventBus.post(new SwitchSystemPowerHandler(false).getRequest());

            }
        };
        mBtnPower.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    downTime = System.currentTimeMillis();
                    mHandler.postDelayed(mRunnable, 4000);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    upTime = System.currentTimeMillis();

                    if ((upTime - downTime) > 4000) {
//		            mEventBus.post(new SwitchSystemPowerHandler(false).getRequest()); //TODO we have to create a controller for this

                    } else {
                        mHandler.removeCallbacks(mRunnable);
                        Toast toast = Toast.makeText(getActivity(), R.string.toast_long_press_to_power_off, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM | Gravity.RIGHT, 150, 20);

                        TextView textView = new TextView(IPT.getInstance());
                        textView.setBackgroundResource(R.drawable.toast_bg_left);
                        if (getResources().getConfiguration().locale.getCountry().equals("CN")) {
                            textView.setWidth(300);
                        } else {
                            textView.setWidth(700);
                        }
                        textView.setTextColor(Color.WHITE);
                        textView.setTextSize(20);
                        textView.setGravity(Gravity.CENTER);
                        textView.setText(R.string.toast_long_press_to_power_off);
                        toast.setView(textView);
                        toast.show();
                    }
                }
                return false;
            }
        });
/*      mBtnPower.setOnClickListener(new OnClickListener(){
        @Override
		public void onClick(View arg0) {
			if(!mLongClick)
			{
//				Toast.makeText(SystemOffActivity.this, "Press and hold for 5 seconds", Toast.LENGTH_LONG).show();
				Toast toast = Toast.makeText(getActivity(), R.string.toast_long_press_to_power_off,  Toast.LENGTH_LONG);
				toast.setGravity(Gravity.BOTTOM | Gravity.RIGHT, 150, 20);
				
				TextView textView = new TextView(IPT.getInstance());
				textView.setBackgroundResource(R.drawable.toast_bg_left);
				if (getResources().getConfiguration().locale.getCountry().equals("CN")) {
					textView.setWidth(300);
				}else {
					textView.setWidth(700);
				}
				textView.setTextColor(Color.WHITE);
				textView.setTextSize(20);
				textView.setGravity(Gravity.CENTER);
				textView.setText(R.string.toast_long_press_to_power_off);
				toast.setView(textView);
				toast.show();
			}
			mLongClick = false;
		}
      });*/

        this.addOptionFragments();

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

        // Close the menu on resume of the fragment
        if (mViewMasterAnimation.getVisibility() == View.VISIBLE) {
            showMenu();
        }
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
            case R.id.btnMenuMaster:
            case R.id.btnMenuMasterActive:
            case R.id.masterMenuAnimation:
                showMenu();
                break;
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
