package com.wlison.view.nicelibrary.widget.popup;

import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;

import com.wlison.view.nicelibrary.utils.NiceUtils;

/**
 * Author: Simon
 * Date：5/24/2019 16:50
 * Desc:
 */
public class PopupWindowView extends PopupWindow {
    private final PopupWindowManage mWindowManage;

    // 获取 PopupWindow 的宽
    @Override
    public int getWidth() {
        return mWindowManage.mPopupView.getMeasuredWidth();
    }

    // 获取 PopupWindow 的高
    @Override
    public int getHeight() {
        return mWindowManage.mPopupView.getMeasuredHeight();
    }

    // 回调接口
    public interface OnNicePopupClickListener {
        void OnPopupClick(View view, int layoutResId);
    }

    private PopupWindowView(Context context) {
        mWindowManage = new PopupWindowManage(context, this);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mWindowManage.setBackGroundLevel(1.0f);
    }

    public static class Builder {
        private final PopupWindowManage.PopupParams params;
        private OnNicePopupClickListener listener;

        public Builder(Context context) {
            params = new PopupWindowManage.PopupParams(context);
        }

        /**
         * @param layoutResId 设置PopupWindow 布局ID
         * @return Builder
         */
        public Builder setView(int layoutResId) {
            params.mView = null;
            params.layoutResId = layoutResId;
            return this;
        }

        /**
         * @param view 设置PopupWindow布局
         * @return Builder
         */
        public Builder setView(View view) {
            params.mView = view;
            params.layoutResId = 0;
            return this;
        }

        /**
         * 设置子View
         *
         * @param listener ViewInterface
         * @return Builder
         */
        public Builder setViewOnclickListener(OnNicePopupClickListener listener) {
            this.listener = listener;
            return this;
        }

        /**
         * 设置宽度和高度 如果不设置 默认是wrap_content
         *
         * @param width 宽
         * @return Builder
         */
        public Builder setWidthAndHeight(int width, int height) {
            params.mWidth = width;
            params.mHeight = height;
            return this;
        }

        /**
         * 设置背景灰色程度
         *
         * @param level 0.0f-1.0f
         * @return Builder
         */
        public Builder setBackGroundLevel(float level) {
            params.isShowBg = true;
            params.bg_level = level;
            return this;
        }

        /**
         * 是否可点击Outside消失
         *
         * @param touchable 是否可点击
         * @return Builder
         */
        public Builder setOutsideTouchable(boolean touchable) {
            params.isTouchable = touchable;
            return this;
        }

        /**
         * 设置动画
         *
         * @return Builder
         */
        public Builder setAnimationStyle(int animationStyle) {
            params.isShowAnim = true;
            params.animationStyle = animationStyle;
            return this;
        }

        public PopupWindowView create() {
            final PopupWindowView popupWindow = new PopupWindowView(params.mContext);
            params.apply(popupWindow.mWindowManage);
            if (listener != null && params.layoutResId != 0) {
                listener.OnPopupClick(popupWindow.mWindowManage.mPopupView, params.layoutResId);
            }
            NiceUtils.measureWidthAndHeight(popupWindow.mWindowManage.mPopupView);
            return popupWindow;
        }
    }
}
