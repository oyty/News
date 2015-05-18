package com.oyty.newscenter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.oyty.base.BasePager;
import com.oyty.bean.NewsCenterBean;
import com.oyty.bean.NewsCenterBean.ChildrenItem;
import com.oyty.news.R;
import com.oyty.ui.widget.indicator.PageIndicator;
import com.oyty.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻中心，侧滑菜单，新闻页面界面
 * 主要是指针框架，每个指针对应界面为NewsCategory
 */
public class NewsPager extends BasePager {
    private static final String LOG_TAG = NewsPager.class.getSimpleName();

    //page指针，和其关联的viewpager
    private PageIndicator mIndicator;

    /**
     * 指针对应的界面还是有预加载功能的
     */
    private ViewPager mViewPager;
    private NewsCenterBean.DataItem mDataItem;

    /**
     * 每个指针对应的界面
     */
    List<NewsCategoryPager> ncLists = new ArrayList<NewsCategoryPager>();

    public NewsPager(Context context, NewsCenterBean.DataItem newsCenterData) {
        super(context);
        this.mDataItem = newsCenterData;
    }

    @Override
    public View initView() {
        //布局，上面是indicator，下面是viewpager
        View view = View.inflate(context, R.layout.frame_news, null);
        mIndicator = (PageIndicator) view.findViewById(R.id.indicator);
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        return view;
    }

    @Override
    public void initData() {

        //显示pager指针下面viewpager的数据，传递给NewsCategory，因为所有的下层页面都是相同的
        for (ChildrenItem item : mDataItem.children) {
            ncLists.add(new NewsCategoryPager(context, item.url));
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(ncLists);
        mViewPager.setAdapter(adapter);
        mIndicator.setViewPager(mViewPager);

        mIndicator.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (position != 0) {
                    mSM.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                } else {
                    mSM.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                }
                LogUtil.getLogger().i(LOG_TAG, "changed");
//                ncLists.get(position).initData();
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    private final class ViewPagerAdapter extends PagerAdapter {

        private List<NewsCategoryPager> lists;
        public ViewPagerAdapter(List<NewsCategoryPager> ncLists) {
            this.lists = ncLists;
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mDataItem.children.get(position).title;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(lists.get(position).getRootView());
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(lists.get(position).getRootView());
            LogUtil.getLogger().i(LOG_TAG, "初始化");
            return lists.get(position).getRootView();
        }

    }

}



