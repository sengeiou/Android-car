package com.tgf.kcwc.seecar.manage;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.tgf.kcwc.R;
import com.tgf.kcwc.view.FlowLayout;

/**
 * Auther: Scott
 * Date: 2017/3/2 0002
 * E-mail:hekescott@qq.com
 */

public class ChatOrderCommentRow extends EaseChatRow {


    private TextView tipTv;
    private View rootview;
    private FlowLayout flowLayout;
    private String[] tag =  new String[]{"全部","这是","测试标签",
            "这是测试标签","FlowLayout","衣服","鞋子",
            "春","夏","深秋","寒冬",
            "测一下看看效果如何","心情还不错哦","这是测试标签","这是测试标签",
            "这是测试标签","受益匪浅啊","123456789","电话号码"};
    public ChatOrderCommentRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {

        rootview =  inflater.inflate(R.layout.chatitem_mordercomment,this);
    }

    @Override
    protected void onFindViewById() {
        flowLayout = (FlowLayout) rootview.findViewById(R.id.ordercomment_fl);
    }

    @Override
    protected void onUpdateView() {
        int padding =flowLayout.dip2px(5);
        flowLayout.setPadding(padding, padding, padding, padding);// 设置内边距
        for (int i = 0; i < tag.length; i++) {
            final String tep = tag[i];
            TextView tv = new TextView(getContext());
            tv.setText(tep);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tv.setPadding(padding, padding, padding, padding);
            tv.setGravity(Gravity.CENTER);
        }
    }

    @Override
    protected void onSetUpView() {
    }

    @Override
    protected void onBubbleClick() {

    }
}
