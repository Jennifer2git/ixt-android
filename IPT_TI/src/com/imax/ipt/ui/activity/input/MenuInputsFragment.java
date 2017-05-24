package com.imax.ipt.ui.activity.input;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.imax.ipt.R;
import com.imax.ipt.common.Constants;
import com.imax.ipt.controller.eventbus.handler.ui.CloseFragmentEvent;
import com.imax.ipt.controller.inputs.MenuInputsController;
import com.imax.ipt.model.DeviceType;
import com.imax.ipt.ui.activity.input.gaming.GamingActivity;
import com.imax.ipt.ui.activity.input.karaoke.KaraokeActivity;
import com.imax.ipt.ui.activity.input.media.MediaDeviceActivity;
import com.imax.ipt.ui.activity.input.movie.MovieLibraryActivity;
import com.imax.ipt.ui.activity.input.music.MusicLibraryActivity;
import com.imax.ipt.ui.activity.input.tv.TVActivity;
import com.imax.ipt.ui.activity.media.ExternalInputActivity;
import com.imax.ipt.ui.activity.media.ZaxelActivity;
import com.imax.ipt.ui.activity.menu.MenuItemFragment;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.viewmodel.MenuMasterElement;
import com.imax.ipt.ui.widget.semicircular.SemiCircularMenu;
import com.imax.ipt.ui.widget.semicircular.SemiCircularMenu.OnClickHitButton;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rlopez
 */
public class MenuInputsFragment extends MenuItemFragment {
    public static final String TAG = "MenuInputsFragment";
    private SemiCircularMenu mCircularMenu;

    private List<MenuMasterElement> mElements = new ArrayList<MenuMasterElement>();

    public static String name = "Inputs";

    private View mInputsLayout;

    private MenuInputsController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mInputsLayout = inflater.inflate(R.layout.fragment_inputs, null);
        controller = new MenuInputsController(this);
        controller.getActiveDeviceTypes();

