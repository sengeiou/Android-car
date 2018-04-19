package com.tgf.kcwc.ticket;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.ContactAdapter;
import com.tgf.kcwc.adapter.OnItemClickListener;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.mvp.model.ContactUser;
import com.tgf.kcwc.mvp.presenter.MyFellowPresenter;
import com.tgf.kcwc.mvp.view.MyFellowView;
import com.tgf.kcwc.util.IOUtils;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Auther: Scott
 * Date: 2017/2/10 0010
 * E-mail:hekescott@qq.com
 */

public class MyFellowFragment extends BaseFragment implements MyFellowView {
    private static final String    TAG       = "MyFellowFragment";
    private RecyclerView recyleViewFellow;
    private ArrayList<ContactUser> myFellows = new ArrayList<>();
    private IndexBar               indexBar;
    private SuspensionDecoration   mDecoration;
    private ContactAdapter mFellowAdapter;
    private TextView               mTvSideBarHint;
    public MyFriendActivity        myFriendActivity;
    String token;
    String userId;
    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_myfellow;
    }

    @Override
    protected void initView() {
        userId =  IOUtils.getUserId(getContext());
        token = IOUtils.getToken(getContext());
        myFriendActivity = (MyFriendActivity) getActivity();
        MyFellowPresenter myFellowPresenter = new MyFellowPresenter();
        myFellowPresenter.attachView(this);
        myFellowPresenter.getFellowList(token,userId);

        recyleViewFellow = findView(R.id.myfellow_rv);
        indexBar = findView(R.id.myfellow_indexBar);
        mTvSideBarHint = findView(R.id.tvSideBarHint);
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
    public void showMyFellows(ArrayList<ContactUser> fellowlist) {
        myFellows = fellowlist;

        if (myFriendActivity.mSelecUsers != null && myFellows != null) {
            if (myFriendActivity.mSelecUsers.size() != 0 && myFellows.size() != 0) {
                for (ContactUser selectUser : myFriendActivity.mSelecUsers) {
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
                Iterator itr = myFriendActivity.mSelecUsers.iterator();
                while (itr.hasNext()) {
                    ContactUser itrValue = (ContactUser) itr.next();
                    if (contactUser.mobile.equals(itrValue.mobile)) {
                        if (!contactUser.isSelected) {
                            itr.remove();
                            return;
                        }
                    }
                }
                myFriendActivity.mSelecUsers.add(contactUser);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, ContactUser contactUser,
                                           int position) {
                return false;
            }
        });
    }


    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }
}
