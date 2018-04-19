package com.tgf.kcwc.ticket.apply;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.ApplyTicketModel;
import com.tgf.kcwc.mvp.model.PreTicketModel;
import com.tgf.kcwc.mvp.model.TicketDescModel;
import com.tgf.kcwc.mvp.presenter.ApplyTicketPresenter;
import com.tgf.kcwc.mvp.presenter.PreRegTicketPresenter;
import com.tgf.kcwc.mvp.view.PreRegTicketView;
import com.tgf.kcwc.mvp.view.WrapApplyTicketView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.OpenShareWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2017/2/16 11:08
 * E-mail：fishloveqin@gmail.com
 * 预报名领取赠票
 */

public class PreRegistrationActivity extends BaseActivity {

    protected SimpleDraweeView cover;
    protected RelativeLayout content;
    protected ScrollView scrollView;
    private OpenShareWindow openShareWindow = null;
    private int mExId;
    private String mExName;
    private String mExCover;
    private List<BaseFragment> mSubFragments = new ArrayList<BaseFragment>();
    private PreRegTicketPresenter mApplyStatusPresenter;
    private ApplyTicketPresenter mPresenter;
    private String applyType = "1";

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("" + mExName);

    }

    private WbShareHandler mWbHandler;


    @Override
    protected void setUpViews() {
        initView();


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mExId = intent.getIntExtra(Constants.IntentParams.ID, -1);
        mExName = intent.getStringExtra(Constants.IntentParams.DATA);
        mExCover = intent.getStringExtra(Constants.IntentParams.DATA2);
        openShareWindow = new OpenShareWindow(PreRegistrationActivity.this);
        super.setContentView(R.layout.activity_apply_ticket);


    }


    private WrapApplyTicketView wrapApplyTicketView = new WrapApplyTicketView() {


        @Override
        public void showForms(PreTicketModel formsModel) {

            ApplyFormsFragment fragment = (ApplyFormsFragment) mSubFragments.get(0);
            fragment.loadForms(formsModel);
        }

        @Override
        public void showApplyList(ApplyTicketModel applyModel) {
            ApplyFormsFragment fragment = (ApplyFormsFragment) mSubFragments.get(0);
            fragment.loadApplyList(applyModel);
        }

        @Override
        public void showAD(String adText) {

            ApplyFormsFragment fragment = (ApplyFormsFragment) mSubFragments.get(0);
            fragment.loadAD(adText);
        }

        @Override
        public void showTicketDesc(TicketDescModel model) {

            ApplyFormsFragment fragment = (ApplyFormsFragment) mSubFragments.get(0);
            fragment.loadTicketDesc(model);
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

    };

    private void addSubFragments() {

        mSubFragments.clear();

        ApplyFormsFragment applyFormsFragment = new ApplyFormsFragment();
        applyFormsFragment.setName(mExName);
        FailureFragment failureFragment = new FailureFragment();
        SuccessFragment successFragment = new SuccessFragment();
        mSubFragments.add(applyFormsFragment);
        mSubFragments.add(failureFragment);
        mSubFragments.add(successFragment);
        FragmentManager fragmentManager = getSupportFragmentManager();

        for (BaseFragment f : mSubFragments) {
            if (!f.isAdded()) {
                fragmentManager.beginTransaction().add(R.id.content, f).commit();
            }
        }
    }

    /**
     * 根据index切换Fragment布局(已缓存Fragment， 调用show、hide方法显示、隐藏)
     *
     * @param index
     */
    private BaseFragment switchSubFragment(int index) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        BaseFragment fragment = mSubFragments.get(index);
        for (BaseFragment f : mSubFragments) {

            if (f == fragment) {

                fragmentManager.beginTransaction().show(fragment).commit();

            } else {
                fragmentManager.beginTransaction().hide(f).commit();
            }
        }
        return fragment;
    }

    private void initView() {
        cover = (SimpleDraweeView) findViewById(R.id.cover);
        content = (RelativeLayout) findViewById(R.id.content);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        addSubFragments();

        mApplyStatusPresenter = new PreRegTicketPresenter();
        mApplyStatusPresenter.attachView(mApplyView);
        mApplyStatusPresenter.getApplyStatus(mExId + "", "3", IOUtils.getToken(mContext));
        mPresenter = new ApplyTicketPresenter();
        mPresenter.attachView(wrapApplyTicketView);

        mPresenter
                .getApplyForms(
                        mExId + "", "2",
                        "3", "", "",
                        IOUtils.getToken(
                                mContext));

        mPresenter.getADInfo(builderReqParams(false));
        mPresenter.getApplyList(builderReqParams(false),true);
        mPresenter.getTicketDescInfo(builderReqParams(true));
    }

    private Map<String, String> builderReqParams(boolean isType) {
//http://car.i.cacf.cn/ticket/api/getFavText?token=C9Jmfnl1IQKMCWNg7ctkq5zIeEvg1eF9&apply_type=1&event_id=2
        //http://car.i.cacf.cn/cert/api/applyLists?token=C9Jmfnl1IQKMCWNg7ctkq5zIeEvg1eF9&event_id=257

        Map<String, String> params = new HashMap<String, String>();
        params.put("token", IOUtils.getToken(mContext));
        if (isType) {
            params.put("type", Constants.Types.APPLY_TICKET + "");
        } else {
            params.put("apply_type", applyType);
        }

        params.put("event_id", mExId + "");
        return params;
    }


    private PreRegTicketView<DataItem> mApplyView = new PreRegTicketView<DataItem>() {
        @Override
        public void showData(DataItem dataItem) {

            int code = dataItem.code;

            switch (code) {

                case Constants.NetworkStatusCode.SUCCESS:

                    switchSubFragment(0);
                    break;
                case Constants.NetworkStatusCode.STATUS_CODE4:
                case Constants.NetworkStatusCode.STATUS_CODE2:

                    switchSubFragment(1);
                    break;

                case Constants.NetworkStatusCode.STATUS_CODE3:

                    switchSubFragment(2);
                    break;

            }
        }
    };


}
