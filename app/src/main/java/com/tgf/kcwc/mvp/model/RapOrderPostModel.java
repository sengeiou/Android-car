package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Auther: Scott
 * Date: 2017/11/8 0008
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class RapOrderPostModel {

    public int offer_id;
    public String im_user_id;

//      "data": {
//        "offer_id": 3629,
//                "im_user_id": "im_development_1244_kcwc",
//                "messages": [
//        {
//            "create_time": "2017-11-08 16:40:28",
//                "content": "抢单成功后，现在可以进行聊天了！",
//                "ext": {
//            "type": "buycar",
//                    "data": {
//                "id": "buycar_806_"
//            }
//        }
//        },
//        {
//            "create_time": "2017-11-08 16:40:28",
//                "content": "",
//                "ext": {
//            "type": "buycar",
//                    "data": {
//                "id": "buycar_806_",
//                        "tip": "如果您不想被Ta打扰，可“免TA打扰”关闭对话！"
//            }
//        }
//        }
//    ]
//    }
}
