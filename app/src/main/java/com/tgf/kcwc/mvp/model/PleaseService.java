package com.tgf.kcwc.mvp.model;

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
 * Auther: Scott
 * Date: 2017/1/13 0013
 * E-mail:hekescott@qq.com
 */
public interface PleaseService {

    /**
     * 请你玩去列表
     */
    @GET("thread/play/lists")
    Observable<PleasePlayModel> getPleaseList(@QueryMap Map<String, String> params);

    /**
     * 轮播
     */
    @GET("carousel")
    Observable<BannerModel> getPleaseBanner(@Query("token") String token,
                                            @Query("t") String t,
                                            @Query("mark") String mark);

    /**
     * 详情
     */
    @GET("thread/play/detail")
    Observable<PleaseDetailsBean> getDetailsData(@Query("token") String token,
                                                 @Query("id") String id);

    /**
     * 路书
     */
    @GET("/ride/ride/getactivityline")
    Observable<DrivingRoadBookBean> getDetails(@Query("token") String token,
                                               @Query("threadId") String threadId);

    /**
     * 发起请你玩
     */
    @POST("thread/play/create")
    @FormUrlEncoded
    Observable<BaseBean> getCreate(@FieldMap Map<String, String> params);

    /**
     * 发起请你玩
     */
    @POST("thread/play/edit")
    @FormUrlEncoded
    Observable<BaseBean> getEdit(@FieldMap Map<String, String> params);

    /**
     * 发起请你玩
     */
    @GET("thread/play/edit")
    Observable<CompilePleaseBean> getEdit(@Query("token") String token,@Query("id") String id);

    /**
     * 取消报名
     */
    @GET("thread/activity_apply/cancel")
    Observable<BaseBean> getApplyCancel(@Query("token") String token, @Query("thread_id") String id);
}
