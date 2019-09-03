package com.wlison.view.nice.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.wlison.view.nice.R;
import com.wlison.view.nice.base.BaseActivity;
import com.wlison.view.nicelibrary.widget.NiceScrollView;

import butterknife.BindView;

/**
 * Author: Simon
 * Date：8/30/2019 16:44
 * Desc:
 */
public class NiceScrollViewActivity extends BaseActivity {
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.scrollView)
    NiceScrollView scrollView;
    @BindView(R.id.imageView)
    ImageView imgPic;

    @Override
    protected int getLayoutId() {
        return R.layout.nice_scroll_view_activity;
    }

    @Override
    protected String setToolBarTitle() {
        return "ScrollView";
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initScroll();
    }

    @SuppressLint("NewApi")
    private void initScroll() {
        scrollView.setOnScrollCallbacks(new NiceScrollView.OnScrollCallbacks() {
            @Override
            public void onScrollChanged(int x, int scrollY, int oldl, int oldt) {
                int height = imgPic.getTop();
                float scale = (float)scrollY / (height/2);

                if (scrollY <= 0) text.setAlpha(1);
                else if (scrollY > 0 && scrollY <= height) text.setAlpha(1-scale);
                else text.setAlpha(0);
            }
        });

    }
}
