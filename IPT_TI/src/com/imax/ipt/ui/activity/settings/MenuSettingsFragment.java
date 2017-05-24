package com.imax.ipt.ui.activity.settings;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.ui.CloseFragmentEvent;
import com.imax.ipt.ui.activity.menu.MenuItemFragment;
import com.imax.ipt.ui.activity.settings.maintenance.MaintenceActivity;
import com.imax.ipt.ui.activity.settings.multiview.MultiViewActivity;
import com.imax.ipt.ui.activity.settings.preferences.OtherActivity;
import com.imax.ipt.ui.activity.settings.preferences.PreferencesActivity;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.viewmodel.MenuMasterElement;
import com.imax.ipt.ui.widget.semicircular.SemiCircularMenu;
import com.imax.ipt.ui.widget.semicircular.SemiCircularMenu.OnClickHitButton;

public class MenuSettingsFragment extends MenuItemFragment {
    private static final String TAG = "MenuSettingsFragment";
    private View mSettingsLayout;
    private SemiCircularMenu mCircularMenu;
    private List<MenuMasterElement> mElements = new ArrayList<MenuMasterElement>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSettingsLayout = inflater.inflate(R.layout.fragment_settings, null);
        this.init(mSettingsLayout);
        return mSettingsLayout;
    }

    private void init(View view) {
        mElements.add(new MenuMasterElement(R.drawable.selector_menu_settings_multiview_button, getString(R.string.others)));
        mElements.add(new MenuMasterElement(R.drawable.selector_menu_settings_maintence_button, getString(R.string.maintenance)));
        mElements.add(new MenuMasterElement(R.drawable.selector_menu_settings_pref_button, getString(R.string.preferences)));

        mCircularMenu = (SemiCircularMenu) view.findViewById(R.id.circular_menu);
        this.setmResource(R.layout.layout_semicircular_menu_settings);
        mCircularMenu.setLayoutResource(getmResource(), new OnClickHitButton() {
            @Override
            public void onShow() {
                eventBus.unregister(MenuSettingsFragment.this);
                eventBus.post(new CloseFragmentEvent());
                eventBus.register(MenuSettingsFragment.this);
                show();
            }
        });

        int i = 0;
        for (MenuMasterElement masterElement : mElements) {
            ImageView item = new ImageView(getActivity());
            item.setId(masterElement.getmResource());
            IPTTextView textView = new IPTTextView(getActivity());
            textView.setText(masterElement.getText());
            textView.setTextSize(18);
            textView.setId(masterElement.getmResource() + 1);
            item.setImageResource(masterElement.getmResource());


            item.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.drawable.selector_menu_settings_maintence_button:
                            MaintenceActivity.fire(getActivity());
                            break;
                        case R.drawable.selector_menu_settings_multiview_button:
                            OtherActivity.fire(getActivity());
                            break;
                        case R.drawable.selector_menu_settings_pref_button:
                            PreferencesActivity.fire(getActivity());
                            break;
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
     * @param id
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
        return layout;
    }

    /**
     *
     */
    @Override
    protected void show() {
        mForeground = !mForeground;
        this.mCircularMenu.switchState(true);
    }

}
