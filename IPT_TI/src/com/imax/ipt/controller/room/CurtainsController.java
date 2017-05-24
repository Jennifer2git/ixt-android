package com.imax.ipt.controller.room;

import android.view.View;
import android.widget.Toast;

import com.imax.ipt.controller.BaseController;
import com.imax.ipt.controller.eventbus.handler.push.CurtainStateChangedEvent;
import com.imax.ipt.controller.eventbus.handler.rooms.GetCurtainStateHandler;
import com.imax.ipt.controller.eventbus.handler.rooms.GetCurtainStateHandler.CurtainState;
import com.imax.ipt.controller.eventbus.handler.rooms.MoveCurtainHandler;
import com.imax.ipt.ui.activity.room.CurtainsActivity;
import com.imax.iptevent.EventBus;

public class CurtainsController extends BaseController {
    private EventBus mEventBus;
    private CurtainsActivity mCurtainsActivity;
    private boolean curtainState;

    public CurtainsController(CurtainsActivity curtainsActivity) {
        this.mEventBus = getEventBus();
        this.mEventBus.register(this);
        this.mCurtainsActivity = curtainsActivity;
        this.init();
    }

    private void init() {
        this.getCurtainState();
    }

    @Override
    public void onDestroy() {
        this.mEventBus.unregister(this);
    }

    /**
     * Server interaction
     */
    private void getCurtainState() {
        this.mEventBus.post(new GetCurtainStateHandler().getRequest());
    }

    public void onEventMainThread(GetCurtainStateHandler getCurtainStateHandler) {
        //int state = getCurtainStateHandler.getStateCurtain();
        //Toast.makeText(mCurtainsActivity, "Curtain State :" + state, Toast.LENGTH_LONG).show();

//      CurtainState curtainState = getCurtainStateHandler.getCurtainState();
//      Toast.makeText(mCurtainsActivity, "Curtain State :" + curtainState.toString(), Toast.LENGTH_LONG).show();
    }

    public void onEvent(final CurtainStateChangedEvent curtainStateChangedEvent) {
        mCurtainsActivity.CurtainStateChanged(curtainStateChangedEvent.getCurtainState());
    }

    public void openCurtain() {
        this.mEventBus.post(new MoveCurtainHandler(true).getRequest());
    }

    public void closeCurtain() {
        this.mEventBus.post(new MoveCurtainHandler(false).getRequest());
    }
}
