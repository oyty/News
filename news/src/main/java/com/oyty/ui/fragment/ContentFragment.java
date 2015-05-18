package com.oyty.ui.fragment;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.oyty.base.BaseFragment;
import com.oyty.base.BasePager;
import com.oyty.news.R;
import com.oyty.ui.pager.GovAffairsPager;
import com.oyty.ui.pager.HomePager;
import com.oyty.ui.pager.NewsCenterPager;
import com.oyty.ui.pager.SettingPager;
import com.oyty.ui.pager.SmartServicePager;
import com.oyty.ui.widget.LazyViewPager.OnPageChangeListener;
import com.oyty.ui.widget.MyViewPager;
import com.oyty.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 主页面，为一个viewpager，切换不同的fragment
 */
public class ContentFragment extends BaseFragment implements OnCheckedChangeListener {

    private static final String LOG_TAG = ContentFragment.class.getSimpleName();

    private MyViewPager mViewPager;
    private RadioGroup mMainRadio;
    private List<BasePager> mPagerLists;
    private BasePager mCurrentPager;

    @Override
    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_home, null);
        return view;
    }

    @Override
    public void initData(View view) {
        mViewPager = (MyViewPager) view.findViewById(R.id.view_pager);
        mMainRadio = (RadioGroup) view.findViewById(R.id.main_radio);

        mPagerLists = new ArrayList<BasePager>();

        mPagerLists.add(new HomePager(context));
        mPagerLists.add(new NewsCenterPager(context));
        mPagerLists.add(new SmartServicePager(context));
        mPagerLists.add(new GovAffairsPager(context));
        mPagerLists.add(new SettingPager(context));

        // 初始化适配器
        ViewPagerAdapter adapter = new ViewPagerAdapter();
        mViewPager.setAdapter(adapter);

        //为底部RadioGroup设置点击监听
        mMainRadio.setOnCheckedChangeListener(this);

        // 默认选中首页
        mMainRadio.check(R.id.mHomeRb);
        // 初始化首页数据
        mPagerLists.get(0).initData();

        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                mCurrentPager = mPagerLists.get(position);

                // 切换页面，初始化数据
                mCurrentPager.initData();
                LogUtil.getLogger().i(LOG_TAG, "进入第" + position + "个页面");
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public BasePager getCurrentPager() {
        return mCurrentPager;
    }

    /**
     * 在此处使用ViewPager有两个地方需要改进
     * 第一个：ViewPager有预加载的功能，总是加载当前页面和前后页面，当然如果包含第一页和最后一页，就只能加载两个页面了
     *      但是此处我们不需要有预加载功能
     * 第二个：ViewPager默认是可以从一个页面滑动到另一个页面的，但是此处我们不需要它滑动，这个要涉及到事件分发机制
     */
    private final class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPagerLists.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((MyViewPager) container).removeView(mPagerLists.get(position).getRootView());
            LogUtil.getLogger().i(LOG_TAG, "destroy -- " + position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mPagerLists.get(position).getRootView();
            ((MyViewPager) container).addView(view);

            LogUtil.getLogger().i(LOG_TAG, "instantiateItem -- " + position);
            return view;
        }
    }

    /**
     * 为radiofroup设置点击监听
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.mHomeRb:
                /**
                 * 直接将viewpager显示为点击的页面
                 * 第二个参数boolean smoothScroll，如果为true的话，那么viewpagewr切换的
                 * 的时候会有动画效果
                 */
                mViewPager.setCurrentItem(0, false);
                mSM.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);//是否可以滑动
                break;
            case R.id.mNewsCenterRb:
                mViewPager.setCurrentItem(1, false);
                mSM.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                break;
            case R.id.mSmartServiceRb:
                mViewPager.setCurrentItem(2, false);
                mSM.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                break;
            case R.id.mGovAffairsRb:
                mViewPager.setCurrentItem(3, false);
                mSM.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                break;
            case R.id.mSettingRb:
                mViewPager.setCurrentItem(4, false);
                mSM.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                break;
        }
    }


}
