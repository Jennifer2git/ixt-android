package com.imax.ipt.ui.activity.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.imax.ipt.IPT;
import com.imax.ipt.controller.eventbus.handler.ui.CloseFragmentEvent;
import com.imax.iptevent.EventBus;

public abstract class MenuItemFragment extends Fragment {
    public static final String TAG = "MenuItemFragment";
    private int mResource;
    protected boolean mForeground = false;
    protected EventBus eventBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.eventBus = IPT.getInstance().getEventBus();
        this.eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.eventBus.unregister(this);
    }

    /**
     *
     */
    protected abstract void show();


    public void onEvent(CloseFragmentEvent event) //
    {
        if (isForeground()) {
            this.show();
        }
    }

    /**
     * Getters and Setters
     */

    public boolean isForeground() {
        return mForeground;
    }

    public void setmForeground(boolean mForeground) {
        this.mForeground = mForeground;
    }

    public int getmResource() {
        return mResource;
    }

    public void setmResource(int mResource) {
        this.mResource = mResource;
    }

}
