package com.tgf.kcwc.businessconcerns;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.CommonAdapter;
import com.tgf.kcwc.adapter.HistoryCursorAdapter;
import com.tgf.kcwc.adapter.OnItemClickListener;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.callback.OnSingleClickListener;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.FriendGroupingModel;
import com.tgf.kcwc.mvp.model.FriendListModel;
import com.tgf.kcwc.mvp.presenter.BusinessAttentionPresenter;
import com.tgf.kcwc.mvp.view.BusinessAttentionView;
import com.tgf.kcwc.seek.RecordSQLiteOpenHelper;
import com.tgf.kcwc.seek.SeekDetailsActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.StringUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @anthor noti
 * @time 2017/8/8 13:59
 * 好友搜索
 */

public class FriendSeekActivity extends BaseActivity implements View.OnClickListener,BusinessAttentionView{
    //返回
    private TextView back;
    private TextView historyClear;
    private LinearLayout historyLl;
    private EditText searchEt;
    private ImageView searchDelete;
    private ImageView searchIcon;
    //空视图
    private LinearLayout emptyLl;
    private ListView historyList;
    private HistoryCursorAdapter historyAdapter;

    BusinessAttentionPresenter mPresenter;
    ArrayList<FriendListModel.ListItem> friendList = new ArrayList<>();

    private ListView searchList;
    private WrapAdapter searchAdapter;
    private AdapterView.OnItemClickListener itemClickListener;
    private RecordSQLiteOpenHelper helper = new RecordSQLiteOpenHelper(this);
    private SQLiteDatabase db;

    private String friendName;
    private int page = 1;
    private boolean isRefresh;

