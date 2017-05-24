package com.imax.ipt.controller.room;

import android.util.Log;

import com.imax.ipt.controller.BaseController;
import com.imax.ipt.controller.eventbus.handler.push.LightLevelChangedEvent;
import com.imax.ipt.controller.eventbus.handler.push.SelectedLightingPresetChangedEvent;
import com.imax.ipt.controller.eventbus.handler.rooms.*;
import com.imax.ipt.ui.activity.room.LightingActivity;
import com.imax.iptevent.EventBus;

public class LigthingController extends BaseController {
    private LightingActivity mLightingActivity;
    private EventBus mEventBus;

    public LigthingController(LightingActivity lightingActivity) {
        this.mEventBus = getEventBus();
        this.mEventBus.register(this);
        this.mLightingActivity = lightingActivity;
        this.init();
    }

    private void init() {
        this.getLigthingPresets();

//      this.mEventBus.post(new GetLightLevelHandler().getRequest());
    }

    public void onResume() {
//      this.mEventBus.post(new GetLightLevelHandler().getRequest());
    }

    public void onPause() {
        this.mEventBus.unregister(this);
    }

    @Override
    public void onDestroy() {
//      this.mEventBus.unregister(this);
    }

    public void controlLighting(int cmd) {
        this.mEventBus.post(new ControlLightingHandler(cmd).getRequest());
    }

    /**
     *
     * @param time
     */
    public void setLightingDelayTime(int time){
        this.mEventBus.post(new SetLightingDelayTimeHandler(time).getRequest());
    }
    /**
     * @param upDown
     */
    public void adjustLightLevel(boolean upDown) {
        this.mEventBus.post(new AdjustLightLevelHandler(upDown).getRequest());
    }

    /**
     * @param lightingPresetId
     */
    public void selectLightingPreset(int lightingPresetId) {
        this.mEventBus.post(new SelectLightingPresetHandler(lightingPresetId).getRequest());
    }

    /**
     * Server interaction
     */
    private void getLigthingPresets() {
//      this.mEventBus.post(new GetLightingPresetsHandler().getRequest());
    }

    /**
     *
     * @param lightingHandler
     */
    public void onEvent(final ControlLightingHandler lightingHandler) {
        int res = lightingHandler.getResponse();
        if (res == 1) {
            Log.d("tag", "success");
        }

    }

    public void onEvent(final SelectedLightingPresetChangedEvent selectedLightingPresetChangedEvent) {
        mLightingActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//            mLightingActivity.enableSelectedPreset(selectedLightingPresetChangedEvent.getLightingPresetId());
            }
        });
    }

    public void onEvent(LightLevelChangedEvent adjustLightLevelEvent) {
//      mLightingActivity.setLevel(adjustLightLevelEvent.getmLightLevel());
    }

    public void onEvent(GetLightLevelHandler getLightLevelHandler) {
//      mLightingActivity.setLevel(getLightLevelHandler.getLightLevel());
    }
}
