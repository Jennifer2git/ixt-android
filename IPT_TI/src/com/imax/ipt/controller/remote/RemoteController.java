package com.imax.ipt.controller.remote;

import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.ir.IRPulseEvent;
import com.imax.ipt.controller.eventbus.handler.remote.ExecuteRemoteControlHandler;
import com.imax.ipt.ui.activity.media.MediaRemote.ExecuteRemoteControl;
import com.imax.ipt.ui.activity.settings.multiview.MultiViewActivity;
import com.imax.iptevent.EventBus;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

public class RemoteController implements OnClickListener {

    private Activity mActivity;
    private EventBus mEventBus;
    private int inputId;

    public RemoteController(Activity activity, int inputId) {
        mActivity = activity;
        this.inputId = inputId;

        mEventBus = IPT.getInstance().getEventBus();
        mEventBus.register(this);
    }

    public void destroy() {
        mEventBus.unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                mActivity.finish();
                break;
            case R.id.btnMultiview:
                MultiViewActivity.fire(mActivity);
                break;
            case R.id.txtTvOk:
                mEventBus.post(new IRPulseEvent(inputId, 39).getRequest());
                break;
            case R.id.btnUp:
                mEventBus.post(new IRPulseEvent(inputId, 12).getRequest());
                break;
            case R.id.btnDown:
                mEventBus.post(new IRPulseEvent(inputId, 13).getRequest());
                break;
            case R.id.btnLeft:
                mEventBus.post(new IRPulseEvent(inputId, 14).getRequest());
                break;
            case R.id.btnRigth:
                mEventBus.post(new IRPulseEvent(inputId, 15).getRequest());
                break;
            case R.id.btnMenu:
                mEventBus.post(new IRPulseEvent(inputId, 7).getRequest());
                break;
            case R.id.btnPlayPause:
                mEventBus.post(new IRPulseEvent(inputId, 0).getRequest());
                break;
        }
    }

    public void onEvent(IRPulseEvent event) {
        // event bus require at least one onEvent method
    }
}
