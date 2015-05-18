package com.oyty.newscenter;

import android.content.Context;

import com.oyty.bean.TopicDetailBean;
import com.oyty.net.controller.OnNetworkDataCallBack;
import com.oyty.net.controller.Response;
import com.oyty.net.controller.VolleyNetworkWrapper;
import com.oyty.utils.GsonUtils;

/**
 * Created by oyty on 5/11/15.
 */
public class TopicDetailNetHelper extends VolleyNetworkWrapper {

    private String mUrl;

    private OnNetworkDataCallBack<TopicDetailBean> mOnNetworkDataCallBack;

    public TopicDetailNetHelper(Context context) {
        super(context);
    }

    @Override
    public String initUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public void setOnResponseListener(OnNetworkDataCallBack<TopicDetailBean> callBack) {
        this.mOnNetworkDataCallBack = callBack;
    }

    @Override
    public void requestDataSuccess(Response response) {
        mOnNetworkDataCallBack.onNetworkDataSuccess(GsonUtils.json2Bean(response.data, TopicDetailBean.class));
    }

    @Override
    public void requestDataFail(String message) {
        mOnNetworkDataCallBack.onNetworkDataFailed(message);
    }
}
