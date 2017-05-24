package com.imax.ipt.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.*;
import android.view.View.OnTouchListener;
import android.widget.*;
import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.common.Constants;
import com.imax.ipt.controller.GlobalController;
import com.imax.ipt.controller.eventbus.handler.input.GetNowPlayingInputHandler;
import com.imax.ipt.controller.eventbus.handler.input.SetNowPlayingInputHandler;
import com.imax.ipt.controller.eventbus.handler.power.SwitchSystemPowerHandler;
import com.imax.ipt.controller.eventbus.handler.remote.ExecuteRemoteControlHandler;
import com.imax.ipt.controller.inputs.InputController;
import com.imax.ipt.model.DeviceType;
import com.imax.ipt.model.Input;
import com.imax.ipt.ui.activity.input.DialogSetInputNameFragment;
import com.imax.ipt.ui.activity.input.gaming.GamingActivity;
import com.imax.ipt.ui.activity.input.gdc.GdcActivity;
import com.imax.ipt.ui.activity.input.karaoke.KaraokeActivity;
import com.imax.ipt.ui.activity.input.movie.MovieLibraryActivity;
import com.imax.ipt.ui.activity.input.movie.MozaxRemoteControlActivity;
import com.imax.ipt.ui.activity.input.music.MusicLibraryActivity;
import com.imax.ipt.ui.activity.input.tv.TVActivity;
import com.imax.ipt.ui.activity.media.*;
import com.imax.ipt.ui.activity.room.LightingActivity;
import com.imax.ipt.ui.activity.settings.maintenance.MaintenceActivity;
import com.imax.ipt.ui.activity.settings.preferences.OtherActivity;
import com.imax.ipt.ui.activity.settings.preferences.PreferencesActivity;
import com.imax.ipt.ui.adapters.InputGridAdapter;
import com.imax.ipt.ui.adapters.InputSelectionAdapter;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.util.Blur;
import com.imax.ipt.ui.util.DisplayUtil;
import com.imax.ipt.ui.util.VibrateUtil;
import com.imax.ipt.ui.viewmodel.MenuMasterElement;
import com.imax.iptevent.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by yanli on 2015/9/29.
 * Main LobbyActivity show all the menu
 */
