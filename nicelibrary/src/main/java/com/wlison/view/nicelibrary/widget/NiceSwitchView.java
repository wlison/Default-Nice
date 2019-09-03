package com.wlison.view.nicelibrary.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.wlison.view.nicelibrary.R;

/**
 * Author: Simon
 * Date：5/14/2019 13:47
 * Desc:
 */
public class NiceSwitchView extends View {
    // 共同的动画时间
    private static final long commonDuration = 300L;

    private ValueAnimator innerContentAnimator;
    private ValueAnimator knobExpandAnimator;
    private ValueAnimator knobMoveAnimator;
    private GestureDetector gestureDetector;

    private static final int intrinsicWidth = 0;
    private static final int intrinsicHeight = 0;

    private int width;
    private int height;
    private int centerX;
    private int centerY;
    private int shadowSpace;
    private int outerStrokeWidth;
    private int mSwitchColor;
    private int mTempColor;

    private float cornerRadius;
    private float knobMaxExpandWidth;
    private float intrinsicKnobWidth;
    private float knobExpandRate;
    private float knobMoveRate;
    private float innerContentRate = 1.0F;
    private float intrinsicInnerWidth;
    private float intrinsicInnerHeight;

    private Paint paint;
    private Drawable mDrawableShadow;
    private RectF knobBound;
    private RectF innerContentBound;
    private RectF ovalForPath;
    private Path roundRectPath;
    private RectF tempForRoundRect;

    private boolean knobState;
    private boolean isOn;
    private boolean preIsOn;
    private boolean dirtyAnimation = false;
    private boolean isAttachedToWindow = false;

    private static final int backgroundColor = 0xFFCCCCCC;
//    private static final int backgroundColor = 0xFFFF0000;
    private static final int foregroundColor = 0xFFF5F5F5;
//    private static final int foregroundColor = 0xFF888888;
    private int colorStep = backgroundColor;
//    private int colorStep = 0xFFFFFF00;

    private Context mContext;
    private OnNiceSwitchClickListener mOnNiceSwitchClickListener;


    public interface OnNiceSwitchClickListener{
        void onSwitchClick(boolean isOpen);
    }

    public void setOnNiceSwitchClickListener(OnNiceSwitchClickListener onSwitchStateChangeListener){
        this.mOnNiceSwitchClickListener = onSwitchStateChangeListener;
    }

    public OnNiceSwitchClickListener getOnNiceSwitchClickListener(){
        return this.mOnNiceSwitchClickListener;
    }

    public NiceSwitchView(Context context) {
        this(context,null);
        this.mContext = context;
    }

    public NiceSwitchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
        this.mContext = context;
    }

    public NiceSwitchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.NiceSwitchView);
        // 设置颜色
        mSwitchColor = typedArray.getColor(R.styleable.NiceSwitchView_nice_switchColor, ContextCompat.getColor(context,R.color.primary_dark));
        mTempColor = mSwitchColor;

        /*
        dp->px:  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics());
        in->px: TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_IN, 20, context.getResources().getDisplayMetrics());
        mm->px: TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 20, context.getResources().getDisplayMetrics());
        pt->px: TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, 20, context.getResources().getDisplayMetrics());
        sp->px: TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, context.getResources().getDisplayMetrics());
        */
        // 把非标准尺寸转换成标准尺寸
        int defaultOuterStrokeWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.5F, context.getResources().getDisplayMetrics());
        int defaultShadowSpace = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics());

        outerStrokeWidth = typedArray.getDimensionPixelOffset(R.styleable.NiceSwitchView_nice_outerStrokeWidth, defaultOuterStrokeWidth);
        // 按钮阴影长
        shadowSpace = typedArray.getDimensionPixelOffset(R.styleable.NiceSwitchView_nice_shadowSpace, defaultShadowSpace);
        // 释放资源
        typedArray.recycle();

        knobBound = new RectF();
        innerContentBound = new RectF();
        ovalForPath = new RectF();
        tempForRoundRect = new RectF();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        roundRectPath = new Path();

        gestureDetector = new GestureDetector(context, gestureListener);
        gestureDetector.setIsLongpressEnabled(false);

