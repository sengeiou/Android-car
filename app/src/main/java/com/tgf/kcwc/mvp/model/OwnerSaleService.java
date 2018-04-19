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
 * Author：Jenny
 * Date:2017/2/10 16:34
 * E-mail：fishloveqin@gmail.com
 */

public interface OwnerSaleService {

    @GET("thread/goods")
        // @Query("goods_type") String type
    Observable<ResponseMessage<SaleListModel>> getSaleList(@Query("area_id") String areaId,
                                                           @Query("brand_id") String brandId,
                                                           @Query("car_series_id") String seriesId,
                                                           @Query("max_price") String maxPrice,
                                                           @Query("min_price") String minPrice,
                                                           @Query("sort") String sort);


    @GET("thread/goods")
        // @Query("goods_type") String type
    Observable<ResponseMessage<SaleListModel>> getSaleList(@QueryMap Map<String, String> params);


    @GET("thread/goods/detail")
    Observable<ResponseMessage<SaleDetailModel>> getSaleDetail(@Query("thread_id") String id, @Query("token") String token);

    /**
     * @param params
     * @param eventId 展会ID 0表示不参展
     * @return
     */
    @FormUrlEncoded
    @POST("thread/create/goods")
    Observable<OwnerSaleModel> publishOwnerSaleGoods(@FieldMap Map<String, String> params,
                                                     @Field("event_id") int eventId
    );
    /**
     * @param params
     * @param eventId 展会ID 0表示不参展
     * @return
     */
    @FormUrlEncoded
    @POST("thread/create/editGoods")
    Observable<OwnerSaleEditModel> publishOwnerSaleEditGoods(@FieldMap Map<String, String> params,
                                                     @Field("event_id") int eventId,
                                                     @Field("thread_id") int threadId
    );

    @GET("thread/goods/my")
    Observable<ResponseMessage<SaleMgrModel>> getSaleMgrList(@Query("status") String status,
                                                             @Query("token") String token);

    @FormUrlEncoded
    @POST("thread/goods/down")
    Observable<ResponseMessage<List<String>>> unShelve(@Field("id") String id,
                                                       @Field("token") String token);

    @GET("thread/goods/edit_msg")
    Observable<ResponseMessage<GoodsEditModel>> goodsEditInfo(@Query("id") String id);

    @GET("thread/goods/msg_list")
    Observable<ResponseMessage<List<Account.UserInfo>>> getContactList(@Query("thread_id") String id,
                                                                       @Query("token") String token);

    @GET("thread/goods/msg_detail")
    Observable<ResponseMessage<List<ChatMessageBean>>> getMsgDatas(@Query("thread_id") String id,
                                                                   @Query("user_id") String user_id,
                                                                   @Query("token") String token);

}
