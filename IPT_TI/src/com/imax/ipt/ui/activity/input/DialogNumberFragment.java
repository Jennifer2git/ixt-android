package com.imax.ipt.ui.activity.input;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.R.layout;
import com.imax.ipt.controller.eventbus.handler.remote.ExecuteRemoteControlHandler;
import com.imax.ipt.ui.activity.media.MediaRemote;
import com.imax.ipt.ui.util.DisplayUtil;
import com.imax.iptevent.EventBus;

/**
 * A simple {@link Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link DialogNumberFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the
 * {@link DialogNumberFragment#newInstance} factory method to create an
 * instance of this fragment.
 */
public class DialogNumberFragment extends DialogFragment implements OnClickListener {
    public static final String TAG = "DialogNumberFragment";

    private EventBus mEventBus;
    private Button mBtnOK;
    private Button mBtnCancel;
    private Button mBtnNum0;
    private Button mBtnNum1;
    private Button mBtnNum2;
    private Button mBtnNum3;
    private Button mBtnNum4;
    private Button mBtnNum5;
    private Button mBtnNum6;
    private Button mBtnNum7;
    private Button mBtnNum8;
    private Button mBtnNum9;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "inputId";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int inputId;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of this fragment using
     * the provided parameters.
     */
    public static DialogNumberFragment newInstance(int inputId) {
        DialogNumberFragment fragment = new DialogNumberFragment();
        Bundle args = new Bundle();
//        args.putInt(ARG_PARAM1, inputId);
//        fragment.setArguments(args);
        return fragment;
    }

    public DialogNumberFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//			mParam1 = getArguments().getString(ARG_PARAM1);
//			mParam2 = getArguments().getString(ARG_PARAM2);

            inputId = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mViewDialogFragment = inflater.inflate(layout.fragment_dialog_number, container, false);
        Dialog d = getDialog();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());
        lp.width = DisplayUtil.dip2px(inflater.getContext(), 450);
        lp.height = DisplayUtil.dip2px(inflater.getContext(), 450);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.night_blue));
////		
        d.show();
        d.getWindow().setAttributes(lp);

        this.init(mViewDialogFragment);

        return mViewDialogFragment;
    }

    private void init(View view) {
        this.mEventBus = IPT.getInstance().getEventBus();
//        this.mEventBus.register(this);

        mBtnOK = (Button) view.findViewById(R.id.btnOk);
        mBtnOK.setOnClickListener(this);

        mBtnCancel = (Button) view.findViewById(R.id.btnCancel);
        mBtnCancel.setOnClickListener(this);

        mBtnNum0 = (Button) view.findViewById(R.id.num0);
        mBtnNum0.setOnClickListener(this);
        mBtnNum1 = (Button) view.findViewById(R.id.num1);
        mBtnNum1.setOnClickListener(this);
        mBtnNum2 = (Button) view.findViewById(R.id.num2);
        mBtnNum2.setOnClickListener(this);
        mBtnNum3 = (Button) view.findViewById(R.id.num3);
        mBtnNum3.setOnClickListener(this);
        mBtnNum4 = (Button) view.findViewById(R.id.num4);
        mBtnNum4.setOnClickListener(this);
        mBtnNum5 = (Button) view.findViewById(R.id.num5);
        mBtnNum5.setOnClickListener(this);
        mBtnNum6 = (Button) view.findViewById(R.id.num6);
        mBtnNum6.setOnClickListener(this);
        mBtnNum7 = (Button) view.findViewById(R.id.num7);
        mBtnNum7.setOnClickListener(this);
        mBtnNum8 = (Button) view.findViewById(R.id.num8);
        mBtnNum8.setOnClickListener(this);
        mBtnNum9 = (Button) view.findViewById(R.id.num9);
        mBtnNum9.setOnClickListener(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mEventBus.unregister(this);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated to
     * the activity and potentially other fragments contained in that activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                this.mEventBus.post(new ExecuteRemoteControlHandler(MediaRemote.ExecuteRemoteControl.Enter.toString()).getRequest());
                dismiss();
                break;

            case R.id.btnCancel:
                this.mEventBus.post(new ExecuteRemoteControlHandler(MediaRemote.ExecuteRemoteControl.BackMenu.toString()).getRequest());
                dismiss();
                break;

            case R.id.num0:
                this.mEventBus.post(new ExecuteRemoteControlHandler(MediaRemote.ExecuteRemoteControl.Key0.toString()).getRequest());
                break;

            case R.id.num1:
                this.mEventBus.post(new ExecuteRemoteControlHandler(MediaRemote.ExecuteRemoteControl.Key1.toString()).getRequest());
                break;

            case R.id.num2:
                this.mEventBus.post(new ExecuteRemoteControlHandler(MediaRemote.ExecuteRemoteControl.Key2.toString()).getRequest());
                break;

            case R.id.num3:
                this.mEventBus.post(new ExecuteRemoteControlHandler(MediaRemote.ExecuteRemoteControl.Key3.toString()).getRequest());
                break;
            case R.id.num4:
                this.mEventBus.post(new ExecuteRemoteControlHandler(MediaRemote.ExecuteRemoteControl.Key4.toString()).getRequest());
                break;
            case R.id.num5:
                this.mEventBus.post(new ExecuteRemoteControlHandler(MediaRemote.ExecuteRemoteControl.Key5.toString()).getRequest());
                break;
            case R.id.num6:
                this.mEventBus.post(new ExecuteRemoteControlHandler(MediaRemote.ExecuteRemoteControl.Key6.toString()).getRequest());
                break;
            case R.id.num7:
                this.mEventBus.post(new ExecuteRemoteControlHandler(MediaRemote.ExecuteRemoteControl.Key7.toString()).getRequest());
                break;
            case R.id.num8:
                this.mEventBus.post(new ExecuteRemoteControlHandler(MediaRemote.ExecuteRemoteControl.Key8.toString()).getRequest());
                break;
            case R.id.num9:
                this.mEventBus.post(new ExecuteRemoteControlHandler(MediaRemote.ExecuteRemoteControl.Key9.toString()).getRequest());
                break;

            case R.id.btnClose:
                dismiss();
                break;
        }
    }

}
