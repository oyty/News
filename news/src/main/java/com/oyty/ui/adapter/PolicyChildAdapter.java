package com.oyty.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oyty.bean.PolicyChildDataBean;
import com.oyty.news.R;

import java.util.List;

/**
 * Created by oyty on 5/13/15.
 */
public class PolicyChildAdapter extends BaseAdapter<PolicyChildDataBean.PolicyChildItem> {

    public PolicyChildAdapter(Context context, List<PolicyChildDataBean.PolicyChildItem> lists) {
        super(context, lists);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.adapter_policy_child, null);
            viewHolder.mPolicyChildLabel = (TextView) convertView.findViewById(R.id.mPolicyChildLabel);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PolicyChildDataBean.PolicyChildItem item = getDataLists().get(i);
        viewHolder.mPolicyChildLabel.setText(item.title);

        return convertView;
    }

    class ViewHolder {
        TextView mPolicyChildLabel;
    }
}
