package com.imax.ipt.ui.activity.settings.maintenance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.ListView;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.ui.MediaMenuLibraryEvent.MenuEvent;
import com.imax.ipt.controller.settings.MaintanceController;
import com.imax.ipt.ui.activity.BaseActivity;
import com.imax.ipt.ui.activity.room.LightingSettingsActivity;
import com.imax.ipt.ui.util.DisplayUtil;
import com.imax.ipt.ui.viewmodel.MenuLibraryElement;

import java.util.ArrayList;
import java.util.List;

public class MaintenceActivity extends BaseActivity {
    private static final String TAG = "MaintenceActivity";
    private List<MenuLibraryElement> mMenuOptions = new ArrayList<MenuLibraryElement>();
    private MaintanceController mMaintanceController;
    private LoginFragment mLoginFragment;
    private ListView mMenuMaintaneceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_maintenance);
        this.init();

//        DialogLoginFragment loginDialog = new DialogLoginFragment();
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        loginDialog.show(ft);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMaintanceController.onDestroy();
    }

    /**
     * @param mContext
     */
    public static void fire(Context mContext) {
        Intent intent = new Intent(mContext, MaintenceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

    }

    /**
     *
     */
    public void loginOn() {
        mLoginFragment.loginOn();
    }

    /**
     *
     */
    public void loginOff() {
        mLoginFragment.loginOff();
    }

    private void init() {

        MenuLibraryElement menuProjector = new MenuLibraryElement(2, getResources().getString(R.string.projector), null, MenuEvent.PROJECTOR);
        MenuLibraryElement menuSettings = new MenuLibraryElement(4, getResources().getString(R.string.calibration), null, MenuEvent.SETTINGS);
        MenuLibraryElement menuFocus = new MenuLibraryElement(5, getResources().getString(R.string.focus), null, MenuEvent.FOCUS);

//        MenuLibraryElement menuRebootDevice = new MenuLibraryElement(3, getResources().getString(R.string.reboot_devices), null, MenuEvent.REBOOT_DEVICES);
//      MenuLibraryElement menuLighting = new MenuLibraryElement(5, getResources().getString(R.string.lighting_settings), null, MenuEvent.LIGHTING);
//      MenuLibraryElement menuVersion = new MenuLibraryElement(5, getResources().getString(R.string.version), null, MenuEvent.VERSION);
//        MenuLibraryElement menuGeneral = new MenuLibraryElement(5, getResources().getString(R.string.general), null, MenuEvent.DEMO);

        mMenuOptions.add(menuSettings);
        mMenuOptions.add(menuProjector);
        mMenuOptions.add(menuFocus);


//        mMenuOptions.add(menuLighting);
//        mMenuOptions.add(menuRebootDevice);
//        mMenuOptions.add(menuGeneral);
//      mMenuOptions.add(menuVersion);


        this.addMenuLibraryFragment(getResources().getString(R.string.maintenance), mMenuOptions, DisplayUtil.dip2px(this,230));//460
        this.addMenuFragment();
        this.mMaintanceController = new MaintanceController(this);

        //add by jennifer
//         MenuItemAdapter mMenuMaintanenceAdapter = new MenuItemAdapter(this,mMenuOptions,true);
//        mMenuMaintaneceList = (ListView) findViewById(R.id.list_menuMaintenance);
//        mMenuMaintaneceList.setAdapter(mMenuMaintanenceAdapter);

    }

    public void showFragment(final MenuEvent mCurrentEvent) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                switch (mCurrentEvent) {
                    case LOGIN:
                        mLoginFragment = new LoginFragment();
                        transaction.replace(R.id.menuMaintenance, mLoginFragment);
                        transaction.commitAllowingStateLoss();
                        break;
                    case PROJECTOR:
                        ProjectorFragment mProjectorFragment = new ProjectorFragment();
                        transaction.replace(R.id.menuMaintenance, mProjectorFragment);
                        transaction.commitAllowingStateLoss();
                        break;
                    case REBOOT_DEVICES:
                        RebootFragment mRebootFragment = new RebootFragment();
                        transaction.replace(R.id.menuMaintenance, mRebootFragment);
                        transaction.commitAllowingStateLoss();
                        break;
                    case SETTINGS:
                        PCABFragment pcabFragment = new PCABFragment();
                        transaction.replace(R.id.menuMaintenance, pcabFragment);
                        transaction.commitAllowingStateLoss();
                        break;
                    case DEMO:
                        MaintenceGeneralFragment generalFragment = new MaintenceGeneralFragment();
                        transaction.replace(R.id.menuMaintenance, generalFragment);
                        transaction.commitAllowingStateLoss();
                        break;
                    case LIGHTING:
                        LightingSettingsActivity.fire(MaintenceActivity.this);
                        Log.d(TAG, "Lighting event on Maintenance Activity");
                        break;
                    case FOCUS:
                        ControlFocusFragment focusFragment = new ControlFocusFragment();
                        transaction.replace(R.id.menuMaintenance, focusFragment);
                        transaction.commitAllowingStateLoss();
                        break;

                    default:
                        Log.d(TAG, "Default event on Maintenance Activity");
                        break;
                }
            }
        });

    }

}
