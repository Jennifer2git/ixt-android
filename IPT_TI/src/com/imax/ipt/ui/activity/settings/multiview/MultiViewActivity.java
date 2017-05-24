package com.imax.ipt.ui.activity.settings.multiview;

import java.util.List;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.ui.SetupMultiviewEvent.MultiView;
import com.imax.ipt.controller.remote.VolumeController;
import com.imax.ipt.controller.remote.VolumeView;
import com.imax.ipt.controller.settings.MultiViewController;
import com.imax.ipt.model.Input;
import com.imax.ipt.ui.activity.BaseActivity;
import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable.DeviceKind;
import com.imax.ipt.ui.widget.verticalseekbar.VerticalSeekBar;

public class MultiViewActivity extends BaseActivity implements OnClickListener, VolumeView {
    public static String TAG = "MultiViewActivity";

    private DialogMultiviewLayoutFragment mDialogMultiviewLayoutFragment;
    private DialogMultiviewSelectInputFragment mDialogMultiviewSelectInputFragment;
    private ImageButton btnMultiViewLayout;
//   private EventBus mEventBus;

//   private ImageButton mBtnSourceOne;
//   private IPTTextView mTxtSourceOne;
//   private ImageButton mBtnSourceTwo;
//   private IPTTextView mTxtSourceTwo;
//   private ImageButton mBtnSourceThree;
//   private IPTTextView mTxtSourceThree;
//   private ImageButton mBtnSourceFour;
//   private IPTTextView mTxtSourceFour;
//
//   private ImageButton mBtnSelectInput1;
//   private ImageButton mBtnSelectInput2;
//   private ImageButton mBtnSelectInput3;
//   private ImageButton mBtnSelectInput4;
//
//   private ImageButton mBtnFocusAudio1;
//   private ImageButton mBtnFocusAudio2;
//   private ImageButton mBtnFocusAudio3;
//   private ImageButton mBtnFocusAudio4;
//
//   private ImageButton mBtnFullScreen1;
//   private ImageButton mBtnFullScreen2;
//   private ImageButton mBtnFullScreen3;
//   private ImageButton mBtnFullScreen4;

    private MultiView mCurrentView = MultiView.NotSet;

    private MultiViewController mMultiViewController;

    private VolumeController mVolumeController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_multi_view);

        mMultiViewController = new MultiViewController(this);
//      this.mEventBus = mMultiViewController.getEventBus();

        mVolumeController = new VolumeController(this);

        this.init();
    }

    protected void init()   // khh: init can be called multiple times during the lifetime of the Activity, which would span changes in multiple PIP modes
    {
//      mMultiViewController = new MultiViewController(this);      
        this.addMenuFragment();
        btnMultiViewLayout = (ImageButton) findViewById(R.id.btnMultiViewLayout);
        btnMultiViewLayout.setOnClickListener(this);
    }

    public void buildMultiview(MultiView multiView, List<Input> mInputs, int audioFocusOutputIndex) {
        FragmentTransaction transaction = null;

        DeviceKind[] deviceKinds = new DeviceKind[mInputs.size()];
        String[] deviceNames = new String[mInputs.size()];
        int[] inputIds = new int[mInputs.size()];
        boolean[] irSupporteds = new boolean[mInputs.size()];
        for (int i = 0; i < mInputs.size(); i++) {
            deviceKinds[i] = mInputs.get(i).getDeviceKind();
            deviceNames[i] = mInputs.get(i).getDisplayName();
            inputIds[i] = mInputs.get(i).getId();
            irSupporteds[i] = mInputs.get(i).isIrSupported();
        }

        switch (multiView) {
            case SINGLE: {
                mCurrentView = MultiView.SINGLE;
                NoPipFragment pipSourcesFragment = NoPipFragment.newInstance(deviceKinds, deviceNames, audioFocusOutputIndex, inputIds, irSupporteds);
                pipSourcesFragment.setController(mMultiViewController);
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.pipLayout, pipSourcesFragment);
                break;
            }

            case FOUR_UP: {
                mCurrentView = MultiView.FOUR_UP;
                PipQuadFragment pipSourcesFragment = PipQuadFragment.newInstance(deviceKinds, deviceNames, audioFocusOutputIndex, inputIds, irSupporteds);
                pipSourcesFragment.setController(mMultiViewController);
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.pipLayout, pipSourcesFragment);
                break;
            }

            case THREE_TOP: {
                mCurrentView = MultiView.THREE_TOP;
                PipThreeTop pipSourcesFragment = PipThreeTop.newInstance(deviceKinds, deviceNames, audioFocusOutputIndex, inputIds, irSupporteds);
                pipSourcesFragment.setController(mMultiViewController);
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.pipLayout, pipSourcesFragment);
                break;
            }

            case THREE_RIGTH: {
                mCurrentView = MultiView.THREE_RIGTH;
                PipThreeSide pipSourcesFragment = PipThreeSide.newInstance(deviceKinds, deviceNames, audioFocusOutputIndex, inputIds, irSupporteds);
                pipSourcesFragment.setController(mMultiViewController);
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.pipLayout, pipSourcesFragment);
                break;
            }

            case THREE_BOTTOM: {
                mCurrentView = MultiView.THREE_BOTTOM;
                PipThreeBottom pipSourcesFragment = PipThreeBottom.newInstance(deviceKinds, deviceNames, audioFocusOutputIndex, inputIds, irSupporteds);
                pipSourcesFragment.setController(mMultiViewController);
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.pipLayout, pipSourcesFragment);
                break;
            }
        }

        if (transaction != null) {
            try {
                transaction.commitAllowingStateLoss();
            } catch (Exception e) {
                Log.w(TAG, e.toString());
            }
        }
    }

