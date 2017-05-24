package com.imax.ipt.ui.activity.media;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.imax.ipt.R;
import com.imax.ipt.common.Constants;
import com.imax.ipt.controller.eventbus.handler.media.GetZaxelHandler;
import com.imax.ipt.ui.activity.input.Dialog3DFragment;
import com.imax.ipt.ui.activity.input.movie.MovieLibraryActivity;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.util.MySwitch;
import com.imax.ipt.ui.util.MySwitch.OnSwitchListener;

import java.util.ArrayList;
import java.util.List;

public class ZaxelRemoteFullActivity extends MediaRemote implements OnClickListener {

    private static final String TAG = ZaxelRemoteFullActivity.class.getSimpleName();
    private IPTTextView mTxtTitle;
    private ListView mListView;

    private ZaxelAdapter zaxelAdapter;
    private static boolean isPlayList = false;

    List<String> playList = new ArrayList<String>();
    List<String> clipList = new ArrayList<String>();
    List<String> tmpPlayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zaxel_remote_full);
        this.addMenuFragment();
        this.init();
    }

    private void init() {
        Log.d(TAG,"cll " + TAG);

        this.mTitle = getIntent().getStringExtra(MEDIA_TITLE);
//        this.guid = getIntent().getStringExtra(MEDIA_ID);
        this.mTxtTitle = (IPTTextView) findViewById(R.id.txtTitle);
        this.mTxtTitle.setTypeface(Typeface.createFromAsset(getAssets(), Constants.FONT_LIGHT_PATH));
        this.mTxtTitle.setText(getResources().getString(R.string.now_playing) + mTitle.toUpperCase());

//        this.mSeekBarVolumen = (VerticalSeekBar) findViewById(R.id.seekBarVolume);
//        this.mSeekBarVolumen = (SeekBar) findViewById(R.id.seekBarVolume);
//        this.mSeekBarVolumen.setOnSeekBarChangeListener(this);
//        this.mSeekBarVolumen.setMax(Constants.MAX_VOLUME_VALUE);
//        this.sendRequestGetVolume();

//        this.mBtnMute = (ImageButton) findViewById(R.id.btnVolMute);
//        this.mBtnMute.setOnClickListener(this);
//        this.sendRequestGetMuteVolume();
//        volupButton = (ImageButton) findViewById(R.id.volumeUp);
//        voldownButton = (ImageButton) findViewById(R.id.volumeDown);
//        volupButton.setOnClickListener(this);
//        voldownButton.setOnClickListener(this);

        this.mBtnStop = (ImageButton) findViewById(R.id.btnStop);
        this.mBtnStop.setVisibility(View.VISIBLE);
        this.mBtnStop.setOnClickListener(onClickListener);

        this.mBtnPlay = (ImageButton) findViewById(R.id.btnPlay);
        this.mBtnPlay.setVisibility(View.VISIBLE);
        this.mBtnPlay.setOnClickListener(onClickListener);

        this.mBtnPause = (ImageButton) findViewById(R.id.btnPause);
        this.mBtnPause.setVisibility(View.VISIBLE);
        this.mBtnPause.setOnClickListener(onClickListener);


        findViewById(R.id.btnBack).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MovieLibraryActivity.fire(ZaxelRemoteFullActivity.this);
                finish();
            }
        });


        Button zoomButton = (Button) findViewById(R.id.btnZoom);
        zoomButton.setOnClickListener(onClickListener);


        ImageButton addButton = (ImageButton) findViewById(R.id.btnZaxelAdd);
        ImageButton repeatButton = (ImageButton) findViewById(R.id.btnZaxelRepeat);
        addButton.setOnClickListener(onClickListener);
        repeatButton.setOnClickListener(onClickListener);

        mListView = (ListView) findViewById(R.id.play_list);
        zaxelAdapter = new ZaxelAdapter(this);
        //begin jennifer add switch btn
        MySwitch btnCliporPlay = (MySwitch) findViewById(R.id.btnCliporPlay);
        btnCliporPlay.setImageResource(R.drawable.knob_outline3, R.drawable.knob_outline3, R.drawable.knob3);
        isPlayList = true;
        zaxelAdapter.setData(clipList);
        btnCliporPlay.setSwitchState(isPlayList);
        btnCliporPlay.setOnSwitchListener(new OnSwitchListener() {

            @Override
            public void onSwitched(boolean isSwitchOn) {
                isPlayList = isSwitchOn;
                if (isPlayList) {
                    getZaxelPlayList();
                } else {
                    getZaxelClipList();
                }

            }
        });
        //end jennifer add switch btn
        zaxelAdapter.setData(playList);
        mListView.setAdapter(zaxelAdapter);
        mListView.setOnItemClickListener(onItemClickListener);
        getZaxelPlayList();
    }

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            mEventBus.post(new GetZaxelHandler(Constants.DEVICE_ID_ZAXEL, 1, playList.get(position)).getRequest());
        }

    };

    private OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int cmd = 0;
            int paramCount = 0;
            String param = "";
            switch (v.getId()) {
                case R.id.btnStop:
                    cmd = 9;
                    break;
                case R.id.btnPlay:
                    cmd = 11;
                    break;
                case R.id.btnPause:
                    cmd = 10;
                    break;
                case R.id.btnZaxelAdd:
                    cmd = 3;
                    paramCount = 1;
                    param = "00";
//			showDir();
                    break;
                case R.id.btnZaxelRepeat:
                    cmd = 19;
                    paramCount = 1;
                    param = "0";
                    break;
                case R.id.btnZoom:

                    showDialog3D();
                    break;

                default:
                    break;

            }
            mEventBus.post(new GetZaxelHandler(cmd, paramCount, param).getRequest());

        }
    };

    //todo update the dialog

    private void showDialog3D() {
        FragmentManager fm = getSupportFragmentManager();
        Dialog3DFragment mDialog3DFragment = Dialog3DFragment.newInstance(inputId);
        mDialog3DFragment.show(fm, "fragment_dialog_3d");
    }

    private List<String> initData() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {

            list.add("movie" + i);
        }

        return list;
    }

    private void getZaxelPlayList() {
        this.mEventBus.post(new GetZaxelHandler(22, 0, "").getRequest());
    }

    private void getZaxelClipList() {
        this.mEventBus.post(new GetZaxelHandler(3, 0, "00").getRequest());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * @param context
     */
    public static void fire(Context context, String title) {
        Intent intent = new Intent(context, ZaxelRemoteFullActivity.class);
//        intent.putExtra(MediaRemote.MEDIA_ID, guid);
        intent.putExtra(MediaRemote.MEDIA_TITLE, title);
//        intent.putExtra(INPUT_ID, inputId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * @param getZaxelHandler
     */
    public void onEventMainThread(GetZaxelHandler getZaxelHandler) {
        int response = getZaxelHandler.getResponse();
        String content = getZaxelHandler.getContent();
        //playlist
        if (response == 22) {
            playList.clear();
            String[] clips = content.split(",");
            for (int i = 0; i < clips.length; i++) {
                playList.add(clips[i]);
            }
            zaxelAdapter.setData(playList);
        }

        //cliplist
        if (response == 3) {
            playList.clear();
            String[] clips = content.split(",");
            for (int i = 0; i < clips.length; i++) {
                playList.add(clips[i]);
            }
            zaxelAdapter.setData(playList);
//			mListView.setAdapter(zaxelAdapter);
        }
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
//            build(mMusicAlbum);
            }
        });
    }

    private void showDir() {
        Builder builder = new Builder(getApplicationContext());
        clipList = initData();
        final String[] clips = new String[clipList.size()];
        int i = 0;
        for (String name : clipList) {
            clips[i] = name;
            i++;
        }
        boolean[] bools = new boolean[clips.length];
        tmpPlayList.clear();
        builder.setMultiChoiceItems(clips, bools, new DialogInterface.OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                if (isChecked) {
                    tmpPlayList.add(clips[which]);
                } else {
                    if (tmpPlayList.contains(clips[which])) {
                        tmpPlayList.remove(clips[which]);
                    }
                }
            }
        });

