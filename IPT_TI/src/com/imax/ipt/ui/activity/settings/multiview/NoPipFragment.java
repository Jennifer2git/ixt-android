package com.imax.ipt.ui.activity.settings.multiview;

import com.imax.ipt.R;
import com.imax.ipt.R.layout;
import com.imax.ipt.controller.settings.MultiViewController;
import com.imax.ipt.model.DeviceType;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable;
import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable.DeviceKind;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link NoPipFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link NoPipFragment#newInstance} factory method
 * to create an instance of this fragment.
 */
public class NoPipFragment extends Fragment implements OnClickListener {
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

    private ImageButton btnFocusAudio1;
    private ImageButton btnSelectInput1;
    private ImageButton btnFullScreen1;
    private ImageButton btnSource1;
    private IPTTextView txtSource1;

    private MultiViewController controller;

    /**
     * Use this factory method to create a new instance of this fragment using
     * the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NoPipFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoPipFragment newInstance(DeviceKind[] deviceKinds, String[] deviceNames, int audioFocusOutputIndex, int[] inputIds, boolean[] irSupporteds) {
        String[] deviceKindStrings = new String[deviceKinds.length];
        for (int i = 0; i < deviceKinds.length; i++) {
            deviceKindStrings[i] = deviceKinds[0].getDeviceKind();
        }

        NoPipFragment fragment = new NoPipFragment();
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

    public NoPipFragment() {
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
        View view = inflater.inflate(R.layout.fragment_no_pip, container, false);

        btnFocusAudio1 = (ImageButton) view.findViewById(R.id.btnFocusAudio1);
        btnFocusAudio1.setOnClickListener(this);
        btnSelectInput1 = (ImageButton) view.findViewById(R.id.btnSelectInput1);
        btnSelectInput1.setOnClickListener(this);
        btnFullScreen1 = (ImageButton) view.findViewById(R.id.btnFullScreen1);
        btnFullScreen1.setOnClickListener(this);
        btnSource1 = (ImageButton) view.findViewById(R.id.btnSource1);
        btnSource1.setOnClickListener(this);
        txtSource1 = (IPTTextView) view.findViewById(R.id.txtSource1);
        txtSource1.setSelected(true);

        if (deviceKinds.length > 0) {
            btnSource1.setImageDrawable(FactoryDeviceTypeDrawable.createDeviceTypeDrawable(getResources(), deviceKinds[0]));
        }
        if (deviceNames.length > 0) {
            txtSource1.setText(deviceNames[0]);
        }

        switch (audioFocusOutputIndex) {
            case 0:
                btnFocusAudio1.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_audio_focus_btn_active));
                break;
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFocusAudio1:
                if (inputIds.length > 0)
                    controller.setAudioFocus(inputIds[0]);
                break;

            case R.id.btnSelectInput1:
                controller.OpenInputSelectionDialog(0);
                break;

            case R.id.btnSource1:
                if (inputIds.length > 0 && deviceNames.length > 0 && deviceKinds.length > 0)
                    controller.openRemoteControl(inputIds[0], deviceNames[0], deviceKinds[0], irSupporteds[0]);
                break;

            case R.id.btnFullScreen1:
                if (inputIds.length > 0)
                    controller.setFullScreen(inputIds[0]);
                break;
        }
    }

}
