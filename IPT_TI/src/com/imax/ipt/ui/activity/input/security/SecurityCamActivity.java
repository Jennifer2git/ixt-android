package com.imax.ipt.ui.activity.input.security;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.imax.ipt.R;
import com.imax.ipt.controller.inputs.SecurityController;
import com.imax.ipt.model.SecurityCameraLocation;
import com.imax.ipt.ui.activity.input.InputActivity;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable;
import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable.DeviceKind;
import com.imax.ipt.ui.viewmodel.MenuLibraryElement;

public class SecurityCamActivity extends InputActivity {
    private SecurityController mSecurityController;
    private GridLayout mGridLayout;

    private List<MenuLibraryElement> mMenuOptions = new ArrayList<MenuLibraryElement>();
    private LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_security_cameras);
        this.addMenuFragment();
        this.addMenuLibraryFragment(getResources().getString(R.string.security_cam), mMenuOptions, 500);

        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.mSecurityController = new SecurityController(this);
        this.init();
    }

    protected void init() {
        this.mGridLayout = (GridLayout) findViewById(R.id.gridLayout);
//      this.setupView(mSecurityController.getDevicesType(DeviceKind.Security));
        this.mSecurityController.init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSecurityController.onDestroy();
    }

    /**
     * @param mContext
     */
    public static void fire(Context mContext) {
        Intent intent = new Intent(mContext, SecurityCamActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    public void displayCameraLocations(List<SecurityCameraLocation> getmSecurityCamLocations) {
        for (SecurityCameraLocation securityCameraLocation : getmSecurityCamLocations) {
            View inflateView = mInflater.inflate(R.layout.item_select_input, null);
            inflateView.setId(R.layout.item_select_input);
            inflateView.setOnClickListener(this);
//         inflateView.setTag(i);
            inflateView.setTag(securityCameraLocation.getId());
            ImageView imageView = (ImageView) inflateView.findViewById(R.id.btnSource);
            Drawable drawable = FactoryDeviceTypeDrawable.createDeviceTypeDrawable(getResources(), DeviceKind.Security);
            imageView.setImageDrawable(drawable);
            IPTTextView iptTextView = (IPTTextView) inflateView.findViewById(R.id.txtSource);
//         iptTextView.setText(deviceType.getDisplayName());
            iptTextView.setText(securityCameraLocation.getLocationDisplayName());
            mGridLayout.addView(inflateView);
        }
    }

    @Override
    public void onClick(View v) {
        // send API to select Security Camera
        mSecurityController.SelectSecurityCameraLocation(Integer.parseInt(v.getTag().toString()));
    }

    public void toggleSelectedLocation(int selectedLocationId) {
        for (int i = 0; i < mGridLayout.getChildCount(); i++) {
            View layoutView = mGridLayout.getChildAt(i);
            ImageView imageView = (ImageView) layoutView.findViewById(R.id.btnSource);

            int id = Integer.parseInt(layoutView.getTag().toString());
            if (id == selectedLocationId) {
                imageView.setImageResource(R.drawable.ipt_gui_asset_security_camera_select_btn_active);
            } else {
                Drawable drawable = FactoryDeviceTypeDrawable.createDeviceTypeDrawable(getResources(), DeviceKind.Security);
                imageView.setImageDrawable(drawable);
            }
        }
    }
}
