package com.wlison.view.nice.activity;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wlison.view.nice.R;
import com.wlison.view.nice.base.BaseActivity;
import com.wlison.view.nice.utils.ToastUtils;
import com.wlison.view.nicelibrary.utils.NiceUtils;
import com.wlison.view.nicelibrary.widget.popup.PopupWindowView;

import butterknife.BindView;

/**
 * Author: Simon
 * Date：5/27/2019 10:52
 * Desc:
 */
public class NicePopupWindowActivity extends BaseActivity implements PopupWindowView.OnNicePopupClickListener {
    @BindView(R.id.btn_right)
    Button btnRight;
    private PopupWindowView popupWindow;

    @Override
    protected int getLayoutId() {
        return R.layout.nice_popup_window_activity;
    }

    @Override
    protected String setToolBarTitle() {
        return "PopupWindow";
    }

    //向左弹出
    public void onWindowLeft(View view) {
        if (popupWindow != null && popupWindow.isShowing()) return;
        popupWindow = new PopupWindowView.Builder(this)
                .setView(R.layout.popup_left_or_right)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, btnRight.getHeight())
                .setAnimationStyle(R.style.popupRightToLeft)
                .setViewOnclickListener(this)
                .create();
        popupWindow.showAsDropDown(view, -popupWindow.getWidth(), -view.getHeight());
    }

    public void onWindow(View view) {
        if (popupWindow != null && popupWindow.isShowing()) return;
        View upView = LayoutInflater.from(this).inflate(R.layout.popup_window_up, null);
        //测量View的宽高
        NiceUtils.measureWidthAndHeight(upView);
        popupWindow = new PopupWindowView.Builder(this)
                .setView(R.layout.popup_window_up)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, upView.getMeasuredHeight())
                .setBackGroundLevel(0.5f) // 取值范围0.0f-1.0f 值越小越暗
                .setAnimationStyle(R.style.popupWindowUp)
                .setViewOnclickListener(this)
                .create();
        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void OnPopupClick(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.popup_window_up:
                final TextView popup = (TextView) view.findViewById(R.id.tv_popup);
                final TextView window = (TextView) view.findViewById(R.id.tv_window);
                popup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showLongToast(popup.getText());
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                    }
                });
                window.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showLongToast("window");
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                    }
                });
                break;
        }
    }

}
