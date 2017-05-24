package com.imax.ipt.ui.activity.settings.maintenance;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.*;
import com.imax.ipt.R;
import com.imax.ipt.ui.util.DisplayUtil;

/**
 * Created by yanli on 2016/2/23.
 */
public class DialogLoginFragment extends DialogFragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View logginFragment = inflater.inflate(R.layout.fragment_dialog_loggin, container, false);
            Dialog d = getDialog();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());
//        lp.width = 1618;
//        lp.height = 1300;
//        lp.x = -200;
//        lp.y = -180;

//        lp.width = 810;//(380,260) -(1190,920) =(810,660)
        lp.width = DisplayUtil.dip2px(inflater.getContext(), 800);
        lp.height = DisplayUtil.dip2px(inflater.getContext(),400);
        d.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
//        d.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.color_cataogue_bg));
//        d.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.catalogue_details_card_bg2));
//        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        d.show();
        d.getWindow().setAttributes(lp);

        setStyle(android.R.style.Theme_Translucent_NoTitleBar_Fullscreen, 0);
            return logginFragment;

    }

    @Override
    public void onClick(View v) {

    }
}
