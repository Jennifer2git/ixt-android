package com.imax.ipt.controller.settings;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.imax.ipt.R;
import com.imax.ipt.model.LightingEvent;
import com.imax.ipt.model.LightingPreset;
import com.imax.ipt.ui.activity.settings.preferences.LightingPresetSelectionDialogFragment;

public class LightingEventsAdapter extends BaseAdapter implements OnClickListener {
    private final String TAG = "LightingEventsAdapter";

    private Activity activity;
    private static LayoutInflater inflater = null;
    private LightingEvent[] lightingEvents;
    private LightingPreset[] lightingPresets;

    public LightingEventsAdapter(Activity activity, LightingEvent[] lightingEvents, LightingPreset[] lightingPresets) {
        this.activity = activity;
        this.lightingEvents = lightingEvents;
        this.lightingPresets = lightingPresets;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return lightingEvents.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return lightingPresets[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return lightingEvents[position].getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null)
            view = inflater.inflate(R.layout.list_row_lighting_event, null);

        TextView txtEvent = (TextView) view.findViewById(R.id.txtEvent);
        TextView txtPreset = (TextView) view.findViewById(R.id.txtPreset);
        Button btnPreset = (Button) view.findViewById(R.id.btnPreset);

        txtEvent.setText(activity.getString(R.string.default_lighting_preset) + lightingEvents[position].getDisplayName());

        LightingPreset preset = lightingPresets[position];
        if (preset.getId() == 0) {
            txtPreset.setText(activity.getString(R.string.event_lighting_not_set));
        } else {
            txtPreset.setText(preset.getDisplayName());
        }
//      btnPreset.setBackgroundResource(R.drawable.selector_menu_settings_pref_button);

        btnPreset.setTag(position);
        btnPreset.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        int position = Integer.parseInt(v.getTag().toString());

        // open the popup view for preset selection
        android.app.FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        Fragment prev = activity.getFragmentManager().findFragmentByTag("lightingPreset");
        if (prev != null) {
            ft.remove(prev);
        }
        // ft.addToBackStack(null);

        // Create and show the dialog
        DialogFragment newFragment = LightingPresetSelectionDialogFragment.newInstance(lightingEvents[position].getId(), lightingPresets[position].getId());
        newFragment.show(ft, "lightingPreset");
    }

}
