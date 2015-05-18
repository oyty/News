package com.oyty.base;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by oyty on 5/5/15.
 */
public class BaseApplication extends Application {

    /** 请求标签 */
    private static final String DEFAULT_REQ_TAG = "Volley_syt";

    /** Volley全局请求队列 */
    private RequestQueue mRequestQueue;

    /** 单例唯一的Application */
    private static BaseApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化fresco图片加载框架
        Fresco.initialize(this);
    }

    /**
     * 获取BaseApplication单一实例
     */
    public static BaseApplication getInstance() {
        if(mInstance == null) {
            synchronized (BaseApplication.class) {
                if(mInstance == null)
                    mInstance = new BaseApplication();
            }
        }
        return mInstance;
    }

    /**
     * 获取Volley请求序列
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * 按默认标签添加Volley请求
     */
    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(DEFAULT_REQ_TAG);
        getRequestQueue().add(request);
    }

    /**
     * 按默认标签取消所有将要执行或者正要执行的Volley请求
     */
    public void cancelPendingRequest() {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(DEFAULT_REQ_TAG);
        }
    }

}