public class LobbyActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnTouchListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    public final String TAG = LobbyActivity.class.getSimpleName();
    public final String ACTION_SHOW_LOBBY = "com.imax.ipt.ui.activity.LobbyActivity.action_show_lobby";

    private long downTime = 0;
    private long upTime = 0;
    private Handler mHandler = new Handler();

    private GridView mInputsGridView;
    private InputGridAdapter mInputGridAdapter;

    private LayoutInflater inflater;
    private List<Object> dataList = new ArrayList<Object>();

    private final int COLUM_NUMBER = 4;

    private Typeface mFaceHair;
    private Typeface mFaceLight;

    private EventBus mEventBus;
    private ImageButton mBtnClose;
    private GridView gridView;
    private InputSelectionAdapter mInputSelectionAdapter;
    private List<Input> inputs = new ArrayList<>();
    private Integer outputPostion;

    private List<MenuMasterElement> mElements = new ArrayList<MenuMasterElement>();

    private ImageView imgBlur;

    private Button btnPowerOff;
    private Button btnLightsOn;
    private Button btnLightsOff;
    private ImageButton btnLights;

    protected SeekBar mSeekBarVolumen;
    protected ImageButton mBtnMute;
    protected ImageButton volupButton;
    protected ImageButton voldownButton;

    private boolean mClosed = false;
    private static boolean hasLighting;

    public static int mVolume = 0;
    public static boolean mMute = false;

    private DialogSetInputNameFragment mDialogSetInputNameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.mEventBus = IPT.getInstance().getEventBus();
        this.mEventBus.register(this);
        setContentView(R.layout.activity_lobby_layout);
        super.onCreate(savedInstanceState);
    }


    private void initUI() {
        mFaceHair = Typeface.createFromAsset(getAssets(), Constants.FONT_HAIRLINE_PATH);
        mFaceLight = Typeface.createFromAsset(getAssets(), Constants.FONT_LIGHT_PATH);

        ImageButton btnRemote = (ImageButton) findViewById(R.id.btn_remote);
        ImageButton btnMaintence = (ImageButton) findViewById(R.id.btn_maintence);
        ImageButton btnOthers = (ImageButton) findViewById(R.id.btn_others);
        ImageButton btnPrefer = (ImageButton) findViewById(R.id.btn_prefer);
        ImageButton btnPoweroffHP = (ImageButton) findViewById(R.id.btn_poweroff_hp);
        ImageButton btnClose = (ImageButton) findViewById(R.id.btn_nav_close);
        btnLights = (ImageButton) findViewById(R.id.btn_lights);
        btnLightsOn = (Button) findViewById(R.id.btn_lights_on);
        btnLightsOff = (Button) findViewById(R.id.btn_lights_off);
        btnPowerOff = (Button) findViewById(R.id.btn_power_off);
        imgBlur = (ImageView) findViewById(R.id.img_blur);

        btnLights.setOnClickListener(this);
        setLightingShow();

        btnPowerOff.setTypeface(mFaceHair);
        btnPowerOff.setOnTouchListener(btnPowerOffTouchListener);

//        Button btnSettings = (Button) findViewById(R.id.btn_settings);
//        btnRemote.setOnClickListener(this);
//        btnSettings.setOnClickListener(this);
        btnPoweroffHP.setVisibility(View.GONE);
        btnRemote.setVisibility(View.GONE);
        btnMaintence.setOnClickListener(this);
        btnOthers.setOnClickListener(this);
        btnPrefer.setOnClickListener(this);
        btnClose.setOnClickListener(this);
    }


    final Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            mEventBus.post(new SwitchSystemPowerHandler(false).getRequest());
            VibrateUtil.vibrate(LobbyActivity.this, 100);
        }
    };

    OnTouchListener btnPowerOffTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                btnPowerOff.setTypeface(mFaceLight);
                btnPowerOff.setTextColor(getResources().getColor(R.color.color_actionable));
                downTime = System.currentTimeMillis();
                mHandler.postDelayed(mRunnable, 4000);
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {

                btnPowerOff.setTypeface(mFaceHair);
                btnPowerOff.setTextColor(getResources().getColor(R.color.color_noactionable_text));
                upTime = System.currentTimeMillis();
                if ((upTime - downTime) > 4000) {

                } else {
                    mHandler.removeCallbacks(mRunnable);
                    Toast toast = Toast.makeText(getActivity(), R.string.toast_long_press_to_power_off, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, DisplayUtil.dip2px(LobbyActivity.this, 15));

                    TextView textView = new TextView(IPT.getInstance());
                    textView.setBackgroundResource(R.drawable.global_notification_bg);
                    if (getResources().getConfiguration().locale.getCountry().equals("CN")) {
                        textView.setWidth(300);
                    } else {
                        textView.setWidth(700);
                    }
                    textView.setTextColor(getResources().getColor(R.color.color_lobby));
                    textView.setTypeface(mFaceLight);
                    textView.setTextSize(20);
                    textView.setGravity(Gravity.CENTER);
                    textView.setText(R.string.toast_long_press_to_power_off);
                    toast.setView(textView);
                    toast.show();
                }
            }
            return false;
        }
    };


    private void initData() {
        initInputsGirdView();
    }

    private Activity getActivity() {
        return this;
    }

    private void initInputsGirdView() {
        mInputsGridView = (GridView) findViewById(R.id.grid_inputs);

        Vector<DeviceType> deviceTypes = (Vector<DeviceType>) GlobalController.getInstance()
                .readConfigFromPreference(Constants.CONFIG_KEY_ACTIVE_DEVICE);

        if (deviceTypes == null) {
            showNoChannel(this, getActivity().getString(R.string.toast_device_no));
            Toast.makeText(this, "No channel to show. Please restart the application.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        dataList.clear();
        dataList.addAll(deviceTypes);

        inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInputGridAdapter = new InputGridAdapter(this, dataList, inflater);
        mInputsGridView.setAdapter(mInputGridAdapter);

//        int size = dataList.size();
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        float density = dm.density;
//        int allWidth = (int) (210 * size * density);
//        int itemWidth = (int) (220 * density);

//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                allWidth, itemWidth);
//        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.ll_item_lobby);

//        mInputsGridView.setLayoutParams(params);
//        mInputsGridView.setColumnWidth(itemWidth);
//        mInputsGridView.setGravity(Gravity.CENTER);
//        mInputsGridView.setStretchMode(GridView.NO_STRETCH);
//        mInputsGridView.setNumColumns(COLUM_NUMBER);
//        mInputsGridView.setSelector(getResources().getDrawable(android.R.color.transparent));

        mInputsGridView.setOnItemClickListener(this);
        mInputsGridView.setOnTouchListener(this);
        mInputsGridView.setOnItemLongClickListener(this);

    }

    private void setLightingShow() {
        hasLighting = this.getSharedPreferences("config", MODE_PRIVATE).getBoolean(Constants.CONFIG_KEY_LIGHT, false);

        btnLightsOn.setTypeface(mFaceHair);
        btnLightsOff.setTypeface(mFaceHair);
        btnLightsOn.setOnTouchListener(btnLightingPowerListener);
        btnLightsOff.setOnTouchListener(btnLightingPowerListener);

        if (!hasLighting) {
            btnLights.setVisibility(View.GONE);
            btnLightsOn.setVisibility(View.GONE);
            btnLightsOff.setVisibility(View.GONE);
            return;

        } else if (hasLighting) {
            btnLights.setVisibility(View.VISIBLE);
            btnLightsOn.setVisibility(View.VISIBLE);
            btnLightsOff.setVisibility(View.VISIBLE);
        }

        btnLightsOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalController.getInstance().setLightsOn();
            }
        });
        btnLightsOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalController.getInstance().setLightsOff();
            }
        });
    }

    public void getNowPlayingStatus() {
        this.mEventBus.post(new GetNowPlayingInputHandler().getRequest());
    }

    public void onEvent(GetNowPlayingInputHandler getNowPlayingInputHandler) {
        if (!mClosed) {
            return;
        }
        mClosed = false;
        Input nowPlaying = getNowPlayingInputHandler.getNowPlayingInput();
        if (nowPlaying == null) {
            return;
        }
        switch (nowPlaying.getDeviceKind()) {
            case Movie:
                MovieLibraryActivity.fireToFront(getActivity());
                break;
            case Music:
                MusicLibraryActivity.fire(getActivity());
                break;
            case Imax:
//                MediaDeviceActivity.fire(getActivity());// here call HOME PREMIERE
                startHPremiereApk();
                break;
            case Oppo:
                OppoRemoteFullActivity.fire(getActivity(), null, nowPlaying.getDisplayName(), Constants.DEVICE_ID_OPPO);
                break;
            case Kaleidescape:
                KaleidoscopeActivity.fire(getActivity(), null, nowPlaying.getDisplayName(), Constants.DEVICE_ID_KALEIDESCOPE);
                break;
            case Himedia:
                HimediaActivity.fire(getActivity(), null, nowPlaying.getDisplayName(), Constants.DEVICE_ID_HIMEDIA);
            case Mymovie:
                MymovieActivity.fire(getActivity(), null, nowPlaying.getDisplayName(), Constants.DEVICE_ID_Mymovie);
                break;
            case Bestv:
                break;
            case OnlineMovie://sony
//                TVActivity.fire(getActivity());// this will make repeat change chanel
                SonyRemoteFullActivity.fire(getActivity(), null, nowPlaying.getDisplayName(), Constants.DEVICE_ID_HIMEDIA);
                break;
            case Gdc:
                GdcActivity.fire(getActivity(), "", nowPlaying.getDisplayName(), 0);
                break;
            case Extender:
                ExternalInputActivity.fire(getActivity(), "", nowPlaying.getDisplayName(), 0);
                break;
            case Game:
                GamingActivity.fire(getActivity(), "", nowPlaying.getDisplayName(), 0);
                break;
            case Karaoke:
                KaraokeActivity.fire(getActivity(), "", nowPlaying.getDisplayName(), 0);
                break;
            case Zaxel:
                ZaxelRemoteFullActivity.fire(getActivity(), nowPlaying.getDisplayName());
                break;
            case Security:
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "CLL onResume ");
        if (!IPT.isPowerOn()) {
            Intent intentWelcome = new Intent(this, WelcomeActivity.class);
            startActivity(intentWelcome);
            super.onResume();
            finish();
            return;
        }
        initUI();
        initData();
        super.onResume();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "CLL onStop");
        dataList.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        this.mEventBus.unregister(this);
        Log.d(TAG, "CLL onDestroy ");
        super.onDestroy();
    }

    public static void fire(Context context) {
        Intent intent = new Intent(context, LobbyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private Button.OnTouchListener btnLightingPowerListener = new Button.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    switch (v.getId()) {
                        case R.id.btn_lights_on:
                            btnLightsOn.setTypeface(mFaceLight);
                            break;
                        case R.id.btn_lights_off:
                            btnLightsOff.setTypeface(mFaceLight);
                            break;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    switch (v.getId()) {
                        case R.id.btn_lights_on:
                            btnLightsOn.setTypeface(mFaceHair);
                            break;
                        case R.id.btn_lights_off:
                            btnLightsOff.setTypeface(mFaceHair);
                            break;
                    }
                    break;
                default:
                    //do nothing.
            }
            return false;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_nav_close:
                getNowPlayingStatus();
                mClosed = true;
                break;

            case R.id.btn_power_off: // aready has itself onclick
                break;
            case R.id.btn_remote:
                this.mEventBus.post(new ExecuteRemoteControlHandler("PreviousMenu").getRequest());
                MozaxRemoteControlActivity.fire(LobbyActivity.this, "guid", "", 1);
                InputController inputController = new InputController();
                int MOVIE_ID = 2;
                inputController.getEventBus().post(new SetNowPlayingInputHandler(MOVIE_ID).getRequest()); //movie -1
                inputController.onDestroy();

                break;
            case R.id.btn_maintence:
                MaintenceActivity.fire(this);
                break;
            case R.id.btn_others:
                OtherActivity.fire(this);
                break;
            case R.id.btn_prefer:
                PreferencesActivity.fire(this);
                break;
            case R.id.btn_poweroff_hp:
//                Log.d(TAG,"cll send cmd to HP to power off");
//                Intent intent = new Intent();
//                intent.setAction(IPTService.BROADCAST_ACTION_2HP_SYSTEM_POWERING_OFF);
//                intent.putExtra("IPT_SYSTEM_STATE","powering_off");
//                sendBroadcast(intent);
                break;
            case R.id.btn_lights:
                LightingActivity.fire(LobbyActivity.this);
                break;
            default:
                //do nothing

        }

    }


    private boolean pressed = false;    //in order to each touch only touch one item
    private int itemPressed = -1;       //record each touch position

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        float currentXPosition = event.getX();
        float currentYPosition = event.getY();
        int position = mInputsGridView.pointToPosition((int) currentXPosition, (int) currentYPosition);

        /**
         position no equal to -1 prevent don't touch effective area
         pressed must is false because should to prevent touch more than one item.
         */
