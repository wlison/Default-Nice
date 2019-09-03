package com.wlison.view.nicelibrary.widget.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Author: Simon
 * Date：5/24/2019 16:48
 * Desc:
 */
public class PopupWindowManage {
    private int layoutResId; // 布局id
    private Context context;
    private PopupWindow popupWindow;
    public View mPopupView; // 弹窗布局View
    private View mView;
    private Window mWindow;

    public PopupWindowManage(Context context, PopupWindow popupWindow) {
        this.context = context;
        this.popupWindow = popupWindow;
    }

    public void setView(int layoutResId) {
        mView = null;
        this.layoutResId = layoutResId;
        installContent();
    }

    public void setView(View view) {
        mView = view;
        this.layoutResId = 0;
        installContent();
    }

    private void installContent() {
        if (layoutResId != 0) {
            mPopupView = LayoutInflater.from(context).inflate(layoutResId, null);
        } else if (mView != null) {
            mPopupView = mView;
        }
        popupWindow.setContentView(mPopupView);
    }

    /**
     * 设置宽度
     *
     * @param width  宽
     * @param height 高
     */
    private void setWidthAndHeight(int width, int height) {
        if (width == 0 || height == 0) {
            //如果没设置宽高，默认是WRAP_CONTENT
            popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            popupWindow.setWidth(width);
            popupWindow.setHeight(height);
        }
    }


    /**
     * 设置背景灰色程度
     *
     * @param level 0.0f-1.0f
     */
    void setBackGroundLevel(float level) {
        mWindow = ((Activity) context).getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        params.alpha = level;
        mWindow.setAttributes(params);
    }


    /**
     * 设置动画
     */
    private void setAnimationStyle(int animationStyle) {
        popupWindow.setAnimationStyle(animationStyle);
    }

    /**
     * 设置Outside是否可点击
     *
     * @param touchable 是否可点击
     */
    private void setOutsideTouchable(boolean touchable) {
        // 设置背景透明
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置 outside 是否可点击
        popupWindow.setOutsideTouchable(touchable);
        popupWindow.setFocusable(touchable);
    }


    public static class PopupParams {
        public int layoutResId; // 布局id
        public Context mContext;
        public int mWidth, mHeight; // 弹窗的宽和高
        public boolean isShowBg, isShowAnim;
        public float bg_level; // 屏幕背景灰色程度
        public int animationStyle; // 动画 StyleId
        public View mView;
        public boolean isTouchable = true;

        public PopupParams(Context mContext) {
            this.mContext = mContext;
        }

        public void apply(PopupWindowManage manage) {
            if (mView != null) {
                manage.setView(mView);
            } else if (layoutResId != 0) {
                manage.setView(layoutResId);
            } else {
                throw new IllegalArgumentException("PopupView's manage is null");
            }
            manage.setWidthAndHeight(mWidth, mHeight);
            // 设置 outside 是否可点击
            manage.setOutsideTouchable(isTouchable);
            if (isShowBg) {
                //设置背景
                manage.setBackGroundLevel(bg_level);
            }
            if (isShowAnim) {
                manage.setAnimationStyle(animationStyle);
            }
        }
    }
}
