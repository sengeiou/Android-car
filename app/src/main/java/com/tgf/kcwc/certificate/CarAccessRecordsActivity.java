package com.tgf.kcwc.certificate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.callback.OnSingleClickListener;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.me.UserPageActivity;
import com.tgf.kcwc.mvp.model.AccessRecord;
import com.tgf.kcwc.mvp.model.AccessRecordModel;
import com.tgf.kcwc.mvp.presenter.CertDataPresenter;
import com.tgf.kcwc.mvp.view.CertDataView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;

/**
 * Author：Jenny
 * Date:2017/1/3 20:44
 * E-mail：fishloveqin@gmail.com
 * 证件及打卡记录
 */

public class CarAccessRecordsActivity extends BaseActivity implements CertDataView<AccessRecordModel> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mId = getIntent().getIntExtra(Constants.IntentParams.ID, -1);
        setContentView(R.layout.activity_car_access_records);
    }

    private ListView          mList;
    private int               mId;
    private CertDataPresenter mPresenter;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

//        setTitleBarBg(R.color.white);
        backEvent(back);
//        back.setImageResource(R.drawable.nav_back_selector2);
        text.setText("证件及打卡记录");
//        text.setTextColor(mRes.getColor(R.color.text_color12));
        function.setImageResource(R.drawable.cover_default);
        function.setOnClickListener(new OnSingleClickListener() {
            @Override
            protected void onSingleClick(View view) {
                // TODO: 2017/9/14 个人主页
                Intent intent = new Intent();
                intent.putExtra(Constants.IntentParams.ID,
                        Integer.parseInt(IOUtils.getUserId(mContext)));
                intent.setClass(mContext, UserPageActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void setUpViews() {

        mList = (ListView) findViewById(R.id.list);
        mPresenter = new CertDataPresenter();
        mPresenter.attachView(this);

        mPresenter.loadAccessRecords(mId + "", IOUtils.getToken(mContext));
    }

    @Override
    public void showData(AccessRecordModel model) {

        WrapAdapter<AccessRecord> adapter = new WrapAdapter<AccessRecord>(mContext,
            R.layout.access_record_item, model.records) {
            @Override
            public void convert(ViewHolder helper, AccessRecord item) {

                helper.setText(R.id.content, item.content);
                helper.setText(R.id.time, item.time);
                ImageView imageView = helper.getView(R.id.statusImg);
                if (helper.getPosition() == 0) {
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        };
        mList.setAdapter(adapter);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

        showLoadingIndicator(active);

    }

    @Override
    public void showLoadingTasksError() {

        dismissLoadingDialog();
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
