package com.oyty.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.oyty.base.Constants;
import com.oyty.news.R;
import com.oyty.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 导航界面
 */
public class GuideActivity extends Activity implements OnPageChangeListener, OnClickListener {

    private ViewPager mGuidePager;
    private Button mGuideAction;
    private List<View> mImages;

    private boolean mSettingFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();

        initViewLintener();

        process();
    }

    protected void initView() {
        mGuidePager = (ViewPager) findViewById(R.id.mGuidePager);
        mGuideAction = (Button) findViewById(R.id.mGuideAction);
    }

    protected void initViewLintener() {
        mGuidePager.setOnPageChangeListener(this);
        mGuideAction.setOnClickListener(this);
    }

    protected void process() {
        Intent intent = getIntent();
        if(intent != null) {
            mSettingFlag = intent.getBooleanExtra(Constants.SETTING_GUIDE, false);
        }

        mImages = new ArrayList<View>();

        ImageView iv1 = new ImageView(this);
        ImageView iv2 = new ImageView(this);
        ImageView iv3 = new ImageView(this);

        iv1.setBackgroundResource(R.drawable.guide_1);
        iv2.setBackgroundResource(R.drawable.guide_2);
        iv3.setBackgroundResource(R.drawable.guide_3);

        mImages.add(iv1);
        mImages.add(iv2);
        mImages.add(iv3);

        GuideViewPagerAdapter adapter = new GuideViewPagerAdapter();
        mGuidePager.setAdapter(adapter);
    }


    private final class GuideViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImages.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);//该方法就是抛出了一个异常
            ((ViewPager) container).removeView(mImages.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mImages.get(position);
            ((ViewPager) container).addView(view);
            return view;
        }
    }


    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    //当前选择的界面
    @Override
    public void onPageSelected(int position) {
        if (position == mImages.size() - 1) {
            //显示button
            mGuideAction.setVisibility(View.VISIBLE);
        } else {
            mGuideAction.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if(mSettingFlag) {
            finish();
            return;
        }
        //标记非第一次进入
        SPUtils.putBoolean(this, Constants.Preferences.IS_FIRST, false);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
