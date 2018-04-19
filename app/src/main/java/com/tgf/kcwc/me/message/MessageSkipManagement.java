package com.tgf.kcwc.me.message;

import android.content.Context;
import android.util.Log;

import com.tgf.kcwc.mvp.model.MessageSystemListBean;

/**
 * Created by Administrator on 2017/11/28.
 */

public class MessageSkipManagement {

    static Context mContext;

    /**
     * @param type   跳转的类型
     * @param module 跳转的版块
     */
    public static void skipModule(Context context, int type, String module) {
        mContext = context;
        switch (module) {
            case "system"://系统消息
                skipSystem(type);
                break;
/*            case ""://私信
                skipPrivate(type);
                break;*/
            case "thecar"://看车
                skipLookCar(type);
                break;
            case "coupon"://代金券
                skipVoucher(type);
                break;
            case "comment"://评论
                skipComment(type);
                break;
            case "fabulous"://点赞
                skipLike(type);
                break;
            case "activity"://活动
                skipActivity(type);
                break;
            case "ticket"://票证
                skipTickets(type);
                break;
            case "themoto"://我要买
                skipBuy(type);
                break;
            case "car_user_sale"://车主自售
                skipSell(type);
                break;
            case "fix_red"://固定红包分类
                skipClassify(type);
                break;
            case "activity_red":// 红包活动
                skipRedPacket(type);
                break;
            case "card"://名片消息
                skipBusinessCard(type);
                break;
        }
    }


    /**
     * 系统消息
     *
     * @param type
     */
    public static void skipSystem(int type) {
        switch (type) {
            case 1:
                break;

            default:
                break;
        }
    }

    /**
     * 私信
     *
     * @param type
     */
    public static void skipPrivate(int type) {

        switch (type) {
            case 1:
                break;

            default:
                break;
        }
    }

    /**
     * 看车
     *
     * @param type
     */
    public static void skipLookCar(int type) {

        switch (type) {
            case 1:
                break;

            default:
                break;
        }
    }

    /**
     * 代金券
     *
     * @param type
     */
    public static void skipVoucher(int type) {
        switch (type) {
            case 1: //代金券订单支付成功消息
                Log.i("skip", "代金券订单支付成功消息");
                break;
            case 2://代金券领取成功消息
                Log.i("skip", "代金券领取成功消息");
                break;
            case 3://代金券使用通知
                Log.i("skip", "代金券使用通知");
                break;
            case 4://代金券过期通知
                Log.i("skip", "代金券过期通知");
                break;
            case 5://获得专属代金券消息
                Log.i("skip", "获得专属代金券消息");
                break;
            case 6://代金券领取失效通知
                Log.i("skip", "代金券领取失效通知");
                break;
            case 7://代金券到期提醒(到期前3天/到期前1天)
                Log.i("skip", "代金券到期提醒(到期前3天/到期前1天)");
                break;
            case 8://代金券订单支付失败
                Log.i("skip", "代金券订单支付失败");
                break;

            default:
                break;
        }
    }

    /**
     * 评论
     *
     * @param type
     */
    public static void skipComment(int type) {

        switch (type) {
            case 1:
                break;

            default:
                break;
        }
    }

    /**
     * 点赞
     *
     * @param type
     */
    public static void skipLike(int type) {

        switch (type) {
            case 1:
                break;

            default:
                break;
        }
    }

    /**
     * 活动
     *
     * @param type
     */
    public static void skipActivity(int type) {

        switch (type) {
            case 1:
                break;

            default:
                break;
        }
    }

    /**
     * 门票/证
     *
     * @param type
     */
    public static void skipTickets(int type) {

        switch (type) {
            case 1:
                break;

            default:
                break;
        }
    }

    /**
     * 我要买
     *
     * @param type
     */
    public static void skipBuy(int type) {

        switch (type) {
            case 1:
                break;

            default:
                break;
        }
    }

    /**
     * 车主自售
     *
     * @param type
     */
    public static void skipSell(int type) {

        switch (type) {
            case 1: //审核不通过时的 	重新申请
                Log.i("skip", "审核不通过时的 重新申请");
                break;
            case 2://审核通过时的   	参展申请详情
                Log.i("skip", "审核通过时的   参展申请详情");
                break;
            case 3://打回修改时的   	修改申请
                Log.i("skip", "打回修改时的   修改申请");
                break;
            case 4://开展提醒时的	  	展会详情
                Log.i("skip", "开展提醒时的  展会详情");
                break;
            case 5://参展提醒时的   	参展申请详情
                Log.i("skip", "参展提醒时的   参展申请详情");
                break;
            case 6://车辆进场提醒时的 参展申请详情
                Log.i("skip", "车辆进场提醒时的 参展申请详情");
                break;
            case 7://超时预警时的		参展申请详情
                Log.i("skip", "超时预警时的 参展申请详情");
                break;
            case 8://延迟订单取消时间	参展申请详情
                Log.i("skip", "延迟订单取消时间 参展申请详情");
                break;
            case 9: //订单超时取消		参展申请详情
                Log.i("skip", "订单超时取消 参展申请详情");
                break;
            case 10://提前离场		参展申请详情
                Log.i("skip", "提前离场 参展申请详情");
                break;
            case 11://车辆离场提醒	参展申请详情
                Log.i("skip", "车辆离场提醒 参展申请详情");
                break;
            case 12://车辆离场提醒	参展申请详情
                Log.i("skip", "车辆离场提醒 参展申请详情");
                break;

            default:
                break;
        }
    }

    /**
     * 固定红包分类
     *
     * @param type
     */
    public static void skipClassify(int type) {

        switch (type) {
            case 1:
                break;

            default:
                break;
        }
    }

    /**
     * 红包活动
     *
     * @param type
     */
    public static void skipRedPacket(int type) {

        switch (type) {
            case 1:
                break;

            default:
                break;
        }
    }

    /**
     * 名片消息
     *
     * @param type
     */
    public static void skipBusinessCard(int type) {

        switch (type) {
            case 1:
                break;

            default:
                break;
        }
    }


}
