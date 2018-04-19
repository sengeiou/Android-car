package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.RideDataModel;
import com.tgf.kcwc.mvp.model.RideDataNodeItem;
import com.tgf.kcwc.mvp.model.RideReportDetailModel;
import com.tgf.kcwc.mvp.model.Topic;
import com.tgf.kcwc.mvp.model.TripBookDetailModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/5/11 0011
 * E-mail:hekescott@qq.com
 */

public interface TripBookDetailView extends WrapView{
    void showTitle(TripBookDetailModel tripBookDetailModel);
    void showRideReport(RideReportDetailModel.RideBean rideReport);
    void showUserInfo(TripBookDetailModel.User userInfo);
    void showRoadLineDesc(List<TripBookDetailModel.NodeDesc> lineList);
    void showRecomdInfo(TripBookDetailModel.Recomment recommendInfo);
    void showTagList(ArrayList<Topic> topiclist);
    void showHonorList(List<TripBookDetailModel.Honor> honorList);

    void showUnStop(RideDataModel unstopNode);

    void showDeleteSuccess();

}
