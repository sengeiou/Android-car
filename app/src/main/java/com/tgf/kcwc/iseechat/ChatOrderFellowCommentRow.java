package com.tgf.kcwc.iseechat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedgehog.ratingbar.RatingBar;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.tgf.kcwc.R;
import com.tgf.kcwc.view.FlowLayout;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/3/2 0002
 * E-mail:hekescott@qq.com
 */

public class ChatOrderFellowCommentRow extends EaseChatRow {

    private View       rootview;
    private FlowLayout mCommentTag;
    private RatingBar ratingStar;
    private TextView commnetTv;
    private TextView starDesc;

    public ChatOrderFellowCommentRow(Context context, EMMessage message, int position,
                                     BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {

        rootview = inflater.inflate(R.layout.chatitem_ordercomment, this);
    }

    @Override
    protected void onFindViewById() {
        mCommentTag = (FlowLayout) rootview.findViewById(R.id.chatorder_comment_tag);
        starDesc = (TextView) rootview.findViewById(R.id.star_desctv);
        ratingStar = (RatingBar) rootview.findViewById(R.id.chatorder_comment_rating);
        commnetTv = (TextView) rootview.findViewById(R.id.chatorder_comment);
        mCommentTag.setHorizontalSpacing(10);
        mCommentTag.setVerticalSpacing(10);
    }

    @Override
    protected void onUpdateView() {

    }

    @Override
    protected void onSetUpView() {

//        if(!TextUtils.isEmpty(json)){
            ObjectMapper mapper = new ObjectMapper();
            try {
                String json = message.getJSONObjectAttribute("data").getString("content");
                Comment comment = mapper.readValue(json, Comment.class);
                ratingStar.setStar(comment.star);
                starDesc.setText(comment.star_describe);
                commnetTv.setText(comment.content);
                mCommentTag.removeAllViews();
               Resources res =   getContext().getResources();
                for(String tag:comment.tag){
                    TextView textView = new TextView(getContext());
                    textView.setTextColor(res.getColor(R.color.voucher_gray));
                    textView.setText(tag);
                    textView.setGravity(Gravity.CENTER);
                    textView.setPadding(10,4,4,4);
                    textView.setBackground(getContext().getResources().getDrawable(R.drawable.shapeborder_radiusgrayrect_bg));
                    mCommentTag.addView(textView);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//        }


    }

    @Override
    protected void onBubbleClick() {
    }

    private static class Comment {
        public int               star;
        public ArrayList<String> tag;
        public String            content;
        public String            star_describe;
    }
}
