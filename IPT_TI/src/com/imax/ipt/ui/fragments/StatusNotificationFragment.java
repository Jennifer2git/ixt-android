package com.imax.ipt.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.imax.ipt.R;
import com.imax.ipt.model.SystemStatus;
import com.imax.ipt.model.TabletStatus;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link StatusNotificationFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the
 * {@link StatusNotificationFragment#newInstance} factory method to create an
 * instance of this fragment.
 */
public class StatusNotificationFragment extends Fragment {
    private static final String ARG_NOTIFICATION_SYSTEM_STATUS = "systemStatus";
    private static final String ARG_NOTIFICATION_TABLET_STATUS = "tabletStatus";

    private SystemStatus systemStatus;
    private TabletStatus tabletStatus;
//   private boolean fireAlarm;

    private Button btnFireAlarm;
    private Button btnConnectionIpt;
    private Button btnConnectionWifi;
    private Button btnPowerOutage;
    private Button btnBatteryLow;

    /**
     * Use this factory method to create a new instance of this fragment using
     * the provided parameters.
     *
     * @param systemStatusValue .
     * @param tabletStatus .
     * @return A new instance of fragment StatusNotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatusNotificationFragment newInstance(int systemStatusValue, byte tabletStatus) {
        StatusNotificationFragment fragment = new StatusNotificationFragment();
        Bundle args = new Bundle();

        args.putInt(ARG_NOTIFICATION_SYSTEM_STATUS, systemStatusValue);
        args.putByte(ARG_NOTIFICATION_TABLET_STATUS, tabletStatus);

        fragment.setArguments(args);
        return fragment;
    }

    public StatusNotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            systemStatus = new SystemStatus(getArguments().getInt(ARG_NOTIFICATION_SYSTEM_STATUS));
            tabletStatus = TabletStatus.newInstance(getArguments().getByte(ARG_NOTIFICATION_TABLET_STATUS));

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewContainer = inflater.inflate(R.layout.fragment_status_notification, container, false);
        btnFireAlarm = (Button) viewContainer.findViewById(R.id.btnFireAlarm);
        btnConnectionIpt = (Button) viewContainer.findViewById(R.id.btnConnectionIpt);
        btnConnectionWifi = (Button) viewContainer.findViewById(R.id.btnConnectionWifi);
        btnPowerOutage = (Button) viewContainer.findViewById(R.id.btnPowerOutage);
        btnBatteryLow = (Button) viewContainer.findViewById(R.id.btnBatteryLow);

        if (systemStatus.isFireAlarmSignaled()) {
            btnFireAlarm.setVisibility(View.VISIBLE);
        } else {
            btnFireAlarm.setVisibility(View.GONE);
        }

        if (systemStatus.isUpsOnBatterySignaled()) {
            btnPowerOutage.setVisibility(View.VISIBLE);
        } else {
            btnPowerOutage.setVisibility(View.GONE);
        }

        if (tabletStatus.isBatteryLow()) {
            btnBatteryLow.setVisibility(View.VISIBLE);
        } else {
            btnBatteryLow.setVisibility(View.GONE);
        }

        if (tabletStatus.isIptWifiNotConnected()) {
            btnConnectionWifi.setVisibility(View.VISIBLE);
        } else if (tabletStatus.isIptServerNotConnected()) {
            btnConnectionIpt.setVisibility(View.VISIBLE);
        }

        return viewContainer;
    }

//   // TODO: Rename method, update argument and hook method into UI event
//   public void onButtonPressed(Uri uri)
//   {
//      if (mListener != null)
//      {
//         mListener.onFragmentInteraction(uri);
//      }
//   }
//
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
//
//   @Override
//   public void onDetach()
//   {
//      super.onDetach();
//      mListener = null;
//   }

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

}
