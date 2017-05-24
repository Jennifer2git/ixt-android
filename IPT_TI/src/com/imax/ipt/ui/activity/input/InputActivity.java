package com.imax.ipt.ui.activity.input;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridLayout;
import android.widget.Toast;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.input.SetNowPlayingInputHandler;
import com.imax.ipt.controller.inputs.InputController;
import com.imax.ipt.model.Input;
import com.imax.ipt.ui.activity.BaseActivity;
import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable.DeviceKind;
import com.imax.ipt.ui.viewmodel.RemoteControlUtil;

import java.util.List;

public abstract class InputActivity extends BaseActivity implements OnClickListener {
    public static final String TAG = "InputActivity";

    private List<Input> mInputs;
    private LayoutInflater mInflater;
    protected InputController mInputController;
    protected GridLayout mGridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mInputController = new InputController(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInputController.onDestroy();
    }

    public void setupView(List<Input> inputs) {
        this.mInputs = inputs;
        if (mInputs.size() == 0) {
            return;
        }
        for (int i = 0; i < mInputs.size(); i++) {
            Log.d(TAG, "mInputs = " + i + mInputs.get(i).getDisplayName());
        }

        Input input = mInputs.get(0);
        Log.d(TAG, "getDeviceKind = " + input.getDeviceKind());

        if (input.getDeviceKind() == DeviceKind.Imax || input.getDeviceKind() == DeviceKind.OnlineMovie) {
            mInputController.getEventBus().post(new SetNowPlayingInputHandler(input.getId()).getRequest());
            RemoteControlUtil.openRemoteControl(getApplicationContext(), input.getId(), input.getDisplayName(), input.getDeviceKind(), input.isIrSupported());
            return;
        }

        if (input.getDeviceKind() == DeviceKind.Zaxel) {
            mInputController.getEventBus().post(new SetNowPlayingInputHandler(input.getId()).getRequest());
            RemoteControlUtil.openRemoteControl(getApplicationContext(), input.getId(), input.getDisplayName(), input.getDeviceKind(), input.isIrSupported());
            return;
        }

        if (input.getDeviceKind() == DeviceKind.Game || input.getDeviceKind() == DeviceKind.Karaoke
                || input.getDeviceKind() == DeviceKind.Extender) {
            mInputController.getEventBus().post(new SetNowPlayingInputHandler(input.getId()).getRequest());
        }

        switch (input.getDeviceKind()) {
            case Imax:
                RemoteControlUtil.openRemoteControl(getApplicationContext(), input.getId(), input.getDisplayName(), input.getDeviceKind(), input.isIrSupported());
                break;
            case OnlineMovie:
                RemoteControlUtil.openRemoteControl(getApplicationContext(), input.getId(), input.getDisplayName(), input.getDeviceKind(), input.isIrSupported());
                break;
            case Oppo:
                break;
            case Himedia:
                break;
            case Kaleidescape:
                break;
            case Bestv:
                break;
            case Game:
                break;
            case Karaoke:
                break;
            case Gdc:
                break;
            case Extender:
                break;
            case Zaxel:
                RemoteControlUtil.openRemoteControl(getApplicationContext(), input.getId(), input.getDisplayName(), input.getDeviceKind(), input.isIrSupported());
                break;
        }
        mInputController.getEventBus().post(new SetNowPlayingInputHandler(input.getId()).getRequest());

    }

    /**
     *
     */
    @Override
    public void onClick(View v) {
        Integer position = (Integer) v.getTag();
        Input input = mInputs.get(position);

        if (input.getDeviceKind() == DeviceKind.Game) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.gaming_prompt, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, -300);
            toast.show();
            return;
        }

        if (input.getDeviceKind() == DeviceKind.Karaoke) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.external_input_karaoke_prompt, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, -300);
            toast.show();
            return;
        }

        if (input.getDeviceKind() == DeviceKind.Extender) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.external_input_prompt, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, -300);
            toast.show();
            return;
        }

        mInputController.getEventBus().post(new SetNowPlayingInputHandler(input.getId()).getRequest());

        //mInputController.getEventBus().post(new SetSelectedInputHandler(position, deviceType.getId()).getRequest());
        RemoteControlUtil.openRemoteControl(getApplicationContext(), input.getId(), input.getDisplayName(), input.getDeviceKind(), input.isIrSupported());
    }
}
