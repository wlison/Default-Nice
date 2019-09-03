package com.wlison.view.nicelibrary.widget.date;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.wlison.view.nicelibrary.R;
import com.wlison.view.nicelibrary.utils.NiceUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Author: Simon
 * Date：5/17/2019 10:28
 * Desc:
 */
public class NiceCalenderView extends View {
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";

    private SimpleDateFormat mDateFormat;
    private Context mContext;
    private Paint mPaint;
    private List<String> mSelectDate;
    private Calendar mSelectCalendar;
    private Calendar mCalendar;
    private Drawable mDayBackground;
    private Drawable mCurrentDayBackground;
    private Drawable mSelectDayBackground;
    private Drawable mCurrentDay;
    private Drawable mBackground;
    private Drawable mSelectBackground;
    private int mSlop;
    private int mTextColor;
    private int mSelectTextColor;
    private float mTextSize;
    private float mSelectTextSize;
    private String mDateFormatPattern; // 日期格式化格式
    private Typeface mTypeface;
    private boolean mIsChangeDateStatus; // 日期状态是否能够改变
    private int mColumnWidth;
    private int mRowHeight;
    private int[][] mDays = new int[6][7]; // 存储对应列行处的天
    private int mDownX;
    private int mDownY;

    private OnNiceDataClickListener  mOnNiceDataClickListener;
    private OnNiceDateChangeListener mNiceChangeListener;
    private boolean mIsMultipleChoice;

    public void setOnNiceDataClickListener(OnNiceDataClickListener listener){
        this.mOnNiceDataClickListener = listener;
    }

    public void setOnNiceDateChangeListener(OnNiceDateChangeListener listener) {
        this.mNiceChangeListener = listener;
    }
    public interface OnNiceDataClickListener{

        /**
         * 日期点击监听.
         * @param view     与次监听器相关联的 View.
         * @param year     对应的年.
         * @param month    对应的月.
         * @param day      对应的日.
         */
        void onNiceDataClick(NiceCalenderView view, int year, int month, int day);
    }

    public interface OnNiceDateChangeListener {

        /**
         * 选中的天发生了改变监听回调, 改变有 2 种, 分别是选中和取消选中.
         * @param view     与次监听器相关联的 View.
         * @param select   true 表示是选中改变, false 是取消改变.
         * @param year     对应的年.
         * @param month    对应的月.
         * @param day      对应的日.
         */
        void onNiceSelectedDayChange(NiceCalenderView view, boolean select, int year, int month, int day);
    }

    public NiceCalenderView(Context context) {
        this(context,null);
        this.mContext = context;
    }

