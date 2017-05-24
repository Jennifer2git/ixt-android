package com.imax.ipt.ui.widget.gridview;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.imax.ipt.R;
import com.imax.ipt.model.Media;
import com.imax.ipt.ui.animation.AnimationHelper;
import com.imax.ipt.ui.layout.BetterImageView;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.util.ImageUtil;
import com.imax.ipt.ui.widget.circularseekbar.CircularSeekBar;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GridAdapter_bake extends ArrayAdapter<Media> implements SectionIndexer {
    public static final boolean DEBUG = false;
    private static final String TAG = "GridAdapter";
    private static final Map<String, SoftReference<ReplaceableBitmapDrawable>> mImageCache = new ConcurrentHashMap<String, SoftReference<ReplaceableBitmapDrawable>>();

    private int mEmptyElements = 0;

    private float IMAGE_WIDTH = 115;
    private float IMAGE_HEIGHT = 175;
    private float IMAGE_PADDING = 5;

    private final Context mContext;
    private Bitmap mDefaultBitmap;
    private final Handler mHandler;
    private float mScale;
    private int mImageWidth;
    private int mImageHeight;
    private int mImagePadding;
    private int mResource;
    private int mDefaultLogo;
    private int mTotalCount;
    private boolean mViewShowMediaLoadingProgress = false;

    private int mProgressMediaLoading = 0;
//   private ViewHolder loadingViewHolder = null;

    private ArrayList<Media> mMediaList;

    public static final String POINTER_ID = "-2";
    public static final String DEFAULT_ID = "-1";

    public GridAdapter_bake(Context context, int resource, ArrayList<Media> mMediaList, int defaultLogo, int imageWidth, int imageHeight, int imagePadding, int mEmptyElements) {
        super(context, resource, mMediaList);
        this.mContext = context;
        this.mResource = resource;
        this.mMediaList = mMediaList;
        this.IMAGE_HEIGHT = imageHeight;
        this.IMAGE_WIDTH = imageWidth;
        this.IMAGE_PADDING = imagePadding;
        this.mEmptyElements = mEmptyElements;
        this.mDefaultLogo = defaultLogo;
        this.init();
        mHandler = new Handler();
    }

    /**
     * @param totalCount
     */
    private void createLazyElements(int totalCount) {
        if (mMediaList == null) {
            return;
        }
        Media emptyElement = new Media();
        emptyElement.setId(DEFAULT_ID);
        for (int i = 0; i < getEmptyElements(); i++) {
            mMediaList.add(emptyElement);
        }

        Media pointer = new Media();
        pointer.setId(POINTER_ID);
        for (int i = 0; i < totalCount; i++) {
            mMediaList.add(pointer);
        }

        int tailCount = (totalCount % 3) + 6;
        emptyElement = new Media();
        emptyElement.setId(DEFAULT_ID);
        for (int i = 0; i < tailCount; i++) {
            mMediaList.add(emptyElement);
        }
    }

    /**
     *
     */
    private void init() {
        mDefaultBitmap = BitmapFactory.decodeResource(mContext.getResources(), mDefaultLogo);
        mScale = mContext.getResources().getDisplayMetrics().density;
        mImageWidth = (int) (IMAGE_WIDTH * mScale);
        mImageHeight = (int) (IMAGE_HEIGHT * mScale);
        mImagePadding = (int) (IMAGE_PADDING * mScale);
    }

    /**
     *
     *
     */
    private static final class ViewHolder {
        public RelativeLayout layoutMedia;
        public ImageView imgPoster;
        public IPTTextView txtMediaTitle;
        public RelativeLayout layoutProgress;
    }

    /**
     *
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout view = (RelativeLayout) convertView;
        if (view == null) {
            ViewHolder viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = (RelativeLayout) inflater.inflate(mResource, null);
            RelativeLayout layoutProgress = (RelativeLayout) view.findViewById(R.id.layoutProgress);
            RelativeLayout layoutMedia = (RelativeLayout) view.findViewById(R.id.layout);
            BetterImageView imagePoster = (BetterImageView) view.findViewById(R.id.imgPoster);
            imagePoster.setScaleType(ImageView.ScaleType.FIT_XY);
            imagePoster.setLayoutParams(new TwoWayAbsListView.LayoutParams(mImageWidth, mImageHeight));
            imagePoster.setPadding(mImagePadding, mImagePadding, mImagePadding, mImagePadding);

            IPTTextView txtMediaTitle = (IPTTextView) view.findViewById(R.id.txtMediaTitle);
            viewHolder.imgPoster = imagePoster;
            viewHolder.txtMediaTitle = txtMediaTitle;
            viewHolder.layoutMedia = layoutMedia;
            viewHolder.layoutProgress = layoutProgress;
            view.setTag(viewHolder);
        }

        Media media = this.getItem(position);
        ViewHolder holder = (ViewHolder) view.getTag();
        RelativeLayout layout = holder.layoutMedia;
        RelativeLayout layoutProgress = holder.layoutProgress;
        IPTTextView txtMediaTitle = holder.txtMediaTitle;
        ImageView imageView = holder.imgPoster;
        layout.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        txtMediaTitle.setVisibility(View.VISIBLE);
        layoutProgress.setVisibility(View.INVISIBLE);

        // put the default logo, so that re-use of the view will not display the previous coverart
        imageView.setImageResource(mDefaultLogo);
        if (media.getCoverArtPath().length() > 0)
            imageView.setImageDrawable(getCachedThumbnailAsync(media.getId(), media.getCoverArtPath()));

        Log.d("test", "path=" + media.getCoverArtPath());
//      if (position%2 ==0) {
//    	  
//    	  imageView.setImageDrawable(mContext.getResources().getDrawable(mDefaultLogo));
//		
//		}else {
//			imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default_movie1));
//		}

        txtMediaTitle.setText(media.getTitle());

        // hidde dummy rows
        if (media != null && media.getId().equals(POINTER_ID)) {
            layout.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            txtMediaTitle.setVisibility(View.VISIBLE);

        } else if (media != null && media.getId().equals(DEFAULT_ID)) {
            layout.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.INVISIBLE);
            txtMediaTitle.setVisibility(View.INVISIBLE);
        } else if (media != null && media.isLoading()) {
//         loadingViewHolder = holder;

            //if (ismViewShowMediaLoadingProgress())
            {
                imageView.setVisibility(View.INVISIBLE);
                layoutProgress.setVisibility(View.VISIBLE);
                CircularSeekBar mCircularSeekbar = (CircularSeekBar) layoutProgress.findViewById(R.id.progressSeekBar);

                ProgressBar pgrLoad = (ProgressBar) layoutProgress.findViewById(R.id.prgLoad);


                IPTTextView txtProgress = (IPTTextView) layoutProgress.findViewById(R.id.txtProgress);
                float newProgress = getmProgressMediaLoading() / 100f;
                txtProgress.setText(getmProgressMediaLoading() + "%");

                if (getmProgressMediaLoading() == 0) {
                    pgrLoad.setIndeterminate(true);
                } else {
                    pgrLoad.setIndeterminate(false);
                    pgrLoad.setProgress(getmProgressMediaLoading());
                }

                AnimationHelper.animate(mCircularSeekbar, newProgress, null);
            }
        }
        view.requestLayout();
        return view;

    }

    public void cleanup() {
        cleanupCache();
    }

    /**
     * Retrieves a drawable from the image cache, identified by the specified id.
     * If the drawable does not exist in the cache, it is loaded asynchronously
     * and added to the cache. If the drawable cannot be added to the cache, the
     * specified default drawable is returned.
     *
     * @param uri The uri of the drawable to retrieve
     * @return The drawable identified by id or defaultImage
     */
    private ReplaceableBitmapDrawable getCachedThumbnailAsync(String id, String url) {
        ReplaceableBitmapDrawable drawable = null;

        WorkQueue wq = WorkQueue.getInstance();
        synchronized (wq.mQueue) {
            SoftReference<ReplaceableBitmapDrawable> reference = mImageCache.get(id);
            if (reference != null) {
                drawable = reference.get();
            }

            if (drawable == null || !drawable.isLoaded()) {
                drawable = new ReplaceableBitmapDrawable(mDefaultBitmap);
                mImageCache.put(id, new SoftReference<ReplaceableBitmapDrawable>(drawable));
                ImageLoadingArgs args = new ImageLoadingArgs(mHandler, drawable, url);
                wq.execute(new ImageLoader(args));

            }
        }
        return drawable;
    }

    /**
     * Removes all the callbacks from the drawables stored in the memory cache.
     * This method must be called from the onDestroy() method of any activity
     * using the cached drawables. Failure to do so will result in the entire
     * activity being leaked.
     */
    public void cleanupCache() {
        for (SoftReference<ReplaceableBitmapDrawable> reference : mImageCache.values()) {
            final ReplaceableBitmapDrawable drawable = reference.get();
            if (drawable != null) drawable.setCallback(null);
        }

    }

    /**
     * Deletes the specified drawable from the cache.
     *
     * @param uri The uri of the drawable to delete from the cache
     */
    public void deleteCachedCover(Uri uri) {
        mImageCache.remove(ContentUris.parseId(uri));
    }

    /**
     * Class to asynchronously perform the loading of the bitmap
     */
    public class ImageLoader implements Runnable {
        protected ImageLoadingArgs mArgs = null;

        public ImageLoader(ImageLoadingArgs args) {
            mArgs = args;
        }

        public void run() {
            final Bitmap bitmap = ImageUtil.getImage(mArgs.mUrl);
            if (DEBUG) Log.i(TAG, "run() bitmap: " + bitmap);
            if (bitmap != null) {
                final ReplaceableBitmapDrawable d = mArgs.mDrawable;
                if (d != null) {
                    mArgs.mHandler.post(new Runnable() {
                        public void run() {
                            if (DEBUG) Log.i(TAG, "ImageLoader.run() - setting the bitmap for uri: " + mArgs.mUrl);
                            d.setBitmap(bitmap);
                        }
                    });
                } else {
                    Log.d(TAG, "ImageLoader.run() - FastBitmapDrawable is null for uri: " + mArgs.mUrl);
                }
            } else {
                Log.d(TAG, "ImageLoader.run() - bitmap is null for uri: " + mArgs.mUrl);
            }
        }

        public void cancel() {
            mImageCache.remove(mArgs.mUrl);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj != null && obj instanceof ImageLoader) // TODO THIS IS NOT GOOD
            // instanceof .
            {
                if (mArgs.mUrl != null) {
                    return mArgs.mUrl.equals(((ImageLoader) obj).mArgs);
                }
            }
            return false;
        }

        @Override
        public int hashCode() {
            return mArgs.mUrl.hashCode();
        }

    }

    /**
     * Class to hold all the parts necessary to load an image
     */
    public class ImageLoadingArgs {
        Handler mHandler;
        ReplaceableBitmapDrawable mDrawable;
        String mUrl;

        /**
         * @param contentResolver - ContentResolver to use
         * @param drawable        - FastBitmapDrawable whose underlying bitmap should be
         *                        replaced with new bitmap
         * @param uri             - Uri of image location
         */
        public ImageLoadingArgs(Handler handler, ReplaceableBitmapDrawable drawable, String mUrl) {
            this.mHandler = handler;
            this.mDrawable = drawable;
            this.mUrl = mUrl;
        }
    }

    public static class WorkQueue {
        private static WorkQueue sInstance = null;
        private static final int NUM_OF_THREADS = 10;
        private static final int MAX_QUEUE_SIZE = 100;

        private final int mNumOfThreads;
        private final PoolWorker[] mThreads;
        protected final LinkedList<ImageLoader> mQueue;

        public static synchronized WorkQueue getInstance() {
            if (sInstance == null) {
                sInstance = new WorkQueue(NUM_OF_THREADS);
            }
            return sInstance;
        }

        private WorkQueue(int nThreads) {
            mNumOfThreads = nThreads;
            mQueue = new LinkedList<ImageLoader>();
            mThreads = new PoolWorker[mNumOfThreads];

            for (int i = 0; i < mNumOfThreads; i++) {
                mThreads[i] = new PoolWorker();
                mThreads[i].start();
            }
        }

        public void execute(ImageLoader r) {
            synchronized (mQueue) {
                mQueue.remove(r);
                if (mQueue.size() > MAX_QUEUE_SIZE) {
                    mQueue.removeFirst().cancel();
                }
                mQueue.addLast(r);
                mQueue.notify();
            }
        }

        private class PoolWorker extends Thread {
            private boolean mRunning = true;

            @Override
            public void run() {
                Runnable r;

                while (mRunning) {
                    synchronized (mQueue) {
                        while (mQueue.isEmpty() && mRunning) {
                            try {
                                mQueue.wait();
                            } catch (InterruptedException ignored) {
                            }
                        }

                        r = mQueue.removeFirst();
                    }

                    // If we don't catch RuntimeException,
                    // the pool could leak threads
                    try {
                        r.run();
                    } catch (RuntimeException e) {
                        Log.e(TAG, "RuntimeException", e);
                    }
                }
                Log.i(TAG, "PoolWorker finished");
            }

            public void stopWorker() // TODO CALL THIS
            {
                mRunning = false;
            }
        }
    }

    @Override
    public int getPositionForSection(int section) {
        return 1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 20;
    }

    @Override
    public Object[] getSections() {
        return mContext.getResources().getStringArray(R.array.alphabet);
    }

    /**
     * Getters and setters
     *
     * @return
     */
    public int getEmptyElements() {
        return mEmptyElements;
    }

    /**
     * @param mEmptyElements
     */
    public void setEmptyElements(int mEmptyElements) {
        this.mEmptyElements = mEmptyElements;
    }

    /**
     * @return
     */
    public int getmTotalCount() {
        return mTotalCount;
    }

    /**
     *
     */
    public void addElements(List<? extends Media> movieLites, int index) {
        try {
//         if (index == 0)
//         {
//            index = getEmptyElements();
//         }

            index += getEmptyElements();
            for (Media media : movieLites) {
                this.mMediaList.set(index, media);
                index++;
            }
        } catch (Exception e)// TODO Fix this
        {
            Log.d(TAG, "This good error to fix " + e.getMessage(), e);
        }

    }

    /**
     * @param mTotalCount
     */

    public boolean ismViewShowMediaLoadingProgress() {
        return mViewShowMediaLoadingProgress;
    }

    public void setmViewShowMediaLoadingProgress(boolean mViewShowMediaLoadingProgress) {
//      if (!mViewShowMediaLoadingProgress)
//         loadingViewHolder = null;
        this.mViewShowMediaLoadingProgress = mViewShowMediaLoadingProgress;
    }

    public void setTotalCount(int mTotalCount) {
        this.createLazyElements(mTotalCount);
        this.mTotalCount = mTotalCount;
    }

    public int getmProgressMediaLoading() {
        return mProgressMediaLoading;
    }

    public void setmProgressMediaLoading(int mProgressMediaLoading) {
        this.mProgressMediaLoading = mProgressMediaLoading;
    }

