package com.peng.saishi.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.facebook.drawee.drawable.DrawableUtils;

public class ImageLoadingDrawable extends Drawable{
	
    private Paint mRingBackgroundPaint;
    private int mRingBackgroundColor;
    // 画圆环的画笔
    private Paint mRingPaint;
    // 圆环颜色
    private int mRingColor;
    // 圆环宽度
    private float mStrokeWidth;
    // 当前进度
    private int mProgress;
    
    private Paint text_paintPaint;

    public ImageLoadingDrawable(){
        initAttrs();
    }
    
    private void initAttrs() {
    	
        mStrokeWidth = 4;
        mRingBackgroundColor = 0xFFadadad;
        mRingColor = 0xFF0EB6D2;
        initVariable();
        text_paintPaint = new Paint();
        text_paintPaint.setColor(Color.WHITE);
        text_paintPaint.setTextSize(20);
        text_paintPaint.setTextAlign(Align.CENTER);
    	
    }

    private void initVariable() {
        mRingBackgroundPaint = new Paint();
        mRingBackgroundPaint.setAntiAlias(true);
        mRingBackgroundPaint.setColor(mRingBackgroundColor);
        mRingBackgroundPaint.setStyle(Paint.Style.STROKE);
        mRingBackgroundPaint.setStrokeWidth(mStrokeWidth);

        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setColor(mRingColor);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(mStrokeWidth);
        
    }
    
    
    

    @Override
    public void draw(Canvas canvas) {

        drawText(canvas,mProgress);
    }

    private void drawText(Canvas canvas, int mProgress2) {
		// TODO Auto-generated method stub
    	Rect bound= getBounds();
    	int x=bound.centerX();
    	int y = bound.centerY();
    	canvas.drawText(mProgress2/100+"%", x, y, text_paintPaint);
	}


    @Override
    protected boolean onLevelChange(int level) {
        mProgress = level;
        if(level > 0 && level < 10000) {
            invalidateSelf();
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void setAlpha(int alpha) {
        mRingPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mRingPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return DrawableUtils.getOpacityFromColor(this.mRingPaint.getColor());
    }
}