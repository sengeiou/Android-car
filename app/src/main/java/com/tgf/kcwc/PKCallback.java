package com.tgf.kcwc;

/**
 * Author：Jenny
 * Date:2017/1/3 11:34
 * E-mail：fishloveqin@gmail.com
 */

public interface PKCallback<T> {

    void onRefresh(int pkNums,T item);
}
