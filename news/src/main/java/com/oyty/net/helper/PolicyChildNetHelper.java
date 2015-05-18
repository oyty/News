package com.oyty.net.helper;

import android.content.Context;

import com.oyty.bean.PolicyChildDataBean;
import com.oyty.net.controller.OnNetworkDataCallBack;
import com.oyty.net.controller.Response;
import com.oyty.net.controller.VolleyNetworkWrapper;
import com.oyty.utils.GsonUtils;

/**
 * Created by oyty on 5/12/15.
 */
public class PolicyChildNetHelper extends VolleyNetworkWrapper {

    private String mUrl;

    private OnNetworkDataCallBack<PolicyChildDataBean> mOnNetworkDataCallBack;

    public PolicyChildNetHelper(Context context) {
        super(context);
    }

    @Override
    public String initUrl() {
        return mUrl;
    }
    public void setUrl(String url) {
        this.mUrl = url;
    }

    public void setOnResponseistener(OnNetworkDataCallBack<PolicyChildDataBean> callBack) {
        this.mOnNetworkDataCallBack = callBack;
    }

    @Override
    public void requestDataSuccess(Response response) {
        mOnNetworkDataCallBack.onNetworkDataSuccess(GsonUtils.json2Bean(response.data, PolicyChildDataBean.class));
    }

    @Override
    public void requestDataFail(String message) {
        mOnNetworkDataCallBack.onNetworkDataFailed(message);
    }
}