    public NiceCalenderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NiceCalenderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mSelectCalendar = Calendar.getInstance(Locale.CHINA);
        mCalendar = Calendar.getInstance(Locale.CHINA);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectDate = new ArrayList<>();
        setClickable(true);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NiceCalenderView);
        // 设置字体颜色
        int textColor = typedArray.getColor(R.styleable.NiceCalenderView_nice_calenderTextColor, ContextCompat.getColor(context,R.color.black80));
        setTextColor(textColor);
        // 选中后的字体颜色
        int selectTextColor = typedArray.getColor(R.styleable.NiceCalenderView_nice_calenderTextColorSelect, ContextCompat.getColor(context,R.color.black80));
        setSelectTextColor(selectTextColor);
        // 设置字体大小
        float textSize = typedArray.getDimension(R.styleable.NiceCalenderView_nice_calenderTextSize, NiceUtils.sp2px(context,14));
        setTextSize(textSize);
        // 设置选中后的字体大小
        float selectTextSize = typedArray.getDimension(R.styleable.NiceCalenderView_nice_calenderTextSizeSelect, NiceUtils.sp2px(context,14));
        setSelectTextSize(selectTextSize);
        // 默认天的背景
        mBackground = typedArray.getDrawable(R.styleable.NiceCalenderView_nice_dayBackground);
        setDayBackground(mBackground);
        // 选中后 天的背景
        mSelectBackground = typedArray.getDrawable(R.styleable.NiceCalenderView_nice_selectDayBackground);
        setSelectDayBackground(mSelectBackground);
        // 当前天的背景
        mCurrentDay = typedArray.getDrawable(R.styleable.NiceCalenderView_nice_selectDayBackgroundCurrent);
        setCurrentDayBackground(mCurrentDay);
        // 日期格式
        String pattern = typedArray.getString(R.styleable.NiceCalenderView_nice_dateFormatPattern);
        setDateFormatPattern(pattern);
        // 选中状态
        boolean isChange = typedArray.getBoolean(R.styleable.NiceCalenderView_nice_isChangeDateStatus, false);
        setChangeDateStatus(isChange);
        boolean isMultipleChoice = typedArray.getBoolean(R.styleable.NiceCalenderView_nice_isMultipleChoice,false);

        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*
        Paint.Style.FILL_AND_STROKE  填充且描边
        Paint.Style.STROKE           描边
        Paint.Style.FILL             填充
        */

        mColumnWidth = getWidth() / 7;
        mRowHeight = getHeight() / 6;
        mPaint.setTextSize(mTextSize);

        int year  = mCalendar.get(Calendar.YEAR);
        // 获取的月份要少一月, 所以这里 + 1
        int month = mCalendar.get(Calendar.MONTH) + 1;
        // 获取当月的天数
        int days  = NiceUtils.getMonthDays(year, month);
        // 获取当月第一天位于周几
        int week  = NiceUtils.getFirstDayWeek(year, month);
        //当前时间
        Time time = new Time();
        time.setToNow();
        int yeCur = time.year;
        int monthCur = time.month+1;
        int dayCur = time.monthDay;

        // 绘制每天
        for (int day = 1; day <= days; day++) {
            // 获取天在行、列的位置
            int column  =  (day + week - 1) % 7;
            int row     =  (day + week - 1) / 7;
            // 存储对应天
            mDays[row][column] = day;

            String dayStr = String.valueOf(day);
            float textWidth = mPaint.measureText(dayStr);
            int x = (int) (mColumnWidth * column + (mColumnWidth - textWidth) / 2);
            int y = (int) (mRowHeight * row + mRowHeight / 2 - (mPaint.ascent() + mPaint.descent()) / 2);

            // 判断 day 是否在选择日期内
            if(mSelectDate == null || mSelectDate.size() == 0 ||
                    !mSelectDate.contains(getFormatDate(year, month - 1, day))){
                // 没有则绘制默认背景和文字颜色
                if (yeCur == year && monthCur == month && dayCur == day) drawBackground(canvas, mCurrentDayBackground, column, row);
                else drawBackground(canvas, mDayBackground, column, row);
                drawText(canvas, dayStr, mTextColor, mTextSize, x, y);
            }else{
                // 否则绘制选择后的背景和文字颜色
                drawBackground(canvas, mSelectDayBackground, column, row);
                drawText(canvas, dayStr, mSelectTextColor, mSelectTextSize, x, y);
            }
        }

