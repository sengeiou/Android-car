package com.tgf.kcwc.mvp.model;

/**
 * Author:Jenny
 * Date:2017/7/19
 * E-mail:fishloveqin@gmail.com
 * 对话框实体类
 */

public class AlertDialogEntity {

    public String title;

    public String msg;

    public String btnText1;

    public String btnText2;

    public AlertDialogEntity(String title, String msg, String btnText1, String btnText2) {
        this.title = title;
        this.msg = msg;
        this.btnText1 = btnText1;
        this.btnText2 = btnText2;
    }

    public AlertDialogEntity() {
    }

    @Override
    public String toString() {
        return "AlertDialogEntity{" + "type='" + title + '\'' + ", msg='" + msg + '\''
               + ", btnText1='" + btnText1 + '\'' + ", btnText2='" + btnText2 + '\'' + '}';
    }
}
