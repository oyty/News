package com.oyty.base;

import android.content.Context;

import com.oyty.utils.SPUtils;

/**
 * Created by oyty on 5/12/15.
 */
public class Config {

    /**
     * 是否显示图片
     * @param context
     * @return
     */
    public boolean isLoadImage(Context context) {
        return SPUtils.getBoolean(context, Constants.Preferences.IS_LOAD_IMAGE, true);
    }
}
