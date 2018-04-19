package com.tgf.kcwc.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Auther: Scott
 * Date: 2017/7/17 0017
 * E-mail:hekescott@qq.com
 */

@SuppressLint("AppCompatCustomView")
public class EmojiTextView extends TextView {
    public EmojiTextView(Context context) {
        super(context);
    }

    public EmojiTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EmojiTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void setEmojiToTextView(String textStr){

        setText(Html.fromHtml(textStr));
    }


}
