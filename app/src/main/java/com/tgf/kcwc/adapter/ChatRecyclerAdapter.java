package com.tgf.kcwc.adapter;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.mvp.model.ChatMessageBean;
import com.tgf.kcwc.view.GifTextView;

/**
 * Created by Mao Jiqing on 2016/9/29.
 */
public class ChatRecyclerAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<ChatMessageBean> userList = new ArrayList<ChatMessageBean>();
    private ArrayList<String> imageList = new ArrayList<String>();
    private HashMap<Integer, Integer> imagePosition = new HashMap<Integer, Integer>();
    public static final int FROM_USER_MSG = 0;//接收消息类型
    public static final int TO_USER_MSG = 1;//发送消息类型
    public static final int FROM_USER_IMG = 2;//接收消息类型
    public static final int TO_USER_IMG = 3;//发送消息类型
    public static final int FROM_USER_VOICE = 4;//接收消息类型
    public static final int TO_USER_VOICE = 5;//发送消息类型
    private int mMinItemWith;// 设置对话框的最大宽度和最小宽度
    private int mMaxItemWith;
    public Handler handler;
    private SendErrorListener sendErrorListener;
    public List<String> unReadPosition = new ArrayList<String>();
    private int voicePlayPosition = -1;
    private LayoutInflater mLayoutInflater;
    private boolean isGif = true;
    public boolean isPicRefresh = true;

    public interface SendErrorListener {
        public void onClick(int position);
    }

    public void setSendErrorListener(SendErrorListener sendErrorListener) {
        this.sendErrorListener = sendErrorListener;
    }





    public ChatRecyclerAdapter(Context context, List<ChatMessageBean> userList) {
        this.context = context;
        this.userList = userList;
        mLayoutInflater = LayoutInflater.from(context);
        // 获取系统宽度
        WindowManager wManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wManager.getDefaultDisplay().getMetrics(outMetrics);
        mMaxItemWith = (int) (outMetrics.widthPixels * 0.5f);
        mMinItemWith = (int) (outMetrics.widthPixels * 0.15f);
        handler = new Handler();
    }

    public void setIsGif(boolean isGif) {
        this.isGif = isGif;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

    public void setImagePosition(HashMap<Integer, Integer> imagePosition) {
        this.imagePosition = imagePosition;
    }

    /**
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case FROM_USER_MSG:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_msgfrom_list_item, parent, false);
                holder = new FromUserMsgViewHolder(view);
                break;
            case TO_USER_MSG:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_msgto_list_item, parent, false);
                holder = new ToUserMsgViewHolder(view);
                break;
        }
        return holder;
    }

    class FromUserMsgViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView headicon;
        private TextView chat_time;
        private GifTextView content;

        public FromUserMsgViewHolder(View view) {
            super(view);
            headicon = (SimpleDraweeView) view
                    .findViewById(R.id.tb_other_user_icon);
            chat_time = (TextView) view.findViewById(R.id.chat_time);
            content = (GifTextView) view.findViewById(R.id.content);
        }
    }





    class ToUserMsgViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView headicon;
        private TextView chat_time;
        private GifTextView content;
        private ImageView sendFailImg;

        public ToUserMsgViewHolder(View view) {
            super(view);
            headicon = (SimpleDraweeView) view
                    .findViewById(R.id.tb_my_user_icon);
            chat_time = (TextView) view
                    .findViewById(R.id.mychat_time);
            content = (GifTextView) view
                    .findViewById(R.id.mycontent);
        }
    }





    /**
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatMessageBean tbub = userList.get(position);
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case FROM_USER_MSG:
                fromMsgUserLayout((FromUserMsgViewHolder) holder, tbub, position);
                break;


            case TO_USER_MSG:
                toMsgUserLayout((ToUserMsgViewHolder) holder, tbub, position);
                break;


        }
    }

    @Override
    public int getItemViewType(int position) {
        return userList.get(position).type;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    private void fromMsgUserLayout(final FromUserMsgViewHolder holder, final ChatMessageBean tbub, final int position) {
        /* time */
        if (position != 0) {
            String showTime = getTime(tbub.createTime, userList.get(position - 1)
                    .createTime);
            if (showTime != null) {
                holder.chat_time.setVisibility(View.VISIBLE);
                holder.chat_time.setText(showTime);
            } else {
                holder.chat_time.setVisibility(View.GONE);
            }
        } else {
            String showTime = getTime(tbub.createTime, null);
            holder.chat_time.setVisibility(View.VISIBLE);
            holder.chat_time.setText(showTime);
        }
        holder.content.setVisibility(View.VISIBLE);
        holder.content.setSpanText(handler, tbub.content, isGif);
    }





    private void toMsgUserLayout(final ToUserMsgViewHolder holder, final ChatMessageBean tbub, final int position) {
        /* time */
        if (position != 0) {
            String showTime = getTime(tbub.createTime, userList.get(position - 1)
                    .createTime);
            if (showTime != null) {
                holder.chat_time.setVisibility(View.VISIBLE);
                holder.chat_time.setText(showTime);
            } else {
                holder.chat_time.setVisibility(View.GONE);
            }
        } else {
            String showTime = getTime(tbub.createTime, null);
            holder.chat_time.setVisibility(View.VISIBLE);
            holder.chat_time.setText(showTime);
        }

        holder.content.setVisibility(View.VISIBLE);
        holder.content.setSpanText(handler, tbub.content, isGif);
    }




    @SuppressLint("SimpleDateFormat")
    public String getTime(String time, String before) {
        String show_time = null;
        if (before != null) {
            try {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date now = df.parse(time);
                java.util.Date date = df.parse(before);
                long l = now.getTime() - date.getTime();
                long day = l / (24 * 60 * 60 * 1000);
                long hour = (l / (60 * 60 * 1000) - day * 24);
                long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
                if (min >= 1) {
                    show_time = time.substring(11);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            show_time = time.substring(11);
        }
        String getDay = getDay(time);
        if (show_time != null && getDay != null)
            show_time = getDay + " " + show_time;
        return show_time;
    }

    @SuppressLint("SimpleDateFormat")
    public static String returnTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }

    @SuppressLint("SimpleDateFormat")
    public String getDay(String time) {
        String showDay = null;
        String nowTime = returnTime();
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date now = df.parse(nowTime);
            java.util.Date date = df.parse(time);
            long l = now.getTime() - date.getTime();
            long day = l / (24 * 60 * 60 * 1000);
            if (day >= 365) {
                showDay = time.substring(0, 10);
            } else if (day >= 1 && day < 365) {
                showDay = time.substring(5, 10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return showDay;
    }

    public static Bitmap getLoacalBitmap(String url) {
        try {
            ByteArrayOutputStream out;
            FileInputStream fis = new FileInputStream(url);
            BufferedInputStream bis = new BufferedInputStream(fis);
            out = new ByteArrayOutputStream();
            @SuppressWarnings("unused")
            int hasRead = 0;
            byte[] buffer = new byte[1024 * 2];
            while ((hasRead = bis.read(buffer)) > 0) {
                // 读出多少数据，向输出流中写入多少
                out.write(buffer);
                out.flush();
            }
            out.close();
            fis.close();
            bis.close();
            byte[] data = out.toByteArray();
            // 长宽减半
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = 3;
            return BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

}
