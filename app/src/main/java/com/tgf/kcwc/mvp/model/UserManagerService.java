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
 * Date:2016/12/8 20:27
 * E-mail：fishloveqin@gmail.com
 * 用户基本操作Service
 */

public interface UserManagerService {

    @FormUrlEncoded
    @POST("account/login")
    Observable<ResponseMessage<Account>> login(@Field("tel") String tel,
                                               @Field("ver_code") String verCode);

    /**
     * 验证码请求
     *
     * @return
     */
    @GET("publics/verify/send")
    Observable<ResponseMessage> sendSMS(@Query("tel") String tel, @Query("timestamp") String timestamp,
                                        @Query("nonce") String nonce, @Query("sign") String sign);

    /**
     * 验证码请求
     *
     * @return
     */
    @GET("publics/verify/voice")
    Observable<ResponseMessage> sendVoice(@Query("tel") String tel, @Query("timestamp") String timestamp,
                                          @Query("nonce") String nonce, @Query("sign") String sign);

    /**
     * 微信授权登录请求
     *
     * @return
     */
    @GET("account/wechat/login")
    Observable<ResponseMessage<Account>> getWxLogin(@Query("code") String code);

    /**
     * 微信绑定手机号
     *
     * @return
     */
    @FormUrlEncoded
    @POST("account/wechat/bindMobile")
    Observable<ResponseMessage<Account>> postBindPhone(@Field("openid") String openid, @Field("ver_code") String verCode, @Field("tel") String tel);

    /**
     * 微信更新绑定手机号
     *
     * @return
     */
    @FormUrlEncoded
    @POST("account/wechat/changebind")
    Observable<ResponseMessage> postUpdateBindPhone(@Field("openid") String openid, @Field("uid") String userId, @Field("token") String token);

    /**
     * 密码登录
     *
     * @return
     */
    @FormUrlEncoded
    @POST("account/login/index")
    Observable<ResponseMessage<Account>> pwdLogin(@Field("tel") String tel,
                                                  @Field("pwd") String pwd,
                                                  @Field("current_city") String current_city,
                                                  @Field("register_latitude") String register_latitude,
                                                  @Field("register_longitude") String register_longitude,
                                                  @Field("terminal") int terminal,
                                                  @Field("app_code") String app_code);

    /**
     * 手机验证码登录
     *
     * @return
     */

    @FormUrlEncoded
    @POST("account/login/tellogin")
    Observable<ResponseMessage<Account>> vertifyCodeLogin(@Field("tel") String tel,
                                                          @Field("ver_code") String ver_code,
                                                          @Field("current_city") String current_city,
                                                          @Field("register_latitude") String register_latitude,
                                                          @Field("register_longitude") String register_longitude,
                                                          @Field("terminal") int terminal,
                                                          @Field("app_code") String app_code,
                                                          @Field("merchant_type") int merchant_type);

    /**
     * 编辑用户信息
     *
     * @return
     */

    @FormUrlEncoded
    @POST("account/user/edit")
    Observable<ResponseMessage<Object>> editUserInfo(@Field("avatar") String avatarurl,
                                                     @Field("user_id") String userID,
                                                     @Field("ver_code") String verCode,
                                                     @Field("tel") String tel,
                                                     @Field("gender") Integer gender,
                                                     @Field("pwd") String pwd,
                                                     @Field("nick_name") String nickName);

    /**
     * 编辑用户信息
     *
     * @return
     */

    @FormUrlEncoded
    @POST("account/user/setpwd")
    Observable<ResponseMessage<Object>> setPWD(@Field("ver_code") String verCode,
                                               @Field("tel") String tel, @Field("pwd") String pwd);

    /**
     * 会员中心-首页
     *
     * @param token
     * @param type  传入APP表示入口为移动APP端
     * @return
     */
    @GET("user/user/center")
    Observable<ResponseMessage<UserHomeDataModel>> getUserHomeInfo(@Query("token") String token, @Query("type") String type);

