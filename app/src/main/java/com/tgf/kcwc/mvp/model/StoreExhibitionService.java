package com.tgf.kcwc.mvp.model;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface StoreExhibitionService {

    /**
     * 拍展会展车列表
     */
    @GET("photograph/event/lists")
    Observable<StoreExhibitionBean> getDataLists(@Query("token") String token,
                                                 @Query("page") int page,
                                                 @Query("status") String status,
                                                 @Query("event_id") String event_id);

    /**
     * 拍展会展车列表
     */
    @GET("photograph/event/eventList")
    Observable<ExhibitionArryBean> getEventList(@Query("token") String token);

    /**
     * 拍展会展车列表 撤销
     */
    @GET("photograph/event/cancel")
    Observable<StreBelowAmendBean> getCancel(@Query("token") String token, @Query("id") String id);

    /**
     * 品牌展车-获取展会、权限及参展品牌信息
     */
    @GET("photograph/event/add")
    Observable<StorJurisdictionBean> getAdd(@Query("token") String token);

    /**
     * 拍店内展车列表 详情
     */
    @GET("photograph/event/detail")
    Observable<CompileExhibitionBean> getDetail(@Query("token") String token, @Query("id") String id);

    //获取车型的内饰、外观、颜色种类
    @GET("car/car/getcarcolor")
    Observable<ResponseMessage<List<CarColor>>> getCarCategoryColors(@Query("id") String id,
                                                                     @Query("type") String type,
                                                                     @Query("color_type") String colorType);

    //发布
    @POST("photograph/event/create")
    @FormUrlEncoded
    Observable<BaseBean> getCreate(@Field("token") String token,
                                   @Field("appearance_color_name") String appearance_color_name,
                                   @Field("appearance_color_value") String appearance_color_value,
                                   @Field("appearance_img") String appearance_img,
                                   @Field("booth_id") String booth_id,
                                   @Field("event_id") String event_id,
                                   @Field("factory_id") String factory_id,
                                   @Field("hall_id") String hall_id,
                                   @Field("interior_color_name") String interior_color_name,
                                   @Field("interior_color_value") String interior_color_value,
                                   @Field("interior_img") String interior_img,
                                   @Field("product_id") String car_id,
                                   @Field("product_name") String car_name,
                                   @Field("series_id") String series_id,
                                   @Field("status") String status);
    //编辑
    @POST("photograph/event/edit")
    @FormUrlEncoded
    Observable<BaseBean> getEdit(@Field("token") String token,
                                   @Field("appearance_color_name") String appearance_color_name,
                                   @Field("appearance_color_value") String appearance_color_value,
                                   @Field("appearance_img") String appearance_img,
                                   @Field("booth_id") String booth_id,
                                   @Field("event_id") String event_id,
                                   @Field("factory_id") String factory_id,
                                   @Field("hall_id") String hall_id,
                                   @Field("interior_color_name") String interior_color_name,
                                   @Field("interior_color_value") String interior_color_value,
                                   @Field("interior_img") String interior_img,
                                   @Field("product_id") String car_id,
                                   @Field("product_name") String car_name,
                                   @Field("series_id") String series_id,
                                   @Field("status") String status,@Field("id") String id);
}
