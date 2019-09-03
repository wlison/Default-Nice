package com.wlison.view.nice.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wlison.view.nice.R;

import butterknife.ButterKnife;

/**
 * Author: Simon
 * Date：5/6/2019 17:39
 * Desc:
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected abstract int getLayoutId();
    protected abstract String setToolBarTitle();

    protected Toolbar mToolbar;
    protected Activity mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mContext = this;
        ButterKnife.bind(this);

        initToolbar();
        initView(savedInstanceState);
        initData();
    }

    protected void initData() {
    }

    protected void initView(Bundle savedInstanceState) {
    }

    protected void showBack() {
        //setNavigationIcon必须在setSupportActionBar(toolbar);方法后面加入
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_action_return);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!backClick()) {
                    onBackPressed();
                }
            }
        });
    }

    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle(setToolBarTitle());
//            mToolbar.setTitle();
            if (isShowBacking()) {
                showBack();
            }
        }
    }

    protected boolean backClick() {
        return false;
    }

    protected boolean isShowBacking() {
        return true;
    }
}
