package com.tgf.kcwc.certificate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.OrganizationBean;
import com.tgf.kcwc.mvp.presenter.CertDataPresenter;
import com.tgf.kcwc.mvp.view.CertDataView;
import com.tgf.kcwc.view.FunctionView;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/3/28 11:54
 * E-mail：fishloveqin@gmail.com
 */

public class OrgListActivity extends BaseActivity implements CertDataView<List<OrganizationBean>>,
                             AdapterView.OnItemClickListener {
    protected TextView mClosedBtn;
    protected EditText mSearchEdit;
    protected ListView mList;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {
        initView();
        mPresenter = new CertDataPresenter();
        mPresenter.attachView(this);
        mPresenter.getOrgList("");
    }

    private CertDataPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_org_list);

    }

    @Override
    public void showData(List<OrganizationBean> organizationBeans) {

        WrapAdapter<OrganizationBean> adapter = new WrapAdapter<OrganizationBean>(mContext,
            organizationBeans, R.layout.org_list_item) {
            @Override
            public void convert(ViewHolder helper, OrganizationBean item) {

                helper.setText(R.id.name, item.name);
            }
        };
        mList.setAdapter(adapter);

    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return mContext;
    }

    private void initView() {
        mClosedBtn = (TextView) findViewById(R.id.closedBtn);
        mClosedBtn.setOnClickListener(this);
        mSearchEdit = (EditText) findViewById(R.id.etSearch);
        mSearchEdit.addTextChangedListener(mTWListener);
        mList = (ListView) findViewById(R.id.list);
        mList.setOnItemClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    TextWatcher mTWListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            mPresenter.getOrgList(s.toString());
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        OrganizationBean organizationBean = (OrganizationBean) parent.getAdapter()
            .getItem(position);
        Intent intent = new Intent();
        intent.putExtra(Constants.IntentParams.DATA, organizationBean.name);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {
            case R.id.closedBtn:
                finish();
                break;
        }

    }
}
