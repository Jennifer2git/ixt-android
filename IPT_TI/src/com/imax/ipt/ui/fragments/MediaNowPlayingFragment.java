//package com.imax.ipt.ui.fragments;
//
//import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnTouchListener;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import com.imax.ipt.R;
//import com.imax.ipt.model.Media;
//import com.imax.ipt.ui.activity.input.tv.IRControl;
//import com.imax.ipt.ui.layout.IPTTextView;
//import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable.DeviceKind;
//import com.imax.ipt.ui.util.ImageUtil;
//import com.imax.ipt.ui.viewmodel.RemoteControlUtil;
//
//public class MediaNowPlayingFragment extends Fragment implements OnTouchListener
//{
//
//   private Media media;
//   private ImageView mImagePoster;
//   private IPTTextView mTxtTitle;
//   private LoaderImage mLoaderImg;
//   private DeviceKind deviceKind;
//
//   @Override
//   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
//   {
//      View mMediaNowPlayingMovie = inflater.inflate(R.layout.fragment_media_now_playing, null);
//      mMediaNowPlayingMovie.setOnTouchListener(this);
//      this.init(mMediaNowPlayingMovie);
//      return mMediaNowPlayingMovie;
//   }
//
//   private void init(View view)
//   {
//      Media media = getMedia();
//      if (media != null)
//      {
//         mImagePoster = (ImageView) view.findViewById(R.id.imgPoster);
//         mTxtTitle = (IPTTextView) view.findViewById(R.id.txtMediaTitle);
//         switch (media.getMediaType())
//         {
//         case MOVIE:
//            mLoaderImg = new LoaderImage();
//            mLoaderImg.execute(media.getCoverArtPath());
//            mTxtTitle.setText(media.getTitle());
//            deviceKind = DeviceKind.Movie;
//            break;
//         case ACTOR:
//            break;
//         case ALBUMS:
//            break;
//         case ARTIST:
//            break;
//         case DIRECTOR:
//            break;
//         default:
//            break;
//
//         }
//
//      }
//   }
//
//   @Override
//   public void onDestroy()
//   {
//      super.onDestroy();
//      if (mLoaderImg != null)
//      {
//         mLoaderImg.cancel(true);
//      }
//   }
//
//   /**
//    * TODO CREATE GENERIC CLASS FOR THIS
//    * 
//    * @author rlopez
//    * 
//    */
//   class LoaderImage extends AsyncTask<String, Void, Bitmap>
//   {
//      @Override
//      protected Bitmap doInBackground(String... params)
//      {
//         return ImageUtil.getImage(params[0]);
//      }
//
//      @Override
//      protected void onPostExecute(Bitmap result)
//      {
//         super.onPostExecute(result);
//
//         if (result != null)
//         {
//            BitmapDrawable background = new BitmapDrawable(getActivity().getResources(), result);
//            mImagePoster.setImageDrawable(background);
//         }
//      }
//
//   }
//
//   /**
//    * Getters and Setters
//    * 
//    */
//
//   public Media getMedia()
//   {
//      return media;
//   }
//
//   public void setMedia(Media media)
//   {
//      this.media = media;
//   }
//
//   @Override
//   public boolean onTouch(View v, MotionEvent event)
//   {
//      if(event.getAction()==MotionEvent.ACTION_DOWN)
//      {         
//         SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
//         boolean isControlComplexityFull = prefs.getBoolean(IRControl.PREFERENCE_DEFAULT_CONTROL_COMPLEXITY_FULL, Boolean.FALSE);
//         
//         RemoteControlUtil.openRemoteControl(getActivity(), 0, media.getTitle(), deviceKind, isControlComplexityFull);
//      }
//       return false;
//   }
//
//}
