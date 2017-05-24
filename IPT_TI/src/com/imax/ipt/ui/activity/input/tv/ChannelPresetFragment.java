package com.imax.ipt.ui.activity.input.tv;

import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.ir.IRChannelEvent;
import com.imax.ipt.controller.eventbus.handler.remote.AddInputChannelPresetHandler;
import com.imax.ipt.controller.eventbus.handler.remote.DeleteInputChannelPresetHandler;
import com.imax.ipt.controller.eventbus.handler.remote.EditInputChannelPresetHandler;
import com.imax.ipt.controller.eventbus.handler.remote.GetInputChannelPresetsForInputHandler;
import com.imax.ipt.controller.eventbus.handler.remote.InputChannelPresetsAdapter;
import com.imax.ipt.model.InputChannelPreset;
import com.imax.iptevent.EventBus;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ToggleButton;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link ChannelPresetFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the {@link ChannelPresetFragment#newInstance}
 * factory method to create an instance of this fragment.
 */
public class ChannelPresetFragment extends DialogFragment
        implements OnClickListener, OnCheckedChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_INPUT_ID = "inputId";

    // TODO: Rename and change types of parameters
    private int inputId;
    private EventBus mEventBus;
    private boolean isEditMode = false;

    private InputChannelPresetsAdapter adapter;

    private ImageButton btnClose;
    private ImageButton btnAddNew;
    private ImageButton btnDelete;
    private ToggleButton tglEditMode;
    private GridView gridInputChannelPresets;
    private InputChannelPreset[] channelPresets;
//   private ArrayList<InputChannelPreset> channelPresets;

    private int editModeSelectedPresetId = -1;

    /**
     * Use this factory method to create a new instance of this fragment using
     * the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChannelPresetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChannelPresetFragment newInstance(int inputId) {
        ChannelPresetFragment fragment = new ChannelPresetFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INPUT_ID, inputId);
        fragment.setArguments(args);
        return fragment;
    }

    public ChannelPresetFragment() {
        // Required empty public constructor
    }

//   public void setEventBus(EventBus eventBus)
//   {
//      mEventBus = eventBus;
//   }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            inputId = getArguments().getInt(ARG_INPUT_ID);
        }

        mEventBus = IPT.getInstance().getEventBus();
        mEventBus.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_channel_preset, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        gridInputChannelPresets = (GridView) view.findViewById(R.id.gridInputChannelPresets);
        btnClose = (ImageButton) view.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(this);
        btnAddNew = (ImageButton) view.findViewById(R.id.btnAddNew);
        btnAddNew.setOnClickListener(this);
        btnDelete = (ImageButton) view.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);
        tglEditMode = (ToggleButton) view.findViewById(R.id.tglEditMode);
        tglEditMode.setOnCheckedChangeListener(this);

        // retrieve the channel presets for this input
        mEventBus.post(new GetInputChannelPresetsForInputHandler(inputId).getRequest());

        return view;
    }

    public void onEvent(GetInputChannelPresetsForInputHandler getInputChannelPresetsForInputHandler) {
        if (adapter == null) {
//         channelPresets = new ArrayList<InputChannelPreset>(Arrays.asList(getInputChannelPresetsForInputHandler.getInputChannelPresets()));

            channelPresets = getInputChannelPresetsForInputHandler.getInputChannelPresets();

            adapter = new InputChannelPresetsAdapter(getActivity(), this, channelPresets);
            adapter.setEditMode(isEditMode);
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    gridInputChannelPresets.setAdapter(adapter);
                }
            });

//         gridInputChannelPresets.setOnItemClickListener(this);
        }
    }

//   @Override
//   public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//   {
//      
//      // khh: Grid cannot contains click-able items in order to receive onItemClick
//      if (isEditMode)
//      {
//         int selectedItemPresetId = channelPresets.get(position).getPresetId();
//         
//         for (InputChannelPreset channelPreset : channelPresets) {
//            channelPreset.setSelected(channelPreset.getPresetId() == selectedItemPresetId);            
//         }
//         adapter.notifyDataSetChanged();
//      }
//      else
//      {
//         // change the channel
//         mEventBus.post(new IRChannelEvent(inputId, channelPresets.get(position).getChannel()).getRequest());   
//      }           
//   }

    public void switchChannelPreset(int position) {
//      mEventBus.post(new IRChannelEvent(inputId, channelPresets.get(position).getChannel()).getRequest());
        mEventBus.post(new IRChannelEvent(inputId, channelPresets[position].getChannel()).getRequest());
    }

    public void makeSelectionInEditMode(int position) {
        if (editModeSelectedPresetId == position || !isEditMode)
            return;

//      int selectedItemPresetId = channelPresets.get(position).getPresetId();
        int selectedItemPresetId = channelPresets[position].getPresetId();

        for (InputChannelPreset channelPreset : channelPresets) {
            channelPreset.setSelected(channelPreset.getPresetId() == selectedItemPresetId);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mEventBus.unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClose:
                dismiss();
                break;

            case R.id.btnAddNew:
                mEventBus.post(new AddInputChannelPresetHandler(inputId, 0, "").getRequest());
                break;

            case R.id.btnDelete:
                mEventBus.post(new DeleteInputChannelPresetHandler(editModeSelectedPresetId).getRequest());
                break;
        }
    }

    public void onEvent(DeleteInputChannelPresetHandler deleteInputChannelPresetHandler) {
//      adapter.notifyDataSetInvalidated();
        adapter = null;
        mEventBus.post(new GetInputChannelPresetsForInputHandler(inputId).getRequest());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        setEditMode(isChecked);
    }

    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
        if (isEditMode) {
            btnDelete.setVisibility(View.VISIBLE);
            btnAddNew.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.INVISIBLE);
            btnAddNew.setVisibility(View.INVISIBLE);
        }
        if (adapter != null) {
            adapter.setEditMode(isEditMode);
            adapter.notifyDataSetChanged();
        }
    }

    //   private int channelPresetAddedIndex;
    public void AddChannelPreset(int channel, String displayName) {
//      this.channelPresetAddedIndex = channelPresetAddedIndex;
        mEventBus.post(new AddInputChannelPresetHandler(inputId, channel, displayName).getRequest());
    }

    public void onEvent(AddInputChannelPresetHandler addInputChannelPresetHandler) {
        //channelPresets.get(channelPresetAddedIndex).setPresetId(addInputChannelPresetHandler.getPresetId());

//      adapter.notifyDataSetInvalidated();
        adapter = null;
        mEventBus.post(new GetInputChannelPresetsForInputHandler(inputId).getRequest());
    }

    public void EditChannelPreset(InputChannelPreset modifiedChannelPreset) {
        mEventBus.post(new EditInputChannelPresetHandler(modifiedChannelPreset).getRequest());
    }

    public void setEditModeSelectedPresetId(int editModeSelectedPresetId) {
        this.editModeSelectedPresetId = editModeSelectedPresetId;
    }

//   // TODO: Rename method, update argument and hook method into UI event
//   public void onButtonPressed(Uri uri)
//   {
///*      if (mListener != null)
//      {
//         mListener.onFragmentInteraction(uri);
//      }*/
//   }
//
//   @Override
//   public void onAttach(Activity activity)
//   {
//      super.onAttach(activity);
//      try
//      {
////         mListener = (OnFragmentInteractionListener) activity;
//      } catch (ClassCastException e)
//      {
//         throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
//      }
//   }
//
//   @Override
//   public void onDetach()
//   {
//      super.onDetach();
////      mListener = null;
//   }
//
///*   *//**
//    * This interface must be implemented by activities that contain this
//    * fragment to allow an interaction in this fragment to be communicated to
//    * the activity and potentially other fragments contained in that activity.
//    * <p>
//    * See the Android Training lesson <a href=
//    * "http://developer.android.com/training/basics/fragments/communicating.html"
//    * >Communicating with Other Fragments</a> for more information.
//    *//*
//   public interface OnFragmentInteractionListener
//   {
//      // TODO: Update argument type and name
//      public void onFragmentInteraction(Uri uri);
//   }*/

}
