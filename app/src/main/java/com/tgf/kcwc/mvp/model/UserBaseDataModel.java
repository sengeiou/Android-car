package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author:Jenny
 * Date:2017/5/17
 * E-mail:fishloveqin@gmail.com
 * <p>
 * 用户基本资料信息
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserBaseDataModel {

    /**
     * id : 110
     * username : gongchuang
     * nickname : 落叶111
     * avatar : /project/1703/01/906f74df289b747be3ad25571d79d243.jpg
     * sex : 1
     * age : 26
     * sign_text : 从你的全世界路过。。。。
     * level : 0
     * type : 3
     * last_login_time : 2017-05-15 11:40:55
     * bj_image : /project/1704/24/90cf6ec3d04eabc9c02bd657d59fbdec.png
     * is_model : 1
     * is_doyen : 1
     * is_master : 1
     * is_vip : 1
     * fans_num : 1
     * follow_num : 0
     * logo : /brand/1701/20/44b5fc2bdabd9356006b7b6bd118fe4c.jpg
     */

    public int id;
    public String username;
    public String nickname;
    public String avatar;
    public int sex;
    public int age;
    @JsonProperty("sign_text")
    public String signText;
    public int level;
    public int type;
    @JsonProperty("last_login_time")
    public String lastLoginTime;
    @JsonProperty("bj_image")
    public String bgImg;
    @JsonProperty("is_model")
    public int isModel;
    @JsonProperty("is_doyen")
    public int isDoyen;
    @JsonProperty("is_master")
    public int isMaster;
    @JsonProperty("is_vip")
    public int isVIP;
    @JsonProperty("fans_num")
    public int fansNum;
    @JsonProperty("follow_num")
    public int followNum;
    @JsonProperty("im_id")
    public String imId;
    public String logo;
    @JsonProperty("org_id")
    public int orgId;
    @JsonProperty("is_login")
    public int isLogin;
    @JsonProperty("is_sale")
    public int isSale;
    public String tel;
    public String relation; //关系（'not_concern--未关注','mutual_concern--双向关注','myself--自己','already_concern--已关注'）
}
