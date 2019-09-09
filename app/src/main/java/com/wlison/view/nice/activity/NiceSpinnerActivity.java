package com.wlison.view.nice.activity;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wlison.view.nice.R;
import com.wlison.view.nice.base.BaseActivity;
import com.wlison.view.nice.utils.ToastUtils;
import com.wlison.view.nicelibrary.model.NiceItemString;
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
    @BindView(R.id.niceSpring1)
    NiceSpinnerView niceSpring1;
    @BindView(R.id.niceSpring2)
    NiceSpinnerView niceSpring2;
    @BindView(R.id.niceSpring3)
    NiceSpinnerView niceSpring3;

    private List<String> list = new LinkedList<>(Arrays.asList("梅艳芳", "陈奕迅", "张学友", "刘德华", "黎明", "郭富城","陈慧琳", "郑秀文","欧阳娜娜", "张国荣", "周杰伦",
                                                                 "周润发", "李小龙", "夕阳无限好", "浮夸", "明年今日",  "裙下之臣"));
    private static final String[] CHANNELS = new String[]{"关注", "重要", "全部", "完赛", "论坛", "新闻", "NBA"};

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
        niceSpring1.setNiceSpinnerTeamData(CHANNELS);
        niceSpring2.setNiceSpinnerTeamData(CHANNELS);
        niceSpring3.setNiceSpinnerData(list);

        // 自定义布局
        niceSpring1.setItemLayoutId(R.layout.item_spinner_left_list);
        // 设置选中第几条
        niceSpring.setTextContext("张国荣");

        niceSpring.setOnNiceSpinnerClickListener(new NiceSpinnerView.OnNiceSpinnerClickListener() {
            @Override
            public void onNiceClick(BaseQuickAdapter adapter, View view, int position) {
                String s = ((NiceItemString) adapter.getItem(position)).getTitle();
                ToastUtils.showLongToast("点击第" + s + "   "+position);
            }
        });

        niceSpring1.setOnNiceSpinnerClickListener(new NiceSpinnerView.OnNiceSpinnerClickListener() {
            @Override
            public void onNiceClick(BaseQuickAdapter adapter, View view, int position) {
                String s = ((NiceItemString) adapter.getItem(position)).getTitle();
                ToastUtils.showLongToast("点击第" + s + "   "+position);
            }
        });

        niceSpring2.setOnNiceSpinnerClickListener(new NiceSpinnerView.OnNiceSpinnerClickListener() {
            @Override
            public void onNiceClick(BaseQuickAdapter adapter, View view, int position) {
                String s = ((NiceItemString) adapter.getItem(position)).getTitle();
                ToastUtils.showLongToast("点击第" + s + "   "+position);
            }
        });

        niceSpring3.setOnNiceSpinnerClickListener(new NiceSpinnerView.OnNiceSpinnerClickListener() {
            @Override
            public void onNiceClick(BaseQuickAdapter adapter, View view, int position) {
                String s = ((NiceItemString) adapter.getItem(position)).getTitle();
                ToastUtils.showLongToast("点击第" + s + "   "+position);
            }
        });
    }

}
