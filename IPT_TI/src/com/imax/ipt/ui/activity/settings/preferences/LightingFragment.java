package com.imax.ipt.ui.activity.settings.preferences;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.rooms.GetLightingPresetsHandler;
import com.imax.ipt.controller.settings.LightingEventsAdapter;
import com.imax.ipt.controller.settings.PreferencesController;
import com.imax.ipt.model.LightingEvent;
import com.imax.ipt.model.LightingPreset;
import com.imax.iptevent.EventBus;


public class LightingFragment extends Fragment {
    private View mLightingLayout;
//   private EventBus mEventBus;

    private LightingEventsAdapter adapter;
    private ListView listViewEvents;

    private PreferencesController preferencesController;

    private LightingEvent[] lightingEvents;
    private LightingPreset[] lightingPresets;

    public void setPreferencesController(PreferencesController preferencesController) {
        this.preferencesController = preferencesController;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLightingLayout = inflater.inflate(R.layout.fragment_pref_lighting, null);
//      this.mEventBus = IPT.getInstance().getEventBus();
//      this.mEventBus.registerSticky(this); //TODO replace for a controller

        listViewEvents = (ListView) mLightingLayout.findViewById(R.id.listViewEvents);

        preferencesController.GetLightingEventsHandler();

        return mLightingLayout;
    }
   
/*   @Override
   public void onDestroy()
   {
      super.onDestroy();
      this.mEventBus.unregister(this);
   }
   
   
   */

    /**
     * @param getLightingEventsHandler
     *//*
   public void onEvent(GetLightingPresetsHandler getLightingEventsHandler) //TODO change this for a controller -- THIS IS BAD 
   {
         
   }
*/
    public void populateList(LightingEvent[] lightingEvents, LightingPreset[] lightingPresets) {
        this.lightingEvents = lightingEvents;
        this.lightingPresets = lightingPresets;
        adapter = new LightingEventsAdapter(getActivity(), lightingEvents, lightingPresets);
        listViewEvents.setAdapter(adapter);
    }

    public void updateList(int eventId, LightingPreset preset) {
        int position = 0;
        for (LightingEvent lightingEvent : lightingEvents) {
            if (lightingEvent.getId() == eventId) {
                lightingPresets[position] = preset;
                listViewEvents.invalidateViews();
                break;
            }
            position++;
        }
    }
}
