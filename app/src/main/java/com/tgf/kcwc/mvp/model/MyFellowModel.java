package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by heke on 17/8/20.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyFellowModel {


    /**
     * follow_id : 300
     * follow_time : 2017-08-20 18:04:30
     * remark :
     * tel : 15223329438
     * register_area : 重庆
     * nickname : 重庆展会o0M0I7
     * avatar : /thread/1705/20/ab44a2f9d8d3ec026909f8b7c180d484.jpeg
     * sex : 1
     * sign_text :
     * is_model : 1
     * is_doyen : 1
     * is_master : 0
     * logo :
     * relation : already_concern
     */

    public int follow_id;
    public String follow_time;
    public String remark;
    public String tel;
    public String register_area;
    public String nickname;
    public String avatar;
    public int sex;
    public String sign_text;
    public int is_model;
    public int is_doyen;
    public int is_master;
    public String logo;
    public String relation;
}
