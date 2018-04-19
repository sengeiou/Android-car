package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.FilterItem;
import com.tgf.kcwc.mvp.model.GeneralizationService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.FilterDataView;
import com.tgf.kcwc.util.RXUtil;

import java.util.List;

import rx.Observer;
import rx.Subscription;

/**
 * Author：Jenny
 * Date:2017/2/13 21:06
 * E-mail：fishloveqin@gmail.com
 */

public class FilterDataPresenter extends WrapPresenter<FilterDataView> {

    private FilterDataView        mView;
    private Subscription          mSubscription;
    private GeneralizationService mService = null;

    @Override
    public void attachView(FilterDataView view) {
        this.mView = view;
        mService = ServiceFactory.getGeneralizationService();
    }

    /**
     *
     * 加载区域列表
     *
     */
    public void loadFilterAreaList(String tag) {

        mSubscription = RXUtil.execute(mService.getAreaDatas(tag),
            new Observer<ResponseMessage<List<FilterItem>>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(ResponseMessage<List<FilterItem>> listResponseMessage) {

                    mView.showData(listResponseMessage.data);
                }
            });

        mSubscriptions.add(mSubscription);
    }

    /**
     *
     * 加载区域列表
     *
     */
    public void loadFilterGoodsList(String tag) {

        mSubscription = RXUtil.execute(mService.getCategoryDatas(tag),
            new Observer<ResponseMessage<List<FilterItem>>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(ResponseMessage<List<FilterItem>> listResponseMessage) {

                    mView.showData(listResponseMessage.data);
                }
            });

        mSubscriptions.add(mSubscription);
    }
}
