package com.imax.ipt.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.TextView;
import com.imax.ipt.R;
import com.imax.ipt.ui.util.TypeFaces;

import static com.imax.ipt.ui.util.DisplayUtil.dip2px;

public class IPTCheckableTextView extends TextView implements Checkable {
    private final static String TAG = "IPTCheckableTextView";

    private final static int titlePaddingSize = 10;

    private Context context;

    public IPTCheckableTextView(Context context) {
        super(context, null);
        this.context = context;

        initProperties();
    }

    public IPTCheckableTextView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        this.context = context;

        initProperties();
    }

    public IPTCheckableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;

        initProperties();
    }

    private void initProperties() {
//        setTextColor(context.getResources().getColor(R.color.electric_blue));
//        setTextColor(context.getResources().getColor(R.color.gray_light));
        // 38px,should be change to dp
        // setPadding(38, 38, 38, 38);
        int px = dip2px(context, titlePaddingSize);
        setPadding(px, px, px, px);
//        Typeface face = TypeFaces.get(context, "fonts/EurostileLTStd.otf");
        Typeface face = TypeFaces.get(context, "fonts/Montserrat-Hairline.otf");
        setTypeface(face);
        checkedStateChanged();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private boolean checked;

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void setChecked(boolean checked) {
        if (this.checked != checked) {
            this.checked = checked;
            checkedStateChanged();
        }
    }

    @Override
    public void toggle() {
        setChecked(!checked);
    }

    private void checkedStateChanged() {
        if (checked) {
//         setShadowLayer(40, 0, 0, Color.WHITE);
//            setTextColor(0xFFFFFFFF);
            setTextColor(getResources().getColor(R.color.gold));
            Typeface face = TypeFaces.get(context, "fonts/Montserrat-Light.otf");
            setTypeface(face);

            //edit by watershao
//         setShadowLayer(50, 0, 0, context.getResources().getColor(R.color.blue));
//            int px = dip2px(context,8);
//            int r = dip2px(context,12);
//            setShadowLayer(25, -15, -15, context.getResources().getColor(R.color.blue));
//            setShadowLayer(25, 15, 15, context.getResources().getColor(R.color.blue));
//            setShadowLayer(r, px, px, context.getResources().getColor(R.color.blue));
//            setAlpha(1.0f);
        } else {
//            setTextColor(context.getResources().getColor(R.color.electric_blue));
            setTextColor(context.getResources().getColor(R.color.gray_light));
            Typeface face = TypeFaces.get(context, "fonts/Montserrat-Hairline.otf");
            setTypeface(face);

//            setShadowLayer(10, 0, 0, 0xFF000000);
//            setShadowLayer(dip2px(context,5), 0, 0, 0xFF000000);
//            setAlpha(0.75f);
        }
    }

}
