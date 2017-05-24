package com.imax.ipt.controller.settings;

import com.imax.ipt.IPT;
import com.imax.ipt.controller.eventbus.handler.settings.maintenance.GetVersionHandler;
import com.imax.ipt.controller.eventbus.handler.settings.maintenance.ResetProjectorLampTimeHandler;
import com.imax.ipt.ui.activity.settings.preferences.OtherActivity;
import com.imax.iptevent.EventBus;

// server functions : TmsJsonIptService
public class OtherController {

    private EventBus mEventBus;
    private OtherActivity otherActivity;

    public OtherController(OtherActivity otherActivity) {
        this.otherActivity = otherActivity;
        mEventBus = IPT.getInstance().getEventBus();
        mEventBus.register(this);

        mEventBus.post(new GetVersionHandler().getRequest());
    }

    public void getVersion() {
        mEventBus.post(new GetVersionHandler().getRequest());
    }

    public void resetProjectorLampTime(int position) {
        mEventBus.post(new ResetProjectorLampTimeHandler(position).getRequest());
    }


    public void onEventMainThread(GetVersionHandler handler) {
        otherActivity.showVersion(handler);
    }

    public void destroy() {
        mEventBus.unregister(this);
    }
}
