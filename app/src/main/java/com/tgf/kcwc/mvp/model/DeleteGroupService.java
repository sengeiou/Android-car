package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/8/10
 * @describle
 */
public interface DeleteGroupService {
    /**
     * 删除分组
     * @param token
     * @param groupId
     * @return
     */
    @GET("friend/group/delete")
    Observable<AddCustomerModel> deleteGroup(@Query("token") String token,@Query("id") int groupId);
}
