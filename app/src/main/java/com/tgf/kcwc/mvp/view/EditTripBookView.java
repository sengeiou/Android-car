package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.EditbookModel;
import com.tgf.kcwc.mvp.model.RideDataNodeItem;
import com.tgf.kcwc.mvp.model.Topic;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/5/4 0004
 * E-mail:hekescott@qq.com
 */

public interface EditTripBookView extends WrapView {
    void showEditTripbookView(EditbookModel mEditbookModel);

    void showTagList(ArrayList<Topic> topiclist);

    void showSuccess(String statusMessage);

    void showUnStop(ArrayList<RideDataNodeItem> nodeList);

    void showContinue(ArrayList<RideDataNodeItem> nodeList);

    void showDoFailed(String statusMessage);
}
