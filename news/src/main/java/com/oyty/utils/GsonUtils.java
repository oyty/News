package com.oyty.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class GsonUtils {

	public static <T> T json2Bean(String result , Class<T> clazz){
		Gson gson = new Gson();
		T t = gson.fromJson(result, clazz);
		return t;
	}

    public static String bean2Json(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static <T> String array2Json(List<T> lists) {
        Type listType = new TypeToken<List<T>>(){}.getType();
        Gson gson = new Gson();
        return gson.toJson(lists, listType);
    }

    public static <T> List<T>  json2Array(String result, TypeToken<List<T>> typeToken) {
        Gson gson = new Gson();
        return gson.fromJson(result, typeToken.getType());
    }

}
