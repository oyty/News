package com.oyty.ui.pager;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.oyty.base.BasePager;
import com.oyty.base.Constants;
import com.oyty.bean.NewsCenterBean;
import com.oyty.net.controller.OnNetworkDataCallBack;
import com.oyty.net.helper.NewsCenterNetHelper;
import com.oyty.news.R;
import com.oyty.newscenter.NewsPager;
import com.oyty.newscenter.PicPager;
import com.oyty.newscenter.TopicPager;
import com.oyty.ui.activity.MainActivity;
import com.oyty.utils.GsonUtils;
import com.oyty.utils.LogUtil;
import com.oyty.utils.SPUtils;
import com.oyty.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个类是新闻中心的页面，首先initData从网络获取数据，该页面的侧滑菜单是最顶层的，所以在获取的网络数据中
 * 提取出侧滑菜单的数据，然后初始化MenuFragment页面，而默认我们先处理侧滑菜单的第一个条目，新闻页面，
 * 该页面的布局，上面是titlebar，下面是FrameLayout，很明显的，我们侧滑菜单中，选择什么，就在该FrameLayout中
 * 添加什么view，添加之前要removeAllViews，我们默认的是显示侧滑菜单第一条的数据，所以我们添加第一条数据新闻页面的时候
 * 要将我们获取的网络数据传递给NewsPager，当然，你可以在构造方法中传，也可以调用初始化数据方法initData。
 * 这个地方不想是viewpager，页面已经设置好了，这个地方我们要动态的添加和删除view，所以我们以构造方法的形式传递过去初始化数据。
 * 这页面有titlebar，但是不同的侧滑菜单选择对应的titlebar是不同的，所以这个应该在侧滑菜单对应的pager类里面进行初始化。
 *
 * @author Administrator
 */
public class NewsCenterPager extends BasePager {

    private static final String LOG_TAG = NewsCenterPager.class.getSimpleName();

    /* 新闻中心菜单条目的title */
    private List<String> mMenuData = new ArrayList<String>();

    /* 新闻中心条目对应的页面 */
    private List<BasePager> mPagerLists = new ArrayList<BasePager>();

    private int currentPosition = 0;

    private FrameLayout mNewsCenterContent;
    private View mLoadingView;

    public NewsCenterPager(Context context) {
        super(context);
    }


    @Override
    public View initView() {
        //新闻中心的布局，上面是titlebar，下面是FrameLayout
        View view = View.inflate(context, R.layout.frame_news_center, null);

        mNewsCenterContent = (FrameLayout) view.findViewById(R.id.news_center_fl);
        mLoadingView = view.findViewById(R.id.mLoadingView);

        return view;
    }

    @Override
    protected void initTitleBar() {
        mTitleLabel.setText(mMenuData.get(currentPosition));
        mLeftAction.setVisibility(View.VISIBLE);
        mLeftAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSM.toggle();
            }
        });
    }

    @Override
    public void initData() {

        /**
         * 先判断是否有缓存，有的话，直接处理缓存数据，然后继续调getNewsCenterData()方法
         * 这样，如果有网络，缓存赋值后，获取的新的数据覆盖
         * 如果没有网络，显示的就是缓存数据，这样体验好
         */
        String result = SPUtils.getString(context, Constants.preferences.NEWS_CENTER_CATEGORIES, "");

        if (!TextUtils.isEmpty(result)) {
            LogUtil.getLogger().i(LOG_TAG, "拿缓存数据");

            processData(GsonUtils.json2Bean(result, NewsCenterBean.class));
        } else {
            // 更新数据可以主动请求
            getNewsCenterData();
        }
    }

    /**
     * 从服务器获取新闻中心数据
     */
    private void getNewsCenterData() {
        NewsCenterNetHelper netHelper = new NewsCenterNetHelper(context);
        netHelper.setOnResponseListener(new OnNetworkDataCallBack<NewsCenterBean>() {
            @Override
            public void onNetworkDataSuccess(NewsCenterBean newsCenterBean) {
                processData(newsCenterBean);

                //将数据缓存起来
                SPUtils.putString(context, Constants.preferences.NEWS_CENTER_CATEGORIES, GsonUtils.bean2Json(newsCenterBean));
            }

            @Override
            public void onNetworkDataFailed(String message) {
                ToastUtil.showToast(context, message);
                mLoadingView.setVisibility(View.GONE);
            }
        });
        netHelper.execute();

    }

    /**
     * 处理服务器返回的json数据
     *
     */
    protected void processData(NewsCenterBean newsCenter) {
        mLoadingView.setVisibility(View.GONE);
        initNewsCenterMenu(newsCenter);
        initNewsCenterData(newsCenter);

        // 默认现实第一条菜单条目对应数据
        switchContentLayout(0);
    }

    /**
     * 初始化新闻中心的菜单数据
     */
    private void initNewsCenterMenu(NewsCenterBean newsCenter) {

        //封装菜单title数据到MenuFragment页面显示
        mMenuData.clear();
        for(int i=0; i<newsCenter.data.size(); i++) {
            mMenuData.add(newsCenter.data.get(i).title);
            if(i == 2)
                break;
        }

        ((MainActivity) context).getMenuFragment().initMenuData(mMenuData);
    }

    /**
     * 初始化新闻中心菜单条目对应的数据
     */
    private void initNewsCenterData(NewsCenterBean newsCenter) {
        mPagerLists.clear();
        mPagerLists.add(new NewsPager(context, newsCenter.data.get(0)));
        mPagerLists.add(new TopicPager(context));
        mPagerLists.add(new PicPager(context));
//        mPagerLists.add(new InteractionPager(context, newsCenter.data.get(3)));
    }

    /**
     * 点击新闻中心的菜单条目后，要切换不同的fragment页面
     * 如何回调该方法，当然是在MenuFragment里面
     */
    @Override
    public void switchContentLayout(int position) {
        //用对应的页面替换帧布局
        mNewsCenterContent.removeAllViews();

        mNewsCenterContent.addView(mPagerLists.get(position).getRootView());

        //切换到指定页面，并初始化该页面的数据
        mPagerLists.get(position).initData();

        currentPosition = position;
        initTitleBar();
    }

}
