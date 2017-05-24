package com.imax.ipt.ui.widget.horizontallistview;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.imax.ipt.R;
import com.imax.ipt.common.Constants;
import com.imax.ipt.ui.viewmodel.MenuLibraryElement;

import java.util.List;

public class HorizontalListViewAdapter extends BaseAdapter {
	private int[] mIconIDs;
	private List<MenuLibraryElement> mTitles;
	private Context mContext;
	private LayoutInflater mInflater;
	Bitmap iconBitmap;
	private int selectIndex = -1;
	private boolean isSubMenu = false;


	public HorizontalListViewAdapter(Context context, List titles,Boolean isSubMenu) {
		this.mContext = context;
		this.mTitles = titles;
		this.isSubMenu = isSubMenu;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	}


	@Override
	public int getCount() {
//		return mIconIDs.length;
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
			holder = new ViewHolder();
			if (isSubMenu) {
				convertView = mInflater.inflate(R.layout.horizonlist_row_checkable_submenu_item, null);
				holder.mTitle = (TextView) convertView.findViewById(R.id.text_list_subitem);

			} else {
				convertView = mInflater.inflate(R.layout.horizonlist_row_checkable_menu_item, null);
				holder.mTitle = (TextView) convertView.findViewById(R.id.text_list_item);

			}
			Typeface face = Typeface.createFromAsset(convertView.getContext().getAssets(), Constants.FONT_LIGHT_PATH);
			holder.mTitle.setTypeface(face);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position == selectIndex) {
			convertView.setSelected(true);
			String name = (String) mTitles.get(position).getName();
			holder.mTitle.setText(Html.fromHtml("<u>" + name + "</u>"));
//			tvTest.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //ÏÂ»®Ïß
//			tvTest.getPaint().setAntiAlias(true);//¿¹¾â³Ý

		} else {
			convertView.setSelected(false);
			holder.mTitle.setText((String)mTitles.get(position).getName());
		}

		return convertView;
	}

	private static class ViewHolder {
		private TextView mTitle;
		private ImageView mImage;
	}

//	private Bitmap getPropThumnail(int id) {
//		Drawable d = mContext.getResources().getDrawable(id);
////		Bitmap b = BitmapUtil.drawableToBitmap(d);
////		Bitmap bb = BitmapUtil.getRoundedCornerBitmap(b, 100);
//		int w = mContext.getResources().getDimensionPixelOffset(R.dimen.thumnail_default_width);
//		int h = mContext.getResources().getDimensionPixelSize(R.dimen.thumnail_default_height);
//
//		Bitmap thumBitmap = ThumbnailUtils.extractThumbnail(b, w, h);
//
//		return thumBitmap;
//	}

	public void setSelectIndex(int i) {
		selectIndex = i;
	}



	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		super.registerDataSetObserver(observer);
	}
}