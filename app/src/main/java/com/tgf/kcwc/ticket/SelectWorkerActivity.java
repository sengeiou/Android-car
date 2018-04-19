package com.tgf.kcwc.ticket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.ContactAdapter;
import com.tgf.kcwc.adapter.OnItemClickListener;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.coupon.manage.CouponSendSeeActivity;
import com.tgf.kcwc.globalchat.Constant;
import com.tgf.kcwc.mvp.model.ContactUser;
import com.tgf.kcwc.mvp.model.WorkeModel;
import com.tgf.kcwc.mvp.presenter.SelectWorkerPresenter;
import com.tgf.kcwc.mvp.view.SelectWorkerView;
import com.tgf.kcwc.ticket.manage.TicketOrgSendSeeActivity;
import com.tgf.kcwc.ticket.manage.TicketSendSeeActivity;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Auther: Scott
 * Date: 2017/10/20 0020
 * E-mail:hekescott@qq.com
 */

public class SelectWorkerActivity extends BaseActivity implements SelectWorkerView {

    private SelectWorkerPresenter selectWorkerPresenter;
    private RecyclerView recyleViewFellow;
    private ArrayList<ContactUser> myFellows = new ArrayList<>();
    private IndexBar indexBar;
    private SuspensionDecoration   mDecoration;
    private ContactAdapter mFellowAdapter;
    private TextView               mTvSideBarHint;
    @Override
    protected void setUpViews() {
        Intent fromIntent = getIntent();
        Serializable contactSel = fromIntent.getSerializableExtra(Constants.IntentParams.DATA);
        if(contactSel!= null){
            mSelecUsers = (ArrayList<ContactUser>) contactSel;
        }
        findViewById(R.id.selectworker_back).setOnClickListener(this);
        findViewById(R.id.title_okbtn).setOnClickListener(this);
        selectWorkerPresenter = new SelectWorkerPresenter();
        selectWorkerPresenter.attachView(this);
        selectWorkerPresenter.getSelectWorkerList(IOUtils.getToken(this));
        recyleViewFellow = (RecyclerView) findViewById(R.id.worker_rv);
        indexBar = (IndexBar) findViewById(R.id.worker_indexBar);
        mTvSideBarHint = (TextView) findViewById(R.id.tvSideBarHint);
        mFellowAdapter = new ContactAdapter(getContext(), myFellows);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyleViewFellow.setLayoutManager(linearLayoutManager);
        recyleViewFellow.setAdapter(mFellowAdapter);
        mDecoration = new SuspensionDecoration(getContext(), myFellows);
        recyleViewFellow.addItemDecoration(mDecoration);
        //indexbar初始化
        indexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(linearLayoutManager);//设置RecyclerView的LayoutManager
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectworker);
    }
    public ArrayList<ContactUser> mSelecUsers = new ArrayList<>();
    @Override
    public void showWorkerList(ArrayList<ContactUser> workeModellist) {
        myFellows = workeModellist;
        if (mSelecUsers != null && myFellows != null) {
            if (mSelecUsers.size() != 0 && myFellows.size() != 0) {
                for (ContactUser selectUser : mSelecUsers) {
                    for (ContactUser myFellow : myFellows) {
                        if (selectUser.mobile.equals(myFellow.mobile)) {
                            myFellow.isSelected = true;
                        }
                    }
                }
            }
        }

        mFellowAdapter.setDatas(myFellows);
        mFellowAdapter.notifyDataSetChanged();
        indexBar.setmSourceDatas(myFellows)//设置数据
                .invalidate();
        mDecoration.setmDatas(myFellows);
        mFellowAdapter.setOnItemClicklisenter(new OnItemClickListener<ContactUser>() {

            @Override
            public void onItemClick(ViewGroup parent, View view, ContactUser contactUser, int position) {
                Iterator itr = mSelecUsers.iterator();
                while (itr.hasNext()) {
                    ContactUser itrValue = (ContactUser) itr.next();
                    if (contactUser.mobile.equals(itrValue.mobile)) {
                        if (!contactUser.isSelected) {
                            itr.remove();
                            return;
                        }
                    }
                }
                mSelecUsers.add(contactUser);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, ContactUser contactUser,
                                           int position) {
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
             switch (view.getId()){
                         case R.id.selectworker_back:
                           finish();
                             break;
                         case R.id.title_okbtn:
                         {
                             finish();
                             Intent toIntent = new Intent();
                             toIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                             toIntent.setClass(this, TicketOrgSendSeeActivity.class);
                             toIntent.putExtra(Constants.IntentParams.DATA, (Serializable) mSelecUsers);
                             startActivity(toIntent);
                         }
                             break;
                         default:
                             break;
                     }
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return SelectWorkerActivity.this;
    }
}
