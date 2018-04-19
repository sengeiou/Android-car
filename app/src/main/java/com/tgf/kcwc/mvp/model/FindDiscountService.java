package com.tgf.kcwc.mvp.model;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 找优惠接口
 * Auther: Scott
 * Date: 2017/4/25 0025
 * E-mail:hekescott@qq.com
 */

public interface FindDiscountService {
    /**
     * 限时优惠
     *
     * @return
     */
    @GET("privilege/special/lists")
    Observable<ResponseMessage<DiscountLimitModel>> getDiscountLimitList(@Query("event_id") Integer eventId,
                                                                         @Query("cityid") Integer cityid,
                                                                         @Query("end_price") Integer endPrice,
                                                                         @Query("factory_id") Integer factoryId,
                                                                         @Query("order") Integer order,
                                                                         @Query("price_range") Integer priceRange,
                                                                         @Query("special") Integer special,
                                                                         @Query("start_price") Integer startPrice,
                                                                         @Query("car_series") String seriesId, @Query("car") String carId,
                                                                         @Query("page") int page, @Query("pageSize") int pageSize);

    /**
     * 找优惠代金券
     *
     * @return
     */
    @GET("privilege/coupon/lists")
    Observable<ResponseMessage<DiscountCouponModel>> getDiscountCouponList(@Query("cityid") Integer cityid,
                                                                           @Query("end_price") Integer endPrice,
                                                                           @Query("factory_id") Integer factoryId,
                                                                           @Query("order") Integer order,
                                                                           @Query("price_range") Integer priceRange,
                                                                           @Query("special") Integer special,
                                                                           @Query("start_price") Integer startPrice);

    /**
     * 找优惠购车有礼
     *
     * @return
     */
    @GET("privilege/gift/lists")
    Observable<ResponseMessage<DiscountGiftModel>> getDiscountGiftList(@Query("cityid") Integer cityid,
                                                                       @Query("end_price") Integer endPrice,
                                                                       @Query("factory_id") Integer factoryId,
                                                                       @Query("order") Integer order,
                                                                       @Query("price_range") Integer priceRange,
                                                                       @Query("special") Integer special,
                                                                       @Query("start_price") Integer startPrice);

    /**
     * 礼包详情
     *
     * @return
     */
    @GET("privilege/gift/detail")
    Observable<ResponseMessage<GiftPackDetailModel>> getGiftPackDetail(@Query("id") Integer id);

    /**
     * 限时优惠详情
     *
     * @return
     */
    @GET("privilege/special/detail")
    Observable<ResponseMessage<LimitDiscountDetailModel>> getDiscountLimitDetail(@Query("id") Integer id);

    /**
     * 限时优惠列表 （重构）
     *
     * @return
     */
    @GET("store/benefit/lists")
    Observable<ResponseMessage<DiscountLimitNewModel>> getDataLists(@Query("seat_num_range_key") String seat_num_range_key, @Query("brand_id") String brand_id, @Query("price_range_max") String price_range_max,
                                                                    @Query("price_range_min") String price_range_min, @Query("page") Integer page, @Query("city_id") String city_id);

    /**
     * 展会特惠 （重构）
     *
     * @return
     */
    @GET("event/benefit/lists")
    Observable<ResponseMessage<LimitDiscountEveModel>> getEventDataLists(@Query("event_id") String event_id, @Query("page") Integer page, @Query("factory_id") String factory_id);

    /**
     * 展会列表
     *
     * @return
     */
    @GET("event/benefit/eventlist")
    Observable<ResponseMessage<List<SelectExbitionModel>>> getEventList();

    /**
     * 展会详情（展会）
     *
     * @return
     */
    @GET("/event/benefit/detail")
    Observable<ResponseMessage<LimitDiscountNewDetailsModel>> getEventDetail(@Query("benefit_id") String benefit_id);

    /**
     * 展会详情（限时）
     *
     * @return
     */
    @GET("/store/benefit/detail")
    Observable<ResponseMessage<LimitDiscountNewDetailsLimitModel>> getStoreDetail(@Query("benefit_id") String benefit_id,@Query("longitude") double longitude,@Query("latitude") double latitude);
}