//        mPaint = new Paint();
//        // 设置是否抗锯齿
//        mPaint.setAntiAlias(true);
//        // 设置填充样式
//        mPaint.setStyle(Paint.Style.STROKE);
//        // 设置颜色
//        mPaint.setColor(Color.BLUE);
//        // 设置画笔宽度
//        mPaint.setStrokeWidth(1);
//        // 设置阴影
//        mPaint.setShadowLayer(10,15,15, Color.RED);
//
//        RectF rectF = new RectF(0f, 0f, 800f, 600f);
//        canvas.drawRect(rectF,mPaint);
//
//        // 绘制圆角，半径小于矩形宽的一半
//        mPaint.setColor(Color.RED);
//        // 发现，绘制后，圆角弧度与
//        canvas.drawRoundRect(rectF, 200f, 200f, mPaint);
//        mPaint.setColor(Color.BLACK);
//        canvas.drawCircle(200f, 200f, 200f, mPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!isClickable()) return false;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                int diffX = Math.abs(upX - mDownX);
                int diffY = Math.abs(upY - mDownY);
                if(diffX < mSlop && diffY < mSlop){
                    int column = upX / mColumnWidth;
                    int row    = upY / mRowHeight;
                    onClick(mDays[row][column]);
                }
                break;
            default:
        }
        return super.onTouchEvent(event);
    }

    private void onClick(int day){
        if(day < 1) return;

        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        if(mOnNiceDataClickListener != null){
            mOnNiceDataClickListener.onNiceDataClick(NiceCalenderView.this, year, month, day);
        }

        if(mIsChangeDateStatus){
            // 如果选中的天已经选择则取消选中
            String date = getFormatDate(year, month, day);
            // 是否多选
            if (mIsMultipleChoice) {
                if (mSelectDate != null && mSelectDate.contains(date)) {
                    mSelectDate.remove(date);
                    if (mNiceChangeListener != null) {
                        mNiceChangeListener.onNiceSelectedDayChange(this, false, year, month, day);
                    }
                } else {
                    if (mSelectDate == null) mSelectDate = new ArrayList<>();
                    mSelectDate.add(date);
                    if (mNiceChangeListener != null) {
                        mNiceChangeListener.onNiceSelectedDayChange(this, true, year, month, day);
                    }
                }
            } else {
                if (mSelectDate != null && mSelectDate.size() > 0) {
                    mSelectDate.clear();
                    mSelectDate.add(date);
                    if (mNiceChangeListener != null) {
                        mNiceChangeListener.onNiceSelectedDayChange(this, false, year, month, day);
                    }
                }else {
                    if (mSelectDate == null) mSelectDate = new ArrayList<>();
                    mSelectDate.add(date);
                }
            }
            invalidate();
        }
    }

    private void drawText(Canvas canvas, String text, @ColorInt int color, float size, int x, int y){
        mPaint.setColor(color);
        mPaint.setTextSize(size);
        if(mTypeface != null){
            mPaint.setTypeface(mTypeface);
        }
        canvas.drawText(text, x, y, mPaint);
    }

    private void drawBackground(Canvas canvas, Drawable background, int column, int row){
        if(background != null){
            canvas.save();
            int dx = (mColumnWidth * column) + (mColumnWidth / 2) - (background.getIntrinsicWidth() / 2);
            int dy = (mRowHeight * row) + (mRowHeight / 2) - (background.getIntrinsicHeight() / 2);
            canvas.translate(dx, dy);
            background.draw(canvas);
            canvas.restore();
        }
    }

    /**
     * 根据指定的年月日按当前日历的格式格式化后返回.
     *
     * @param year  年.
     * @param month 月.
     * @param day   日.
     * @return 格式化后的日期.
     */
    public String getFormatDate(int year, int month, int day){
        mSelectCalendar.set(year, month, day);
        return mDateFormat.format(mSelectCalendar.getTime());
    }


    /**
     * 设置选中的日期数据.
     *
     * @param days 日期数据, 日期格式为 {@link #setDateFormatPattern(String)} 方法所指定,
     * 如果没有设置则以默认的格式 {@link #DATE_FORMAT_PATTERN} 进行格式化.
     */
    public void setSelectDate(List<String> days){
        this.mSelectDate = days;
        invalidate();
    }

    /**
     * 获取选中的日期数据.
     *
     * @return 日期数据.
     */
    public List<String> getSelectDate(){
        return mSelectDate;
    }

    /**
     * 切换到下一个月.
     */
    public void nextMonth(){
        mCalendar.add(Calendar.MONTH, 1);
        invalidate();
    }

    /**
     * 切换到上一个月.
     */
    public void lastMonth(){
        mCalendar.add(Calendar.MONTH, -1);
        invalidate();
    }

    /**
     * 获取当前年份.
     *
     * @return year.
     */
    public int getYear(){
        return mCalendar.get(Calendar.YEAR);
    }

    /**
     * 获取当前月份.
     *
     * @return month. (思考后, 决定这里直接按 Calendar 的 API 进行返回, 不进行 +1 处理)
     */
    public int getMonth(){
        return mCalendar.get(Calendar.MONTH);
    }

    /**
     * 设置当前显示的 Calendar 对象.
     *
     * @param calendar 对象.
     */
    public void setCalendar(Calendar calendar){
        this.mCalendar = calendar;
        invalidate();
    }

    /**
     * 获取当前显示的 Calendar 对象.
     *
     * @return Calendar 对象.
     */
    public Calendar getCalendar(){
        return mCalendar;
    }

    /**
     * 设置文字颜色.
     *
     * @param textColor 文字颜色 {@link ColorInt}.
     */
    public void setTextColor(@ColorInt int textColor){
        this.mTextColor = textColor;
    }

    /**
     * 设置选中后的的文字颜色.
     *
     * @param textColor 文字颜色 {@link ColorInt}.
     */
    public void setSelectTextColor(@ColorInt int textColor){
        this.mSelectTextColor = textColor;
    }

    /**
     * 设置文字大小.
     *
     * @param textSize 文字大小 (sp).
     */
    public void setTextSize(float textSize){
        this.mTextSize = textSize;
    }

    /**
     * 设置选中后的的文字大小.
     *
     * @param textSize 文字大小 (sp).
     */
    public void setSelectTextSize(float textSize){
        this.mSelectTextSize = textSize;
    }

    /**
     * 设置天的背景.
     *
     * @param background 背景 drawable.
     */
    public void setDayBackground(Drawable background){
        if(background != null && mDayBackground != background){
            this.mDayBackground = background;
            setCompoundDrawablesWithIntrinsicBounds(mDayBackground);
        }
    }

    // 当前
    public void setCurrentDayBackground(Drawable background){
        if(background != null && mCurrentDayBackground != background){
            this.mCurrentDayBackground = background;
            setCompoundDrawablesWithIntrinsicBounds(mCurrentDayBackground);
        }
    }

    /**
     * 设置选择后天的背景.
     *
     * @param background 背景 drawable.
     */
    public void setSelectDayBackground(Drawable background){
        if(background != null && mSelectDayBackground != background){
            this.mSelectDayBackground = background;
            setCompoundDrawablesWithIntrinsicBounds(mSelectDayBackground);
        }
    }

    private void setCompoundDrawablesWithIntrinsicBounds(Drawable drawable){
        if(drawable != null){
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }
    }

    /**
     * 设置日期格式化格式.
     *
     * @param pattern 格式化格式, 如: yyyy-MM-dd.
     */
    public void setDateFormatPattern(String pattern){
        if(!TextUtils.isEmpty(pattern)){
            this.mDateFormatPattern = pattern;
        }else{
            this.mDateFormatPattern = DATE_FORMAT_PATTERN;
        }
        this.mDateFormat = new SimpleDateFormat(mDateFormatPattern, Locale.CHINA);
    }

    /**
     * 获取日期格式化格式.
     *
     * @return 格式化格式.
     */
    public String getDateFormatPattern(){
        return mDateFormatPattern;
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

    /**
     * 获取 {@link Paint} 对象.
     * @return {@link Paint}.
     */
    public Paint getPaint(){
        return mPaint;
    }

    /**
     * 设置点击是否能够改变日期状态 (默认或选中状态).
     *
     * 默认是 false, 即点击只会响应点击事件 {@link OnDataClickListener}, 日期状态而不会做出任何改变.
     *
     * @param isChanged 是否能改变日期状态.
     */
    public void setChangeDateStatus(boolean isChanged){
        this.mIsChangeDateStatus = isChanged;
    }

    /**
     *  是否是多选  默认是单选：false     多选：true
     * @param isMultipleChoice
     */
    public void setMultipleChoice(boolean isMultipleChoice){
        this.mIsMultipleChoice = isMultipleChoice;
    }

    /**
     * 获取是否能改变日期状态.
     *
     * @return {@link #mIsChangeDateStatus}.
     */
    public boolean isChangeDateStatus(){
        return mIsChangeDateStatus;
    }
}
