package com.imax.ipt.ui.activity.settings.multiview;

import java.util.ResourceBundle.Control;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.ui.SetupMultiviewEvent;
import com.imax.ipt.controller.eventbus.handler.ui.SetupMultiviewEvent.MultiView;
import com.imax.ipt.controller.settings.MultiViewController;
import com.imax.iptevent.EventBus;

public class DialogMultiviewLayoutFragment extends DialogFragment implements OnClickListener {
    public static final String TAG = "DialogMultiviewLayoutFragment";

    public static final String CURRENT_MULTIVIEW = "CURRENT_MULTIVIEW";
    private EventBus mEventBus;
    private ImageButton mBtnMultiviewFull;
    private ImageButton mBtnMultiview4up;
    private ImageButton mBtnMultiview3Top;
    private ImageButton mBtnMultiview3Rigth;
    private ImageButton mBtnMultiview3Bottom;
    private ImageButton mBtnClose;

    private MultiViewController mMultiViewController;

    @Override
    public View onCreateView(LayoutInflater mInflater, ViewGroup container, Bundle savedInstanceState) {
        View mViewDialogFragment = mInflater.inflate(R.layout.fragment_dialog_multiview_layout, null);

        Dialog d = getDialog();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());
        lp.width = 1300;
        lp.height = 300;
        lp.x = 10;
        lp.y = 325;
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        d.show();
        d.getWindow().setAttributes(lp);

        this.init(mViewDialogFragment);
        return mViewDialogFragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void SetController(MultiViewController multiViewController) {
        mMultiViewController = multiViewController;
    }

    private void init(View view) {
        this.mEventBus = IPT.getInstance().getEventBus();

        mBtnMultiviewFull = (ImageButton) view.findViewById(R.id.btnMultiViewFull);
        mBtnMultiviewFull.setOnClickListener(this);

        mBtnMultiview4up = (ImageButton) view.findViewById(R.id.btnMultiView4Up);
        mBtnMultiview4up.setOnClickListener(this);

        mBtnMultiview3Top = (ImageButton) view.findViewById(R.id.btnMultiView3top);
        mBtnMultiview3Top.setOnClickListener(this);

        mBtnMultiview3Rigth = (ImageButton) view.findViewById(R.id.btnMultiView3right);
        mBtnMultiview3Rigth.setOnClickListener(this);

        mBtnMultiview3Bottom = (ImageButton) view.findViewById(R.id.btnMultiView3bottom);
        mBtnMultiview3Bottom.setOnClickListener(this);

        mBtnClose = (ImageButton) view.findViewById(R.id.btnClose);
        mBtnClose.setOnClickListener(this);

        this.setupLayout();
    }

    private void setupLayout() {
        MultiView view = MultiView.getMultiView(getArguments().getInt(CURRENT_MULTIVIEW));
        switch (view) {
            case FOUR_UP:
                this.mBtnMultiviewFull.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_layout_btn_full_inactive));
                this.mBtnMultiview4up.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_layout_btn_4up_active));
                this.mBtnMultiview3Top.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_layout_btn_3top_inactive));
                this.mBtnMultiview3Rigth.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_layout_btn_3right_inactive));
                this.mBtnMultiview3Bottom.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_layout_btn_3bottom_inactive));

                break;
            case SINGLE:
                this.mBtnMultiviewFull.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_layout_btn_full_active));
                this.mBtnMultiview4up.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_layout_btn_4up_inactive));
                this.mBtnMultiview3Top.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_layout_btn_3top_inactive));
                this.mBtnMultiview3Rigth.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_layout_btn_3right_inactive));
                this.mBtnMultiview3Bottom.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_layout_btn_3bottom_inactive));

                break;
            case THREE_BOTTOM:
                this.mBtnMultiviewFull.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_layout_btn_full_inactive));
                this.mBtnMultiview4up.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_layout_btn_4up_inactive));
                this.mBtnMultiview3Top.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_layout_btn_3top_inactive));
                this.mBtnMultiview3Rigth.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_layout_btn_3right_inactive));
                this.mBtnMultiview3Bottom.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_layout_btn_3bottom_active));

                break;
            case THREE_RIGTH:
                this.mBtnMultiviewFull.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_layout_btn_full_inactive));
                this.mBtnMultiview4up.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_layout_btn_4up_inactive));
                this.mBtnMultiview3Top.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_layout_btn_3top_inactive));
                this.mBtnMultiview3Rigth.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_layout_btn_3right_active));
                this.mBtnMultiview3Bottom.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_layout_btn_3bottom_inactive));

                break;
            case THREE_TOP:
                this.mBtnMultiviewFull.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_layout_btn_full_inactive));
                this.mBtnMultiview4up.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_layout_btn_4up_inactive));
                this.mBtnMultiview3Top.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_layout_btn_3top_active));
                this.mBtnMultiview3Rigth.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_layout_btn_3right_inactive));
                this.mBtnMultiview3Bottom.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_layout_btn_3bottom_inactive));
                break;
            default:
                Log.d(TAG, "Entro to default case ");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMultiViewFull:
                mMultiViewController.setPipMode(MultiView.SINGLE);
//         mEventBus.post(new SetupMultiviewEvent(MultiView.SINGLE));
                dismiss();
                break;
            case R.id.btnMultiView4Up:
                mMultiViewController.setPipMode(MultiView.FOUR_UP);
//         mEventBus.post(new SetupMultiviewEvent(MultiView.FOUR_UP));
                dismiss();
                break;
            case R.id.btnMultiView3top:
                mMultiViewController.setPipMode(MultiView.THREE_TOP);
//         mEventBus.post(new SetupMultiviewEvent(MultiView.THREE_TOP));
                dismiss();
                break;
            case R.id.btnMultiView3right:
                mMultiViewController.setPipMode(MultiView.THREE_RIGTH);
//         mEventBus.post(new SetupMultiviewEvent(MultiView.THREE_RIGTH));
                dismiss();
                break;
            case R.id.btnMultiView3bottom:
                mMultiViewController.setPipMode(MultiView.THREE_BOTTOM);
//         mEventBus.post(new SetupMultiviewEvent(MultiView.THREE_BOTTOM));
                dismiss();
                break;
            case R.id.btnClose:
                dismiss();
                break;
        }
    }

}
