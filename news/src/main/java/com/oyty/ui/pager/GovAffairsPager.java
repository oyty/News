package com.oyty.ui.pager;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.oyty.base.BasePager;
import com.oyty.base.Constants;
import com.oyty.bean.PolicyChildDataBean;
import com.oyty.bean.PolicyDataModel;
import com.oyty.net.controller.OnNetworkDataCallBack;
import com.oyty.net.helper.PolicyChildNetHelper;
import com.oyty.net.helper.PolicyNewsNetHelper;
import com.oyty.news.R;
import com.oyty.ui.activity.MainActivity;
import com.oyty.ui.activity.NewsDetailActivity;
import com.oyty.ui.activity.PolicyMoreActivity;
import com.oyty.ui.adapter.PolicyAdapter;
import com.oyty.ui.adapter.PolicyChildAdapter;
import com.oyty.utils.GsonUtils;
import com.oyty.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class GovAffairsPager extends BasePager implements AdapterView.OnItemClickListener, View.OnClickListener {

	private ListView mListView;
	private PolicyAdapter mAdapter;

    private PolicyChildAdapter mChildAdapter;

    private PolicyAdapter.PolicyHolder viewHolder;

    private PolicyAdapter.OnGroupItemClickListener listener;

    /* 政务指南菜单条目的title */
    private List<String> mMenuData = new ArrayList<String>();
    private List<PolicyDataModel> policyLists = new ArrayList<PolicyDataModel>();
    private List<PolicyChildDataBean.PolicyChildItem> childLists = new ArrayList<PolicyChildDataBean.PolicyChildItem>();

    private int mCurrentPosition = 0;
    private int mGroupPosition = 0;

	public GovAffairsPager(Context context) {
		super(context);
	}

    @Override
    protected void initTitleBar() {
        mTitleLabel.setText(mMenuData.get(mCurrentPosition));
        mLeftAction.setVisibility(View.VISIBLE);
        mLeftAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSM.toggle();
            }
        });
    }

    @Override
	public View initView() {
		View view = View.inflate(context, R.layout.frame_policy, null);
        mListView = (ListView) view.findViewById(R.id.mListView);
		initViewListener();
		return view;
	}

    // GroupItem的回调
	private void initViewListener() {
        listener = new PolicyAdapter.OnGroupItemClickListener() {
            @Override
            public void onGroupItemClick(int groupPosition, View view) {
                viewHolder = (PolicyAdapter.PolicyHolder) view.getTag();

                if(viewHolder.mChildLayout.getVisibility() == View.VISIBLE) {
                    viewHolder.mChildLayout.setVisibility(View.GONE);
                    viewHolder.mGroupArr.setBackgroundResource(R.drawable.news_cate_arr);
                } else {
                    viewHolder.mChildLayout.setVisibility(View.VISIBLE);
                    viewHolder.mGroupArr.setBackgroundResource(R.drawable.gov_affair_arr_bottom);
                }
                mGroupPosition = groupPosition;

                String result = mCacheDao.getCache(policyLists.get(mCurrentPosition).children.get(groupPosition).url);
                if(!TextUtils.isEmpty(result)) {
                    processChildData(GsonUtils.json2Bean(result, PolicyChildDataBean.class).policy);
                }

                getPolicyChildData(policyLists.get(mCurrentPosition).children.get(groupPosition).url);
            }
        };
	}

    /**
     * 设置子listview的高度
     */
    public void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if(listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 新闻条目点击
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra(Constants.NEWS_DETAIL_URL, childLists.get(i).url);
        context.startActivity(intent);
    }

    /**
     * 查看更多
     */
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, PolicyMoreActivity.class);
        intent.putExtra(Constants.NEWS_DETAIL_URL, policyLists.get(mCurrentPosition).children.get(mGroupPosition).url);
        context.startActivity(intent);
    }

    private void getPolicyChildData(final String url) {
        PolicyChildNetHelper netHelper = new PolicyChildNetHelper(context);
        netHelper.setUrl(url);
        netHelper.setOnResponseistener(new OnNetworkDataCallBack<PolicyChildDataBean>() {
            @Override
            public void onNetworkDataSuccess(PolicyChildDataBean result) {
                processChildData(result.policy);

                mCacheDao.putCache(url, GsonUtils.bean2Json(result));
            }

            @Override
            public void onNetworkDataFailed(String message) {
                ToastUtil.showToast(context, message);
            }
        });
        netHelper.execute();
    }

    private void processChildData(List<PolicyChildDataBean.PolicyChildItem> result) {
        viewHolder.mLoadingView.setVisibility(View.GONE);
        childLists.clear();
        childLists.addAll(result);


        initPolicyChildData();
    }

    private void initPolicyChildData() {
        if(mChildAdapter == null) {
            mChildAdapter = new PolicyChildAdapter(context, childLists);
        } else {
            mChildAdapter.setDataLists(childLists);
        }
        viewHolder.mChildListView.setAdapter(mChildAdapter);
        viewHolder.mChildListView.setOnItemClickListener(this);
        viewHolder.mCheckMoreAction.setOnClickListener(this);
        setListViewHeight(viewHolder.mChildListView);
    }

    @Override
	public void initData() {
		String policyStr = mCacheDao.getCache(Constants.Preferences.POLICY_NEWS);

		if(!TextUtils.isEmpty(policyStr)) {
			processData(GsonUtils.json2Array(policyStr, new TypeToken<List<PolicyDataModel>>(){}));
		}

		getPolicyData();
	}

	private void getPolicyData() {
		PolicyNewsNetHelper netHelper = new PolicyNewsNetHelper(context);
		netHelper.setOnResponseListener(new OnNetworkDataCallBack<List<PolicyDataModel>>() {
			@Override
			public void onNetworkDataSuccess(List<PolicyDataModel> result) {
				processData(result);

                mCacheDao.putCache(Constants.Preferences.POLICY_NEWS, GsonUtils.array2Json(result));
			}

			@Override
			public void onNetworkDataFailed(String message) {
				ToastUtil.showToast(context, message);
			}
		});
		netHelper.execute();
	}

	private void processData(List<PolicyDataModel> policyDataModels) {

        mLoadingView.setVisibility(View.GONE);
        policyLists.clear();;
        policyLists.addAll(policyDataModels);

        initPolicyMenu();
        initPolicyData();

        switchContentLayout(0);

	}

    private void initPolicyData() {
        if(mAdapter == null) {
            mAdapter = new PolicyAdapter(context, policyLists.get(mCurrentPosition).children, listener);
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.setDataLists(policyLists.get(mCurrentPosition).children);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initPolicyMenu() {
        // 封装菜单title数据到MenuFragment页面显示
        mMenuData.clear();
        for (PolicyDataModel model : policyLists) {
            mMenuData.add(model.title);
        }

        ((MainActivity) context).getMenuFragment().initMenuData(mMenuData);
    }

    @Override
    public void switchContentLayout(int position) {
        mAdapter = new PolicyAdapter(context, policyLists.get(position).children, listener);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        mCurrentPosition = position;
        initTitleBar();

    }

}
