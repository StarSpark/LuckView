package com.star.spark.widget;

import android.content.Context;
import android.graphics.PaintFlagsDrawFilter;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.star.spark.R;

import java.util.ArrayList;

/**
 * 北京润泽智慧科技有限公司  版权所有
 * Copyright (c) 2017 www.runnongjinfu.com All Rights Reserved
 *
 * @author  王志星
 * @描述
 * @创建日期 2019/3/29 16:29.
 */
public class NineLuckView extends View {
    private Paint mPaint;
    private TextPaint mTextPaint;
    //存储矩形的集合
    private ArrayList<RectF> mRects;
    //矩形的描边宽度
    private float mStrokWidth = 5;
    private int[] mItemColor = {Color.GREEN,Color.YELLOW};//矩形的颜色
    private int mRectSize;//矩形的宽和高（矩形为正方形）
    private boolean mClickStartFlag = false;//是否点击中间矩形的标记
    private int mRepeatCount = 3;//转的圈数
    private int mLuckNum = 3;//最终中奖位置
    private int mPosition = -1;//抽奖块的位置
    private int mStartLuckPosition = 0;//开始抽奖的位置
    private int [] mImgs = {R.drawable.adad, R.drawable.adas,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.adda,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.cmb_bank,R.drawable.ic_launcher};
    private String[] mLuckStr = {"杨幂","杨超越","迪丽热巴迪丽热巴","孟美岐","杨紫","唐嫣","赵丽颖","林允儿","刘亦菲"};
    private OnLuckViewAnimListener onLuckPanAnimEndListener;
    private boolean canTouch=true;
    /**
     *摇奖盘的动画时间（单位：秒）
     */
    private int animationTime=3;

    public boolean isCanTouch() {
        return canTouch;
    }

    public void setCanTouch(boolean canTouch) {
        this.canTouch = canTouch;
    }

    public int getAnimationTime() {
        return animationTime;
    }

    public void setAnimationTime(int animationTime) {
        this.animationTime = animationTime;
    }

    public OnLuckViewAnimListener getOnLuckPanAnimEndListener() {
        return onLuckPanAnimEndListener;
    }

    public void setOnLuckViewAnimEndListener(OnLuckViewAnimListener onLuckPanAnimEndListener) {
        this.onLuckPanAnimEndListener = onLuckPanAnimEndListener;
    }

    public NineLuckView(Context context) {
        this(context,null);
    }

    public NineLuckView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NineLuckView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public int getmLuckNum() {
        return mLuckNum;
    }

    public void setmLuckNum(int mLuckNum) {
        this.mLuckNum = mLuckNum;
    }

    public int[] getmImgs() {
        return mImgs;
    }

    public void setmImgs(int[] mImgs) {
        this.mImgs = mImgs;
        invalidate();
    }

    public String[] getmLuckStr() {
        return mLuckStr;
    }

    public void setmLuckStr(String[] mLuckStr) {
        this.mLuckStr = mLuckStr;
        invalidate();
    }

