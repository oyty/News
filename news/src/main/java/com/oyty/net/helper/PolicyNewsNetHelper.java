package com.oyty.net.helper;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.oyty.bean.PolicyDataModel;
import com.oyty.net.controller.OnNetworkDataCallBack;
import com.oyty.net.controller.Response;
import com.oyty.net.controller.VolleyNetworkWrapper;
import com.oyty.utils.AppUtil;
import com.oyty.utils.GsonUtils;

import java.util.List;

/**
 * Created by oyty on 5/12/15.
 */
public class PolicyNewsNetHelper extends VolleyNetworkWrapper {

    private OnNetworkDataCallBack<List<PolicyDataModel>> mOnNetworkDataCallBack;

    public PolicyNewsNetHelper(Context context) {
        super(context);
    }

    @Override
    public String initUrl() {
        return AppUtil.POLICY_NEWS;
    }

    public void setOnResponseListener(OnNetworkDataCallBack<List<PolicyDataModel>> callBack) {
        this.mOnNetworkDataCallBack = callBack;
    }

    @Override
    public void requestDataSuccess(Response response) {
        mOnNetworkDataCallBack.onNetworkDataSuccess(GsonUtils.json2Array(response.data, new TypeToken<List<PolicyDataModel>>(){}));
    }

    @Override
    public void requestDataFail(String message) {
        mOnNetworkDataCallBack.onNetworkDataFailed(message);
    }
}
