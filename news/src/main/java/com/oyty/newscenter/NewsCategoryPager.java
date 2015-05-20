package com.oyty.newscenter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.oyty.base.BasePager;
import com.oyty.base.Constants;
import com.oyty.bean.NewsCategoryBean;
import com.oyty.bean.NewsCategoryBean.News;
import com.oyty.bean.NewsCategoryBean.TopNews;
import com.oyty.net.controller.OnNetworkDataCallBack;
import com.oyty.net.helper.NewsCategoryNetHelper;
import com.oyty.news.R;
import com.oyty.ui.activity.NewsDetailActivity;
import com.oyty.ui.adapter.NewsItemAdapter;
import com.oyty.ui.widget.pulltorefresh.PullToRefreshBase;
import com.oyty.ui.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.oyty.ui.widget.pulltorefresh.PullToRefreshListView;
import com.oyty.ui.widget.sliderimage.SliderLayout;
import com.oyty.ui.widget.sliderimage.SliderTypes.BaseSliderView;
import com.oyty.ui.widget.sliderimage.SliderTypes.TextSliderView;
import com.oyty.utils.CommonUtil;
import com.oyty.utils.GsonUtils;
import com.oyty.utils.LogUtil;
import com.oyty.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 新闻中心，侧滑菜单新闻，每一个指针对应界面
 * 主要是一个PullToRefreshListView框架，讲轮播图加入到header view里面
 */
public class NewsCategoryPager extends BasePager implements BaseSliderView.OnSliderClickListener {

    private static final String LOG_TAG = NewsCategoryPager.class.getSimpleName();
    private static final String URL_EXTRA = "url_extra";


    private String url;
    private PullToRefreshListView ptrlv;

    //添加RollViewPager的容器
    private SliderLayout mSliderLayout;

    //这个是整个的滚动viewpager的顶层视图
    private View mRollView;

    private NewsItemAdapter mAdapter;
    private String moreUrl;


    private List<News> newsLists = new ArrayList<NewsCategoryBean.News>();

    public NewsCategoryPager(Context context, String url) {
        super(context);
        this.url = url;
        initData();
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.adapter_item_news, null);
        ptrlv = (PullToRefreshListView) view.findViewById(R.id.lv_item_news);

        mRollView = View.inflate(context, R.layout.layout_roll_view, null);
        mSliderLayout = (SliderLayout) mRollView.findViewById(R.id.slider);

        initListView();

