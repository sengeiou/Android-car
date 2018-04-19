package com.tgf.kcwc.mvp.model;

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
 * Created by Administrator on 2017/4/21.
 */

public interface IntegralService {


    /**
     * 积分个人信息
     */
    @GET("/points/user/userDetail")
    Observable<IntegralUserinfoBean> getUserinfo(@Query("token") String token, @Query("type") String type, @Query("vehicle_type") String vehicle_type);

    /**
     * 历史户积分明细exp经验,points积分
     */
    @GET("/points/user/userRecords")
    Observable<IntegralListBean> getRecordpList(@Query("token") String token, @Query("type") String type, @Query("page") int page);

    /**
     * 历史户积分明细exp经验,points积分
     */
    @GET("/points/user/orgRecords")
    Observable<OrgIntegralListBean> getOrgRecords(@Query("token") String token, @Query("org_id") String org_id, @Query("type") String type, @Query("page") int page);

    /**
     * 获取商品列表
     */
    @GET("/points/product/pointsshop")
    Observable<IntegralGoodListBean> getPointsshop(@Query("token") String token, @Query("type") String type,
                                                   @Query("vehicle_type") String vehicle_type,
                                                   @Query("page") int page);

    /**
     * 获取商品详情
     */
    @GET("/points/product/details")
    Observable<IntegralConversionGoodDetailsBean> getGooddetails(@Query("token") String token, @Query("id") String id);

    /**
     * 兑换商品
     */
    @POST("/points/user/buyProduct")
    @FormUrlEncoded
    Observable<IntegralExchangeBean> getBuyProduct(@FieldMap Map<String, String> params);

    /**
     * 兑换商品成功
     */
    @GET("/points/product/recordDetail")
    Observable<IntegralExchangeSucceedBean> getRecordDetail(@Query("id") String id);

    /**
     * 修改收件地址
     */
    @POST("/points/product/editAddress")
    @FormUrlEncoded
    Observable<IntegralExchangeSucceedBean> getEditAddress(@FieldMap Map<String, String> params);

    /**
     * 积分交易
     * type  0是全部 1是银元 2是金币  3是钻石
     */
    @GET("/points/goods/goodsList")
    Observable<IntegralDiamondListBean> getGoodsList(@Query("token") String token, @Query("type") String type, @Query("vehicle_type") String vehicle_type, @Query("page") int page);

    /**
     * 兑换纪录
     */
    @GET("/points/user/pointsReceiveList")
    Observable<IntegralRecordListBean> getPointsReceiveList(@Query("token") String token, @Query("vehicle_type") String vehicle_type, @Query("page") int page);

    /**
     * 兑换纪录详情
     */
    @GET("/points/product/recordDetail")
    Observable<IntegralRecordBean> getRecordDetail(@Query("token") String token, @Query("id") String id);

    /**
     * 购买详情
     */
    @GET("/points/goods/goodsDetail")
    Observable<IntegralPurchaseBean> getGoodsDetail(@Query("token") String token, @Query("id") String id);

    /**
     * 购买纪录
     */
    @GET("/points/goods/logRecords")
    Observable<IntegralPurchaseRecordListBean> getLogRecords(@Query("token") String token, @Query("page") int page);

    /**
     * 发布交易信息
     */
    @POST("/points/goods/addGoods")
    @FormUrlEncoded
    Observable<BaseArryBean> getAddGoods(@FieldMap Map<String, String> params);

    /**
     * 机构交易纪录
     */
    @GET("/points/goods/orgLogRecords")
    Observable<OrgIntegralRecordListBean> getOrgLogRecords(@Query("token") String token, @Query("vehicle_type") String vehicle_type, @Query("page") int page);

    /**
     * 机构交易纪录
     */
    @GET("/points/user/orgLogDetail")
    Observable<OrgIntegralDetailsBean> getOrgLogDetail(@Query("token") String token, @Query("id") String id);

    /**
     * 机构信息
     */
    @GET("/points/user/orgDetail")
    Observable<OrgDetailsBean> getOrgDetail(@Query("token") String token, @Query("org_id") String org_id, @Query("type") String type);


    /**
     * 个人积分购买详情
     *
     * @param id
     * @param token
     * @return
     */
    @GET("/points/goods/RecordsDetail")
    Observable<ResponseMessage<IntegralOrderDetailBean>> getOrderRecordsDetail(@Query("id") String id,
                                                                               @Query("token") String token);

    /**
     * 个人积分支付宝
     *
     * @param token
     * @return
     */
    @POST("/points/goods/aliAppPay")
    @FormUrlEncoded
    Observable<ResponseMessage<String>> getAliAppPay(@Field("order_id") String order_id,
                                                     @Field("token") String token);

    /**
     * 个人积分微信
     *
     * @param token
     * @return
     */
    @POST("/points/goods/wechatPay")
    @FormUrlEncoded
    Observable<ResponseMessage<OrderPayParam>> getWechatPay(@Field("order_id") String order_id, @Field("token") String token, @Field(" pay_type") String pay_type);

    /**
     * 订单状态
     *
     * @param token
     * @return
     */
    @GET("points/goods/isPayStatus")
    Observable<BaseArryBean> getIsPayStatus(@Query("id") String id, @Query("token") String token);

    /**
     * 生成订单
     *
     * @param token
     * @return
     */
    @POST("points/goods/buyGoods")
    @FormUrlEncoded
    Observable<ResponseMessage<String>> getBuyGoods(@Field("token") String token, @Field("goods_id") String goods_id);

}
