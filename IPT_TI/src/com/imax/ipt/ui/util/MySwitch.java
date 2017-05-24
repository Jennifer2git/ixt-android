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

    //���ؿ���ʱ�ı������ر�ʱ�ı�����������ť
    private Bitmap switch_on_Bkg, switch_off_Bkg, slip_Btn;
    private Rect on_Rect, off_Rect;

    //�Ƿ����ڻ���
    private boolean isSlipping = false;
    //��ǰ����״̬��trueΪ������falseΪ�ر�
    private boolean isSwitchOn = false;

    //��ָ����ʱ��ˮƽ����X����ǰ��ˮƽ����X
    private float previousX, currentX;

    //���ؼ�����
    private OnSwitchListener onSwitchListener;
    //�Ƿ������˿��ؼ�����
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
        //�Ұ��Rect����������ť���Ұ��ʱ��ʾ���ؿ���
        //�������ұ�ʱӦ�û��Ƶľ�������
        on_Rect = new Rect(switch_off_Bkg.getWidth() - slip_Btn.getWidth(), 0, switch_off_Bkg.getWidth(), slip_Btn.getHeight());
        //����Rect����������ť������ʱ��ʾ���عر�
        //���������ʱӦ�û��Ƶľ�������
        off_Rect = new Rect(0, 0, slip_Btn.getWidth(), slip_Btn.getHeight());

    }

    public void setImageResource(int switchOnBkgResId, int switchOffBkgResId, int slipBtnResId) {

        switch_on_Bkg = BitmapFactory.decodeResource(getResources(), switchOnBkgResId);
        switch_off_Bkg = BitmapFactory.decodeResource(getResources(), switchOffBkgResId);
        slip_Btn = BitmapFactory.decodeResource(getResources(), slipBtnResId);
//		//�Ұ��Rect����������ť���Ұ��ʱ��ʾ���ؿ���
		on_Rect = new Rect(switch_off_Bkg.getWidth() - slip_Btn.getWidth(), 0, switch_off_Bkg.getWidth(), slip_Btn.getHeight());
//		//����Rect����������ť������ʱ��ʾ���عر�
		off_Rect = new Rect(0, 0, slip_Btn.getWidth(), slip_Btn.getHeight());

    }

    //�÷�������ֱ�����ÿ���Ϊ���������򡰹ء�
    public void setSwitchState(boolean switchState) {
        //������if���֮���ʵ��������״̬������ֻ������falseʱ����ȷ�ģ�����Ϊtrueʱ��Ч��ͼƬ��ʾʧ��
        if (switchState) {//���Ϊ���������õ�ǰ����Ϊͼ����
            currentX = switch_off_Bkg.getWidth();
        } else {
            currentX = 0;
        }
        isSwitchOn = switchState;
        invalidate();//���������仰�޷�����״̬
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
        //������ť���������
        float left_SlipBtn;
        //��ָ���������ߵ�ʱ���ʾ����Ϊ�ر�״̬���������Ұ�ߵ�ʱ���ʾ����Ϊ����״̬
        //�����жϿ��ر���ͼ��Ļ���ʱ��
        /*
		 * ��δ�����Ϊ��ʵ�����м�Ϊ��׼�����������ƫ����Ϊ�أ����������ƫ����Ϊ��
		 * */
        if (currentX < (switch_on_Bkg.getWidth() / 2)) {
            canvas.drawBitmap(switch_off_Bkg, matrix, paint);
        } else {
            canvas.drawBitmap(switch_on_Bkg, matrix, paint);
        }
		
		/*
		 * ��δ�����Ϊ��ʵ�ֻ��黬����������ҵ�ʱ��ı俪��״̬
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
        //�жϵ�ǰ�Ƿ����ڻ���
        if (isSlipping) {
            if (currentX > switch_on_Bkg.getWidth()) {
                left_SlipBtn = switch_on_Bkg.getWidth() - slip_Btn.getWidth();
            } else {
                left_SlipBtn = currentX - slip_Btn.getWidth() / 2;
            }
        } else {
            //���ݵ�ǰ�Ŀ���״̬���û�����ť��λ��
            //���������Ϊ׼
            if (isSwitchOn) {
                left_SlipBtn = on_Rect.left;
                Log.d("isSwitchOn", "--------->isSwitchOn: " + left_SlipBtn);
            } else {
                left_SlipBtn = off_Rect.left;
                Log.d("isSwitchOn", "--------->isSwitchOn: " + left_SlipBtn);
            }
        }

        //�Ի�����ť��λ�ý����쳣�ж�
        if (left_SlipBtn < 0) {//������黬�����������
            left_SlipBtn = 0;
        } else if (left_SlipBtn > switch_on_Bkg.getWidth() - slip_Btn.getWidth()) {//������黬���ұ�������
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
            //����
            case MotionEvent.ACTION_MOVE:
                currentX = event.getX();
                break;

            //����
            case MotionEvent.ACTION_DOWN:
                if (event.getX() > switch_on_Bkg.getWidth() || event.getY() > switch_on_Bkg.getHeight()) {
                    return false;
                }

                isSlipping = true;
                previousX = event.getX();
                currentX = previousX;
                break;

            //�ɿ�
            case MotionEvent.ACTION_UP:
                isSlipping = false;
                //�ɿ�ǰ���ص�״̬
                boolean previousSwitchState = isSwitchOn;

                if (event.getX() >= (switch_on_Bkg.getWidth() / 2)) {//������жϾ����˻��ƻ����ʱ����������һ��ʱ��ʼ�µ�λ�û��ƻ���
                    isSwitchOn = true;
                    Log.d("isSwitchOn", "--------->isSwitchOn: " + isSwitchOn);
                } else {
                    isSwitchOn = false;
                    Log.d("isSwitchOn", "--------->isSwitchOn: " + isSwitchOn);
                }

                //��������˼�����������ô˷���
                if (isSwitchListenerOn && (previousSwitchState != isSwitchOn)) {
                    onSwitchListener.onSwitched(isSwitchOn);
                }
                break;

            default:
                break;
        }

        //���»��ƿؼ�
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
