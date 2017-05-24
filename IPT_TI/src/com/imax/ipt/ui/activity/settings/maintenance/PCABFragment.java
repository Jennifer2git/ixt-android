package com.imax.ipt.ui.activity.settings.maintenance;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.settings.maintenance.GetPcabCalibrationStatusHandler;
import com.imax.ipt.controller.settings.PcabController;
import com.imax.ipt.ui.layout.IPTTextView;

public class PCABFragment extends Fragment implements
        OnClickListener {
    private View mPcabLayout;
    private RelativeLayout pcabStatusLayout;
    private RelativeLayout pcabCalibratingLayout;
    private PcabController mController;
    private ImageView calibrationImg;
    private TextView lastTextView;
    private TextView nextTextView;
    private TextView mailTextView;

    private IPTTextView mIPTTextViewCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getResources().getConfiguration().locale.getCountry().equals("CN")) {
            mPcabLayout = inflater.inflate(
                    R.layout.fragment_maintenance_pcab_cn, null);

        } else {

            mPcabLayout = inflater.inflate(
                    R.layout.fragment_maintenance_pcab_en, null);
        }
        this.init(mPcabLayout);
        return mPcabLayout;
    }

    private void init(View view) {
        mController = new PcabController(this);
        pcabStatusLayout = (RelativeLayout) mPcabLayout.findViewById(R.id.pcab_status);
        pcabCalibratingLayout = (RelativeLayout) mPcabLayout.findViewById(R.id.pcab_calibration);

        calibrationImg = (ImageView) mPcabLayout.findViewById(R.id.calibrationImg);
        lastTextView = (TextView) mPcabLayout.findViewById(R.id.calibrationLastTime);
        nextTextView = (TextView) mPcabLayout.findViewById(R.id.calibrationNextTime);
        mailTextView = (TextView) mPcabLayout.findViewById(R.id.calibrationMail);

        calibrationImg.setOnClickListener(this);
        mailTextView.setOnClickListener(this);

        mIPTTextViewCount = (IPTTextView) mPcabLayout.findViewById(R.id.txtCountTime);
        mIPTTextViewCount.setText(0 + "%");
        mIPTTextViewCount.setOnClickListener(this);
    }

    public void updateProgress(final int progress) {
        if (progress == 100) {
            mIPTTextViewCount.setText(progress + "%");
        } else {

            mIPTTextViewCount.setText(progress + "%");
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mController.destroy();
    }

    public void notifyPcabStatus(GetPcabCalibrationStatusHandler handler) {

        if (handler.getPcabStatus() == 0) {
            lastTextView.setText(handler.getLastTime());
            nextTextView.setText(handler.getNextTime());
            pcabStatusLayout.setVisibility(View.VISIBLE);
            pcabCalibratingLayout.setVisibility(View.GONE);
        } else if (handler.getPcabStatus() == 1) {
            lastTextView.setText(handler.getLastTime());
            nextTextView.setText(handler.getNextTime());
            pcabStatusLayout.setVisibility(View.GONE);
            pcabCalibratingLayout.setVisibility(View.VISIBLE);
        }
    }

    public void switchPcabCalibration(boolean calibration) {
        if (calibration) {
            pcabStatusLayout.setVisibility(View.GONE);
            pcabCalibratingLayout.setVisibility(View.VISIBLE);
        } else {
            pcabStatusLayout.setVisibility(View.VISIBLE);
            pcabCalibratingLayout.setVisibility(View.GONE);

        }
    }

    public void calibrationDone() {
        Toast.makeText(getActivity(), R.string.calibration_done, Toast.LENGTH_LONG).show();
        mController.getPcabState();
    }

    @Override
    public void onClick(View view) {
        if (view == calibrationImg) {
            mController.switchPcabCalibration(true);
            switchPcabCalibration(true);
        } else if (view == mailTextView) {
//			mController.switchProjector(Constants.PROJECTOR_POSITION_RIGHT,
//					mRightProjectorButton.isChecked());
        } else if (view == mIPTTextViewCount) {
            if (mIPTTextViewCount.getText().toString().equalsIgnoreCase("100%")) {
//				mController.getPcabState();
//				switchPcabCalibration(false);
            }
        }
    }
}
