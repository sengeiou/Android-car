package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.ExhibitionPicsModel;
import com.tgf.kcwc.mvp.model.ImgItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/1/20 0020
 * E-mail:hekescott@qq.com
 */

public interface ExhibitionAlbumView extends WrapView {

    void showExhbitionPics(ArrayList<ImgItem> exhibitImgesList);
}