    @Override
    protected void setUpViews() {
        searchEt = (EditText) findViewById(R.id.et_search);
        searchDelete = (ImageView) findViewById(R.id.seek_delete);
        searchIcon = (ImageView) findViewById(R.id.seek_icon);
        historyLl = (LinearLayout) findViewById(R.id.hend);
        emptyLl = (LinearLayout) findViewById(R.id.empty_hint_layout);
        back = (TextView) findViewById(R.id.seek_back);
        historyClear = (TextView) findViewById(R.id.tv_clear);
        historyList = (ListView) findViewById(R.id.history_list_view);
        searchList = (ListView) findViewById(R.id.search_list_view);
        searchDelete.setOnClickListener(this);
        back.setOnClickListener(this);
        historyClear.setOnClickListener(this);
        searchEt.setOnKeyListener(new MyKeyListener());
        searchEt.addTextChangedListener(new MyTextWatcher());
        initRefreshLayout(mBGDelegate);
        mPresenter = new BusinessAttentionPresenter();
        mPresenter.attachView(this);
        queryData("");
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_seek);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter!=null){
            mPresenter.detachView();
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void showGrouping(ArrayList<FriendGroupingModel.ListItem> list) {

    }

    @Override
    public void showFriendList(ArrayList<FriendListModel.ListItem> list) {
        historyLl.setVisibility(View.GONE);
        searchList.setVisibility(View.VISIBLE);
        emptyLl.setVisibility(View.GONE);
        if (!isRefresh){
            friendList.clear();
        }
        if (list.size() != 0 && list != null) {
            friendList.addAll(list);
        } else {
            searchList.setVisibility(View.GONE);
            emptyLl.setVisibility(View.VISIBLE);
        }
        if (searchAdapter != null) {
            searchAdapter.notifyDataSetChanged();
        } else {
            initAdapter();
            searchList.setAdapter(searchAdapter);
            searchList.setOnItemClickListener(itemClickListener);
        }
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    public class MyTextWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            friendName = charSequence.toString();
            if (charSequence.toString().trim().length() == 0){
                searchIcon.setVisibility(View.VISIBLE);
                historyLl.setVisibility(View.VISIBLE);
                searchDelete.setVisibility(View.GONE);
                searchList.setVisibility(View.GONE);
                queryData("");
            }else {
                Log.e("TAG", "onTextChanged: "+friendName);
                searchIcon.setVisibility(View.GONE);
                historyLl.setVisibility(View.GONE);
                searchDelete.setVisibility(View.VISIBLE);
                searchList.setVisibility(View.VISIBLE);
                // TODO: 2017/8/9 搜索方法
                queryData(friendName);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
    public class MyKeyListener implements View.OnKeyListener{

        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {// 修改回车键功能
                // 先隐藏键盘
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                friendName = searchEt.getText().toString().trim();
                if (StringUtils.checkNull(friendName)) {
                    Log.e("TAG", "onKey: ");
                    //保存搜索关键字
                    saveKeyword(friendName);
                    mPresenter.getSearchFriendList(IOUtils.getToken(getContext()),friendName,page);
                } else {
                    Toast.makeText(mContext, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.seek_back:
                finish();
                break;
            case R.id.seek_delete:
                searchEt.setText("");
                break;
            case R.id.tv_clear:
                //删除表
                clearData();
                //查询默认数据
                queryData("");
                break;
        }
    }

    /**
     * 保存搜索关键字，存在即不用保存
     */
    public void saveKeyword(String name){
        boolean hasData = hasData(name);
        if (!hasData) {
            insertData(name);
//            queryData("");
        }
    }
    /**
     * 查询数据库是否存在当前记录
     */
    private boolean hasData(String tempName) {
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from friend where name =?", new String[]{tempName});
        //判断是否有下一个
        return cursor.moveToNext();
    }
    /**
     * 插入某条数据
     */
    private void insertData(String tempName) {
        db = helper.getWritableDatabase();
        db.execSQL("insert into friend(name) values('" + tempName + "')");
        db.close();
    }
    /**
     * 删除某条数据
     */
    private void deleteData(String name) {
        db = helper.getWritableDatabase();
        db.execSQL("delete from friend where name = '" + name + "'");
        db.close();
    }
    /**
     * 清空数据
     */
    private void clearData() {
        //数据库不存在即调用oncreate创建
        db = helper.getWritableDatabase();
        db.execSQL("delete from friend");
        db.close();
    }
    /**
     * 模糊查询数据并设置数据
     */
    private void queryData(String tempName) {
        final Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from friend where name like '%" + tempName + "%' order by id desc ", null);
        historyAdapter = new HistoryCursorAdapter(mContext,R.layout.seek_history_item, cursor, new String[]{"name"},
                new int[]{R.id.name}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        historyAdapter.setOnClearClickListener(new HistoryCursorAdapter.OnClearClickListener() {
            @Override
            public void onClearClick(String position) {
                deleteData(position);
//                cursor.requery();
                queryData("");
            }
        });
        historyAdapter.setOnItemClickListener(new HistoryCursorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String str) {
                friendName = str;
                searchEt.setText(friendName);
                mPresenter.getSearchFriendList(IOUtils.getToken(getContext()),friendName,page);
            }
        });
        historyList.setAdapter(historyAdapter);
        historyAdapter.notifyDataSetChanged();
        historyLl.setVisibility(View.VISIBLE);
        searchList.setVisibility(View.GONE);
        emptyLl.setVisibility(View.GONE);
    }
    public void initAdapter(){
        itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // TODO: 2017/8/7 跳转详情
                int friendId = friendList.get(i).id;//好友id
                Intent intent = new Intent(mContext,CustomerDetailActivity.class);
                intent.putExtra(Constants.IntentParams.DATA,friendId);
                startActivity(intent);
            }
        };
        searchAdapter = new WrapAdapter<FriendListModel.ListItem>(this, friendList, R.layout.friend_list_item) {
            @Override
            public void convert(ViewHolder helper, final FriendListModel.ListItem item) {
                //缺性别
                String avatarUrl = URLUtil.builderImgUrl(item.avatar, 144, 144);
                SimpleDraweeView avatar = helper.getView(R.id.tagHeaderImg);
                avatar.setImageURI(avatarUrl);
                helper.setText(R.id.item_nickname, item.name);
                helper.setText(R.id.item_source, item.source);
                ImageView imageView = helper.getView(R.id.item_relation);
                imageView.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    protected void onSingleClick(View view) {
                        if (item.isAllot == 0) {

                        } else if (item.isAllot == 1) {

                        }
                    }
                });
            }
        };
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
        mPresenter.getSearchFriendList(IOUtils.getToken(getContext()),friendName,page);
    }
}
