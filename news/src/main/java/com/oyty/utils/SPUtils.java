package com.oyty.utils;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtils {
	private static String SP_NAME = "config";
	private static SharedPreferences sp;

	public static void putBoolean(Context context, String key, Boolean value) {
		if(sp == null) {
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		}
		sp.edit().putBoolean(key, value).commit();
	}

	public static boolean getBoolean(Context context, String key, Boolean defValue) {
		if(sp == null) {
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		}
		return sp.getBoolean(key, defValue);
	}

	public static void putString(Context context, String key, String value) {
		if(sp == null) {
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		}
		if (key == null) {
			return;
		}
		sp.edit().putString(key, value).commit();
	}

	public static String getString(Context context, String key, String defValue) {
		if(sp == null) {
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		}
		return sp.getString(key, defValue);
	}

	public static void putInt(Context context, String key, int value) {
		if(sp == null) {
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		}
		sp.edit().putInt(key, value).commit();
	}

	public static int getInt(Context context, String key, int defValue) {
		if(sp == null) {
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		}
		return sp.getInt(key, defValue);
	}

	public static Map<String, ?> getAll(Context context) {
		if(sp == null) {
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		}
		return sp.getAll();
	}
}
