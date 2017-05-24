package com.imax.ipt.ui.layout;

import android.content.Context;
import android.graphics.*;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import com.imax.ipt.R;
import com.imax.ipt.ui.util.DisplayUtil;

public class MenuShapeView extends Drawable {
    private static final String TAG = MenuShapeView.class.getSimpleName();
    private Context mContext;

    private int POINT_START_X = 300;
    private int POINT_START_Y = 0;
    private int POINT_END_X = 300;
    private int POINT_END_Y = 1600;
    private int POINT_CONTROL_X = -100;
    private int POINT_CONTROL_Y = 800;
    private int REC_POINT_END_X = 1200;

    public MenuShapeView(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void draw(Canvas canvas) {

        PointF mPointStart = new PointF(POINT_START_X, POINT_START_Y);
        PointF mPointControl = new PointF(POINT_CONTROL_X, POINT_CONTROL_Y);
        PointF mPointEnd = new PointF(POINT_END_X, POINT_END_Y);

        Path pathCurveLine = new Path();
        Paint paintCurveLine = new Paint();
        paintCurveLine.setAntiAlias(true);
        paintCurveLine.setStyle(Style.STROKE);
        paintCurveLine.setStrokeWidth(3);
        paintCurveLine.setColor(mContext.getResources().getColor(R.color.glass_line));

        Path pathCurve = new Path();
        Paint paintCurve = new Paint();
        paintCurve.setAntiAlias(true);
        paintCurve.setStyle(Style.FILL);
        paintCurve.setShader(new LinearGradient(mPointStart.x, mPointStart.y, mPointEnd.x, mPointEnd.y,
                mContext.getResources().getColor(R.color.glass_gradient_start_color), mContext.getResources().getColor(R.color.glass_gradient_end_color), Shader.TileMode.CLAMP));

        Paint paintRect = new Paint();
        paintRect.setAntiAlias(true);
        paintRect.setStyle(Style.FILL);
        paintRect.setShader(new LinearGradient(mPointStart.x, mPointStart.y, mPointEnd.x, mPointEnd.y,
                mContext.getResources().getColor(R.color.glass_gradient_start_color), mContext.getResources().getColor(R.color.glass_gradient_end_color), Shader.TileMode.CLAMP));

        pathCurveLine = drawCurve(mPointStart, mPointControl, mPointEnd);
        pathCurve = drawCurve(mPointStart, mPointControl, mPointEnd);

        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        float widthRatio = (float)dm.widthPixels / DisplayUtil.BASE_WIDTH;
        float heightRatio = (float)dm.heightPixels / DisplayUtil.BASE_HEIGHT;
        canvas.scale(widthRatio, heightRatio, widthRatio*(mPointStart.x), heightRatio*mPointStart.y );

        canvas.drawPath(pathCurveLine, paintCurveLine);
        canvas.drawPath(pathCurve, paintCurve);
        canvas.drawRect(mPointStart.x, mPointStart.y, REC_POINT_END_X, mPointEnd.y, paintRect);

    }

    private Path drawCurve(PointF mPointStart, PointF mPointControl, PointF mPointEnd) {
        Path myPath = new Path();
        myPath.moveTo(mPointStart.x, mPointStart.y);
        myPath.quadTo(mPointControl.x, mPointControl.y, mPointEnd.x, mPointEnd.y);
        return myPath;
    }

    @Override
    public int getOpacity() {
        return 0;
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }


}
