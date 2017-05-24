package com.imax.ipt.controller.settings;

import android.util.Log;
import com.imax.ipt.IPT;
import com.imax.ipt.controller.BaseController;
import com.imax.ipt.controller.eventbus.handler.push.MaintenanceLoginSessionExpiredEvent;
import com.imax.ipt.controller.eventbus.handler.settings.maintenance.MaintenanceLoginHandler;
import com.imax.ipt.controller.eventbus.handler.ui.MediaMenuLibraryEvent;
import com.imax.ipt.controller.eventbus.handler.ui.MediaMenuLibraryEvent.MenuEvent;
import com.imax.ipt.ui.activity.menu.MenuLibraryFragment;
import com.imax.ipt.ui.activity.settings.maintenance.MaintenceActivity;
import com.imax.ipt.ui.util.VersionUtil;
import com.imax.iptevent.EventBus;

public class MaintanceController extends BaseController {

    public static final String TAG = "MaintanceController";

    private EventBus mEventBus;
    private MaintenceActivity mMaintenceActivity;
    private int mCurrentLogin;

    public MaintanceController(MaintenceActivity maintenceActivity) {
        this.mMaintenceActivity = maintenceActivity;
        this.mEventBus = getEventBus();
        this.mEventBus.register(this);
        MenuLibraryFragment.setPosition(0);

        mCurrentLogin = (Integer) getValue(IPT.MAINTENANCE_LOGIN);
        switch (mCurrentLogin) {
            case MaintenanceLoginHandler.MAINTENANCE_RESPONSE_OK:
                mMaintenceActivity.showFragment(MenuEvent.SETTINGS);

                break;
            case MaintenanceLoginHandler.MAINTENANCE_RESPONSE_BAD:
                mMaintenceActivity.showFragment(MenuEvent.LOGIN);
                break;
            default:
                Log.d(TAG, "Default case for Maintenance");
                break;
        }
    }

    @Override
    public void onDestroy() {
        this.mEventBus.unregister(this);
    }

    /**
     * @param menuMovieLibraryEvent
     */
    public void onEventMainThread(final MediaMenuLibraryEvent menuMovieLibraryEvent) {
        if (mCurrentLogin == MaintenanceLoginHandler.MAINTENANCE_RESPONSE_BAD) {
            this.mMaintenceActivity.showFragment(MenuEvent.LOGIN);
            return;
        }
        switch (menuMovieLibraryEvent.getEvent()) {
            case SETTINGS:
//         Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
//         dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//         mMaintenceActivity.startActivity(dialogIntent);

                this.mMaintenceActivity.showFragment(MenuEvent.SETTINGS);
                break;
            case REBOOT_DEVICES:
                this.mMaintenceActivity.showFragment(MenuEvent.REBOOT_DEVICES);
                break;
            case PROJECTOR:
                this.mMaintenceActivity.showFragment(MenuEvent.PROJECTOR);
                break;

            case DEMO:
                this.mMaintenceActivity.showFragment(MenuEvent.DEMO);
                break;

            case FOCUS:
                this.mMaintenceActivity.showFragment(MenuEvent.FOCUS);
                break;
            case VERSION:
                VersionUtil.showVersion(mMaintenceActivity);
                break;

            default:
                break;
        }
    }

    /**
     * @param maintenanceLoginHandler
     */
    public void onEvent(MaintenanceLoginHandler maintenanceLoginHandler) {
        // PUT ON THE CONTEXT
        this.put(IPT.MAINTENANCE_LOGIN, maintenanceLoginHandler.getmLoginResponse());
        this.mCurrentLogin = maintenanceLoginHandler.getmLoginResponse();
        switch (maintenanceLoginHandler.getmLoginResponse()) {
            case MaintenanceLoginHandler.MAINTENANCE_RESPONSE_OK:
                mMaintenceActivity.loginOn();
//         mMaintenceActivity.showFragment(MenuEvent.SETTINGS);
                int pos = MenuLibraryFragment.getPosition();
                if (pos == 0) {
                    mMaintenceActivity.showFragment(MenuEvent.SETTINGS);
                }
                if (pos == 1) {
                    mMaintenceActivity.showFragment(MenuEvent.PROJECTOR);
                }
                if (pos == 2) {
                    mMaintenceActivity.showFragment(MenuEvent.REBOOT_DEVICES);
                }
                if (pos == 3) {
                    mMaintenceActivity.showFragment(MenuEvent.DEMO);
                }
                if (pos == 5) {
                    mMaintenceActivity.showFragment(MenuEvent.FOCUS);
                }
                break;

            case MaintenanceLoginHandler.MAINTENANCE_RESPONSE_BAD:
                mMaintenceActivity.loginOff();
                break;
            default:
                Log.d(TAG, "Maintenance Login Handler Default Case");
                break;
        }
    }

    /**
     * @param maintenanceLoginSessionExpiredEvent
     */
    public void onEvent(MaintenanceLoginSessionExpiredEvent maintenanceLoginSessionExpiredEvent) {
        IPT.getInstance().getIPTContext().put(IPT.MAINTENANCE_LOGIN, MaintenanceLoginHandler.MAINTENANCE_RESPONSE_BAD);
        mMaintenceActivity.showFragment(MenuEvent.LOGIN);
    }

}
