package com.imax.ipt.controller;

import android.util.Log;

import com.imax.ipt.IPT;
import com.imax.iptevent.EventBus;

public abstract class BaseController {
    private static final String TAG = "BaseController";

    public static final String DEVICE_TYPES = "DEVICE_TYPES";

    protected EventBus mEventBus;

    public BaseController() {
        this.mEventBus = IPT.getInstance().getEventBus();
    }


    public EventBus getEventBus() {
        return mEventBus;
    }

    public void setEventBus(EventBus mEventBus) {
        this.mEventBus = mEventBus;
    }


    public abstract void onDestroy();

    /**
     * @param key
     * @return
     */
    public int getIntContextValue(int key) {
        Object value = getValue(key);

        if (value instanceof Integer) {
            return (Integer) value;
        } else {
            throw new IllegalArgumentException("Use different method to extract the value");
        }
    }

    /**
     * @param key
     * @param value
     */
    public void put(Object key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null :" + key);
        }
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null  :" + value);
        }
        IPT.getInstance().getIPTContext().put(key, value);
    }

    /**
     * @param key
     * @return
     */
    public Object getValue(Object key) {
        Object value = null;
        if ((value = IPT.getInstance().getIPTContext().get(key)) == null) {
            //throw new IllegalArgumentException("There is not a value for id :" + key);
            Log.e(TAG, "There is not a value for id :" + key);
        }
        return value;
    }

    public boolean containsValue(Object key) {
        return IPT.getInstance().getIPTContext().contains(key);
    }

}
