package com.tgf.kcwc.mvp.model;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface TransmitWinningHomeService {

    /**
     * 转发有奖列表
     */
    @GET("/activityforward/index/lists")
    Observable<TransmitWinningHomeListBean> getIndexLists(@Query("token") String token,
                                                          @Query("page") int page);

    /**
     * 转发有奖列表
     */
    @GET(" /activityforward/user/lists")
    Observable<TransmitWinningHomeListBean> getUserLists(@Query("token") String token, @Query("page") int page);

    /**
     * 转发有奖详情
     */
    @GET("/activityforward/Index/details")
    Observable<TransmitWinningDetailsBean> getDetails(@Query("token") String token,
                                                      @Query("id") String id);

    /**
     * 转发有奖榜单
     */
    @GET("/activityforward/index/recordlists")
    Observable<TransmitWinningCrunchiesBean> getRecordLists(@Query("token") String token,
                                                            @Query("fid") String fid);

    /**
     * 转发有奖成功
     */
    @GET("/activityforward/user/forward")
    Observable<TransmitDrawSucceedBean> getForward(@Query("token") String token,
                                    @Query("fid") String fid, @Query("type") String type);

    /**
     * 抽奖
     */
    @GET("/activityforward/user/forward")
    Observable<TransmWinningResultBean> getForward2(@Query("token") String token,
                                                    @Query("fid") String fid, @Query("type") String type);

    /**
     * 抽奖记录
     */
    @GET("/activityforward/user/recordDetails")
    Observable<RaffleRecordBean> getrecordDetails(@Query("token") String token, @Query("fid") String fid);

    /**
     * 奖品列表
     */
    @GET("/activityforward/Index/prizeList")
    Observable<TransmitWinningRaffleBean> getPrizeList(@Query("token") String token, @Query("id") String id);
    /**
     * 个人主页荣誉榜单
     */
    @GET("/tongji/tongji/index")
    Observable<MeCrunchiesBean> getIndexList(@Query("token") String token, @Query("type") String type);
}
