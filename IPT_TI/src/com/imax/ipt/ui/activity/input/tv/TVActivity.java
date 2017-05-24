package com.imax.ipt.ui.activity.input.tv;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.GridLayout;
import com.imax.ipt.R;
import com.imax.ipt.ui.activity.input.InputActivity;
import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable;
import com.imax.ipt.ui.viewmodel.MenuLibraryElement;

import java.util.ArrayList;
import java.util.List;

public class TVActivity extends InputActivity implements OnClickListener {
    private List<MenuLibraryElement> mMenuOptions = new ArrayList<MenuLibraryElement>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_tv);
        this.addMenuFragment(getResources().getString(R.string.tv));
        this.addMenuLibraryFragment(getResources().getString(R.string.tv), mMenuOptions, 300);
        this.init();
    }

    protected void init() {
        this.mGridLayout = (GridLayout) findViewById(R.id.gridLayout);
        this.setupView(mInputController.getInputs(FactoryDeviceTypeDrawable.DeviceKind.OnlineMovie));
        MenuLibraryElement menuConsole = new MenuLibraryElement(1, getResources().getString(R.string.source), null, null);
        mMenuOptions.add(menuConsole);
    }

    /**
     * @param mContext
     */
    public static void fire(Context mContext) {
        Intent intent = new Intent(mContext, TVActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

}
