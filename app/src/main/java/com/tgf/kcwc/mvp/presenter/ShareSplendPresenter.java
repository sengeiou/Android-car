package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ApplyListBean;
import com.tgf.kcwc.mvp.model.AttentionBean;
import com.tgf.kcwc.mvp.model.DrivingService;
import com.tgf.kcwc.mvp.model.RelationBean;
import com.tgf.kcwc.mvp.model.ShareSplendBean;
import com.tgf.kcwc.mvp.view.ApplyView;
import com.tgf.kcwc.mvp.view.ShareView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;

/**
 * Created by Administrator on 2017/4/21.
 */

public class ShareSplendPresenter extends WrapPresenter<ShareView> {

    private ShareView mView;
    private DrivingService mService;
    @Override
    public void attachView(ShareView view) {
        mView=view;
        mService = ServiceFactory.getDrivingService();
    }

    public void getShareList( String token,String id){
        RXUtil.execute(mService.getShareList(token,id), new Observer<ShareSplendBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
            }

            @Override
            public void onNext(ShareSplendBean shareSplendBean) {
                if (shareSplendBean.code==0){
                    mView.dataListSucceed(shareSplendBean);
                }else {
                    mView.dataListDefeated("关注失败");
                }
            }
        });
    }


}
