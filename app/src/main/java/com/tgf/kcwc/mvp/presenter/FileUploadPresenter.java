package com.tgf.kcwc.mvp.presenter;

import android.view.View;

import com.lzy.imagepicker.bean.ImageItem;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.entity.PathItem;
import com.tgf.kcwc.mvp.ServiceFactory;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.UploadAvatarService;
import com.tgf.kcwc.mvp.view.FileUploadView;
import com.tgf.kcwc.util.RXUtil;

import okhttp3.RequestBody;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Author：Jenny
 * Date:2016/12/8 20:55
 * E-mail：fishloveqin@gmail.com
 */

public class FileUploadPresenter extends WrapPresenter<FileUploadView> {
    private FileUploadView mView;
    private Subscription mSubscription;
    private UploadAvatarService mService = null;
    private int mUploadCount;
    private int mUploadingCount;

    @Override
    public void attachView(FileUploadView view) {
        this.mView = view;
        mService = ServiceFactory.getUploadService();
    }

    /**
     * 上传文件
     */
    public void uploadFile(RequestBody rb) {

        mSubscription = RXUtil.execute(mService.uploadFile(rb), new Observer<ResponseMessage>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseMessage listResponseMessage) {

                mView.resultData(listResponseMessage);
            }
        });

        mSubscriptions.add(mSubscription);
    }

    /**
     * 上传文件
     */
    public synchronized void uploadFile(RequestBody rb, RequestBody module, RequestBody itemId,
                                        RequestBody token, final View v) {
        mUploadCount++;

        mSubscription = RXUtil.execute(mService.uploadImage(rb, module, itemId, token),
                new Observer<ResponseMessage<PathItem>>() {
                    @Override
                    public void onCompleted() {

                        if (mUploadCount == mUploadingCount)
                            mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<PathItem> resMsg) {
                        mUploadingCount++;

                        DataItem item = new DataItem();
                        item.resp = resMsg;
                        item.code = resMsg.statusCode;
                        item.msg = resMsg.statusMessage;
                        item.v = v;
                        mView.resultData(item);

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
     * 上传文件
     */
    public void uploadFile(RequestBody rb, RequestBody module, RequestBody itemId,
                           RequestBody token, final ImageItem imgItem) {

        mSubscription = RXUtil.execute(mService.uploadImage(rb, module, itemId, token),
                new Observer<ResponseMessage<PathItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<PathItem> resMsg) {

                        DataItem item = new DataItem();
                        item.resp = resMsg;
                        item.code = resMsg.statusCode;
                        item.msg = resMsg.statusMessage;
                        imgItem.url = resMsg.data.path;
                        System.out.println("imgItem.url:" + imgItem.url);
                        mView.resultData(item);
                        mView.setLoadingIndicator(false);
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
     * 上传文件
     */
    public void uploadFile(RequestBody rb, RequestBody module, RequestBody itemId,
                           RequestBody token, final String path) {

        mSubscription = RXUtil.execute(mService.uploadImage(rb, module, itemId, token),
                new Observer<ResponseMessage<PathItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<PathItem> resMsg) {

                        DataItem item = new DataItem();
                        item.resp = resMsg;
                        item.code = resMsg.statusCode;
                        item.msg = resMsg.statusMessage;
                        item.url = resMsg.data.path;
                        item.path=path;
                        mView.resultData(item);
                        mView.setLoadingIndicator(false);
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
     * 上传文件
     */
    public void uploadAvatarFile(RequestBody rb, RequestBody module, RequestBody itemId,
                                 RequestBody token) {

        mSubscription = RXUtil.execute(mService.uploadImage(rb, module, itemId, token),
                new Observer<ResponseMessage<PathItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        mView.showLoadingTasksError();
                    }

                    @Override
                    public void onNext(ResponseMessage<PathItem> resMsg) {

                        DataItem item = new DataItem();
                        item.resp = resMsg;
                        item.code = resMsg.statusCode;
                        item.msg = resMsg.statusMessage;
                        String url = resMsg.data.path;
                        item.content = url;
                        mView.resultData(item);
                        mView.setLoadingIndicator(false);
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
