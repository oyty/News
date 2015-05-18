package com.oyty.ui.widget.setting;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.oyty.news.R;


public class NormalRowView extends BaseRowView implements OnClickListener {

    private Context mContext;
    private ImageView mWidgetRowIconImg;
    private TextView mWidgetRowLabel;
    private ImageView mWidgetRowActionImg;
    private OnRowClickListener listener;
    private RowDescriptor descriptor;

    public enum RowActionEnum {
        TEXT_SIZE, TRAFFIC_STATISTICS, CLEAR_CACHE, GUIDE, FEEDBACK, CHECK_UPDATE, ABOUT
    }

    public NormalRowView(Context context) {
        super(context);
        initView(context);
    }

    public NormalRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public NormalRowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        View mRowView = View.inflate(mContext, R.layout.view_generate_row, this);
        mWidgetRowIconImg = (ImageView) mRowView.findViewById(R.id.mWidgetRowIconImg);
        mWidgetRowLabel = (TextView) mRowView.findViewById(R.id.mWidgetRowLabel);
        mWidgetRowActionImg = (ImageView) mRowView.findViewById(R.id.mWidgetRowActionImg);

        mWidgetRowActionImg.setImageResource(R.drawable.pay_nofify_nav);
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onRowClick(descriptor.action);
        }
    }

    @Override
    public void initData(BaseRowDescriptor baseRowDescriptor,
                         OnRowClickListener listener) {
        this.descriptor = (RowDescriptor) baseRowDescriptor;
        this.listener = listener;
        notifyDataChanged();
    }

    public void notifyDataChanged() {
        if (descriptor != null) {
            mWidgetRowIconImg.setBackgroundResource(descriptor.iconResId);
            mWidgetRowLabel.setText(descriptor.label);

            if (listener != null) {
                setOnClickListener(this);
                setBackgroundResource(R.drawable.widget_general_row_selector);
                mWidgetRowActionImg.setVisibility(View.VISIBLE);
            } else {
                setBackgroundColor(Color.WHITE);
                mWidgetRowActionImg.setVisibility(View.GONE);
            }
            setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.GONE);
        }
    }


}
