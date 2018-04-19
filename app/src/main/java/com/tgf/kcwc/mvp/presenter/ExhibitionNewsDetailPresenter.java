package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ExhibitionNews;
import com.tgf.kcwc.mvp.model.ExhibitionService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.ExhibitionNewsDetailView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.Subscription;

/**
 * Auther: Scott
 * Date: 2017/1/20 0020
 * E-mail:hekescott@qq.com
 */

public class ExhibitionNewsDetailPresenter extends WrapPresenter<ExhibitionNewsDetailView> {
    private ExhibitionNewsDetailView mView;
    private ExhibitionService        mService;

    @Override
    public void attachView(ExhibitionNewsDetailView view) {
        mView = view;
        mService = ServiceFactory.getExhibitionService();
    }

    public void getExhibitionActicle(int newsId) {
     Subscription  subscription= RXUtil.execute(mService.getNewsDetail(newsId, IOUtils.getToken(mView.getContext())),
            new Observer<ResponseMessage<ExhibitionNews>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    mView.showLoadingTasksError();
                }

                @Override
                public void onNext(ResponseMessage<ExhibitionNews> exhibitionNewsResponseMessage) {
                    mView.showArticle(exhibitionNewsResponseMessage.getData());
                }
            });
        mSubscriptions.add(subscription);
    }
}
