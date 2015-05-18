package com.oyty.ui.pager;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.oyty.base.BasePager;
import com.oyty.base.Constants;
import com.oyty.bean.ServiceDataModel;
import com.oyty.bean.SmartServiceBean;
import com.oyty.net.controller.OnNetworkDataCallBack;
import com.oyty.net.helper.SmartServiceNetHelper;
import com.oyty.news.R;
import com.oyty.ui.activity.MainActivity;
import com.oyty.ui.activity.NewsDetailActivity;
import com.oyty.ui.adapter.ServiceDataAdapter;
import com.oyty.utils.GsonUtils;
import com.oyty.utils.LogUtil;
import com.oyty.utils.SPUtils;
import com.oyty.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 智慧服务
 */
public class SmartServicePager extends BasePager implements AdapterView.OnItemClickListener {

    private static final String LOG_TAG = SmartServicePager.class.getSimpleName();

    /* 智慧服务菜单条目的title */
    private List<String> mMenuData = new ArrayList<String>();

    private List<SmartServiceBean.DataItem> mDataItemLists;

    private GridView mGridView;
    private ServiceDataAdapter mAdapter;

    private int mCurrentPosition = 0;

	public SmartServicePager(Context context) {
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
		View view = View.inflate(context, R.layout.frame_smart_service, null);
        mGridView = (GridView) view.findViewById(R.id.mGridView);
		return view;
	}

	@Override
	public void initData() {
        String result = SPUtils.getString(context, Constants.preferences.SMART_SERVICE_CATEGORIES, "");

        if (!TextUtils.isEmpty(result)) {
            LogUtil.getLogger().i(LOG_TAG, "拿缓存数据");

            processData(GsonUtils.json2Bean(result, SmartServiceBean.class));
        }
        // 更新数据可以主动请求
        getSmartServiceData();
	}

    private void getSmartServiceData() {
        SmartServiceNetHelper netHelper = new SmartServiceNetHelper(context);
        netHelper.setOnResponseListener(new OnNetworkDataCallBack<SmartServiceBean>() {
            @Override
            public void onNetworkDataSuccess(SmartServiceBean result) {
                processData(result);

                SPUtils.putString(context, Constants.preferences.SMART_SERVICE_CATEGORIES, GsonUtils.bean2Json(result));
            }

            @Override
            public void onNetworkDataFailed(String message) {
                ToastUtil.showToast(context, message);
            }
        });
        netHelper.execute();
    }

    private void processData(SmartServiceBean smartServiceBean) {
        mLoadingView.setVisibility(View.GONE);

        initSmartServiceMenu(smartServiceBean);
        initSmartServiceData(smartServiceBean);

        // 默认现实第一条菜单条目对应数据
        switchContentLayout(0);

    }

    private void initSmartServiceData(SmartServiceBean smartServiceBean) {
        mDataItemLists = smartServiceBean.data;
        if(mAdapter == null) {
            mAdapter = new ServiceDataAdapter(context, mDataItemLists.get(mCurrentPosition).items);
            mGridView.setAdapter(mAdapter);
            mGridView.setOnItemClickListener(this);
        }
    }

    private void initSmartServiceMenu(SmartServiceBean smartServiceBean) {
        // 封装菜单title数据到MenuFragment页面显示
        mMenuData.clear();
        for (SmartServiceBean.DataItem item : smartServiceBean.data) {
            mMenuData.add(item.title);
        }

        ((MainActivity) context).getMenuFragment().initMenuData(mMenuData);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        ServiceDataModel dataItem = mAdapter.getDataLists().get(position);
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra(Constants.NEWS_DETAIL_URL, dataItem.url);
        intent.putExtra(Constants.GRID_TOOLS_TITLE, dataItem.title);
        context.startActivity(intent);
    }

    @Override
    public void switchContentLayout(int position) {
        mAdapter.setDataLists(mDataItemLists.get(position).items);
        mAdapter.notifyDataSetChanged();

        mCurrentPosition = position;
        initTitleBar();
    }
}
