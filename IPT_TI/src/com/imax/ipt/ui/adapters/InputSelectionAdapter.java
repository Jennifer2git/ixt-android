package com.imax.ipt.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.imax.ipt.R;
import com.imax.ipt.model.Input;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable;
import com.imax.ipt.ui.util.TypeFaces;

import java.util.List;

public class InputSelectionAdapter extends ArrayAdapter<Input> {
    private int resourceId;
    private Context mContext;

//    public InputSelectionAdapter(Context context, int resourceId, List<Input> deviceTypes) {
    public InputSelectionAdapter(Context context, int resourceId, List<Input> deviceTypes) {
        super(context, resourceId, deviceTypes);
        this.resourceId = resourceId;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout view = (LinearLayout) convertView;
        if (view == null) {
            ViewHolder viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            view = (RelativeLayout) inflater.inflate(R.layout.item_select_input, null);
            view = (LinearLayout) inflater.inflate(R.layout.item_inputs_lob, null);
//            ImageView mBtnSource = (ImageView) view.findViewById(R.id.btnSource);
//            IPTTextView mTextSource = (IPTTextView) view.findViewById(R.id.txtSource);
            ImageView mBtnSource = (ImageView) view.findViewById(R.id.img_input_icon);
            IPTTextView mTextSource = (IPTTextView) view.findViewById(R.id.txt_input_name);
            mTextSource.setTypeface(TypeFaces.get(mContext, "fonts/Montserrat-Hairline.otf"));
            viewHolder.mBtnSource = mBtnSource;
            viewHolder.mTxtSource = mTextSource;
            view.setTag(viewHolder);
        }
        Input mInput = this.getItem(position);
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.mBtnSource.setImageDrawable(FactoryDeviceTypeDrawable.createDeviceTypeDrawable(mContext.getResources(), mInput.getDeviceKind()));
        holder.mTxtSource.setText(mInput.getDisplayName());
        return view;
    }

    public class ViewHolder {
        public ImageView mBtnSource;
        public IPTTextView mTxtSource;
    }

}
