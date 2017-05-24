package com.imax.ipt.controller.settings;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.imax.ipt.R;
import com.imax.ipt.model.LightingPreset;
import com.imax.ipt.ui.activity.settings.preferences.LightingPresetSelectionDialogFragment;

public class LightingPresetsAdapter extends BaseAdapter implements OnClickListener {
    private final String TAG = "LightingPresetsAdapter";

    private Activity activity;
    private LightingPresetSelectionDialogFragment parent;
    private static LayoutInflater inflater = null;
    private LightingPreset[] lightingPresets;

    private int lightingEventId;
    private int lightingPresetId;

    public LightingPresetsAdapter(Activity activity, LightingPresetSelectionDialogFragment parent, LightingPreset[] lightingPresets, int lightingEventId, int lightingPresetId) {
        this.activity = activity;
        this.parent = parent;
        this.lightingPresets = lightingPresets;
        this.lightingEventId = lightingEventId;
        this.lightingPresetId = lightingPresetId;

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return lightingPresets.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return lightingPresets[position].getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null)
            view = inflater.inflate(R.layout.grid_item_lighting_preset, null);

        LightingPreset preset = lightingPresets[position];

        TextView txtPreset = (TextView) view.findViewById(R.id.txtPreset);
//      Button btnPreset = (Button)view.findViewById(R.id.btnPreset);
        ToggleButton tglPreset = (ToggleButton) view.findViewById(R.id.tglPreset);
        tglPreset.setTag(position);
        tglPreset.setOnClickListener(this);

        txtPreset.setText(preset.getDisplayName());
        tglPreset.setChecked(preset.getId() == lightingPresetId);

        return view;
    }

    @Override
    public void onClick(View v) {
        //lightingPresetId = Integer.parseInt(v.getTag().toString());
        int position = Integer.parseInt(v.getTag().toString());
        LightingPreset preset = lightingPresets[position];
        lightingPresetId = preset.getId();

        parent.selectingLightingPreset(preset);
    }
}
