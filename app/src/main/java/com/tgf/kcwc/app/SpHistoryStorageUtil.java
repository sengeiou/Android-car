package com.tgf.kcwc.app;

import android.content.Context;

import com.amap.api.services.core.PoiItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tgf.kcwc.util.GsonUtil;
import com.tgf.kcwc.util.SharedPreferenceUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Auther: Scott
 * Date: 2017/11/21 0021
 * E-mail:hekescott@qq.com
 */

public class SpHistoryStorageUtil {
    private Context context;
    private int HISTORY_MAX;
    private static SpHistoryStorageUtil instance;
    private Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
            .create();

    private SpHistoryStorageUtil(Context context, int historyMax) {
        this.context = context.getApplicationContext();
        this.HISTORY_MAX = historyMax;
    }

    public static synchronized SpHistoryStorageUtil getInstance(Context context, int historyMax) {
        if (instance == null) {
            synchronized (SpHistoryStorageUtil.class) {
                if (instance == null) {
                    instance = new SpHistoryStorageUtil(context, historyMax);
                }
            }
        }
        return instance;
    }

    private static SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    public String generateKey() {
        return mFormat.format(new Date());
    }

    public ArrayList<PoiItem> sortHistory() {
        ArrayList<PoiItem> mResults = new ArrayList<>();
        Map<String, PoiItem> hisAll = (Map<String, PoiItem>) getAll();
        //将key排序升序
        if (hisAll != null && hisAll.size() != 0) {


            Object[] keys = hisAll.keySet().toArray();
            Arrays.sort(keys);
            int keyLeng = keys.length;
            //这里计算 如果历史记录条数是大于 可以显示的最大条数，则用最大条数做循环条件，防止历史记录条数-最大条数为负值，数组越界
            int hisLeng = keyLeng > HISTORY_MAX ? HISTORY_MAX : keyLeng;
            for (int i = hisLeng - 1; i >= 0; i--) {
                mResults.add(hisAll.get(keys[i]));
            }
        }
        return mResults;
    }

    public Map<String, ?> getAll() {
        Map<String, PoiItem> retMap = new HashMap<>();
        String mapString = SharedPreferenceUtil.getSearchHistory(context);
        Map<String, PoiItem> tempMap = gson.fromJson(mapString, new TypeToken<Map<String, PoiItem>>() {
        }.getType());
        if (tempMap != null) {
            retMap = tempMap;
            for (String p : retMap.keySet()) {
                System.out.println("key:" + p + " values:" + retMap.get(p).getAdName());
            }
        }

        return retMap;
    }

    public void save(PoiItem value) {
        Map<String, PoiItem> historys = (Map<String, PoiItem>) getAll();

        if (historys.size() > 1) {
            Iterator<Map.Entry<String, PoiItem>> it = historys.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, PoiItem> entry = it.next();
                if (value.equals(entry.getValue())) {
                    it.remove();
                }
            }

//            for (Map.Entry<String, PoiItem> entry : historys.entrySet()) {
//                if (value.equals(entry.getValue())) {
//                    historys.remove(entry.getKey());
//                }
//            }
        }
        historys.put(generateKey(), value);
        String gsonStr = gson.toJson(historys);
        SharedPreferenceUtil.putSearchHistory(context, gsonStr);
        for (String p : historys.keySet()) {
            System.out.println("key:" + p + " values:" + historys.get(p).getAdName());
        }
//        put(generateKey(), value);
    }

//    public void remove(String key) {
//
//        SharedPreferences sp = context.getSearchHistory(SEARCH_HISTORY,
//                Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.remove(key);
//        editor.commit();
//    }

    public void clear() {
        SharedPreferenceUtil.putSearchHistory(context, "");
    }
}
