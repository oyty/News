package com.oyty.newscenter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.oyty.base.BaseListView;
import com.oyty.base.Constants;
import com.oyty.bean.NewsCenterTopicBean;
import com.oyty.net.controller.OnNetworkDataCallBack;
import com.oyty.net.helper.NewsCenterTopicNetHelper;
import com.oyty.news.R;
import com.oyty.ui.activity.TopicDetailActivity;
import com.oyty.ui.adapter.NewsCenterTopicAdapter;
import com.oyty.ui.widget.pulltorefresh.PullToRefreshBase;
import com.oyty.utils.AppUtil;
import com.oyty.utils.GsonUtils;
import com.oyty.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class TopicPager extends BaseListView {

    private String moreUrl;
    private List<NewsCenterTopicBean.TopicNews> topicLists = new ArrayList<NewsCenterTopicBean.TopicNews>();
    private NewsCenterTopicAdapter mAdapter;

	public TopicPager(Context context) {
		super(context);
	}

    @Override
    protected void initListView() {
        ptrlv.getRefreshableView().setDividerHeight(0);

        //3-设置刷新的监听器
        ptrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            //下拉松手后会被调用
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                //下拉刷新，重新请求最新的数据
                getNewsCenterTopicData(AppUtil.NEWS_CENTER_TOPIC, true);
            }

            //加载更多时会被调用或上拉时调用
            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                //上拉加载更多，请求的是更多的数据
                getNewsCenterTopicData(moreUrl, false);
            }
        });

        ptrlv.getRefreshableView().setOnItemClickListener(new MyOnItemClickListener());
    }

    private final class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            NewsCenterTopicBean.TopicNews news = null;
            //要先判断是否有加头view
            if (ptrlv.getRefreshableView().getHeaderViewsCount() < 1) {
                news = topicLists.get(position);
            } else {
                news = topicLists.get(position - 1);
            }

            //点击每一个新闻条目后，进入到新闻详情页面
            Intent intent = new Intent(context, TopicDetailActivity.class);
            intent.putExtra(Constants.NEWS_DETAIL_URL, news.url);
            intent.putExtra(Constants.TOPIC_NEWS_TITLE, news.title);
            context.startActivity(intent);
        }

    }

    @Override
	public View initView() {
		View view = View.inflate(context, R.layout.frame_topic, null);
        return view;
	}

	@Override
	public void initData() {
        String newsCenterTopic = mCacheDao.getCache(Constants.Preferences.NEWS_CENTER_TOPIC);

        if(!TextUtils.isEmpty(newsCenterTopic)) {
            processData(GsonUtils.json2Bean(newsCenterTopic, NewsCenterTopicBean.class), true);
        }
        // 主动刷新
        getNewsCenterTopicData(AppUtil.NEWS_CENTER_TOPIC, true);
	}

    private void getNewsCenterTopicData(String url, final boolean isReferesh) {
        NewsCenterTopicNetHelper netHelper = new NewsCenterTopicNetHelper(context);
        netHelper.setUrl(url);
        netHelper.setOnResponseListener(new OnNetworkDataCallBack<NewsCenterTopicBean>() {
            @Override
            public void onNetworkDataSuccess(NewsCenterTopicBean result) {
                processData(result, isReferesh);

                mCacheDao.putCache(Constants.Preferences.NEWS_CENTER_TOPIC, GsonUtils.bean2Json(result));
            }

            @Override
            public void onNetworkDataFailed(String message) {
                ToastUtil.showToast(context, message);
            }
        });
        netHelper.execute();
    }

    private void processData(NewsCenterTopicBean result, boolean isRefresh) {
        moreUrl = result.more;
        if (!TextUtils.isEmpty(moreUrl)) {
            ptrlv.setHasMoreData(true);
        } else {
            ptrlv.setHasMoreData(false);
        }

        //对news里面的数据进行封装
        if (result.topic != null) {
            //如果是刷新的话，清空数据，添加最新数据，滚动加载更多的话，直接添加数据
            if (isRefresh) {
                topicLists.clear();
                topicLists.addAll(result.topic);
            } else {
                //添加的仅仅是更多的数据moreUrl
                topicLists.addAll(result.topic);
            }
        }
        if (mAdapter == null) {
            mAdapter = new NewsCenterTopicAdapter(context, topicLists);
            ptrlv.getRefreshableView().setAdapter(mAdapter);
        } else {
            mAdapter.setDataLists(topicLists);
            mAdapter.notifyDataSetChanged();
        }

        //设置下拉刷新和滚动加载更多完成的方法
        setRefreshCompltete();
    }

}
