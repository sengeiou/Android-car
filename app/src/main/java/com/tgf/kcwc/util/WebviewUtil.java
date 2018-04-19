package com.tgf.kcwc.util;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Auther: Scott
 * Date: 2017/7/13 0013
 * E-mail:hekescott@qq.com
 */

public class WebviewUtil {
    public static String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style type=\"text/css\">p{line-height: 24px;background-color: #fff;}img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<!DOCTYPE html>" + head + "<body>" + bodyHTML + "</body></html>";
    }
    private void loadWebView() {

        Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
//                String string = URLUtil.getHtml("https://m.51kcwc.com/#/app/eval/show/" + mId);
//                subscriber.onNext(string);
            }
        })
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
//                        webView.loadData(s, "text/html; charset=utf-8", "utf-8");
                    }
                });

    }
}
