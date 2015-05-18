package com.oyty.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.oyty.base.BaseActivity;
import com.oyty.base.Constants;
import com.oyty.bean.TopicDetailBean;
import com.oyty.net.controller.OnNetworkDataCallBack;
import com.oyty.news.R;
import com.oyty.newscenter.TopicDetailNetHelper;
import com.oyty.ui.adapter.TopicDetailAdapter;
import com.oyty.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oyty on 5/11/15.
 */
public class TopicDetailActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context mContext;

    private SimpleDraweeView mTopicDetailImg;
    private TextView mTopicDetailLabel;
    private ListView mTopicDetailListView;

    private TopicDetailAdapter mAdapter;

    private View mLoadingView;

    private List<TopicDetailBean.TopicDetailNews> topicLists = new ArrayList<TopicDetailBean.TopicDetailNews>();

    @Override
    public int initLayoutID() {
        return R.layout.activity_topic_detail;
    }

    @Override
    protected void initView() {
        mContext = this;
        mTopicDetailImg = (SimpleDraweeView) findViewById(R.id.mTopicDetailImg);
        mTopicDetailLabel = (TextView) findViewById(R.id.mTopicDetailLabel);
        mTopicDetailListView = (ListView) findViewById(R.id.mTopicDetailListView);
        mLoadingView = findViewById(R.id.mLoadingView);
    }

    @Override
    protected void initViewLintener() {
        mTopicDetailListView.setOnItemClickListener(this);
    }

    @Override
    protected void process() {
        Intent intent = getIntent();
        if (intent != null) {
            String url = intent.getStringExtra(Constants.NEWS_DETAIL_URL);
            String title = intent.getStringExtra(Constants.TOPIC_NEWS_TITLE);
            if (!TextUtils.isEmpty(url)) {
                getNewsDetailData(url);
            } else {
                ToastUtil.showToast(mContext, "出错了");
            }
            if (!TextUtils.isEmpty(title)) {
                mTitleLabel.setText(title);
            }
        }
    }

    private void getNewsDetailData(String url) {
        TopicDetailNetHelper netHelper = new TopicDetailNetHelper(mContext);
        netHelper.setUrl(url);
        netHelper.setOnResponseListener(new OnNetworkDataCallBack<TopicDetailBean>() {
            @Override
            public void onNetworkDataSuccess(TopicDetailBean result) {
                processData(result);
            }

            @Override
            public void onNetworkDataFailed(String message) {
                ToastUtil.showToast(mContext, message);
            }
        });
        netHelper.execute();
    }

    private void processData(TopicDetailBean result) {
        mLoadingView.setVisibility(View.GONE);
        topicLists.clear();
        topicLists.addAll(result.news);

        mTopicDetailImg.setImageURI(Uri.parse(result.listimage));
        mTopicDetailLabel.setText(result.description);

        if (mAdapter == null) {
            mAdapter = new TopicDetailAdapter(mContext, result.news);
            mTopicDetailListView.setAdapter(mAdapter);
        } else {
            mAdapter.setDataLists(result.news);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void initTitleAction() {
        mLeftBackAction.setVisibility(View.VISIBLE);
        mLeftBackAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TopicDetailBean.TopicDetailNews news = null;
        news = topicLists.get(i);

        if (!news.is_read) {
            news.is_read = true;
        }
        mAdapter.notifyDataSetChanged();

        //点击每一个新闻条目后，进入到新闻详情页面
        Intent intent = new Intent(mContext, NewsDetailActivity.class);
        intent.putExtra(Constants.NEWS_DETAIL_URL, news.url);
        mContext.startActivity(intent);
    }
}
