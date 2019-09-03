package com.wlison.view.nicelibrary.adapter;

import android.content.Context;

import com.wlison.view.nicelibrary.model.NiceSpinnerModel;
import com.wlison.view.nicelibrary.widget.NiceSpinnerTextFormatter;

import java.util.List;

/**
 * Author: Simon
 * Date：5/10/2019 17:09
 * Desc:
 */
public class NiceSpinnerAdapter<T> extends BaseNiceSpinnerAdapter{
    private final List<T> items;

    public NiceSpinnerAdapter(Context context, List<T> items, NiceSpinnerModel model) {
        super(context, model, new NiceSpinnerTextFormatter());
        this.items = items;
    }

    @Override
    public int getCount() {
//        return items.size() - 1;
        return items.size();
    }

    @Override
    public T getItem(int position) {
//        if (position >= selectedIndex) {
//            return items.get(position + 1);
//        } else {
        return items.get(position);
//        }
    }

    @Override
    public T getItemInDataset(int position) {
        return items.get(position);
    }
}
