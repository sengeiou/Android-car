package com.tgf.kcwc.util;

import android.content.Context;

/**
 * @anthor noti
 * @time 2017/9/13
 * @describle
 */
public class CarNumUtils {
    /**
     * 判断是否是车牌号
     */
    public static boolean isCarNo(Context context ,String CarNum) {
        if (!(CarNum == null || CarNum.equals(""))) {
            if (CarNum.length() == 6){
                //不包含I O i o的判断
                if (CarNum.contains("I") || CarNum.contains("i") || CarNum.contains("O") || CarNum.contains("o")) {
                    CommonUtils.showToast(context, "车牌号输入有误");
                    return false;
                } else {
                    if (!CarNum.matches("^[A-Z_a-z]{1}[A-Z_a-z_0-9]{5}$")) {
                        CommonUtils.showToast(context, "车牌号输入有误");
                        return false;
                    }else {
                        return true;
                    }
                }
            }else {
                CommonUtils.showToast(context, "请输入6位车牌号");
                return false;
            }
        } else {
            CommonUtils.showToast(context, "车牌号不能为空");
            return false;
        }
    }
}
