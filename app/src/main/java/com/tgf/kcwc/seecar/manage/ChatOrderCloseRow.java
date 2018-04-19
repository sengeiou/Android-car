package com.tgf.kcwc.seecar.manage;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.tgf.kcwc.R;

/**
 * Auther: Scott
 * Date: 2017/3/2 0002
 * E-mail:hekescott@qq.com
 */

public class ChatOrderCloseRow extends EaseChatRow {


    private View rootview;

    public ChatOrderCloseRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {

        rootview =  inflater.inflate(R.layout.chatitem_orderseem_wcomment,this);
    }

    @Override
    protected void onFindViewById() {
    }

    @Override
    protected void onUpdateView() {

    }

    @Override
    protected void onSetUpView() {
    }

    @Override
    protected void onBubbleClick() {

    }
}
