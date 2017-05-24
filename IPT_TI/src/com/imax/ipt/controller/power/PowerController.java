package com.imax.ipt.controller.power;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.imax.ipt.R;
import com.imax.ipt.controller.BaseController;
import com.imax.ipt.controller.eventbus.handler.power.SwitchSystemPowerHandler;
import com.imax.ipt.service.IPTService;
import com.imax.ipt.common.PowerState;
import com.imax.ipt.ui.activity.power.SystemCountUpDownActivity;
import com.imax.ipt.ui.activity.power.SystemOffActivity;
import com.imax.iptevent.EventBus;

public class PowerController extends BaseController {
    public static final String TAG = "PowerController";

    public static final int NONE = 0;
    public static final int PowerControlDeviceNotConnected = 1;
    public static final int PoweringUpInProgress = 2;
    public static final int PoweringDownInProgress = 3;

    private SystemOffActivity systemOffActivity;
    private EventBus mEventBus;

    /**
     * @param systemOffActivity
     */
    public PowerController(SystemOffActivity systemOffActivity) {
        this.systemOffActivity = systemOffActivity;
        this.mEventBus = getEventBus();
        this.mEventBus.register(this);
    }

    @Override
    public void onDestroy() {
        this.mEventBus.unregister(this);
    }

    /**
     * @param on_off
     */
    public void switchSystemPower(boolean on_off) {
        this.mEventBus.post(new SwitchSystemPowerHandler(on_off).getRequest());
    }

    public void onEvent(SwitchSystemPowerHandler switchSystemPowerHandler) {
        switch (switchSystemPowerHandler.getState()) {
            // should not advance to movie library screen if an power action is taking place
//      case NONE:
//         MovieLibraryActivity.fire(systemOffActivity);
//         break;

            case PowerControlDeviceNotConnected:
                systemOffActivity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(systemOffActivity, R.string.power_device_not_connected, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                });
                break;

            case PoweringUpInProgress: {
                Intent iptSystemPowerStateChangedIntent = new Intent(IPTService.BROADCAST_ACTION_SYSTEM_POWERING_ON);
                LocalBroadcastManager.getInstance(systemOffActivity).sendBroadcast(iptSystemPowerStateChangedIntent);

                systemOffActivity.finish();
                SystemCountUpDownActivity.fire(systemOffActivity, PowerState.PoweringOn);
                break;
            }

            case PoweringDownInProgress: {
                Intent iptSystemPowerStateChangedIntent = new Intent(IPTService.BROADCAST_ACTION_SYSTEM_POWERING_OFF);
                LocalBroadcastManager.getInstance(systemOffActivity).sendBroadcast(iptSystemPowerStateChangedIntent);

                systemOffActivity.finish();
                SystemCountUpDownActivity.fire(systemOffActivity, PowerState.PoweringOff);
                break;
            }

            default:
                Log.d(TAG, "Default case on power off");
                break;
        }
    }

//   
//   public void onEvent(SystemPowerStateChangedEvent systemPowerStateChangedEvent)
//   {
//      if (systemPowerStateChangedEvent.getmPowerState() == PowerState.Off) {
//         mWelcomeActivity.DisplayPowerOnButton();
//      }
//   }   
}
