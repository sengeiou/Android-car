package com.tgf.kcwc.mvp.model;

import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Author：Jenny
 * Date:2017/2/7 15:00
 * E-mail：fishloveqin@gmail.com
 * 证件网络请求Service
 */

public interface CertifcateService {

    @GET("cert/cert/lists")
    Observable<ResponseMessage<CheckinTypeModel>> professionalCertList(@Query("event_id") String senseId,
                                                                       @Query("type") String type,
                                                                       @Query("qd_id") String channelId,
                                                                       @Query("id") String id);

    @FormUrlEncoded
    @POST("cert/user/applyForCert")
    Observable<ResponseMessage<Object>> commitForms(@FieldMap Map<String, String> params);

    @GET("cert/user/lists")
    Observable<ResponseMessage<CertListModel>> getCertLists(@Query("token") String token);

    @GET("cert/cert/details")
    Observable<ResponseMessage<CertDetailModel>> getCertDetail(@Query("is_need_receive") String isNeedReceive

                                                               , @Query("id") String id,
                                                               @Query("is_need_tongji") String isNeedTotal,
                                                               @Query("code") String code,

                                                               @Query("token") String token); //is_need_receive=&id=&is_need_tongji=&code=

    @GET("cert/user/printCert")
    Observable<ResponseMessage<CertResultModel>> getPrintData(@Query("id") String id,
                                                              @Query("token") String token);

    @GET("cert/user/setStatus")
    Observable<ResponseMessage<Object>> applyCertLoss(@Query("id") String id,
                                                      @Query("token") String token);

    @GET("cert/cert/certContent")
    Observable<ResponseMessage<CertResultModel>> getCertTypeDesc(@Query("id") String id);

    @GET("cert/user/follow")
    Observable<ResponseMessage<AccessRecordModel>> getAccessRecords(@Query("id") String id,
                                                                    @Query("token") String token);

    @GET("cert/cert/certContent")
    Observable<ResponseMessage<CertResultModel>> getCertDesc(@Query("id") String id);

    @GET("cert/user/applyStatus")
    Observable<ResponseMessage<Object>> getCertStatus(@Query("cid") String cid,
                                                      @Query("event_id") String eventId,
                                                      @Query("qd_id") String qdId,
                                                      @Query("token") String token);

    @FormUrlEncoded
    @POST("cert/cert/certRelation")
    Observable<ResponseMessage<Object>> commitCertRelationshipForms(@FieldMap Map<String, String> params); //证件注册关联手机号码

    @GET("cert/cert/orgLists")
    Observable<ResponseMessage<List<OrganizationBean>>> getOrgList(@Query("keyword") String keyword);
}
