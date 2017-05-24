package com.imax.ipt.ui.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.imax.ipt.R;
import com.imax.ipt.common.Constants;
import com.imax.ipt.model.DeviceType;
import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable;
import com.imax.ipt.ui.util.TypeFaces;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanli on 2015/12/2.
 */
public class InputGridAdapter extends BaseAdapter {
    private static final int COLUM_NUMBER = 4;
    private LayoutInflater mInflater;
    private List<Object> mDataList = new ArrayList<>();
    private Context mContext;
    private int mSelectedPos = -1;

    public InputGridAdapter(Context context, List<?> list, LayoutInflater inflater) {
        this.mDataList =(List<Object>) list;
        this.mContext = context;
        this.mInflater = inflater;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vholder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_inputs_lob, null);
            vholder = new ViewHolder();
            vholder.img_input_icon = (ImageView) convertView.findViewById(R.id.img_input_icon);
            vholder.txt_input_name = (TextView) convertView.findViewById(R.id.txt_input_name);
            vholder.txt_input_name.setTypeface(TypeFaces.get(mContext, Constants.FONT_HAIRLINE_PATH));
            convertView.setTag(vholder);
            convertView.setBackground(mContext.getResources().getDrawable(R.drawable.selector_inputs_griditem_topleft));

        }else {
            vholder = (ViewHolder)convertView.getTag();
        }

        if(mDataList.get(position) == null) {
            return null;
        }

        DeviceType input = (DeviceType) mDataList.get(position);
        vholder.txt_input_name.setText(input.getDisplayName().toUpperCase());
        vholder.img_input_icon.setImageDrawable(FactoryDeviceTypeDrawable.createDeviceTypeDrawable(mContext.getResources(), input.getDeviceKind()));

//            switch (input.getDeviceKind()){
//
//                case Movie:
////                    vholder.txt_input_name.setText(mContext.getResources().getString(R.string.movies));
//                    vholder.img_input_icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.selector_menu_input_movie_button));
//
//                    break;
//                case Music:
////                    vholder.txt_input_name.setText(mContext.getResources().getString(R.string.music));
//                    vholder.img_input_icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.selector_menu_input_music_button));
//
//                    break;
//                case MediaDevices:
////                    vholder.txt_input_name.setText(mContext.getResources().getString(R.string.media_devices));
//                    vholder.img_input_icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.selector_menu_input_media_devices_button));
//                    break;
//                case TV:
////                    vholder.txt_input_name.setText(mContext.getResources().getString(R.string.online_movie));
//                    vholder.img_input_icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.selector_menu_input_online_movie_button));
//                    break;
//                case Security:
////                    vholder.txt_input_name.setText(mContext.getResources().getString(R.string.zaxel));
//                    vholder.img_input_icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.selector_menu_input_zaxel_button));
//                    break;
//                case Gaming:
////                    vholder.txt_input_name.setText(mContext.getResources().getString(R.string.gaming));
//                    vholder.img_input_icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.selector_menu_input_gaming_button));
//                    break;
//                case Karaoke:
////                    vholder.txt_input_name.setText(mContext.getResources().getString(R.string.external_input_karaoke));
//                    vholder.img_input_icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.selector_menu_input_karaoke_button));
//                    break;
//                case Extender:
////                    vholder.txt_input_name.setText(mContext.getResources().getString(R.string.external_input));
//                    vholder.img_input_icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.selector_menu_input_external_input_button));
//                    break;
//            default:
//                //do nothing.
//            }
        return convertView;
    }

    final class ViewHolder{
        TextView txt_input_name;
        EditText edit_input_name;
        ImageView img_input_icon;
    }
}
