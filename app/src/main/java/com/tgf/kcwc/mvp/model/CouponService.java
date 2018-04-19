package com.tgf.kcwc.mvp.model;

import com.tgf.kcwc.entity.DataItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
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

public interface CouponService {
    /**
     * 代金券推荐获取
     *
     * @return
     */
    @GET("coupon/recommend/lists")
    Observable<ResponseMessage<CouponRecomentModel>> getRecomentList(@Query("latitude") String latitude,
                                                                     @Query("longitude") String longitude);

    /**
     * 代金券分类列表
     *
     * @return
     */
    @GET("coupon/category/lists")
    Observable<ResponseMessage<ArrayList<CouponCategory>>> getCategoryList();

    /**
     * 代金券详情
     *
     * @return
     */
    @GET("coupon/detail")
    Observable<ResponseMessage<CouponDetailModel>> getCouponDetail(@Query("coupon_id") int couponId,
                                                                   @Query("latitude") String latitude,
                                                                   @Query("longitude") String longitude, @Query("token") String token);

    /**
     * 代金券附近
     *
     * @return
     */
    @GET("coupon/nearby/lists")
    Observable<ResponseMessage<CouponNearModel>> getCouponNear(@Query("page") int page,
                                                               @Query("latitude") String latitude,
                                                               @Query("longitude") String longitude,
                                                               @Query("distance") int distance, @Query("category_id") int categoryId,
                                                               @Query("brand_id") int brandId, @Query("order") int order);

    /**
     * 获取我的代金券列表
     *
     * @param token
     * @param isExpiration
     * @return
     */
    @GET("coupon/my/lists")
    Observable<ResponseMessage<List<Coupon>>> getMyCoupons(@Query("token") String token,
                                                           @Query("is_expiration") String isExpiration);

    /**
     * 获取我的代金券列表
     *
     * @param token
     * @param isExpiration
     * @return
     */
    @GET("coupon/order/my")
    Observable<ResponseMessage<MyCouponModel>> getMyCoupons(@Query("token") String token,
                                                            @Query("is_expiration") String isExpiration, @Query("page") int page, @Query("pageSize") int pageSize);

    /**
     * 获取代金券订单详情
     *
     * @param token
     * @return
     */
    @GET("coupon/order/detail")
    Observable<ResponseMessage<CouponOrderDetailModel>> getCouponOderDetal(@Query("token") String token,
                                                                           @Query("order_id") int orderId,
                                                                           @Query("latitude") String lat,
                                                                           @Query("longitude") String lng);
    /**
     * 提交代金券评价
     *
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("comment/comment/publishDiscussion")
    Observable<ResponseMessage> postCouponEvalue(@Field("token") String token,
                                                                           @Field("star_service") int starService,
                                                                           @Field("star") int star,
                                                                           @Field("invisible") int invisible,
                                                                           @Field("star_shop") int starShop,
                                                                           @Field("resource_id") int resourceId,
                                                                           @Field("module") String module,
                                                                           @Field("text") String text,
                                                                           @Field("imgs") String imgs);

    /**
     * 代金券领取/购买
     *
     * @param couponId
     * @param distributeListId
     * @param payType
     * @param num
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("coupon/send/get_coupon")
    Observable<ResponseMessage<TransactionModel>> payCouponOrder(@Field("coupon_id") String couponId,
                                                                 @Field("distribute_list_id") String distributeListId,
                                                                 @Field("pay_type") String payType,
                                                                 @Field("num") String num,
                                                                 @Field("token") String token);

    @GET("coupon/exclusive/lists")
    Observable<ResponseMessage<List<ExclusiveCoupon>>> getExclusiveCoupons(@Query("token") String token,
                                                                 @Query("is_expiration") String isExpiration);

    /**
     * 代金券在线
     */
    @GET("coupon/online/lists")
    Observable<ResponseMessage<OnlineCoupon>> getOnlineCoupon(@Query("token") String token, @Query("page") int page,
                                                              @Query("pageSize")int pageSize,@Query("category_id") Integer categoryId,
                                                              @Query("brand_id")Integer brandId,@Query("distance") Integer distanceId ,
                                                              @Query("order") Integer orderId);

//&category_id=3&distance=1&order=1
    /**
     * 代金券列表过滤
     *
     * @return int categoryId,int brandId,int cityId,int distanceId,int orderId, String latitude,String longitude, int page
     */
    @GET("coupon/category/coupon_lists")
    Observable<ResponseMessage<CouponCategoryListModel>> getCouponCategorylist(@Query("category_id") int categoryId, @Query("brand_id") int brandId
            , @Query("city_id") int cityId, @Query("distance") int distanceId, @Query("order") int order, @Query("latitude") String latitude
            , @Query("longitude") String longitude, @Query("page") int page);

    /**
     * 代金券排序过滤
     *
     * @return
     */
    @GET("coupon/search/order")
    Observable<ResponseMessage<ArrayList<DataItem>>> getCouponRankOrder();

    /**
     * 代金券附近过滤
     *
     * @return
     */
    @GET("coupon/search/distance")
    Observable<ResponseMessage<CouponDistanceFilterModel>> getCouponDistanceFilter();

    @GET("publics/area")
    Observable<ResponseMessage<List<DataItem>>> getAreaDatas(@Query("parent_area") String tag);

    /**
     * 代金券申请退款
     *
     * @return
     */
    @GET("coupon/order/can_refund")
    Observable<ResponseMessage<CouponRefoundModel>> getRefondInfo(@Query("token") String token, @Query("order_id") int orderId);

    /**
     * 代金券提交申请退款
     *
     * @return
     */
    @POST("coupon/order/reply_refund")
    @FormUrlEncoded
    Observable<ResponseMessage> postRefond(@Field("token") String token, @Field("code_ids") String codeIds, @Field("refund_type") int type,
                                           @Field("remark") String why, @Field("remark_text") String backNote);

    /**
     * 代金券退款详情
     *
     * @return
     */
    @GET("coupon/order/refund_detail")
    Observable<ResponseMessage<RefondDetailModel>> getRefondDetail(@Query("token") String token, @Query("code_id") int codeId);

    /**
     * 扫码核销列表
     *
     * @return
     */
    @GET("coupon/check/check_list")
    Observable<ResponseMessage<ScanOffListModel>> getScanoffList(@Query("token") String token);

    /**
     * 扫码核销明细
     *
     * @return
     */
    @GET("coupon/check/check_detail")
    Observable<ResponseMessage<ScanOffDetailModel>> getScanoffDetail(@Query("token") String token, @Query("coupon_id") String coupoid);


    /**
     * //购票相关代金券
     *
     * @param eventId
     * @param token
     * @return
     */
    @GET("coupon/send/event_can_use")
    Observable<ResponseMessage<List<TicketCoupon>>> getTicketCoupons(@Query("event_id") String eventId, @Query("token") String token);


    /**
     * 领取票的代金券
     *
     * @param params
     * @return
     */
    @POST("coupon/send/get_coupon")
    @FormUrlEncoded
    Observable<ResponseMessage<Object>> getCoupon(@FieldMap Map<String, String> params);
}
