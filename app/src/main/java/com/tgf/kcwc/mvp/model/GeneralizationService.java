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
 * Date:2017/2/13 20:56
 * E-mail：fishloveqin@gmail.com
 * 通用基础服务
 */

public interface GeneralizationService {

    @GET("publics/area")
    Observable<ResponseMessage<List<FilterItem>>> getAreaDatas(@Query("parent_area") String tag);

    @GET("thread/goods/category")
    Observable<ResponseMessage<List<FilterItem>>> getCategoryDatas(@Query("parent_id") String tag);

    // 关注
    @FormUrlEncoded
    @POST("/user/follow")
    Observable<ResponseMessage<Object>> executeAttention(@Field("follow_id") String id,
                                                         @Field("token") String token);

    // 关注 加备注
    @FormUrlEncoded
    @POST("/user/follow")
    Observable<ResponseMessage<Object>> executeAttention(@Field("follow_id") String id,
                                                         @Field("token") String token, @Field("remark") String remark);

    // 取消关注
    @FormUrlEncoded
    @POST("/user/follow/cancel")
    Observable<ResponseMessage<Object>> cancelAttention(@Field("follow_id") String id,
                                                        @Field("token") String token);

    //app更新
    @GET("version/index/update")
    Observable<ResponseMessage<UpdateModel>> getUpdateApi(@Query("version_code") String verCode);

    /**
     * 添加收藏数据
     *
     * @param params
     * @return
     */
    @POST("collect/collect/add")
    @FormUrlEncoded
    Observable<ResponseMessage<Object>> addFavoriteData(@FieldMap Map<String, String> params);

    /**
     * 删除收藏数据
     *
     * @param params
     * @return
     */
    @POST("collect/collect/removeBySource")
    @FormUrlEncoded
    Observable<ResponseMessage<Object>> cancelFavoriteData(@FieldMap Map<String, String> params);

    /**
     * 轮播
     */
    @GET("carousel")
    Observable<BannerModel> getBanner(@Query("t") String t,
                                      @Query("mark") String mark);


    /**
     * 验证码请求
     *
     * @return
     */
    @GET("publics/verify/send")
    Observable<ResponseMessage<Object>> sendSMS(@Query("tel") String tel, @Query("timestamp") String timestamp,
                                        @Query("nonce") String nonce, @Query("sign") String sign,@Query("code_type") String type);
}
