package com.tgf.kcwc.mvp.model;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/9/15
 * @describle
 */
public interface ExhibitionApplyService {
    /**
     *
     * @param token
     * @param applyId
     * @param applyName
     * @param applyPhone
     * @param carImageIn
     * @param carImageOut
     * @param drivingLicense
     * @param idcardBack
     * @param idcardFront
     * @param parkTimeId
     * @param plateNumber
     * @param threadId
     * @return
     */
    @POST("car/consumersale/apply")
    @FormUrlEncoded
    Observable<ExhibitionApplyModel> commitApply(@Field("token") String token,
                                                                  @Field("apply_id") int applyId,
                                                                  @Field("apply_name") String applyName,
                                                                  @Field("status") int status,
                                                                  @Field("car_image_in") String carImageIn,
                                                                  @Field("car_image_out") String carImageOut,
                                                                  @Field("driving_license") String drivingLicense,
                                                                  @Field("event_id") int eventId,
                                                                  @Field("idcard_back") String idcardBack,
                                                                  @Field("idcard_front") String idcardFront,
                                                                  @Field("park_time_id") int parkTimeId,
                                                                  @Field("plate_number") String plateNumber,
                                                                  @Field("thread_id") int threadId,
                                                                  @Field("park_id") int parkId
                                                                  );
}
