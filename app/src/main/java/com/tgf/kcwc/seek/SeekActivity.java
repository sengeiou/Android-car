package com.tgf.kcwc.seek;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.mvp.model.SeekBean;
import com.tgf.kcwc.mvp.presenter.SeekPresenter;
import com.tgf.kcwc.mvp.view.SeekView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.StringUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MyListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/22.
 */

public class SeekActivity extends BaseActivity implements SeekView {
    private SeekPresenter mSeekPresenter;
    private EditText etSearch;
    private TextView tvTip;
    private MyListView listView;
    private MyListView searchlistView;
    private TextView tvClear;
    private TextView back;
    private LinearLayout hend;
    private LinearLayout emptyhintlayout;
    private RecordSQLiteOpenHelper helper = new RecordSQLiteOpenHelper(this);
    private SQLiteDatabase db;
    private BaseAdapter adapter;
    private WrapAdapter<SeekBean.DataList> mAdapter;
    private List<SeekBean.DataList> seekBeanList = new ArrayList<>();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSeekPresenter!=null){
            mSeekPresenter.detachView();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_seek);
        mSeekPresenter = new SeekPresenter();
        mSeekPresenter.attachView(this);
        // 初始化控件
        initView();

        // 清空搜索历史
        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
                queryData("");
            }
        });

        // 搜索框的键盘搜索键点击回调
        etSearch.setOnKeyListener(new View.OnKeyListener() {// 输入完后按键盘上的搜索键

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {// 修改回车键功能
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    String tempName = etSearch.getText().toString().trim();
                    if (StringUtils.checkNull(tempName)) {
                        mSeekPresenter.getsSearch(IOUtils.getToken(mContext), etSearch.getText().toString().trim());
                        setLoadingIndicator(true);
                    } else {
                        Toast.makeText(mContext, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
                    }
                    // TODO 根据输入的内容模糊查询商品，并跳转到另一个界面，由你自己去实现
                    //Toast.makeText(mContext, "clicked!", Toast.LENGTH_SHORT).show();

                }
                return false;
            }
        });

        // 搜索框的文本变化实时监听
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    tvTip.setText("搜索历史");
                    queryData("");
                } else {
                    tvTip.setText("搜索历史");
                    String tempName = etSearch.getText().toString();
                    // 根据tempName去模糊查询数据库中有没有数据
                    search(tempName);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.name);
                String name = textView.getText().toString();
                etSearch.setText(name);
                staractivity(name);
                // TODO 获取到item上面的文字，根据该关键字跳转到另一个页面查询，由你自己去实现
            }
        });

        // 第一次进入查询所有的历史记录
        queryData("");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void search(String name) {
        mSeekPresenter.getsSearchList(IOUtils.getToken(mContext), name);
    }

    public void staractivity(String name) {
        // 按完搜索键后将当前查询的关键字保存起来,如果该关键字已经存在就不执行保存
        boolean hasData = hasData(name);
        if (!hasData) {
            insertData(name);
            queryData("");
        }
        Map<String, Serializable> args = new HashMap<String, Serializable>();
        args.put("name", name);
        CommonUtils.startNewActivity(mContext, args, SeekDetailsActivity.class);
    }

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    /**
     * 插入数据
     */
    private void insertData(String tempName) {
        db = helper.getWritableDatabase();
        db.execSQL("insert into records(name) values('" + tempName + "')");
        db.close();
    }

    /**
     * 模糊查询数据
     */
    private void queryData(String tempName) {
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name like '%" + tempName + "%' order by id desc ", null);

        // 创建adapter适配器对象
        adapter = new SimpleCursorAdapter(this, R.layout.activity_seek_item, cursor, new String[]{"name"},
                new int[]{R.id.name}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        // 设置适配器
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        hend.setVisibility(View.VISIBLE);
        searchlistView.setVisibility(View.GONE);
        emptyhintlayout.setVisibility(View.GONE);
    }

    /**
     * .
     * 检查数据库中是否已经有该条记录
     */
    private boolean hasData(String tempName) {
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name =?", new String[]{tempName});
        //判断是否有下一个
        return cursor.moveToNext();
    }

    /**
     * 清空数据
     */
    private void deleteData() {
        db = helper.getWritableDatabase();
        db.execSQL("delete from records");
        db.close();
    }

    private void initView() {
        etSearch = (EditText) findViewById(R.id.et_search);
        tvTip = (TextView) findViewById(R.id.tv_tip);
        listView = (MyListView) findViewById(R.id.listView);
        searchlistView = (MyListView) findViewById(R.id.searchlistView);
        tvClear = (TextView) findViewById(R.id.tv_clear);
        back = (TextView) findViewById(R.id.back);
        hend = (LinearLayout) findViewById(R.id.hend);
        emptyhintlayout = (LinearLayout) findViewById(R.id.emptyhintlayout);

        // 调整EditText左边的搜索按钮的大小
        Drawable drawable = getResources().getDrawable(R.drawable.search_btn_selector);
        drawable.setBounds(0, 0, 60, 60);// 第一0是距左边距离，第二0是距上边距离，60分别是长宽
        etSearch.setCompoundDrawables(null, null, drawable, null);// 只放右边
    }

    @Override
    public void dataListSucceed(SeekBean seekBean) {
        if (seekBean.data.list != null && seekBean.data.list.size() != 0) {
            seekBeanList.clear();
            seekBeanList.addAll(seekBean.data.list);
            mAdapter = new WrapAdapter<SeekBean.DataList>(mContext, R.layout.activity_seek_item, seekBeanList) {
                @Override
                public void convert(ViewHolder helper, SeekBean.DataList item) {
                    ImageView imageView = helper.getView(R.id.img);
                    TextView name = helper.getView(R.id.name);
                    imageView.setVisibility(View.GONE);
                    name.setText(item.name);
                }
            };
            searchlistView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            searchlistView.setVisibility(View.VISIBLE);
            hend.setVisibility(View.GONE);
            emptyhintlayout.setVisibility(View.GONE);
            searchlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SeekBean.DataList dataList = seekBeanList.get(position);
                    staractivity(dataList.name);
                }
            });
        } else {
            searchlistView.setVisibility(View.VISIBLE);
            hend.setVisibility(View.GONE);
            emptyhintlayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void dataSucceed(SeekBean seekBean) {
        setLoadingIndicator(false);
        if (seekBean.data.list != null && seekBean.data.list.size() != 0) {

            String tempName = etSearch.getText().toString().trim();
            staractivity(tempName);
        } else {
            searchlistView.setVisibility(View.GONE);
            hend.setVisibility(View.GONE);
            emptyhintlayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void dataListDefeated(String msg) {
        CommonUtils.showToast(mContext, msg);
        setLoadingIndicator(false);
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