//		builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.setTitle(R.string.zaxel_prompt);

//		dialog.setMessage(getResources().getString(R.string.prompt_ac_system_problem));
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getText(R.string.zaxel_ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (tmpPlayList != null && tmpPlayList.size() > 0) {
                    String param = "";
                    for (String name : tmpPlayList) {
                        name = name + ",";
                        param = name;
                    }
                    mEventBus.post(new GetZaxelHandler(23, 1, param).getRequest());
                    zaxelAdapter.setData(tmpPlayList);
                }

                dialog.cancel();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getText(R.string.zaxel_cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


}

class ZaxelAdapter extends BaseAdapter {

    private static final String TAG = ZaxelAdapter.class.getSimpleName();

    private List<String> zaxelList;

    private Context ctx;
    private String id;

    public ZaxelAdapter(Context context) {
        ctx = context;
        zaxelList = new ArrayList<String>();
    }

    public int getCount() {
        return zaxelList.size();
    }

    public Object getItem(int position) {
        return zaxelList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * @param mData
     */
    public void setData(List<String> mData) {
        zaxelList = mData;
        notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final String name = zaxelList.get(position);

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(
                    R.layout.zaxel_item, null);
            viewHolder = new ViewHolder();
            viewHolder.nameTextView = (TextView) convertView
                    .findViewById(R.id.zaxel_movie_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.nameTextView.setText(name);

        return convertView;
    }

    static class ViewHolder {
        public TextView nameTextView;
    }

}
