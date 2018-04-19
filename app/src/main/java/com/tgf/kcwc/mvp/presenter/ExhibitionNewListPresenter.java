package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ExhibitionNewsListModel;
import com.tgf.kcwc.mvp.model.ExhibitionService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.NewsListView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;

/**
 * Auther: Scott
 * Date: 2017/1/18 0018
 * E-mail:hekescott@qq.com
 */

public class ExhibitionNewListPresenter extends WrapPresenter<NewsListView> {
    private NewsListView      mView;
    private ExhibitionService mService;

    @Override
    public void attachView(NewsListView view) {
        mView = view;
        mService = ServiceFactory.getExhibitionService();
    }

    public void getNewsList(int eventId, int pageSize, int page) {
        RXUtil.execute(mService.getNews(eventId, pageSize, page),
            new Observer<ResponseMessage<ExhibitionNewsListModel>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    mView.showLoadingTasksError();
                }

                @Override
                public void onNext(ResponseMessage<ExhibitionNewsListModel> exhibitionNewsListModelResponseMessage) {
                ExhibitionNewsListModel exhibitionNewsListModel =    exhibitionNewsListModelResponseMessage.getData();
                    if(exhibitionNewsListModel.newsList!=null&&exhibitionNewsListModel.newsList.size()!=0){
                        mView.showEventList(exhibitionNewsListModel.newsList);
                    }else{
                        mView.showNomore("没数据了");
                    }
                }
            });
    }
}
