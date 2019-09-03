package com.wlison.view.nice.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.wlison.view.nice.R;
import com.wlison.view.nice.base.BaseActivity;
import com.wlison.view.nice.utils.ToastUtils;
import com.wlison.view.nicelibrary.widget.NiceSpinnerView;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

/**
 * Author: Simon
 * Date：5/6/2019 17:38
 * Desc:
 */
public class NiceSpinnerActivity extends BaseActivity {
    @BindView(R.id.nice_spring)
    NiceSpinnerView niceSpring;

    private List<String> list = new LinkedList<>(Arrays.asList("陈奕迅", "张学友", "刘德华", "黎明", "郭富城","陈慧琳", "郑秀文","欧阳娜娜", "梅艳芳", "张国荣", "周杰伦",
                                                                 "周润发", "李小龙", "夕阳无限好", "浮夸", "明年今日",  "裙下之臣"));
    private static final String[] CHANNELS = new String[]{"关注", "重要", "全部", "完赛", "论坛", "新闻", "NBA","刘德华", "陈慧琳", "郑秀文", "小马哥"};

    @Override
    protected int getLayoutId() {
        return R.layout.nice_spring_activity;
    }

    @Override
    protected String setToolBarTitle() {
        return "NiceSpring";
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        niceSpring.setNiceSpinnerData(list);

        niceSpring.setOnNiceSpinnerClickListener(new NiceSpinnerView.OnNiceSpinnerClickListener() {
            @Override
            public void onNiceClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtils.showLongToast("点击第" + position);
            }
        });
    }

}
