package com.imax.ipt.ui.activity.input.media;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridLayout;
import com.imax.ipt.R;
import com.imax.ipt.ui.activity.input.InputActivity;
import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable.DeviceKind;
import com.imax.ipt.ui.viewmodel.MenuLibraryElement;

import java.util.ArrayList;
import java.util.List;

public class MediaDeviceActivity extends InputActivity {
    private List<MenuLibraryElement> mMenuOptions = new ArrayList<MenuLibraryElement>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_media_devices);
        this.addMenuFragment();
        this.addMenuLibraryFragment(getResources().getString(R.string.media_devices), mMenuOptions, 600);
        this.init();

    }


    protected void init() {
        this.mGridLayout = (GridLayout) findViewById(R.id.gridLayout);
        this.setupView(mInputController.getInputs(DeviceKind.Imax));//here call PrimaRemoteActivity
        MenuLibraryElement menuConsole = new MenuLibraryElement(1, getResources().getString(R.string.source), null, null);
        mMenuOptions.add(menuConsole);
    }

    /**
     * @param mContext
     */
    public static void fire(Context mContext) {
        Intent intent = new Intent(mContext, MediaDeviceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}