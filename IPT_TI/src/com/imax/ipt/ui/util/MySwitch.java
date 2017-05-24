package com.imax.ipt.ui.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MySwitch extends View implements OnTouchListener {

    //开关开启时的背景，关闭时的背景，滑动按钮
    private Bitmap switch_on_Bkg, switch_off_Bkg, slip_Btn;
    private Rect on_Rect, off_Rect;

    //是否正在滑动
    private boolean isSlipping = false;
    //当前开关状态，true为开启，false为关闭
    private boolean isSwitchOn = false;

    //手指按下时的水平坐标X，当前的水平坐标X
    private float previousX, currentX;

    //开关监听器
    private OnSwitchListener onSwitchListener;
    //是否设置了开关监听器
    private boolean isSwitchListenerOn = false;
    private boolean openOrClose = true;

    public MySwitch(Context context) {
        super(context);
        init();
    }


    public MySwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        setOnTouchListener(this);
    }


    public void setImageResource(Bitmap switchOnBkg, Bitmap switchOffBkg, Bitmap slipBtn) {

        switch_on_Bkg = switchOnBkg;
        switch_off_Bkg = switchOffBkg;
        slip_Btn = slipBtn;
        //右半边Rect，即滑动按钮在右半边时表示开关开启
        //滑块在右边时应该绘制的矩形坐标
        on_Rect = new Rect(switch_off_Bkg.getWidth() - slip_Btn.getWidth(), 0, switch_off_Bkg.getWidth(), slip_Btn.getHeight());
        //左半边Rect，即滑动按钮在左半边时表示开关关闭
        //滑块在左边时应该绘制的矩形坐标
        off_Rect = new Rect(0, 0, slip_Btn.getWidth(), slip_Btn.getHeight());

    }

    public void setImageResource(int switchOnBkgResId, int switchOffBkgResId, int slipBtnResId) {

        switch_on_Bkg = BitmapFactory.decodeResource(getResources(), switchOnBkgResId);
        switch_off_Bkg = BitmapFactory.decodeResource(getResources(), switchOffBkgResId);
        slip_Btn = BitmapFactory.decodeResource(getResources(), slipBtnResId);
//		//右半边Rect，即滑动按钮在右半边时表示开关开启
		on_Rect = new Rect(switch_off_Bkg.getWidth() - slip_Btn.getWidth(), 0, switch_off_Bkg.getWidth(), slip_Btn.getHeight());
//		//左半边Rect，即滑动按钮在左半边时表示开关关闭
		off_Rect = new Rect(0, 0, slip_Btn.getWidth(), slip_Btn.getHeight());

    }

    //该方法可以直接设置开关为“开”，或“关”
    public void setSwitchState(boolean switchState) {
        //添加这个if语句之后才实现了设置状态，否则只能设置false时是正确的，设置为true时无效，图片显示失败
        if (switchState) {//如果为开，则设置当前坐标为图标宽度
            currentX = switch_off_Bkg.getWidth();
        } else {
            currentX = 0;
        }
        isSwitchOn = switchState;
        invalidate();//如果不加这句话无法设置状态
    }


    protected boolean getSwitchState() {
        return isSwitchOn;
    }


    protected void updateSwitchState(boolean switchState) {
        isSwitchOn = switchState;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        Matrix matrix = new Matrix();
        Paint paint = new Paint();
        //滑动按钮的左边坐标
        float left_SlipBtn;
        //手指滑动到左半边的时候表示开关为关闭状态，滑动到右半边的时候表示开关为开启状态
        //这里判断开关背景图标的绘制时机
        /*
		 * 这段代码是为了实现以中间为基准，滑块过中线偏左这为关，滑块过中线偏右则为开
		 * */
        if (currentX < (switch_on_Bkg.getWidth() / 2)) {
            canvas.drawBitmap(switch_off_Bkg, matrix, paint);
        } else {
            canvas.drawBitmap(switch_on_Bkg, matrix, paint);
        }
		
		/*
		 * 这段代码是为了实现滑块滑到最左或最右的时候改变开关状态
		 * */
//		if(currentX < (switch_on_Bkg.getWidth() /4)) {
//			canvas.drawBitmap(switch_off_Bkg, matrix, paint);
//			openOrClose = true;
//		} else if(currentX > (switch_on_Bkg.getWidth() *3/4)){
//			canvas.drawBitmap(switch_on_Bkg, matrix, paint);
//			openOrClose = false;
//		}else if(openOrClose){
//			canvas.drawBitmap(switch_off_Bkg, matrix, paint);
//		}else{
//			canvas.drawBitmap(switch_on_Bkg, matrix, paint);
//		}
        //判断当前是否正在滑动
        if (isSlipping) {
            if (currentX > switch_on_Bkg.getWidth()) {
                left_SlipBtn = switch_on_Bkg.getWidth() - slip_Btn.getWidth();
            } else {
                left_SlipBtn = currentX - slip_Btn.getWidth() / 2;
            }
        } else {
            //根据当前的开关状态设置滑动按钮的位置
            //以左边坐标为准
            if (isSwitchOn) {
                left_SlipBtn = on_Rect.left;
                Log.d("isSwitchOn", "--------->isSwitchOn: " + left_SlipBtn);
            } else {
                left_SlipBtn = off_Rect.left;
                Log.d("isSwitchOn", "--------->isSwitchOn: " + left_SlipBtn);
            }
        }

        //对滑动按钮的位置进行异常判断
        if (left_SlipBtn < 0) {//如果滑块滑到左边外面了
            left_SlipBtn = 0;
        } else if (left_SlipBtn > switch_on_Bkg.getWidth() - slip_Btn.getWidth()) {//如果滑块滑到右边外面了
            left_SlipBtn = switch_on_Bkg.getWidth() - slip_Btn.getWidth();
        }

        canvas.drawBitmap(slip_Btn, left_SlipBtn, 0, paint);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        setMeasuredDimension(switch_on_Bkg.getWidth(), switch_on_Bkg.getHeight());
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        switch (event.getAction()) {
            //滑动
            case MotionEvent.ACTION_MOVE:
                currentX = event.getX();
                break;

            //按下
            case MotionEvent.ACTION_DOWN:
                if (event.getX() > switch_on_Bkg.getWidth() || event.getY() > switch_on_Bkg.getHeight()) {
                    return false;
                }

                isSlipping = true;
                previousX = event.getX();
                currentX = previousX;
                break;

            //松开
            case MotionEvent.ACTION_UP:
                isSlipping = false;
                //松开前开关的状态
                boolean previousSwitchState = isSwitchOn;

                if (event.getX() >= (switch_on_Bkg.getWidth() / 2)) {//这里的判断决定了绘制滑块的时机，即超过一半时开始新的位置绘制滑块
                    isSwitchOn = true;
                    Log.d("isSwitchOn", "--------->isSwitchOn: " + isSwitchOn);
                } else {
                    isSwitchOn = false;
                    Log.d("isSwitchOn", "--------->isSwitchOn: " + isSwitchOn);
                }

                //如果设置了监听器，则调用此方法
                if (isSwitchListenerOn && (previousSwitchState != isSwitchOn)) {
                    onSwitchListener.onSwitched(isSwitchOn);
                }
                break;

            default:
                break;
        }

        //重新绘制控件
        invalidate();
        return true;
    }


    public void setOnSwitchListener(OnSwitchListener listener) {
        onSwitchListener = listener;
        isSwitchListenerOn = true;
    }


    public interface OnSwitchListener {
        abstract void onSwitched(boolean isSwitchOn);
    }
}
