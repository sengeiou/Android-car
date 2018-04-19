package com.tgf.kcwc.me.integral.orgintegral;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.OrgIntegralDetailsBean;
import com.tgf.kcwc.mvp.presenter.OrgIntegralDetailsPresenter;
import com.tgf.kcwc.mvp.view.OrgIntegralDetailsView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.ShowAlertDialogUtil;
import com.tgf.kcwc.view.FunctionView;

/**
 * Created by Administrator on 2017/11/2.
 */

public class OrgIntegralDetailsActivity extends BaseActivity implements OrgIntegralDetailsView {


    OrgIntegralDetailsPresenter mOrgIntegralDetailsPresenter;

    private String ID = "";
    private String type ="1";
    private TextView mNums;
    private TextView mType;
    private TextView mBehavior;
    private TextView mOperator;
    private TextView mTime;
    private TextView mTitle;

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("经验值详情");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orgintegraldetails);
        ID = getIntent().getStringExtra(Constants.IntentParams.ID);
        type = getIntent().getStringExtra(Constants.IntentParams.ID2);
        mOrgIntegralDetailsPresenter = new OrgIntegralDetailsPresenter();
        mOrgIntegralDetailsPresenter.attachView(this);

        mNums = findViewById(R.id.nums);
        mType = findViewById(R.id.type);
        mBehavior = findViewById(R.id.behavior);
        mOperator = findViewById(R.id.operator);
        mTime = findViewById(R.id.time);
        mTitle = findViewById(R.id.title_bar_text);

        if (type.equals("1")) {
            mTitle.setText("积分详情");
        } else {
            mTitle.setText("经验值详情");
        }
        mOrgIntegralDetailsPresenter.getOrgLogDetail(IOUtils.getToken(mContext), ID);
    }

    @Override
    public void DataSucceed(OrgIntegralDetailsBean orgIntegralDetailsBean) {
        OrgIntegralDetailsBean.Data data = orgIntegralDetailsBean.data;
        if (data.crease.equals("reward")) {
            mNums.setText("+ " + data.points);
            mNums.setTextColor(getResources().getColor(R.color.text_color36));
        } else {
            mNums.setText("- " + data.points);
            mNums.setTextColor(getResources().getColor(R.color.tab_text_s_color));
        }
        mType.setText("机构");
        //1、个人积分2、商务积分3、机构积分 4、个人经验5、商务经验6、机构经验7、员工个人行为增加机构积分8、商业行为增加机构积分9、个人行为增加机构经验10、商业行为增加机构经验
        switch (data.type) {
            case 1:
                mBehavior.setText("个人积分");
                break;
            case 2:
                mBehavior.setText("商务积分");
                break;
            case 3:
                mBehavior.setText("机构积分");
                break;
            case 4:
                mBehavior.setText("个人经验");
                break;
            case 5:
                mBehavior.setText("商务经验");
                break;
            case 6:
                mBehavior.setText("机构经验");
                break;
            case 7:
                mBehavior.setText("员工个人行为增加机构积分");
                break;
            case 8:
                mBehavior.setText("商业行为增加机构积分");
                break;
            case 9:
                mBehavior.setText("个人行为增加机构经验");
                break;
            case 10:
                mBehavior.setText("商业行为增加机构经验");
                break;
        }
        mOperator.setText("");
        mTime.setText(data.createTime);
    }

    @Override
    public void dataListDefeated(String msg) {
        CommonUtils.showToast(mContext, msg);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {
        CommonUtils.showToast(mContext, "系统异常");
    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
