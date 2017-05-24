package com.imax.ipt.ui.activity.settings.multiview;

import com.imax.ipt.R;
import com.imax.ipt.R.layout;
import com.imax.ipt.controller.settings.MultiViewController;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable;
import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable.DeviceKind;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link PipThreeTop.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link PipThreeTop#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PipThreeTop extends Fragment implements OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DEVICE_KIND_STRINGS = "deviceKindStrings";
    private static final String ARG_DEVICE_NAMES = "deviceNames";
    private static final String ARG_AUDIO_FOCUS_OUTPUT_INDEX = "audioFocusOutputIndex";
    private static final String ARG_INPUT_IDS = "inputIds";
    private static final String ARG_IR_SUPPORTEDS = "irSupporteds";

    private DeviceKind[] deviceKinds;
    private String[] deviceNames;
    private int audioFocusOutputIndex;
    private int[] inputIds;
    private boolean[] irSupporteds;

    private ImageButton mBtnSourceOne;
    private IPTTextView mTxtSourceOne;
    private ImageButton mBtnSourceTwo;
    private IPTTextView mTxtSourceTwo;
    private ImageButton mBtnSourceThree;
    private IPTTextView mTxtSourceThree;
    private ImageButton mBtnSourceFour;
    private IPTTextView mTxtSourceFour;

    private ImageButton mBtnSelectInput1;
    private ImageButton mBtnSelectInput2;
    private ImageButton mBtnSelectInput3;
    private ImageButton mBtnSelectInput4;

    private ImageButton mBtnFocusAudio1;
    private ImageButton mBtnFocusAudio2;
    private ImageButton mBtnFocusAudio3;
    private ImageButton mBtnFocusAudio4;

    private ImageButton mBtnFullScreen1;
    private ImageButton mBtnFullScreen2;
    private ImageButton mBtnFullScreen3;
    private ImageButton mBtnFullScreen4;

    private MultiViewController controller;

