package com.wlison.view.nicelibrary.widget;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wlison.view.nicelibrary.R;
import com.wlison.view.nicelibrary.adapter.BaseNiceSpinnerAdapter;
import com.wlison.view.nicelibrary.adapter.NiceSpinnerAdapter;
import com.wlison.view.nicelibrary.model.NiceSpinnerModel;
import com.wlison.view.nicelibrary.utils.NiceUtils;

import java.util.List;

/**
 * Author: Simon
 * Date：5/10/2019 11:49
 * Desc:
 */
public class NiceSpinnerView extends FrameLayout {
    private static final int MAX_LEVEL = 10000;
    public static final int VERTICAL_OFFSET = 1;

    private int listPaddingBottom = 2;
    private Context mContext;
    private TextView mText;
    private ImageView mImage;
    private ListView listView;
    private PopupWindow window;
    private int mArrowColor;
    private int mTextColor;
    private float mTextSize;
    private boolean isArrowShow;
    private int displayHeight;
    private Drawable drawableArrow;
    private int drawableArrowId;
    private int mItemTextColor;
    private int mItemSelectorColor;
    private NiceSpinnerModel mModel = new NiceSpinnerModel();
    private int mDrawableBackground;
    private int mTextGravity;
    private int mDividerColor;
    private int mDividerHeight;
    private int mWindowDrawable;

    public NiceSpinnerView(@NonNull Context context) {
        super(context);
        this.mContext = context;
        init(context,null);
    }

    public NiceSpinnerView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(context,attrs);
    }

    public NiceSpinnerView(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(context,attrs);
    }

    // 属性设置
    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NiceSpinnerView);
        // 箭头颜色
        mArrowColor = typedArray.getColor(R.styleable.NiceSpinnerView_nice_arrowColor, ContextCompat.getColor(context,R.color.primary_dark));
        // 字体颜色
        mTextColor = typedArray.getColor(R.styleable.NiceSpinnerView_nice_textColor,ContextCompat.getColor(context,R.color.text_black));
        // 字体大小
        mTextSize = typedArray.getFloat(R.styleable.NiceSpinnerView_nice_textSize,16f);
        // 是否显示箭头
        isArrowShow = typedArray.getBoolean(R.styleable.NiceSpinnerView_nice_arrowHide,false);
        // 箭头 id
        drawableArrowId = typedArray.getResourceId(R.styleable.NiceSpinnerView_nice_arrowDrawableId,R.drawable.nice_spinner_arrow);
        // item 字体颜色
        mItemTextColor = typedArray.getColor(R.styleable.NiceSpinnerView_nice_itemTextColor,ContextCompat.getColor(mContext,R.color.black50));
        // item 点击效果
        mItemSelectorColor = typedArray.getResourceId(R.styleable.NiceSpinnerView_nice_itemSelectorColor, R.drawable.nice_selector);
        // drawable Background 背景
        mDrawableBackground = typedArray.getResourceId(R.styleable.NiceSpinnerView_nice_drawable_background,R.drawable.nice_shape_back_width);
        // 字体位置
        mTextGravity = typedArray.getInteger(R.styleable.NiceSpinnerView_nice_textItemGravity,1);
        // listView 分割线颜色
        mDividerColor = typedArray.getColor(R.styleable.NiceSpinnerView_nice_dividerColor,ContextCompat.getColor(context,R.color.black20));
        // listView 分割线高度
        mDividerHeight = typedArray.getInteger(R.styleable.NiceSpinnerView_nice_dividerHeight,1);
        // window 背景设置
        mWindowDrawable = typedArray.getResourceId(R.styleable.NiceSpinnerView_nice_window_background,R.drawable.nice_shape_back_pop);

        mModel.setTextColor(mItemTextColor);
        mModel.setItemSelector(mItemSelectorColor);
        mModel.setItemGravity(mTextGravity);

        initView(context);
        typedArray.recycle();
    }

    private void initView(Context context) {
        mText = new TextView(context);
        mImage = new ImageView(context);

        FrameLayout.LayoutParams params = new LayoutParams(-1,230);

        FrameLayout.LayoutParams params1 = new LayoutParams(-2,-2);
        params1.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;

        FrameLayout.LayoutParams params2 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,-2);
        if (mTextGravity == 1){
            params2.gravity = Gravity.CENTER;
            mText.setPadding(0,26,0,26);
        }else if (mTextGravity == 2){
            params2.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
            mText.setPadding(38,26,0,26);
        }

        // 设置背景
        setBackgroundResource(mDrawableBackground);
        setLayoutParams(params);
        mText.setTextSize(mTextSize);
        mText.setTextColor(mTextColor);
        this.addView(mText,params2);
        this.addView(mImage,params1);
        setClickable(true);

        listView = new ListView(context);
        listView.setId(getId());
        // 分割线颜色
        listView.setDivider(new ColorDrawable(mDividerColor));
        // 分割线高度
        listView.setDividerHeight(mDividerHeight);
