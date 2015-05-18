package com.oyty.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.oyty.base.Constants;
import com.oyty.news.R;
import com.oyty.utils.SPUtils;

/**
 * Splash界面，进入导航界面
 */
public class SplashActivity extends Activity {

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		new Handler(){
			public void handleMessage(android.os.Message msg) {
				boolean flag = SPUtils.getBoolean(SplashActivity.this, Constants.preferences.IS_FIRST, true);
				Intent intent = null;
				if(flag){
					//第一次进入程序,进入向导界面
					intent = new Intent(SplashActivity.this, GuideActivity.class);
					startActivity(intent);
					finish();
				} else {
					//进入主界面
					intent = new Intent(SplashActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				}
			};
		}.sendEmptyMessageDelayed(0, 4000);
		
		//更新升级
		
	}
}
