package com.imax.ipt.ui.widget.horizontallistview;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.imax.ipt.R;
import com.imax.ipt.common.Constants;

import java.util.List;

public class HListViewPickerAdapter extends BaseAdapter {
	private List<String> mTitles;
	private Context mContext;
	private LayoutInflater mInflater;
	private int selectIndex = -1;
	private Typeface mFace;


	public HListViewPickerAdapter(Context context, List titles) {
		this.mContext = context;
		this.mTitles = titles;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
		mFace = Typeface.createFromAsset(mContext.getAssets(), Constants.FONT_LIGHT_PATH);
	}


	@Override
	public int getCount() {
		return mTitles.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.hlist_row_picker_item, null);
			holder = new ViewHolder();
			holder.mTitle = (TextView) convertView.findViewById(R.id.txt_picker_title);
			holder.mTitle.setTypeface(mFace);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position == selectIndex) {
			convertView.setSelected(true);
			holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.gold));
			holder.mTitle.getPaint().setFakeBoldText(true);
		} else {
			convertView.setSelected(false);
			holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.gray_light));
			holder.mTitle.getPaint().setFakeBoldText(false);

		}

		holder.mTitle.setText(mTitles.get(position));
		return convertView;
	}

	private static class ViewHolder {
		private TextView mTitle;
	}

	public void setSelectIndex(int i) {
		selectIndex = i;
	}



	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		super.registerDataSetObserver(observer);
	}
}