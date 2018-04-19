package com.tgf.kcwc.ticket;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.PreTicketModel;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.SpecRectView;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/2/16 16:36
 * E-mail：fishloveqin@gmail.com
 * 赠票种类
 */

public class TicketCategoryActivity extends BaseActivity {

    private ListView mList;
    private List<PreTicketModel.TicketTypeBean> mTypes = null;
    private WrapAdapter<PreTicketModel.TicketTypeBean> mAdapter = null;
    private ImageView mCloseBtn;

    private int mSelect;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {

        mList = (ListView) findViewById(R.id.list);
        mCloseBtn = (ImageView) findViewById(R.id.closedBtn);
        mCloseBtn.setOnClickListener(this);

        if (mSelect != -1) {

            int index = 0;
            for (PreTicketModel.TicketTypeBean bean : mTypes) {

                if (index == mSelect) {
                    bean.info.isSelected = true;
                    break;
                }
                index++;

            }

        }
        initListData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mTypes = intent.getParcelableArrayListExtra(Constants.IntentParams.DATA);
        mSelect = intent.getIntExtra(Constants.IntentParams.DATA2, -1);
        setContentView(R.layout.activity_ticket_category);
    }

    private void updateAdapterUI(int position) {
        int index = 0;
        for (PreTicketModel.TicketTypeBean bean : mTypes) {
            if (bean.info.isSelected == true && index != position) {
                bean.info.isSelected = false;
            }
            index++;
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initListData() {

        mAdapter = new WrapAdapter<PreTicketModel.TicketTypeBean>(mContext, mTypes,
                R.layout.pre_ticket_item) {

            @Override
            public void convert(ViewHolder helper, final PreTicketModel.TicketTypeBean item) {

                SpecRectView specRectView;
                specRectView = helper.getView(R.id.specRectView);
                specRectView.setDrawTicketTypeColor(item.info.color);
                final TextView type = helper.getView(R.id.type);
                type.setText(item.ticketName);
                final ImageView checkImg = helper.getView(R.id.checkImg);
                final TextView checkTv = helper.getView(R.id.checkTv);
                final RelativeLayout checkLayout = helper.getView(R.id.checkLayout);
                if (item.info.isSelected) {

                    checkLayout.setBackgroundResource(R.drawable.button_bg_13);
                    checkTv.setTextColor(mRes.getColor(R.color.white));
                } else {
                    checkLayout.setBackgroundResource(R.drawable.button_bg_12);
                    checkTv.setTextColor(mRes.getColor(R.color.text_more));
                }
                final int position = helper.getPosition();
                checkLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (item.info.isSelected) {
                            item.info.isSelected = false;
                        } else {
                            item.info.isSelected = true;
                        }

                        updateAdapterUI(position);
                    }
                });
                TextView expire = helper.getView(R.id.expire);
                expire.setText("有效期" + DateFormatUtil.dispActiveTime2(item.info.useTimeStart,
                        item.info.useTimeEnd));
                TextView remark = helper.getView(R.id.remark);
                remark.setText(item.info.remarks);
                TextView moneyTag = helper.getView(R.id.moneyTag);
                TextView price = helper.getView(R.id.price);
                price.setText(item.info.price);
                price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
        };
        mList.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {
            case R.id.closedBtn:

                int resultId = findSelectedItemId();
                Intent intent = getIntent();
                intent.putExtra(Constants.IntentParams.ID, resultId);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    private int findSelectedItemId() {
        int position = 0;
        for (PreTicketModel.TicketTypeBean bean : mTypes) {
            if (bean.info.isSelected) {
                return position;
            }
            position++;
        }
        return -1;
    }
}
