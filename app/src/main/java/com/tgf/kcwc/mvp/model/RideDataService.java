package com.tgf.kcwc.mvp.model;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Author:Jenny
 * Date:2017/5/3
 * E-mail:fishloveqin@gmail.com
 * <p>
 * 驾途网络服务Service
 */

public interface RideDataService {

    /**
     * 路线
     */
    @GET("ride/ride/myRideList")
    Observable<ResponseMessage<RideDataModel>> getRideList(@Query("page") String page,
                                                           @Query("pageSize") String pageSize,
                                                           @Query("token") String token);

    /**
     * 规划
     */
    @GET("ride/ride/myRidePlan")
    Observable<ResponseMessage<RideDataModel>> getPlanList(@Query("page") String page,
                                                           @Query("pageSize") String pageSize,
                                                           @Query("token") String token);

    /**
     * 收藏
     */
    @GET("ride/ride/myRideCollect")
    Observable<ResponseMessage<RideDataModel>> getFavoriteList(@Query("page") String page,
                                                               @Query("pageSize") String pageSize,
                                                               @Query("token") String token);

    /**
     * 骑行报告
     *
     * @param rideId
     * @param token
     * @return
     */
    @GET("ride/ride/rideReport")
    Observable<ResponseMessage<RideReportDetailModel>> getRideReport(@Query("rideId") String rideId,
                                                                     @Query("type") String type,
                                                                     @Query("token") String token);

    @FormUrlEncoded
    /**
     * 骑行开始
     * @param threadId
     * @param adds
     * @param alt
     * @param lat
     * @param lng
     * @return
     */
    @POST("ride/ride/start")
    Observable<ResponseMessage<RideData>> rideStart(@FieldMap Map<String, String> params);

    /**
     * 骑行暂停
     */
    @POST("ride/ride/pause")
    @FormUrlEncoded
    Observable<ResponseMessage<RideData>> ridePause(@FieldMap Map<String, String> params);

    /**
     * 骑行重新开始
     */
    @POST("ride/ride/restart")
    @FormUrlEncoded
    Observable<ResponseMessage<Object>> rideRestart(@FieldMap Map<String, String> params);

    /**
     * 结束骑行
     */
    @POST("ride/ride/end")
    @FormUrlEncoded
    Observable<ResponseMessage<Object>> rideEnd(@FieldMap Map<String, String> params);

    /**
     * 提交行驶数据
     */
    @POST("ride/ride/addpAuto")
    @FormUrlEncoded
    Observable<ResponseMessage<Object>> rideAutoData(@FieldMap Map<String, String> params);

    /**
     * 结束骑行
     */
    @POST("ride/line/addLinePlan")
    @FormUrlEncoded
    Observable<ResponseMessage<RideData>> createRideLines(@FieldMap Map<String, String> params);

    /**
     * 预览编辑路线
     */
    @GET("ride/line/editLinePlanLoad")
    Observable<ResponseMessage<RideLinePreviewModel>> loadRidePlan(@Query("rideId") String rideId,
                                                                   @Query("token") String token);

    /**
     * 获取骑行状态
     */
    @GET("roadbook/create/checkNotEnd")
    Observable<ResponseMessage<RideAutoData>> getRideState(@Query("token") String token);

    /**
     * 删除骑行数据
     */
    @GET("ride/ride/delete")
    Observable<ResponseMessage<Object>> deleteRideData(@Query("ride_id") String rideId,
                                                       @Query("token") String token);

    /**
     * 获取当前驾图的状态
     */
    @GET("ride/ride/check")
    Observable<ResponseMessage<RideRunStateModel>> checkRideRunState(@Query("token") String token);


    /**
     * 删除规划的线路
     */
    @GET("ride/line/delete")
    Observable<ResponseMessage<Object>> deletePlanLine(@Query("id") String id, @Query("token") String token);




    /**
     * 生成驾途封面
     */
    @POST("ride/ride/updatecover")
    @FormUrlEncoded
    Observable<ResponseMessage<Object>> createCover(@FieldMap Map<String, String> params);

}
