package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.ExhibitEvent;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/1/22 0022
 * E-mail:hekescott@qq.com
 */

public interface ExhibitEventDetailView extends  WrapView {
    public  void showHead(ExhibitEvent exhibitEvent);
    public  void showGuestList(ArrayList<ExhibitEvent.Guest> guestArrayList);
    public  void showQzoneList(ArrayList<ExhibitEvent.QzoneItem> qzoneArrayList);
}