        return view;
    }

    private void initListView() {
        //1-设置上拉加载不可用（国内一般是滚动到最后一个条目，加载更多）
        ptrlv.setPullLoadEnabled(false);
        //2-滑动到底部是否自动加载更多数据
        ptrlv.setScrollLoadEnabled(true);

        //3-设置刷新的监听器
        ptrlv.setOnRefreshListener(new OnRefreshListener<ListView>() {

            //下拉松手后会被调用
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                //下拉刷新，重新请求最新的数据
                getNewsCategoryData(url, true);
            }

            //加载更多时会被调用或上拉时调用
            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                //上拉加载更多，请求的是更多的数据
                getNewsCategoryData(moreUrl, false);
            }
        });

        ptrlv.getRefreshableView().setOnItemClickListener(new MyOnItemClickListener());
        //8-设置最后更新的时间文本
        ptrlv.setLastUpdatedLabel(CommonUtil.getStringDate());
    }

    /**
     * 每一个新闻条目点击的时候，要跳转到新闻详情页面，并且阅读过的新闻要做好字体颜色的标记
     *
     * @author Administrator
     */
    private final class MyOnItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            News news = null;
            //要先判断是否有加头view
            if (ptrlv.getRefreshableView().getHeaderViewsCount() < 1) {
                news = newsLists.get(position);
            } else {
                news = newsLists.get(position - 1);
            }
            if (!news.is_read) {
                news.is_read = true;
            }
            mAdapter.notifyDataSetChanged();

            //点击每一个新闻条目后，进入到新闻详情页面
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra(Constants.NEWS_DETAIL_URL, news.url);
            context.startActivity(intent);
        }

    }

    @Override
    public void initData() {
        String cache = mCacheDao.getCache(url);
        if(!TextUtils.isEmpty(cache)) {
            processData(GsonUtils.json2Bean(cache, NewsCategoryBean.class), true);
        }

        //传递过过来的是url，所以先请求网络数据
        getNewsCategoryData(url, true);
    }

    private void getNewsCategoryData(final String url, final boolean is_refresh) {
        NewsCategoryNetHelper netHelper = new NewsCategoryNetHelper(context);
        netHelper.setUrl(url);
        netHelper.setOnResponseListener(new OnNetworkDataCallBack<NewsCategoryBean>() {
            @Override
            public void onNetworkDataSuccess(NewsCategoryBean result) {
                LogUtil.getLogger().i(LOG_TAG, result);

                processData(result, is_refresh);

                mCacheDao.putCache(url, GsonUtils.bean2Json(result));
            }

            @Override
            public void onNetworkDataFailed(String message) {
                ToastUtil.showToast(context, message);
            }
        });
        netHelper.execute();
    }

    protected void processData(NewsCategoryBean ncBean, boolean is_refresh) {
        /**
         * 如果是下拉刷新的话，要更新viewpager轮播，如果是下拉加载，则viewpager就不需要更新
         * 而是直接将加载的更多的数据添加现有数据的后面
         */
        if (is_refresh) {
            initSliderImage(ncBean.data);
        }

        /**
         * 每次请求后获取moreUrl，判断是否还有更多数据，控制上拉加载更多
         */
        moreUrl = ncBean.data.more;
        if (!TextUtils.isEmpty(moreUrl)) {
            ptrlv.setHasMoreData(true);
        } else {
            ptrlv.setHasMoreData(false);
        }

        //对news里面的数据进行封装
        if (ncBean.data.news != null) {
            //如果是刷新的话，清空数据，添加最新数据，滚动加载更多的话，直接添加数据
            if (is_refresh) {
                newsLists.clear();
                newsLists.addAll(ncBean.data.news);
            } else {
                //添加的仅仅是更多的数据moreUrl
                newsLists.addAll(ncBean.data.news);
            }
        }
        if (mAdapter == null) {
            mAdapter = new NewsItemAdapter(context, newsLists);
            ptrlv.getRefreshableView().setAdapter(mAdapter);
        } else {
            mAdapter.setDataLists(newsLists);
            mAdapter.notifyDataSetChanged();
        }


        //设置下拉刷新和滚动加载更多完成的方法
        ptrlv.onPullDownRefreshComplete();
        ptrlv.onPullUpRefreshComplete();
        ptrlv.setLastUpdatedLabel(CommonUtil.getStringDate());
    }

    private void initSliderImage(NewsCategoryBean.Data data) {
        HashMap<String, String> url_maps = new HashMap<String, String>();
        for (TopNews topNews : data.topnews) {
            url_maps.put(topNews.title, topNews.topimage + "@" + topNews.url);
        }

        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(context);
            textSliderView
                    .description(name)
                    .image(url_maps.get(name).split("@")[0])
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(NewsCategoryPager.this);

            //add your extra information
            textSliderView.getBundle()
                    .putString(URL_EXTRA, url_maps.get(name).split("@")[1]);

            mSliderLayout.addSlider(textSliderView);
        }
        mSliderLayout.setDuration(4000);

        /**
         * 将rollViewPager添加到listview的header
         * HeaderView必须在setAdapter之前添加，并且如果不zetAdapter的话
         * header是不会显示的
         */
        if (ptrlv.getRefreshableView().getHeaderViewsCount() < 1) {
            ptrlv.getRefreshableView().addHeaderView(mRollView);
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra(Constants.NEWS_DETAIL_URL, slider.getBundle().getString(URL_EXTRA));
        context.startActivity(intent);
    }

}
