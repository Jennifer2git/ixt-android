package com.imax.ipt.controller.inputs;

import com.imax.ipt.common.Constants;
import com.imax.ipt.controller.BaseController;
import com.imax.ipt.controller.GlobalController;
import com.imax.ipt.model.DeviceType;
import com.imax.ipt.ui.activity.input.MenuInputsFragment;

import java.util.Vector;

public class MenuInputsController extends BaseController {
    public static final String TAG = "MenuInputsController";

    private MenuInputsFragment menuInputsFragment;

    public MenuInputsController(MenuInputsFragment menuInputsFragment) {
        this.menuInputsFragment = menuInputsFragment;
    }

    @Override
    public void onDestroy() {
    }

    public void getActiveDeviceTypes() {
//        Vector<DeviceType> deviceTypes = (Vector<DeviceType>) IPT.getInstance().getIPTContext().get(IPT.ACTIVE_DEVICE_TYPES);
        Vector<DeviceType> deviceTypes = (Vector<DeviceType>) GlobalController.getInstance().readConfigFromPreference(Constants.CONFIG_KEY_ACTIVE_DEVICE);
        if (deviceTypes == null) {
            return;
        }
        menuInputsFragment.populateDeviceTypes(deviceTypes.toArray(new DeviceType[deviceTypes.size()]));
    }

}
