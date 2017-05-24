package com.imax.ipt.ui.activity.settings.maintenance;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.remote.ControlFocusHandler;
import com.imax.iptevent.EventBus;

public class ControlFocusFragment extends Fragment {
    private View mFocusLayout;
    private EditText mEditTextPassword;
    private ImageView mImgViewLogin;
    private ImageButton mBtnUp;
    private ImageButton mBtnDown;
    private ImageButton mBtnLeft;
    private ImageButton mBtnRight;
    private EventBus mEventBus;

    private String FocusStart_Left = "LeftFocusStart";
    private String FocusFar_Left = "LeftFocusFar";
    private String FocusNear_Left = "LeftFocusNear";
    private String FocusStart_Right = "RightFocusStart";
    private String FocusFar_Right = "RightFocusFar";
    private String FocusNear_Right = "RightFocusNear";

    public  static int focusState = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFocusLayout = inflater.inflate(R.layout.fragment_maintenance_focus_en, null);
        this.init(mFocusLayout);
        return mFocusLayout;
    }

    private void init(View view) {
        this.mEventBus = IPT.getInstance().getEventBus();
        this.mBtnUp = (ImageButton)mFocusLayout.findViewById(R.id.btnUp);
        this.mBtnDown = (ImageButton)mFocusLayout.findViewById(R.id.btnDown);

        this.mBtnLeft = (ImageButton)mFocusLayout.findViewById(R.id.btnLeft);
        this.mBtnRight = (ImageButton)mFocusLayout.findViewById(R.id.btnRight);

        this.mBtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mEventBus.post(new ControlFocusHandler(FocusStart_Left).getParameters());
                //todo: add judge the state of focus: 10/20/-1/0...
                mEventBus.post(new ControlFocusHandler(FocusNear_Left).getParameters());
            }
        });

        this.mBtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo send cmd :
//                mEventBus.post(new ControlFocusHandler(xxxx).getParameters());
            }
        });




    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
