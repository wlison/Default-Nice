package com.wlison.view.nice.activity;

import android.os.Bundle;

import com.wlison.view.nice.R;
import com.wlison.view.nice.base.BaseActivity;
import com.wlison.view.nice.utils.ToastUtils;
import com.wlison.view.nicelibrary.widget.NiceSwitchView;

import butterknife.BindView;

/**
 * Author: Simon
 * Date：5/16/2019 10:16
 * Desc:
 */
public class NiceSwitchActivity extends BaseActivity {
    @BindView(R.id.nice_switch)
    NiceSwitchView niceSwitch;
    @BindView(R.id.nice_switch1)
    NiceSwitchView niceSwitch1;

    @Override
    protected int getLayoutId() {
        return R.layout.nice_switch_activity;
    }

    @Override
    protected String setToolBarTitle() {
        return "NiceSwitch";
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        niceSwitch1.setNiceOpen(true);
        niceSwitch.setOnNiceSwitchClickListener(new NiceSwitchView.OnNiceSwitchClickListener() {
            @Override
            public void onSwitchClick(boolean isOpen) {
                niceSwitch1.setNiceOpen(!isOpen);
                ToastUtils.showLongToast("This is one   " + isOpen);
            }
        });
        niceSwitch1.setOnNiceSwitchClickListener(new NiceSwitchView.OnNiceSwitchClickListener() {
            @Override
            public void onSwitchClick(boolean isOpen) {
                niceSwitch.setNiceOpen(!isOpen);
                ToastUtils.showLongToast("This is two  " + isOpen);
            }
        });
    }

}
