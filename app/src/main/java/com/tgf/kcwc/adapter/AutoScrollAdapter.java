package com.tgf.kcwc.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.mvp.model.ApplyTicketModel;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.view.autoscrolltext.AutoCircleScrollListView;

import java.util.List;

/**
 * Author:Jenny
 * Date:2017/11/9
 * E-mail:fishloveqin@gmail.com
 */

public class AutoScrollAdapter extends BaseAdapter {


    private List<ApplyTicketModel.User> mUsers;
    private Context mContext;
    private Resources mRes;

    /**
     * 循环展示次数,最理想的值为3
     */

    public AutoScrollAdapter(List<ApplyTicketModel.User> users, Context context) {
        this.mUsers = users;
        this.mContext = context;
        mRes = mContext.getResources();
    }

    @Override
    public int getCount() {
        if (mUsers.size() > 0) {

            return mUsers.size() * AutoCircleScrollListView.REPEAT_TIMES;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mUsers.get(position % mUsers.size());
    }

    @Override
    public long getItemId(int position) {
        return position % mUsers.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        WrapAdapter.ViewHolder holder = WrapAdapter.ViewHolder.get(mContext, view, viewGroup, R.layout.auto_text_item_view, position);

        ApplyTicketModel.User user = mUsers.get(position % mUsers.size());
        TextView nicknameTv = holder.getView(R.id.nicknameTv);
        TextView contentTv = holder.getView(R.id.contentTv);
        TextView brandTv = holder.getView(R.id.brandTv);
        RelativeLayout rl = holder.getView(R.id.rl);
        nicknameTv.setText(user.nickname);
        if (TextUtils.isEmpty(user.nickname)) {
            nicknameTv.setVisibility(View.GONE);
            contentTv.setVisibility(View.GONE);
        } else {
            nicknameTv.setVisibility(View.VISIBLE);
            contentTv.setVisibility(View.VISIBLE);
        }
        contentTv.setText("刚刚预约观展");
        if (TextUtils.isEmpty(user.brand)) {
            brandTv.setVisibility(View.GONE);
        } else {
            brandTv.setVisibility(View.VISIBLE);
        }
        CommonUtils.customDisplayAttentionText(",   关注" + "   " + user.brand + user.series, brandTv, mRes);
        // brandTv.setText(user.brand + "" + user.series);
        return holder.getConvertView();
    }


}
