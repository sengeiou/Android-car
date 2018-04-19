package com.tgf.kcwc.ticket.manage;

import android.content.Context;
import android.view.View;

import com.tgf.kcwc.R;
import com.tgf.kcwc.view.BottomPushPopupWindow;


/**
 * Auther: Scott
 * Date: 2017/2/7 0007
 * E-mail:hekescott@qq.com
 */

public class TicketSendWindow extends BottomPushPopupWindow<Void> implements View.OnClickListener {
    public TicketSendWindow(Context context) {
        super(context, null);
    }

    @Override
    protected View generateCustomView(Void aVoid) {
        View root = View.inflate(context, R.layout.bottomticket_pop_window, null);
        root.findViewById(R.id.ticket_contacts).setOnClickListener(this);
        root.findViewById(R.id.ticket_user).setOnClickListener(this);
        root.findViewById(R.id.ticket_input).setOnClickListener(this);
        root.findViewById(R.id.ticket_cancel).setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.ticket_contacts:
//                dismiss();
//                break;
//            case R.id.ticket_user:
//                dismiss();
//                break;
//            case R.id.ticket_input:
//
//                break;
//            default:
//                break;
//        }
        dismiss();
        if (mListener != null) {
            mListener.onClick(v);
        }
    }
    /**
     * 自定义点击事件回调
     */
    public interface OnClickListener {
        void onClick(View v);
    }
    private OnClickListener mListener;

    public void setOnClickListener(OnClickListener listener) {
        this.mListener = listener;
    }
}