//   private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of this fragment using
     * the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PipThreeUp.
     */
    // TODO: Rename and change types and number of parameters
    public static PipThreeTop newInstance(DeviceKind[] deviceKinds, String[] deviceNames, int audioFocusOutputIndex, int[] inputIds, boolean[] irSupporteds) {
        String[] deviceKindStrings = new String[deviceKinds.length];
        for (int i = 0; i < deviceKinds.length; i++) {
            deviceKindStrings[i] = deviceKinds[i].getDeviceKind();
        }

        PipThreeTop fragment = new PipThreeTop();
        Bundle args = new Bundle();
        args.putStringArray(ARG_DEVICE_KIND_STRINGS, deviceKindStrings);
        args.putStringArray(ARG_DEVICE_NAMES, deviceNames);
        args.putInt(ARG_AUDIO_FOCUS_OUTPUT_INDEX, audioFocusOutputIndex);
        args.putIntArray(ARG_INPUT_IDS, inputIds);
        args.putBooleanArray(ARG_IR_SUPPORTEDS, irSupporteds);
        fragment.setArguments(args);
        return fragment;
    }

    public void setController(MultiViewController controller) {
        this.controller = controller;
    }

    public PipThreeTop() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String[] deviceKindStrings = getArguments().getStringArray(ARG_DEVICE_KIND_STRINGS);
            deviceKinds = new DeviceKind[deviceKindStrings.length];
            for (int i = 0; i < deviceKindStrings.length; i++) {
                deviceKinds[i] = DeviceKind.getDeviceKindEnum(deviceKindStrings[i]);
            }

            deviceNames = getArguments().getStringArray(ARG_DEVICE_NAMES);
            audioFocusOutputIndex = getArguments().getInt(ARG_AUDIO_FOCUS_OUTPUT_INDEX);
            inputIds = getArguments().getIntArray(ARG_INPUT_IDS);
            irSupporteds = getArguments().getBooleanArray(ARG_IR_SUPPORTEDS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pip_three_top, container, false);

        mBtnSourceOne = (ImageButton) view.findViewById(R.id.btnSource1);
        mTxtSourceOne = (IPTTextView) view.findViewById(R.id.txtSource1);
        mBtnSourceOne.setOnClickListener(this);

        mBtnSelectInput1 = (ImageButton) view.findViewById(R.id.btnSelectInput1);
        mBtnSelectInput1.setTag(0);
        mBtnSelectInput1.setOnClickListener(this);
        mBtnFocusAudio1 = (ImageButton) view.findViewById(R.id.btnFocusAudio1);
        mBtnFocusAudio1.setOnClickListener(this);

        mBtnFullScreen1 = (ImageButton) view.findViewById(R.id.mBtnFullScreen1);
        mBtnFullScreen1.setOnClickListener(this);

        mBtnSourceTwo = (ImageButton) view.findViewById(R.id.btnSource2);
        mTxtSourceTwo = (IPTTextView) view.findViewById(R.id.txtSource2);
        mBtnSourceTwo.setOnClickListener(this);

        mBtnSelectInput2 = (ImageButton) view.findViewById(R.id.btnSelectInput2);
        mBtnSelectInput2.setOnClickListener(this);
        mBtnSelectInput2.setTag(1);
        mBtnFocusAudio2 = (ImageButton) view.findViewById(R.id.btnFocusAudio2);
        mBtnFocusAudio2.setOnClickListener(this);

        mBtnFullScreen2 = (ImageButton) view.findViewById(R.id.mBtnFullScreen2);
        mBtnFullScreen2.setOnClickListener(this);

        mBtnSourceThree = (ImageButton) view.findViewById(R.id.btnSource3);
        mTxtSourceThree = (IPTTextView) view.findViewById(R.id.txtSource3);
        mBtnSourceThree.setOnClickListener(this);

        mBtnSelectInput3 = (ImageButton) view.findViewById(R.id.btnSelectInput3);
        mBtnSelectInput3.setOnClickListener(this);
        mBtnSelectInput3.setTag(2);
        mBtnFocusAudio3 = (ImageButton) view.findViewById(R.id.btnFocusAudio3);
        mBtnFocusAudio3.setOnClickListener(this);

        mBtnFullScreen3 = (ImageButton) view.findViewById(R.id.mBtnFullScreen3);
        mBtnFullScreen3.setOnClickListener(this);

        mBtnSourceFour = (ImageButton) view.findViewById(R.id.btnSource4);
        mTxtSourceFour = (IPTTextView) view.findViewById(R.id.txtSource4);
        mBtnSourceFour.setOnClickListener(this);

        mBtnSelectInput4 = (ImageButton) view.findViewById(R.id.btnSelectInput4);
        mBtnSelectInput4.setOnClickListener(this);
        mBtnSelectInput4.setTag(3);
        mBtnFocusAudio4 = (ImageButton) view.findViewById(R.id.btnFocusAudio4);
        mBtnFocusAudio4.setOnClickListener(this);

        mBtnFullScreen4 = (ImageButton) view.findViewById(R.id.mBtnFullScreen4);
        mBtnFullScreen4.setOnClickListener(this);

        mTxtSourceOne.setSelected(true);
        mTxtSourceTwo.setSelected(true);
        mTxtSourceThree.setSelected(true);
        mTxtSourceFour.setSelected(true);

        if (deviceKinds.length >= 4) {
            mBtnSourceOne.setImageDrawable(FactoryDeviceTypeDrawable.createDeviceTypeDrawable(getResources(), deviceKinds[0]));
            mBtnSourceTwo.setImageDrawable(FactoryDeviceTypeDrawable.createDeviceTypeDrawable(getResources(), deviceKinds[1]));
            mBtnSourceThree.setImageDrawable(FactoryDeviceTypeDrawable.createDeviceTypeDrawable(getResources(), deviceKinds[2]));
            mBtnSourceFour.setImageDrawable(FactoryDeviceTypeDrawable.createDeviceTypeDrawable(getResources(), deviceKinds[3]));
        }
        if (deviceNames.length >= 4) {
            mTxtSourceOne.setText(deviceNames[0]);
            mTxtSourceTwo.setText(deviceNames[1]);
            mTxtSourceThree.setText(deviceNames[2]);
            mTxtSourceFour.setText(deviceNames[3]);
        }
        if (inputIds.length >= 4) {
            mBtnFocusAudio1.setTag(inputIds[0]);
            mBtnFocusAudio2.setTag(inputIds[1]);
            mBtnFocusAudio3.setTag(inputIds[2]);
            mBtnFocusAudio4.setTag(inputIds[3]);

            mBtnFullScreen1.setTag(inputIds[0]);
            mBtnFullScreen2.setTag(inputIds[1]);
            mBtnFullScreen3.setTag(inputIds[2]);
            mBtnFullScreen4.setTag(inputIds[3]);
        }

        mBtnSourceOne.setTag(0);
        mBtnSourceTwo.setTag(1);
        mBtnSourceThree.setTag(2);
        mBtnSourceFour.setTag(3);

        mBtnSelectInput1.setTag(0);
        mBtnSelectInput2.setTag(1);
        mBtnSelectInput3.setTag(2);
        mBtnSelectInput4.setTag(3);

        switch (audioFocusOutputIndex) {
            case 0:
                mBtnFocusAudio1.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_audio_focus_btn_active));
                break;

            case 1:
                mBtnFocusAudio2.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_audio_focus_btn_active));
                break;

            case 2:
                mBtnFocusAudio3.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_audio_focus_btn_active));
                break;

            case 3:
                mBtnFocusAudio4.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_audio_focus_btn_active));
                break;
        }

        return view;
    }

