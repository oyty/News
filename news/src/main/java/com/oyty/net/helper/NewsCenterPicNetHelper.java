package com.oyty.net.helper;

import android.content.Context;

import com.oyty.bean.NewsCenterPicBean;
import com.oyty.net.controller.OnNetworkDataCallBack;
import com.oyty.net.controller.Response;
import com.oyty.net.controller.VolleyNetworkWrapper;
import com.oyty.utils.GsonUtils;

/**
 * Created by oyty on 5/10/15.
 */
public class NewsCenterPicNetHelper extends VolleyNetworkWrapper{

    private String mUrl;

    private OnNetworkDataCallBack<NewsCenterPicBean> mOnNetworkDataCallBack;

    public NewsCenterPicNetHelper(Context context) {
        super(context);
    }

    @Override
    public String initUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public void setOnResponseListener (OnNetworkDataCallBack<NewsCenterPicBean> callBack) {
        this.mOnNetworkDataCallBack = callBack;
    }

    @Override
    public void requestDataSuccess(Response response) {
        mOnNetworkDataCallBack.onNetworkDataSuccess(GsonUtils.json2Bean(response.data, NewsCenterPicBean.class));
    }

    @Override
    public void requestDataFail(String message) {
        mOnNetworkDataCallBack.onNetworkDataFailed(message);
    }
}
