package com.tgf.kcwc.mvp.model;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Auther: Scott
 * Date: 2017/1/13 0013
 * E-mail:hekescott@qq.com
 */
public interface SignInService {
    /**
     * 路书
     */
    @GET("ride/ride/getactivityline")
    Observable<DrivingRoadBookBean> getDetails(@Query("token") String token,
                                               @Query("thread_id") String threadId);

    /**
     * 点亮节点
     */
    @GET("ride/checkin/light")
    Observable<BaseBean> getLight(@Query("token") String token,
                                  @Query("ride_check_id") String ride_check_id);

    /**
     * 创建签到
     */
    @GET("ride/checkin/createcheck")
    Observable<QrcodeSiginBean> getCreateCheck(@Query("token") String token, @Query("ride_check_id") String ride_check_id);

    /**
     * 手动结束活动
     */
    @GET("/ride/Checkin/endActivity")
    Observable<BaseArryBean> getEndAcitivity(@Query("token") String token, @Query("threadId") String threadId);

    /**
     * 二维码签到
     */
    @GET("/ride/Checkin/checkNow")
    Observable<BaseBean> getCheckNow(@Query("token") String token, @Query("address") String address, @Query("lat") String lat, @Query("lng") String lng, @Query("rideCheckId") String rideCheckId);

    /**
     * 续接点
     */
    @GET("/ride/ride/mergeLineDetail")
    Observable<ContinueBean> getMergeLineDetail(@Query("token") String token, @Query("ride_id") String ride_id);

    /**
     * 续接点
     */
    @POST("/ride/ride/mergeLine")
    @FormUrlEncoded
    Observable<BaseBean> getMergeLine(@Field("token") String token, @Field("data") String data);

}
