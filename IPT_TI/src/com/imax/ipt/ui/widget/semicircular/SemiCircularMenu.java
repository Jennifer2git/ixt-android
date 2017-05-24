package com.imax.ipt.ui.widget.semicircular;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.imax.ipt.R;

/**
 * @author Rodrigo Lopez
 */
public class SemiCircularMenu extends RelativeLayout {
    private SemiCircularLayout mArcLayout;
    private int mResource = -1;
    private OnClickHitButton clickHitButton;

    public SemiCircularMenu(Context context) {
        super(context);
    }

    public SemiCircularMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(Context context) {
        if (mResource == -1) {
            throw new IllegalArgumentException("Must to add an Arc Layout");
        }

        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(mResource, this);
        mArcLayout = (SemiCircularLayout) findViewById(R.id.item_layout);

        final ViewGroup controlLayout = (ViewGroup) findViewById(R.id.control_layout);
        controlLayout.setClickable(true);

        controlLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    clickHitButton.onShow();
                }
                return false;
            }
        });
    }

    public void switchState(boolean visibility) {
        mArcLayout.switchState(visibility);
    }

    public void addItem(View item) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mArcLayout.addView(item, lp);
        mArcLayout.showView(false);
    }

//   /**
//    * 
//    * @param listener
//    * @return
//    */
//   private OnClickListener getItemClickListener(final OnClickListener listener)
//   {
//      return new OnClickListener() {
//         @Override
//         public void onClick(final View viewClicked)
//         {
//            Animation animation = bindItemAnimation(viewClicked, true, 400);
//            animation.setAnimationListener(new AnimationListener() {
//               @Override
//               public void onAnimationStart(Animation animation)
//               {
//
//               }
//
//               @Override
//               public void onAnimationRepeat(Animation animation)
//               {
//
//               }
//
//               @Override
//               public void onAnimationEnd(Animation animation)
//               {
//                  postDelayed(new Runnable() {
//
//                     @Override
//                     public void run()
//                     {
//                        itemDidDisappear();
//                     }
//                  }, 0);
//               }
//            });
//
//            final int itemCount = mArcLayout.getChildCount();
//            for (int i = 0; i < itemCount; i++)
//            {
//               View item = mArcLayout.getChildAt(i);
//               if (viewClicked != item)
//               {
//                  bindItemAnimation(item, false, 300);
//               }
//            }
//
//            mArcLayout.invalidate();
//
//            // if (listener != null)
//            // {
//            // listener.onClick(viewClicked);
//            // }
//         }
//      };
//   }

//   /**
//    * 
//    * @param child
//    * @param isClicked
//    * @param duration
//    * @return
//    */
//   private Animation bindItemAnimation(final View child, final boolean isClicked, final long duration)
//   {
//      Animation animation = createItemDisapperAnimation(duration, isClicked);
//      child.setAnimation(animation);
//      return animation;
//   }
//
//   /**
//    * 
//    */
//   private void itemDidDisappear()
//   {
//      final int itemCount = mArcLayout.getChildCount();
//      for (int i = 0; i < itemCount; i++)
//      {
//         View item = mArcLayout.getChildAt(i);
//         item.clearAnimation();
//      }
//
//      // mArcLayout.switchState(false);
//   }

    /**
     * @param duration
     * @param isClicked
     * @return
     */
//   private Animation createItemDisapperAnimation(final long duration, final boolean isClicked)
//   {
//      AnimationSet animationSet = new AnimationSet(true);
//      animationSet.addAnimation(new ScaleAnimation(1.0f, isClicked ? 2.0f : 0.0f, 1.0f, isClicked ? 2.0f : 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
//      animationSet.addAnimation(new AlphaAnimation(1.0f, 0.0f));
//
//      animationSet.setDuration(duration);
//      animationSet.setInterpolator(new DecelerateInterpolator());
//      animationSet.setFillAfter(true);
//
//      return animationSet;
//   }
    public void setLayoutResource(int mResource, OnClickHitButton clickHitButton) {
        this.mResource = mResource;
        this.clickHitButton = clickHitButton;
        this.init(getContext());
    }

    public interface OnClickHitButton {
        void onShow();
    }

}
