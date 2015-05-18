package com.oyty.ui.widget.setting;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;


public abstract class BaseRowView extends LinearLayout {

    public BaseRowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BaseRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseRowView(Context context) {
        super(context);
    }

    public abstract void initData(BaseRowDescriptor baseRowDescriptor,
                                  OnRowClickListener listener);

    public abstract void notifyDataChanged();

}
