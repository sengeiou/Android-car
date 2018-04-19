package com.tgf.kcwc.iseechat;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.exceptions.HyphenateException;
import com.tgf.kcwc.R;

import org.json.JSONException;

/**
 * Auther: Scott
 * Date: 2017/3/2 0002
 * E-mail:hekescott@qq.com
 */

public class ChatTipRow extends EaseChatRow {


    private TextView tipTv;
    private View rootview;
    public ChatTipRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        rootview =  inflater.inflate(R.layout.listitem_tip,this);
    }

    @Override
    protected void onFindViewById() {
        tipTv = (TextView) rootview.findViewById(R.id.chat_tiptv);

    }

    @Override
    protected void onUpdateView() {

    }

    @Override
    protected void onSetUpView() {
            try {
                tipTv.setText(message.getJSONObjectAttribute("data").getString("content"));
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    @Override
    protected void onBubbleClick() {

    }
}
