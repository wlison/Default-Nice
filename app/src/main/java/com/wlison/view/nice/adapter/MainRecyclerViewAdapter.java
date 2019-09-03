package com.wlison.view.nice.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wlison.view.nice.R;

import java.util.List;

/**
 * Author: Simon
 * Date：5/6/2019 18:04
 * Desc:
 */
public class MainRecyclerViewAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private List<String> list;

    public MainRecyclerViewAdapter(@Nullable List<String> data) {
        super(R.layout.item_main_recycler_view,data);
        this.list = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        switch (helper.getLayoutPosition() % 3){
            case 0:
                helper.setImageResource(R.id.image_view, R.drawable.xsht_z);
                break;
            case 1:
                helper.setImageResource(R.id.image_view, R.drawable.ic_har);
                break;
            case 2:
                helper.setImageResource(R.id.image_view, R.drawable.ic_mtod);
                break;
        }
        helper.setText(R.id.text_view,item);
    }
}
