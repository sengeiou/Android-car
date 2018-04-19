package com.tgf.kcwc.mvp.model;

import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.presenter.StoreCarListModel;

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
 * Date:2016/12/13 09:01
 * E-mail：fishloveqin@gmail.com
 * 整车模块服务接口
 */

public interface VehicleService {

    /**
     * @param brandId  品牌Id
     * @param page     页码
     * @param priceKey 价格keyword
     * @param priceMin
     * @param priceMax
     * @param seatKey  座位数keyword
     * @return
     */

    @GET("/car/car/serieslist")
    Observable<ResponseMessage<SeriesModel>> getSeriesList(@Query("brand_ids") String brandId,
                                                           @Query("page") String page,
                                                           @Query("price_range_key") String priceKey,
                                                           @Query("price_range_min") String priceMin,
                                                           @Query("price_range_max") String priceMax,
                                                           @Query("seat_num_range_key") String seatKey,
                                                           @Query("is_first") String first,
                                                           @Query("token") String token);


    /**
     * @param params
     * @return
     */

    @GET("/car/car/serieslist")
    Observable<ResponseMessage<SeriesModel>> getSeriesList(@QueryMap Map<String, String> params);


    /**
     * 品牌列表
     * 二级品牌
     *
     * @return
     */
    @GET("car/factory/lists")
    //  一级品牌 car/brandlist/getBrandList
    Observable<ResponseMessage<List<Brand>>> brandList(@Query("vehicle_type") String type, @Query("is_need_all") String isNeedAll);


    /**
     * 品牌列表
     * 二级品牌
     *
     * @return
     */
    @POST("car/factory/lists") //  一级品牌 car/brandlist/getBrandList
    @FormUrlEncoded
    Observable<ResponseMessage<List<Brand>>> brandList(@Field("vehicle_type") String type);


    /**
     * 品牌列表 拍店内展车
     *
     * @param token
     * @return
     */
    @GET("photograph/store/add")
    Observable<ResponseMessage<StoreBrand>> getaddbrandList(@Query("token") String token);

    /**
     * 品牌列表 拍展会展车
     *
     * @param token
     * @return
     */
    @GET("photograph/event/add")
    Observable<ResponseMessage<StoreBrand>> getExhibitionaddbrandList(@Query("token") String token,@Query("event_id") String event_id);

    /**
     * 展车品牌
     *
     * @return
     */
    @GET("event/showcar/eventbrand")
    Observable<ResponseMessage<List<Brand>>> sepBrandList(@Query("event_id") String eventId, @Query("is_need_all") String isNeedAll);


    /**
     * 展车品牌
     *
     * @return
     */
    @GET("event/factory/lists")
    Observable<ResponseMessage<List<Brand>>> getExhibitionBrandList(@Query("event_id") String eventId, @Query("is_need_all") String isNeedAll);

    /**
     * 代金券列表
     *
     * @return
     */
    @GET("coupon/store")
    Observable<ResponseMessage<List<Coupon>>> couponList(@Query("org_id") String orgId);

    /**
     * 礼包、限时优惠列表
     *
     * @return
     */
    @GET("orgshop/detail/orgshopgift")
    Observable<ResponseMessage<List<Coupon>>> giftsList(@Query("store_id") String orgId,
                                                        @Query("type") String type,
                                                        @Query("token") String token);

    /**
     * 店铺详情
     *
     * @return
     */
    @GET("orgshop/detail")
    Observable<ResponseMessage<StoreDetailData>> storeDetail(@Query("org_id") String id, @Query("token") String token);

    /**
     * 新车发布列表
     *
     * @return
     */
    @GET("/event/car/newcar")
    Observable<ResponseMessage<CarLaunchModel>> newCarsLaunchList(@QueryMap Map<String, String> params);

    /**
     * 展会品牌展车列表
     *
     * @return
     */
    @GET("event/showcar")
    Observable<ResponseMessage<MotorshowModel>> brandModelsList(@Query("event_id") String senseId,
                                                                @Query("hall_id") String hallId,
                                                                @Query("single_brand_id") String brandId,
                                                                @Query("page") String page,
                                                                @Query("pageSize") String pageSize,
                                                                @Query("isSpecial") String isSpecial,
                                                                @Query("token") String token);


    /**
     * 展会品牌展车列表
     *
     * @return
     */
    @GET("event/showcar")
    Observable<ResponseMessage<MotorshowModel>> brandModelsList(@QueryMap Map<String, String> params);


    /**
     * 车型对比
     *
     * @param carIds
     * @return
     */
    @FormUrlEncoded
    @POST("car/car/paramCompare")
    Observable<ResponseMessage<CarContrastModel>> contrastList(@Field("car_ids") String carIds,
                                                               @Field("token") String token);

