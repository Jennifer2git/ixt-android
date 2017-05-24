package com.imax.ipt.ui.activity.settings.maintenance;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import com.imax.ipt.R;
import com.imax.ipt.controller.settings.DemoController;
import com.imax.ipt.ui.util.MySwitch;
import com.imax.ipt.ui.util.MySwitch.OnSwitchListener;

public class MaintenceGeneralFragment extends Fragment {
    public static final String TAG = "MaintenceGeneralFragment";

    private DemoController demoController;
    private View mGeneralLayout;
    private Boolean mDemoMode;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//	   demoController = new DemoController(this);
        if (getResources().getConfiguration().locale.getCountry().equals("CN")) {

            mGeneralLayout = inflater.inflate(R.layout.fragment_maintenance_general_cn, null);
        } else {

            mGeneralLayout = inflater.inflate(R.layout.fragment_maintenance_general_en, null);
        }

        this.init();
        return mGeneralLayout;
    }

    /**
     * @param view
     */
    private void init() {

        MySwitch btnSimpleorFull1 = (MySwitch) mGeneralLayout.findViewById(R.id.btnSimpleFull1);
//        btnSimpleorFull1.setImageResource(R.drawable.knob_outline, R.drawable.knob_outline, R.drawable.knob);
        btnSimpleorFull1.setImageResource(R.drawable.system_demo__sliderbg_icn, R.drawable.system_demo__sliderbg_icn, R.drawable.system_demo__slideron_icn);

        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean mode = sp.getBoolean("default_zaxel", false);
        mDemoMode = mode;
        Button btnDemoMode = (Button) mGeneralLayout.findViewById(R.id.btnDemoMode);
//        btnDemoMode.setPressed(mDemoMode);
        if(mDemoMode){
            btnDemoMode.setText("ON");
        }else{
            btnDemoMode.setText("OFF");
        }

        btnSimpleorFull1.setSwitchState(mode);
        btnSimpleorFull1.setOnSwitchListener(new OnSwitchListener() {

            @Override
            public void onSwitched(boolean isSwitchOn) {
                // TODO Auto-generated method stub
                Editor editor = sp.edit();
                editor.putBoolean("default_zaxel", isSwitchOn);
                editor.apply();
            }
        });

        btnDemoMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDemoMode = !mDemoMode;
                if(mDemoMode){
                    btnDemoMode.setText("ON");
                }else{
                    btnDemoMode.setText("OFF");
                }
                Editor editor = sp.edit();
                editor.putBoolean("default_zaxel", mDemoMode);
                editor.apply();
            }
        });

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
//		demoController.destroy();
    }
}