//   // TODO: Rename method, update argument and hook method into UI event
//   public void onButtonPressed(Uri uri)
//   {
//      if (mListener != null)
//      {
//         mListener.onFragmentInteraction(uri);
//      }
//   }

//   @Override
//   public void onAttach(Activity activity)
//   {
//      super.onAttach(activity);
//      try
//      {
//         mListener = (OnFragmentInteractionListener) activity;
//      } catch (ClassCastException e)
//      {
//         throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
//      }
//   }

//   @Override
//   public void onDetach()
//   {
//      super.onDetach();
//      mListener = null;
//   }
//
//   /**
//    * This interface must be implemented by activities that contain this
//    * fragment to allow an interaction in this fragment to be communicated to
//    * the activity and potentially other fragments contained in that activity.
//    * <p>
//    * See the Android Training lesson <a href=
//    * "http://developer.android.com/training/basics/fragments/communicating.html"
//    * >Communicating with Other Fragments</a> for more information.
//    */
//   public interface OnFragmentInteractionListener
//   {
//      // TODO: Update argument type and name
//      public void onFragmentInteraction(Uri uri);
//   }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFocusAudio1:
            case R.id.btnFocusAudio2:
            case R.id.btnFocusAudio3:
            case R.id.btnFocusAudio4: {
                int inputId = Integer.parseInt(v.getTag().toString());
                controller.setAudioFocus(inputId);
                break;
            }

            case R.id.btnSelectInput1:
            case R.id.btnSelectInput2:
            case R.id.btnSelectInput3:
            case R.id.btnSelectInput4: {
                int index = Integer.parseInt(v.getTag().toString());
                controller.OpenInputSelectionDialog(index);
                break;
            }

            case R.id.btnSource1:
            case R.id.btnSource2:
            case R.id.btnSource3:
            case R.id.btnSource4: {
                int index = Integer.parseInt(v.getTag().toString());
                controller.openRemoteControl(inputIds[index], deviceNames[index], deviceKinds[index], irSupporteds[index]);
                break;
            }

            case R.id.mBtnFullScreen1:
            case R.id.mBtnFullScreen2:
            case R.id.mBtnFullScreen3:
            case R.id.mBtnFullScreen4: {
                int inputId = Integer.parseInt(v.getTag().toString());
                controller.setFullScreen(inputId);
                break;
            }
        }
    }
}
