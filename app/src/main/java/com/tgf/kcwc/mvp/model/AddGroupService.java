package com.tgf.kcwc.mvp.model;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor noti
 * @time 2017/8/8 17:24
 */

public interface AddGroupService {
    /**
     * 增加分组名
     * @param name
     * @return
     */
    @FormUrlEncoded
    @POST("friend/group/create")
    Observable<AddGroupModel> addGroup(@Field("token") String token,@Field("name") String name);
}
