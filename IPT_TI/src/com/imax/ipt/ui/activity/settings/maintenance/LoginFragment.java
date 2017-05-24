package com.imax.ipt.ui.activity.settings.maintenance;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.settings.maintenance.MaintenanceLoginHandler;
import com.imax.iptevent.EventBus;

public class LoginFragment extends Fragment {
    private View mLoginLayout;
    private EditText mEditTextPassword;
    private ImageView mImgViewLogin;
    private ImageButton mBtnLogin;
    private EventBus mEventBus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLoginLayout = inflater.inflate(R.layout.fragment_pref_login, null);
        this.init(mLoginLayout);
        return mLoginLayout;
    }

    private void init(View view) {
        this.mEventBus = IPT.getInstance().getEventBus(); // TODO Change this for
//        this.mImgViewLogin = (ImageView) view.findViewById(R.id.mImgViewLogin);
        this.mEditTextPassword = (EditText) view.findViewById(R.id.txtLogin);
        this.mEditTextPassword.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    mEventBus.post(new MaintenanceLoginHandler(mEditTextPassword.getText().toString()).getRequest());
                    return true;
                }
                return false;
            }
        });
        this.mBtnLogin = (ImageButton) view.findViewById(R.id.btnLogin);
        this.mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mEventBus.post(new MaintenanceLoginHandler(mEditTextPassword.getText().toString()).getRequest());
            }
        });

    }

    public void loginOn() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

//                mImgViewLogin.setImageDrawable(getResources().getDrawable(R.drawable.ipt_gui_asset_password_lock_icon_default));
                Toast.makeText(getActivity(), R.string.pwd_valid, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void loginOff() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                mImgViewLogin.setImageDrawable(getResources().getDrawable(R.drawable.dvd_donothing_icn));
                Toast.makeText(getActivity(), R.string.pwd_invalid, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
