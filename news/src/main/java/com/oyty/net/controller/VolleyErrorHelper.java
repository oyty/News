package com.oyty.net.controller;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.oyty.news.R;


/**
 * 网络异常提示帮助类
 */
public class VolleyErrorHelper {

    public static String getMessage(Context context, Object error) {
        if (isServerProblem(error)) {
            return context.getResources()
                    .getString(R.string.server_other_error);
        } else if (error instanceof TimeoutError) {
            return context.getResources().getString(R.string.server_timeout);
        } else if (error instanceof ParseError) {
            return context.getResources()
                    .getString(R.string.server_parse_error);
        }
        return context.getResources().getString(R.string.server_other_error);
    }

    private static boolean isServerProblem(Object error) {
        return ((error instanceof NetworkError) || error instanceof ServerError)
                || (error instanceof AuthFailureError);
    }

}
