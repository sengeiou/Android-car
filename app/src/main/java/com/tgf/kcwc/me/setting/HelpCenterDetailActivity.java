package com.tgf.kcwc.me.setting;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.HelpCenterDetailModel;
import com.tgf.kcwc.mvp.presenter.HelpCenterDetailPresenter;
import com.tgf.kcwc.mvp.view.HelpCenterDetailView;
import com.tgf.kcwc.util.StringUtils;
import com.tgf.kcwc.view.FunctionView;

/**
 * @anthor noti
 * @time 2017/8/22
 * @describle
 */
public class HelpCenterDetailActivity extends BaseActivity implements HelpCenterDetailView{
    private TextView titleTv;
    private TextView contentTv;
    HelpCenterDetailPresenter helpCenterDetailPresenter;
    private String title;

    @Override
    protected void setUpViews() {
        int id = getIntent().getIntExtra(Constants.IntentParams.ID,-1);
        title = getIntent().getStringExtra(Constants.IntentParams.NAME);

        titleTv = (TextView) findViewById(R.id.item_detail_title);
        contentTv = (TextView) findViewById(R.id.item_detail_content);

        helpCenterDetailPresenter = new HelpCenterDetailPresenter();
        helpCenterDetailPresenter.attachView(this);
        helpCenterDetailPresenter.getHelpDetail(Constants.SALE_TOKEN,id);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        if (StringUtils.checkNull(title)){
            text.setText(title);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center_detail);
    }

    @Override
    public void showHelpCenterDetail(HelpCenterDetailModel detailModel) {
        titleTv.setText(detailModel.title);
        contentTv.setText(detailModel.content);
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
