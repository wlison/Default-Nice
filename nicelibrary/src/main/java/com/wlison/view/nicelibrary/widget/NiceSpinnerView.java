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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.wlison.view.nicelibrary.R;
import com.wlison.view.nicelibrary.adapter.NiceSpinnerAdapter;
import com.wlison.view.nicelibrary.model.NiceItemString;
import com.wlison.view.nicelibrary.utils.NiceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: Simon
 * Date：5/10/2019 11:49
 * Desc:
 */
public class NiceSpinnerView extends FrameLayout {
    public static final int TEXT_GRAVITY_LEFT = 1;
    public static final int TEXT_GRAVITY_CENTER = 2;
    private static final int MAX_LEVEL = 10000;
    public static final int VERTICAL_OFFSET = 1;

    private int listPaddingBottom = 2;
    private Context mContext;
    private TextView mText;
    private ImageView mImage;
    private PopupWindow window;
    private int mArrowColor;
    private int mTextColor;
    private float mTextSize;
    private int arrowMarignRight;
    private boolean isArrowShow;
    private int displayHeight;
    private Drawable drawableArrow;
    private int drawableArrowId;
    private int mItemTextColor;
    private int mItemSelectorColor;
    private int mDrawableBackground;
    private int mTextGravity,mTextPadding;
//    private int mWindowDrawable;
    private String mItemContext;
    private List<String> mDataList = new ArrayList<>();
    private RecyclerView mNiceRecyclerView;
    private NiceSpinnerAdapter mSpinnerAdapter;
    private List<NiceItemString> mItemStringList;
    private int mSelectType;
    private int layoutId = R.layout.item_spinner_center_list;

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
        mArrowColor = typedArray.getColor(R.styleable.NiceSpinnerView_nice_arrowColor, ContextCompat.getColor(context,R.color.colorPrimary));
        // 字体颜色
        mTextColor = typedArray.getColor(R.styleable.NiceSpinnerView_nice_textColor,ContextCompat.getColor(context,R.color.second_text_color));
        // 字体大小
        mTextSize = typedArray.getFloat(R.styleable.NiceSpinnerView_nice_textSize,16f);
        // 是否显示箭头
        isArrowShow = typedArray.getBoolean(R.styleable.NiceSpinnerView_nice_arrowHide,false);
        // 箭头 id
        drawableArrowId = typedArray.getResourceId(R.styleable.NiceSpinnerView_nice_arrowDrawableId,-1);
        // item 字体颜色
        mItemTextColor = typedArray.getColor(R.styleable.NiceSpinnerView_nice_itemTextColor,ContextCompat.getColor(mContext,R.color.second_text_color));
        // item 点击效果
        mItemSelectorColor = typedArray.getResourceId(R.styleable.NiceSpinnerView_nice_itemSelectorColor, R.drawable.nice_selector);
        // drawable Background 背景
        mDrawableBackground = typedArray.getResourceId(R.styleable.NiceSpinnerView_nice_drawable_background,R.drawable.nice_shape_back_width);
        // 字体位置
        mTextGravity = typedArray.getInt(R.styleable.NiceSpinnerView_nice_textItemGravity,2);
        // 字体的 padding
        mTextPadding = typedArray.getInt(R.styleable.NiceSpinnerView_nice_itemTextPadding,10);
        // window 背景设置
//        mWindowDrawable = typedArray.getResourceId(R.styleable.NiceSpinnerView_nice_window_background,R.drawable.nice_shape_back_pop);
        // 选中Type
        mSelectType = typedArray.getInt(R.styleable.NiceSpinnerView_nice_selectType,0);
        // 自定义箭头离右边距离
        arrowMarignRight = typedArray.getInt(R.styleable.NiceSpinnerView_nice_arrowMarginRight,8);

        initView(context);
        typedArray.recycle();
    }

    private void initView(Context context) {
        mText = new TextView(context);
        mImage = new ImageView(context);
        // 框 LayoutParams
        FrameLayout.LayoutParams params = new LayoutParams(-1,230);
        // 箭头 LayoutParams
        FrameLayout.LayoutParams params1 = new LayoutParams(-2,-2);
        params1.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        // 字体 LayoutParams
        FrameLayout.LayoutParams params2 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,-2);

        if (mTextGravity != 0) {
            switch (mTextGravity){
                case TEXT_GRAVITY_LEFT:
                    params2.gravity = Gravity.LEFT;
                    mText.setPadding(NiceUtils.dp2px(context,mTextPadding), 26, 0, 26);
                    break;
                case TEXT_GRAVITY_CENTER:
                    params2.gravity = Gravity.CENTER;
                    mText.setPadding(0, 26, 0, 26);
                    break;
            }
        }

        // 自定义箭头离右边距离
        if (drawableArrowId == -1)drawableArrowId = R.drawable.nice_spinner_arrow;
        else params1.rightMargin = NiceUtils.dp2px(context,arrowMarignRight);

        // 设置背景
        setBackgroundResource(mDrawableBackground);
        // 设置控件新属性
        setLayoutParams(params);
        // 字体大小
        mText.setTextSize(mTextSize);
        // 字体颜色
        mText.setTextColor(mTextColor);
        this.addView(mText,params2);
        this.addView(mImage,params1);
        setClickable(true);

        mNiceRecyclerView = new RecyclerView(context);
        mNiceRecyclerView.setId(getId());
        mNiceRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        // item Layout
        if (mTextGravity == 1) {
            layoutId = R.layout.item_spinner_left_list;
        } else if (mTextGravity == 2) {
            layoutId = R.layout.item_spinner_center_list;
        }
        // recycler Adapter
        mSpinnerAdapter = new NiceSpinnerAdapter(layoutId,new ArrayList<NiceItemString>());
        // 间隔线
