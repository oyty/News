package com.oyty.net.helper;

import android.content.Context;

import com.oyty.bean.SmartServiceBean;
import com.oyty.net.controller.OnNetworkDataCallBack;
import com.oyty.net.controller.Response;
import com.oyty.net.controller.VolleyNetworkWrapper;
import com.oyty.utils.AppUtil;
import com.oyty.utils.GsonUtils;

/**
 * Created by oyty on 5/10/15.
 */
public class SmartServiceNetHelper extends VolleyNetworkWrapper {
    private OnNetworkDataCallBack<SmartServiceBean> mOnNetworkDataCallBack;

    public SmartServiceNetHelper(Context context) {
        super(context);
    }

    @Override
    public String initUrl() {
        return AppUtil.SMART_SERVICE_CATEGORIES;
    }

    public void setOnResponseListener (OnNetworkDataCallBack<SmartServiceBean> callBack) {
        this.mOnNetworkDataCallBack = callBack;
    }

    @Override
    public void requestDataSuccess(Response response) {
        mOnNetworkDataCallBack.onNetworkDataSuccess(GsonUtils.json2Bean(response.result, SmartServiceBean.class));
    }

    @Override
    public void requestDataFail(String message) {
        mOnNetworkDataCallBack.onNetworkDataFailed(message);
    }
}
