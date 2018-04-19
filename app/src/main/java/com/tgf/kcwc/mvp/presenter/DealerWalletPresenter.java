package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.AccountBalanceModel;
import com.tgf.kcwc.mvp.model.AccountBillModel;
import com.tgf.kcwc.mvp.model.BalanceDetailModel;
import com.tgf.kcwc.mvp.model.BalanceStatementModel;
import com.tgf.kcwc.mvp.model.BankCardModel;
import com.tgf.kcwc.mvp.model.DealerWalletService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.DealerWalletView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;

import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Author：Jenny
 * Date:2016/12/14 15:55
 * E-mail：fishloveqin@gmail.com
 * 机构钱包Presenter
 */

public class DealerWalletPresenter extends WrapPresenter<DealerWalletView> {
    private DealerWalletView mView;
    private Subscription mSubscription;
    private DealerWalletService mService = null;

    @Override
    public void attachView(DealerWalletView view) {
        this.mView = view;
        mService = ServiceFactory.getDealerWalletService();
    }

    public void getBankCards(String token) {

        mSubscription = RXUtil.execute(mService.getBankCards(token),
                new Observer<ResponseMessage<List<BankCardModel>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<List<BankCardModel>> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                            mView.showData(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
                        }

                    }
                });
        mSubscriptions.add(mSubscription);
    }


    public void validateCheckCode(String token, String tel, String code) {

        mSubscription = RXUtil.execute(mService.validateCheckCode(token, tel, code),
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
                            mView.showData(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
                        }

                    }
                });
        mSubscriptions.add(mSubscription);
    }


    /**
     * 设置支付密码
     *
     * @param params
     */
    public void setPayPwd(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.setPayPwd(params),
                new Observer<ResponseMessage<Object>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<Object> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                            mView.showData(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
                        }

                    }
                }, new Action0() {
                    @Override
                    public void call() {

                        mView.setLoadingIndicator(true);
                    }
                });
        mSubscriptions.add(mSubscription);
    }


    public void getAccountBalances(String token) {

        mSubscription = RXUtil.execute(mService.getAccountBalances(token),
                new Observer<ResponseMessage<AccountBalanceModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<AccountBalanceModel> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                            mView.showData(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
                        }

                    }
                });
        mSubscriptions.add(mSubscription);
    }


    /**
     * 零钱明细清单
     *
     * @param params
     */
    public void getBalanceStatements(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.getTradeList(params),
                new Observer<ResponseMessage<BalanceStatementModel>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<BalanceStatementModel> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                            mView.showData(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
                        }

                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        mView.setLoadingIndicator(true);
                    }
                });
        mSubscriptions.add(mSubscription);
    }


    public void getBalanceDetail(String token, String id) {

        mSubscription = RXUtil.execute(mService.getBalanceDetail(token, id),
                new Observer<ResponseMessage<BalanceDetailModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<BalanceDetailModel> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                            mView.showData(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
                        }

                    }
                });
        mSubscriptions.add(mSubscription);
    }


    public void validatePayPwd(String token, String pwd) {

        mSubscription = RXUtil.execute(mService.validatePayPwd(token, pwd),
                new Observer<ResponseMessage<Object>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<Object> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                            mView.showData(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
                        }

                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        mView.setLoadingIndicator(true);
                    }
                });
        mSubscriptions.add(mSubscription);
    }


    public void validatePayPwd(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.validatePayPwd(params),
                new Observer<ResponseMessage<Object>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<Object> responseMessage) {

                        mView.showData(responseMessage);

                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        mView.setLoadingIndicator(true);
                    }
                });
        mSubscriptions.add(mSubscription);
    }


    /**
     * 设置银行卡选中
     *
     * @param params
     */
    public void setBankCardSelected(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.setBankCardSelected(params),
                new Observer<ResponseMessage<Object>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<Object> responseMessage) {

                        mView.showData(responseMessage);

                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        mView.setLoadingIndicator(true);
                    }
                });
        mSubscriptions.add(mSubscription);
    }

    public void getSelectedBankCard(String token) {

        mSubscription = RXUtil.execute(mService.getSelectedBankCard(token),
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
                            mView.showData(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
                        }

                    }
                });
        mSubscriptions.add(mSubscription);
    }


    /**
     * 申请提现
     *
     * @param params
     */
    public void applyWithdraw(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.applyWithdraw(params),
                new Observer<ResponseMessage<Object>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<Object> responseMessage) {

                        mView.showData(responseMessage);

                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        mView.setLoadingIndicator(true);
                    }
                });
        mSubscriptions.add(mSubscription);
    }


    /**
     * 充值
     *
     * @param params
     */
    public void prePaidToCard(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.setPrePaid(params),
                new Observer<ResponseMessage<Object>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<Object> responseMessage) {

                        mView.showData(responseMessage);

                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        mView.setLoadingIndicator(true);
                    }
                });
        mSubscriptions.add(mSubscription);
    }


    public void getAccountBill(String token, String date) {

        mSubscription = RXUtil.execute(mService.getAccountBill(token, date),
                new Observer<ResponseMessage<AccountBillModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<AccountBillModel> responseMessage) {

                        if (responseMessage.statusCode == Constants.NetworkStatusCode.SUCCESS) {
                            mView.showData(responseMessage.data);
                        } else {
                            CommonUtils.showToast(mView.getContext(), responseMessage.statusMessage);
                        }

                    }
                });
        mSubscriptions.add(mSubscription);
    }


    /**
     * 设置免密支付
     *
     * @param params
     */
    public void setNonPwdPayment(Map<String, String> params) {

        mSubscription = RXUtil.execute(mService.setNonPwdPayment(params),
                new Observer<ResponseMessage<Object>>() {
                    @Override
                    public void onCompleted() {

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<Object> responseMessage) {

                        mView.showData(responseMessage);

                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        mView.setLoadingIndicator(true);
                    }
                });
        mSubscriptions.add(mSubscription);
    }
}
