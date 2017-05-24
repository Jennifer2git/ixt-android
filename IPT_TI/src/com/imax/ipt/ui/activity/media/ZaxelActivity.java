package com.imax.ipt.ui.activity.media;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View.OnClickListener;
import com.imax.ipt.R;
import com.imax.ipt.ui.activity.input.InputActivity;
import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable.DeviceKind;
import com.imax.ipt.ui.viewmodel.MenuLibraryElement;

import java.util.ArrayList;
import java.util.List;

public class ZaxelActivity extends InputActivity implements OnClickListener {
    private List<MenuLibraryElement> mMenuOptions = new ArrayList<MenuLibraryElement>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_tv);
        this.init();
    }


    protected void init() {
//        this.mGridLayout = (GridLayout) findViewById(R.id.gridLayout);
        addMenuFragment();
        this.setupView(mInputController.getInputs(DeviceKind.Zaxel));// to set the view to start zaxelremotefullactivity
        MenuLibraryElement menuConsole = new MenuLibraryElement(1, getResources().getString(R.string.source), null, null);
        mMenuOptions.add(menuConsole);
    }

    /**
     * @param mContext
     */
    public static void fire(Context mContext) {
        Intent intent = new Intent(mContext, ZaxelActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

}
