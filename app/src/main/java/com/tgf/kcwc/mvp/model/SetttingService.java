package com.tgf.kcwc.mvp.model;

import org.json.JSONObject;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 设置接口集合
 * Auther: Scott
 * Date: 2017/7/31 0031
 * E-mail:hekescott@qq.com
 */

public interface SetttingService {
    /**
     * 反馈
     */
    @POST("account/feedback/create")
    @FormUrlEncoded
    Observable<ResponseMessage> postFanKuiMsg(@Field("token") String token, @Field("tel") String tel,
                                              @Field("content") String content);
    /**
     * 设置代金券在线
     */
    @GET("online/coupon/set_status")
    Observable<ResponseMessage> setCouponOnline(@Query("token") String token);
    /**
     * 获取代金券在线
     */
    @GET("online/coupon/get_status")
    Observable<ResponseMessage> getCouponOnline(@Query("token") String token);
}
