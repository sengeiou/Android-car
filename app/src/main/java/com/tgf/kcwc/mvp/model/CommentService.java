package com.tgf.kcwc.mvp.model;

import java.util.List;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Author：Jenny
 * Date:2017/1/20 13:43
 * E-mail：fishloveqin@gmail.com
 * 评论/评价服务接口
 */

public interface CommentService {

    /**
     * 评论列表
     * @param module
     * @param resId
     * @param vehicleType
     * @return
     */
    @GET("comment/comment_list/commentList")
    Observable<ResponseMessage<CommentModel>> getCommentList(@Query("module") String module,
                                                             @Query("resource_id") String resId,
                                                             @Query("vehicle_type") String vehicleType,@Query("token") String token);

    /**
     * 评论列表
     * @param module
     * @param resId
     * @param vehicleType
     * @return
     */
    @GET("comment/comment_list/commentList")
    Observable<ResponseMessage<CommentModel>> getCommentList(@Query("module") String module,
                                                             @Query("resource_id") String resId,
                                                             @Query("vehicle_type") String vehicleType,@Query("page") int page,@Query("pagecount") int pagecount);
    /**
     * 评论详情
     * @param id 评论id
     * @return
     */
    @GET("comment/comment_list/commentDetail")
    Observable<ResponseMessage<CommentModel>> getCommentDeatail(@Query("id") String id,@Query("token") String token);

    /**
     * 评价列表
     * @param module
     * @param resId
     * @param vehicleType
     * @return
     */
    @GET("comment/comment_list/discussionList")
    Observable<ResponseMessage<CommentModel>> getEvaluateList(@Query("module") String module,
                                                              @Query("resource_id") String resId,
                                                              @Query("vehicle_type") String vehicleType);

    //点赞列表
    @GET("praise/lists/thread")
    Observable<ResponseMessage<LikeListModel>> getLikeList(@Query("thread_id") String id,
                                                           @Query("token") String token);

    // 点赞 thread||comment
    @FormUrlEncoded
    @POST("praise/create")
    Observable<ResponseMessage<LikeBean>> executePraise(@Field("resource_id") String id,
                                                            @Field("type") String type,
                                                            @Field("token") String token);

    // 发布评论
    @FormUrlEncoded
    @POST("comment/comment/publishcomment")
    Observable<ResponseMessage<List<String>>> publishCmt(@FieldMap Map<String, String> params);

}
