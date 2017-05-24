package com.imax.ipt.ui.activity.settings.maintenance;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.settings.maintenance.GetProjectorLampStatusHandler;
import com.imax.ipt.controller.settings.ProjectorController;

public class ProjectorFragment extends Fragment implements OnClickListener {
    private View mProjectorLayout;
    private ProjectorController mController;
    private TextView mLeftProjectorHours;
    private TextView mRightProjectorHours;
    private ImageView mLeftProjectorIV;
    private ImageView mRightProjectorIV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getResources().getConfiguration().locale.getCountry().equals("CN")) {
            mProjectorLayout = inflater.inflate(
                    R.layout.fragment_maintenance_projector_cn, null);

        } else {
            mProjectorLayout = inflater.inflate(
                    R.layout.fragment_maintenance_projector_en, null);

        }

        this.init(mProjectorLayout);
        return mProjectorLayout;
    }

    private void init(View view) {
        mController = new ProjectorController(this);
        mLeftProjectorIV = (ImageView) view.findViewById(R.id.projectorImgLeft);
        mRightProjectorIV = (ImageView) view.findViewById(R.id.projectorImgRight);
        mLeftProjectorHours = (TextView) view.findViewById(R.id.projectorTimeLeft);
        mRightProjectorHours = (TextView) view.findViewById(R.id.projectorTimeRight);

        mLeftProjectorIV.setOnClickListener(this);
        mRightProjectorIV.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mController.destroy();
    }

    public void notifyProjectorStatus(GetProjectorLampStatusHandler handler) {
        mLeftProjectorHours.setText(handler.getLeftLampHourUsed() + " " + getResources().getString(R.string.maintenance_projector_used));
        mRightProjectorHours.setText(handler.getRightLampHourUsed() + " " + getResources().getString(R.string.maintenance_projector_used));
//        mRightProjectorHours.setText(handler.getRightLampHourUsed() + "");

    }


    @Override
    public void onClick(View view) {
        if (view == mLeftProjectorIV) {
//			mController.resetProjectorLampTime(1);

        } else if (view == mRightProjectorIV) {
//			mController.resetProjectorLampTime(2);
        }
    }
}
