package com.imax.ipt.controller.settings;

import android.util.Log;
import com.imax.ipt.IPT;
import com.imax.ipt.controller.eventbus.handler.power.GetPowerSocketStateHandler;
import com.imax.ipt.controller.eventbus.handler.power.GetSystemPowerStateHandler;
import com.imax.ipt.controller.eventbus.handler.power.RebootPowerSocketHandler;
import com.imax.ipt.controller.eventbus.handler.push.PowerSocketStateChangedEvent;
import com.imax.ipt.controller.eventbus.handler.push.SystemPowerStateChangedEvent;
import com.imax.ipt.ui.activity.settings.maintenance.RebootFragment;
import com.imax.iptevent.EventBus;

public class RebootController {

    private EventBus mEventBus;
    private RebootFragment mView;

    public RebootController(RebootFragment view) {
        mView = view;
        mEventBus = IPT.getInstance().getEventBus();
        mEventBus.register(this);
//		mEventBus.post(new GetSystemPowerStateHandler().getRequest());
//		mEventBus.post(new GetPowerSocketStateHandler().getRequest());
        // todo send event to get the active devices.
    }

    public void destroy() {
        mEventBus.unregister(this);
    }

    public void onEvent(
            SystemPowerStateChangedEvent systemPowerStateChangedEvent) {
    }

    public void onEvent(GetSystemPowerStateHandler handler) {

    }

    public void RebootPowerSocket(int powerSocket) {
        mEventBus.post(new RebootPowerSocketHandler(powerSocket).getRequest());
    }

    public void onEventMainThread(RebootPowerSocketHandler handler) {
        Log.d("RebootController", "RebootPowerSocketHandler");
    }

    public void onEventMainThread(GetPowerSocketStateHandler handler) {
        Integer[] powerSockets = handler.getPowerSockets();
        Integer[] powerStates = handler.getPowerStates();
//		mView.initPowerSocketStates(powerSockets, powerStates);
    }

    public void onEventMainThread(PowerSocketStateChangedEvent handler) {
//		mView.changePowerSocketState(handler.getPowerSocket(),
//				handler.getPowerState());

    }
}
