package com.tgf.kcwc.me.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.AccountModel;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MyListView;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/8/22
 * @describle 账号与安全
 */
public class AccountActivity extends BaseActivity {
    private MyListView mListView;
    private WrapAdapter<AccountModel> mAdapter;
    private AdapterView.OnItemClickListener onItemClickListener;
    private ArrayList<AccountModel> mList = new ArrayList<>();

    private TextView addTv;
    private TextView wechatTv;
    private TextView qqTv;
    private TextView sinaTv;
    private String title;

    @Override
    protected void setUpViews() {
        addTv = (TextView) findViewById(R.id.account_wechat);
        qqTv = (TextView) findViewById(R.id.account_qq);
        sinaTv = (TextView) findViewById(R.id.account_sina);
        wechatTv.setOnClickListener(this);
        qqTv.setOnClickListener(this);
        sinaTv.setOnClickListener(this);

        mListView = (MyListView) findViewById(R.id.account_list_view);
        mAdapter = new WrapAdapter<AccountModel>(this, mList, R.layout.acount_item) {
            @Override
            public void convert(ViewHolder helper, AccountModel item) {
                //已绑定
                helper.getView(R.id.item_status);
                //手机号
                helper.getView(R.id.item_phone);
                //撤销绑定
                TextView cancelTv = helper.getView(R.id.item_cancel);
                cancelTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        title = "撤销绑定";
                        Intent intent = new Intent(AccountActivity.this,CancelReplaceActivity.class);
                        intent.putExtra(Constants.IntentParams.TITLE,title);
                        startActivity(intent);
                    }
                });
                //修改
                TextView modifyTv = helper.getView(R.id.item_modify);
                modifyTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        title = "更换手机绑定";
                        Intent intent = new Intent(AccountActivity.this,CancelReplaceActivity.class);
                        intent.putExtra(Constants.IntentParams.TITLE,title);
                        startActivity(intent);
                    }
                });
            }
        };
        onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        };
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(onItemClickListener);

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("账号与安全");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.account_add://添加手机绑定
                title = "更换手机绑定";
                Intent intent = new Intent(this,CancelReplaceActivity.class);
                intent.putExtra(Constants.IntentParams.TITLE,title);
                startActivity(intent);
                break;
            case R.id.account_wechat://添加微信绑定
                break;
            case R.id.account_qq://添加qq绑定
                break;
            case R.id.account_sina://添加新浪绑定
                break;
        }
    }
}
