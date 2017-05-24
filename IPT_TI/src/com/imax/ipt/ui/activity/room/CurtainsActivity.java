package com.imax.ipt.ui.activity.room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.rooms.GetCurtainStateHandler.CurtainState;
import com.imax.ipt.controller.room.CurtainsController;
import com.imax.ipt.ui.activity.BaseActivity;

public class CurtainsActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = "CurtainsActivity";
    private CurtainsController mCurtainsController;
    private ImageButton mBtnOpen;

    private Button mBtnCurtainOpen;
    private Button mBtnCurtainClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_curtains);
        this.addMenuFragment();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        this.init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCurtainsController.onDestroy();
    }

    private void init() {
        mCurtainsController = new CurtainsController(this);
        mBtnOpen = (ImageButton) findViewById(R.id.imgCurtain);
        mBtnOpen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurtainsController.openCurtain();
            }
        });

        mBtnCurtainOpen = (Button) findViewById(R.id.btnCurtainOpen);
        mBtnCurtainClose = (Button) findViewById(R.id.btnCurtainClose);

        CurtainState curtainState = (CurtainState) (IPT.getInstance().getIPTContext().get(IPT.STATE_CURTAINS));

        //added by watershao
//      curtainState = CurtainState.Opened;

        switch (curtainState) {
            case Opened:
                mBtnCurtainOpen.setVisibility(View.GONE);
                break;

            case Closed:
                mBtnCurtainClose.setVisibility(View.GONE);
                break;
        }

        mBtnCurtainOpen.setOnClickListener(this);
        mBtnCurtainClose.setOnClickListener(this);
    }

    /**
     * @param mContext
     */
    public static void fire(Context mContext) {
        Intent intent = new Intent(mContext, CurtainsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnCurtainOpen:
                mCurtainsController.openCurtain();
                break;

            case R.id.btnCurtainClose:
                mCurtainsController.closeCurtain();
                break;
        }
    }

    public void CurtainStateChanged(final CurtainState curtainState) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                switch (curtainState) {
                    case Opened:
                        mBtnCurtainOpen.setVisibility(View.GONE);
                        mBtnCurtainClose.setVisibility(View.VISIBLE);
                        break;

                    case Closed:
                        mBtnCurtainOpen.setVisibility(View.VISIBLE);
                        mBtnCurtainClose.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }
}