    /**
     * 初始化数据
     */
    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mStrokWidth);

        mRects = new ArrayList<>();
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(42);//px
        mTextPaint.setColor(Color.parseColor("#363636"));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectSize = Math.min(w, h)/3;//获取矩形的宽和高
        mRects.clear();//当控件大小改变的时候清空数据
        initRect();//重新加载矩形数据
    }

    /**
     * 加载矩形数据(按照顺时针方向画各个矩形)
     */
    private void initRect() {
        //加载前三个矩形
        for(int x = 0;x<3;x++){
            float left = x * mRectSize;
            float top = 0;
            float right  = (x + 1) * mRectSize;
            float bottom = mRectSize;
            RectF rectF = new RectF(left,top,right,bottom);
            mRects.add(rectF);
        }
        //加载第四个
        mRects.add(new RectF(getWidth()-mRectSize,mRectSize,getWidth(),mRectSize * 2));
        //加载第五~七个
        for(int y= 3;y>0;y--){
            float left = getWidth() - (4-y) * mRectSize;
            float top = mRectSize * 2;
            float right  = (y - 3) * mRectSize+getWidth();
            float bottom = mRectSize * 3;
            RectF rectF = new RectF(left,top,right,bottom);
            mRects.add(rectF);
        }
        //加载第八个
        mRects.add(new RectF(0,mRectSize,mRectSize,mRectSize * 2));
        //加载第九个
        mRects.add(new RectF(mRectSize,mRectSize,mRectSize*2,mRectSize * 2));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!canTouch) {
            return true;
        }
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            performClick();
            if(mRects.get(8).contains(event.getX(),event.getY())){
                mClickStartFlag = true;
            }else {
                mClickStartFlag = false;
            }
            return true;
        }
        if(event.getAction() == MotionEvent.ACTION_UP){
            performClick();
            if(mClickStartFlag){
                if(mRects.get(8).contains(event.getX(),event.getY())){
                    startAnim();//判断只有手指落下和抬起都在中间的矩形内才开始抽奖
                }
                mClickStartFlag = false;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRects(canvas);
        drawImagesAndTexts(canvas);
    }

    /**
     * 画图片
     * @param canvas
     */
    private void drawImagesAndTexts(Canvas canvas) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        //绘制文本的高度
        float textHeight = Math.abs((fontMetrics.bottom - fontMetrics.top));
        //针对绘制bitmap添加抗锯齿
        Paint mImgPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mImgPaint.setFilterBitmap(true);
        //针对canvas加抗锯齿
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
        for (int x = 0;x<mRects.size();x++){
            RectF rectF = mRects.get(x);
            float left = rectF.centerX() - mRectSize/4;
            float top = rectF.centerY() - mRectSize/4-textHeight/2;
            Bitmap bitmap=BitmapFactory.decodeResource(getResources(),mImgs[x]);
            Bitmap tempBitmap;
            if (bitmap.getWidth()>bitmap.getHeight()) {
                tempBitmap=Bitmap.createScaledBitmap(bitmap,mRectSize/2,mRectSize/2*bitmap.getHeight()/bitmap.getWidth(),false);
                canvas.drawBitmap(tempBitmap,left,top+(mRectSize/2-tempBitmap.getHeight())/2,mImgPaint);
            }else {
                tempBitmap=Bitmap.createScaledBitmap(bitmap,mRectSize/2*bitmap.getWidth()/bitmap.getHeight(),mRectSize/2,false);
                canvas.drawBitmap(tempBitmap,left+(mRectSize/2-tempBitmap.getWidth())/2,top,mImgPaint);
            }
            //比较精确的测量文本宽度的方式
            float textWidth = mTextPaint.measureText(mLuckStr[x]);
            //处理文字长度比矩形宽时的情况
            if (textWidth>mRectSize) {
                canvas.drawText(mLuckStr[x],rectF.left,top+mRectSize/2+textHeight,mTextPaint);
            }else {
                canvas.drawText(mLuckStr[x],rectF.centerX()-textWidth/2,top+mRectSize/2+textHeight,mTextPaint);
            }
        }
    }

    /**
     * 画矩形
     * @param canvas
     */
    private void drawRects(Canvas canvas) {
        for (int x = 0;x<mRects.size();x++){
            RectF rectF = mRects.get(x);
            if(x == 8){
                mPaint.setColor(Color.WHITE);
                canvas.drawRect(rectF, mPaint);
            }else {
                mPaint.setColor(mItemColor[x%2]);
                if(mPosition == x){
                    mPaint.setColor(Color.BLUE);
                }
                canvas.drawRect(rectF, mPaint);
            }
        }
    }
    public void setPosition(int position){
        mPosition = position;
        invalidate();
    }
    /**
     * 开始动画
     */
    private void startAnim(){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(mStartLuckPosition, mRepeatCount * 8 + mLuckNum).setDuration(animationTime*1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int position = (int) animation.getAnimatedValue();
                setPosition(position%8);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                canTouch=false;
                if(onLuckPanAnimEndListener!=null){
                    onLuckPanAnimEndListener.onAnimStart();
                }
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                canTouch=true;
                mStartLuckPosition = mLuckNum;
                if(onLuckPanAnimEndListener!=null){
                    onLuckPanAnimEndListener.onAnimEnd(mPosition,mLuckStr[mPosition]);
                }
            }
        });
        valueAnimator.start();
    }
    public interface OnLuckViewAnimListener {
        void onAnimStart();
        void onAnimEnd(int position,String msg);
    }
}
