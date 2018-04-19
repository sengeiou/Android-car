package com.tgf.kcwc.mvp.view;

/**
 * Author：Jenny
 * Date:2017/3/9 14:03
 * E-mail：fishloveqin@gmail.com
 */

public interface AttentionView<T> extends WrapView {

    public void showAddAttention(T t);
    public void showCancelAttention(T t);

}
