package com.oyty.net.helper;

import android.content.Context;

import com.oyty.bean.NewsCenterBean;
import com.oyty.net.controller.OnNetworkDataCallBack;
import com.oyty.net.controller.Response;
import com.oyty.net.controller.VolleyNetworkWrapper;
import com.oyty.utils.AppUtil;
import com.oyty.utils.GsonUtils;

/**
 * 获取新闻中心数据
 *
 * Created by oyty on 5/6/15.
 */
public class NewsCenterNetHelper extends VolleyNetworkWrapper {

    private OnNetworkDataCallBack<NewsCenterBean> mOnNetworkDataCallBack;

    public NewsCenterNetHelper(Context context) {
        super(context);
    }

    @Override
    public String initUrl() {
        return AppUtil.NEWS_CENTER_CATEGORIES;
    }


    public void setOnResponseListener(OnNetworkDataCallBack<NewsCenterBean> callBack) {
        this.mOnNetworkDataCallBack = callBack;
    }

    @Override
    public void requestDataSuccess(Response response) {
        NewsCenterBean newsCenterBean = GsonUtils.json2Bean(response.result, NewsCenterBean.class);
        
        mOnNetworkDataCallBack.onNetworkDataSuccess(newsCenterBean);
    }

    @Override
    public void requestDataFail(String message) {
        mOnNetworkDataCallBack.onNetworkDataFailed(message);
    }
}
