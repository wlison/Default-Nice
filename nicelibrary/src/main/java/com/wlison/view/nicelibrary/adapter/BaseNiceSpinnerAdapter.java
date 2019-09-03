package com.wlison.view.nicelibrary.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wlison.view.nicelibrary.R;
import com.wlison.view.nicelibrary.model.NiceSpinnerModel;

/**
 * Author: Simon
 * Date：5/10/2019 17:22
 * Desc:
 */
public abstract class BaseNiceSpinnerAdapter<T> extends BaseAdapter {
    private SpinnerTextFormatter spinnerTextFormatter;
    private NiceSpinnerModel mModel;
    private int selectedIndex;

    public BaseNiceSpinnerAdapter(Context context, NiceSpinnerModel model, SpinnerTextFormatter spinnerText) {
        this.spinnerTextFormatter = spinnerText;
        this.mModel = model;
    }

    @Override
    public abstract int getCount();

    @Override
    public abstract T getItem(int position);

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        TextView tvContext;
        if (convertView == null) {
            if (mModel.getItemGravity() == 1) convertView = View.inflate(context, R.layout.item_spinner_center_list, null);
            else if (mModel.getItemGravity() == 2) convertView = View.inflate(context, R.layout.item_spinner_left_list, null);
            tvContext = (TextView) convertView.findViewById(R.id.tv_spinner);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                tvContext.setBackground(ContextCompat.getDrawable(context, mModel.getItemSelector()));
            }
            convertView.setTag(new ViewHolder(tvContext));
        } else {
            tvContext = ((ViewHolder) convertView.getTag()).textView;
        }

        tvContext.setText(spinnerTextFormatter.format(getItem(position).toString()));
        tvContext.setTextColor(mModel.getTextColor());
        return convertView;
    }

    public abstract T getItemInDataset(int position);

    public int getSelectedIndex() {
        return selectedIndex;
    }

    void setSelectedIndex(int index) {
        selectedIndex = index;
    }

    private static class ViewHolder {
        TextView textView;

        ViewHolder(TextView textView) {
            this.textView = textView;
        }
    }
}
