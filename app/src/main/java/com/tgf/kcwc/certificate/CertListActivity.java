package com.tgf.kcwc.certificate;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.CertItem;
import com.tgf.kcwc.mvp.model.CertListModel;
import com.tgf.kcwc.mvp.presenter.CertDataPresenter;
import com.tgf.kcwc.mvp.view.CertDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2017/2/7 20:56
 * E-mail：fishloveqin@gmail.com
 * 证件列表
 */

public class CertListActivity extends BaseActivity implements CertDataView<CertListModel> {

    private CertDataPresenter mPrerenter;

    private ListView mList;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        setTitleBarBg(R.color.white);
        back.setImageResource(R.drawable.nav_back_selector2);
        backEvent(back);
        text.setText("证件列表");
        text.setTextColor(mRes.getColor(R.color.text_color12));
        function.setImageResource(R.drawable.rp_user);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent();
//                intent.putExtra(Constants.IntentParams.ID,
//                        Integer.parseInt(IOUtils.getUserId(mContext)));
//                intent.setClass(mContext, UserPageActivity.class);
//                startActivity(intent);
                CommonUtils.gotoMainPage(mContext, Constants.Navigation.ME_INDEX);
            }
        });
    }

    @Override
    protected void setUpViews() {

        mList = (ListView) findViewById(R.id.list);
        initEmptyView();
        mPrerenter = new CertDataPresenter();
        mPrerenter.attachView(this);
        mPrerenter.loadCertListDatas(IOUtils.getToken(mContext));
        mList.setOnItemClickListener(mOnItemClickListener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cert_list);
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent,
                                View view,
                                int position,
                                long id) {

            Map<String, Serializable> args = new HashMap<String, Serializable>();
            CertItem item = (CertItem) parent
                    .getAdapter()
                    .getItem(position);

            if (item == null) {
                return;
            }
            args.put(
                    Constants.IntentParams.ID,
                    item.id);
            args.put(
                    Constants.IntentParams.DATA,
                    item.certSN);
            if (item.type == 4) {//新增证件的详情
                CommonUtils
                        .startNewActivity(
                                mContext, args,
                                CarCertDetailActivity.class);
            } else {//跳转原来的证件详情
                CommonUtils
                        .startNewActivity(
                                mContext, args,
                                CertDetailActivity.class);
            }

        }
    };

    private boolean isFirst = true;

    @Override
    public void showData(CertListModel certListModel) {

        int size = certListModel.items.size();

        if (size == 0) {

            mEmptyLayout.setVisibility(View.VISIBLE);
            mList.setVisibility(View.GONE);
            return;
        } else {

            mEmptyLayout.setVisibility(View.GONE);
            mList.setVisibility(View.VISIBLE);
        }

        WrapAdapter<CertItem> adapter = new WrapAdapter<CertItem>(mContext, certListModel.items,
                R.layout.cert_list_item) {
            @Override
            public void convert(ViewHolder helper, CertItem item) {

                helper.setText(R.id.senseName, item.eventInfo.name);

                TextView expireTv = helper.getView(R.id.expire);
                expireTv.setText(DateFormatUtil.dispActiveTime2(item.certInfo.startTime, item.certInfo.endTime));
                helper.setText(R.id.title, item.certInfo.name);
                TextView currentTv = helper.getView(R.id.currentRecord);
                TextView totalTv = helper.getView(R.id.totalRecord);
                Button button = helper.getView(R.id.statusBtn);
                //0 审核中，1 通过，2 不通过
                int checktatus = item.checkStatus;
                if (checktatus == 0) {
                    button.setText("审核中");
                    button.setBackgroundResource(R.drawable.button_bg_16);
                    button.setVisibility(View.VISIBLE);
                    expireTv.setVisibility(View.GONE);
                    currentTv.setVisibility(View.GONE);
                    totalTv.setVisibility(View.GONE);
                } else if (checktatus == 1) {
                    expireTv.setVisibility(View.VISIBLE);
                    currentTv.setVisibility(View.VISIBLE);
                    int status = item.status;
                    if (status == 2) {
                        button.setText("已挂失");
                        button.setVisibility(View.VISIBLE);
                        button.setBackgroundResource(R.drawable.button_bg_17);
                    } else if (status == 3) {

                        button.setText("已过期");
                        button.setVisibility(View.VISIBLE);
                        button.setBackgroundResource(R.drawable.button_bg_17);
                    } else {
                        button.setVisibility(View.GONE);

                    }

                } else {
                    button.setText("未通过");
                    button.setBackgroundResource(R.drawable.button_bg_6);
                    button.setVisibility(View.VISIBLE);
                    expireTv.setVisibility(View.GONE);
                    currentTv.setVisibility(View.GONE);
                    totalTv.setVisibility(View.GONE);
                }

                //1表示每天、2表示一共
                StringBuilder stringBuilder = new StringBuilder();
                String str1 = "";
                if (item.certInfo.timesType == 1) {

                    int times = item.certInfo.times;
                    if (times == 0) {
                        str1 = String.format(getString(R.string.cert_current_record),
                                item.timesToday + "", "不限");
                    } else {
                        str1 = String.format(getString(R.string.cert_current_record),
                                item.timesToday + "", times + "");
                    }

                } else {
                    str1 = String.format(getString(R.string.cert_current_record2),
                            item.timesAll + "", item.certInfo.times + "");
                    totalTv.setVisibility(View.GONE);
                }

                //currentTv.setText(str1);
                String str2 = String.format(getString(R.string.cert_total_record), item.timesAll);

                CommonUtils.customDisplayText(str2, totalTv, mRes);

                CommonUtils.customDisplayText2(mRes, currentTv, str1, R.color.btn_select_color);
            }
        };

        mList.setAdapter(adapter);

        if (isFirst) {
            isFirst = false;
            View v = LayoutInflater.from(mContext).inflate(R.layout.no_more_data_layout, null,
                    false);
            mList.addFooterView(v);
        }
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
        mPrerenter.detachView();
    }
}
