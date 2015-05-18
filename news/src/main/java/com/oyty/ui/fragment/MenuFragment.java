package com.oyty.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.oyty.news.R;
import com.oyty.base.BaseFragment;
import com.oyty.ui.activity.MainActivity;
import com.oyty.ui.adapter.BaseAdapter;
import com.oyty.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 侧滑菜单
 */
public class MenuFragment extends BaseFragment {

	private static final String LOG_TAG = MenuFragment.class.getSimpleName();

	/* 要显示的菜单title数据 */
	private List<String> menuLists = new ArrayList<String>();
	private ListView lvMenu;
	private MenuDataAdapter adapter;
	
	@Override
	public View initView() {
		View view = View.inflate(getActivity(), R.layout.fragment_news_menu, null);
		return view;
	}

	@Override
	public void initData(View view) {
		
		lvMenu = (ListView) view.findViewById(R.id.lv_menu_news_center);
		lvMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//点击之后，传递点击条目后，更新数据，重新getView
				adapter.setClickPosition(position);
				adapter.notifyDataSetChanged();
				
				/**
				 * 回调当前页面的switchFragment方法
				 * 多态，切换页面，主页面和设置页面将是空实现，因为没有侧滑菜单
				 */
				((MainActivity)context).getHomeFragment().getCurrentPager().switchContentLayout(position);
				
				//Toggle the SlidingMenu. If it is open, it will be closed, and vice versa
				mSM.toggle();
			}
		});
	}

    /**
     * 初始化menu
     * @param menuData
     */
	public void initMenuData(List<String> menuData) {
		LogUtil.getLogger().i(LOG_TAG, "初始化menu" + menuData);
		menuLists.clear();
		this.menuLists.addAll(menuData);
		if(adapter == null){
			adapter = new MenuDataAdapter(getActivity(), menuLists);
		}
		lvMenu.setAdapter(adapter);
	}
	
	/**
	 * 将BaseAdapter封装后，代码变的很简洁
	 * @author Administrator
	 *
	 */
	private final class MenuDataAdapter extends BaseAdapter<String> {

		private List<String> mLists;
		private int mCurrentItem = 0;//当前点击的条目，默认为0
		
		public MenuDataAdapter(Context context, List<String> lists) {
			super(context, lists);
			this.mLists = lists;
		}
		
		//传过来点击的条目的位置
		public void setClickPosition(int position) {
			this.mCurrentItem = position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = View.inflate(context, R.layout.adapter_item_menu, null);
			}
			
			ImageView ivMenu = (ImageView) convertView.findViewById(R.id.iv_menu_item);
			TextView tvMenu = (TextView) convertView.findViewById(R.id.tv_menu_item);
			
			/**
			 * 根据clickPosition设置条目的字体颜色和背景图片
			 */
			if(mCurrentItem == position){
				//该条目被点击
				tvMenu.setTextColor(Color.RED);
				ivMenu.setBackgroundResource(R.drawable.menu_arr_select);
				convertView.setBackgroundResource(R.drawable.menu_item_bg_select);
			} else {
				tvMenu.setTextColor(Color.WHITE);
				ivMenu.setBackgroundResource(R.drawable.menu_arr_normal);
				convertView.setBackgroundResource(Color.TRANSPARENT);
			}
			
			tvMenu.setText(menuLists.get(position));
			
			return convertView;
		}
		
	}
	
}