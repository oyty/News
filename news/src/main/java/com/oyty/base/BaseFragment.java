package com.oyty.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.oyty.ui.activity.MainActivity;

public abstract class BaseFragment extends Fragment {

	public Context context;
	private View view;

	public SlidingMenu mSM;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		context = getActivity();
		mSM = ((MainActivity)context).getSlidingMenu();
		initData(view);//这个方法只会执行一次
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = initView();
		return view;
	}

	/**
	 * 初始化view
	 * @return
	 */
	public abstract View initView();
	
	/**
	 * 初始化数据
	 * @param view
	 */
	public abstract void initData(View view);
	
}
