package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.EvaluationModel;
import com.tgf.kcwc.mvp.model.MotoBuyService;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.MotoEvaluationView;
import com.tgf.kcwc.util.RXUtil;

import rx.Observer;
import rx.functions.Action0;

/**
 * Auther: Scott
 * Date: 2017/2/27 0027
 * E-mail:hekescott@qq.com
 */

public class BuyMotoEvaluationPresenter extends WrapPresenter<MotoEvaluationView> {
    private MotoEvaluationView mView;
    private MotoBuyService mService;
    @Override
    public void attachView(MotoEvaluationView view) {
        mView = view;
        mService = ServiceFactory.getMotoBuyService();
    }

    /**
     *
     * @param token
     * @param id  订单id
     * @param offer_id 	报价单ID
     * @param star
     * @param tag
     * @param text 说明文字
     */
    public void  postEvaluation(String token,  int id, int offer_id, int star, String tag, String text,int isAnsyc){
        RXUtil.execute(mService.postEvaluation(token, id, offer_id, star, tag, text,isAnsyc), new Observer<ResponseMessage>() {
            @Override
            public void onCompleted() {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onError(Throwable e) {
                mView.setLoadingIndicator(false);
            }

            @Override
            public void onNext(ResponseMessage responseMessage) {
                if (responseMessage.statusCode == 0) {
                    mView.showCommitSuccess();
                } else {
                    mView.showLoadingTasksError();
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                mView.setLoadingIndicator(true);
            }
        });
    }

    public void getTags(String token,  int id, int offer_id){
        RXUtil.execute(mService.getTagList(token, id, offer_id), new Observer<ResponseMessage<EvaluationModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<EvaluationModel> evaluationModelResponseMessage) {
                    mView.showTags(evaluationModelResponseMessage.getData().tags);
            }
        });
    }

}
