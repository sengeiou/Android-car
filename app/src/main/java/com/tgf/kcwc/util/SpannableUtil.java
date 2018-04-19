package com.tgf.kcwc.util;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;

/**
 * Auther: Scott
 * Date: 2017/1/5 0005
 * E-mail:hekescott@qq.com
 */

public class SpannableUtil {
    //给String设置中划线
    public static SpannableString getDelLineString(String string) {
        SpannableString spannableString = new SpannableString(string);
        spannableString.setSpan(new StrikethroughSpan(), 0, string.length(),
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
    //给String设置中划线 从1开始
    public static SpannableString getDelLineString(int startIdex,String string) {
        SpannableString spannableString = new SpannableString(string);
        spannableString.setSpan(new StrikethroughSpan(), startIdex, string.length(),
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
    public static SpannableString getColorString(String string) {
        SpannableString spannableString = new SpannableString(string);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 0, string.length(),
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
    public static SpannableString getColorString(int color,String string) {
        SpannableString spannableString = new SpannableString(string);
        spannableString.setSpan(new ForegroundColorSpan(color), 0, string.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     *缩小第一个字符
     * @param context context
     * @param string
     * @param sizeDp 第一个字符大小
     * @return
     */
    public static SpannableString getSpanMoneyString(Context context,String string, int sizeDp) {
        SpannableString spannableString = new SpannableString(string);
        spannableString.setSpan(new AbsoluteSizeSpan(DensityUtils.dip2px(context,sizeDp)), 0, 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
