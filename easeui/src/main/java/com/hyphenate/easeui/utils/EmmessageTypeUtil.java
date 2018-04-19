package com.hyphenate.easeui.utils;

import android.text.TextUtils;

import com.hyphenate.chat.EMMessage;

import org.json.JSONObject;

/**
 * Auther: Scott
 * Date: 2017/11/15 0015
 * E-mail:hekescott@qq.com
 */


public class EmmessageTypeUtil {
    /**
     * 判断是不是tip类型的
     */
    public static int isEmmessageTypeTip(EMMessage message){
        try {
            String modelStr = message.getStringAttribute("model");
            if (!TextUtils.isEmpty(modelStr)) {
                if (modelStr.equals("buycar")) {
                    JSONObject jsonData = message.getJSONObjectAttribute("data");
                    if (jsonData.has("type")) {
                        switch (jsonData.getString("type")) {
                            case "tip":
                                return 1;
                            case "complete":
                                return 2;
                            case "comment":
                                return 3;
                            case "noDisturb":
                                return 4;
//                            case "noDisturb":
//                                return MESSAGE_TYPE_CLOSEORDER;
//                            case "complete":
//
//                                break;
//                            default:
//                                break;
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }
}