//        if(Build.VERSION.SDK_INT >= 11){
//            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }
//        mDrawableShadow = context.getResources().getDrawable(R.drawable.nice_shadow);

        initAnimators();
    }

    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener(){

        // Touch down时触发
        @Override
        public boolean onDown(MotionEvent event){
            if(!isEnabled()) return false;
            preIsOn = isOn;
            innerContentAnimator.setFloatValues(innerContentRate, 0.0F);
            innerContentAnimator.start();
            knobExpandAnimator.setFloatValues(knobExpandRate, 1.0F);
            knobExpandAnimator.start();
            return true;
        }

        // 在Touch down之后一定时间（115ms）触发
        @Override
        public void onShowPress(MotionEvent event){ }

        @Override
        public boolean onSingleTapUp(MotionEvent event){
            isOn = knobState;
            if(preIsOn == isOn){
                isOn = !isOn;
                knobState = !knobState;
            }
            if(knobState){
                knobMoveAnimator.setFloatValues(knobMoveRate, 1.0F);
                knobMoveAnimator.start();
                innerContentAnimator.setFloatValues(innerContentRate, 0.0F);
                innerContentAnimator.start();
            }else{
                knobMoveAnimator.setFloatValues(knobMoveRate, 0.0F);
                knobMoveAnimator.start();
                innerContentAnimator.setFloatValues(innerContentRate, 1.0F);
                innerContentAnimator.start();
            }
            knobExpandAnimator.setFloatValues(knobExpandRate, 0.0F);
            knobExpandAnimator.start();
            if(NiceSwitchView.this.mOnNiceSwitchClickListener != null && isOn != preIsOn){
                NiceSwitchView.this.mOnNiceSwitchClickListener.onSwitchClick(isOn);
            }
            return true;
        }
        // 滑动一段距离，up时触发
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }
        // 滑动时触发
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(e2.getX() > centerX){
                if(!knobState){
                    knobState = !knobState;

                    knobMoveAnimator.setFloatValues(knobMoveRate, 1.0F);
                    knobMoveAnimator.start();
                    innerContentAnimator.setFloatValues(innerContentRate, 0.0F);
                    innerContentAnimator.start();
                }
            }else{
                if(knobState){
                    knobState = !knobState;
                    knobMoveAnimator.setFloatValues(knobMoveRate, 0.0F);
                    knobMoveAnimator.start();
                }
            }
            return true;
        }
    };

    private void initAnimators(){
        innerContentAnimator = ValueAnimator.ofFloat(innerContentRate, 1.0F);
        knobExpandAnimator = ValueAnimator.ofFloat(knobExpandRate, 1.0F);
        knobMoveAnimator = ValueAnimator.ofFloat(knobMoveRate, 1.0F);

        innerContentAnimator.setDuration(commonDuration);
        knobExpandAnimator.setDuration(commonDuration);
        knobMoveAnimator.setDuration(commonDuration);

        innerContentAnimator.setInterpolator(new DecelerateInterpolator());
        knobExpandAnimator.setInterpolator(new DecelerateInterpolator());
        knobMoveAnimator.setInterpolator(new DecelerateInterpolator());

        innerContentAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setInnerContentRate((Float) animation.getAnimatedValue());
            }
        });
        knobExpandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setKnobExpandRate((Float) animation.getAnimatedValue());
            }
        });
        knobMoveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setKnobMoveRate((Float) animation.getAnimatedValue());
            }
        });

    }

    @Override
    protected void onAttachedToWindow(){
        super.onAttachedToWindow();
        isAttachedToWindow = true;

        if(dirtyAnimation){
            knobState = this.isOn;
            if(knobState){

                knobMoveAnimator.setFloatValues(knobMoveRate, 1.0F);
                knobMoveAnimator.start();

                innerContentAnimator.setFloatValues(innerContentRate, 0.0F);
                innerContentAnimator.start();
            }else{

                knobMoveAnimator.setFloatValues(knobMoveRate, 0.0F);
                knobMoveAnimator.start();

                innerContentAnimator.setFloatValues(innerContentRate, 1.0F);
                innerContentAnimator.start();
            }

            knobExpandAnimator.setFloatValues(knobExpandRate, 0.0F);
            knobExpandAnimator.start();

            if(NiceSwitchView.this.mOnNiceSwitchClickListener != null && isOn != preIsOn){
                NiceSwitchView.this.mOnNiceSwitchClickListener.onSwitchClick(isOn);
            }

            dirtyAnimation = false;
        }
    }

    @Override
    protected void onDetachedFromWindow(){
        super.onDetachedFromWindow();
        isAttachedToWindow = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        // 先执行原来的测量方法
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取原先的测量结果
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        // 设置自己自定义控件的宽高
        if((float) height / (float) width < 0.33333F){
            height = (int) ((float) width * 0.33333F);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.getMode(widthMeasureSpec));
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec));
            // 保存计算好的结果
            super.setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        }

        centerX = width / 2;
        centerY = height / 2;

        cornerRadius = centerY - shadowSpace;

        innerContentBound.left = outerStrokeWidth + shadowSpace;
        innerContentBound.top = outerStrokeWidth + shadowSpace;
        innerContentBound.right = width - outerStrokeWidth - shadowSpace;
        innerContentBound.bottom = height - outerStrokeWidth - shadowSpace;

        intrinsicInnerWidth = innerContentBound.width();
        intrinsicInnerHeight = innerContentBound.height();

        knobBound.left = outerStrokeWidth + shadowSpace;
        knobBound.top = outerStrokeWidth + shadowSpace;
        knobBound.right = height - outerStrokeWidth - shadowSpace;
        knobBound.bottom = height - outerStrokeWidth - shadowSpace;

        intrinsicKnobWidth = knobBound.height();
        knobMaxExpandWidth = (float) width * 0.7F;
        if(knobMaxExpandWidth > knobBound.width() * 1.25F){
            knobMaxExpandWidth = knobBound.width() * 1.25F;
        }
    }

    public void setOpen(boolean on, boolean animated){
        if(this.isOn == on) return;

        if(!isAttachedToWindow && animated){
            dirtyAnimation = true;
            this.isOn = on;
            return;
        }
        this.isOn = on;
        knobState = this.isOn;

        if(!animated){
            if(on){
                setKnobMoveRate(1.0F);
                setInnerContentRate(0.0F);
            }else{
                setKnobMoveRate(0.0F);
                setInnerContentRate(1.0F);
            }
            setKnobExpandRate(0.0F);
        }else{
            if(knobState){
                knobMoveAnimator.setFloatValues(knobMoveRate, 1.0F);
                knobMoveAnimator.start();

                innerContentAnimator.setFloatValues(innerContentRate, 0.0F);
                innerContentAnimator.start();
            }else{
                knobMoveAnimator.setFloatValues(knobMoveRate, 0.0F);
                knobMoveAnimator.start();

                innerContentAnimator.setFloatValues(innerContentRate, 1.0F);
                innerContentAnimator.start();
            }
            knobExpandAnimator.setFloatValues(knobExpandRate, 0.0F);
            knobExpandAnimator.start();
        }
        if(NiceSwitchView.this.mOnNiceSwitchClickListener != null && isOn != preIsOn){
            NiceSwitchView.this.mOnNiceSwitchClickListener.onSwitchClick(isOn);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(!isEnabled()) return false;

        switch(event.getAction()){
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(!knobState){
                    innerContentAnimator.setFloatValues(innerContentRate, 1.0F);
                    innerContentAnimator.start();
                }
                knobExpandAnimator.setFloatValues(knobExpandRate, 0.0F);
                knobExpandAnimator.start();

                isOn = knobState;

                if(NiceSwitchView.this.mOnNiceSwitchClickListener != null && isOn != preIsOn){
                    NiceSwitchView.this.mOnNiceSwitchClickListener.onSwitchClick(isOn);
                }
                break;
        }
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public void setEnabled(boolean enabled){
        super.setEnabled(enabled);
        if(enabled){
            this.mSwitchColor = mTempColor;
        }else{
            this.mSwitchColor = this.RGBColorTransform(0.5F, mTempColor, 0xFFFFFFFF);
        }
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        float w = (float) intrinsicInnerWidth / 2.0F * innerContentRate;
        float h = (float) intrinsicInnerHeight / 2.0F * innerContentRate;

        this.innerContentBound.left = centerX - w;
        this.innerContentBound.top = centerY - h;
        this.innerContentBound.right = centerX + w;
        this.innerContentBound.bottom = centerY + h;

        w = intrinsicKnobWidth + (float) (knobMaxExpandWidth - intrinsicKnobWidth) * knobExpandRate;

        boolean left = knobBound.left + knobBound.width() / 2 > centerX;

        if(left){
            knobBound.left = knobBound.right - w;
        }else{
            knobBound.right = knobBound.left + w;
        }

        float kw = knobBound.width();
        w = (float) (width - kw - ((shadowSpace + outerStrokeWidth) * 2)) * knobMoveRate;

        this.colorStep = RGBColorTransform(knobMoveRate, backgroundColor, mSwitchColor);

        knobBound.left = shadowSpace + outerStrokeWidth + w;
        knobBound.right = knobBound.left + kw;

        paint.setColor(colorStep);
        paint.setStyle(Paint.Style.FILL);

        drawRoundRect(shadowSpace, shadowSpace, width - shadowSpace, height - shadowSpace, cornerRadius, canvas, paint);

        paint.setColor(foregroundColor);
        // 设置圆角矩形
        canvas.drawRoundRect(innerContentBound, innerContentBound.height() / 2, innerContentBound.height() / 2, paint);
//        shadowDrawable.setBounds((int) (knobBound.left - shadowSpace), (int) (knobBound.top - shadowSpace), (int) (knobBound.right + shadowSpace), (int) (knobBound.bottom + shadowSpace));
//        shadowDrawable.draw(canvas);
        // 设置阴影
        paint.setShadowLayer(2, 0, shadowSpace / 2, isEnabled() ? 0x20000000 : 0x10000000);

//        paint.setColor(isEnabled() ? 0x20000000 : 0x10000000);
//        drawRoundRect(knobBound.left, knobBound.top + shadowSpace / 2, knobBound.right, knobBound.bottom + shadowSpace / 2, cornerRadius - outerStrokeWidth, canvas, paint);
//
//        paint.setColor(foregroundColor);
        canvas.drawRoundRect(knobBound, cornerRadius - outerStrokeWidth, cornerRadius - outerStrokeWidth, paint);
        paint.setShadowLayer(0, 0, 0, 0);

        paint.setColor(backgroundColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1.5f);
        canvas.drawRoundRect(knobBound, cornerRadius - outerStrokeWidth, cornerRadius - outerStrokeWidth, paint);
    }

    private void drawRoundRect(float left, float top, float right, float bottom, float radius, Canvas canvas, Paint paint){

        tempForRoundRect.left = left;
        tempForRoundRect.top = top;
        tempForRoundRect.right = right;
        tempForRoundRect.bottom = bottom;

        canvas.drawRoundRect(tempForRoundRect, radius, radius, paint);
    }

    // 重新计算 RGB 值
    private int RGBColorTransform(float progress, int fromColor, int toColor) {
        int or = (fromColor >> 16) & 0xFF;
        int og = (fromColor >> 8) & 0xFF;
        int ob = fromColor & 0xFF;

        int nr = (toColor >> 16) & 0xFF;
        int ng = (toColor >> 8) & 0xFF;
        int nb = toColor & 0xFF;

        int rGap = (int) ((float) (nr - or) * progress);
        int gGap = (int) ((float) (ng - og) * progress);
        int bGap = (int) ((float) (nb - ob) * progress);

        return 0xFF000000 | ((or + rGap) << 16) | ((og + gGap) << 8) | (ob + bGap);

    }


    public void setSwitchColor(int color){
        this.mSwitchColor = color;
        mTempColor = this.mSwitchColor;
    }

    public int getSwitchColor(){
        return this.mSwitchColor;
    }

    public boolean isOpen(){
        return this.isOn;
    }

    public void setNiceOpen(boolean on){
        setOpen(on, false);
    }

    public void setInnerContentRate(float rate){
        this.innerContentRate = rate;
        invalidate();
    }

    public float getInnerContentRate(){
        return this.innerContentRate;
    }

    public void setKnobExpandRate(float rate){
        this.knobExpandRate = rate;
        invalidate();
    }

    public float getKnobExpandRate(){
        return this.knobExpandRate;
    }

    public void setKnobMoveRate(float rate){
        this.knobMoveRate = rate;
        invalidate();
    }

    public float getKnobMoveRate(){
        return knobMoveRate;
    }

}
