package com.tgf.kcwc.mvp.model;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/8/15 17:39
 */

public interface EditCustomerService {
    /**
     * 保存客户资料
     */
    @POST("friend/friend/edit")
    @FormUrlEncoded
    Observable<ResponseMessage<AddCustomerModel>> saveCustomerDetail(@Field("token") String token,
                                                                        @Field("birthday") String birthday,
                                                                        @Field("company") String company,
                                                                        @Field("department") String department,
                                                                        @Field("home_address") String homeAddress,
                                                                        @Field("id") int id,
                                                                        @Field("latitude") String latitude,
                                                                        @Field("longitude") String longitude,
                                                                        @Field("name") String name,
                                                                        @Field("position") String position,
                                                                        @Field("qq") String qq,
                                                                        @Field("remark") String remark,
                                                                        @Field("s_address") String sAddress,
                                                                        @Field("tel") String tel,
                                                                        @Field("wechat") String wechat,
                                                                        @Field("weibo") String weibo
    );
}
