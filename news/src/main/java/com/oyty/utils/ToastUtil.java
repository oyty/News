/*******************************************************************************
 *    系统名称   ：速运通App
 *    客户           ： 速运通研发人员
 *    文件名       ： ToastUtil.java
 *              (C) Copyright sf_Express Corporation 2014
 *               All Rights Reserved.
 * *****************************************************************************
 *    注意： 本内容仅限于顺丰速运资讯科技本部IT产品中心内部使用，禁止转发
 ******************************************************************************/
package com.oyty.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.oyty.news.R;


/**
 * @author 479716
 * @see
 * @since
 * @date 2014-6-25下午4:28:15
 */
public class ToastUtil {

	private static Toast mToast;

	private static Object mSynObj = new Object();

	public static void showToast(final Context context, final String msg,
			final int duration) {
		synchronized (mSynObj) {
			if (mToast == null) {
				mToast = Toast.makeText(context, msg, duration);
			} else {
				mToast.setText(msg);
				mToast.setDuration(duration);
			}
			View view = mToast.getView();
			view.setBackgroundResource(R.drawable.round_corner_half_transparent);
			mToast.show();
		}
	}

	public static void showToast(final Context context, final String msg) {
		if(!TextUtils.isEmpty(msg))
			showToast(context, msg, Toast.LENGTH_SHORT);
	}

	public static void showToast(final Context context, final int msgId) {
		showToast(context, context.getString(msgId));
	}

	public static Toast getmToast() {
		return mToast;
	}

}