    /**
     * 根据品牌id获取车型列表
     *
     * @param brandId
     * @return
     */
    @POST("car/car/getCarByBrand")
    @FormUrlEncoded
    Observable<ResponseMessage<List<CarBean>>> getCarsByBrandId(@Field("brand_id") String brandId,
                                                                @Field("token") String token);

    /**
     * 添加定制
     *
     * @return brand_ids
     * power
     * country_ids
     * token
     * battery
     * seat_num
     * vehicle_type
     * cc_max
     * price_max
     * power_forms
     * car_level_ids
     * price_min
     */
    @POST("car/custom/setCustomMade")
    @FormUrlEncoded
    Observable<ResponseMessage<List<String>>> setCustomizationInfo(@FieldMap Map<String, String> params);

    /**
     * 玩家这么说
     *
     * @param id
     * @return
     */
    @GET("thread/goods/more")
    //thread/lists/store thread/goods/more
    Observable<ResponseMessage<List<Topic>>> playerTopics(@Query("thread_id") String id);

    /**
     * 销售精英列表
     *
     * @param orgId
     * @param token
     * @return
     */
    @GET("orgshop/detail/orguser")
    Observable<ResponseMessage<SalespersonModel>> salespersonLists(@Query("org_id") String orgId,
                                                                   @Query("token") String token);

    /**
     * 新车图库
     *
     * @param id
     * @param page
     * @param pageSize
     * @return
     */
    @GET("event/car/newcar_gallery")
    Observable<ResponseMessage<CarGalleryModel>> newCarGallery(@Query("newcar_id") String id,
                                                               @Query("page") String page,
                                                               @Query("pageSize") String pageSize);

