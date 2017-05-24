package com.imax.ipt.ui.activity.input.gaming;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.common.Constants;
import com.imax.ipt.controller.eventbus.handler.push.AudioMuteChangedEvent;
import com.imax.ipt.controller.eventbus.handler.remote.ExecuteRemoteControlHandler;
import com.imax.ipt.ui.activity.LobbyActivity;
import com.imax.ipt.ui.activity.input.DialogZoomFragment;
import com.imax.ipt.ui.activity.input.InputActivity;
import com.imax.ipt.ui.activity.media.MediaRemote;
import com.imax.ipt.ui.activity.media.MediaRemote.ExecuteRemoteControl;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable.DeviceKind;
import com.imax.ipt.ui.viewmodel.MenuLibraryElement;
import com.imax.iptevent.EventBus;

import java.util.ArrayList;
import java.util.List;

public class GamingActivity extends InputActivity implements OnClickListener {

    private List<MenuLibraryElement> mMenuOptions = new ArrayList<MenuLibraryElement>();
    public static final String TAG = GamingActivity.class.getSimpleName();

    private Button mbtnZoom;
    private Button mbtnMaster;
    protected EventBus mEventBus;
    private IPTTextView mTxtTitle;
    private String mTitle;
    private String guid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_gaming);
//      this.addMenuLibraryFragment(getResources().getString(R.string.gaming), mMenuOptions,520);

        init1();
        this.init();

    }

    protected void init() {
        addMenuFragment();

        this.mTitle = getIntent().getStringExtra(MediaRemote.MEDIA_TITLE);
        this.guid = getIntent().getStringExtra(MediaRemote.MEDIA_ID);
        this.mTxtTitle = (IPTTextView) findViewById(R.id.txtTitle);
        this.mTxtTitle.setTypeface(Typeface.createFromAsset(getAssets(), Constants.FONT_LIGHT_PATH));
        this.mTxtTitle.setText(mTitle.toUpperCase());

        this.mGridLayout = (GridLayout) findViewById(R.id.gridLayout);//how to remove here

        this.setupView(mInputController.getInputs(DeviceKind.Game));

        MenuLibraryElement menuConsole = new MenuLibraryElement(1, getResources().getString(R.string.console), null, null);
        mMenuOptions.add(menuConsole);

        mbtnZoom = (Button) findViewById(R.id.btnZoom);
        mbtnZoom.setVisibility(View.VISIBLE);
        mbtnZoom.setOnClickListener(this);
        mbtnMaster = (Button) findViewById(R.id.btnMenuMaster);
        mbtnMaster.setOnClickListener(this);
    }


    private void init1() {
        mEventBus = IPT.getInstance().getEventBus();
        mEventBus.register(this);
    }

    /**
     * @param
     */
    public static void fire(Context context, String guid, String title, int inputId) {

        Intent intent = new Intent(context, GamingActivity.class);
        intent.putExtra(MediaRemote.MEDIA_ID, guid);
        intent.putExtra(MediaRemote.MEDIA_TITLE, title);
//        intent.putExtra(INPUT_ID, inputId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
    }


    /**
     * @param audioMuteChangedEvent
     */
    public void onEvent(AudioMuteChangedEvent audioMuteChangedEvent) {
//        final boolean mute = audioMuteChangedEvent.ismMute();
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mBtnMute.setPressed(mute);
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnZoom:
                mEventBus.post(new ExecuteRemoteControlHandler(ExecuteRemoteControl.Zoom.toString()).getRequest());
                showDialogZoom();
                break;
            case R.id.btnMenuMaster:
                LobbyActivity.fire(this);
                break;
        }
    }

    private void showDialogZoom() {
        FragmentManager fm = getSupportFragmentManager();
        DialogZoomFragment mDialogZoomFragment = DialogZoomFragment.newInstance(mInputController.getInputs(DeviceKind.Game).get(0).getId());
        mDialogZoomFragment.show(fm, "fragment_dialog_zoom");
    }

}
