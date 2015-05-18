package com.oyty.net.controller;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.oyty.news.R;
import com.oyty.utils.LogUtil;
import com.oyty.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Volley网络请求包装类
 */
public abstract class VolleyNetworkWrapper {
    private static final String LOG_TAG = VolleyNetworkWrapper.class.getSimpleName();

    protected Context mContext;

    public VolleyNetworkWrapper(Context context) {
        this.mContext = context;
    }

    public void execute() {

        String requestUrl = initUrl();

        LogUtil.getLogger().i(LOG_TAG, requestUrl);

        CustomVolleyRequest request = new CustomVolleyRequest(Request.Method.GET,
                requestUrl, normalListener, errorListener);

        RequestQueueHelper.getInstance(mContext).addToRequestQueue(request);
    }

    private Listener<String> normalListener = new Listener<String>() {

        @Override
        public void onResponse(String response) {

            if (TextUtils.isEmpty(response)) {
                requestDataFail(mContext
                        .getString(R.string.server_other_error));
                return;
            }

            JSONObject jsonResult = null;
            try {
                jsonResult = new JSONObject(response);
                Response customResponse = new Response(jsonResult);
                if(customResponse.isSuccess()) {
                    requestDataSuccess(customResponse);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    private ErrorListener errorListener = new ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            requestNetworkFail(error);
        }
    };


    /**
     * 自定义处理连接服务器失败时要重写该方法 ,请求连接服务器失败调用的方法
     *
     * @param volleyError
     */
    public void requestNetworkFail(VolleyError volleyError) {
        ToastUtil.showToast(mContext,
                VolleyErrorHelper.getMessage(mContext, volleyError),
                Toast.LENGTH_SHORT);
    }

    /**
     * 请求的url
     */
    public abstract String initUrl();

    /**
     * 请求数据成功，返回json数据
     */
    public abstract void requestDataSuccess(Response response);


    /**
     * 自定义处理请求数据失败时要重写该方法 请求数据失败，返回后台对应业务逻辑错误提示
     */
    public abstract void requestDataFail(String message);

}
