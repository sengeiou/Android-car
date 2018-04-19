package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.SeekBean;
import com.tgf.kcwc.mvp.model.SeekDetailsBean;
import com.tgf.kcwc.mvp.model.SeekService;
import com.tgf.kcwc.mvp.view.SeekDetailsView;
import com.tgf.kcwc.mvp.view.SeekView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;

/**
 * Created by Administrator on 2017/4/21.
 */

public class SeekDetailsPresenter extends WrapPresenter<SeekDetailsView> {

    private SeekDetailsView mView;
    private SeekService mService;

    @Override
    public void attachView(SeekDetailsView view) {
        mView = view;
        mService = ServiceFactory.getSeekService();
    }

    public void getsDispatchList(String token, String dispatch, String thread_model, String name, int page) {
        RXUtil.execute(mService.getsDispatchList(token, dispatch, thread_model, name, page), new Observer<SeekDetailsBean>() {
            @Override
            public void onCompleted() {
                mView.getContext();
            }

            @Override
            public void onError(Throwable e) {
                mView.dataListDefeated("网络异常，稍候再试！");
            }

            @Override
            public void onNext(SeekDetailsBean seekBean) {
                if (seekBean.code == 0) {
                    mView.dataListSucceed(seekBean);
                } else {
                    mView.dataListDefeated(seekBean.msg);
                }
            }
        });
    }
}