//   public void invalidateLoadingViewHolder()
//   {
//      if (loadingViewHolder != null)
//      {
//         
//
//         
//         
//         RelativeLayout layoutProgress = loadingViewHolder.layoutProgress;
//
////       imageView.setVisibility(View.INVISIBLE);
//         layoutProgress.setVisibility(View.VISIBLE);
//         
//         CircularSeekBar mCircularSeekbar = (CircularSeekBar) layoutProgress.findViewById(R.id.progressSeekBar);
//         IPTTextView txtProgress = (IPTTextView) layoutProgress.findViewById(R.id.txtProgress);
//         float newProgress = getmProgressMediaLoading() / 100f;
//         txtProgress.setText(getmProgressMediaLoading() + "%");
//         AnimationHelper.animate(mCircularSeekbar, newProgress, null);
//         
//         layoutProgress.invalidate();
//         
//         
///*         Media media = this.getItem(position);
//         ViewHolder holder = (ViewHolder) view.getTag();
//         RelativeLayout layout = holder.layoutMedia;
//         RelativeLayout layoutProgress = holder.layoutProgress;
//         IPTTextView txtMediaTitle = holder.txtMediaTitle;
//         ImageView imageView = holder.imgPoster;
//         layout.setVisibility(View.VISIBLE);
//         imageView.setVisibility(View.VISIBLE);
//         txtMediaTitle.setVisibility(View.VISIBLE);
//         layoutProgress.setVisibility(View.INVISIBLE);
//         imageView.setImageDrawable(getCachedThumbnailAsync(media.getId(), media.getCoverArtPath()));
//         txtMediaTitle.setText(media.getTitle());
//
//         // hidde dummy rows
//         if (media != null && media.getId().equals(POINTER_ID))
//         {
//            layout.setVisibility(View.VISIBLE);
//            imageView.setVisibility(View.VISIBLE);
//            txtMediaTitle.setVisibility(View.VISIBLE);
//
//         }
//         else if (media != null && media.getId().equals(DEFAULT_ID))
//         {
//            layout.setVisibility(View.INVISIBLE);
//            imageView.setVisibility(View.INVISIBLE);
//            txtMediaTitle.setVisibility(View.INVISIBLE);
//         }
//         else if (media != null && media.isLoading())
//         {
//            if (ismViewShowMediaLoadingProgress())
//            {
//               imageView.setVisibility(View.INVISIBLE);
//               layoutProgress.setVisibility(View.VISIBLE);
//               CircularSeekBar mCircularSeekbar = (CircularSeekBar) layoutProgress.findViewById(R.id.progressSeekBar);
//               IPTTextView txtProgress = (IPTTextView) layoutProgress.findViewById(R.id.txtProgress);
//               float newProgress = getmProgressMediaLoading() / 100f;
//               txtProgress.setText(getmProgressMediaLoading() + "%");
//               AnimationHelper.animate(mCircularSeekbar, newProgress, null);
//               
//               loadingViewHolder = holder;
//            }
//         }*/
//      }
//   }
}
