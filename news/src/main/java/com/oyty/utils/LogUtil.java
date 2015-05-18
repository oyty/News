package com.oyty.utils;

import android.util.Log;

/**
 * 日志工具类
 */
public class LogUtil {

    private static final int logLevel = Log.VERBOSE;

    private static LogUtil logger = null;

    private static final String PREFIX_NAME = "syt_";

    /**
     * 日志开关 true:打开; false：关闭
     */
    public static final boolean ISDEBUG = true;

    private LogUtil() {

    }

    public static LogUtil getLogger() {
        if (logger == null) {
            logger = new LogUtil();
        }
        return logger;
    }

    private String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return null;
        }

        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }

            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }

            if (st.getClassName().equals(this.getClass().getName())) {
                continue;
            }

            return "[" + Thread.currentThread().getName() + ":"
                    + st.getClassName() + ":" + st.getLineNumber() + "]";
        }

        return null;
    }

    public void v(String tag, Object o) {
        if (!ISDEBUG)
            return;
        if (logLevel <= Log.VERBOSE) {
            String name = getFunctionName();
            if (name != null) {
                Log.v(PREFIX_NAME + tag, name + " - " + o);
            } else {
                Log.v(PREFIX_NAME + tag, o.toString());
            }
        }
    }

    public void d(String tag, Object o) {
        if (!ISDEBUG)
            return;
        if (logLevel <= Log.DEBUG) {
            String name = getFunctionName();
            if (name != null) {
                Log.d(PREFIX_NAME + tag, name + " - " + o);
            } else {
                Log.d(PREFIX_NAME + tag, o.toString());
            }
        }
    }

    public void i(String tag, Object o) {
        if (!ISDEBUG) {
            return;
        }
        if (logLevel <= Log.INFO) {
            String name = getFunctionName();
            if (name != null) {
                Log.i(PREFIX_NAME + tag, name + "-" + o);
            } else {
                Log.i(PREFIX_NAME + tag, o.toString());
            }
        }
    }

    public void w(String tag, Object o) {
        if (!ISDEBUG)
            return;
        if (logLevel <= Log.WARN) {
            String name = getFunctionName();
            if (name != null) {
                Log.w(PREFIX_NAME + tag, name + " - " + o);
            } else {
                Log.w(PREFIX_NAME + tag, o.toString());
            }
        }
    }

    public void e(String tag, Object o) {
        if (!ISDEBUG)
            return;
        if (logLevel <= Log.ERROR) {
            String name = getFunctionName();
            if (name != null) {
                Log.e(PREFIX_NAME + tag, name + " - " + o);
            } else {
                Log.e(PREFIX_NAME + tag, o.toString());
            }
        }

    }

    public void e(String tag, Exception ex) {
        if (!ISDEBUG)
            return;
        if (logLevel <= Log.ERROR) {
            Log.e(PREFIX_NAME + tag, "error", ex);
        }
    }

}
