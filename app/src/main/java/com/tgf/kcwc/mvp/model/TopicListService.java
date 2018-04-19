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

public interface TopicListService {


    /*-------------------------------------------------------话题列表接口-----------------------------------------------------------------*/

    /**
     * 话题列表
     */
    @GET("/thread/lists/topic")
    Observable<TopicBean> getTopicList(@Query("token") String token,@Query("type") String type,@Query("page") int page);

    /*-------------------------------------------------------话题详情接口-----------------------------------------------------------------*/

    /**
     * 话题详情
     */
    @GET("thread/detail/topic")
    Observable<TopicDetailsBean> getTopicDetail(@Query("token") String token,@Query("topic_id") String topic_id);
    /**
     * 话题下列表数据
     */
    @GET("thread/lists/topic_detail")
    Observable<TopicDetailsListBean> GetTopicListData(@Query("token") String token,@Query("topic_id") String topic_id,@Query("page") int page);
    /**
     * 申请主持人
     */
    @POST("thread/topic/apply")
    @FormUrlEncoded
    Observable<BaseArryBean> GetApplyHost(@Field("token") String token,@Field("topic_id") String topic_id);
    /**
     * 关注
     */
    @POST("thread/topic/attention")
    @FormUrlEncoded
    Observable<BaseArryBean> GetTopicAttention(@Field("token") String token, @Field("topic_id") String topic_id);
    /**
     * 取消关注
     */
    @POST("thread/topic/attention_delete")
    @FormUrlEncoded
    Observable<BaseArryBean> GetTopicAttentionDelete(@Field("token") String token,@Field("topic_id") String topic_id);

    /*-------------------------------------------------------编辑话题接口-----------------------------------------------------------------*/
    /**
     * 编辑
     */
    @POST("/thread/topic/update")
    @FormUrlEncoded
    Observable<BaseArryBean> GetUpdate(@Field("token") String token,@Field("cover") String cover,@Field("id") String id,@Field("intro") String intro);
    /**
     * 帖子置顶
     */
    @POST("/thread/topic/top")
    @FormUrlEncoded
    Observable<BaseArryBean> GetTop(@Field("token") String token,@Field("thread_id") String thread_id,@Field("topic_id") String topic_id);

    /*-----------------------------------------------------------创建话题---------------------------------------------------------------------------*/
    /**
     * 创建话题
     */
    @POST("/thread/topic/create")
    @FormUrlEncoded
    Observable<BaseBean> GetCreate(@Field("token") String token,@Field("category_id") String category_id,@Field("cover") String cover,@Field("intro") String intro,@Field("title") String title,@Field("category_type") String category_type);
    /**
     * 获取分类
     */
    @GET("/tags/category/get")
    Observable<FoundTypeBean> GetType(@Query("token") String token,@Query("parent_id") String parent_id);
     /*-----------------------------------------------------------玩得爽---------------------------------------------------------------------------*/
    /**
     * 玩得爽列表
     */
    @GET("/thread/lists/digest")
    Observable<HaveFunBean> GetDiges(@Query("token") String token,@Query("type") String type,@Query("page") int page);
}
