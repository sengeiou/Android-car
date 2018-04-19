package com.tgf.kcwc.mvp.model;


import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/8/8 14:20
 */

public interface AddCustomerService {
    /**
     * 添加客户
     * @param name
     * @param tel
     * @param friendGroupId
     * @return
     */
    @FormUrlEncoded
    @POST("friend/friend/create")
    Observable<ResponseMessage<AddCustomerModel>> addFriend(@Field("token") String token, @Field("name") String name, @Field("tel") String tel, @Field("friend_group_id") int friendGroupId);
}
