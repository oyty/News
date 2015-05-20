package com.oyty.ui.pager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import com.oyty.base.BasePager;
import com.oyty.base.Constants;
import com.oyty.news.R;
import com.oyty.ui.activity.GuideActivity;
import com.oyty.ui.widget.setting.NormalRowView;
import com.oyty.ui.widget.setting.OnRowClickListener;
import com.oyty.ui.widget.setting.RowDescriptor;
import com.oyty.utils.SPUtils;
import com.oyty.utils.ToastUtil;

public class SettingPager extends BasePager implements OnRowClickListener {

    private NormalRowView mTextSizeRow;
    private NormalRowView mClearCacheRow;
    private NormalRowView mGuideRow;
    private NormalRowView mFeedbackRow;
    private NormalRowView mCheckUpdateRow;
    private NormalRowView mAboutRow;

    private String[] textSizeStr = new String[]{"大号字体", "中号字体", "小号字体"};

    public SettingPager(Context context) {
        super(context);
    }

    @Override
    protected void initTitleBar() {
        mTitleLabel.setText("设置");
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.frame_setting, null);
        initRowView(view);
        return view;
    }

    private void initRowView(View view) {
        mTextSizeRow = (NormalRowView) view.findViewById(R.id.mTextSizeRow);
        mClearCacheRow = (NormalRowView) view.findViewById(R.id.mClearCacheRow);
        mGuideRow = (NormalRowView) view.findViewById(R.id.mGuideRow);
        mFeedbackRow = (NormalRowView) view.findViewById(R.id.mFeedbackRow);
        mCheckUpdateRow = (NormalRowView) view.findViewById(R.id.mCheckUpdateRow);
        mAboutRow = (NormalRowView) view.findViewById(R.id.mAboutRow);
    }

    @Override
    public void initData() {

        RowDescriptor descriptor1 = new RowDescriptor(R.drawable.icon_setting_textsize, "设置字体", NormalRowView.RowActionEnum.TEXT_SIZE);
        mTextSizeRow.initData(descriptor1, this);
        RowDescriptor descriptor3 = new RowDescriptor(R.drawable.icon_setting_clear_cache, "清除缓存", NormalRowView.RowActionEnum.CLEAR_CACHE);
        mClearCacheRow.initData(descriptor3, this);
        RowDescriptor descriptor4 = new RowDescriptor(R.drawable.icon_setting_book, "新手引导", NormalRowView.RowActionEnum.GUIDE);
        mGuideRow.initData(descriptor4, this);
        RowDescriptor descriptor5 = new RowDescriptor(R.drawable.icon_setting_feedback, "意见反馈", NormalRowView.RowActionEnum.FEEDBACK);
        mFeedbackRow.initData(descriptor5, this);
        RowDescriptor descriptor6 = new RowDescriptor(R.drawable.icon_setting_checkversion, "检测新版本", NormalRowView.RowActionEnum.CHECK_UPDATE);
        mCheckUpdateRow.initData(descriptor6, this);
        RowDescriptor descriptor7 = new RowDescriptor(R.drawable.icon_setting_info, "关于", NormalRowView.RowActionEnum.ABOUT);
        mAboutRow.initData(descriptor7, this);
    }

    @Override
    public void onRowClick(NormalRowView.RowActionEnum action) {
        switch (action) {
            case TEXT_SIZE:
                setTextSize();
                break;
            case CLEAR_CACHE:
                clearCache();
                break;
            case GUIDE:
                guideApp();
                break;
            case FEEDBACK:
                feedback();
                break;
            case CHECK_UPDATE:
                checkUpdate();
                break;
            case ABOUT:
                aboutApp();
                break;
        }
    }

    private void clearCache() {
        boolean flag = mCacheDao.clearCache();
        if(flag) {
            ToastUtil.showToast(context, "缓存清理成功！");
        } else {
            ToastUtil.showToast(context, "缓存清理失败！");
        }
    }

    private void guideApp() {
        Intent intent = new Intent(context, GuideActivity.class);
        intent.putExtra(Constants.SETTING_GUIDE, true);
        context.startActivity(intent);
    }

    private void feedback() {
        ToastUtil.showToast(context, "意见反馈");
    }


    private void aboutApp() {
        ToastUtil.showToast(context, "这是毕业设计");
    }

    private void checkUpdate() {
        ToastUtil.showToast(context, "已是最新版本！");
    }

    private void setTextSize() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("正文字体");
        builder.setItems(textSizeStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SPUtils.putInt(context, Constants.Preferences.TEXT_SIZE, i);
            }
        });
        builder.show();
    }

}
