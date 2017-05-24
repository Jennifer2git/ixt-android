package com.imax.ipt.ui.activity.settings.preferences;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.ui.MediaMenuLibraryEvent.MenuEvent;
import com.imax.ipt.controller.settings.PreferencesController;
import com.imax.ipt.model.LightingEvent;
import com.imax.ipt.model.LightingPreset;
import com.imax.ipt.ui.activity.BaseActivity;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.util.DisplayUtil;
import com.imax.ipt.ui.viewmodel.MenuLibraryElement;

import java.util.ArrayList;
import java.util.List;

public class PreferencesActivity extends BaseActivity
        implements LightingPresetSelectionDialogFragment.OnLightingPresetSelectedListener {
    private static final String TAG = "PreferencesActivity";

    private List<MenuLibraryElement> mMenuOptions = new ArrayList<MenuLibraryElement>();
    private PreferencesController mPreferencesController;

    private LightingFragment mLightingFragment;
    private IPTTextView txtVersionInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_preferences);

        StringBuilder versionSb = new StringBuilder();
        PackageManager manager = getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            versionSb.append("°æ±¾ºÅ : " + info.versionCode + System.getProperty("line.separator") + "°æ±¾Ãû³Æ: " + info.versionName);

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

//        txtVersionInfo = (IPTTextView) findViewById(R.id.txtVersionInfo);
//      txtVersionInfo.setText(versionSb.toString());
//        txtVersionInfo.setText("");


        this.init();
    }

//   @Override
//   public View onCreateView(String name, Context context, AttributeSet attrs)
//   {
//      // TODO Auto-generated method stub
//      View view = super.onCreateView(name, context, attrs);
//      
//      
//      
//      return view;
//   }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPreferencesController.onDestroy();
    }

    /**
     * @param mContext
     */
    public static void fire(Context mContext) {
        Intent intent = new Intent(mContext, PreferencesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     *
     */
    protected void init() {
        Log.d(TAG, "init");
        this.mPreferencesController = new PreferencesController(this);

        MenuLibraryElement menuGeneral = new MenuLibraryElement(1, getResources().getString(R.string.general), null, MenuEvent.GENERAL);
        mMenuOptions.add(menuGeneral);
//        MenuLibraryElement menuFav = new MenuLibraryElement(2, getResources().getString(R.string.favourites), null, MenuEvent.PREF_FAVOURITES);
//        mMenuOptions.add(menuFav);

//        MenuLibraryElement menuLighting = new MenuLibraryElement(3, getResources().getString(R.string.lighting), null, MenuEvent.LIGHTING);
//      mMenuOptions.add(menuLighting);
        this.addMenuFragment();
        this.addMenuLibraryFragment(getResources().getString(R.string.preferences), mMenuOptions, DisplayUtil.dip2px(this,230));//730
        this.showFragment(MenuEvent.GENERAL);
    }

    public void showFragment(MenuEvent mCurrentEvent) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (mCurrentEvent) {
            case GENERAL:
                GeneralFragment mGeneralFragment = new GeneralFragment();
//                mGeneralFragment.setPreferencesController(mPreferencesController);
                transaction.replace(R.id.menuPreferences, mGeneralFragment);
                transaction.commitAllowingStateLoss();
                break;
            case PREF_FAVOURITES:
                FavouritesFragment mFavoutites = new FavouritesFragment();
                mFavoutites.setPreferencesController(mPreferencesController);
                transaction.replace(R.id.menuPreferences, mFavoutites);
                transaction.commitAllowingStateLoss();
                break;
            case LIGHTING:
                mLightingFragment = new LightingFragment();
                // khh: need to pass a preference controller to the lighting fragment
                mLightingFragment.setPreferencesController(mPreferencesController);

                transaction.replace(R.id.menuPreferences, mLightingFragment);
                transaction.commitAllowingStateLoss();
                break;
            default:
                break;

        }
    }

    public void populateLightingEvents(final LightingEvent[] lightingEvents, final LightingPreset[] lightingPresets) {
        if (mLightingFragment != null) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    mLightingFragment.populateList(lightingEvents, lightingPresets);
                }
            });

        }
    }

    @Override
    public void onLightPresetSelected(int eventId, LightingPreset preset) {
        mLightingFragment.updateList(eventId, preset);
    }

}
