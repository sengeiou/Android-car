package com.tgf.kcwc.callback;

import android.view.View;
import android.view.ViewGroup;

import com.tgf.kcwc.mvp.model.Brand;

/**
 * Author：Jenny
 * Date:2017/4/12 16:28
 * E-mail：fishloveqin@gmail.com
 */

public interface FragmentDataCallback<T> {

    public void refresh( T t);

}