    /**
     * 会员中心-更换背景图/头像
     *
     * @param token
     * @return
     */
    @POST("/user/homepage/replacepic")
    @FormUrlEncoded
    Observable<ResponseMessage<Account.UserInfo>> getReplacepic(@Field("token") String token, @Field("type") String type, @Field("url") String url);

    /**
     * 发送短信
     *
     * @param type 类型
     * @param tel  手机号码
     * @return
     */
    @GET("user/user/sendsms")
    Observable<ResponseMessage<Object>> sendSMS(@Query("type") String type,
                                                @Query("tel") String tel,
                                                @Query("token") String token);

    /**
     * 获取个主主页详细数据
     *
     * @param userId
     * @return
     */
    @GET("user/homepage/main")
    Observable<ResponseMessage<PersonHomeDataModel>> getPersonalDetailData(@Query("user_id") String userId,
                                                                           @Query("token") String token);

    /**
     * 获取个人基本数据
     *
     * @param userId
     * @return
     */
    @GET("user/homepage/topinfo")
    Observable<ResponseMessage<UserBaseDataModel>> getPersonalBaseData(@Query("user_id") String userId,
                                                                       @Query("token") String token);

    /**
     * 用户关注列表
     *
     * @param userId
     * @return
     */
    @GET("user/follow/followlist")
    Observable<ResponseMessage<AttentionDataModel>> getAttentionList(@Query("user_id") String userId,
                                                                     @Query("token") String token);

    /**
     * 用户粉丝列表
     *
     * @param userId
     * @return
     */
    @GET("user/follow/lists")
    Observable<ResponseMessage<AttentionDataModel>> getFansList(@Query("user_id") String userId,
                                                                @Query("token") String token);

    /**
     * 用户关注更多列表
     *
     * @param userId
     * @return
     */
    @GET("user/follow/recommend")
    Observable<ResponseMessage<List<AttentionDataModel.UserInfo>>> getAttentionMoreList(@Query("user_id") String userId,

                                                                                        @Query("token") String token);

    /**
     * 用户动态信息
     *
     * @param userId
     * @return
     */
    @GET("user/homepage/dynamic")
    Observable<ResponseMessage<UserDynamicModel>> getDynamicInfo(@Query("user_id") String userId,
                                                                 @Query("token") String token);

    /**
     * 用户详细信息
     *
     * @param userId
     * @return
     */
    @GET("user/homepage/detail")
    Observable<ResponseMessage<UserDetailDataModel>> getUserDetailInfo(@Query("user_id") String userId,
                                                                       @Query("token") String token);

    /**
     * 用户兴趣标签
     *
     * @return
     */
    @GET("tags/category/lists")
    Observable<ResponseMessage<List<HobbyTag>>> getHobbyTags(@Query("token") String token);

    /**
     * 修改用户资料
     *
     * @param params
     * @return
     */
    @POST("user/homepage/edit")
    @FormUrlEncoded
    Observable<ResponseMessage<Account.UserInfo>> updateUserInfo(@FieldMap Map<String, String> params);

    @GET("collect/collect/lists")
    Observable<ResponseMessage<MyFavoriteDataModel>> getMyFavoriteData(@Query("model") String model,
                                                                       @Query("page") String page,
                                                                       @Query("page_size") String pageSize,
                                                                       @Query("token") String token);

    @GET("account/user/easemobuser")
    Observable<ResponseMessage<EaseLoginModel>> getLoginEase(@Query("token") String token, @Query("type") String type);

    /**
     * 删除收藏数据
     *
     * @param params
     * @return
     */
    @POST("collect/collect/remove")
    @FormUrlEncoded
    Observable<ResponseMessage<Object>> deleteFavoriteData(@FieldMap Map<String, String> params);

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


    @GET("user/homepage/ride")
    Observable<ResponseMessage<UserRideDataModel>> getRideData(@Query("user_id") String userId);


    /**
     * @param nickname
     * @return
     */
    @POST("user/homepage/editnickname")
    @FormUrlEncoded
    Observable<ResponseMessage<Object>> checkNicknameIsExist(@Field("nickname") String nickname, @Field("token") String token);

}
