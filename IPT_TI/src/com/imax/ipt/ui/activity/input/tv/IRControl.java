package com.imax.ipt.ui.activity.input.tv;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;

import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.ir.IRPulseEvent;
import com.imax.ipt.ui.activity.media.MediaRemote;
import com.imax.ipt.ui.util.VibrateUtil;

// Auxiliary Controls
/// <summary>
/// 
/// </summary>
/// <param name="inputId"></param>
/// <param name="code">
///         
/*
 PlayPause = 0,
 Stop = 1,
 Pause = 2,
 Next = 3,
 Previous = 4,
 FastForward = 5, 
 Rewind = 6,

 RootMenu = 7,
 PopupMenu = 8,

 Audio = 9,
 Subtitles = 10,
 Enter = 11,

 DirectionUp = 12,
 DirectionDown = 13,
 DirectionLeft = 14,
 DirectionRight = 15, 

 // system volume
 VolumeUp = 16,
 VolumeDown = 17, 
 VolumeMute = 18,

 // custom inputs (IR function support depends on device type and manufacture IR signal implementation)
 Rec = 19,
 Power = 20,
 Numeric0 = 21,
 Numeric1 = 22, 
 Numeric2 = 23,
 Numeric3 = 24,
 Numeric4 = 25,
 Numeric5 = 26,
 Numeric6 = 27,
 Numeric7 = 28,
 Numeric8 = 29,
 Numeric9 = 30,
 ChannelUp = 31,
 ChannelDown = 32,
 Cancel = 33,
 Exit = 34,
 Info = 35,
 TvVideoInput = 36,
 Guide = 37,
 Prev = 38     (or Back)
 * */
public class IRControl extends MediaRemote {
    public static final String PREFERENCE_DEFAULT_CUSTOM_INPUT_CONTROL_COMPLEXITY_FULL = "default_custom_input_control_complexity_full";

    public static final String INPUT_ID = "INPUT_ID";
    //   protected int inputId;
    protected GridLayout gridLayout;
    protected ImageButton mBtnTVChDown;
    protected ImageButton mBtnTVChUp;
    protected Button mPrevCh;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case R.id.btnPrevCh:
                mEventBus.post(new IRPulseEvent(inputId, 38).getRequest());
                break;
            case R.id.btnTvChDown:
                mEventBus.post(new IRPulseEvent(inputId, 32).getRequest());
                break;

            case R.id.btnTvChUp:
                mEventBus.post(new IRPulseEvent(inputId, 31).getRequest());
                break;

            case R.id.btn1:
                mEventBus.post(new IRPulseEvent(inputId, 22).getRequest());
                break;
            case R.id.btn2:
                mEventBus.post(new IRPulseEvent(inputId, 23).getRequest());

                break;
            case R.id.btn3:
                mEventBus.post(new IRPulseEvent(inputId, 24).getRequest());

                break;
            case R.id.btn4:
                mEventBus.post(new IRPulseEvent(inputId, 25).getRequest());

                break;
            case R.id.btn5:
                mEventBus.post(new IRPulseEvent(inputId, 26).getRequest());

                break;
            case R.id.btn6:
                mEventBus.post(new IRPulseEvent(inputId, 27).getRequest());

                break;
            case R.id.btn7:
                mEventBus.post(new IRPulseEvent(inputId, 28).getRequest());

                break;
            case R.id.btn8:
                mEventBus.post(new IRPulseEvent(inputId, 29).getRequest());

                break;
            case R.id.btn9:
                mEventBus.post(new IRPulseEvent(inputId, 30).getRequest());

                break;
            case R.id.btn0:
                mEventBus.post(new IRPulseEvent(inputId, 21).getRequest());
                break;

            case R.id.btnEnter:
                mEventBus.post(new IRPulseEvent(inputId, 39).getRequest());
                break;

            case R.id.txtTvOk:
                mEventBus.post(new IRPulseEvent(inputId, 39).getRequest());
                break;

            case R.id.btnClear:
                mEventBus.post(new IRPulseEvent(inputId, 33).getRequest());
                break;

            case R.id.btnMenu:
                mEventBus.post(new IRPulseEvent(inputId, 7).getRequest());
                break;

            case R.id.btnTvFav:
                // open Channel Preset fragments
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("channelPreset");
                if (prev != null) {
                    ft.remove(prev);
                }
                // ft.addToBackStack(null);

                // Create and show the dialog
                ChannelPresetFragment channelPresetFragment = ChannelPresetFragment.newInstance(inputId);
//         channelPresetFragment.setEventBus(mEventBus);
                channelPresetFragment.show(ft, "channelPreset");
                break;
        }
        VibrateUtil.vibrate(getApplication(), 200);
    }

    public void setGridListener(OnClickListener clickListener) {
        if (gridLayout == null) {
            return;
        }
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View view = gridLayout.getChildAt(i);
            view.setOnClickListener(clickListener);
        }
    }

}
