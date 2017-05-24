package com.imax.ipt.ui.widget.verticalseekbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.AbsSeekBar;

public class VerticalSeekBar extends AbsSeekBar {

    private static final String TAG = "VerticalSeekBar" ;
    private Drawable mThumb;

    public interface OnSeekBarChangeListener {
        void onProgressChanged(VerticalSeekBar VerticalSeekBar, int progress, boolean fromUser);

        void onStartTrackingTouch(VerticalSeekBar VerticalSeekBar);

        void onStopTrackingTouch(VerticalSeekBar VerticalSeekBar);
    }

    private OnSeekBarChangeListener mOnSeekBarChangeListener;

    public VerticalSeekBar(Context context) {
        this(context, null);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.seekBarStyle);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        mOnSeekBarChangeListener = l;
    }

    void onStartTrackingTouch() {
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onStartTrackingTouch(this);
        }
    }

    void onStopTrackingTouch() {
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onStopTrackingTouch(this);
        }
    }

    void onProgressRefresh(float scale, boolean fromUser) {
        Log.d(TAG, "call onProgressRefresh");
        Drawable thumb = mThumb;
        if (thumb != null) {
            setThumbPos(getHeight(), thumb, scale, Integer.MIN_VALUE);
            invalidate();
        }
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onProgressChanged(this, getProgress(), isPressed());
        }
    }

    private void setThumbPos(int w, Drawable thumb, float scale, int gap) {
        int available = w - getPaddingLeft() - getPaddingRight();
        int thumbWidth = thumb.getIntrinsicWidth();
        int thumbHeight = thumb.getIntrinsicHeight();
        available -= thumbWidth;

        // The extra space for the thumb to move on the track
        available += getThumbOffset() * 2;

        int thumbPos = (int) (scale * available);

        int topBound, bottomBound;
        if (gap == Integer.MIN_VALUE) {
            Rect oldBounds = thumb.getBounds();
            topBound = oldBounds.top;
            bottomBound = oldBounds.bottom;
        } else {
            topBound = gap;
            bottomBound = gap + thumbHeight;
        }
        thumb.setBounds(thumbPos, topBound, thumbPos + thumbWidth, bottomBound);
    }

    @Override
    protected void onDraw(Canvas c) {
        c.rotate(-90);// ��ת90�ȣ���ˮƽSeekBar������
        c.translate(-getHeight(), 0);// ��������ת��õ���VerticalSeekBar�Ƶ���ȷ��λ��,ע�⾭��ת����ֵ����
        super.onDraw(c);
    }


    @Override
    public void setProgress(int progress) {
        super.setProgress(progress);
        onSizeChanged(getWidth(), getHeight(), 0, 0);

    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());// ���ֵ����
    }

    @Override
    public void setThumb(Drawable thumb) {
        mThumb = thumb;
        super.setThumb(thumb);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldw, oldh);// ���ֵ����
    }

    // ��Դ����ȫ��ͬ����Ϊ���ÿ��ֵ���������onStartTrackingTouch()����
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                setPressed(true);
                onStartTrackingTouch();
                trackTouchEvent(event);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                trackTouchEvent(event);
                attemptClaimDrag();
                break;
            }

            case MotionEvent.ACTION_UP: {
                trackTouchEvent(event);
                onStopTrackingTouch();
                setPressed(false);
                // ProgressBar doesn't know to repaint the thumb drawable
                // in its inactive state when the touch stops (because the
                // value has not apparently changed)
                invalidate();
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                onStopTrackingTouch();
                setPressed(false);
                invalidate(); // see above explanation
                break;
            }

            default:
                break;
        }
        return true;
    }

    // ���ֵ��������
    private void trackTouchEvent(MotionEvent event) {
        final int height = getHeight();
        final int available = height - getPaddingBottom() - getPaddingTop();
//        final int available = height - getPaddingLeft() - getPaddingRight();
        int Y = (int) event.getY();
        float scale;
        float progress = 0;
        if (Y > height - getPaddingBottom()) {
//        if (Y > height - getPaddingLeft()) {
            scale = 0.0f;
        } else if (Y < getPaddingTop()){
//        } else if (Y < getPaddingRight()) {
            scale = 1.0f;
        } else {
            scale = (float) (height - getPaddingBottom() - Y) / (float) available;
//            scale = (float) (height - getPaddingLeft() - Y) / (float) available;
        }
        final int max = getMax();
        progress = scale * max;
        setProgress((int) progress);
        Log.d(TAG, "the height:" + height + ",Y=" +Y +",getPaddingRight:" +getPaddingRight()
                +", getPaddingLeft:" +getPaddingLeft() + ",scale:" +scale + ",progress:" + progress);
    }

    private void attemptClaimDrag() {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
    }
}