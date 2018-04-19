package com.tgf.kcwc.mvp.model;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/9/14
 * @describle
 */
public interface ExhibitionDepositService {
    /**
     * 展位投诉
     * @param token
     * @param applyId
     * @param complain
     * @return
     */
    @POST("car/consumersale/addcomplain")
    @FormUrlEncoded
    Observable<ResponseMessage> exhibitionDeposit(@Field("token") String token,
                                                  @Field("apply_id") int applyId,
                                                  @Field("complain") String complain,
                                                  @Field("complain_img_1") String complainImg1,
                                                  @Field("complain_img_2") String complainImg2,
                                                  @Field("complain_img_3") String complainImg3,
                                                  @Field("complain_img_4") String complainImg4,
                                                  @Field("complain_img_5") String complainImg5
                                                  );
}
