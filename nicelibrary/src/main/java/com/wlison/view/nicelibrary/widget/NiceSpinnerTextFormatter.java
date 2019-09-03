package com.wlison.view.nicelibrary.widget;

import android.text.Spannable;
import android.text.SpannableString;

import com.wlison.view.nicelibrary.adapter.SpinnerTextFormatter;


public class NiceSpinnerTextFormatter implements SpinnerTextFormatter {

    @Override
    public Spannable format(String text) {
        return new SpannableString(text);
    }
}
