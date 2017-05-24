package com.imax.ipt.controller.settings;

import com.imax.ipt.IPT;
import com.imax.ipt.controller.eventbus.handler.push.PcabCalibrationStateChangedEvent;
import com.imax.ipt.controller.eventbus.handler.settings.maintenance.GetPcabCalibrationStatusHandler;
import com.imax.ipt.controller.eventbus.handler.settings.maintenance.GetProjectorLampStatusHandler;
import com.imax.ipt.controller.eventbus.handler.settings.maintenance.SwitchPcabCalibrationHandler;
import com.imax.ipt.controller.eventbus.handler.settings.maintenance.SwitchProjectorLampHandler;
import com.imax.ipt.ui.activity.settings.maintenance.MaintenceGeneralFragment;
import com.imax.ipt.ui.activity.settings.maintenance.PCABFragment;
import com.imax.ipt.ui.activity.settings.maintenance.ProjectorFragment;
import com.imax.iptevent.EventBus;

// server functions : TmsJsonIptService
public class DemoController {

    private EventBus mEventBus;
    private MaintenceGeneralFragment generalFragment;

    public DemoController(MaintenceGeneralFragment generalFragment) {
        this.generalFragment = generalFragment;
        mEventBus = IPT.getInstance().getEventBus();
        mEventBus.register(this);

//		mEventBus.post(new GetPcabCalibrationStatusHandler().getRequest());
    }


    public void onEventMainThread(GetPcabCalibrationStatusHandler handler) {
//		mPcabFragment.notifyPcabStatus(handler);
    }


    public void destroy() {
        mEventBus.unregister(this);
    }
}
