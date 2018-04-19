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

public interface StoreBelowService {

    /**
     * 拍店内展车列表
     */
    @GET("photograph/store/lists")
    Observable<StoreBelowBean> getDataLists(@Query("token") String token, @Query("page") int page,
                                            @Query("status") String status);
    /**
     * 拍店内展车列表 撤销
     */
    @GET("photograph/store/cancel")
    Observable<StreBelowAmendBean> getCancel(@Query("token") String token, @Query("id") String id);
    /**
     * 拍店内展车列表 详情
     */
    @GET("photograph/store/detail")
    Observable<CompileStoreBean> getDetail(@Query("token") String token, @Query("id") String id);

    //获取车型的内饰、外观、颜色种类
    @GET("car/car/getcarcolor")
    Observable<ResponseMessage<List<CarColor>>> getCarCategoryColors(@Query("id") String id,
                                                                     @Query("type") String type,
                                                                     @Query("color_type") String colorType);

    //编辑
    @POST("photograph/store/edit")
    @FormUrlEncoded
    Observable<BaseBean> getEdit(@Field("token") String token,
                                                          @Field("appearance_color_name") String appearance_color_name,
                                                          @Field("appearance_color_value") String appearance_color_value,
                                                          @Field("appearance_img") String appearance_img,
                                                          @Field("car_id") String car_id,
                                                          @Field("car_name") String car_name,
                                                          @Field("diff_conf") String diff_conf,
                                                          @Field("factory_id") String factory_id,
                                                          @Field("interior_color_name") String interior_color_name,
                                                          @Field("interior_color_value") String interior_color_value,
                                                          @Field("interior_img") String interior_img,
                                                          @Field("series_id") String series_id,
                                                          @Field("status") String status,
                                                          @Field("type_exist") String type_exist,
                                                          @Field("type_show") String type_show,@Field("id") String id);
    //发布
    @POST("photograph/store/create")
    @FormUrlEncoded
    Observable<BaseBean> getCreate(@Field("token") String token,
                                                          @Field("appearance_color_name") String appearance_color_name,
                                                          @Field("appearance_color_value") String appearance_color_value,
                                                          @Field("appearance_img") String appearance_img,
                                                          @Field("car_id") String car_id,
                                                          @Field("car_name") String car_name,
                                                          @Field("diff_conf") String diff_conf,
                                                          @Field("factory_id") String factory_id,
                                                          @Field("interior_color_name") String interior_color_name,
                                                          @Field("interior_color_value") String interior_color_value,
                                                          @Field("interior_img") String interior_img,
                                                          @Field("series_id") String series_id,
                                                          @Field("status") String status,
                                                          @Field("type_exist") String type_exist,
                                                          @Field("type_show") String type_show);
}
