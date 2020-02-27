package com.awarmisland.customview.waveView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.awarmisland.customview.utils.DensityUtil;

/**
 * 水波纹
 */
public class WaveView extends View {
    private Paint mPaint;
    private Paint mTextPaint;
    private int mWaveDx;
    private int mWidth;
    private int mHeight;
    private int mWaveHeight;
    private int dx;
    private int dy;

    private ValueAnimator mYValueAnimator;

    public WaveView(Context context) {
        super(context);
        initView();
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
    private void initView(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.parseColor("#FF3891"));
        mPaint.setStyle(Paint.Style.FILL);
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.parseColor("#6A5ACD"));
        mTextPaint.setTextSize(DensityUtil.dip2px(getContext(),18));
        //波纹的长度
        mWaveDx = getResources().getDisplayMetrics().widthPixels;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //控件的宽高
        mWidth = MeasureUtils.measureView(widthMeasureSpec,mWaveDx);
        mHeight = MeasureUtils.measureView(heightMeasureSpec,300);
        //水波纹高度
        mWaveHeight = DensityUtil.dip2px(getContext(),16);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawWave(canvas);
        drawText(canvas);
    }
    private void drawText(Canvas canvas){
        //文字
        String text = Double.valueOf(((double)dy)/mHeight * 100).intValue()+"%";
        float textWidth = mTextPaint.measureText(text);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float y = getHeight()/2 + (Math.abs(fontMetrics.ascent) - fontMetrics.descent)/2;
        canvas.drawText(text,(getWidth()-textWidth)/2,y,mTextPaint);
    }
    private void drawWave(Canvas canvas){
//        Log.d("wave_view",""+dx);
        Path path = new Path();
        path.reset();
        path.moveTo(-mWaveDx+dx,mHeight-dy);
        if(mWaveDx>0){
            for(int i = -mWaveDx;i < getWidth()+mWaveDx;i+=mWaveDx){
                path.rQuadTo(mWaveDx/4,-mWaveHeight,mWaveDx/2,0);
                path.rQuadTo(mWaveDx/4,mWaveHeight,mWaveDx/2,0);
            }
        }

        //绘制封闭的区域
        path.lineTo(mWidth,mHeight);
        path.lineTo(0,mHeight);
        //path.close() 绘制封闭的区域
        path.close();
        if(mPaint!=null){
            canvas.drawPath(path,mPaint);
        }
    }

    public void startAnimation(){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0,mWaveDx);
        valueAnimator.setDuration(2000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dx = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();
        post(new Runnable() {
            @Override
            public void run() {
                startYAnim();
            }
        });
    }

    private void startYAnim(){
        mYValueAnimator = ValueAnimator.ofInt(0,mHeight);
        mYValueAnimator.setDuration(4000);
        mYValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mYValueAnimator.setInterpolator(new LinearInterpolator());
        mYValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dy = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        mYValueAnimator.start();
    }
}
