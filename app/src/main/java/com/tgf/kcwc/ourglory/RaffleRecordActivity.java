package com.tgf.kcwc.ourglory;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hedgehog.ratingbar.RatingBar;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.MeCrunchiesBean;
import com.tgf.kcwc.mvp.model.RaffleRecordBean;
import com.tgf.kcwc.mvp.model.TransmitWinningCrunchiesBean;
import com.tgf.kcwc.mvp.presenter.MeRaffleRecordPresenter;
import com.tgf.kcwc.mvp.presenter.TransmitWinningCrunchiesPresenter;
import com.tgf.kcwc.mvp.presenter.TransmitWinningRaffleRecordPresenter;
import com.tgf.kcwc.mvp.view.MeRaffleRecordView;
import com.tgf.kcwc.mvp.view.TransmitWinningRaffleRecordView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.VacancyListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/10.
 */

public class RaffleRecordActivity extends BaseActivity implements MeRaffleRecordView {

    private MeRaffleRecordPresenter mMeRaffleRecordPresenter;
    private ListView mListView;
    private WrapAdapter<MeCrunchiesBean.DataList> mCrunchieslistviewAdapter;
    private List<MeCrunchiesBean.DataList> dataList = new ArrayList<>();
    private String type = "";

    private TextView mNumtext;
    private SimpleDraweeView mHead;
    private SimpleDraweeView mGender;
    private TextView mName;
    private TextView mIntegral;
    private TextView mTime;
    private LinearLayout melayout;
    private LinearLayout hendimag;

    private LinearLayout shengqianlayouts;
    private TextView shengqiantexts;

    private String typeString = "积分";
    private String typename = "";

