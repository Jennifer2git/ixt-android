package com.imax.ipt.ui.activity.settings.multiview;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;

import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.input.GetInputsByDeviceKindHandler;
import com.imax.ipt.controller.eventbus.handler.input.SetSelectedInputHandler;
import com.imax.ipt.model.Input;
import com.imax.ipt.ui.adapters.InputSelectionAdapter;
import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable.DeviceKind;
import com.imax.iptevent.EventBus;

public class DialogMultiviewSelectInputFragment extends DialogFragment implements OnClickListener, OnItemClickListener {
    public static final String OUTPUT_POSITION = "outPutPosition";

    private EventBus mEventBus;
    private ImageButton mBtnClose;
    private GridView gridView;
    private InputSelectionAdapter mInputSelectionAdapter;
    private List<Input> inputs;
    private Integer outputPostion;

    @Override
    public View onCreateView(LayoutInflater mInflater, ViewGroup container, Bundle savedInstanceState) {
        View mViewDialogFragment = mInflater.inflate(R.layout.fragment_dialog_multiview_select_input, null);

        Dialog d = getDialog();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());
        lp.width = 1300;
        lp.height = 700;
        lp.x = 10;
        lp.y = 5;

        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        d.show();
        d.getWindow().setAttributes(lp);

        this.mEventBus = IPT.getInstance().getEventBus();
        this.mEventBus.register(this);

        this.init(mViewDialogFragment);
        return mViewDialogFragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mEventBus.unregister(this);
    }

    private void init(View view) {
        this.gridView = (GridView) view.findViewById(R.id.gridViewInputSelection);
        this.inputs = new ArrayList<Input>();
        this.mInputSelectionAdapter = new InputSelectionAdapter(getActivity(), R.layout.item_select_input, inputs);
        this.gridView.setAdapter(mInputSelectionAdapter);
        this.gridView.setOnItemClickListener(this);
        this.mEventBus = IPT.getInstance().getEventBus();
        this.mBtnClose = (ImageButton) view.findViewById(R.id.btnClose);
        this.mBtnClose.setOnClickListener(this);
        this.outputPostion = getArguments().getInt(OUTPUT_POSITION, 0);
        this.sendGetInputsByDeviceKindRequestEvent();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClose:
                dismiss();
                break;
        }
    }


    /**
     * Server interaction
     */

    private void sendGetInputsByDeviceKindRequestEvent() {
        this.mEventBus.post(new GetInputsByDeviceKindHandler(DeviceKind.NotSet.getDeviceKind()).getRequest());
    }


    public void onEvent(GetInputsByDeviceKindHandler getInputsByDeviceKindHandler) {
        final Input[] inputs = getInputsByDeviceKindHandler.getInputs();
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mInputSelectionAdapter.clear();
                mInputSelectionAdapter.addAll(inputs);
                mInputSelectionAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
        Input input = inputs.get(position);
        mEventBus.post(new SetSelectedInputHandler(outputPostion, input.getId()).getRequest());
        dismiss();
    }
}
