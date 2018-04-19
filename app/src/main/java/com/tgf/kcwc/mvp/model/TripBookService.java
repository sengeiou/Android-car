package com.tgf.kcwc.mvp.model;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;


/**
 * Auther: Scott
 * Date: 2017/4/28 0028
 * E-mail:hekescott@qq.com
 * 路书网络请求Service
 */

public interface TripBookService {

    @GET("roadbook/index/myRoadBookList.html")
    Observable<ResponseMessage<TripBookModel>> getMyTripBookList(@Query("token") String token);

    @GET("roadbook/index/recommend")
    Observable<ResponseMessage<TripBookModel>> getripBookRecomenList(@Query("lat") String lat,@Query("lng") String lng,@Query("page")int page);

    @GET("roadbook/index/find")
    Observable<ResponseMessage<TripBookModel>> getripBookFindList(@Query("lat") String lat,@Query("lng") String lng
            ,@Query("day") Integer day,@Query("start_adds") String startLat,@Query("end_adds") String endLng,@Query("page") int page);
    @GET("roadbook/index/lists")
    Observable<ResponseMessage<TripBookModel>> getripBookPlayList(@Query("lat") String lat,@Query("lng") String lng
            ,@Query("day") Integer day,@Query("start_adds") String startLat,@Query("end_adds") String endLng,@Query("page") int page);
    @GET("roadbook/index/lists")
    Observable<ResponseMessage<TripBookModel>> getripBookPlayList(@QueryMap Map<String, String> params);
    @GET("roadbook/index/city")
    Observable<ResponseMessage<TripBookModel>> getripBookCityList(@Query("lat") String lat, @Query("lng") String lng, @Query("adcode") String adcode,@Query("page") int page);

    @GET("roadbook/create/index.html")
    Observable<ResponseMessage<EditbookModel>> getEditbookInfo(@Query("token") String token,@Query("rideId") int rideId);

    @GET("roadbook/create/getRide.html")
    Observable<ResponseMessage<EditbookModel>> getEditbookRideInfo(@Query("token") String token,@Query("bookId") int bookId);
    @GET("roadbook/create/getRide.html")
    Observable<ResponseMessage<RideDataModel>> getRideLines(@Query("token") String token);
    @GET("roadbook/index/delete")
    Observable<ResponseMessage> deleterTripBook(@Query("book_id") int bookId,@Query("token") String token);

    @GET(" /roadbook/index/getAroundByGCT")
    Observable<ResponseMessage<NearAttractionModel>> getNearAttraction(@Query("token") String token,@Query("type") int type,@Query("lat") String lat
            ,@Query("lng") String lng,@Query("page") Integer page,@Query("pageSize")Integer pageSize);

    @FormUrlEncoded
    @POST("roadbook/create/nodeSave")
    Observable<ResponseMessage> createRoadBook(@FieldMap Map<String, Object> params);

    @GET("roadbook/index/detail.html")
    Observable<ResponseMessage<TripBookDetailModel>> getTripBookDetail(@Query("bookId") String bookId, @Query("token") String token);
    @GET("roadbook/index/aroundThread")
    Observable<ResponseMessage<TripBookThreadModel>> getTripBookThread(@Query("bookLineId") int bookLineId,@Query("page") int page,@Query("pageSize") int pageSize);
    @GET("roadbook/index/aroundOrg")
    Observable<ResponseMessage<TripAroundOrgModel>> getTripAroudOrg(@Query("bookLineId") int bookLineId,@Query("page") int page,@Query("pageSize") int pageSize);
    @GET(" roadbook/index/aroundCoupon")
    Observable<ResponseMessage<TripAroundCouponModel>> getTripAroudCoupon(@Query("bookLineId") int bookLineId,@Query("page") int page,@Query("pageSize") int pageSize);
    @GET("roadbook/index/aroundAlong")
    Observable<ResponseMessage<TripAroundTongxModel>> getTripAroudTongX(@Query("bookLineId") int bookLineId);
    @GET("roadbook/index/map.html")
    Observable<ResponseMessage<TripBookMapModel>> getTripMapdetail(@Query("bookId") int bookId,@Query("lat") String lat,@Query("lng") String lng);
}
