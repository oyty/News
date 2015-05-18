package com.oyty.net.helper;

import android.content.Context;

import com.oyty.bean.HomeSliderBean;
import com.oyty.net.controller.OnNetworkDataCallBack;
import com.oyty.net.controller.Response;
import com.oyty.net.controller.VolleyNetworkWrapper;
import com.oyty.utils.AppUtil;
import com.oyty.utils.GsonUtils;

/**
 * 获取首页轮播图数据
 *
 * Created by oyty on 5/9/15.
 */
public class HomeSliderNetHelper extends VolleyNetworkWrapper {

    private OnNetworkDataCallBack<HomeSliderBean> mOnNetworkDataCallBack;

    public HomeSliderNetHelper(Context context) {
        super(context);
    }

    @Override
    public String initUrl() {
        return AppUtil.HOME_SLIDER;
    }

    public void setOnResponseListener(OnNetworkDataCallBack<HomeSliderBean> callBack) {
        this.mOnNetworkDataCallBack = callBack;
    }

    @Override
    public void requestDataSuccess(Response response) {
        mOnNetworkDataCallBack.onNetworkDataSuccess(GsonUtils.json2Bean(response.result, HomeSliderBean.class));
    }

    @Override
    public void requestDataFail(String message) {
        mOnNetworkDataCallBack.onNetworkDataFailed(message);
    }
}
