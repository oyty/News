package com.oyty.newscenter;

import android.content.Context;
import android.view.View;

import com.oyty.base.BasePager;
import com.oyty.bean.NewsCenterBean;
import com.oyty.news.R;

public class InteractionPager extends BasePager {


    private NewsCenterBean.DataItem mDataItem;

	public InteractionPager(Context context, NewsCenterBean.DataItem newsCenterData) {
		super(context);
        this.mDataItem = newsCenterData;
	}

	@Override
	public View initView() {
		View view = View.inflate(context, R.layout.frame_interaction, null);
		return view;
	}

	@Override
	public void initData() {

	}

}