        return mInputsLayout;
    }

    @Override
    public void onStart() {
        super.onStart();

//      controller.getActiveDeviceTypes();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (controller != null)
            controller.onDestroy();
    }

    public void populateDeviceTypes(DeviceType[] deviceTypes) {

        if (mElements.size() > 0) {
            // input menu is already populated
            //    Movie Activity is a long running activity that may receive the response multiple times
            return;
        }

        View view = mInputsLayout;
        //edit by watershao
        // dynamic input menu text
/*      for (DeviceType deviceType : deviceTypes)
      {
         switch (deviceType.getDeviceKind())
         {
         case Music:
            mElements.add(new MenuMasterElement(R.drawable.selector_menu_input_music_button, deviceType.getDisplayName()));
            break;
         case Movie:
            mElements.add(new MenuMasterElement(R.drawable.selector_menu_input_movie_button, deviceType.getDisplayName()));
            break;
         case Gaming:
            mElements.add(new MenuMasterElement(R.drawable.selector_menu_input_gaming_button, deviceType.getDisplayName()));
            break;
         case MediaDevices:
            mElements.add(0, new MenuMasterElement(R.drawable.selector_menu_input_media_devices_button, deviceType.getDisplayName()));
            break;
         case TV:
            mElements.add(0, new MenuMasterElement(R.drawable.selector_menu_input_tv_button, deviceType.getDisplayName()));
            break;
         case Security:
            mElements.add(0, new MenuMasterElement(R.drawable.selector_menu_input_security_button, deviceType.getDisplayName()));
            break;
         }*/

//         if (deviceType.getDeviceKind().equalsIgnoreCase(DeviceKind.Music.toString()))
//         {
//            mElements.add(new MenuMasterElement(R.drawable.selector_menu_input_music_button, deviceType.getDisplayName()));            
//         }
//         else if (deviceType.getDeviceKind().equalsIgnoreCase(DeviceKind.Movie.toString()))
//         {
//            mElements.add(new MenuMasterElement(R.drawable.selector_menu_input_movie_button, deviceType.getDisplayName()));      
//         }
//         else if (deviceType.getDeviceKind().equalsIgnoreCase(DeviceKind.Gaming.toString()))
//         {
//            mElements.add(new MenuMasterElement(R.drawable.selector_menu_input_gaming_button, deviceType.getDisplayName()));      
//         }
//         else if (deviceType.getDeviceKind().equalsIgnoreCase(DeviceKind.MediaDevices.toString()))
//         {
//            mElements.add(0, new MenuMasterElement(R.drawable.selector_menu_input_media_devices_button, deviceType.getDisplayName()));      
//         }
//         else if (deviceType.getDeviceKind().equalsIgnoreCase(DeviceKind.TV.toString()))
//         {
//            mElements.add(0, new MenuMasterElement(R.drawable.selector_menu_input_tv_button, deviceType.getDisplayName()));     
//         }
//         else if (deviceType.getDeviceKind().equalsIgnoreCase(DeviceKind.Security.toString()))
//         {
//            mElements.add(0, new MenuMasterElement(R.drawable.selector_menu_input_security_button, deviceType.getDisplayName()));      
//         }

//      }

        //edit by watershao

        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean mode = sp.getBoolean("default_zaxel", false);
        if (mode) {
            mElements.add(new MenuMasterElement(R.drawable.selector_menu_input_zaxel_button, getString(R.string.zaxel)));
        }else{
            mElements.add(new MenuMasterElement(R.drawable.selector_menu_input_external_input_button, getString(R.string.external_input)));
        }
        mElements.add(new MenuMasterElement(R.drawable.selector_menu_input_gaming_button, getString(R.string.gaming)));
//        mElements.add(new MenuMasterElement(R.drawable.selector_menu_input_music_button, getString(R.string.music)));
        mElements.add(new MenuMasterElement(R.drawable.selector_menu_input_output_button, getString(R.string.security_cam)));
        mElements.add(new MenuMasterElement(R.drawable.selector_menu_input_tv_button, getString(R.string.tv)));
        mElements.add(new MenuMasterElement(R.drawable.selector_menu_input_media_devices_button, getString(R.string.media_devices)));
        mElements.add(new MenuMasterElement(R.drawable.selector_menu_input_movie_button, getString(R.string.movie)));

        mCircularMenu = (SemiCircularMenu) view.findViewById(R.id.circular_menu);
        setmResource(R.layout.layout_semicircular_menu_input);
        mCircularMenu.setLayoutResource(getmResource(), new OnClickHitButton() {

            @Override
            public void onShow() {
                eventBus.unregister(MenuInputsFragment.this);
                eventBus.post(new CloseFragmentEvent());
                eventBus.register(MenuInputsFragment.this);
                show();
            }
        });
        int i = 0;
        for (MenuMasterElement menuMasterElement : mElements) {
            ImageView item = new ImageView(getActivity());
            item.setId(menuMasterElement.getmResource());
            IPTTextView textView = new IPTTextView(getActivity());
            textView.setText(menuMasterElement.getText());
            textView.setTextSize(20);
            textView.setId(menuMasterElement.getmResource() + 1);
            textView.setSingleLine(true);
            item.setImageResource(menuMasterElement.getmResource());

            item.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.drawable.selector_menu_input_zaxel_button:
                            ZaxelActivity.fire(getActivity());
//                            ZaxelRemoteFullActivity.fire();
                            break;
                        case R.drawable.selector_menu_input_external_input_button:
//                            ExternalInputActivity.fire(getActivity());
                            ExternalInputActivity.fire(getActivity(), null, menuMasterElement.getText().toUpperCase(), 0);
                            break;
                        case R.drawable.selector_menu_input_output_button:
//                            KaraokeActivity.fire(getActivity());
                            KaraokeActivity.fire(getActivity(),"",menuMasterElement.getText().toUpperCase(),0);

                            break;
                        case R.drawable.selector_menu_input_media_devices_button:
                            MediaDeviceActivity.fire(getActivity());
                            break;
                        case R.drawable.selector_menu_input_tv_button:
                            TVActivity.fire(getActivity());
                            break;
                        case R.drawable.selector_menu_input_movie_button:
                            MovieLibraryActivity.fireToFront(getActivity());
                            break;
                        case R.drawable.selector_menu_input_music_button:
                            MusicLibraryActivity.fire(getActivity());

                            break;
                        case R.drawable.selector_menu_input_gaming_button:
//                            GamingActivity.fire(getActivity());
                            GamingActivity.fire(getActivity(),null,"GAME", Constants.DEVICE_ID_GAME);

                            break;
                        default:
                            Log.d(TAG, "Hiting transparent element");
                    }
                }
            });
            mCircularMenu.addItem(createRelativeLayout(item, textView, i));
            i++;
        }

    }

    /**
     * @param imageView
     * @param textView
     * @param position
     * @return
     */
    private RelativeLayout createRelativeLayout(ImageView imageView, IPTTextView textView, int position) {
        RelativeLayout layout = new RelativeLayout(getActivity());
        RelativeLayout.LayoutParams lpTextView = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpTextView.addRule(RelativeLayout.CENTER_VERTICAL);
        layout.addView(textView, lpTextView);
        RelativeLayout.LayoutParams lpImageView = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpImageView.addRule(RelativeLayout.RIGHT_OF, textView.getId());
        layout.addView(imageView, lpImageView);
        layout.setGravity(Gravity.RIGHT);

//      RelativeLayout layout = new RelativeLayout(getActivity());
//      RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//
//      switch (position)
//      {
//      case 0:
//         layout.addView(imageView);
//
//         // layout.addView(textView, params);
//         break;
//      case 1:
//
//         layout.addView(imageView, params);
//         // layout.addView(textView, params);
//         break;
//
//      case 2:
//         layout.addView(imageView, params);
//         // layout.addView(textView, params);
//         break;
//      case 3:
//         layout.addView(imageView, params);
//         // layout.addView(textView, params);
//         break;
//      case 4:
//         layout.addView(imageView, params);
//         // layout.addView(textView, params);
//         break;
//      case 5:
//         layout.addView(imageView, params);
//         // layout.addView(textView, params);
//         break;
//      case 6:
//         layout.addView(imageView, params);
//         // layout.addView(textView, params);
//         break;
//      case 7:
//         layout.addView(imageView, params);
//         // layout.addView(textView, params);
//         break;
//
//      }

        return layout;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    @Override
    public void onResume() {
        super.onResume();

        // fix for Multi-view Null crashes
//      controller.getActiveDeviceTypes();
    }

    @Override
    protected void show() {
        mForeground = !mForeground;
        mCircularMenu.switchState(true);
    }

}
