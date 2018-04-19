package com.tgf.kcwc.mvp.model;

import java.util.ArrayList;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Author：Scott
 * Date:2016/12/13 15:27
 * E-mail：hekescott@qq.com
 * 获取经销商Service
 */

public interface MotoDeatailService {
//    @FormUrlEncoded
//    @POST("public/organization")
//    Observable<ResponseMessage<OrganizationList>> getOrglist(@Field("brand_id") String brandid,
//                                                    @Field("city_id") String cityid,
//                                                    @Field("filter_coupon") String scoupon,
//                                                    @Field("latitude") String latitude,
//                                                    @Field("longitude") String longitude,
//                                                    @Field("moto_id") String motoid,
//                                                    @Field("type") String type);

    /**
     *  摩托车详情
     * @param motoid
     * @return
     */
    @GET("car/car/cardetail")
    Observable<ResponseMessage<CarBean>> getMotoDetail(@Query("id") String motoid, @Query("token") String token);
    /**
     *  服务城市
     * @return
     */
    @GET("city/index/openlist")
    Observable<ResponseMessage<ServiceCity>> getOpenCity(@Query("lat") String lat,@Query("lng") String lng);

    /**
     *  车型分类图库
     * @return
     */
    @GET("event/photo/carimg")
    Observable<ResponseMessage<CarModelPicsModel>> getCarModelCatgoryImg(@Query("car_id") int carId);
    /**
     *  车辆详情-达人评测
     * @return
     */
    @GET("thread/evaluate/more")
    Observable<ResponseMessage<ArrayList<DaRenEvaluateModel>>> getDarenEvaluate(@Query("car_id") int carId);
    /**
     *  车辆详情-车主自售
     * @return
     */
    @GET("thread/goods/more_by_car")
    Observable<ResponseMessage<ArrayList<CarOwnerSalerModel>>> getCarOwnerList(@Query("car_id") int carId);
    /**
     * 经销商列表
     */
    @GET("orgshop/detail/orgshoplist")
    Observable<ResponseMessage<ArrayList<OrgModel>>> getExhibitOrgModel(@Query("id") int brandId, @Query("longitude") String longitude, @Query("latitude") String latitude);
}
