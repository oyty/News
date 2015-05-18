package com.oyty.net.helper;

import android.content.Context;

import com.oyty.bean.HomeGridBean;
import com.oyty.bean.ServiceDataModel;
import com.oyty.net.controller.OnNetworkDataCallBack;
import com.oyty.net.controller.Response;
import com.oyty.net.controller.VolleyNetworkWrapper;
import com.oyty.utils.AppUtil;
import com.oyty.utils.GsonUtils;

import java.util.List;

/**
 * 获取首页下面工具栏数据
 *
 * Created by oyty on 5/9/15.
 */
public class HomeGridNetHelper extends VolleyNetworkWrapper {

    private OnNetworkDataCallBack<List<ServiceDataModel>> mOnNetworkDataCallBack;

    public HomeGridNetHelper(Context context) {
        super(context);
    }

    @Override
    public String initUrl() {
        return AppUtil.HOME_TOOLS;
    }

    public void setOnResponseListener(OnNetworkDataCallBack<List<ServiceDataModel>> callBack) {
        this.mOnNetworkDataCallBack = callBack;
    }

    @Override
    public void requestDataSuccess(Response response) {
        mOnNetworkDataCallBack.onNetworkDataSuccess(GsonUtils.json2Bean(response.result, HomeGridBean.class).data);
    }

    @Override
    public void requestDataFail(String message) {
        mOnNetworkDataCallBack.onNetworkDataFailed(message);
    }
}
