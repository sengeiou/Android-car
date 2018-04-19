package com.tgf.kcwc.see;

import android.support.annotation.NonNull;

/**
 * Author：Jenny
 * Date:2016/12/12 17:50
 * E-mail：fishloveqin@gmail.com
 */
public class TitleItem  {
    @NonNull
    public String  text;
    @NonNull
    public String  url;
    @NonNull
    public boolean isSplitLine;

    public TitleItem(@NonNull String text, @NonNull String url, boolean isSplitLine) {
        this.text = text;
        this.url = url;
        this.isSplitLine = isSplitLine;
    }
}