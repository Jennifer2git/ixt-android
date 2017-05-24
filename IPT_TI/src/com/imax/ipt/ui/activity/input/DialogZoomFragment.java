package com.imax.ipt.ui.activity.input;

import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.R.layout;
import com.imax.ipt.controller.eventbus.handler.input.GetScreenAspectRatioHandler;
import com.imax.ipt.controller.eventbus.handler.input.GetVideoModeHandler;
import com.imax.ipt.controller.eventbus.handler.input.GetZoomModeHandler;
import com.imax.ipt.controller.eventbus.handler.input.SetScreenAspectRatioHandler;
import com.imax.ipt.controller.eventbus.handler.input.SetScreenAspectRatioHandler.ScreenAspectRatio;
import com.imax.ipt.controller.eventbus.handler.input.SetVideoModeHandler;
import com.imax.ipt.controller.eventbus.handler.input.SetVideoModeHandler.VideoMode;
import com.imax.ipt.controller.eventbus.handler.input.SetZoomModeHandler;
import com.imax.ipt.controller.eventbus.handler.input.SetZoomModeHandler.ZoomMode;
import com.imax.ipt.controller.eventbus.handler.push.ScreenAspectRatioChangedEvent;
import com.imax.ipt.controller.eventbus.handler.push.VideoModeChangedEvent;
import com.imax.ipt.controller.eventbus.handler.settings.multiview.GetPipModeHandler;
import com.imax.ipt.controller.eventbus.handler.settings.multiview.SetPipModeHandler;
import com.imax.ipt.controller.eventbus.handler.ui.SetupMultiviewEvent.MultiView;
import com.imax.iptevent.EventBus;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link DialogZoomFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the
 * {@link DialogZoomFragment#newInstance} factory method to create an
 * instance of this fragment.
 */
public class DialogZoomFragment extends DialogFragment implements OnClickListener {
    public static final String TAG = "DialogZoomFragment";

    private EventBus mEventBus;
    private Button mBtnZoomNormal;
    private Button mBtnZoomWide;
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

    /**
     * Use this factory method to create a new instance of this fragment using
     * the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DialogVideoViewFragment.
     */
//	// TODO: Rename and change types and number of parameters
//	public static DialogVideoViewFragment newInstance(String param1,
//			String param2) {
//		DialogVideoViewFragment fragment = new DialogVideoViewFragment();
//		Bundle args = new Bundle();
//		args.putString(ARG_PARAM1, param1);
//		args.putString(ARG_PARAM2, param2);
//		fragment.setArguments(args);
//		return fragment;
//	}
    public static DialogZoomFragment newInstance(int inputId) {
        DialogZoomFragment fragment = new DialogZoomFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, inputId);
        fragment.setArguments(args);
        return fragment;
    }

    public DialogZoomFragment() {

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
        // Inflate the layout for this fragment
        View mViewDialogFragment = inflater.inflate(R.layout.fragment_dialog_zoom, container, false);

        Dialog d = getDialog();
//		  WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//		  lp.copyFrom(d.getWindow().getAttributes());
//		  lp.width = 1300;
//		  lp.height = 300;
//		  lp.x = 10;
//		  lp.y = 325;
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
////		  d.getWindow().setBackgroundDrawable(new ColorDrawable(0));
////		
//		  d.show();
//		  d.getWindow().setAttributes(lp);

        this.init(mViewDialogFragment);

        return mViewDialogFragment;
    }

    private void init(View view) {
        this.mEventBus = IPT.getInstance().getEventBus();
        this.mEventBus.register(this);

        mBtnZoomNormal = (Button) view.findViewById(R.id.btnZoomNormal);
        mBtnZoomNormal.setOnClickListener(this);

        mBtnZoomWide = (Button) view.findViewById(R.id.btnZoomWide);
        mBtnZoomWide.setOnClickListener(this);

        mBtnAspectRatioScope = (Button) view.findViewById(R.id.btnAspectRatioScope);
        mBtnAspectRatioScope.setOnClickListener(this);

//	      mBtnMultiviewFull = (ImageButton) view.findViewById(R.id.btnMultiViewFull);
//	      mBtnMultiviewFull.setOnClickListener(this);
//
//	      mBtnMultiview4up = (ImageButton) view.findViewById(R.id.btnMultiView4Up);
//	      mBtnMultiview4up.setOnClickListener(this);
//
//	      mBtnMultiview3Top = (ImageButton) view.findViewById(R.id.btnMultiView3top);
//	      mBtnMultiview3Top.setOnClickListener(this);
//
//	      mBtnMultiview3Rigth = (ImageButton) view.findViewById(R.id.btnMultiView3right);
//	      mBtnMultiview3Rigth.setOnClickListener(this);
//
//	      mBtnMultiview3Bottom = (ImageButton) view.findViewById(R.id.btnMultiView3bottom);
//	      mBtnMultiview3Bottom.setOnClickListener(this);
//
        mBtnClose = (ImageButton) view.findViewById(R.id.btnClose);
        mBtnClose.setOnClickListener(this);
//
//	      this.setupLayout();

        sendGetZoomMode();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mEventBus.unregister(this);
    }

    ;

    private void sendGetZoomMode() {
        mEventBus.post(new GetZoomModeHandler().getRequest());
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

/*	   public void onEvent(final VideoModeChangedEvent videoModeChangedEvent)
	   {
		   getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Log.d(TAG, "onEvent:GetVideoModeHandler");
					VideoMode videoMode = videoModeChangedEvent.getVideoMode();
					switch (videoMode)
					{
					case TwoD:
						mBtnZoomNormal.setBackground(getResources().getDrawable(R.drawable.ipt_gui_asset_video_mode_2d_active));
						mBtnZoomWide.setBackground(getResources().getDrawable(R.drawable.ipt_gui_asset_video_mode_2d_inactive));
						break;
						
					case ThreeDFp:
						mBtnZoomNormal.setBackground(getResources().getDrawable(R.drawable.ipt_gui_asset_video_mode_2d_inactive));
						mBtnZoomWide.setBackground(getResources().getDrawable(R.drawable.ipt_gui_asset_video_mode_2d_active));
						break;
						
					}
				}
				   
			   });
	   }*/

    public void onEvent(final GetZoomModeHandler getZoomModeResponseEvent) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Log.d(TAG, "onEvent:GetVideoModeHandler");
                ZoomMode zoomMode = getZoomModeResponseEvent.getZoomMode();
                switch (zoomMode) {
                    case Normal:
                        mBtnZoomNormal.setBackground(getResources().getDrawable(R.drawable.ipt_gui_asset_video_mode_2d_active));
                        mBtnZoomWide.setBackground(getResources().getDrawable(R.drawable.ipt_gui_asset_video_mode_2d_inactive));
                        break;

                    case Wide:
                        mBtnZoomNormal.setBackground(getResources().getDrawable(R.drawable.ipt_gui_asset_video_mode_2d_inactive));
                        mBtnZoomWide.setBackground(getResources().getDrawable(R.drawable.ipt_gui_asset_video_mode_2d_active));
                        break;

                }
            }

        });
    }


//	@Override
//	public void onAttach(Activity activity) {
//		super.onAttach(activity);
//		try {
//			mListener = (OnFragmentInteractionListener) activity;
//		} catch (ClassCastException e) {
//			throw new ClassCastException(activity.toString()
//					+ " must implement OnFragmentInteractionListener");
//		}
//	}

//	@Override
//	public void onDetach() {
//		super.onDetach();
//		mListener = null;
//	}

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnZoomNormal:
                this.mEventBus.post(new SetZoomModeHandler(ZoomMode.Normal, inputId).getRequest());
                dismiss();
                break;

            case R.id.btnZoomWide:
                this.mEventBus.post(new SetZoomModeHandler(ZoomMode.Wide, inputId).getRequest());
                dismiss();
                break;

            case R.id.btnClose:
                dismiss();
                break;
        }
    }

}
