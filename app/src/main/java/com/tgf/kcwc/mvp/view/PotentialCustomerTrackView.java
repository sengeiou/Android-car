package com.tgf.kcwc.mvp.view;


import com.tgf.kcwc.mvp.model.CustomerTrackModel;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/7/31 11:01
 */

public interface PotentialCustomerTrackView extends WrapView {

    void showTrackList(ArrayList<CustomerTrackModel.CustomerTrackItem> list);
}
