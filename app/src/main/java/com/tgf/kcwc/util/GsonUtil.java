package com.tgf.kcwc.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Author:Jenny
 * Date:2017/5/9
 * E-mail:fishloveqin@gmail.com
 *
 * Gson工具处理json格式工具类
 */

public final class GsonUtil {

    public static <T> String objToJson(T t) {

        Gson gson = new Gson();
        String json = gson.toJson(t);
        return json;
    }

    public static <T> T jsonToObject(String json, Type type) {
        Gson gson = new Gson();

        return gson.fromJson(json, type);

    }
}
