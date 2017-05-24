package com.imax.ipt.ui.activity.settings.preferences;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.settings.maintenance.GetVersionHandler;
import com.imax.ipt.controller.eventbus.handler.ui.MediaMenuLibraryEvent.MenuEvent;
import com.imax.ipt.controller.settings.OtherController;
import com.imax.ipt.ui.activity.BaseActivity;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.util.DisplayUtil;
import com.imax.ipt.ui.viewmodel.MenuLibraryElement;

import java.util.ArrayList;
import java.util.List;

public class OtherActivity extends BaseActivity {
    private static final String TAG = "OtherActivity";

    private List<MenuLibraryElement> mMenuOptions = new ArrayList<MenuLibraryElement>();
    private OtherController otherController;

    private IPTTextView txtVersionInfo;

    private VersionFragment versionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_others);

//        StringBuilder versionSb = new StringBuilder();
//        PackageManager manager = getPackageManager();
//        try {
//            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
//            versionSb.append("°æ±¾ºÅ : " + info.versionCode + System.getProperty("line.separator")
//                    + "°æ±¾Ãû³Æ: " + info.versionName);
//
//        } catch (NameNotFoundException e) {
//            e.printStackTrace();
//        }

//        txtVersionInfo = (IPTTextView) findViewById(R.id.txtVersionInfo);
//      txtVersionInfo.setText(versionSb.toString());
//        txtVersionInfo.setText("");
        this.init();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        otherController.destroy();
    }

    /**
     * @param mContext
     */
    public static void fire(Context mContext) {
        Intent intent = new Intent(mContext, OtherActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     *
     */
    protected void init() {
        this.otherController = new OtherController(this);
        MenuLibraryElement menuGeneral = new MenuLibraryElement(1, getResources().getString(R.string.other_version), null, MenuEvent.VERSION);
        mMenuOptions.add(menuGeneral);
        this.addMenuFragment();
        this.addMenuLibraryFragment(getResources().getString(R.string.others), mMenuOptions, DisplayUtil.dip2px(this,230));
        this.showFragment(MenuEvent.VERSION);
    }

    public void showFragment(MenuEvent mCurrentEvent) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (mCurrentEvent) {
            case VERSION:
                versionFragment = new VersionFragment();
                transaction.replace(R.id.menuVersion, versionFragment);
                versionFragment.setOtherController(otherController);
                transaction.commitAllowingStateLoss();
                break;
            case PREF_FAVOURITES:
                FavouritesFragment mFavoutites = new FavouritesFragment();
//         mFavoutites.setPreferencesController(mPreferencesController);
                transaction.replace(R.id.menuVersion, mFavoutites);
                transaction.commitAllowingStateLoss();
                break;
            default:
                break;

        }
    }


    public void showVersion(final GetVersionHandler handler) {
        if (versionFragment != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    versionFragment.setVersion(handler);
                }
            });

        }
    }

}
