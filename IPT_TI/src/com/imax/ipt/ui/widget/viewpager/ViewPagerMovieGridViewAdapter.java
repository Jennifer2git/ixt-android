//package com.imax.ipt.ui.widget.viewpager;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import com.imax.ipt.R;
//import com.imax.ipt.model.Media;
//import com.imax.ipt.ui.layout.IPTTextView;
//import com.imax.ipt.ui.widget.gridview.TwoWayAbsListView;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by yanli on 2015/12/1.
// */
//public class ViewPagerMovieGridViewAdapter extends BaseAdapter {
//
//    private List<Media> list;
//    private Context context;
//    /** ViewPager页码 */
//    private int index;
//    /** 根据屏幕大小计算得到的每页item个数 */
//    private int pageItemCount;
//    /** 传进来的List的总长度 */
//    private int totalSize;
//
//    /** 当前页item的实际个数 */
//    // private int itemRealNum;
//
//    public ViewPagerMovieGridViewAdapter(Context context,List<?> list){
//        this.context = context;
//        this.list = (List<Media>)list;
//    }
//
//    public ViewPagerMovieGridViewAdapter(Context context, int index, List<?> list, int pageItemCount) {
//        this.context = context;
//        this.index = index;
//        this.list = new ArrayList<>();
//        this.pageItemCount = pageItemCount;
//        totalSize = list.size();
//        int list_index = index * pageItemCount;
//        for(int i= list_index; i<list.size(); i++){
//            this.list.add((Media)list.get(i));
//        }
//    }
//
//    @Override
//    public int getCount() {
//        int size = totalSize / pageItemCount;
//        if (index == size)
//            return totalSize - pageItemCount * index;
//        else
//            return pageItemCount;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        {
//            RelativeLayout view = (RelativeLayout) convertView;
//            if (view == null) {
//                ViewHolder viewHolder = new ViewHolder();
//                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                view = (RelativeLayout) inflater.inflate(mResource, null);
//                RelativeLayout layoutProgress = (RelativeLayout) view.findViewById(R.id.layoutProgress);
//                RelativeLayout layoutMedia = (RelativeLayout) view.findViewById(R.id.layout);
//                BetterImageView imagePoster = (BetterImageView) view.findViewById(R.id.imgPoster);
//                imagePoster.setScaleType(ImageView.ScaleType.FIT_XY);
//                imagePoster.setLayoutParams(new TwoWayAbsListView.LayoutParams(mImageWidth, mImageHeight));
//                imagePoster.setPadding(mImagePadding, mImagePadding, mImagePadding, mImagePadding);
//
//                IPTTextView txtMediaTitle = (IPTTextView) view.findViewById(R.id.txtMediaTitle);
//                viewHolder.imgPoster = imagePoster;
//                viewHolder.txtMediaTitle = txtMediaTitle;
//                viewHolder.layoutMedia = layoutMedia;
//                viewHolder.layoutProgress = layoutProgress;
//                view.setTag(viewHolder);
//            }
//
//            Media media = this.getItem(position);
//            ViewHolder holder = (ViewHolder) view.getTag();
//            RelativeLayout layout = holder.layoutMedia;
//            RelativeLayout layoutProgress = holder.layoutProgress;
//            IPTTextView txtMediaTitle = holder.txtMediaTitle;
//            ImageView imageView = holder.imgPoster;
//            layout.setVisibility(View.VISIBLE);
//            imageView.setVisibility(View.VISIBLE);
//            txtMediaTitle.setVisibility(View.VISIBLE);
//            layoutProgress.setVisibility(View.INVISIBLE);
//
//            // put the default logo, so that re-use of the view will not display the previous coverart
//            imageView.setImageResource(mDefaultLogo);
//            if (media.getCoverArtPath().length() > 0)
//                imageView.setImageDrawable(getCachedThumbnailAsync(media.getId(), media.getCoverArtPath()));
//
//            Log.d("test", "path=" + media.getCoverArtPath());
////      if (position%2 ==0) {
////
////    	  imageView.setImageDrawable(mContext.getResources().getDrawable(mDefaultLogo));
////
////		}else {
////			imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default_movie1));
////		}
//
//            txtMediaTitle.setText(media.getTitle());
//
//            // hidde dummy rows
//            if (media != null && media.getId().equals(POINTER_ID)) {
//                layout.setVisibility(View.VISIBLE);
//                imageView.setVisibility(View.VISIBLE);
//                txtMediaTitle.setVisibility(View.VISIBLE);
//
//            } else if (media != null && media.getId().equals(DEFAULT_ID)) {
//                layout.setVisibility(View.INVISIBLE);
//                imageView.setVisibility(View.INVISIBLE);
//                txtMediaTitle.setVisibility(View.INVISIBLE);
//            } else if (media != null && media.isLoading()) {
////         loadingViewHolder = holder;
//
//                //if (ismViewShowMediaLoadingProgress())
//                {
//                    imageView.setVisibility(View.INVISIBLE);
//                    layoutProgress.setVisibility(View.VISIBLE);
//                    CircularSeekBar mCircularSeekbar = (CircularSeekBar) layoutProgress.findViewById(R.id.progressSeekBar);
//
//                    ProgressBar pgrLoad = (ProgressBar) layoutProgress.findViewById(R.id.prgLoad);
//
//
//                    IPTTextView txtProgress = (IPTTextView) layoutProgress.findViewById(R.id.txtProgress);
//                    float newProgress = getmProgressMediaLoading() / 100f;
//                    txtProgress.setText(getmProgressMediaLoading() + "%");
//
//                    if (getmProgressMediaLoading() == 0) {
//                        pgrLoad.setIndeterminate(true);
//                    } else {
//                        pgrLoad.setIndeterminate(false);
//                        pgrLoad.setProgress(getmProgressMediaLoading());
//                    }
//
//                    AnimationHelper.animate(mCircularSeekbar, newProgress, null);
//                }
//            }
//            view.requestLayout();
//            return view;
//
//        }
//    }
//
//    class ViewHolder {
//        public RelativeLayout layoutMedia;
//        public ImageView imgPoster;
//        public IPTTextView txtMediaTitle;
//        public RelativeLayout layoutProgress;
//
//        protected void findViews() {
////            mView = context;
////            iv_cover = (ImageView) mView.findViewById(R.id.imgPoster);
////            tv_title = (TextView) mView.findViewById(R.id.txtMediaTitle);
////            progress = (ProgressBar) findViewById(R.id.pro)
//        }
//
//        protected void updateViews(int position, Object inst) {
////            iv_cover.setImageURI(Uri.parse(list.get(position).getCoverArtPath()));
////            tv_title.setText(list.get(position).getTitle());
////            progress
//        }
//    }
//
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
