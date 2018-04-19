package com.tgf.kcwc.mvp.model;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Auther: Scott
 * Date: 2017/2/20 0020
 * E-mail:hekescott@qq.com
 */

public interface MotoBuyService {
    @FormUrlEncoded
    @POST("carorder/order/add")
    Observable<ResponseMessage<MotoOrder>> commitMotoOrder(@Field("token") String token,
                                                           @Field("car_id") int carId,
                                                           @Field("car_series_id") int carSeriesId,  @Field("in_color_id") int inLookColorId,  @Field("out_color_id") int outLookColorId,
                                                           @Field("contact") String contact,
                                                           @Field("description") String description,
                                                           @Field("lat") String lat,
                                                           @Field("lng") String log,
                                                           @Field("tel") String tel);
    @GET("carorder/order/wait_response")
    Observable<ResponseMessage<WaittingPriceModel>> getWaittingPrice(@Query("token") String token,
                                                                     @Query("id") int carId,
                                                                     @Query("lat") String lat,
                                                                     @Query("lng") String log);

    @GET("carorder/order/complete_detail")
    Observable<ResponseMessage<YuyueBuyModel>> getCompleteServer(@Query("token") String token,
                                                                       @Query("id") int carId);

    @GET("carorder/order/book")
    Observable<ResponseMessage<YuyueBuyModel>> getYuyueBuy(@Query("token") String token,
                                                           @Query("id") int carId);
    @GET("carorder/order/new_response")
    Observable<ResponseMessage<NewPriceModel>> getNewPrice(@Query("token") String token,

                                                           @Query("id") int orderId);
    @FormUrlEncoded
    @POST("carorder/order/cancel")
    Observable<ResponseMessage> deleteOrder(@Field("token") String token,@Field("id") int orderId);
    @FormUrlEncoded
    @POST("carorder/order/discussion_act")
    Observable<ResponseMessage> postEvaluation(@Field("token") String token, @Field("id") int id,
                                               @Field("offer_id") int offer_id,
                                               @Field("star") int star, @Field("tag") String tag,
                                               @Field("text") String text,@Field("is_anonymous") int isAnys);
    @GET("carorder/order/discussion")
    Observable<ResponseMessage<EvaluationModel>> getTagList(@Query("token") String token, @Query("id") int id,
                                               @Query("offer_id") int offer_id);
    @FormUrlEncoded
    @POST("carorder/offer/ndisturb")
    Observable<ResponseMessage> postNodisturb(@Field("token") String token, @Field("id") int id,
                                               @Field("offer_id") int offer_id);
    @FormUrlEncoded
    @POST("carorder/offer/complete")
    Observable<ResponseMessage> postOrderfellowComplete(@Field("token") String token, @Field("id") int id,
                                               @Field("offer_id") int offer_id);

    @FormUrlEncoded
    @POST("carorder/order/complete")
    Observable<ResponseMessage> postCarOrderComplete(@Field("token") String token, @Field("id") int id);

    /**
     * 关闭报价
     */
    @FormUrlEncoded
    @POST("carorder/order/close")
    Observable<ResponseMessage> postClosePrice(@Field("token") String token, @Field("id") int id);

    /**
     * 我的看车请求
     */
    @GET("carorder/order/lists")
    Observable<ResponseMessage<MySeecarMsgModel>> getMySeecarMsg(@Query("token") String token,
                                                           @Query("page") int page,@Query("pageSize") int pageSize,@Query("type") int type);
    /**
     * 我的看车请求
     * @return
     */
    @FormUrlEncoded
    @POST("carorder/order/remove")
    Observable<ResponseMessage> postRemoveOrder(@Field("token") String token,@Field("id") int type);


}
