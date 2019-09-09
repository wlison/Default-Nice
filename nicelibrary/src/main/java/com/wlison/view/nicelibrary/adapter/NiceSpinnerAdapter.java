package com.wlison.view.nicelibrary.adapter;

import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wlison.view.nicelibrary.R;
import com.wlison.view.nicelibrary.model.NiceItemString;

import java.util.List;

/**
 * Author: Simon
 * Date：5/10/2019 17:09
 * Desc:
 */
public class NiceSpinnerAdapter extends BaseQuickAdapter<NiceItemString, BaseViewHolder> {

    public NiceSpinnerAdapter(int layoutResId, @Nullable List<NiceItemString> data) {
        super(layoutResId, data);
    }

    public CustomItemDecoration setItemDecoration(int height){
        return new CustomItemDecoration(height);
    }

    class CustomItemDecoration extends RecyclerView.ItemDecoration {
        private int height;

        CustomItemDecoration(int height){
            this.height = height;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.set(0, 0, 0, height);
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, NiceItemString item) {
        helper.setText(R.id.tvTitle, item.getTitle());
        if (item.getSelectType() == 1){
            helper.setVisible(R.id.imgTick,item.isImgPic());
        }else if (item.getSelectType() == 2) {
            helper.getView(R.id.tvTitle).setSelected(item.isBackground());
        }

        if (helper.getLayoutPosition() == getItemCount()){
            helper.setVisible(R.id.line,false);
        }else helper.setVisible(R.id.line,true);

    }
}