//        listView.setBackground(ContextCompat.getDrawable(mContext,mWindowDrawable));
        listView.setItemsCanFocus(true);
        listView.setVerticalScrollBarEnabled(false); // 不活动的时候隐藏，活动的时候显示
        listView.setHorizontalScrollBarEnabled(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mOnNiceSpinnerClickListener.onNiceClick(parent,view,position,id);
                String str = parent.getItemAtPosition(position).toString();
                mText.setText(str);
                dismissWindow();
            }
        });

        window = new PopupWindow(context);
        window.setContentView(listView);
        window.setOutsideTouchable(true);
        window.setFocusable(true);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (!isArrowShow) animateArrow(false);
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        Activity activity = (Activity) getContext();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        displayHeight = displayMetrics.heightPixels;
    }

    // 该方法在当前View或其父控件的可见性改变时被调用
    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        drawableArrow = initArrowDrawable(mArrowColor);
        if (!isArrowShow && drawableArrow != null) {
//            setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            mImage.setImageDrawable(drawableArrow);
        } else {
            Drawable temp = new ColorDrawable(ContextCompat.getColor(getContext(), android.R.color.transparent));
//            temp.setBounds(0,0,arrowDrawable.getMinimumWidth(),arrowDrawable.getMinimumHeight());
//            setCompoundDrawables(null, null, temp, null);
            mImage.setImageDrawable(temp);
        }
    }

    public Drawable initArrowDrawable(int drawableTint) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), drawableArrowId);
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable);
            if (drawableTint != Integer.MAX_VALUE && drawableTint != 0) {
                DrawableCompat.setTint(drawable, drawableTint);
            }
        }
        return drawable;
    }

    public void animateArrow(boolean isAnimate) {
        int start = isAnimate ? 0 : MAX_LEVEL;
        int end = isAnimate ? MAX_LEVEL : 0;
        ObjectAnimator animator = ObjectAnimator.ofInt(drawableArrow, "level", start, end);
        animator.setInterpolator(new LinearOutSlowInInterpolator());
        animator.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnabled() && event.getAction() == MotionEvent.ACTION_UP){
            if (!window.isShowing())showWindow();
            else dismissWindow();
        }
        return super.onTouchEvent(event);
    }

    private void dismissWindow() {
        if (!isArrowShow) animateArrow(false);
        window.dismiss();
    }

    // 显示距离、离选择框
    public void showWindow() {
        if (!isArrowShow) {
            animateArrow(true);
        }
        measurePopUpDimension();
        // 设置 window 离显示框的高度
        window.showAsDropDown(this, 0, 2);
    }

    private void measurePopUpDimension() {
        int widthSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(displayHeight - getParentVerticalOffset() - getMeasuredHeight(), MeasureSpec.AT_MOST);
        listView.measure(widthSpec, heightSpec);
        window.setWidth(listView.getMeasuredWidth());

        // 设置popup高度  可以自定义
        int height = NiceUtils.getScreenHeight(mContext) / 2;
        int listViewHeight = listView.getMeasuredHeight() - listPaddingBottom;
        window.setHeight(listViewHeight > height ? height : listViewHeight);
        window.setBackgroundDrawable(ContextCompat.getDrawable(mContext,mWindowDrawable));
    }

    // 获得目标控件位置 ，判断是要放在这个目标的上面还是下面
    private int vertical;
    private int getParentVerticalOffset() {
        if (vertical > 0) {
            return vertical;
        }
        int[] locationOnScreen = new int[2];
        getLocationOnScreen(locationOnScreen);
        return vertical = locationOnScreen[VERTICAL_OFFSET];
    }

    // 设置 NiceSpring 数据
    public <T> void setNiceSpinnerData(List<T> list) {
        BaseNiceSpinnerAdapter adapter = new NiceSpinnerAdapter<>(getContext(), list, mModel);
        setAdapterInternal(adapter);
        mText.setText((String)adapter.getItem(0));
    }

    private void setAdapterInternal(BaseNiceSpinnerAdapter adapter) {
        // If the adapter needs to be settled again, ensure to reset the selected index as well
        listView.setAdapter(adapter);
    }

    // 回调接口
    private OnNiceSpinnerClickListener mOnNiceSpinnerClickListener;
    public interface OnNiceSpinnerClickListener{
        void onNiceClick(AdapterView<?> parent, View view, int position, long id);
    }
    public void setOnNiceSpinnerClickListener(OnNiceSpinnerClickListener listener){
        this.mOnNiceSpinnerClickListener = listener;
    }

}
