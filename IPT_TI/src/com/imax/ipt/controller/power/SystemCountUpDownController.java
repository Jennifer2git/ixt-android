package com.imax.ipt.controller.power;

import com.imax.ipt.common.PowerState;
import com.imax.ipt.controller.BaseController;
import com.imax.ipt.controller.eventbus.handler.power.GetSystemPowerProgressHandler;
import com.imax.ipt.controller.eventbus.handler.push.PcabCalibrationStateChangedEvent;
import com.imax.ipt.controller.eventbus.handler.push.SystemPowerProgressChangedEvent;
import com.imax.ipt.controller.eventbus.handler.push.SystemPowerStateChangedEvent;
import com.imax.ipt.ui.activity.power.SystemCountUpDownActivity;
import com.imax.iptevent.EventBus;

public class SystemCountUpDownController extends BaseController {
    private SystemCountUpDownActivity mSystemCountUpDownActivity;
    private EventBus mEventBus;

    public SystemCountUpDownController(SystemCountUpDownActivity countUpDownActivity) {
        this.mSystemCountUpDownActivity = countUpDownActivity;
        this.mEventBus = getEventBus();
        this.mEventBus.register(this);

        // retrieve the initial progress
//        this.mEventBus.post(new GetSystemPowerProgressHandler().getRequest());
    }

    public void onResume() {
        // retrieve the initial progress
//        this.mEventBus.post(new GetSystemPowerProgressHandler().getRequest());
    }

    /**
     *
     */
    public void cancel() {
//      this.mEventBus.post(new SwitchSystemPowerHandler(false));
//      SystemOffActivity.fire(mSystemCountUpDownActivity);
    }


    @Override
    public void onDestroy() {
        this.mEventBus.unregister(this);
    }

    /**
     * Server interaction
     */
    public void onEvent(final SystemPowerProgressChangedEvent systemPowerProgressChangedEvent) {
        mSystemCountUpDownActivity.updateProgress(systemPowerProgressChangedEvent.getmEstimatedSecondsRemaining(), systemPowerProgressChangedEvent.getmPercentCompleted());
    }

    public void onEvent(final SystemPowerStateChangedEvent systemPowerStateChangedEvent) {
        if (systemPowerStateChangedEvent.getmPowerState() == PowerState.On
                || systemPowerStateChangedEvent.getmPowerState() == PowerState.Off) {

            mSystemCountUpDownActivity.finish();
        }

    }

    public void onEvent(final GetSystemPowerProgressHandler getSystemPowerProgressHandler) {
//        mSystemCountUpDownActivity.updateProgress(getSystemPowerProgressHandler.getmEstimatedSecondsRemaining(), getSystemPowerProgressHandler.getmPercentCompleted());
    }


    public void onEventMainThread(PcabCalibrationStateChangedEvent pcabCalibrationStateChangedEvent) {
        if (pcabCalibrationStateChangedEvent.getPcabState() == 1 && pcabCalibrationStateChangedEvent.getProgress() != -1) {

            mSystemCountUpDownActivity.updatePcabCalibration(pcabCalibrationStateChangedEvent.getProgress());
        }
    }
}
