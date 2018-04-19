package com.tgf.kcwc.mvp.model;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @anthor Administrator
 * @time 2017/8/10
 * @describle
 */
public interface GroupingService {
    /**
     * 好友分组
     * @return
     */
    @FormUrlEncoded
    @POST("friend/friend/grouping")
    Observable<AddCustomerModel> grouping(@Field("token") String token,@Field("friend_group_id") int friendGroupId,@Field("ids") String ids);
}
