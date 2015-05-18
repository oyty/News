package com.oyty.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ViewPager页面不可滑动
 */
public class MyViewPager extends LazyViewPager {

	
	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 通过查看源码知道，在VIewPager的onTouchEvent方法里面处理了滑动翻页效果
	 * 那么我们只要重写该方法，不做任何处理就可以了。
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		return false;
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return false;
	}

}
