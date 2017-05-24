package com.imax.ipt.controller.eventbus.handler.remote;

import com.imax.ipt.R;
import com.imax.ipt.model.InputChannelPreset;
import com.imax.ipt.ui.activity.input.tv.ChannelPresetFragment;
import com.imax.ipt.ui.layout.IPTTextView;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;

public class InputChannelPresetsAdapter extends BaseAdapter
        implements OnFocusChangeListener, OnClickListener {
    private final String TAG = "FavoriteChannelsAdapter";

    private Activity activity;
    private static LayoutInflater inflater = null;
    private ChannelPresetFragment parentFragment;

    private InputChannelPreset[] channelPresets;
//   private ArrayList<InputChannelPreset> channelPresets;

    private int selectedIndex;

    private boolean isEditMode = false;

    public InputChannelPresetsAdapter(Activity activity, ChannelPresetFragment parentFragment, InputChannelPreset[] channelPresets) {
        this.activity = activity;
        this.parentFragment = parentFragment;
        this.channelPresets = channelPresets;

//      // create a copy of the ChannelPresets array, for detecting changes
//      this.channelPresets = new InputChannelPreset[channelPresets.length];
//      for (int i = 0; i < channelPresets.length; i++)
//      {
//         this.channelPresets[i] = new InputChannelPreset(channelPresets[i]);
//      }

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
//      return channelPresets.size();
        return channelPresets.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
//      return channelPresets.get(position);
        return channelPresets[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
//      return channelPresets.get(position).getPresetId();
        return channelPresets[position].getPresetId();
    }

    static class ViewHolder {
        // selection mode
        IPTTextView lblChannelDisplayName;
        IPTTextView lblChannelNumber;
        ImageButton imbPreset;

        // edit mode
        EditText txtChannelDisplayName;
        EditText txtChannelNumber;
        //      ToggleButton tglPreset;
        boolean isEditMode;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        boolean createNewViewHolder = false;
        if (convertView == null) {
            createNewViewHolder = true;

        } else {
            holder = (ViewHolder) convertView.getTag();
            if (holder.isEditMode != isEditMode) {
                createNewViewHolder = true;
            }
        }

        if (createNewViewHolder) {
            int resource;
            if (isEditMode)
                resource = R.layout.grid_item_edit_channel_preset;
            else
                resource = R.layout.grid_item_channel_preset;

            convertView = inflater.inflate(resource, null);
            holder = new ViewHolder();
            holder.isEditMode = isEditMode;
            if (isEditMode) {
                holder.txtChannelDisplayName = (EditText) convertView.findViewById(R.id.txtChannelDisplayName);
                holder.txtChannelDisplayName.setOnFocusChangeListener(this);

                holder.txtChannelNumber = (EditText) convertView.findViewById(R.id.txtChannelNumber);
                holder.txtChannelNumber.setOnFocusChangeListener(this);
//            holder.tglPreset = (ToggleButton)convertView.findViewById(R.id.tglPreset);
            } else {
                holder.lblChannelDisplayName = (IPTTextView) convertView.findViewById(R.id.lblChannelDisplayName);
                holder.lblChannelNumber = (IPTTextView) convertView.findViewById(R.id.lblChannelNumber);
                holder.imbPreset = (ImageButton) convertView.findViewById(R.id.imbPreset);
                holder.imbPreset.setOnClickListener(this);
            }

            convertView.setTag(holder);
        }

        InputChannelPreset preset = channelPresets[position];
        if (isEditMode) {
            holder.txtChannelDisplayName.setTag(position);
            holder.txtChannelDisplayName.setText(preset.getDisplayName());
            holder.txtChannelDisplayName.addTextChangedListener(new DisplayTextTextWatcher(holder.txtChannelDisplayName));

            holder.txtChannelNumber.setText(Integer.toString(preset.getChannel()));
            holder.txtChannelNumber.setTag(position);
            holder.txtChannelNumber.addTextChangedListener(new ChannelTextWatcher(holder.txtChannelNumber));

//         holder.tglPreset.setChecked(preset.isSelected());         
        } else {
            holder.lblChannelDisplayName.setText(preset.getDisplayName());
            holder.lblChannelNumber.setText(Integer.toString(preset.getChannel()));

            holder.imbPreset.setTag(position);
        }

        return convertView;
    }

    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    private class DisplayTextTextWatcher implements TextWatcher {
        private EditText mEditText;

        public DisplayTextTextWatcher(EditText e) {
            mEditText = e;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            selectedIndex = Integer.parseInt(mEditText.getTag().toString());

            InputChannelPreset channelPreset = channelPresets[selectedIndex];
            if (!channelPreset.getDisplayName().equals(s.toString())) {
                channelPreset.setDisplayName(s.toString());
                if (channelPreset.getPresetId() == 0) {
                    // insert new channel preset
                    parentFragment.AddChannelPreset(0, s.toString());
                } else {
                    parentFragment.EditChannelPreset(channelPreset);
                }
            }
        }
    }

    private class ChannelTextWatcher implements TextWatcher {
        private EditText mEditText;

        public ChannelTextWatcher(EditText e) {
            mEditText = e;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            selectedIndex = Integer.parseInt(mEditText.getTag().toString());

            if (s.toString().length() <= 0)
                return;

            InputChannelPreset channelPreset = channelPresets[selectedIndex];
            int newChannel = Integer.parseInt(s.toString());
            if (channelPreset.getChannel() != newChannel) {
                channelPreset.setChannel(newChannel);
                if (channelPreset.getPresetId() == 0) {
                    // insert new channel preset
                    parentFragment.AddChannelPreset(newChannel, "");
                } else {
                    parentFragment.EditChannelPreset(channelPreset);
                }
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus)
            parentFragment.setEditModeSelectedPresetId(channelPresets[Integer.parseInt(v.getTag().toString())].getPresetId());
    }

    @Override
    public void onClick(View v) {
        parentFragment.switchChannelPreset(Integer.parseInt(v.getTag().toString()));
    }
}