//        mNiceRecyclerView.addItemDecoration(mSpinnerAdapter.setItemDecoration((int) context.getResources().getDimension(R.dimen.dp_1)));
        mNiceRecyclerView.setAdapter(mSpinnerAdapter);
        mNiceRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                mOnNiceSpinnerClickListener.onNiceClick(adapter, view, position);
                NiceItemString model = (NiceItemString) adapter.getItem(position);
                mItemContext = model.getTitle();
                List<NiceItemString> list = adapter.getData();
                for (NiceItemString item : list){
                    item.setImgPic(false);
                    item.setBackground(false);
                }
                if (model.getSelectType() == 1) model.setImgPic(true);
                else if (model.getSelectType() == 2) model.setBackground(true);
                adapter.notifyDataSetChanged();
                mText.setText(mItemContext);
                dismissWindow();
            }
        });

        window = new PopupWindow(context);
        window.setContentView(mNiceRecyclerView);
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
            mImage.setImageDrawable(drawableArrow);
        } else {
            Drawable temp = new ColorDrawable(ContextCompat.getColor(getContext(), android.R.color.transparent));
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

    // 箭头动画
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
        window.showAsDropDown(this, 0, 1);
    }

    private void measurePopUpDimension() {
        int widthSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(displayHeight - getParentVerticalOffset() - getMeasuredHeight(), MeasureSpec.AT_MOST);
        mNiceRecyclerView.measure(widthSpec, heightSpec);
        window.setWidth(mNiceRecyclerView.getMeasuredWidth());

        // 设置popup高度  可以自定义
        int height = NiceUtils.getScreenHeight(mContext) / 2;
        int listViewHeight = mNiceRecyclerView.getMeasuredHeight() - listPaddingBottom;
        window.setHeight(listViewHeight > height ? height : listViewHeight);
        window.setBackgroundDrawable(null);
//        window.setBackgroundDrawable(ContextCompat.getDrawable(mContext,mWindowDrawable));
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

    /**
     * @param itemLayoutId 自定义itemLayoutId
     */
    public void setItemLayoutId(int itemLayoutId){
        mSpinnerAdapter = new NiceSpinnerAdapter(itemLayoutId,new ArrayList<NiceItemString>());
        mNiceRecyclerView.setAdapter(mSpinnerAdapter);
        mSpinnerAdapter.setNewData(mItemStringList);
    }

    public String getTextContext(){
        return mItemContext;
    }

    /**
     * @param context 设置显示的数据
     */
    public void setTextContext(String context){
        int index = mDataList.indexOf(context);
        if (index != -1){
            mItemContext = context;
            mText.setText(context);
            setItemSelector(index);
        }
        invalidate();
    }

    /**
     * @param position 选中第几条数据
     */
    public void setItemSelector(int position){
        for (NiceItemString item : mItemStringList){
            if (mSelectType == 1) item.setImgPic(false);
            else if (mSelectType == 2) item.setBackground(false);
        }
        if (mSelectType == 1) mItemStringList.get(position).setImgPic(true);
        else if (mSelectType == 2) mItemStringList.get(position).setBackground(true);
        invalidate();
    }

    /**
     * @param teamData 数组
     */
    public void setNiceSpinnerTeamData(String[] teamData){
        mDataList = Arrays.asList(teamData);
        setNiceSpinnerData(mDataList);
    }

    /**
     * @param list list 数据
     */
    public void setNiceSpinnerData(List<String> list) {
        mDataList = list;
        mItemStringList = new ArrayList<>();
        for (int i=0;i<list.size();i++){
            NiceItemString nice = new NiceItemString();
            String item = list.get(i);
            if (i == 0) {
                if (mSelectType == 1) nice.setImgPic(true);
                else if (mSelectType == 2) nice.setBackground(true);
            }
            nice.setTitle(item);
            nice.setSelectType(mSelectType);
            mItemStringList.add(nice);
        }

        mItemContext = list.get(0);
        mText.setText(mItemContext);
        mSpinnerAdapter.setNewData(mItemStringList);
    }

    // 回调接口
    private OnNiceSpinnerClickListener mOnNiceSpinnerClickListener;
    public interface OnNiceSpinnerClickListener{
        void onNiceClick(BaseQuickAdapter adapter, View view, int position);
    }
    public void setOnNiceSpinnerClickListener(OnNiceSpinnerClickListener listener){
        this.mOnNiceSpinnerClickListener = listener;
    }

}
