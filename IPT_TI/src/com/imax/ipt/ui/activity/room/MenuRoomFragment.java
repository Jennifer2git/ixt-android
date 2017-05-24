package com.imax.ipt.ui.activity.room;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.rooms.GetCurtainStateHandler.CurtainState;
import com.imax.ipt.controller.eventbus.handler.ui.CloseFragmentEvent;
import com.imax.ipt.ui.activity.menu.MenuItemFragment;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.viewmodel.MenuMasterElement;
import com.imax.ipt.ui.widget.semicircular.SemiCircularMenu;
import com.imax.ipt.ui.widget.semicircular.SemiCircularMenu.OnClickHitButton;

public class MenuRoomFragment extends MenuItemFragment {
    private View mRoomsLayout;
    private SemiCircularMenu mSemiCircularMenu;
    private List<MenuMasterElement> mElements = new ArrayList<MenuMasterElement>();

    /**
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoomsLayout = inflater.inflate(R.layout.fragment_rooms, null);
        this.init(mRoomsLayout);
        return mRoomsLayout;
    }

    /**
     * @param view
     */
    private void init(View view) {
        //IPT.getInstance().getIPTContext().put(IPT.STATE_CURTAINS, getCurtainStateHandler.getStateCurtain());

        mElements.add(new MenuMasterElement(R.drawable.selector_menu_rooms_climate_button, getString(R.string.climate)));
        mElements.add(new MenuMasterElement(R.drawable.selector_menu_rooms_lighting_button, getString(R.string.lighting)));
        CurtainState curtainState = (CurtainState) (IPT.getInstance().getIPTContext().get(IPT.STATE_CURTAINS));
        if (curtainState != CurtainState.NotAvailable) {
            mElements.add(new MenuMasterElement(R.drawable.selector_menu_rooms_curtains_button, getString(R.string.curtains)));
        }

        mSemiCircularMenu = (SemiCircularMenu) view.findViewById(R.id.circular_menu);
        setmResource(R.layout.layout_semicircular_menu_rooms);
        mSemiCircularMenu.setLayoutResource(getmResource(), new OnClickHitButton() {

            @Override
            public void onShow() {
                eventBus.unregister(MenuRoomFragment.this);
                eventBus.post(new CloseFragmentEvent());
                eventBus.register(MenuRoomFragment.this);
                show();

            }
        });

        int i = 0;
        for (MenuMasterElement menuMasterElement : mElements) {
            ImageView item = new ImageView(getActivity());
            item.setId(menuMasterElement.getmResource());
            IPTTextView textView = new IPTTextView(getActivity());
            textView.setText(menuMasterElement.getText());
            textView.setTextSize(18);
            textView.setId(menuMasterElement.getmResource() + 1);
            item.setImageResource(menuMasterElement.getmResource());

            //added by watershao

            if ((textView.getId() - 1) == R.drawable.selector_menu_rooms_climate_button) {
                textView.setEnabled(false);
                item.setEnabled(false);

                textView.setVisibility(View.INVISIBLE);
                item.setVisibility(View.INVISIBLE);
            }
            if ((textView.getId() - 1) == R.drawable.selector_menu_rooms_curtains_button) {
                textView.setEnabled(false);
                item.setEnabled(false);

                textView.setVisibility(View.INVISIBLE);
                item.setVisibility(View.INVISIBLE);
            }

            item.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.drawable.selector_menu_rooms_climate_button:
                            ClimateActivity.fire(getActivity());
                            break;
                        case R.drawable.selector_menu_rooms_curtains_button:
                            CurtainsActivity.fire(getActivity());
                            break;
                        case R.drawable.selector_menu_rooms_lighting_button:
                            LightingActivity.fire(getActivity());
                            break;
                    }
                }
            });
            mSemiCircularMenu.addItem(createRelativeLayout(item, textView, i));
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
        return layout;
    }

    @Override
    protected void show() {
        mForeground = !mForeground;
        mSemiCircularMenu.switchState(true);
    }

}
