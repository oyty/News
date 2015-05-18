package com.oyty.ui.activity;

import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.oyty.news.R;
import com.oyty.ui.fragment.ContentFragment;
import com.oyty.ui.fragment.MenuFragment;

/**
 * 主界面，由两个Fragment组成，MenuFragment，ContentFragment
 */
public class MainActivity extends SlidingFragmentActivity {

    private static final String FRAGMENT_HOME_TAG = "fragment_home_tag";
    private static final String FRAGMENT_MENU_TAG = "fragment_menu_tag";

	private SlidingMenu mSlidingMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        setBehindContentView(R.layout.frame_menu);
		setContentView(R.layout.frame_content);

        initSlideMenu();

        initFragment();
	}

    /**
     * 初始化fragment界面
     */
    private void initFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mMenuLayout, new MenuFragment(), FRAGMENT_MENU_TAG)
                .commit();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mContentLayout, new ContentFragment(), FRAGMENT_HOME_TAG)
                .commit();
    }

    /**
     * 初始化侧滑菜单
     */
    private void initSlideMenu() {
        mSlidingMenu = getSlidingMenu();

        // 设置菜单显示的位置
        mSlidingMenu.setMode(SlidingMenu.LEFT);
        /**
         * 设置菜单touch事件方式
         * SlidingMenu.TOUCHMODE_FULLSCREEN  在任何地方滑动菜单都可以滑动
         * SlidingMenu.TOUCHMODE_MARGIN		  只能在边沿部分进行滑动
         * SlidingMenu.TOUCHMODE_NONE		 不可以滑动
         */
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        mSlidingMenu.setShadowDrawable(R.drawable.shadow);
        mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        mSlidingMenu.setFadeDegree(0.35f);
    }

	/**
	 * 暴露出MenuFragment实例对象
	 * @return
	 */
	public MenuFragment getMenuFragment(){
		return (MenuFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_MENU_TAG);
	}

	/**
	 * 暴露出HomeFragment实例对象
	 * @return
	 */
	public ContentFragment getHomeFragment(){
		return (ContentFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_HOME_TAG);
	}
}
