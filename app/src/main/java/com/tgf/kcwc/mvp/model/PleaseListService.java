package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface PleaseListService {


    /**
     * 请你玩 我报名的
     */
    @GET("thread/play/myapply")
    Observable<PleaseAppBean> getAppList(@Query("token") String token,@Query("page") int page);

    /**
     * 请你玩 我发起的
     */
    @GET("thread/play/mycreate")
    Observable<PleaseFoundListBean> getCreateList(@Query("token") String token,@Query("page") int page);
    /**
     * 请你玩 取消我发起的
     */
    @GET("thread/activity_apply/activitycancel")
    Observable<BaseBean> getActivityCancel(@Query("token") String token,@Query("id") String id);

    /**
     * 请你玩 取消报名
     */
    @GET("thread/activity_apply/cancel")
    Observable<BaseBean> getCancel(@Query("token") String token,@Query("id") String id);
}