//                IPTTextView txtName = (IPTTextView) mInputsGridView.getChildAt(itemPressed).findViewById(R.id.txt_input_name);
//                IPTTextView txtName = (IPTTextView) mInputsGridView.getChildAt(itemPressed).findViewById(R.id.txt_input_name);
        if (position != -1 && !pressed) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                pressed = true;
                itemPressed = position;
                setViewBackground(mInputsGridView.getChildAt(itemPressed), itemPressed);
                IPTTextView txtName = (IPTTextView) mInputsGridView.getChildAt(itemPressed).findViewById(R.id.txt_input_name);
                txtName.setTextColor(getResources().getColor(R.color.gold));
                txtName.setTypeface(Typeface.createFromAsset(getAssets(), Constants.FONT_LIGHT_PATH));
            }
        } else {
            if (pressed) {
                if (position != itemPressed) {
                    if (position == -1) {
                        IPTTextView txtName = (IPTTextView) mInputsGridView.getChildAt(itemPressed).findViewById(R.id.txt_input_name);
                        txtName.setTextColor(getResources().getColor(R.color.gray_light));
                        txtName.setTypeface(Typeface.createFromAsset(getAssets(), Constants.FONT_HAIRLINE_PATH));
                    }
                }

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    itemPressed = position;
                    setViewBackground(mInputsGridView.getChildAt(itemPressed), itemPressed);
                    IPTTextView txtName = (IPTTextView) mInputsGridView.getChildAt(itemPressed).findViewById(R.id.txt_input_name);
                    txtName.setTextColor(getResources().getColor(R.color.gold));
                    txtName.setTypeface(Typeface.createFromAsset(getAssets(), Constants.FONT_LIGHT_PATH));

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    IPTTextView txtName = (IPTTextView) mInputsGridView.getChildAt(itemPressed).findViewById(R.id.txt_input_name);
                    txtName.setTextColor(getResources().getColor(R.color.gray_light));
                    txtName.setTypeface(Typeface.createFromAsset(getAssets(), Constants.FONT_HAIRLINE_PATH));
                    setViewBackground(mInputsGridView.getChildAt(itemPressed), itemPressed);
                    pressed = false;
                    itemPressed = -1;

                }
            }
        }

        return false;

    }


    private void setViewBackground(View view, int position) {

        int location = position % COLUM_NUMBER;
        if (view == null) {
            return;
        }
        if (location == 0 && position < COLUM_NUMBER) { // left top
            view.setBackground(getResources().getDrawable(R.drawable.selector_inputs_griditem_topleft));

        } else if (location == 0 && position >= COLUM_NUMBER - 1) { // left btm
            view.setBackground(getResources().getDrawable(R.drawable.selector_inputs_griditem_btmleft));

        } else if (location == 3 && position <= COLUM_NUMBER - 1) { // right top
            view.setBackground(getResources().getDrawable(R.drawable.selector_inputs_griditem_topright));
        } else if (location == 3 && position > COLUM_NUMBER) { // right btm
            view.setBackground(getResources().getDrawable(R.drawable.selector_inputs_griditem_btmright));
        } else if (position < COLUM_NUMBER) {
            view.setBackground(getResources().getDrawable(R.drawable.selector_inputs_griditem_topmid));
        } else if (COLUM_NUMBER < position && position < COLUM_NUMBER * 2) {
            view.setBackground(getResources().getDrawable(R.drawable.selector_inputs_griditem_btmmid));
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        if (dataList.get(position) == null) {
            return true;
        }
        switch (((DeviceType) dataList.get(position)).getDeviceKind()) {
            case Movie:
                break;
//                case Music:
//                    break;
//            case Gdc:
//                break;
            case Imax:
                break;
            case OnlineMovie:
                break;
            case Oppo:
                break;
            case Himedia:
                break;
            case Mymovie:
                break;
            case Kaleidescape:
                break;
            case Bestv:
                break;
//                case Game:
//                    break;
//                case Karaoke:
//                    break;
//                case Extender:
//                    break;
            case Security:
                break;
//                case NotSet:
//                    break;
//                case ALL:
//                    break;
            case Zaxel:
                break;
            default:
                showDialogSetInputName(position, ((DeviceType) dataList.get(position)).getDeviceKind().toString());
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (dataList.get(position) == null) {
            return;
        }
        switch (((DeviceType) dataList.get(position)).getDeviceKind()) {
            case Security:
                break;
            case Zaxel:
                ZaxelActivity.fire(getActivity());//actually call ZaxelRemoteFullActivity
//                ZaxelRemoteFullActivity.fire(getActivity(), ((DeviceType) dataList.get(position)).getDisplayName());
//                mEventBus.post(new SetNowPlayingInputHandler(Constants.DEVICE_ID_ZAXEL).getRequest());
                break;
            case Extender:
                ExternalInputActivity.fire(getActivity(), null, ((DeviceType) dataList.get(position)).getDisplayName(), 0);
                break;
            case Karaoke:
                KaraokeActivity.fire(getActivity(), null, ((DeviceType) dataList.get(position)).getDisplayName(), 0);
                break;
            case Imax:
                mEventBus.post(new SetNowPlayingInputHandler(Constants.DEVICE_ID_IMAX).getRequest());
                startHPremiereApk();
                break;
            case OnlineMovie://sony
                TVActivity.fire(getActivity());
                break;
            case Bestv:
                mEventBus.post(new SetNowPlayingInputHandler(Constants.DEVICE_ID_BESTV).getRequest());
                break;
            case Himedia:
                mEventBus.post(new SetNowPlayingInputHandler(Constants.DEVICE_ID_HIMEDIA).getRequest());
                HimediaActivity.fire(getActivity(), null, ((DeviceType) dataList.get(position)).getDisplayName(), 0);
                break;
            case Gdc:
                mEventBus.post(new SetNowPlayingInputHandler(Constants.DEVICE_ID_GDC).getRequest());
                GdcActivity.fire(getActivity(), null, ((DeviceType) dataList.get(position)).getDisplayName(), 0);
                break;
            case Mymovie:
                mEventBus.post(new SetNowPlayingInputHandler(Constants.DEVICE_ID_Mymovie).getRequest());
                MymovieActivity.fire(getActivity(), null, ((DeviceType) dataList.get(position)).getDisplayName(), 0);
                break;
            case Kaleidescape:
                mEventBus.post(new SetNowPlayingInputHandler(Constants.DEVICE_ID_KALEIDESCOPE).getRequest());
                KaleidoscopeActivity.fire(getActivity(), null, ((DeviceType) dataList.get(position)).getDisplayName(), 0);
                break;
            case Movie:
                MovieLibraryActivity.fire(getActivity());
                mEventBus.post(new SetNowPlayingInputHandler(Constants.DEVICE_ID_MOVIE).getRequest());

                break;
            case Music://R.drawable.selector_menu_input_music_button:
                MusicLibraryActivity.fire(getActivity());
                break;
            case Game://R.drawable.selector_menu_input_gaming_button:
                GamingActivity.fire(getActivity(), "", ((DeviceType) dataList.get(position)).getDisplayName(), 0);
                break;
            case Oppo:
                mEventBus.post(new SetNowPlayingInputHandler(Constants.DEVICE_ID_OPPO).getRequest());
                OppoRemoteFullActivity.fire(getActivity(), null, ((DeviceType) dataList.get(position)).getDisplayName(), Constants.DEVICE_ID_OPPO);
                break;
            default:
                Log.d(TAG, "Hiting transparent element");
                break;

        }
    }

    private void startHPremiereApk() {

        Intent intent = new Intent("com.imax.ihp.activities.SplashActivity.ACTION_START");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("IPT_VOLUME", mVolume);
        intent.putExtra("IPT_MUTED", mMute);
        // verify there's an app to receive our intent
        PackageManager packageManager = getActivity().getPackageManager();
        List activities = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (activities.size() > 0) {
            startActivity(intent);
        } else {
            showNoAppDialog(this);
//            Toast.makeText(this, R.string.toast_hp_app_no, Toast.LENGTH_LONG).show();
        }
    }


    private void showNoChannel(Context context, String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(context.getApplicationContext());
        builder
                .setTitle(context.getString(R.string.prompt_note))
                .setMessage(msg)
                .setPositiveButton(context.getString(R.string.response_ok), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showNoAppDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context.getApplicationContext());
        builder
                .setTitle(context.getString(R.string.prompt_note))
                .setMessage(context.getString(R.string.toast_hp_app_no))
                .setPositiveButton(context.getString(R.string.response_ok), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void showDialogSetInputName(int index, String deviceKind) {
        FragmentManager fm = getSupportFragmentManager();
        mDialogSetInputNameFragment = DialogSetInputNameFragment.newInstance(index, deviceKind);
        mDialogSetInputNameFragment.show(fm, "fragment_dialog_set_input_name");
    }

    private void appBlur(View view) {
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                view.setDrawingCacheEnabled(true);
                view.buildDrawingCache();

                Bitmap bmp = view.getDrawingCache();
                blur(bmp, view);
                return true;

            }
        });

    }


    private void blur(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        float scaleFactor = 16;
        float radius = 20;

//        Bitmap overlay = Bitmap.createBitmap(
//                (int) (view.getMeasuredWidth() / scaleFactor),
//                (int) (view.getMeasuredHeight() / scaleFactor),
//                Bitmap.Config.ARGB_8888);
        Bitmap overlay = drawableToBitmap(view.getBackground(), view);
//        Bitmap overlay = drawableToBitmap(getResources().getDrawable(R.drawable.global_overlay_color), view);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop()
                / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = Blur.fastblur(getApplicationContext(), overlay, (int) radius, true);
        view.setBackground(new BitmapDrawable(getResources(), overlay));

    }

    public static Bitmap drawableToBitmap(Drawable drawable, View view) {

        // ? drawable ?????
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        w = (int) (view.getMeasuredWidth() / 16);
        h = (int) (view.getMeasuredHeight() / 16);

        // ? drawable ????????
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }


//    private Bitmap createBlurredImage (int radius) {
//        // Load a clean bitmap and work from that.
//        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.global_overlay_color_lobby3);
//
//        // Create another bitmap that will hold the results of the filter.
//        Bitmap blurredBitmap;
//        blurredBitmap = Bitmap.createBitmap(originalBitmap);
//
//        // Create the Renderscript instance that will do the work.
//        RenderScript rs = RenderScript.create(this);
//
//        // Allocate memory for Renderscript to work with
//        Allocation input = Allocation.createFromBitmap (rs, originalBitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SCRIPT);
//        Allocation output = Allocation.createTyped (rs, input.getType());
//
//        // Load up an instance of the specific script that we want to use.
//        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
//        script.setInput(input);
//
//        // Set the blur radius
//        script.setRadius(radius);
//
//        // Start the ScriptIntrinisicBlur
//        script.forEach(output);
//
//        // Copy the output to the blurred bitmap
//        output.copyTo(blurredBitmap);
//        return blurredBitmap;
//    }

}
