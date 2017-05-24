package com.imax.ipt.controller.inputs;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.imax.ipt.IPT;
import com.imax.ipt.controller.BaseController;
import com.imax.ipt.controller.eventbus.handler.input.GetInputsByDeviceKindHandler;
import com.imax.ipt.controller.eventbus.handler.input.GetSecurityCameraLocations;
import com.imax.ipt.controller.eventbus.handler.input.SelectSecurityCameraLocationHandler;
import com.imax.ipt.controller.eventbus.handler.input.SetNowPlayingInputHandler;
import com.imax.ipt.controller.eventbus.handler.push.SelectedSecutiyCameraLocationChangedEvent;
import com.imax.ipt.model.Input;
import com.imax.ipt.model.SecurityCameraLocation;
import com.imax.ipt.ui.activity.input.security.SecurityCamActivity;
import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable.DeviceKind;
import com.imax.iptevent.EventBus;

public class SecurityController extends BaseController {
    private EventBus mEventBus;
    private SecurityCamActivity securityCameraAcitivity;

    private List<SecurityCameraLocation> mSecurityCamLocations;

    public SecurityController(SecurityCamActivity securityCameraAcitivity) {
        this.mEventBus = this.getEventBus();
        this.mEventBus.register(this);

        this.securityCameraAcitivity = securityCameraAcitivity;
    }

    /**
     *
     */
    public void init() {
        Map<DeviceKind, Vector<Input>> inputsByDeviceKind = (Map<DeviceKind, Vector<Input>>) IPT.getInstance().getIPTContext().get(IPT.INPUTS_BY_DEVICE_KIND);
        Vector<Input> securityInputs = inputsByDeviceKind.get(DeviceKind.Security);
        if (securityInputs != null && securityInputs.size() > 0) {
            mEventBus.post(new SetNowPlayingInputHandler(securityInputs.get(0).getId()).getRequest());
        }
//      this.mEventBus.post(new GetInputsByDeviceKindHandler(DeviceKind.Security.toString()).getRequest());          

        this.getSecurityCameraLocation();
    }

    /**
     *
     */
    @Override
    public void onDestroy() {
        this.mEventBus.unregister(this);
    }

    public void SelectSecurityCameraLocation(int locationId) {
        this.mEventBus.post(new SelectSecurityCameraLocationHandler(locationId).getRequest());
    }

    /**
     *
     */
    private void getSecurityCameraLocation() {
        this.mEventBus.post(new GetSecurityCameraLocations().getRequest());
    }

    /**
     *
     */
    public void onEvent(final GetSecurityCameraLocations getSecurityCameraLocations) {
        securityCameraAcitivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                securityCameraAcitivity.displayCameraLocations(getSecurityCameraLocations.getmSecurityCamLocations());
                securityCameraAcitivity.toggleSelectedLocation(getSecurityCameraLocations.getSelectedId());
            }
        });
    }

    public void onEvent(final SelectedSecutiyCameraLocationChangedEvent selectedSecutiyCameraLocationChangedEvent) {
        securityCameraAcitivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                securityCameraAcitivity.toggleSelectedLocation(selectedSecutiyCameraLocationChangedEvent.getLocationId());
            }
        });
    }

//   public void onEvent(final GetInputsByDeviceKindHandler getInputsByDeviceKindHandler)
//   {           
//      if (getInputsByDeviceKindHandler.getInputs().length > 0 && getInputsByDeviceKindHandler.getInputs()[0].getDeviceKind() == DeviceKind.Security)
//      {
//         mEventBus.post(new SetNowPlayingInputHandler(getInputsByDeviceKindHandler.getInputs()[0].getId()).getRequest());         
//      }      
//   }

//   public void ChangeVideoSource()
//   {
//      //this.mEventBus.post(new SetNowPlayingInputHandler(inputId) SelectSecurityCameraLocationHandler(locationId).getRequest());
//   }

}
