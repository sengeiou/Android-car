package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.BankCardModel;
import com.tgf.kcwc.mvp.model.MyWalletService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.BindBankCardView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import java.util.Map;

import rx.Observer;
import rx.Subscription;

/**
 * Author：Jenny
 * Date:2016/12/14 15:55
 * E-mail：fishloveqin@gmail.com
 * 我的钱包Presenter
 */

public class BindBankCardPresenter extends WrapPresenter<BindBankCardView> {
    private BindBankCardView mView;
    private Subscription mSubscription;
    private MyWalletService mService = null;

    @Override
    public void attachView(BindBankCardView view) {
        this.mView = view;
        mService = ServiceFactory.getMyWalletService();
    }

    public void getBankCardInfo(String token, String code) {

        mSubscription = RXUtil.execute(mService.getBankCardInfo(token, code),
                new Observer<ResponseMessage<BankCardModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<BankCardModel> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                            mView.loadCardInfo(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
                        }

                    }
                });
        mSubscriptions.add(mSubscription);
    }


    public void bindBankCard(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.bindBankCard(params),
                new Observer<ResponseMessage<Object>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<Object> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                            mView.bindCard(true);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
                            mView.bindCard(false);
                        }

                    }
                });
        mSubscriptions.add(mSubscription);
    }
}
