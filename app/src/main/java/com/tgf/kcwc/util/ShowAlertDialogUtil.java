package com.tgf.kcwc.util;

import android.content.Context;

import com.tgf.kcwc.mvp.model.AlertDialogEntity;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Author:Jenny
 * Date:2017/7/21
 * E-mail:fishloveqin@gmail.com
 */

public class ShowAlertDialogUtil {

    public static SweetAlertDialog showRideOperatorDialog(Context context, AlertDialogEntity entity,
                                                          SweetAlertDialog.OnSweetClickListener listener1,
                                                          SweetAlertDialog.OnSweetClickListener listener2) {

        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
        dialog.setTitleText(entity.title);
        dialog.setContentText(entity.msg);
        dialog.setCancelText(entity.btnText1);
        dialog.setConfirmText(entity.btnText2);
        dialog.setCancelClickListener(listener1);
        dialog.setConfirmClickListener(listener2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        return dialog;
    }


    public static SweetAlertDialog showRideOperatorDialog(Context context, AlertDialogEntity entity,
                                                          SweetAlertDialog.OnSweetClickListener listener
    ) {

        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
        dialog.setTitleText(entity.title);
        dialog.setContentText(entity.msg);
        dialog.setConfirmText(entity.btnText2);
        dialog.setConfirmClickListener(listener);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        return dialog;
    }
}
