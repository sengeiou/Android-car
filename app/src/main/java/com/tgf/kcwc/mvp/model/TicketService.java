package com.tgf.kcwc.mvp.model;

import java.util.List;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Author：Jenny
 * Date:2017/1/5 20:50
 * E-mail：fishloveqin@gmail.com
 */

public interface TicketService {

    @GET("ticket/order/orderDetails")
    Observable<ResponseMessage<OrderModel>> getOrderDetails(@Query("id") String id,
                                                            @Query("token") String token);

    /**
     * @param senseId
     * @param id
     * @param payStatus
     * @param type
     * @param status
     * @param token
     * @return
     */
    @GET("ticket/user/lists")
    Observable<ResponseMessage<MyTicketListModel>> myTicketList(@Query("event_id") String senseId,
                                                                @Query("id") String id,
                                                                @Query("pay_status") String payStatus,
                                                                @Query("type") String type,
                                                                @Query("status") String status,
                                                                @Query("token") String token);

    /**
     * 赠票列表
     *
     * @param senseId
     * @param id
     * @param status
     * @param token
     * @return
     */
    @GET("ticket/user/freeList")
    Observable<ResponseMessage<FreeTicketListModel>> freeTicketList(@Query("event_id") String senseId,
                                                                    @Query("id") String id,
                                                                    @Query("status") String status,
                                                                    @Query("token") String token);

    /**
     * 赠票详情
     *
     * @param id
     * @param token
     * @return
     */
    @GET("ticket/user/freeDetails")
    Observable<ResponseMessage<TicketDetailModel>> freeTicketDetail(@Query("id") String id,
                                                                    @Query("token") String token);

    /**
     * 领取门票
     *
     * @param ids
     * @param token
     * @return
     */
    @GET("ticket/user/receiveTicket")
    Observable<ResponseMessage<Object>> receiveTickets(@Query("ids") String ids,
                                                       @Query("token") String token);

    /**
     * 购票列表
     *
     * @param senseId 展会id
     * @param id      门票id
     * @param token   用户token
     * @return
     */
    @GET("ticket/ticket/lists")
    Observable<ResponseMessage<BuyTicketModel>> buyTicketList(@Query("id") String id,
                                                              @Query("event_id") String senseId,
                                                              @Query("token") String token);

    /**
     * 创建订单
     *
     * @param tIds
     * @param nums
     * @param token
     * @return
     */
    @GET("ticket/order/createOrder")
    Observable<ResponseMessage<Order>> generateOrder(@Query("tid") String tIds,
                                                     @Query("nums") String nums,
                                                     @Query("token") String token, @Query("trade_type") String type);

    /**
     * 获取微信支付后台参数
     *
     * @param id
     * @param from
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("ticket/order/getPayApi")
    Observable<ResponseMessage<OrderPayParam>> getWXPayParams(@Field("id") String id,
                                                              @Field("trade_type") String from,
                                                              @Field("token") String token);

    @FormUrlEncoded
    @POST("ticket/order/saveOrder")
    Observable<ResponseMessage<OrderPayParam>> getWXPayParams(@FieldMap Map<String, String> params);

    @GET("ticket/order/orderPayStatus")
    Observable<ResponseMessage<OrderModel>> queryOrderStatus(@Query("id") String orderId,
                                                             @Query("token") String token);

    @GET("purse/recharge/singleQuery")
    Observable<ResponseMessage<Object>> findPrePaidQueryByOrderId(@Query("order_sn") String orderId,
                                                             @Query("token") String token);

    @GET("coupon/send/order_pay_status")
    Observable<ResponseMessage<CouponPayModel>> queryCouponOrderStatus(@Query("order_id") String orderId,
                                                                       @Query("token") String token);

    @GET("ticket/ticket/freeList")
    Observable<ResponseMessage<PreTicketModel>> preRegTicketList(@Query("event_id") String senseId,
                                                                 @Query("type") String type,
                                                                 @Query("qd_id") String channelId,
                                                                 @Query("id") String id,
                                                                 @Query("tid") String tid,
                                                                 @Query("token") String token);

    @GET("ticket/ticket/applyStatus")
    Observable<ResponseMessage<Object>> applyStatus(@Query("event_id") String senseId,
                                                    @Query("qd_id") String qdId,
                                                    @Query("token") String token);

    @FormUrlEncoded
    @POST("ticket/ticket/addApply")
    Observable<ResponseMessage<List<String>>> createForms(@FieldMap Map<String, String> params);

    @GET("ticket/ticket/information")
    Observable<ResponseMessage<AboutTicketModel>> getAboutTicket(@Query("event_id") String eventId,
                                                                 @Query("token") String token);


    /**
     * 退款
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("ticket/ticket/createRefund")
    Observable<ResponseMessage<Object>> createRefund(@FieldMap Map<String, String> params);


    /**
     * 退款详情
     *
     * @param id
     * @param token
     * @return
     */
    @GET("ticket/ticket/refundDetails")
    Observable<ResponseMessage<TicketRefundDetailModel>> getTicketRefundDetail(@Query("tu_id") String id,

                                                                               @Query("token") String token);

    /**
     * 获取支付宝支付参数数据
     *
     * @param token
     * @return
     */

    @GET("ticket/order/getAlipayApi")
    Observable<ResponseMessage<Object>> loadPayData(@Query("token") String token, @Query("id") String id);


    /**
     * 获取购票订单数据
     *
     * @param params
     * @return
     */
    @GET("ticket/order/orderDiscount")
    Observable<ResponseMessage<TicketOrderModel>> getTicketOrderDiscountInfo(@QueryMap Map<String, String> params);


    /**
     * 票的转送
     *
     * @param params
     * @return
     */
    @GET("ticket/ticket/ticketSend")
    Observable<ResponseMessage<Object>> setTicketForward(@QueryMap Map<String, String> params);


    /**
     * 获取报名参展列表
     *
     * @param params
     * @return
     */
    @GET("ticket/api/applyLists")
    Observable<ResponseMessage<ApplyTicketModel>> getApplyList(@QueryMap Map<String, String> params);


    /**
     * 获取推广广告信息
     *
     * @param params
     * @return
     */
    @GET("ticket/api/getFavText")
    Observable<ResponseMessage<String>> getADInfo(@QueryMap Map<String, String> params);

    /**
     * 获取推广广告信息
     *
     * @param params
     * @return
     */
    @GET("ticket/api/information")
    Observable<ResponseMessage<TicketDescModel>> getTicketDescInfo(@QueryMap Map<String, String> params);

    /**
     * 获取报名参展列表
     *
     * @param params
     * @return
     */
    @GET("cert/api/applyLists")
    Observable<ResponseMessage<ApplyTicketModel>> getCertApplyList(@QueryMap Map<String, String> params);

}
