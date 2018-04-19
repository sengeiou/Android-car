package com.tgf.kcwc.mvp.model;

import java.util.ArrayList;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Auther: Scott
 * Date: 2017/2/6 0006
 * E-mail:hekescott@qq.com
 * 票务接口
 */

public interface TicketManageService {

    /**
     * 票务管理列表
     * @return
     */
    @GET("ticket/user/handOutList")
    Observable<ResponseMessage<TicketManageListModel>> getTicketManageList(@Query("token") String token);

    /**
     * 票务管理详情 分发id
     * @return
     */
    @GET("ticket/user/handOutDetails")
    Observable<ResponseMessage<TicketManageDetailModel>> getTicketManageDetail(@Query("token") String token,
                                                                               @Query("id") int id,
                                                                               @Query("is_need_tongji") int isTongji);

    /**
     * 票务分发对象 分发id
     * @return
     */
    @GET("ticket/user/handOutListUser")
    Observable<ResponseMessage<TicketSendObjModel>> getTicketSendObj(@Query("token") String token,
                                                                     @Query("id") int id);
    /**
     * 机构分发员工分发对象 分发id
     * @return
     */
    @GET("ticket/hand/handOutListRecord")
    Observable<ResponseMessage<TicketSendOrgObjModel>> getTicketSendOrgObj(@Query("token") String token,
                                                                     @Query("id") int id);

    /**
     * 票务分发对象展会信息 分发id
     * @return
     */
    @GET("ticket/user/handOutDetails")
    Observable<ResponseMessage<TicketExhibitModel>> getTicketExhibitInfo(@Query("token") String token,
                                                                         @Query("id") int id);

    /**
     * 票务 发送 分发id
     * @return
     */
    @FormUrlEncoded
    @POST("ticket/sendTicket")
    Observable<ResponseMessage> posTicketExhibitInfo(@Field("userId") String userId);

    /**
     * 票务 发送 分发id
     * type为1时id=门票id,为2时id=分发id,为3时id=赠票id
     * 
     * 	type 类型1购买2赠送3申领
     * @return
     */
    @GET("ticket/ticket/sendTicket")
    Observable<ResponseMessage> sendTicket(@Query("token") String token, @Query("id") String tfId,
                                           @Query("is_receive") int isReceive,
                                           @Query("mobile") String mobile,
                                           @Query("name") String name,
                                           @Query("nums") int nums,
                                           @Query("type") int type, @Query("re_send") int re_send,
                                           @Query("user_type") int user_type);
    @GET("ticket/ticket/sendTicket")
    Observable<ResponseMessage> sendTicket(@Query("token") String token, @Query("id") String tfId,
                                           @Query("is_receive") int isReceive,
                                           @Query("mobile") String mobile,
                                           @Query("name") String name,
                                           @Query("nums") String nums,
                                           @Query("receive_time_limit") int timelimit,
                                           @Query("type") int type,
                                           @Query("user_type") String user_type);
    @GET("ticket/ticket/sendTicket")
    Observable<ResponseMessage> sendTicket(@Query("token") String token, @Query("id") String tfId,
                                           @Query("is_receive") int isReceive,
                                           @Query("mobile") String mobile,
                                           @Query("sign") String sign,
                                           @Query("nums") String nums,
                                           @Query("type") int type,
                                           @Query("user_type") String user_type);

    /**
     * 机构发给员工 分发门票
     * @return
     */
    @GET("ticket/hand/save")
    Observable<ResponseMessage> sendTicket(@Query("token") String token, @Query("tfh_id") String tfId,
                                             @Query("nums") String nums,@Query("uid") String uid);
    /**
     * 票务  跟踪 分发id
     * @return
     */
    @GET("user/handOutUserDetails")
    Observable<ResponseMessage> getTicketUserRecord(@Query("token") String token, @Query("mobile") String mobile,
                                                    @Query("id") int id, @Query("user_type") int userType);
    /**
     * 票务  分发记录 分发id
     * @return
     */
    @GET("ticket/user/handOutListRecord")
    Observable<ResponseMessage<TicketSendRecordModel>> getTicketSendRecord(@Query("token") String token, @Query("id") int id);

    /**
     * 票务机构分发员工  分发记录 分发id
     * @return
     */
    @GET("ticket/hand/handOutListUser")
    Observable<ResponseMessage<TicketSendOrgRecordModel>> getTicketOrgSendRecord(@Query("token") String token, @Query("id") int id);
    /**
     * 票务  门票跟踪 分发id
     * @return
     */
    @GET("ticket/user/handOutUserDetails")
    Observable<ResponseMessage<TicketFellowModel>> getTicketFellow(@Query("token") String token, @Query("id") int id, @Query("mobile") String mobile, @Query("user_type") int user_type);

//    /**
//     * 票务  我的普通关注
//     * @return
//     */
//    @GET("user/friend")
//    Observable<ResponseMessage<MyFellowlistModel>> getMyFellow(@Query("token") String token);
    /**
     * 票务  限领量检查
     * @return
     */
    @GET("ticket/sendNums")
    Observable<ResponseMessage<ArrayList<CheckSendSeeModel>>> checkSendSeeUser(@Query("token") String token, @Query("id") int tfid, @Query("mobile") String mobile);
    /**
     * 票务  我的普通关注
     * @return
     */
    @GET("user/follow/followlist")
    Observable<ResponseMessage<MyFellowlistModel>> getMyFellow(@Query("token") String token,@Query("user_id") String userId);

    /**
     * 票务  我的普通关注
     * @return
     */
    @GET("ticket/user/workerList")
    Observable<ResponseMessage<ArrayList<ContactUser>>> getWorkerList(@Query("token") String token);
    /**
     * 票务管理列表 展会列表
     * @return
     */
    @GET("ticket/hand/eventList")
    Observable<ResponseMessage<ArrayList<TicketmExhibitModel>>> getTicketmExhibitlist(@Query("token") String token);
    /**
     * 票务管理列表 分发列表
     * @return
     */
    @GET("ticket/hand/lists")
    Observable<ResponseMessage<TicketmListModel>> getTicketmlist(@Query("token") String token,@Query("event_id") String eventId);
    /**
     * 票务管理列表 概览统计
     * @return
     */
    @GET("ticket/hand/handTongji")
    Observable<ResponseMessage<OrgGailanModel>> getTicketmOrgGailanTongji(@Query("token") String token,@Query("event_id") String eventId);
    /**
     * 票务管理列表 分发统计
     * @return
     */
    @GET("ticket/hand/handTongjiUser")
    Observable<ResponseMessage<OrgFenfaModel>> getTicketmOrgFenfaStatitics(@Query("token") String token,@Query("event_id") String eventId,@Query("user_name") String userName );
    /**
     * 票务管理列表 代发统计
     * @return
     */
    @GET("ticket/hand/handTongjiOrg")
    Observable<ResponseMessage<OrgDaifaModel>> getTicketmOrgDaifaStatitics(@Query("token") String token,@Query("event_id") String eventId,@Query("org_name") String userName );
    /**
     * 票务管理详情 机构发给员工
     * @return
     */
    @GET("ticket/hand/handOutDetails")
    Observable<ResponseMessage<TicketOrgManageDetailModel>> getTicketOrgManageDetail(@Query("token") String token,
                                                                               @Query("id") int id,
                                                                               @Query("is_need_tongji") int isTongji);
}
