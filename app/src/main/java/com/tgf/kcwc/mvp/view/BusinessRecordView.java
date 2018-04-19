package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.BusinessRecordModel;

/**
 * @anthor noti
 * @time 2017/8/15
 * @describle
 */
public interface BusinessRecordView extends WrapView {
    /**
     * 显示看车
     * @param seeItem
     */
    void showSeeCar(BusinessRecordModel.SeeItem seeItem);

    /**
     * 显示票证
     * @param ticketItem
     */
    void showTicket(BusinessRecordModel.TicketItem ticketItem);

    /**
     * 显示代金卷
     * @param couponItem
     */
    void showCoupon(BusinessRecordModel.CouponItem couponItem);

    /**
     * 显示活动
     * @param activityItem
     */
    void showActivity(BusinessRecordModel.ActivityItem activityItem);
    /**
     * 显示用户信息
     * @param basicInfoItem
     */
    void showInfo(BusinessRecordModel.BasicInfoItem basicInfoItem);
}
