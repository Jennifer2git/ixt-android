package com.imax.ipt.ui.activity.input;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import com.imax.ipt.R;
import com.imax.ipt.controller.GlobalController;
import com.imax.iptevent.EventBus;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link Dialog3DFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the
 * {@link Dialog3DFragment#newInstance} factory method to create an
 * instance of this fragment.
 */
public class Dialog3DFragment extends DialogFragment implements OnClickListener {
    public static final String TAG = "Dialog3DFragment";

    private EventBus mEventBus;
    private Button mBtn2D;
    private Button mBtn3D;
    private Button mBtnAspectRatioScope;
    private ImageButton mBtnClose;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "inputId";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int inputId;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static Dialog3DFragment newInstance(int inputId) {
        Dialog3DFragment fragment = new Dialog3DFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, inputId);
        fragment.setArguments(args);
        return fragment;
    }

    public Dialog3DFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            inputId = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mViewDialogFragment = inflater.inflate(R.layout.fragment_dialog_3d, container, false);
        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.init(mViewDialogFragment);

        return mViewDialogFragment;
    }

    private void init(View view) {

        mBtn2D = (Button) view.findViewById(R.id.btnZoomNormal);
        mBtn2D.setOnClickListener(this);

        mBtn3D = (Button) view.findViewById(R.id.btnZoomWide);
        mBtn3D.setOnClickListener(this);

        mBtnAspectRatioScope = (Button) view.findViewById(R.id.btnAspectRatioScope);
        mBtnAspectRatioScope.setOnClickListener(this);

        mBtnClose = (ImageButton) view.findViewById(R.id.btnClose);
        mBtnClose.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated to
     * the activity and potentially other fragments contained in that activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    /**
     * type 2DΪ101/3DΪ102
     */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnZoomNormal:
                GlobalController.getInstance().set2DMode();
                break;

            case R.id.btnZoomWide:
                GlobalController.getInstance().set3DMode();
                break;

            case R.id.btnClose:
                dismiss();
                break;
        }
    }

}
