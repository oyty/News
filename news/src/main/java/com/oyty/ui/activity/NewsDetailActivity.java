package com.oyty.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.oyty.base.BaseActivity;
import com.oyty.base.Constants;
import com.oyty.news.R;
import com.oyty.utils.SPUtils;

public class NewsDetailActivity extends BaseActivity implements OnClickListener {
	private WebView mWebView;
	private View mLoadingView;
	
	private String mUrl;
    private String mTitle;
	private WebSettings mSettings;

    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;

    private String[] textSizeStr = new String[] { "大号字体", "中号字体", "小号字体"};

    @Override
    public int initLayoutID() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected void initTitleAction() {
        mTitleLabel.setVisibility(View.INVISIBLE);
        mLeftBackAction.setVisibility(View.VISIBLE);
        mShareAction.setVisibility(View.VISIBLE);
        mSaveAction.setVisibility(View.VISIBLE);
        mTextSizeAction.setVisibility(View.VISIBLE);

        mLeftBackAction.setOnClickListener(this);
        mTextSizeAction.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        mWebView = (WebView) findViewById(R.id.mWebView);
        mLoadingView = findViewById(R.id.mLoadingView);

        setWebViewParam();
        initEvents();
    }

    @Override
    protected void initViewLintener() {
    }

    @Override
    protected void process() {

        mSettings = mWebView.getSettings();

        Intent intent = getIntent();
        if(intent != null){
            mUrl = intent.getStringExtra(Constants.NEWS_DETAIL_URL);
            mTitle = intent.getStringExtra(Constants.GRID_TOOLS_TITLE);
        }
        initTitle();
        mWebView.loadUrl(mUrl);

        switchTextSize(SPUtils.getInt(mContext, Constants.Preferences.TEXT_SIZE, 1));
    }

    class CustomWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {

            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            NewsDetailActivity.this.startActivityForResult(
                    Intent.createChooser(i, "File Chooser"),
                    FILECHOOSER_RESULTCODE);
        }

        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback uploadMsg,
                                    String acceptType) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            NewsDetailActivity.this.startActivityForResult(
                    Intent.createChooser(i, "File Browser"),
                    FILECHOOSER_RESULTCODE);
        }

        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            NewsDetailActivity.this.startActivityForResult(
                    Intent.createChooser(i, "File Chooser"),
                    NewsDetailActivity.FILECHOOSER_RESULTCODE);

        }
    }

    private void initEvents() {
        mWebView.setWebChromeClient(new CustomWebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }

            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, android.net.http.SslError error) {
                handler.proceed();
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//				showProgress();
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mLoadingView.setVisibility(View.GONE);
            }
        });
    }

    private void setWebViewParam() {
        mWebView.setInitialScale(55);// 为x%，最小缩放等级

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        // 设置默认缩放方式尺寸
        webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);

        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        // 让网页自适应屏幕宽度 SINGLE_COLUMN
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null
                    : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }

    /**
     * 初始化title
     */
    private void initTitle() {
        if(!TextUtils.isEmpty(mTitle)) {
            mTitleLabel.setVisibility(View.VISIBLE);
            mTitleLabel.setText(mTitle);
        }
    }

    @Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mTextSizeAction:
            showTextSizeDialog();
			break;
		case R.id.mLeftBackAction:
			finish();
			break;
		}
	}

    private void showTextSizeDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("正文字体");
        builder.setItems(textSizeStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switchTextSize(i);
            }
        });
        builder.show();
    }

    private void switchTextSize(int i) {
		switch (i) {
		case 0:
			mSettings.setTextZoom(200);
			break;
		case 1:
			mSettings.setTextZoom(100);
			break;
		case 2:
			mSettings.setTextZoom(50);
			break;

		default:
			break;
		}
	}
	
}
