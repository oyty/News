package com.oyty.base;

import android.content.Context;

import com.oyty.news.R;
import com.oyty.ui.widget.pulltorefresh.PullToRefreshListView;
import com.oyty.utils.CommonUtil;

/**
 * Created by oyty on 5/11/15.
 */
public abstract class BaseListView extends BasePager {

    public PullToRefreshListView ptrlv;

    public BaseListView(Context context) {
        super(context);
        ptrlv = (PullToRefreshListView) getRootView().findViewById(R.id.ptrlv);
        init();
    }

    private void init() {
        // 设置上拉加载不可用（国内一般是滚动到最后一个条目，加载更多）
        ptrlv.setPullLoadEnabled(false);
        // 滑动到底部是否自动加载更多数据
        ptrlv.setScrollLoadEnabled(true);
        // 设置最后更新的时间文本
        ptrlv.setLastUpdatedLabel(CommonUtil.getStringDate());

        initListView();
    }

    protected abstract void initListView();

    /**
     * 设置下拉刷新和滚动加载更多完成的方法
     */
    public void setRefreshCompltete() {

        ptrlv.onPullDownRefreshComplete();
        ptrlv.onPullUpRefreshComplete();
        ptrlv.setLastUpdatedLabel(CommonUtil.getStringDate());
    }


}
