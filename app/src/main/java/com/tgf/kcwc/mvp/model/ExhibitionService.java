package com.tgf.kcwc.mvp.model;

import java.util.List;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Auther: Scott
 * Date: 2017/1/13 0013
 * E-mail:hekescott@qq.com
 */
public interface ExhibitionService {

    /**
     * 展会列表
     */
    @GET("event/event/lists")
    Observable<ResponseMessage<ExhibitionListModel>> getExhibitionList(@Query("area_id") String areaId,
                                                                       @Query("page") String page,
                                                                       @Query("pagecount") String pagecount,
                                                                       @Query("token") String token, @Query("adcode") String adcode);

    /**
     * 展会列表
     */
    @GET("event/event/lists")
    Observable<ResponseMessage<ExhibitionListModel>> getExhibitionList(@QueryMap Map<String, String> params);


    /**
     * 展会位图
     */
    @GET("event/photo/hall")
    Observable<ResponseMessage<ExhibitPlaceListModel>> getExhibitionPlaceList(@Query("exhibition_id") int exhibitionId,
                                                                              @Query("token") String token);

    /**
     * 展会详情
     */
    @GET("event/event/detail")
    Observable<ResponseMessage<ExhibitionDetailModel>> getExhibitionDetail(@Query("exhibition_id") int exhibitionId,
                                                                           @Query("server_city") String serverCityId, @Query("adcode") String adcode);

    /**
     * 模特列表
     */
    @GET("event/model/listbybrand")
    Observable<ResponseMessage<BeautyListModel>> getBeautyList(@Query("event_id") int exhibitionId,
                                                               @Query("hall_id") int hallId,
                                                               @Query("real_factory_id") int factoryId,
                                                               @Query("pageSize") int pageSize,
                                                               @Query("page") int page);

    /**
     * 模特详情
     */
    @GET("event/model/detail")
    Observable<ResponseMessage<BeautyDetailModel>> getBeautyDetail(@Query("id") int beautyId,
                                                                   @Query("token") String token,
                                                                   @Query("pagecount") int pageSize,
                                                                   @Query("page") int page);

    /**
     * 展会快讯列表
     */
    @GET("event/news/lists")
    Observable<ResponseMessage<ExhibitionNewsListModel>> getNews(@Query("event_id") int eventId,
                                                                 @Query("pageSize") int pageSize,
                                                                 @Query("page") int page);

    /**
     * 展会活动列表
     */
    @GET("activity/lists")
    Observable<ResponseMessage<EventListModel>> getEventlist(@Query("event_id") int eventId,
                                                             @Query("type") int type,
                                                             @Query("pageSize") int pageSize,
                                                             @Query("page") int page);

    /**
     * 展会活动详情
     */
    @GET("activity")
    Observable<ResponseMessage<ExhibitEvent>> getEventDetail(@Query("activity_id") int eventId);

    /**
     * 展会活动图库
     */
    @GET("activity/index/gallery")
    Observable<ResponseMessage<ExhibitionEventPicsModel>> getActivityPhotoStore(@Query("id") int eventId,
                                                                                @Query("pageSize") int pageSize,
                                                                                @Query("page") int page);

    /**
     * 展会快讯详情
     */
    @GET("event/news/detail")
    Observable<ResponseMessage<ExhibitionNews>> getNewsDetail(@Query("id") int newsId,@Query("token") String token);

    /**
     * 展会现场图库
     */
    @GET("event/photo/eventimg")
    Observable<ResponseMessage<ExhibitionPicsModel>> getExhibitPhotoStore(@Query("event_id") int exhibitId,
                                                                          @Query("brand_id") Integer brand_id,
                                                                          @Query("hall_id") Integer hall_id,
                                                                          @Query("is_type") Integer is_type,
                                                                          @Query("pageSize") int pageSize,
                                                                          @Query("page") int page);

    /**
     * 展馆详情
     */
    @GET("event/Photo/detailhall")
    Observable<ResponseMessage<ExhibitPlace>> getExhibitPlaceDetail(@Query("event_id") int eventId, @Query("hall_id") int hallId);

    /**
     * 展会看banner
     */
    @GET("event/photo")
    Observable<ResponseMessage<ExhibitSeeBannerModel>> getExhibitShowBanner(@Query("event_id") int eventId, @Query("is_event_bannner") int isBanner);


    @GET("/event/car/getHallList")
    Observable<ResponseMessage<List<Hall>>> getHallLists(@Query("event_id") String id);

    /**
     * 达人评测详情
     */
    @GET("thread/evaluate/detail")
    Observable<ResponseMessage<PopmanEsDetailModel>> getPopmanEsDetail(@Query("thread_id") String threadId);

    /**
     * 达人评测列表
     */
    @GET("thread/evaluate")
    Observable<ResponseMessage<PopmanEslistModel>> getDarenLists(@Query("brand_id") String brandId, @Query("price_max") String pricMax, @Query("price_min") String priceMin, @Query("seat_num") Integer seatNum,@Query("page") int page,@Query("pageSize") int mPageSize);

    /**
     * 轮播
     */
    @GET("carousel")
    Observable<BannerModel> getBanner(@Query("t") String t,
                                      @Query("mark") String mark);
    /**
     * 首页关注 热门 同城
     */
    @GET("index/home")
    Observable<ResponseMessage<HomeModel>> getHomeList(@Query("token") String token, @Query("type") String type, @Query("city_id") int city_id,
                                            @Query("longitude") String lng,@Query("latitude") String lat,
                                            @Query("page") int page,@Query("pageSize") int pageSize);
    @FormUrlEncoded
    @POST("carorder/order/add")
    Observable<ResponseMessage<MotoOrder>> commitMotoOrder(@Field("token") String token,
                                                           @Field("car_id") int carId,
                                                           @Field("car_series_id") int carSeriesId,
                                                           @Field("contact") String contact,
                                                           @Field("lat") String lat,
                                                           @Field("lng") String log,
                                                           @Field("tel") String tel);
    /**
     *   关注
     */
    @POST("user/follow")
    @FormUrlEncoded
    Observable<BaseBean> getFollow(@Field("token") String token, @Field("follow_id") String follow_id);

    /**
     *   取消关注
     */
    @POST("user/follow/cancel")
    @FormUrlEncoded
    Observable<BaseBean> getFellowCancel(@Field("token") String token, @Field("follow_id") String follow_id);

    // 点赞 thread||comment
    @FormUrlEncoded
    @POST("praise/create")
    Observable<ResponseMessage<Object>> executePraise(@Field("resource_id") String id,
                                                      @Field("type") String type,
                                                      @Field("token") String token);

    //删除帖子
    @FormUrlEncoded
    @POST("thread/detail/delete")
    Observable<ResponseMessage<Object>> deleteThread(@Field("thread_id") String threadId,
                                                    @Field("token") String token);
}
