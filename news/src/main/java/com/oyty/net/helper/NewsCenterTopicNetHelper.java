package com.oyty.net.helper;

import android.content.Context;

import com.oyty.bean.NewsCenterTopicBean;
import com.oyty.net.controller.OnNetworkDataCallBack;
import com.oyty.net.controller.Response;
import com.oyty.net.controller.VolleyNetworkWrapper;
import com.oyty.utils.GsonUtils;

/**
 * Created by oyty on 5/11/15.
 */
public class NewsCenterTopicNetHelper extends VolleyNetworkWrapper {
    private String mUrl;
    private OnNetworkDataCallBack<NewsCenterTopicBean> mOnNetworkDataCallBack;

    public NewsCenterTopicNetHelper(Context context) {
        super(context);
    }

    @Override
    public String initUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public void setOnResponseListener (OnNetworkDataCallBack<NewsCenterTopicBean> callBack) {
        this.mOnNetworkDataCallBack = callBack;
    }

    @Override
    public void requestDataSuccess(Response response) {
        mOnNetworkDataCallBack.onNetworkDataSuccess(GsonUtils.json2Bean(response.data, NewsCenterTopicBean.class));
    }

    @Override
    public void requestDataFail(String message) {
        mOnNetworkDataCallBack.onNetworkDataFailed(message);
    }
}