//   private void buildInputs(int views)
//   {
///*      for (int i = 0; i < views; i++)
//      {
//         switch (i)
//         {
//         case 0:
//            mBtnSourceOne = (ImageButton) findViewById(R.id.btnSource1);
//            mTxtSourceOne = (IPTTextView) findViewById(R.id.txtSource1);
//            mBtnSourceOne.setImageDrawable(FactoryDeviceTypeDrawable.createDeviceTypeDrawable(getResources(), DeviceKind.NotSet));
//            mBtnSourceOne.setOnClickListener(this);
//
//            mBtnSelectInput1 = (ImageButton) findViewById(R.id.btnSelectInput1);
//            mBtnSelectInput1.setTag(i);
//            mBtnSelectInput1.setOnClickListener(this);
//            mBtnFocusAudio1 = (ImageButton) findViewById(R.id.btnFocusAudio1);
//            mBtnFocusAudio1.setOnClickListener(this);
//
//            mBtnFullScreen1 = (ImageButton) findViewById(R.id.mBtnFullScreen1);
//            mBtnFullScreen1.setOnClickListener(this);
//
//            break;
//         case//      mMultiViewController = new MultiViewController(this);      
//         this.addMenuFragment();
//         btnMultiViewLayout = (ImageButton) findViewById(R.id.btnMultiViewLayout);
//         btnMultiViewLayout.setOnClickListener(this); 1:
//            mBtnSourceTwo = (ImageButton) findViewById(R.id.btnSource2);
//            mTxtSourceTwo = (IPTTextView) findViewById(R.id.txtSource2);
//            mBtnSourceTwo.setImageDrawable(FactoryDeviceTypeDrawable.createDeviceTypeDrawable(getResources(), DeviceKind.NotSet));
//            mBtnSourceTwo.setOnClickListener(this);
//
//            mBtnSelectInput2 = (ImageButton) findViewById(R.id.btnSelectInput2);
//            mBtnSelectInput2.setOnClickListener(this);
//            mBtnSelectInput2.setTag(i);
//            mBtnFocusAudio2 = (ImageButton) findViewById(R.id.btnFocusAudio2);
//            mBtnFocusAudio2.setOnClickListener(this);
//
//            mBtnFullScreen2 = (ImageButton) findViewById(R.id.mBtnFullScreen2);
//            mBtnFullScreen2.setOnClickListener(this);
//
//            break;
//         case 2:
//            mBtnSourceThree = (ImageButton) findViewById(R.id.btnSource3);
//            mTxtSourceThree = (IPTTextView) findViewById(R.id.txtSource3);
//            mBtnSourceThree.setImageDrawable(FactoryDeviceTypeDrawable.createDeviceTypeDrawable(getResources(), DeviceKind.NotSet));
//            mBtnSourceThree.setOnClickListener(this);
//
//            mBtnSelectInput3 = (ImageButton) findViewById(R.id.btnSelectInput3);
//            mBtnSelectInput3.setOnClickListener(this);
//            mBtnSelectInput3.setTag(i);
//            mBtnFocusAudio3 = (ImageButton) findViewById(R.id.btnFocusAudio3);
//            mBtnFocusAudio3.setOnClickListener(this);
//
//            mBtnFullScreen3 = (ImageButton) findViewById(R.id.mBtnFullScreen3);
//            mBtnFullScreen3.setOnClickListener(this);
//            break;
//         case 3:
//            mBtnSourceFour = (ImageButton) findViewById(R.id.btnSource4);
//            mTxtSourceFour = (IPTTextView) findViewById(R.id.txtSource4);
//            mBtnSourceFour.setImageDrawable(FactoryDeviceTypeDrawable.createDeviceTypeDrawable(getResources(), DeviceKind.NotSet));
//            mBtnSourceFour.setOnClickListener(this);
//
//            mBtnSelectInput4 = (ImageButton) findViewById(R.id.btnSelectInput4);
//            mBtnSelectInput4.setOnClickListener(this);
//            mBtnSelectInput4.setTag(i);
//            mBtnFocusAudio4 = (ImageButton) findViewById(R.id.btnFocusAudio4);
//            mBtnFocusAudio4.setOnClickListener(this);
//
//            mBtnFullScreen4 = (ImageButton) findViewById(R.id.mBtnFullScreen4);
//            mBtnFullScreen4.setOnClickListener(this);
//            break;
//         }
//      }*/
//
//   }
//
//   private void setupInputs(List<DeviceType> deviceTypes)
//   {
///*      int i = 0;
//      for (DeviceType deviceType : deviceTypes)
//      {
//         switch (i)
//         {
//         case 0:
//            mTxtSourceOne.setText(deviceType.getDisplayName());
//            mBtnSourceOne.setImageDrawable(FactoryDeviceTypeDrawable.createDeviceTypeDrawable(getResources(), deviceType.getDeviceKind()));
//            mBtnSourceOne.setTag(deviceType);
//            mBtnFocusAudio1.setTag(deviceType.getId());
//            mBtnFullScreen1.setTag(deviceType.getId());
//            break;
//         case 1:
//            mTxtSourceTwo.setText(deviceType.getDisplayName());
//            mBtnSourceTwo.setImageDrawable(FactoryDeviceTypeDrawable.createDeviceTypeDrawable(getResources(), deviceType.getDeviceKind()));
//            mBtnSourceTwo.setTag(deviceType);
//            mBtnFocusAudio2.setTag(deviceType.getId());
//            mBtnFullScreen2.setTag(deviceType.getId());
//
//            break;
//         case 2:
//            mTxtSourceThree.setText(deviceType.getDisplayName());
//            mBtnSourceThree.setImageDrawable(FactoryDeviceTypeDrawable.createDeviceTypeDrawable(getResources(), deviceType.getDeviceKind()));
//            mBtnFocusAudio3.setTag(deviceType.getId());
//            mBtnSourceThree.setTag(deviceType);
//            mBtnFullScreen3.setTag(deviceType.getId());
//            break;
//         case 3:
//            mTxtSourceFour.setText(deviceType.getDisplayName());
//            mBtnSourceFour.setImageDrawable(FactoryDeviceTypeDrawable.createDeviceTypeDrawable(getResources(), deviceType.getDeviceKind()));
//            mBtnSourceFour.setTag(deviceType);
//            mBtnFocusAudio4.setTag(deviceType.getId());
//            mBtnFullScreen4.setTag(deviceType.getId());
//
//            break;
//         }
//         i++;
//      }
//
//      
//      // Setup Sound Focus
//      switch (mMultiViewController.getAudioFocusOutputIndex())
//      {
//      case 0:
//         mBtnFocusAudio1.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_audio_focus_btn_active));
//         break;
//      case 1:
//         if(mCurrentView==MultiView.SINGLE || mCurrentView==MultiView.NotSet) //TODO There is NPE when pass the several screens to one .
//         {
//            return;
//         }
//         mBtnFocusAudio1.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_audio_focus_btn_inactive));
//         mBtnFocusAudio2.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_audio_focus_btn_active));
//         mBtnFocusAudio3.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_audio_focus_btn_inactive));
//         mBtnFocusAudio4.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_audio_focus_btn_inactive));
//         break;
//      case 2:
//         if(mCurrentView==MultiView.SINGLE || mCurrentView==MultiView.NotSet)
//         {
//            return;
//         }
//         mBtnFocusAudio1.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_audio_focus_btn_inactive));
//         mBtnFocusAudio2.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_audio_focus_btn_inactive));
//         mBtnFocusAudio3.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_audio_focus_btn_active));
//         mBtnFocusAudio4.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_audio_focus_btn_inactive));
//         break;
//      case 3:
//         if(mCurrentView==MultiView.SINGLE || mCurrentView==MultiView.NotSet)
//         {
//            return;
//         }
//         mBtnFocusAudio1.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_audio_focus_btn_inactive));
//         mBtnFocusAudio2.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_audio_focus_btn_inactive));
//         mBtnFocusAudio3.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_audio_focus_btn_inactive));
//         mBtnFocusAudio4.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_multiview_audio_focus_btn_active));
//         break;
//
//      }*/
//   }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mMultiViewController.onDestroy();
        this.mVolumeController.destroy();
    }

    /**
     * @param mContext
     */
    public static void fire(Context mContext) {
        Intent intent = new Intent(mContext, MultiViewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    private void showDialogMultiview() {
        FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putInt(DialogMultiviewLayoutFragment.CURRENT_MULTIVIEW, mCurrentView.getPipMode());
        mDialogMultiviewLayoutFragment = new DialogMultiviewLayoutFragment();
        mDialogMultiviewLayoutFragment.SetController(mMultiViewController);
        mDialogMultiviewLayoutFragment.setArguments(bundle);
        mDialogMultiviewLayoutFragment.show(fm, "fragment_dialog_multiview");
    }

    public void showDialogSelectionInput(int outputIndex) {
        FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putInt(DialogMultiviewSelectInputFragment.OUTPUT_POSITION, outputIndex);
        mDialogMultiviewSelectInputFragment = new DialogMultiviewSelectInputFragment();
        mDialogMultiviewSelectInputFragment.setArguments(bundle);
        mDialogMultiviewSelectInputFragment.show(fm, "fragment_multiselect_input_fragment");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMultiViewLayout:
                showDialogMultiview();
                break;
        }
      
/*      Integer inputId = null;
      switch (v.getId())
      {
      case R.id.btnSelectInput1:
      case R.id.btnSelectInput2:
      case R.id.btnSelectInput3:
      case R.id.btnSelectInput4:
         Integer outPosition = (Integer) v.getTag();
         this.showDialogSelectionInput(outPosition);
         break;

      case R.id.btnMultiViewLayout:
         showDialogMultiview();
         break;
      case R.id.btnFocusAudio1:
      case R.id.btnFocusAudio2:
      case R.id.btnFocusAudio3:
      case R.id.btnFocusAudio4:
         inputId = (Integer) v.getTag();
         this.mEventBus.post(new SetAudioFocusHandler(inputId).getRequest());
         break;
      case R.id.mBtnFullScreen1:
      case R.id.mBtnFullScreen2:
      case R.id.mBtnFullScreen3:
      case R.id.mBtnFullScreen4:
         inputId = (Integer) v.getTag();
            this.mEventBus.post(new SetPipModeHandler((byte) MultiView.NotSet.getPipMode()).getRequest());
            this.mEventBus.post(new SetNowPlayingInputHandler(inputId).getRequest());
         break;

      case R.id.btnSource1:
      case R.id.btnSource2:
      case R.id.btnSource3:
      case R.id.btnSource4:
         DeviceType deviceType = (DeviceType) v.getTag();
         openRemoteControl(deviceType);
         break;

      }*/
    }

//   public void openRemoteControl(DeviceType deviceType)
//   {
//      // no need to updat Now Playing in control recall
//      
//      switch (deviceType.getDeviceKind())
//      {
//      case Gaming:
////         this.mEventBus.post(new SetNowPlayingInputHandler(deviceType.getId()).getRequest());
//         TVRemoteActivity.fire(this, deviceType.getId(), deviceType.getDisplayName());
//         break;
//      case MediaDevices:
////         this.mEventBus.post(new SetNowPlayingInputHandler(deviceType.getId()).getRequest());
//         TVRemoteActivity.fire(this, deviceType.getId(), deviceType.getDisplayName());
//         break;
//      case Movie:
//      case MediaPlayer:
//         Log.d(TAG, "Movie selected");
////         this.mEventBus.post(new SetNowPlayingInputHandler(deviceType.getId()).getRequest());
//         // display control is a movie is playing
//         mMultiViewController.GetPlayingMovie();
//         
//         // otherwise, display movie browsing screen
//         MovieLibraryActivity.fireToFront(this);
//         break;
//      case Music:
//         Log.d(TAG, "Music selected");
//         // display control is a track is playing
//         
//         // otherwise display music browsing screen         
//         MusicLibraryActivity.fire(this);
//         break;
//      case TV:
////         this.mEventBus.post(new SetNowPlayingInputHandler(deviceType.getId()).getRequest());
//         TVRemoteActivity.fire(this, deviceType.getId(), deviceType.getDisplayName());
//         break;
//      default:
//         Log.d(TAG, "This device is unsupported for input " + deviceType.getDeviceKind());
//         break;
//      }
//   }

    @Override
    public VerticalSeekBar getSeekBar() {
        return (VerticalSeekBar) findViewById(R.id.seekBarVolume);
    }

    @Override
    public ImageButton getMuteButton() {
        return (ImageButton) findViewById(R.id.btnVolMute);
    }
}
