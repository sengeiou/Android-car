package com.tgf.kcwc.mvp.model;

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
 * Auther: Scott
 * Date: 2017/1/13 0013
 * E-mail:hekescott@qq.com
 */
public interface DrivingService {

    /**
     * 开车去列表
     */
    @GET("thread/cycle/lists")
    Observable<DrvingListModel> getDrivingList(@QueryMap Map<String, String> params);

    /**
     * 轮播
     */
    @GET("carousel")
    Observable<BannerModel> getDrivingBanner(@Query("token") String token,
                                             @Query("t") String t,
                                             @Query("mark") String mark);

    /**
     * 开车去详情
     */
    @GET("thread/cycle/detail")
    Observable<DrivDetailsBean> getDetailsData(@Query("token") String token,
                                               @Query("id") String id);

    /**
     * 路书
     */
    @GET("ride/ride/getactivityline")
    Observable<DrivingRoadBookBean> getDetails(@Query("token") String token,
                                               @Query("thread_id") String threadId);

    /**
     * 开车去详情 报名列表
     */
    @GET("thread/activity_apply/lists")
    Observable<ApplyListBean> getApplylist(@Query("token") String token, @Query("id") String id, @Query("page") int page);

    /**
     * 报名列表 处理
     */
    @GET("thread/activity_apply/check")
    Observable<BaseBean> getCheck(@Query("token") String token, @Query("id") String id, @Query("status") int status);

    /**
     * 报名列表  关注
     */
    @POST("user/follow")
    @FormUrlEncoded
    Observable<BaseBean> getFollow(@Field("token") String token, @Field("follow_id") String follow_id);

    /**
     * 报名列表  取消关注
     */
    @POST("user/follow/cancel")
    @FormUrlEncoded
    Observable<BaseBean> getCancel(@Field("token") String token, @Field("follow_id") String follow_id);

    /**
     * 检查状态
     */
    @GET("user/follow/relation")
    Observable<RelationBean> getFollowRelation(@Query("token") String token, @Query("follow_id") String follow_id);

    /**
     * 精彩分享
     */
    @GET("thread/cycle/sharelist")
    Observable<ShareSplendBean> getShareList(@Query("token") String token, @Query("id") String id);

    /**
     * 报名
     */
    @POST("thread/activity_apply/apply")
    @FormUrlEncoded
    Observable<BaseBean> getApply(@Field("token") String token, @Field("nickname") String nickname, @Field("num") String num,
                                  @Field("reason") String reason, @Field("tel") String tel, @Field("thread_id") String thread_id);

    /**
     * 发起开车去
     */
    @POST("thread/cycle/create")
    @FormUrlEncoded
    Observable<BaseBean> getCreate(@FieldMap Map<String, String> params);

    /**
     * 修改开车去
     */
    @POST("thread/cycle/edit")
    @FormUrlEncoded
    Observable<BaseBean> getEdit(@FieldMap Map<String, String> params);

    /**
     * 编辑开车去回显
     */
    @GET("thread/cycle/edit")
    Observable<CompileDrivingBean> getEdit(@Query("token") String token, @Query("id") String id);

    /**
     * 取消报名
     */
    @GET("thread/activity_apply/cancel")
    Observable<BaseBean> getApplyCancel(@Query("token") String token, @Query("thread_id") String id);

    /**
     * 报名协议
     */
    @GET("agreement/agreement/detail")
    Observable<ResponseMessage<List<AgreementModel>>> getAgreement(@Query("place") String place, @Query("platform") String platform);

}
