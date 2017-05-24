package com.imax.ipt.ui.activity.settings.preferences;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.imax.ipt.R;
import com.imax.ipt.apk.update.util.RemoteUpdateUtil;
import com.imax.ipt.controller.eventbus.handler.settings.maintenance.GetVersionHandler;
import com.imax.ipt.controller.settings.OtherController;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.util.VibrateUtil;

public class VersionFragment extends Fragment implements OnClickListener {
    public static final String TAG = "VersionFragment";

    private View mGeneralLayout;
    private IPTTextView txtNumber;

    private IPTTextView pcTextView;
    private IPTTextView amxTextView;
    private IPTTextView androidTextView;
    private IPTTextView snTextView;
    private IPTTextView hardwareTextView;
    private String mAppVersion = "";

    private ImageButton btnCheckSoftwareUpdate;

    private OtherController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getResources().getConfiguration().locale.getCountry().equals("CN")) {

            mGeneralLayout = inflater.inflate(R.layout.fragment_other_version_cn, null);
        } else {

            mGeneralLayout = inflater.inflate(R.layout.fragment_other_version_en, null);
        }
        this.init(mGeneralLayout);
        return mGeneralLayout;
    }

    /**
     * @param view
     */
    private void init(View view) {

        pcTextView = (IPTTextView) mGeneralLayout.findViewById(R.id.versionPc);
        amxTextView = (IPTTextView) mGeneralLayout.findViewById(R.id.versionAmx);
        androidTextView = (IPTTextView) mGeneralLayout.findViewById(R.id.versionAndroid);
        snTextView = (IPTTextView) mGeneralLayout.findViewById(R.id.serialNum);
        hardwareTextView = (IPTTextView) mGeneralLayout.findViewById(R.id.versionHardware);

        btnCheckSoftwareUpdate = (ImageButton) mGeneralLayout.findViewById(R.id.btnCheckSoftwareUpdate);
        btnCheckSoftwareUpdate.setOnClickListener(this);

        PackageManager manager = getActivity().getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(getActivity().getPackageName(), 0);
//		String version = info.versionCode+","+info.versionName;   
            mAppVersion = info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "Error: no app found on pad");
            return;
        }
        controller.getVersion();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnCheckSoftwareUpdate:
                RemoteUpdateUtil remoteUpdateUtil = new RemoteUpdateUtil();
                remoteUpdateUtil.checkRemoteUpdate(getActivity(), true);
                VibrateUtil.vibrate(getActivity(), 100);
                break;

        }

    }

    public void setVersion(GetVersionHandler handler) {
        pcTextView.setText(handler.getPCVersion());
        amxTextView.setText(handler.getAMXVersion());
        snTextView.setText(handler.getSNVersion());
        hardwareTextView.setText(handler.getHardwareVersion());
        androidTextView.setText(mAppVersion);
//	   androidTextView.setText(handler.getAndroidVersion());
    }


    public void setOtherController(OtherController controller) {
        this.controller = controller;
    }


}
