package com.tgf.kcwc.businessconcerns;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.platform.comapi.map.C;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.callback.OnSingleClickListener;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.AddCustomerModel;
import com.tgf.kcwc.mvp.model.FriendGroupingModel;
import com.tgf.kcwc.mvp.model.FriendListModel;
import com.tgf.kcwc.mvp.model.MorePopupwindowBean;
import com.tgf.kcwc.mvp.presenter.AddCustomerPresenter;
import com.tgf.kcwc.mvp.presenter.BusinessAttentionPresenter;
import com.tgf.kcwc.mvp.view.AddCustomerView;
import com.tgf.kcwc.mvp.view.BusinessAttentionView;
import com.tgf.kcwc.potentialcustomertrack.PotentialCustomerTrackActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.StringUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MorePopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * @anthor noti
 * @time 2017/8/4 11:50
 * 添加客户
 */

public class AddCustomerActivity extends BaseActivity implements AddCustomerView, BusinessAttentionView {
    private LinearLayout groupingLl;
    private TextView groupNameTv;
    private EditText nameEt;
    private EditText phoneEt;
    private Button addBtn;
    private AddCustomerPresenter mPresenter;
    private BusinessAttentionPresenter groupPresenter;
    public static final int REQUEST_CODE_ADD_CUSTOMER = 111;
    //popwindow集合数据
    List<MorePopupwindowBean> popList = new ArrayList<>();
    //分组Id
    private int groupNameId;

    @Override
    protected void setUpViews() {
        groupingLl = (LinearLayout) findViewById(R.id.add_customer_ll);
        groupNameTv = (TextView) findViewById(R.id.add_group_name);
        nameEt = (EditText) findViewById(R.id.add_name_et);
        phoneEt = (EditText) findViewById(R.id.add_phone_et);
        addBtn = (Button) findViewById(R.id.add_customer_btn);
        initPop();
        groupPresenter = new BusinessAttentionPresenter();
        groupPresenter.attachView(this);
        //获取组名
        groupPresenter.getGroupingList(IOUtils.getToken(getContext()));
        mPresenter = new AddCustomerPresenter();
        mPresenter.attachView(this);
        addBtn.setOnClickListener(new OnSingleClickListener() {
            @Override
            protected void onSingleClick(View view) {
                String nameStr = nameEt.getText().toString().trim();
                String phoneStr = phoneEt.getText().toString().trim();
                if (!StringUtils.checkNull(nameStr)) {
                    CommonUtils.showToast(mContext, "名称不能为空");
                    return;
                }
                if (!StringUtils.checkNull(phoneStr)) {
                    CommonUtils.showToast(mContext, "手机号码不能为空");
                    return;
                }
                //添加客户
                mPresenter.addCustomer(IOUtils.getToken(getContext()), nameStr, phoneStr, groupNameId);
            }
        });
        groupingLl.setOnClickListener(new OnSingleClickListener() {
            @Override
            protected void onSingleClick(View view) {
                Intent intent = new Intent(mContext, SelectGroupActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_CUSTOMER);
            }
        });
    }

    @Override
    protected void titleBarCallback(ImageButton back, final FunctionView function, TextView text) {
        backEvent(back);
        text.setText("添加客户");
        function.setImageResource(R.drawable.icon_common_right);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MorePopupWindow morePopupWindow = new MorePopupWindow(AddCustomerActivity.this,
                        popList, new MorePopupWindow.MoreOnClickListener() {
                    @Override
                    public void moreOnClickListener(int position, MorePopupwindowBean item) {
                        switch (position) {
                            case 0://首页
                                finish();
                                break;
                            case 1://消息
                                finish();
                                break;
                            case 2://扫一扫
                                finish();
                                break;
                        }
                    }
                });
                morePopupWindow.showPopupWindow(function);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
    }

    /**
     * 初始化popwindow选项
     */
    public void initPop() {
        String[] popValues = mRes.getStringArray(R.array.global_nav_values);
        for (String value : popValues) {
            MorePopupwindowBean morePopupwindowBean = null;
            morePopupwindowBean = new MorePopupwindowBean();
            morePopupwindowBean.title = value;
            popList.add(morePopupwindowBean);
        }
    }

    @Override
    public void addSuccess(ArrayList<AddCustomerModel.FriendItem> item) {
        setLoadingIndicator(false);
        CommonUtils.showToast(mContext, "添加成功");
        finish();
    }

    @Override
    public void addFail(String msg) {
        setLoadingIndicator(false);
        CommonUtils.showToast(mContext, msg);
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

    @Override
    public void showGrouping(ArrayList<FriendGroupingModel.ListItem> list) {
        groupNameTv.setText(list.get(1).name);
        groupNameId = list.get(1).friendGroupId;
    }

    //没有用
    @Override
    public void showFriendList(ArrayList<FriendListModel.ListItem> list) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ADD_CUSTOMER:
                    String groupName = data.getStringExtra(Constants.IntentParams.DATA);
                    groupNameId = data.getIntExtra(Constants.IntentParams.DATA2, -1);
                    groupNameTv.setText(groupName);
                    break;
            }
        }
    }
}
