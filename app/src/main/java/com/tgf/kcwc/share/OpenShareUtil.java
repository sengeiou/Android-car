package com.tgf.kcwc.share;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tgf.kcwc.R;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.util.BitmapUtils;

import java.util.ArrayList;

/**
 * Author:Jenny
 * Date:2017/16
 * E-mail:fishloveqin@gmail.com
 * 第三方分享平台
 */

public class OpenShareUtil {

    /**
     *分享到QQ
     * @param t
     * @param activity
     * @param listener
     * @param args
     */
    public static final void shareToQQ(Tencent t, Activity activity, IUiListener listener,
                                       String... args) {

        Bundle bundle = new Bundle();
        //这条分享消息被好友点击后的跳转URL。
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, Constants.H5.WAP_LINK);
        //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_	SUMMARY不能全为空，最少必须有一个是有值的。
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, args[0]);
        //分享的图片URL
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, args[1]);
        //分享的消息摘要，最长50个字
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, args[2]);

        //手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, "看车玩车");
        //标识该消息的来源应用，值为应用名称+AppId。

        t.shareToQQ(activity, bundle, listener);
    }

    public static final void shareToQQ2(Tencent t, Activity activity, IUiListener listener,
                                       String... args) {

        Bundle bundle = new Bundle();

        //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_	SUMMARY不能全为空，最少必须有一个是有值的。
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, args[0]);
        //分享的图片URL
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, args[1]);
        //分享的消息摘要，最长50个字
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, args[2]);

        //手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, "看车玩车");
        //这条分享消息被好友点击后的跳转URL。
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, args[3]);
        //标识该消息的来源应用，值为应用名称+AppId。

        t.shareToQQ(activity, bundle, listener);
    }

    /**
     * 分享到微信
     * @param api
     */
    public static void sendWX(Context context, IWXAPI api, String... args) {

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = Constants.H5.WAP_LINK;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = args[0];
        msg.description = args[1];
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.app_logo);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 96, 96, true);
        msg.thumbData = BitmapUtils.bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
        bmp.recycle();

    }
    /**
     * 分享到微信
     * @param api
     */
    public static void sendWX2(Context context, IWXAPI api, String... args) {

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = args[2];
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = args[0];
        msg.description = args[1];
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.app_logo);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 96, 96, true);
        msg.thumbData = BitmapUtils.bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
        bmp.recycle();

    }

    /**
     * 分享到微信朋友圈
     * @param api
     */
    public static void sendWXMoments(Context context, IWXAPI api, String... args) {

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = Constants.H5.WAP_LINK;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = args[0];
        msg.description = args[1];
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.app_logo);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 96, 96, true);
        msg.thumbData = BitmapUtils.bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
        bmp.recycle();
    }

    /**
     * 分享到微信朋友圈
     * @param api
     */
    public static void sendWXMoments2(Context context, IWXAPI api, String... args) {

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = args[2];
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = args[0];
        msg.description = args[1];
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.app_logo);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 96, 96, true);
        msg.thumbData = BitmapUtils.bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
        bmp.recycle();
    }

    public static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
            : type + System.currentTimeMillis();
    }

    /**
     * 分享到新浪
     * @param activity
     * @return
     */
    public static WbShareHandler shareSina(Activity activity, String... args) {

        WbShareHandler shareHandler = new WbShareHandler(activity);
        shareHandler.registerApp();

        // sendMultiMessage(activity, shareHandler, true, false);
        sendWebpageMessage(activity, shareHandler, args);
        return shareHandler;
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     */
    private static void sendMultiMessage(Context context, WbShareHandler shareHandler,
                                         boolean hasText, boolean hasImage, String... args) {

        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        if (hasText) {
            weiboMessage.textObject = getTextObj();
        }
        if (hasImage) {
            weiboMessage.imageObject = getImageObj(context);
        }
        weiboMessage.mediaObject = getWebpageObj(context, args);
        shareHandler.shareMessage(weiboMessage, false);

    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     */
    private static void sendWebpageMessage(Context context, WbShareHandler shareHandler,
                                           String... args) {

        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        if (args.length<3){ //第三个是url
            weiboMessage.mediaObject = getWebpageObj(context, args);
        }else {
            weiboMessage.mediaObject = getWebpageObj2(context, args);
        }
        shareHandler.shareMessage(weiboMessage, false);

    }



    /**
     * 创建文本消息对象。
     * @return 文本消息对象。
     */
    private static TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = "20岁富翁拥有两座城堡 抛弃伴侣想找年轻女子生育继承人";
        textObject.title = "xxxx";
        textObject.actionUrl = "http://www.baidu.com";
        return textObject;
    }

    /**
     * 创建图片消息对象。
     * @return 图片消息对象。
     */
    private static ImageObject getImageObj(Context context) {
        ImageObject imageObject = new ImageObject();
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.btn_my_cmt);
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private static WebpageObject getWebpageObj(Context context, String... args) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = args[0];
        mediaObject.description = args[1];
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.app_logo);
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = Constants.H5.WAP_LINK;
        mediaObject.defaultText = args[1];
        return mediaObject;
    }
    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private static WebpageObject getWebpageObj2(Context context, String... args) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = args[0];
        mediaObject.description = args[1];
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.app_logo);
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl =  args[2];
        mediaObject.defaultText = args[1];
        return mediaObject;
    }

    /**
     * 分享到QQ空间
     * @param t
     * @param activity
     * @param listener
     * @param args
     */
    public static void shareToQzone(Tencent t, Activity activity, IUiListener listener,
                                    ArrayList<String> urls, String... args) {

        Bundle bundle = new Bundle();
        bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
            QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, args[0]);//必填
        bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, args[1]);//选填
        bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, Constants.H5.WAP_LINK);//必填
        bundle.putString(QzoneShare.SHARE_TO_QQ_APP_NAME, "看车玩车");
        if (urls != null && urls.size() != 0) {
            bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, urls);
        }

        t.shareToQzone(activity, bundle, listener);

    }

    /**
     * 分享到QQ空间
     * @param t
     * @param activity
     * @param listener
     * @param args
     */
    public static void shareToQzone2(Tencent t, Activity activity, IUiListener listener,
                                    ArrayList<String> urls, String... args) {

        Bundle bundle = new Bundle();
        bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
                QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, args[0]);//必填
        bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, args[1]);//选填
        bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, args[2]);//必填
        bundle.putString(QzoneShare.SHARE_TO_QQ_APP_NAME, "看车玩车");
        if (urls != null && urls.size() != 0) {
            bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, urls);
        }

        t.shareToQzone(activity, bundle, listener);

    }
}
