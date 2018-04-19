package com.tgf.kcwc.mvp.model;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Auther: Scott
 * Date: 2017/3/9 0009
 * E-mail:hekescott@qq.com
 * 我要看请求接口
 */

public interface SeecarService {

    @GET("carorder/patrol_offer/wait_list")
    Observable<ResponseMessage<RapBuymotoModel>> getRapOrdermotoList(@Query("token") String token,
                                                                     @Query("page") int page, @Query("pageSize") int pageSize,
                                                                     @Query("all") int type);
    @GET("carorder/patrol_offer/track_lists")
    Observable<ResponseMessage<PreSeecarModel>> getPreSeecarList(@Query("token") String token, @Query("status") int status, @Query("page") int page, @Query("pageSize") int pageSize);

    @GET("carorder/patrol_order/detail")
    Observable<ResponseMessage<IRapOrderModel>> getIRapOrderDetail(@Query("token") String token, @Query("id") int id);

    @FormUrlEncoded
    @POST("carorder/patrol_offer/save")
    Observable<ResponseMessage<RapOrderPostModel>> postIRapOrderPrice(@Field("token") String token, @Field("id") int id, @Field("offer") int price);

    @GET("carorder/patrol_offer/track")
    Observable<ResponseMessage<OrderBuyTrackModel>> getBuyTrack(@Query("token") String token, @Query("offer_id") int offer_id);


}
