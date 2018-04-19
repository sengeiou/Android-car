package com.tgf.kcwc.me.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.OnItemClickListener;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.HelpCenterModel;
import com.tgf.kcwc.mvp.presenter.HelpCenterPresenter;
import com.tgf.kcwc.mvp.view.HelpCenterView;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/8/22
 * @describle 帮助中心
 */
public class HelpCenterActivity extends BaseActivity implements HelpCenterView {
    private ListView listView;
    private WrapAdapter mAdapter;
    private AdapterView.OnItemClickListener onItemClickListener;
    private ArrayList<HelpCenterModel> helpList = new ArrayList<>();

    HelpCenterPresenter helpCenterPresenter;

    @Override
    protected void setUpViews() {
        listView = (ListView) findViewById(R.id.help_list_view);
        mAdapter = new WrapAdapter<HelpCenterModel>(mContext, helpList, R.layout.help_center_item) {
            @Override
            public void convert(ViewHolder helper, HelpCenterModel item) {
                helper.setText(R.id.item_title, item.title);
            }
        };
        onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //跳转帮助中心详情
                int id = helpList.get(i).id;
                String title = helpList.get(i).title;
                Intent intent = new Intent(mContext,HelpCenterDetailActivity.class);
                intent.putExtra(Constants.IntentParams.ID,id);
                intent.putExtra(Constants.IntentParams.NAME,title);
                startActivity(intent);
            }
        };
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(onItemClickListener);
        helpCenterPresenter = new HelpCenterPresenter();
        helpCenterPresenter.attachView(this);
        helpCenterPresenter.getHelpList(Constants.SALE_TOKEN, "car", "");
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("新手指引");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);
    }

    @Override
    public void showHelpList(HelpCenterModel list) {
        helpList.clear();
        helpList.add(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return this;
    }
}
