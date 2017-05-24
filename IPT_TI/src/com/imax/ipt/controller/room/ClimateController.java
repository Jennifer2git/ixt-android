package com.imax.ipt.controller.room;

import com.imax.ipt.controller.BaseController;
import com.imax.ipt.controller.eventbus.handler.push.ActualTemperatureChangedEvent;
import com.imax.ipt.controller.eventbus.handler.push.FanStateChangedEvent;
import com.imax.ipt.controller.eventbus.handler.push.HvacSystemFaultStateChangedEvent;
import com.imax.ipt.controller.eventbus.handler.push.FanStateChangedEvent.FanState;
import com.imax.ipt.controller.eventbus.handler.push.SetpointTemperatureChangedEvent;
import com.imax.ipt.controller.eventbus.handler.rooms.GetHvacStatesHandler;
import com.imax.ipt.controller.eventbus.handler.rooms.climate.AdjustCurrentSetPointHandler;
import com.imax.ipt.controller.eventbus.handler.rooms.climate.SetFanStateHandler;
import com.imax.ipt.ui.activity.room.ClimateActivity;
import com.imax.iptevent.EventBus;

public class ClimateController extends BaseController {
    private EventBus mEventBus;
    private ClimateActivity mClimateActivity;
    public static final int FANHRENHEIT = 0;
    public static final int CELSIUS = 1;

    private FanState fanState;

    public ClimateController(ClimateActivity climateActivity) {
        this.mClimateActivity = climateActivity;
        this.mEventBus = getEventBus();
        this.mEventBus.register(this);
        this.getHvacStates();
    }

    @Override
    public void onDestroy() {
        this.mEventBus.unregister(this);
    }

    public void adjustCurrentSetpoint(boolean upDown) {
        this.mEventBus.post(new AdjustCurrentSetPointHandler(upDown).getRequest());
    }

    public void setFanStatus(int state) {
        this.mEventBus.post(new SetFanStateHandler(state).getRequest());
    }

    /**
     *
     */
    private void getHvacStates() {
        this.mEventBus.post(new GetHvacStatesHandler().getRequest());
    }

    /**
     * @param getHvacStatesHandler
     */
    public void onEvent(GetHvacStatesHandler getHvacStatesHandler) {
        fanState = getHvacStatesHandler.getmFanState();
        this.mClimateActivity.updateHvacState(getHvacStatesHandler.getmUnit(), getHvacStatesHandler.getmValueSetPoint(), getHvacStatesHandler.getmCurrentTemperature(), getHvacStatesHandler.getmFanState(), getHvacStatesHandler.ismIsFault(), getHvacStatesHandler.getmDescription());
    }

    public void onEvent(SetpointTemperatureChangedEvent changedEvent) {
        this.mClimateActivity.updateSetPoint(changedEvent.getSetPoint(), changedEvent.getUnit());
    }

    public void onEvent(ActualTemperatureChangedEvent changedEvent) {
        this.mClimateActivity.updateTemperature(changedEvent.getCurrentTemperature(), changedEvent.getUnit());
    }

    public void onEvent(final FanStateChangedEvent fanStateChangedEvent) {
        fanState = fanStateChangedEvent.getFanState();

        mClimateActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mClimateActivity.updateFanState(fanStateChangedEvent.getFanState());
            }
        });
    }

    public void onEvent(final HvacSystemFaultStateChangedEvent hvacSystemFaultStateChangedEvent) {
        mClimateActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mClimateActivity.updateFaultDescription(hvacSystemFaultStateChangedEvent.isFault(), hvacSystemFaultStateChangedEvent.getFaultDescription());
            }
        });
    }

    public void switchFanState() {
        switch (fanState) {
            case On:
                setFanStatus(FanState.Auto.getValue());
                break;
            case Auto:
                setFanStatus(FanState.On.getValue());
                break;
        }
    }
}
