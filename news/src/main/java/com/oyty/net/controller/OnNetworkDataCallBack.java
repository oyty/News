package com.oyty.net.controller;

/**
 * 网络连接的回调接口
 */
public interface OnNetworkDataCallBack<T> {

    public void onNetworkDataSuccess(T result);

    public void onNetworkDataFailed(String message);

}

