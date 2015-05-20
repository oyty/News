package com.oyty.ui.pager;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.reflect.TypeToken;
import com.oyty.base.BasePager;
import com.oyty.base.Constants;
import com.oyty.bean.HomeSliderBean;
import com.oyty.bean.NewsCategoryBean;
import com.oyty.bean.ServiceDataModel;
import com.oyty.net.controller.OnNetworkDataCallBack;
import com.oyty.net.helper.HomeGridNetHelper;
import com.oyty.net.helper.HomeSliderNetHelper;
import com.oyty.news.R;
import com.oyty.ui.activity.NewsDetailActivity;
import com.oyty.ui.adapter.ServiceDataAdapter;
import com.oyty.ui.widget.sliderimage.SliderLayout;
import com.oyty.ui.widget.sliderimage.SliderTypes.BaseSliderView;
import com.oyty.ui.widget.sliderimage.SliderTypes.TextSliderView;
import com.oyty.utils.GsonUtils;
import com.oyty.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;

public class HomePager extends BasePager implements BaseSliderView.OnSliderClickListener, AdapterView.OnItemClickListener {

    private static final String URL_EXTRA = "url_extra";

    private SliderLayout mSliderLayout;
    private GridView mGridView;
    private ServiceDataAdapter mAdapter;


    public HomePager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.frame_home, null);
        mSliderLayout = (SliderLayout) view.findViewById(R.id.mSliderLayout);
        mGridView = (GridView) view.findViewById(R.id.mGridView);
        return view;
    }

    /**
     * 初始化首页titlebar
     *
     */
    @Override
    protected void initTitleBar() {
        mTitleLabel.setText(R.string.smart_beijing);
    }


    @Override
    public void initData() {
        initTitleBar();

        String sliderNewsStr = mCacheDao.getCache(Constants.Preferences.HOME_SLIDER_NEWS);
        String gridNewsStr = mCacheDao.getCache(Constants.Preferences.HOME_GRID_NEWS);

        if (!TextUtils.isEmpty(sliderNewsStr)) {
            initSliderImage(GsonUtils.json2Bean(sliderNewsStr, HomeSliderBean.class).data);
        }
        if (!TextUtils.isEmpty(gridNewsStr)) {

            initGridView(GsonUtils.json2Array(gridNewsStr, new TypeToken<List<ServiceDataModel>>(){}));
        }

        requestHomeSliderData();
        requestHomeGridData();
    }

    /**
     * 请求首页轮播图数据
     */
    private void requestHomeSliderData() {
        HomeSliderNetHelper netHelper = new HomeSliderNetHelper(context);
        netHelper.setOnResponseListener(new OnNetworkDataCallBack<HomeSliderBean>() {
            @Override
            public void onNetworkDataSuccess(HomeSliderBean result) {
                initSliderImage(result.data);

                mCacheDao.putCache(Constants.Preferences.HOME_SLIDER_NEWS, GsonUtils.bean2Json(result));
            }

            @Override
            public void onNetworkDataFailed(String message) {
                ToastUtil.showToast(context, message);
            }
        });
        netHelper.execute();
    }

    private void initSliderImage(HomeSliderBean.HomeData data) {
        mLoadingView.setVisibility(View.GONE);
        mSliderLayout.removeAllSliders();
        HashMap<String, String> url_maps = new HashMap<String, String>();
        for (NewsCategoryBean.TopNews topNews : data.topnews) {
            url_maps.put(topNews.title, topNews.topimage + "@" + topNews.url);
        }

        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(context);
            textSliderView
                    .description(name)
                    .image(url_maps.get(name).split("@")[0])
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(HomePager.this);

            //add your extra information
            textSliderView.getBundle()
                    .putString(URL_EXTRA, url_maps.get(name).split("@")[1]);

            mSliderLayout.addSlider(textSliderView);
        }
        mSliderLayout.setDuration(4000);

    }

    /**
     * 请求首页gridview数据
     */
    private void requestHomeGridData() {
        HomeGridNetHelper netHelper = new HomeGridNetHelper(context);
        netHelper.setOnResponseListener(new OnNetworkDataCallBack<List<ServiceDataModel>>() {
            @Override
            public void onNetworkDataSuccess(List<ServiceDataModel> result) {
                initGridView(result);

                mCacheDao.putCache(Constants.Preferences.HOME_GRID_NEWS, GsonUtils.bean2Json(result));
            }

            @Override
            public void onNetworkDataFailed(String message) {
                ToastUtil.showToast(context, message);
            }
        });
        netHelper.execute();
    }

    private void initGridView(List<ServiceDataModel> result) {
        mLoadingView.setVisibility(View.GONE);
        if (mAdapter == null) {
            mAdapter = new ServiceDataAdapter(context, result);
            mGridView.setAdapter(mAdapter);
            mGridView.setOnItemClickListener(this);
        } else {
            mAdapter.setDataLists(result);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ServiceDataModel dataItem = mAdapter.getDataLists().get(i);
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra(Constants.NEWS_DETAIL_URL, dataItem.url);
        intent.putExtra(Constants.GRID_TOOLS_TITLE, dataItem.title);
        context.startActivity(intent);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra(Constants.NEWS_DETAIL_URL, slider.getBundle().getString(URL_EXTRA));
        context.startActivity(intent);
    }

}
