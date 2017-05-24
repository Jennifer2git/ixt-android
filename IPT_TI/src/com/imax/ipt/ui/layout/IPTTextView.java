package com.imax.ipt.ui.layout;

import com.imax.ipt.ui.util.TypeFaces;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class IPTTextView extends TextView {
    private Context mContext;
    private boolean mSelected;

    public IPTTextView(Context context) {
        super(context);
        this.mContext = context;
        this.init();
    }

    public IPTTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        this.mContext = context;
        this.init();
    }

    private void init() {
//        Typeface face = TypeFaces.get(mContext, "fonts/EurostileLTStd.otf");
//        Typeface face = TypeFaces.get(mContext, "fonts/Montserrat-Light.otf");
//        Typeface face = TypeFaces.get(mContext, "fonts/Montserrat-Regular.otf");
        Typeface face = TypeFaces.get(mContext, "fonts/Montserrat-Hairline.otf");
        setTypeface(face);

    }


}
