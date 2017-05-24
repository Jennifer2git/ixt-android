package com.imax.ipt.controller.settings;

import org.apache.commons.net.nntp.NewGroupsOrNewsQuery;

import android.R.integer;

import com.imax.ipt.IPT;
import com.imax.ipt.controller.eventbus.handler.settings.maintenance.GetProjectorLampStatusHandler;
import com.imax.ipt.controller.eventbus.handler.settings.maintenance.GetVersionHandler;
import com.imax.ipt.controller.eventbus.handler.settings.maintenance.ResetProjectorLampTimeHandler;
import com.imax.ipt.controller.eventbus.handler.settings.maintenance.SwitchProjectorLampHandler;
import com.imax.ipt.ui.activity.settings.maintenance.ProjectorFragment;
import com.imax.iptevent.EventBus;

// server functions : TmsJsonIptService
public class ProjectorController {

    private EventBus mEventBus;
    private ProjectorFragment mProjectorFragment;

    public ProjectorController(ProjectorFragment projectorFragment) {
        mProjectorFragment = projectorFragment;
        mEventBus = IPT.getInstance().getEventBus();
        mEventBus.register(this);

        mEventBus.post(new GetProjectorLampStatusHandler().getRequest());

    }

    public void switchProjector(int position, boolean on) {
        mEventBus.post(new SwitchProjectorLampHandler(position, on)
                .getRequest());
    }

    public void resetProjectorLampTime(int position) {
        mEventBus.post(new ResetProjectorLampTimeHandler(position).getRequest());
    }

    public void onEventMainThread(ResetProjectorLampTimeHandler handler) {
        if (handler.getResponse() != 0) {
            // command failed
        }
    }

    public void onEventMainThread(SwitchProjectorLampHandler handler) {
        if (handler.getResponse() != 0) {
            // command failed
        }
    }

    public void onEventMainThread(GetProjectorLampStatusHandler handler) {
        mProjectorFragment.notifyProjectorStatus(handler);
    }


    public void destroy() {
        mEventBus.unregister(this);
    }
}
