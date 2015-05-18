package com.oyty.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oyty.bean.PolicyDataModel;
import com.oyty.news.R;

import java.util.List;

/**
 * Created by oyty on 5/12/15.
 */
public class PolicyAdapter extends BaseAdapter<PolicyDataModel.PolicyItem> {

    private OnGroupItemClickListener mListener;

    public PolicyAdapter(Context context, List<PolicyDataModel.PolicyItem> lists, OnGroupItemClickListener listener) {
        super(context, lists);
        this.mListener = listener;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        PolicyHolder viewHolder = null;
        if(convertView == null) {
            viewHolder = new PolicyHolder();
            convertView = View.inflate(context, R.layout.adapter_policy_group, null);
            viewHolder.mGroupLayout = (RelativeLayout) convertView.findViewById(R.id.mGroupLayout);
            viewHolder.mPolicyGroupLabel = (TextView) convertView.findViewById(R.id.mPolicyGroupLabel);
            viewHolder.mGroupArr = (ImageView) convertView.findViewById(R.id.mGroupArr);
            viewHolder.mChildLayout = (FrameLayout) convertView.findViewById(R.id.mChildLayout);
            viewHolder.mChildListView = (ListView) convertView.findViewById(R.id.mChildListView);
            viewHolder.mCheckMoreAction = (TextView) convertView.findViewById(R.id.mCheckMoreAction);
            viewHolder.mLoadingView = convertView.findViewById(R.id.mLoadingView);

            final View finalConvertView = convertView;
            final int groupPosition = i;
            viewHolder.mGroupLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onGroupItemClick(groupPosition, finalConvertView);
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PolicyHolder) convertView.getTag();
        }
        PolicyDataModel.PolicyItem item = getDataLists().get(i);
        viewHolder.mPolicyGroupLabel.setText(item.title);

        return convertView;
    }

    public class PolicyHolder {
        RelativeLayout mGroupLayout;
        public TextView mPolicyGroupLabel;
        public ImageView mGroupArr;
        public FrameLayout mChildLayout;
        public ListView mChildListView;
        public TextView mCheckMoreAction;
        public View mLoadingView;
    }

    public interface OnGroupItemClickListener {
        void onGroupItemClick(int groupPosition, View view);
    }
}
