package com.tgf.kcwc.mvp.model;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface MessageListService {


    /*-------------------------------------------------------消息列表首页接口-----------------------------------------------------------------*/

    /**
     * 固定头部的数据
     */
    @GET("message/Message/appstatistics")
    Observable<MessageListBean> getAppStatistics(@Query("token") String token);

    /**
     * 其他数据
     */
    @GET("message/Message/privateletterlist")
    Observable<MessageListBean> getPrivateletterList(@Query("token") String token, @Query("page") int page);

    /**
     * 系统消息详情
     */
    @GET("message/Message/lists")
    Observable<MessageSystemListBean> getStatisticsList(@Query("token") String token, @Query("type") String type, @Query("page") int page,@Query("event_id") int event_id);

    /**
     * 私信消息详情
     */
    @GET("message/Message/letterlist")
    Observable<MessagePrivateListBean> getLetterlistList(@Query("token") String token, @Query("letteruser") String letteruser, @Query("page") int page);

    /**
     * 删除消息详情
     */
    @FormUrlEncoded
    @POST("message/message/delmessage")
    Observable<BaseArryBean> getDelmessage(@Field("token") String token, @Field("message_id") String message_id);

    /**
     * 全部标记为已读
     */
    @FormUrlEncoded
    @POST("/message/message/read")
    Observable<BaseBean> getRead(@Field("token") String token);

}
