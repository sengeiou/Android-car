package com.tgf.kcwc.mvp.model;

import com.tgf.kcwc.entity.DataItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Auther: Scott
 * Date: 2017/1/10 0010
 * E-mail:hekescott@qq.com
 */

public interface ManageCouponService {
    /**
     * 代金券  管理列表
     * @return
     */
    @GET("coupon/distribute/lists")
    Observable<ResponseMessage<CouponManageListModel>> getCouponManageList(@Query("token") String token);
    /**
     * 票务 发送 分发id
     * @return
     */
    @FormUrlEncoded
    @POST("coupon/send/distribute")
    Observable<ResponseMessage> sendCoupon(@Field("coupon_id") String couponId);
    /**
     * 代金券  管理详情
     * @return
     */
    @GET("coupon/distribute/detail")
    Observable<ResponseMessage<CouponManageDetailModel>> getCouponManageDetail(@Query("token") String token,@Query("coupon_id") int couponId);
    /**
     * 代金券  分发对象查询
     * @return
     */
    @GET("coupon/distribute/object")
    Observable<ResponseMessage<ArrayList<CouponSendObjModel>>> getSendObjRecord(@Query("token") String token,@Query("coupon_id") int couponId);
    /**
     * 代金券  id对应的展会
     * @return
     */
    @GET("coupon/distribute/info")
    Observable<ResponseMessage<CouponEventModel>> getCouponEvent(@Query("token") String token,@Query("coupon_id") int couponId);
    /**
     * 代金券  id对应的展会
     * @return
     */
    @GET("coupon/distribute/record")
    Observable<ResponseMessage<ArrayList<CouponSendRecordModel>>> getCouponSendRecord(@Query("token") String token,@Query("coupon_id") int couponId);
    /**
     * 代金券 补发  列表id
     * @return
     */
    @FormUrlEncoded
    @POST("coupon/send/reissue")
    Observable<ResponseMessage> reSendCoupon(@Field("token") String token,@Field("distribute_list_id") int listId,@Field("num") int nums );
    /**
     * 代金券 发送  列表id
     * @return
     */
    @FormUrlEncoded
    @POST("coupon/send/distribute")
    Observable<ResponseMessage> sendCoupon(@Field("token") String token,@Field("coupon_id") int couponId,@Field("distribute_id") int distributeId,@Field("get_time_limit") int timelimit,@Field("users") String users);
    /**
     * 代金券  限领量检查
     * @return
     */
    @GET("coupon/distribute/get_surplus")
    Observable<ResponseMessage<ArrayList<CheckSendSeeModel>>> checkCouponSeeUser(@Query("token") String token,@Query("distribute_id") int tfid,@Query("mobile") String mobile);
    /**
     * 代金券  跟踪
     * @return
     */
    @GET("coupon/distribute/tail")
    Observable<ResponseMessage<CouponFellowModel>> getCouponTail(@Query("token") String token,@Query("coupon_id") int couponId,@Query("tel") String mobile);
    /**
     * 扫码核销列表
     * @return
     */
    @GET("coupon/check/check_list")
    Observable<ResponseMessage<ScanOffListModel>> getScanoffList(@Query("token") String token);
    /**
     * 扫码核销明细
     * @return
     */
    @GET("coupon/check/check_detail")
    Observable<ResponseMessage<ScanOffDetailModel>> getScanoffDetail(@Query("coupon_id") String coupoid);
    /**
     * 扫码核销
     * @return
     */
    @FormUrlEncoded
    @POST("coupon/check/scan_check")
    Observable<ResponseMessage<CouponScanOffResultModel>> getScanoffResult(@Field("token") String token,@Field("code") String code);

    /**
     * 扫码分发
     * @return
     */
    @FormUrlEncoded
    @POST("coupon/send/scan_get_coupon")
    Observable<ResponseMessage> postScanSend(@Field("token") String token,
                                            @Field("coupon_id") String couponId,
                                            @Field("distribute_id") String distributeId,
                                            @Field("num") String num);

}
