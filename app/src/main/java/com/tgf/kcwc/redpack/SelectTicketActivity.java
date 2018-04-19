package com.tgf.kcwc.redpack;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.mvp.model.RedTicketModel;
import com.tgf.kcwc.mvp.view.WrapView;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/11/9 0009
 * E-mail:hekescott@qq.com
 */

public class SelectTicketActivity extends BaseActivity implements WrapView {

    private ListView mSelectTicketLv;
    private ArrayList<RedTicketModel> redTicketModels =new ArrayList<>();
    @Override
    protected void setUpViews() {
        mSelectTicketLv = findViewById(R.id.selectticket_lv);
        bindViewData();
    }
    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("选择门票");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private void bindViewData() {
        mSelectTicketLv.setAdapter(new WrapAdapter<RedTicketModel>(getContext(),redTicketModels,R.layout.listitem_redpack_selectticket) {
            @Override
            public void convert(ViewHolder helper, RedTicketModel item) {

            }
        });

    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return this;
    }
}
