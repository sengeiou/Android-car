package com.tgf.kcwc.mvp.presenter;

import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ExhibitionService;
import com.tgf.kcwc.mvp.model.PopmanEsDetailModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.view.PopmanEsDetailView;
import com.tgf.kcwc.util.RXUtil;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Observer;

/**
 * Auther: Scott
 * Date: 2017/7/13 0013
 * E-mail:hekescott@qq.com
 */

public class PopmanEsDetailPresenter extends WrapPresenter<PopmanEsDetailView> {

    private ExhibitionService mService;
    private PopmanEsDetailView mView;

    @Override
    public void attachView(PopmanEsDetailView view) {
        mView = view;
        mService = ServiceFactory.getExhibitionService();
    }

    public void getPopmanEsDetail(String threadId) {
        RXUtil.execute(mService.getPopmanEsDetail(threadId), new Observer<ResponseMessage<PopmanEsDetailModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage<PopmanEsDetailModel> responseMessage) {
                PopmanEsDetailModel popMandetail = responseMessage.getData();
                mView.showPopmanEsContent(popMandetail.evaluate.content);
                mView.showTags(popMandetail.topiclist);
                mView.showCreateUser(popMandetail.createUser);
                mView.showHead(popMandetail);
            }
        });
    }
}
