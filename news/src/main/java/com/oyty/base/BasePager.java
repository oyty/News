package com.oyty.base;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.oyty.db.controller.CacheDao;
import com.oyty.news.R;
import com.oyty.ui.activity.MainActivity;

public abstract class BasePager {

	public Context context;
	private View view;
	public SlidingMenu mSM;
    public View mLoadingView;

    private View mTitleView;

    public TextView mTitleLabel;
    public ImageButton mLeftAction;

    protected CacheDao mCacheDao;

	public BasePager(Context context){
		this.context = context;
        mCacheDao = new CacheDao(context);
		mSM = ((MainActivity)context).getSlidingMenu();
		view = initView();
        mLoadingView = view.findViewById(R.id.mLoadingView);
        initTitle();
	}

    private void initTitle() {
        mTitleView = view.findViewById(R.id.title_bar);
        if(mTitleView != null) {
            mLeftAction = (ImageButton) mTitleView.findViewById(R.id.mLeftAction);
            mTitleLabel = (TextView) mTitleView.findViewById(R.id.mTitleLabel);
        }
    }

    /**
     * 初始化titlebar,子类选择性实现
     */
    protected void initTitleBar() {
    }

    /**
	 * 初始化页面view
	 * @return
	 */
	public abstract View initView();
	
	/**
	 * 初始化页面数据
	 */
	public abstract void initData();
	
	/**
	 * 获取该页面要显示的视图
	 * @return
	 */
	public View getRootView(){
		return view;
	}

    /**
     * 侧滑菜单切换,子类选择性实现
     */
	public void switchContentLayout(int position) {
    }
	
}
