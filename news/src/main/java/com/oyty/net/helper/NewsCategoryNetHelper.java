package com.oyty.net.helper;

import android.content.Context;

import com.oyty.bean.NewsCategoryBean;
import com.oyty.net.controller.OnNetworkDataCallBack;
import com.oyty.net.controller.Response;
import com.oyty.net.controller.VolleyNetworkWrapper;
import com.oyty.utils.GsonUtils;

/**
 * 获取新闻中心侧滑菜单条目数据
 *
 * Created by oyty on 5/8/15.
 */
public class NewsCategoryNetHelper extends VolleyNetworkWrapper {
    private String url;
    private OnNetworkDataCallBack<NewsCategoryBean> mOnNetworkDataCallBack;

    public NewsCategoryNetHelper(Context context) {
        super(context);
    }

    @Override
    public String initUrl() {
        return url;
    }

    public void setUrl(String  url) {
        this.url = url;
    }

    public void setOnResponseListener(OnNetworkDataCallBack<NewsCategoryBean> callBack) {
        this.mOnNetworkDataCallBack = callBack;
    }

    @Override
    public void requestDataSuccess(Response response) {
        mOnNetworkDataCallBack.onNetworkDataSuccess(GsonUtils.json2Bean(response.result, NewsCategoryBean.class));
    }

    @Override
    public void requestDataFail(String message) {
        mOnNetworkDataCallBack.onNetworkDataFailed(message);
    }
}
