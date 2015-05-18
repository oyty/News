package com.oyty.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.oyty.base.BaseActivity;
import com.oyty.base.Constants;
import com.oyty.bean.PolicyChildDataBean;
import com.oyty.net.controller.OnNetworkDataCallBack;
import com.oyty.net.helper.PolicyChildNetHelper;
import com.oyty.news.R;
import com.oyty.ui.adapter.PolicyChildAdapter;
import com.oyty.ui.widget.pulltorefresh.PullToRefreshBase;
import com.oyty.ui.widget.pulltorefresh.PullToRefreshListView;
import com.oyty.utils.CommonUtil;
import com.oyty.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class PolicyMoreActivity extends BaseActivity {

    private View mLoadingView;
    private PullToRefreshListView ptrlv;
    private PolicyChildAdapter mAdapter;

    private String moreUrl;
    private String mUrl;

    private List<PolicyChildDataBean.PolicyChildItem> pcLists = new ArrayList<>();

    @Override
    protected void initTitleAction() {
        mLeftBackAction.setVisibility(View.VISIBLE);
        mLeftBackAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleLabel.setText("更多政务");
    }

    @Override
    public int initLayoutID() {
        return R.layout.activity_policy_more;
    }

    @Override
    protected void initView() {
        mLoadingView = findViewById(R.id.mLoadingView);
        ptrlv = (PullToRefreshListView) findViewById(R.id.ptrlv);
        initListView();
    }

    @Override
    protected void initViewLintener() {

    }

    protected void initListView() {
        // 设置上拉加载不可用（国内一般是滚动到最后一个条目，加载更多）
        ptrlv.setPullLoadEnabled(true);
        // 滑动到底部是否自动加载更多数据
        ptrlv.setScrollLoadEnabled(false);
        // 设置最后更新的时间文本
        ptrlv.setLastUpdatedLabel(CommonUtil.getStringDate());

        //3-设置刷新的监听器
        ptrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            //下拉松手后会被调用
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                //下拉刷新，重新请求最新的数据
                getPolicyMoreData(mUrl, true);
            }

            //加载更多时会被调用或上拉时调用
            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                //上拉加载更多，请求的是更多的数据
                getPolicyMoreData(moreUrl, false);
            }
        });

        ptrlv.getRefreshableView().setOnItemClickListener(new MyOnItemClickListener());
    }

    private final class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            PolicyChildDataBean.PolicyChildItem item = pcLists.get(position);

            //点击每一个新闻条目后，进入到新闻详情页面
            Intent intent = new Intent(mContext, NewsDetailActivity.class);
            intent.putExtra(Constants.NEWS_DETAIL_URL, item.url);
            mContext.startActivity(intent);
        }

    }

    @Override
    protected void process() {
        Intent intent = getIntent();
        if(intent != null) {
            mUrl = intent.getStringExtra(Constants.NEWS_DETAIL_URL);
            if(!TextUtils.isEmpty(mUrl)) {
                getPolicyMoreData(mUrl, true);
            }
        }
    }

    private void getPolicyMoreData(String url, final boolean isRefresh) {
        PolicyChildNetHelper netHelper = new PolicyChildNetHelper(mContext);
        netHelper.setUrl(url);
        netHelper.setOnResponseistener(new OnNetworkDataCallBack<PolicyChildDataBean>() {
            @Override
            public void onNetworkDataSuccess(PolicyChildDataBean result) {
                processChildData(result, isRefresh);
            }

            @Override
            public void onNetworkDataFailed(String message) {
                ToastUtil.showToast(mContext, message);
            }
        });
        netHelper.execute();
    }

    private void processChildData(PolicyChildDataBean result, boolean isRefresh) {
        mLoadingView.setVisibility(View.GONE);
        moreUrl = result.more;
        if (!TextUtils.isEmpty(moreUrl)) {
            ptrlv.setHasMoreData(true);
        } else {
            ptrlv.setHasMoreData(false);
        }

        //对news里面的数据进行封装
        if (result.policy != null) {
            //如果是刷新的话，清空数据，添加最新数据，滚动加载更多的话，直接添加数据
            if (isRefresh) {
                pcLists.clear();
                pcLists.addAll(result.policy);
            } else {
                //添加的仅仅是更多的数据moreUrl
                pcLists.addAll(result.policy);
            }
        }
        if (mAdapter == null) {
            mAdapter = new PolicyChildAdapter(mContext, pcLists);
            ptrlv.getRefreshableView().setAdapter(mAdapter);
        } else {
            mAdapter.setDataLists(pcLists);
            mAdapter.notifyDataSetChanged();
        }

        //设置下拉刷新和滚动加载更多完成的方法
        setRefreshCompltete();

    }

    /**
     * 设置下拉刷新和滚动加载更多完成的方法
     */
    public void setRefreshCompltete() {

        ptrlv.onPullDownRefreshComplete();
        ptrlv.onPullUpRefreshComplete();
        ptrlv.setLastUpdatedLabel(CommonUtil.getStringDate());
    }

}
