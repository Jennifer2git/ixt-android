package com.imax.ipt.controller.settings;

import com.imax.ipt.IPT;
import com.imax.ipt.controller.eventbus.handler.push.PcabCalibrationStateChangedEvent;
import com.imax.ipt.controller.eventbus.handler.settings.maintenance.GetPcabCalibrationStatusHandler;
import com.imax.ipt.controller.eventbus.handler.settings.maintenance.GetProjectorLampStatusHandler;
import com.imax.ipt.controller.eventbus.handler.settings.maintenance.SwitchPcabCalibrationHandler;
import com.imax.ipt.controller.eventbus.handler.settings.maintenance.SwitchProjectorLampHandler;
import com.imax.ipt.ui.activity.settings.maintenance.PCABFragment;
import com.imax.ipt.ui.activity.settings.maintenance.ProjectorFragment;
import com.imax.iptevent.EventBus;

// server functions : TmsJsonIptService
public class PcabController {

    private EventBus mEventBus;
    private PCABFragment mPcabFragment;

    public PcabController(PCABFragment pcabFragment) {
        mPcabFragment = pcabFragment;
        mEventBus = IPT.getInstance().getEventBus();
        mEventBus.register(this);

        mEventBus.post(new GetPcabCalibrationStatusHandler().getRequest());
    }

    public void switchPcabCalibration(boolean on) {
        mEventBus.post(new SwitchPcabCalibrationHandler(on)
                .getRequest());
    }

    public void getPcabState() {
        mEventBus.post(new GetPcabCalibrationStatusHandler().getRequest());
    }

    public void onEventMainThread(SwitchPcabCalibrationHandler handler) {
        if (handler.getResponse() == 0) {
            mPcabFragment.switchPcabCalibration(true);
        } else {
            // command failed
        }
    }

    public void onEventMainThread(GetPcabCalibrationStatusHandler handler) {
        mPcabFragment.notifyPcabStatus(handler);
    }

    public void onEventMainThread(PcabCalibrationStateChangedEvent handler) {
        if (handler.getPcabState() == 1) {
//			mPcabFragment.switchPcabCalibration(false);
//			mEventBus.post(new GetPcabCalibrationStatusHandler().getRequest());

            mPcabFragment.updateProgress(handler.getProgress());
            if (handler.getProgress() == 100) {
                mPcabFragment.calibrationDone();
            }
        }

    }

    public void destroy() {
        mEventBus.unregister(this);
    }
}
