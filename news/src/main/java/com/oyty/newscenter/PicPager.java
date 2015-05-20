package com.oyty.newscenter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.oyty.base.BaseListView;
import com.oyty.base.Constants;
import com.oyty.bean.NewsCenterPicBean;
import com.oyty.net.controller.OnNetworkDataCallBack;
import com.oyty.net.helper.NewsCenterPicNetHelper;
import com.oyty.news.R;
import com.oyty.ui.activity.NewsDetailActivity;
import com.oyty.ui.adapter.NewsCenterPicAdapter;
import com.oyty.ui.widget.pulltorefresh.PullToRefreshBase;
import com.oyty.utils.AppUtil;
import com.oyty.utils.GsonUtils;

import java.util.ArrayList;
import java.util.List;

public class PicPager extends BaseListView {

    private NewsCenterPicAdapter mAdapter;
    private List<NewsCenterPicBean.PicNews> picLists = new ArrayList<NewsCenterPicBean.PicNews>();

    private String moreUrl;


	public PicPager(Context context) {
		super(context);
        initData();
	}

	@Override
	public View initView() {
	    View view = View.inflate(context, R.layout.frame_pic, null);
        return view;
	}

    protected void initListView() {
        ptrlv.getRefreshableView().setDividerHeight(0);

        //3-设置刷新的监听器
        ptrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            //下拉松手后会被调用
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                //下拉刷新，重新请求最新的数据
                getNewsCenterPicData(AppUtil.NEWS_CENTER_PIC, true);
            }

            //加载更多时会被调用或上拉时调用
            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                //上拉加载更多，请求的是更多的数据
                getNewsCenterPicData(moreUrl, false);
            }
        });

        ptrlv.getRefreshableView().setOnItemClickListener(new MyOnItemClickListener());
    }

    private final class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            NewsCenterPicBean.PicNews news = null;
            //要先判断是否有加头view
            if (ptrlv.getRefreshableView().getHeaderViewsCount() < 1) {
                news = picLists.get(position);
            } else {
                news = picLists.get(position - 1);
            }

            //点击每一个新闻条目后，进入到新闻详情页面
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra(Constants.NEWS_DETAIL_URL, news.url);
            context.startActivity(intent);
        }

    }

	@Override
	public void initData() {
        String newsCenterPic = mCacheDao.getCache(Constants.Preferences.NEWS_CENTER_PIC);

        if(!TextUtils.isEmpty(newsCenterPic)) {
            processData(GsonUtils.json2Bean(newsCenterPic, NewsCenterPicBean.class), true);
        } else {
            // 主动刷新
            getNewsCenterPicData(AppUtil.NEWS_CENTER_PIC, true);
        }
	}

    private void getNewsCenterPicData(String url, final boolean isRefresh) {
        NewsCenterPicNetHelper netHelper = new NewsCenterPicNetHelper(context);
        netHelper.setUrl(url);
        netHelper.setOnResponseListener(new OnNetworkDataCallBack<NewsCenterPicBean>() {
            @Override
            public void onNetworkDataSuccess(NewsCenterPicBean result) {
                processData(result, isRefresh);

                mCacheDao.putCache(Constants.Preferences.NEWS_CENTER_PIC, GsonUtils.bean2Json(result));
            }

            @Override
            public void onNetworkDataFailed(String message) {

            }
        });
        netHelper.execute();
    }

    private void processData(NewsCenterPicBean result, boolean isRefresh) {
        moreUrl = result.more;
        if (!TextUtils.isEmpty(moreUrl)) {
            ptrlv.setHasMoreData(true);
        } else {
            ptrlv.setHasMoreData(false);
        }

        //对news里面的数据进行封装
        if (result.news != null) {
            //如果是刷新的话，清空数据，添加最新数据，滚动加载更多的话，直接添加数据
            if (isRefresh) {
                picLists.clear();
                picLists.addAll(result.news);
            } else {
                //添加的仅仅是更多的数据moreUrl
                picLists.addAll(result.news);
            }
        }
        if (mAdapter == null) {
            mAdapter = new NewsCenterPicAdapter(context, picLists);
            ptrlv.getRefreshableView().setAdapter(mAdapter);
        } else {
            mAdapter.setDataLists(picLists);
            mAdapter.notifyDataSetChanged();
        }

        //设置下拉刷新和滚动加载更多完成的方法
        setRefreshCompltete();
    }

}
