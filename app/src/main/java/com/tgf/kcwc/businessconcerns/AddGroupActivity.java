package com.tgf.kcwc.businessconcerns;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.AddGroupModel;
import com.tgf.kcwc.mvp.presenter.AddGroupPresenter;
import com.tgf.kcwc.mvp.view.AddGroupView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.StringUtils;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;

/**
 * @anthor noit
 * @time 2017/8/4 11:49
 * 添加/编辑分组
 */

public class AddGroupActivity extends BaseActivity implements AddGroupView{
    private EditText groupNameEt;
    private Button addBtn;
    private AddGroupPresenter mPresenter;
    private ArrayList<AddGroupModel.ListItem> mList = new ArrayList<>();

    @Override
    protected void setUpViews() {
        addBtn = (Button) findViewById(R.id.add_group_btn);
        groupNameEt = (EditText) findViewById(R.id.add_group_et);
        mPresenter = new AddGroupPresenter();
        mPresenter.attachView(this);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/8/4 保存
                String groupName = groupNameEt.getText().toString();
                if (!StringUtils.checkNull(groupName)){
                    CommonUtils.showToast(mContext,"分组名称不能为空");
                    return;
                }
                mPresenter.addGroup(IOUtils.getToken(getContext()),groupName);
            }
        });
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("添加/编辑分组");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
    }

    @Override
    public void addGroupSuccess(ArrayList<AddGroupModel.ListItem> list) {
        // TODO: 2017/8/8  返回分组管理
        setLoadingIndicator(false);
        CommonUtils.showToast(mContext,"添加成功");
        finish();
    }

    @Override
    public void addGroupFail(String msg) {
        setLoadingIndicator(false);
        CommonUtils.showToast(mContext,msg);
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
        return mContext;
    }
}
