package com.imax.ipt.ui.widget.semicircular;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;

import com.imax.ipt.R;

/**
 * A Layout that arranges its children around its center. The arc can be set by
 * calling {@link #setArc(float, float) setArc()}. You can override the method
 * {@link #onMeasure(int, int) onMeasure()}, otherwise it is always
 * WRAP_CONTENT.
 *
 * @author Rodrigo Lopez
 */
public class SemiCircularLayout extends ViewGroup {
    /**
     * children will be set the same size.
     */
    private int mChildSize;

    private int mChildPadding = 0;

    private int mLayoutPadding = 0;

    public static final float DEFAULT_FROM_DEGREES = 270.0f;

    public static final float DEFAULT_TO_DEGREES = 360.0f;

    private float mFromDegrees = DEFAULT_FROM_DEGREES;

    private float mToDegrees = DEFAULT_TO_DEGREES;

    private final int MIN_RADIUS = 180;

    /* the distance between the layout's center and any child's center */
    private int mRadius;

    private boolean mExpanded = false;

    private int mWidth = 0;
    private int mHeight = 0;


    public SemiCircularLayout(Context context) {
        super(context);
    }

    public SemiCircularLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SemiCircularLayout, 0, 0);
            mFromDegrees = a.getFloat(R.styleable.SemiCircularLayout_fromDegrees, DEFAULT_FROM_DEGREES);
            mToDegrees = a.getFloat(R.styleable.SemiCircularLayout_toDegrees, DEFAULT_TO_DEGREES);
            mChildSize = Math.max(a.getDimensionPixelSize(R.styleable.SemiCircularLayout_childSize, 0), 0);
            mRadius = a.getInt(R.styleable.SemiCircularLayout_initialRadius, mRadius);
            a.recycle();

            mHeight = mChildSize;
            mWidth = 4 * mChildSize;
        }
    }

    //TODO FIX THIS
    private int computeRadius(final float arcDegrees, final int childCount, final int childSize, final int childPadding, final int minRadius) {
//      if (childCount < 2)
//      {
//         return mRadius;
//      }
//
//     final float perDegrees = arcDegrees / (childCount - 1);
//      final float perHalfDegrees = perDegrees / 2;
//      final int perSize = childSize + childPadding;
//
//      final int radius = (int) ((perSize / 2) / Math.sin(Math.toRadians(perHalfDegrees)));

        return mRadius;
    }

    private Rect computeChildFrame(final int centerX, final int centerY, final int radius, final float degrees, final int width, final int height) {

        final double childCenterX = centerX + radius * Math.cos(Math.toRadians(degrees));
        final double childCenterY = centerY + radius * Math.sin(Math.toRadians(degrees));

        // the menu item is not necessarily square in dimension
        //return new Rect((int) (childCenterX - width * 5/6) + 100, (int) (childCenterY - height / 2), (int) (childCenterX + width / 6) + 100, (int) (childCenterY + height / 2));
        return new Rect((int) (childCenterX - width * 7 / 8) + 150, (int) (childCenterY - height / 2), (int) (childCenterX + width / 8) + 150, (int) (childCenterY + height / 2));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int radius = mRadius = computeRadius(Math.abs(mToDegrees - mFromDegrees), getChildCount(), mChildSize, mChildPadding, MIN_RADIUS);
        final int size = radius * 2 + mChildSize + mChildPadding + mLayoutPadding;

        // Create more room for the text label
        setMeasuredDimension(size + 300, size);

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).measure(MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int centerX = getWidth() / 2;
        final int centerY = getHeight() / 2;
        final int radius = mExpanded ? mRadius : 0;

        final int childCount = getChildCount();
        final float perDegrees = (mToDegrees - mFromDegrees) / (childCount - 1);

        float degrees = mFromDegrees;
        for (int i = 0; i < childCount; i++) {
            Rect frame = computeChildFrame(centerX, centerY, radius, degrees, mWidth, mHeight);
            degrees += perDegrees;

            final View child = getChildAt(i);
            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();
            getChildAt(i).layout(frame.left, frame.top, frame.left + width, frame.top + height);
        }
    }

    /**
     * refers to {@link LayoutAnimationController#getDelayForView(View view)}
     */
    private long computeStartOffset(final int childCount, final boolean expanded, final int index, final float delayPercent, final long duration, Interpolator interpolator) {
        final float delay = delayPercent * duration;
        final long viewDelay = (long) (getTransformedIndex(expanded, childCount, index) * delay);
        final float totalDelay = delay * childCount;

        float normalizedDelay = viewDelay / totalDelay;
        normalizedDelay = interpolator.getInterpolation(normalizedDelay);

        return (long) (normalizedDelay * totalDelay);
    }

    private int getTransformedIndex(final boolean expanded, final int count, final int index) {
        if (expanded) {
            return count - 1 - index;
        }

        return index;
    }

    private Animation createExpandAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta, long startOffset, long duration, Interpolator interpolator) {
        Animation animation = new RotateAndTranslateAnimation(0, toXDelta, 0, toYDelta, 0, 720);
        animation.setStartOffset(startOffset);
        animation.setDuration(duration);
        animation.setInterpolator(interpolator);
        animation.setFillAfter(true);

        return animation;
    }

    private Animation createShrinkAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta, long startOffset, long duration, Interpolator interpolator) {
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.setFillAfter(true);

        final long preDuration = duration / 2;
        Animation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setStartOffset(startOffset);
        rotateAnimation.setDuration(preDuration);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setFillAfter(true);

        animationSet.addAnimation(rotateAnimation);

        Animation translateAnimation = new RotateAndTranslateAnimation(0, toXDelta, 0, toYDelta, 360, 720);
        translateAnimation.setStartOffset(startOffset + preDuration);
        translateAnimation.setDuration(duration - preDuration);
        translateAnimation.setInterpolator(interpolator);
        translateAnimation.setFillAfter(true);

        animationSet.addAnimation(translateAnimation);

        return animationSet;
    }

    private void bindChildAnimation(final View child, final int index, final long duration) {
        final boolean expanded = mExpanded;
        final int centerX = getWidth() / 2;
        final int centerY = getHeight() / 2;
        final int radius = expanded ? 0 : mRadius;

        final int childCount = getChildCount();
        final float perDegrees = (mToDegrees - mFromDegrees) / (childCount - 1);
        Rect frame = computeChildFrame(centerX, centerY, radius, mFromDegrees + index * perDegrees, mWidth, mHeight);

        final int toXDelta = frame.left - child.getLeft();
        final int toYDelta = frame.top - child.getTop();

        Interpolator interpolator = mExpanded ? new AccelerateInterpolator() : new OvershootInterpolator(1.5f);
        final long startOffset = computeStartOffset(childCount, mExpanded, index, 0.1f, duration, interpolator);

        Animation animation = mExpanded ? createShrinkAnimation(0, toXDelta, 0, toYDelta, startOffset, duration, interpolator) :
                createExpandAnimation(0, toXDelta, 0, toYDelta, startOffset, duration, interpolator);

        final boolean isLast = getTransformedIndex(expanded, childCount, index) == childCount - 1;
        animation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isLast) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onAllAnimationsEnd();
                        }
                    }, 0);
                }
            }
        });
        child.setAnimation(animation);

    }

    public void showView(boolean visibility) {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            view.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
        }
    }


    public boolean isExpanded() {
        return mExpanded;
    }

    public void setArc(float fromDegrees, float toDegrees) {
        if (mFromDegrees == fromDegrees && mToDegrees == toDegrees) {
            return;
        }

        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;

        requestLayout();
    }

    public void setChildSize(int size) {
        if (mChildSize == size || size < 0) {
            return;
        }

        mChildSize = size;

        requestLayout();
    }

    /**
     * switch between expansion and shrinkage
     *
     * @param showAnimation
     */
    public void switchState(final boolean showAnimation) {

        if (showAnimation) {
            final int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                bindChildAnimation(getChildAt(i), i, 300);
            }
        }
        mExpanded = !mExpanded;

        if (!showAnimation) {
            requestLayout();
        }
        invalidate();

        this.showView(mExpanded);

    }

    private void onAllAnimationsEnd() {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).clearAnimation();
        }

        requestLayout();
    }

}
