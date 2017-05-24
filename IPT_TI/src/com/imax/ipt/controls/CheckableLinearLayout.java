package com.imax.ipt.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class CheckableLinearLayout extends LinearLayout implements Checkable {
    public CheckableLinearLayout(Context context) {
        super(context, null);
    }

    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public CheckableLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
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
            refreshDrawableState();

            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child instanceof Checkable) {
                    ((Checkable) child).setChecked(checked);
                }
            }
        }
    }

    @Override
    public void toggle() {
        setChecked(!checked);
    }


}
