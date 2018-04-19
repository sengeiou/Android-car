package com.tgf.kcwc.see.dealer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hedgehog.ratingbar.RatingBar;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.globalchat.Constant;
import com.tgf.kcwc.globalchat.GlobalChatActivity;
import com.tgf.kcwc.me.UserPageActivity;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.SalespersonModel;
import com.tgf.kcwc.mvp.presenter.SalespersonPresenter;
import com.tgf.kcwc.mvp.view.SalespersonView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SystemInvoker;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

/**
 * Author：Jenny
 * Date:2017/1/16 20:54
 * E-mail：fishloveqin@gmail.com
 */

public class SalespersonListActivity extends BaseActivity implements SalespersonView {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orgId = getIntent().getStringExtra(Constants.IntentParams.ID);
        setContentView(R.layout.activity_salesperson_layout);
    }

    private SalespersonPresenter mPresenter;
    private String orgId = "";
    private ListView mList;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("销售列表");
    }

    @Override
    protected void setUpViews() {
        mList = (ListView) findViewById(R.id.list);
        mList.setOnItemClickListener(mSalepersonItemClickListener);
        mPresenter = new SalespersonPresenter();
        mPresenter.attachView(this);
        mPresenter.loadSalesperson(orgId, IOUtils.getToken(mContext));
    }

    private AdapterView.OnItemClickListener mSalepersonItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Account.UserInfo userInfo = (Account.UserInfo) parent.getAdapter().getItem(position);

            Intent intent = new Intent();
            intent.putExtra(Constants.IntentParams.ID, userInfo.userId);
            intent.setClass(mContext, UserPageActivity.class);
            startActivity(intent);
        }
    };

    @Override
    public void showSales(SalespersonModel model) {

        mList.setAdapter(new WrapAdapter<Account.UserInfo>(mContext,
                R.layout.salespersion_list_item, model.users) {
            @Override
            public void convert(ViewHolder helper, final Account.UserInfo item) {

                helper.setText(R.id.name, item.nickName);
                TextView descText = helper.getView(R.id.desc);
                String desc = String.format(mRes.getString(R.string.salesperosn_desc),
                        item.fans_num, item.follow_num);
                CommonUtils.customDisplayText(desc, descText, mRes, R.color.text_color12);
                RatingBar ratingBar = helper.getView(R.id.ratingBar);
                ratingBar.setStar(Float.parseFloat(item.star));
                helper.setText(R.id.ratingText, item.star);
                SimpleDraweeView genderImg = helper.getView(R.id.genderImg);
                if (item.sex == 1) {
                    genderImg.setImageResource(R.drawable.icon_men);
                } else {
                    genderImg.setImageResource(R.drawable.icon_women);
                }

                SimpleDraweeView avatar = helper.getView(R.id.img);
                avatar.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.avatar, 144, 144)));
                helper.getView(R.id.msg).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent();
                        intent.putExtra(Constant.EXTRA_USER_ID, item.imId + "");
                        intent.putExtra(Constant.EXTRA_USER_NICKNAME, item.nickName);
                        intent.setClass(mContext, GlobalChatActivity.class);
                        //startActivity(intent);
                    }
                });
                helper.getView(R.id.tel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        SystemInvoker.launchDailPage(mContext, item.tel);
                    }
                });
            }
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