    @Override
    protected void setUpViews() {
        type = getIntent().getStringExtra(Constants.IntentParams.ID);
        mListView = (ListView) findViewById(R.id.list);
        mNumtext = (TextView) findViewById(R.id.numtext);
        mHead = (SimpleDraweeView) findViewById(R.id.head);
        mGender = (SimpleDraweeView) findViewById(R.id.genderImg);
        mName = (TextView) findViewById(R.id.name);
        mIntegral = (TextView) findViewById(R.id.integral);
        mTime = (TextView) findViewById(R.id.time);
        melayout = (LinearLayout) findViewById(R.id.melayout);
        hendimag = (LinearLayout) findViewById(R.id.hendimag);
        shengqianlayouts = (LinearLayout) findViewById(R.id.shengqianlayouts);
        shengqiantexts = (TextView) findViewById(R.id.shengqiantexts);
        TextView mTitleText = (TextView) findViewById(R.id.title_bar_text);
        if (type.equals("1")) {
            shengqianlayouts.setVisibility(View.VISIBLE);
            mIntegral.setVisibility(View.GONE);
        } else {
            shengqianlayouts.setVisibility(View.GONE);
            mIntegral.setVisibility(View.VISIBLE);
        }
        switch (type) {
            case "1":
                melayout.setVisibility(View.VISIBLE);
                hendimag.setBackgroundResource(R.drawable.shengqiangaoshou);
                typeString = "-1";
                typename = "省钱高手";
                break;
            case "2":
                melayout.setVisibility(View.VISIBLE);
                hendimag.setBackgroundResource(R.drawable.guanxidashi);
                typeString = "关系维护值";
                typename = "关系大师";
                break;
            case "3":
                melayout.setVisibility(View.VISIBLE);
                hendimag.setBackgroundResource(R.drawable.huodonglingxiu);
                typeString = "活动发起数";
                typename = "活动领袖";
                break;
            case "4":
                melayout.setVisibility(View.VISIBLE);
                hendimag.setBackgroundResource(R.drawable.dengjipaihang);
                typeString = "积分";
                typename = "等级排行";
                break;
            case "5":
                melayout.setVisibility(View.VISIBLE);
                hendimag.setBackgroundResource(R.drawable.tuozhandaren);
                typeString = "推广贡献值";
                typename = "推广明星";
                break;
            case "6":
                melayout.setVisibility(View.GONE);
                typeString = "-2";
                typename = "十佳经销商";
                break;
            case "7":
                melayout.setVisibility(View.VISIBLE);
                hendimag.setBackgroundResource(R.drawable.yingxiangli);
                typeString = "影响力";
                typename = "影响力";
                break;
            case "8":
                melayout.setVisibility(View.GONE);
                typeString = "价值";
                typename = "最具价值群";
                break;
        }

        //榜单
        mCrunchieslistviewAdapter = new WrapAdapter<MeCrunchiesBean.DataList>(mContext,
                R.layout.activity_mecrunchies_item, dataList) {
            @Override
            public void convert(ViewHolder helper, MeCrunchiesBean.DataList item) {
                int position = helper.getPosition();
                TextView numtext = helper.getView(R.id.numtext);
                ImageView numimag = helper.getView(R.id.numimag);
                SimpleDraweeView head = helper.getView(R.id.head);
                SimpleDraweeView genderImg = helper.getView(R.id.genderImg);
                TextView name = helper.getView(R.id.name);
                TextView integral = helper.getView(R.id.integral);
                TextView xuwei = helper.getView(R.id.xuwei);

                LinearLayout gradelayout = helper.getView(R.id.gradelayout);
                RatingBar ratingBar = helper.getView(R.id.ratingBar);
                TextView gradetext = helper.getView(R.id.gradetext);

                LinearLayout shengqianlayout = helper.getView(R.id.shengqianlayout);
                LinearLayout noxuwei = helper.getView(R.id.noxuwei);
                TextView shengqiantext = helper.getView(R.id.shengqiantext);


                int nums = position + 1;
                numtext.setVisibility(View.GONE);
                numimag.setVisibility(View.GONE);
                if (nums == 1) {
                    numtext.setVisibility(View.GONE);
                    numimag.setVisibility(View.VISIBLE);
                    numimag.setImageResource(R.drawable.icon_gold);
                } else if (nums == 2) {
                    numtext.setVisibility(View.GONE);
                    numimag.setVisibility(View.VISIBLE);
                    numimag.setImageResource(R.drawable.icon_yin);
                } else if (nums == 3) {
                    numtext.setVisibility(View.GONE);
                    numimag.setVisibility(View.VISIBLE);
                    numimag.setImageResource(R.drawable.icon_tong);
                } else {
                    numtext.setVisibility(View.VISIBLE);
                    numimag.setVisibility(View.GONE);
                    numtext.setText(nums + "");
                }

                if (item.id == 0) {
                    genderImg.setVisibility(View.GONE);
                    noxuwei.setVisibility(View.GONE);
                    xuwei.setVisibility(View.VISIBLE);
                    head.setImageResource(R.drawable.xuweiyidai);
                } else {
                    genderImg.setVisibility(View.VISIBLE);
                    noxuwei.setVisibility(View.VISIBLE);
                    xuwei.setVisibility(View.GONE);
                    head.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.avatar, 144, 144)));
                    if (type.equals("6")) {
                        gradelayout.setVisibility(View.VISIBLE);
                        integral.setVisibility(View.GONE);
                        shengqianlayout.setVisibility(View.GONE);
                        setRatingScore(ratingBar, 5, (float) item.serviceScore, R.drawable.rating_bar_star_empty, R.drawable.icon_half_a_star_n, R.drawable.rating_bar_star_fill);
                        gradetext.setText(item.value);
                    } else if (type.equals("1")) {
                        gradelayout.setVisibility(View.GONE);
                        integral.setVisibility(View.GONE);
                        shengqianlayout.setVisibility(View.VISIBLE);
                        shengqiantext.setText(item.value);
                    } else {
                        gradelayout.setVisibility(View.GONE);
                        integral.setVisibility(View.VISIBLE);
                        shengqianlayout.setVisibility(View.GONE);
                        integral.setText(typeString + ": " + item.value);
                    }
                    name.setText(item.nickname);
                    if (type.equals("8")) {
                        genderImg.setVisibility(View.GONE);
                    } else {
                        genderImg.setVisibility(View.VISIBLE);
                    }
                    if (item.sex == 1) {
                        genderImg.setImageResource(R.drawable.icon_male);
                    } else {
                        genderImg.setImageResource(R.drawable.icon_famale);
                    }
                }
            }
        };
        mListView.setAdapter(mCrunchieslistviewAdapter);
        mMeRaffleRecordPresenter = new MeRaffleRecordPresenter();
        mMeRaffleRecordPresenter.attachView(this);
        mMeRaffleRecordPresenter.getIndexList(IOUtils.getToken(mContext), type);
    }

    private void setRatingScore(RatingBar ratingBar, int count, float star, int drawableEmptyId,
                                int drawableHalfId, int drawableFillId) {
        ratingBar.setStarEmptyDrawable(getResources().getDrawable(drawableEmptyId));
        ratingBar.setStarHalfDrawable(getResources().getDrawable(drawableHalfId));
        ratingBar.setStarFillDrawable(getResources().getDrawable(drawableFillId));
        ratingBar.setStarCount(count);
        ratingBar.setStarImageSize(star);
        ratingBar.setStar(star);
        ratingBar.halfStar(true);
        ratingBar.setmClickable(false);

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText(typename);
        function.setImageResource(R.drawable.btn_more);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merafflerecor);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }


    @Override
    public void dataListSucceed(MeCrunchiesBean meCrunchiesBean) {
        dataList.clear();
        dataList.addAll(meCrunchiesBean.data.list);
        int size = dataList.size();
        if (size < 10) {
            for (int i = 0; i < 10 - size; i++) {
                MeCrunchiesBean.DataList data = new MeCrunchiesBean.DataList();
                data.id = -1;
                dataList.add(data);
            }
        }
        mCrunchieslistviewAdapter.notifyDataSetChanged();
        MeCrunchiesBean.User user = meCrunchiesBean.data.user;
        mHead.setImageURI(Uri.parse(URLUtil.builderImgUrl(user.avatar, 144, 144)));
        mNumtext.setText(user.index + "");
        mName.setText(user.nickname);
        if (type.equals("1")) {
            shengqiantexts.setText("￥" + user.value);
        } else {
            mIntegral.setText(typeString + ": " + user.value);
        }
        mGender.setImageResource(R.drawable.icon_famale);
        if (user.sex == 1) {
            mGender.setImageResource(R.drawable.icon_male);
        } else {
            mGender.setImageResource(R.drawable.icon_famale);
        }
        mTime.setText("当前统计截止：" + meCrunchiesBean.data.time);
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

    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
