package com.tgf.kcwc.iseechat;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.tgf.kcwc.R;
import com.tgf.kcwc.view.FlowLayout;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Auther: Scott
 * Date: 2017/3/2 0002
 * E-mail:hekescott@qq.com
 */

public class ChatOrderCloseRow extends EaseChatRow {


    private TextView tipTv;
    private View rootview;
    private FlowLayout mCommentTag;

    public ChatOrderCloseRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {

        rootview =  inflater.inflate(R.layout.chatitem_orderclose,this);
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
