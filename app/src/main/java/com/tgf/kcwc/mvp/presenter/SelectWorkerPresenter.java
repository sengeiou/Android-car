package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ContactUser;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.TicketManageService;
import com.tgf.kcwc.mvp.model.WorkeModel;
import com.tgf.kcwc.mvp.view.SelectWorkerView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import java.util.ArrayList;

import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/10/20 0020
 * E-mail:hekescott@qq.com
 */

public class SelectWorkerPresenter extends WrapPresenter<SelectWorkerView> {
    SelectWorkerView mView;
    private TicketManageService mService;

    @Override
    public void attachView(SelectWorkerView view) {
        mView = view;
        mService = ServiceFactory.getTicketManageService();
    }
    public void getSelectWorkerList(String token){
        RXUtil.execute(mService.getWorkerList(token), new Observer<ResponseMessage<ArrayList<ContactUser>>>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage<ArrayList<ContactUser>> arrayListResponseMessage) {
                if (arrayListResponseMessage.statusCode == 0) {
                    mView.showWorkerList(arrayListResponseMessage.getData());
                } else {
                    CommonUtils.showToast(mView.getContext(), arrayListResponseMessage.statusMessage);
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }
}