    /**
     * 展车图库
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GET("event/showcar/brandcarimglist")
    Observable<ResponseMessage<Motorshow>> motorshowGallery(@Query("id") String id,
                                                            @Query("page") String page,
                                                            @Query("pageSize") String pageSize,
                                                            @Query("token") String token);

    //帖子标签、话题
    @GET("thread/lists/topic_search")
    Observable<ResponseMessage<List<Topic>>> getTopicList(@Query("title") String title,

                                                          @Query("token") String token);

    //帖子标签、话题
    @GET("thread/lists/topic_search_v2")
    Observable<ResponseMessage<TopicSearchModel>> getTopicListV2(@Query("title") String title,

                                                                 @Query("token") String token);

    @POST("thread/topic/create")
    @FormUrlEncoded
    Observable<ResponseMessage<Topic>> createTag(@Field("title") String title,
                                                 @Field("token") String token);

    @POST("thread/lists/set_topic")
    @FormUrlEncoded
    Observable<ResponseMessage<Topic>> createTag(@Field("from_id") String fromId,
                                                 @Field("from_type") String type,
                                                 @Field("token") String token);

    //发帖
    @FormUrlEncoded
    @POST("thread/create/words")
    Observable<ResponseMessage<List<String>>> createTopic(@FieldMap Map<String, String> params);


    //编辑发帖
    @FormUrlEncoded
    @POST("thread/edit/words")
    Observable<ResponseMessage<Object>> editTopic(@FieldMap Map<String, String> params);


    //帖子详情
    @GET("thread/detail/words")
    Observable<ResponseMessage<TopicDetailModel>> getTopicDetail(@Query("thread_id") String id,
                                                                 @Query("longitude") String longitude,
                                                                 @Query("latitude") String latitude,
                                                                 @Query("token") String token);

    //车系详情
    @GET("car/car/seriesdetail")
    Observable<ResponseMessage<SeriesDetailModel>> getSeriesDetail(@Query(" series_id") String seriesId,
                                                                   @Query("token") String token);

    //车系、车型图库
    @GET("car/car/getcarpic")
    Observable<ResponseMessage<ModelGalleryModel>> getCarGallery(@Query("id") String id,
                                                                 @Query("type") String type,
                                                                 @Query("color_id_out") String outId,
                                                                 @Query("color_id_in") String inId,
                                                                 @Query("car_id") String carId);

    //获取车型的内饰、外观、颜色种类
    @GET("car/car/getcarcolor")
    Observable<ResponseMessage<List<CarColor>>> getCarCategoryColors(@Query("id") String id,
                                                                     @Query("type") String type,
                                                                     @Query("color_type") String colorType);

    //根据品牌获取车系列表
    @GET("car/brandlist/getSeriesByBrand")
    Observable<ResponseMessage<List<CarBean>>> getSeriesByBrand(@Query("brand_id") String brandId,
                                                                @Query("token") String token);

    //根据车系获取车型列表
    @GET("car/brandlist/getCarBySeries")
    Observable<ResponseMessage<List<CarBean>>> getCarBySeries(@Query("series_id") String seriesId,
                                                              @Query("token") String token);

    @GET("car/car/carpicdetail")
    Observable<ResponseMessage<GalleryDetailModel>> getGalleryDetails(@Query("id") String id,

                                                                      @Query("img_type") String imgType,
                                                                      @Query("type") String type);

    @GET("car/car/carlist")
    Observable<ResponseMessage<List<CarBean>>> getCarList(@Query("car_series_id") String id);

    @GET("store/car/showlist")
    Observable<ResponseMessage<StoreCarModel>> getShowCars(@Query("org_id") String orgId);

    @GET("store/car/existlist")
    Observable<ResponseMessage<StoreCarModel>> getLiveCars(@Query("org_id") String orgId);

    @GET("store/car/lists")
        //page=1&pageSize=10&latitude=29.56301&longitude=106.551557&token=n1Y2RqFWzuqzdExkOlOopl5AGbsvaqeh
    Observable<ResponseMessage<StoreCarListModel>> getStoreCarList(@Query("brand_ids") String brandId,
                                                                   @Query("city_id") String cityId,
                                                                   @Query("car_id") String carId,
                                                                   @Query("page") String page,
                                                                   @Query("price_range_key") String priceKey,
                                                                   @Query("price_range_min") String priceMin,
                                                                   @Query("price_range_max") String priceMax,
                                                                   @Query("seat_num_range_key") String seatKey,
                                                                   @Query("longitude") String longitude,
                                                                   @Query("latitude") String latitude);

    @GET("store/car/detail")
    Observable<ResponseMessage<StoreShowCarDetailModel>> getStoreShowCarDetail(@Query("id") String id, @Query("token") String token);

    @GET("store/car/cardetail")
    Observable<ResponseMessage<StoreShowCarDetailModel>> getStoreShowCarDetail(@Query("car_id") String carId, @Query("org_id") String orgId
            , @Query("token") String token
    );

    //获取颜色配置列表
    @GET("store/car/colorlist")
    Observable<ResponseMessage<List<ColorBean>>> getColorsCfgLists(@Query("id") String id,
                                                                   @Query("token") String token);

    //热搜
    @GET("car/car/searchHot")
    Observable<ResponseMessage<List<HotTag>>> getHotsLists(@Query("token") String token);


    //获取一级品牌列表
    @GET("car/brand/lists")
    Observable<ResponseMessage<List<Brand>>> getBrandsLists(@Query("token") String token, @Query("include_all") String includeAll);


    //根据品牌获取车系列表，在售、预售、停售
    @GET("car/car/serieslistbrandall")
    Observable<ResponseMessage<SeriesByBrandModel>> getSeriesByBrand(@Query("brand_id") String brandId);


    //获取优惠对比结果
    @GET("car/car/favCompare")
    Observable<ResponseMessage<DiscountContrastModel>> getDiscountContrastResult(@Query("id") String carId);


    //删除帖子
    @FormUrlEncoded
    @POST("thread/detail/delete")
    Observable<ResponseMessage<Object>> deleteTopic(@FieldMap Map<String, String> params);


    //查询帖子是否已举报
    @GET("report/report/repeatReport")
    Observable<ResponseMessage<DataItem>> isExistReport(@QueryMap Map<String, String> params);


    //举报
    @POST("report/report/add")
    @FormUrlEncoded
    Observable<ResponseMessage<Object>> addReport(@FieldMap Map<String, String> params);

    /**
     * 获取厂商车系（二级品牌车系分组）
     *
     * @param params
     */

    @GET("car/car/serieslistbybrand")
    Observable<ResponseMessage<FactorySeriesModel>> getFactorySeries(@QueryMap Map<String, String> params);


    /**
     * 帖子标签数据，带新车、展车、模特参
     *
     * @param params
     */

    @GET("thread/lists/event")
    Observable<ResponseMessage<List<TopicTagDataModel>>> getTopicTagDatas(@QueryMap Map<String, String> params);


    /**
     * 发布动态
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("/thread/create/mood")
    Observable<ResponseMessage<Object>> releasemood(@FieldMap Map<String, String> params);

    /**
     * 动态详情
     *
     * @return
     */
    @GET("/thread/detail/mood")
    Observable<ResponseMessage<DynamicDetailsBean>> detailMood(@Query("token") String token, @Query("thread_id") String thread_id);

    /**
     * 关注人
     */
    @POST("user/follow")
    @FormUrlEncoded
    Observable<BaseBean> getFollow(@Field("token") String token, @Field("follow_id") String follow_id);

    /**
     * 取消关注人
     */
    @POST("user/follow/cancel")
    @FormUrlEncoded
    Observable<BaseBean> getFellowCancel(@Field("token") String token, @Field("follow_id") String follow_id);

}
