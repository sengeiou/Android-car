package com.tgf.kcwc.businessconcerns;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.FriendGroupingModel;
import com.tgf.kcwc.mvp.model.FriendListModel;
import com.tgf.kcwc.mvp.presenter.BusinessAttentionPresenter;
import com.tgf.kcwc.mvp.view.BusinessAttentionView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.StringUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @anthor noti
 * @time 2017/8/4 15:08
 * 好友分组
 */

public class FriendGroupingActivity extends BaseActivity implements BusinessAttentionView {
    private EditText searchEt;
    private ListView mListView;
    private WrapAdapter mAdapter;
    private AdapterView.OnItemClickListener mItemListener;
    private BusinessAttentionPresenter mPresenter;
    private ArrayList<FriendListModel.ListItem> mList = new ArrayList<>();
    private int page = 1;
    private boolean isRefresh;
    //搜索的分组id
    private int friendGroupId ;
    //搜索的好友名
    private String friendName;

    @Override
    protected void setUpViews() {
        searchEt = (EditText) findViewById(R.id.friend_seek);
        mListView = (ListView) findViewById(R.id.friend_group_lv);
        //搜索框文本实时监听
        searchEt.addTextChangedListener(new MyTextWatcher());
        // 搜索框的键盘搜索键点击回调
        searchEt.setOnKeyListener(new MyOnKeyListener());
        initRefreshLayout(mBGDelegate);
        mPresenter = new BusinessAttentionPresenter();
        mPresenter.attachView(this);
        // TODO: 2017/8/8
        mPresenter.getFriendAllList(IOUtils.getToken(getContext()), page);
        if (null != mAdapter) {
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(mItemListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.detachView();
        }
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("好友分组");
        function.setTextResource("下一步", R.color.white, 14);
        final ArrayList<Integer> id = new ArrayList<>();
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id.clear();
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).isSelect) {
                        id.add(mList.get(i).id);
                    }
                }
                if (id.size() <= 0) {
                    CommonUtils.showToast(mContext, "请选择好友");
                } else {
                    Gson gson = new Gson();
                    String str = gson.toJson(id);
                    Intent intent = new Intent(mContext, SelectGroupActivity.class);
                    intent.putExtra(Constants.IntentParams.DATA, str);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_grouping);
    }

    BGARefreshLayout.BGARefreshLayoutDelegate mBGDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {

        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            page = 1;
            isRefresh = true;
            loadMore();
        }

        @Override
        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
            isRefresh = false;
            page++;
            loadMore();
            return false;
        }
    };

    public void loadMore() {
        if (friendName != null){
            mPresenter.getSearchFriendList(IOUtils.getToken(getContext()),friendName,page);
        }else {
            mPresenter.getFriendAllList(IOUtils.getToken(getContext()), page);
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            showLoadingIndicator(true);
        } else {
            stopRefreshAll();
            showLoadingIndicator(false);
        }
    }

    @Override
    public void showLoadingTasksError() {

    }

    //不需要
    @Override
    public void showGrouping(ArrayList<FriendGroupingModel.ListItem> list) {
    }

    @Override
    public void showFriendList(ArrayList<FriendListModel.ListItem> list) {
        setLoadingIndicator(false);
        if (page == 1){
            mList.clear();
        }
        mList.addAll(list);
        if (null != mAdapter) {
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(mItemListener);
        } else {
            initAdapter();
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(mItemListener);
        }
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    //点击键盘搜索按钮
    public class MyOnKeyListener implements View.OnKeyListener {

        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {// 修改回车键功能
                // 先隐藏键盘
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                friendName = searchEt.getText().toString().trim();
                if (StringUtils.checkNull(friendName)) {
                    // TODO: 2017/8/8 刷新搜索好友列表
                    mPresenter.getSearchFriendList(IOUtils.getToken(getContext()), friendName, page);
                } else {
                    Toast.makeText(mContext, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        }
    }

    public class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.toString().trim().length() == 0) {
                Toast.makeText(mContext, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
            } else {
                friendName = charSequence.toString();
                mPresenter.getSearchFriendList(IOUtils.getToken(getContext()), friendName, page);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    public void initAdapter() {
        mItemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        };
        mAdapter = new WrapAdapter<FriendListModel.ListItem>(mContext, mList, R.layout.friend_group_item) {

            @Override
            public void convert(final ViewHolder helper, final FriendListModel.ListItem item) {
                final CheckBox checkTv = helper.getView(R.id.item_selector);
                String avatarUrl = URLUtil.builderImgUrl(item.avatar, 144, 144);
                SimpleDraweeView avatar = helper.getView(R.id.tagHeaderImg);
                avatar.setImageURI(avatarUrl);
                helper.setText(R.id.item_nickname, item.name);
                helper.setText(R.id.item_source, item.source);
                //存储选中位置
                checkTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkTv.isChecked()) {
                            mList.get(helper.getPosition()).setSelect(true);
                        } else {
                            mList.get(helper.getPosition()).setSelect(false);
                        }
                    }
                });

            }
        };
    }
}
