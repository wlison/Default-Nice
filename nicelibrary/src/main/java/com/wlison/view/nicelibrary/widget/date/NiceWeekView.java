package com.wlison.view.nicelibrary.widget.date;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.wlison.view.nicelibrary.R;

/**
 * Author: Simon
 * Date：5/31/2019 17:07
 * Desc:
 */
public class NiceWeekView extends View {
    private String[] mWeeks = {"日", "一", "二", "三", "四", "五", "六"};

    private Context mContext;
    private int mTextSize;
    private int mTextColor;
    private Paint mPaint;
    private float mMeasureTextWidth;
    private Typeface mTypeface;


    public NiceWeekView(Context context) {
        this(context,null);
        this.mContext = context;
    }

    public NiceWeekView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NiceWeekView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NiceWeekView);
        int textColor = typedArray.getColor(R.styleable.NiceWeekView_nice_weekTextColor, ContextCompat.getColor(context,R.color.black80));
        int textSize = typedArray.getDimensionPixelSize(R.styleable.NiceWeekView_nice_weekTextSize,-1);
        setTextColor(textColor);
        setTextSize(textSize);

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if(widthMode == MeasureSpec.AT_MOST){
            widthSize = (int) (mMeasureTextWidth * mWeeks.length) + getPaddingLeft() + getPaddingRight();
        }
        if(heightMode == MeasureSpec.AT_MOST){
            heightSize = (int) mMeasureTextWidth + getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTextSize != -1){
            mPaint.setTextSize(mTextSize);
        }
        if (mTypeface != null){
            mPaint.setTypeface(mTypeface);
        }
        // 设置填充样式
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mTextColor);
        int columnWidth = (getWidth() - getPaddingLeft() - getPaddingRight()) / 7;
        for (int i=0;i<mWeeks.length;i++){
            String text = mWeeks[i];
            int fontWidth = (int) mPaint.measureText(text);
            int startX = columnWidth * i + (columnWidth - fontWidth) / 2 + getPaddingLeft();

            /*
            baseLine：一行文字的底线。
            Ascent： 字符顶部到baseLine的距离。
            Descent： 字符底部到baseLine的距离。
            Leading： 字符行间距。
            基线（baeseline），坡顶（ascenter）,坡底（descenter）上坡度（ascent），下坡度（descent）行间距（leading）：
            坡底到下一行坡顶的距离字体的高度＝上坡度＋下坡度＋行间距ascent是指从一个字的基线(baseline)到最顶部的距离，
            descent是指一个字的基线到最底部的距离注意, ascent和top都是负数
            */
            int startY = (int) ((getHeight()) / 2 - (mPaint.ascent() + mPaint.descent()) / 2) + getPaddingTop();
            canvas.drawText(text, startX, startY, mPaint);
        }

    }

    /**
     * 设置文字颜色.
     *
     * @param color 颜色.
     */
    public void setTextColor(@ColorInt int color){
        this.mTextColor = color;
        mPaint.setColor(mTextColor);
    }

    /**
     * 设置字体大小.
     *
     * @param size text size.
     */
    public void setTextSize(int size){
        this.mTextSize = size;
        mPaint.setTextSize(mTextSize);
        mMeasureTextWidth = mPaint.measureText(mWeeks[0]);
    }

    /**
     * 设置字体.
     *
     * @param typeface {@link Typeface}.
     */
    public void setTypeface(Typeface typeface){
        this.mTypeface = typeface;
        invalidate();
    }

}
