package com.oyty.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.oyty.news.R;

/**
 * Created by oyty on 5/5/15.
 */
public abstract class BaseActivity extends Activity {

    public Context mContext;

    public TextView mTitleLabel;
    public ImageButton mLeftBackAction;
    public ImageButton mShareAction;
    public ImageButton mSaveAction;
    public ImageButton mTextSizeAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(initLayoutID());
        ActivityManager.getInstance().addActivity(this);

        initTitleBar();

        initView();

        initViewLintener();

        process();
    }

    private void initTitleBar() {
        mTitleLabel = (TextView) findViewById(R.id.mTitleLabel);
        mLeftBackAction = (ImageButton) findViewById(R.id.mLeftBackAction);
        mShareAction = (ImageButton) findViewById(R.id.mShareAction);
        mSaveAction = (ImageButton) findViewById(R.id.mSaveAction);
        mTextSizeAction  =(ImageButton) findViewById(R.id.mTextSizeAction);

        initTitleAction();
    }

    /**
     * 初始化title的相关操作
     */
    protected abstract void initTitleAction();

    /**
     * 布局id
     */
    public abstract int initLayoutID();

    /**
     * 初始化view
     */
    protected abstract void initView();

    /**
     * 初始化view监听事件
     */
    protected abstract void initViewLintener();

    /**
     * 业务逻辑处理
     */
    protected abstract void process();

}
