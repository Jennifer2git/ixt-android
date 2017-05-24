package com.imax.ipt.ui.activity.settings.preferences;

import java.util.List;

import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.R.layout;
import com.imax.ipt.controller.eventbus.handler.input.GetVideoModeHandler;
import com.imax.ipt.controller.eventbus.handler.push.VideoModeChangedEvent;
import com.imax.ipt.controller.eventbus.handler.rooms.GetLightingPresetsHandler;
import com.imax.ipt.controller.eventbus.handler.settings.preferences.SetLightingPresetForEventHandler;
import com.imax.ipt.controller.settings.LightingPresetsAdapter;
import com.imax.ipt.model.LightingPreset;
import com.imax.iptevent.EventBus;

import android.app.Activity;
import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link LightingPresetSelectionDialogFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link LightingPresetSelectionDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LightingPresetSelectionDialogFragment extends DialogFragment {
    public static final String TAG = "LightingPresetSelectionDialogFragment";

    private EventBus mEventBus;
    private GridView gridLightingPresets;
    private LightingPresetsAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_LIGHTING_EVENT_ID = "lightingEventId";
    private static final String ARG_LIGHTING_PRESET_ID = "lightingPresetId";

    private int lightingEventId;
    private int lightingPresetId;

    private OnLightingPresetSelectedListener mListener;

    /**
     * Use this factory method to create a new instance of this fragment using
     * the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LightingPresetSelectionDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LightingPresetSelectionDialogFragment newInstance(int lightingEventId, int lightingPresetId) {
        LightingPresetSelectionDialogFragment fragment = new LightingPresetSelectionDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LIGHTING_EVENT_ID, lightingEventId);
        args.putInt(ARG_LIGHTING_PRESET_ID, lightingPresetId);
        fragment.setArguments(args);
        return fragment;
    }

    public LightingPresetSelectionDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mEventBus = IPT.getInstance().getEventBus();
        this.mEventBus.register(this);

        if (getArguments() != null) {
/*         mParam1 = getArguments().getString(ARG_PARAM1);
         mParam2 = getArguments().getString(ARG_PARAM2);*/
            lightingEventId = getArguments().getInt(ARG_LIGHTING_EVENT_ID);
            lightingPresetId = getArguments().getInt(ARG_LIGHTING_PRESET_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lighting_preset_selection_dialog, container, false);

        gridLightingPresets = (GridView) view.findViewById(R.id.gridLightingPresets);

        mEventBus.post(new GetLightingPresetsHandler().getRequest());

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

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

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        this.mEventBus.unregister(this);
    }

    public void onEvent(final GetLightingPresetsHandler getLightingPresetsHandler) {
        List<LightingPreset> list = getLightingPresetsHandler.getLightingPresets();
        list.add(new LightingPreset(0, getActivity().getString(R.string.event_lighting_not_set)));

        final LightingPreset[] lightingPresets = new LightingPreset[list.size()];
        list.toArray(lightingPresets);

        final LightingPresetSelectionDialogFragment fragment = this;

        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                adapter = new LightingPresetsAdapter(getActivity(), fragment, lightingPresets, lightingEventId, lightingPresetId);
                gridLightingPresets.setAdapter(adapter);
            }
        });
    }

    public void selectingLightingPreset(LightingPreset preset) {
        mEventBus.post(new SetLightingPresetForEventHandler(lightingEventId, preset.getId()).getRequest());
        gridLightingPresets.invalidateViews();

        mListener.onLightPresetSelected(lightingEventId, preset);

        getDialog().dismiss();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnLightingPresetSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnLightingPresetSelectedListener {
        // TODO: Update argument type and name
        public void onLightPresetSelected(int eventId, LightingPreset preset);
    }

}
