package com.tgf.kcwc.businessconcerns;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.BaseInfoModel;
import com.tgf.kcwc.mvp.presenter.EditCustomerPresenter;
import com.tgf.kcwc.mvp.view.EditCustomerView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;

/**
 * @anthor noti
 * @time 2017/8/3 8:52
 * 编辑客户资料
 */

public class EditCustomerActivity extends BaseActivity implements EditCustomerView{
    private EditText nameEt;
    private EditText phoneEt;
    private EditText companyEt;
    private EditText departmentEt;
    private EditText positionEt;
    private EditText companyAddressEt;
    private EditText familyAddressEt;
    private EditText birthdayEt;
    private EditText qqEt;
    private EditText wbEt;
    private EditText wxEt;
    private EditText tagEt;
    private EditText remarkEt;

    private EditCustomerPresenter mPresenter;

    private int friendId;//好友id
    private BaseInfoModel list;

    @Override
    protected void setUpViews() {
        list = (BaseInfoModel) getIntent().getSerializableExtra(Constants.IntentParams.DATA);
        friendId = getIntent().getIntExtra(Constants.IntentParams.DATA2,-1);
        nameEt = (EditText) findViewById(R.id.edit_customer_name_et);
        phoneEt = (EditText) findViewById(R.id.edit_customer_phone_et);
        companyEt = (EditText) findViewById(R.id.edit_customer_company_et);
        departmentEt = (EditText) findViewById(R.id.edit_customer_department_et);
        positionEt = (EditText) findViewById(R.id.edit_customer_position_et);
        companyAddressEt = (EditText) findViewById(R.id.edit_customer_company_address_et);
        familyAddressEt = (EditText) findViewById(R.id.edit_customer_family_address_et);
        birthdayEt = (EditText) findViewById(R.id.edit_customer_birthday_et);
        qqEt = (EditText) findViewById(R.id.edit_customer_qq_et);
        wbEt = (EditText) findViewById(R.id.edit_customer_wb_et);
        wxEt = (EditText) findViewById(R.id.edit_customer_wx_et);
        tagEt = (EditText) findViewById(R.id.edit_customer_tag_et);
        remarkEt = (EditText) findViewById(R.id.edit_customer_remark_et);
        //初始化数据
        setData();

        mPresenter = new EditCustomerPresenter();
        mPresenter.attachView(this);
    }

    /**
     * 设置数据
     */
    private void setData() {
        if (list.name.equals("-")){
            nameEt.setHint("-");
        }else {
            nameEt.setText(list.name);
        }
        if (list.tel.equals("-")){
            phoneEt.setHint("-");
        }else {
            phoneEt.setText(list.tel);
        }
        if (list.company.equals("-")){
            companyEt.setHint("-");
        }else {
            companyEt.setText(list.company);
        }
        if (list.department.equals("-")){
            departmentEt.setHint("-");
        }else {
            departmentEt.setText(list.department);
        }
        if (list.position.equals("-")){
            positionEt.setHint("-");
        }else {
            positionEt.setText(list.position);
        }
        if (list.sAddress.equals("-")){
            companyAddressEt.setHint("-");
        }else {
            companyAddressEt.setText(list.sAddress);
        }
        if (list.homeAddress.equals("-")){
            familyAddressEt.setHint("-");
        }else {
            familyAddressEt.setText(list.homeAddress);
        }
        if (list.birthday.equals("-")){
            birthdayEt.setHint("-");
        }else {
            birthdayEt.setText(list.birthday);
        }
        if (list.qq.equals("-")){
            qqEt.setHint("-");
        }else {
            qqEt.setText(list.qq);
        }
        if (list.weibo.equals("-")){
            wbEt.setHint("-");
        }else {
            wbEt.setText(list.weibo);
        }
        if (list.wechat.equals("-")){
            wxEt.setHint("-");
        }else {
            wxEt.setText(list.wechat);
        }
//        tagEt.setText();
        if (list.remark.equals("-")){
            remarkEt.setHint("-");
        }else {
            remarkEt.setText(list.remark);
        }
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, final TextView text) {
        backEvent(back);
        text.setText("编辑客户资料");
        text.setTextSize(18);
        function.setTextResource("保存",R.color.white,14);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEt.getText().toString();
                String phone = phoneEt.getText().toString();
                String company = companyEt.getText().toString();
                String department = departmentEt.getText().toString();
                String position = positionEt.getText().toString();
                String companyAddress = companyAddressEt.getText().toString();
                String familyAddress = familyAddressEt.getText().toString();
                String birthday = birthdayEt.getText().toString();
                String qq = qqEt.getText().toString();
                String wb = wbEt.getText().toString();
                String wx = wxEt.getText().toString();
                String tag = tagEt.getText().toString();
                String remark = remarkEt.getText().toString();
                // TODO: 2017/8/14 保存
                mPresenter.editCustomer(IOUtils.getToken(getContext()),birthday,company,department,familyAddress,friendId, mGlobalContext.latitude,mGlobalContext.longitude,name,position,qq,remark,companyAddress,phone,wx,wb);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);
    }

    @Override
    public void saveSuccess() {
        CommonUtils.showToast(mContext,"保存成功");
        finish();
    }

    @Override
    public void saveFail(String msg) {
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
        return this;
    }
}
