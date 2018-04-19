package com.tgf.kcwc.mvp.view;

/**
 * Auther: Scott
 * Date: 2017/7/24 0024
 * E-mail:hekescott@qq.com
 */

public interface FavoriteView extends WrapView {
    void addFavoriteSuccess(Object data);

    void cancelFavorite(Object data);
}
