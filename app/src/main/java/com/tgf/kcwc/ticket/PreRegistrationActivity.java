package com.tgf.kcwc.ticket;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.tauth.Tencent;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.AutoScrollAdapter;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.app.SelectBrandActivity;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.ApplyTicketModel;
import com.tgf.kcwc.mvp.model.Form;
import com.tgf.kcwc.mvp.model.PreTicketModel;
import com.tgf.kcwc.mvp.model.TicketDescModel;
import com.tgf.kcwc.mvp.presenter.ApplyTicketPresenter;
import com.tgf.kcwc.mvp.presenter.PreRegTicketPresenter;
import com.tgf.kcwc.mvp.view.PreRegTicketView;
import com.tgf.kcwc.mvp.view.WrapApplyTicketView;
import com.tgf.kcwc.share.OpenShareUtil;
import com.tgf.kcwc.share.SinaWBCallback;
import com.tgf.kcwc.task.AddressPickTask;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.util.WebviewUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.OpenShareWindow;
import com.tgf.kcwc.view.autoscrolltext.AutoCircleScrollListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.NumberPicker;
import cn.qqtheme.framework.picker.OptionPicker;

/**
 * Author：Jenny
 * Date:2017/2/16 11:08
 * E-mail：fishloveqin@gmail.com
 * 预报名领取赠票
 */

public class PreRegistrationActivity extends BaseActivity
        implements PreRegTicketView<PreTicketModel> {

    protected TextView applyTotalTv;
    protected AutoCircleScrollListView applyList;
    protected WebView webView;
    private SimpleDraweeView mImg;
    private TextView mName;
    private TextView mDesc;
    private TextView mTicketTitle;
    private TextView mErrorMsg;
    private Button mResetBtn;
    private TextView mTicketType;
    private ListView mList;
    private ScrollView mScrollView;
    private RelativeLayout mTypeLayout, mErrorLayout, mCommitLayout,
            mTitleLayout;
    private Button mCommitBtn;
    private LinearLayout mContentLayout;
    private PreRegTicketPresenter mPresenter;
    private PreRegTicketPresenter mApplyStatusPresenter;
    private PreRegTicketPresenter mCommitFormsPresenter;
    private ApplyTicketPresenter mGetBasePresenter;
    private BaseAdapter mAdapter;

    private List<Form> mItems = null;
    private String applyType = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mExId = intent.getIntExtra(Constants.IntentParams.ID, -1);
        mExName = intent.getStringExtra(Constants.IntentParams.DATA);
        mExCover = intent.getStringExtra(Constants.IntentParams.DATA2);
        openShareWindow = new OpenShareWindow(PreRegistrationActivity.this);
        setContentView(R.layout.activity_pre_reg_ticket);

    }

    @Override
    protected void onResume() {
        super.onResume();


        if (brandIndex != -1) {
            String content = KPlayCarApp.getValue(Constants.KeyParams.PRE_REG_SELECT_MODEL_VALUE);
            if (!TextUtils.isEmpty(content)) {
                mItems.get(brandIndex).desc = content;
                mAdapter.notifyDataSetChanged();
            }


        }
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText(mExName + "");

        function.setImageResource(R.drawable.btn_share_selector);
        function.setVisibility(View.VISIBLE);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = "看车玩车";
                final String content = mExName;
                final String cover = URLUtil.builderImgUrl(mExCover);

                openShareWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {

                        switch (position) {

                            case 0:
                                OpenShareUtil.sendWXMoments(mContext, mGlobalContext.getMsgApi(),
                                        title, content);
                                break;
                            case 1:
                                OpenShareUtil.sendWX(mContext, mGlobalContext.getMsgApi(), title,
                                        content);
                                break;
                            case 2:
                                mWbHandler = OpenShareUtil.shareSina(PreRegistrationActivity.this,
                                        title, content);
                                break;

                            case 3:
                                ArrayList<String> url = new ArrayList<String>();
                                url.add(cover);
                                OpenShareUtil.shareToQzone(mGlobalContext.getTencent(),
                                        PreRegistrationActivity.this, mBaseUIListener, url, title,
                                        content);
                                break;
                            case 4:
                                OpenShareUtil.shareToQQ2(mGlobalContext.getTencent(),
                                        PreRegistrationActivity.this, mBaseUIListener,
                                        title, cover,
                                        "您身边的超级汽车生活社区！看资讯、拿优惠、分享精彩、结交朋友!",
                                        cover);
                                break;

                        }
                    }
                });
                openShareWindow.show(PreRegistrationActivity.this);

            }
        });
    }

    private WbShareHandler mWbHandler;


    @Override
    protected void setUpViews() {
        initView();
    }

    private OpenShareWindow openShareWindow = null;
    private int mExId;
    private String mExName;
    private String mExCover;
    private int ticketDescType = -1;

    private void initView() {
        mImg = (SimpleDraweeView) findViewById(R.id.img);
        mImg.setImageURI(Uri.parse(URLUtil.builderImgUrl(mExCover)));
        mName = (TextView) findViewById(R.id.name);
        mDesc = (TextView) findViewById(R.id.desc);
        mErrorMsg = (TextView) findViewById(R.id.errorMsg);
        mResetBtn = (Button) findViewById(R.id.resetBtn);
        mResetBtn.setOnClickListener(this);
        mTicketTitle = (TextView) findViewById(R.id.title);
        mTicketType = (TextView) findViewById(R.id.ticketType);
        mList = (ListView) findViewById(R.id.list);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mErrorLayout = (RelativeLayout) findViewById(R.id.errorLayout);
        mTypeLayout = (RelativeLayout) findViewById(R.id.typeLayout);
        mCommitLayout = (RelativeLayout) findViewById(R.id.commitLayout);
        mContentLayout = findViewById(R.id.contentLayout);
        mTitleLayout = (RelativeLayout) findViewById(R.id.titleLayout);
        mCommitBtn = (Button) findViewById(R.id.commitBtn);
        mCommitBtn.setOnClickListener(this);
        mPresenter = new PreRegTicketPresenter();
        mApplyStatusPresenter = new PreRegTicketPresenter();
        mApplyStatusPresenter.attachView(mApplyView);
        mApplyStatusPresenter.getApplyStatus(mExId + "", "3", IOUtils.getToken(mContext));
        mPresenter.attachView(this);
        mCommitFormsPresenter = new PreRegTicketPresenter();
        mCommitFormsPresenter.attachView(mCommitFormsView);
        //mPresenter.getPreRegTicketInfos("", "", "", "", "", IOUtils.getToken(mContext));
        applyTotalTv = (TextView) findViewById(R.id.applyTotalTv);
        applyList = (AutoCircleScrollListView) findViewById(R.id.applyList);
        webView = (WebView) findViewById(R.id.webView);
        mGetBasePresenter = new ApplyTicketPresenter();
        mGetBasePresenter.attachView(wrapApplyTicketView);

        mGetBasePresenter.getApplyList(builderReqParams(false, null),true);
        ticketDescType = 1;
        mGetBasePresenter.getTicketDescInfo(builderReqParams(true, Constants.Types.APPLY_TICKET + ""));

    }


    WrapApplyTicketView wrapApplyTicketView = new WrapApplyTicketView() {
        @Override
        public void setLoadingIndicator(boolean active) {

        }

        @Override
        public void showLoadingTasksError() {

        }

        @Override
        public Context getContext() {
            return mContext;
        }

        @Override
        public void showApplyList(ApplyTicketModel applyModel) {

            applyTotalTv.setText("已报名: " + applyModel.count + "人");

            wrapperAutoScrollItems(applyModel);

        }

        @Override
        public void showTicketDesc(TicketDescModel model) {


            if (ticketDescType == 1) {
                webView.getSettings().setDefaultTextEncodingName("UTF-8");
                webView.loadDataWithBaseURL("", WebviewUtil.getHtmlData(model.information), "text/html", "UTF-8", "");
            } else if (ticketDescType == 2) {
                showApplySuccess(model);
            }
        }

    };

    private Map<String, String> builderReqParams(boolean isType, String typeValue) {
//http://car.i.cacf.cn/ticket/api/getFavText?token=C9Jmfnl1IQKMCWNg7ctkq5zIeEvg1eF9&apply_type=1&event_id=2
        //http://car.i.cacf.cn/cert/api/applyLists?token=C9Jmfnl1IQKMCWNg7ctkq5zIeEvg1eF9&event_id=257

        Map<String, String> params = new HashMap<String, String>();
        params.put("token", IOUtils.getToken(mContext));
        if (isType) {
            params.put("type", typeValue);
        } else {
            params.put("apply_type", applyType);
        }

        params.put("event_id", mExId + "");
        return params;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mWbHandler != null) {
            mWbHandler.doResultIntent(intent, new SinaWBCallback(mContext));
        }
    }

    /**
     * 使ScrollView指向底部
     */
    private void changeScrollView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0, mScrollView.getHeight());
            }
        }, 300);
    }

    private PreRegTicketView<DataItem> mApplyView = new PreRegTicketView<DataItem>() {
        @Override
        public void showData(DataItem dataItem) {

            int code = dataItem.code;

            switch (code) {

                case Constants.NetworkStatusCode.SUCCESS:

                    mContentLayout.setVisibility(
                            View.VISIBLE);
                    mTypeLayout.setVisibility(
                            View.VISIBLE);
                    mErrorLayout.setVisibility(
                            View.GONE);
                    mCommitLayout.setVisibility(
                            View.VISIBLE);
                    mPresenter
                            .getPreRegTicketInfos(
                                    mExId + "", "2",
                                    "3", "", "",
                                    IOUtils.getToken(
                                            mContext));
                    break;
                case Constants.NetworkStatusCode.STATUS_CODE4:
                case Constants.NetworkStatusCode.STATUS_CODE2:
                    mTypeLayout.setVisibility(
                            View.GONE);
                    mContentLayout.setVisibility(
                            View.GONE);
                    mErrorLayout.setVisibility(
                            View.VISIBLE);
                    mErrorMsg.setVisibility(
                            View.VISIBLE);
                    mCommitLayout.setVisibility(
                            View.GONE);
                    mErrorMsg
                            .setText(String.format(
                                    mRes.getString(
                                            R.string.ticket_apply_msg2),
                                    dataItem.msg));
                    mErrorMsg.setTextColor(
                            mRes.getColor(
                                    R.color.text_color12));
                    mResetBtn.setVisibility(
                            View.VISIBLE);
                    mResetBtn
                            .setOnClickListener(
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            mScrollView
                                                    .setVisibility(
                                                            View.VISIBLE);
                                            mTypeLayout
                                                    .setVisibility(
                                                            View.VISIBLE);
                                            mContentLayout.setVisibility(View.VISIBLE);
                                            mErrorLayout
                                                    .setVisibility(
                                                            View.GONE);
                                            mCommitLayout
                                                    .setVisibility(
                                                            View.VISIBLE);
                                            mPresenter
                                                    .getPreRegTicketInfos(
                                                            mExId + "",
                                                            "2",
                                                            "3",
                                                            "",
                                                            "",
                                                            IOUtils
                                                                    .getToken(
                                                                            mContext));

                                        }
                                    });
                    break;

                case Constants.NetworkStatusCode.STATUS_CODE3:
                    mTypeLayout.setVisibility(
                            View.GONE);
                    mContentLayout.setVisibility(
                            View.GONE);
                    mErrorLayout.setVisibility(
                            View.VISIBLE);
                    mErrorMsg.setVisibility(
                            View.VISIBLE);
                    mErrorMsg.setText(
                            dataItem.msg + "");
                    mErrorMsg.setTextColor(
                            mRes.getColor(
                                    R.color.text_more));
                    mCommitLayout.setVisibility(
                            View.GONE);
                    break;

                default:

                    CommonUtils.showToast(mContext, dataItem.msg);
                    break;
            }

        }
    };

    private PreRegTicketView<DataItem> mCommitFormsView = new PreRegTicketView<DataItem>() {
        @Override
        public void showData(DataItem dataItem) {

            int code = dataItem.code;

            switch (code) {

                case 0:

                    ticketDescType = 2;
                    mGetBasePresenter.getTicketDescInfo(builderReqParams(true, Constants.Types.APPLY_TICKET_SUCCESS + ""));
                    break;
                case 10022:
                    CommonUtils.showToast(
                            mContext, dataItem.msg);
                    break;

                case 20006:
                    break;
            }

        }
    };

    private void showApplySuccess(TicketDescModel model) {


        mContentLayout.setVisibility(View.GONE);
        mTitleLayout.setVisibility(View.GONE);
        mTypeLayout.setVisibility(View.GONE);
        mCommitLayout.setVisibility(View.GONE);
        findViewById(R.id.splitView).setVisibility(View.GONE);
        LinearLayout successLayout = findViewById(R.id.successLayout);

        WebView ticketDescWebView = findViewById(R.id.ticketDescWebView);
        successLayout.setVisibility(View.VISIBLE);

        ticketDescWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        ticketDescWebView.loadDataWithBaseURL("", WebviewUtil.getHtmlData(model.information), "text/html", "UTF-8", "");
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {

            case R.id.commitBtn:
                mCommitFormsPresenter.commitForms(builderFormsDatas());
                break;

            case R.id.typeLayout:

                Intent intent = new Intent();
                intent.setClass(mContext, TicketCategoryActivity.class);
                intent.putParcelableArrayListExtra(Constants.IntentParams.DATA, mTypes);
                intent.putExtra(Constants.IntentParams.DATA2, mSelect);
                startActivityForResult(intent, 2);
                break;
        }

    }

    private Map<String, String> builderFormsDatas() {
        //company=&brand=&dept=&tf_id=&age=&car=&name=&sex=&industry=&location=&address=&price=&position=
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.CheckInFormKey.TOKEN, IOUtils.getToken(mContext));
        params.put(Constants.CheckInFormKey.TF_ID, mTypes.get(mSelect).id + "");
        for (Form form : mItems) {

            String filed = form.field;
            switch (filed) {
                case Constants.CheckInFormKey.NAME:
                    params.put(Constants.CheckInFormKey.NAME, form.desc);
                    break;
                case Constants.CheckInFormKey.SEX:
                    params.put(Constants.CheckInFormKey.SEX, form.desc);
                    break;
                case Constants.CheckInFormKey.AGE:
                    params.put(Constants.CheckInFormKey.AGE, form.desc);
                    break;
                case Constants.CheckInFormKey.LOCATION:
                    params.put(Constants.CheckInFormKey.LOCATION, form.desc);
                    break;
                case Constants.CheckInFormKey.CAR:
                    params.put(Constants.CheckInFormKey.CAR, form.desc);
                    break;
                case Constants.CheckInFormKey.BRAND:
                    params.put(Constants.CheckInFormKey.BRAND, form.desc);
                    break;
                case Constants.CheckInFormKey.ADDRESS:
                    params.put(Constants.CheckInFormKey.ADDRESS, form.desc);
                    break;
                case Constants.CheckInFormKey.AREA:
                    params.put(Constants.CheckInFormKey.AREA, form.desc);
                    break;
                case Constants.CheckInFormKey.SERIES:
                    params.put(Constants.CheckInFormKey.SERIES, form.desc);
                    break;
                case Constants.CheckInFormKey.BRAND_SUB:
                    params.put(Constants.CheckInFormKey.BRAND_SUB, form.desc);
                    break;
                case Constants.CheckInFormKey.CITY:
                    params.put(Constants.CheckInFormKey.CITY, form.desc);
                    break;
                case Constants.CheckInFormKey.COMPANY:
                    params.put(Constants.CheckInFormKey.COMPANY, form.desc);
                    break;
                case Constants.CheckInFormKey.DEPT:
                    params.put(Constants.CheckInFormKey.DEPT, form.desc);
                    break;
                case Constants.CheckInFormKey.INDUSTRY:
                    params.put(Constants.CheckInFormKey.INDUSTRY, form.desc);
                    break;
                case Constants.CheckInFormKey.PRICE:
                    params.put(Constants.CheckInFormKey.PRICE, form.desc);
                    break;
                case Constants.CheckInFormKey.PROVINCE:
                    params.put(Constants.CheckInFormKey.PROVINCE, form.desc);
                    break;
                case Constants.CheckInFormKey.QD_ID:
                    params.put(Constants.CheckInFormKey.QD_ID, form.desc);
                    break;
                case Constants.CheckInFormKey.VISIT_DATE:
                    params.put(Constants.CheckInFormKey.VISIT_DATE, form.desc);
                    break;
                case Constants.CheckInFormKey.POSITION:
                    params.put(Constants.CheckInFormKey.POSITION, form.desc);
                    break;
            }

        }
        return params;
    }

    @Override
    public void showData(PreTicketModel preTicketModel) {


        mTypes = preTicketModel.list;
        if (mTypes.size() == 0) {
            mTypeLayout.setVisibility(
                    View.GONE);
            mContentLayout.setVisibility(
                    View.GONE);
            mErrorLayout.setVisibility(
                    View.VISIBLE);
            mErrorMsg.setVisibility(
                    View.VISIBLE);
            mErrorMsg.setText(
                    "暂无可申领的门票!");
            mErrorMsg.setTextColor(
                    mRes.getColor(
                            R.color.text_more));
            mCommitLayout.setVisibility(
                    View.GONE);
        } else {
            mTypeLayout.setVisibility(View.VISIBLE);
            mContentLayout.setVisibility(View.VISIBLE);
            mErrorLayout.setVisibility(View.GONE);
            mErrorMsg.setVisibility(View.GONE);
            mCommitLayout.setVisibility(View.VISIBLE);
            mTypeLayout.setOnClickListener(this);

            List<PreTicketModel.TicketTypeBean> types = mTypes;
            String[] items = new String[types.size()];
            int index = 0;
            for (PreTicketModel.TicketTypeBean type : types) {
                items[index] = type.ticketName;
                index++;
            }
            mTicketType.setText(items[mSelect]);
            mTypeLayout.setTag(items);
            String remark = preTicketModel.list.get(mSelect).info.remarks;
            showFormData();
        }

    }

    private int mSelect = 0;

    /**
     * 显示表单数据
     */
    private void showFormData() {
        if (mTypes != null && mTypes.size() > 0) {
            mItems = mTypes.get(mSelect).items;
            for (Form form : mItems) {

                switch (form.field) {
                    case Constants.CheckInFormKey.NAME:
                    case Constants.CheckInFormKey.ADDRESS:
                    case Constants.CheckInFormKey.COMPANY:
                    case Constants.CheckInFormKey.DEPT:
                    case Constants.CheckInFormKey.POSITION:

                        form.layoutId = R.layout.base_info_item_1;
                        form.viewTypeId = 0;
                        break;
                    case Constants.CheckInFormKey.SEX:
                        form.layoutId = R.layout.base_info_item_2;
                        form.viewTypeId = 1;
                        break;
                    case Constants.CheckInFormKey.CAR:
                    case Constants.CheckInFormKey.AGE:
                    case Constants.CheckInFormKey.LOCATION:
                    case Constants.CheckInFormKey.BRAND:
                    case Constants.CheckInFormKey.SERIES:
                    case Constants.CheckInFormKey.VISIT_DATE:
                    case Constants.CheckInFormKey.INDUSTRY:
                    case Constants.CheckInFormKey.PRICE:
                        form.layoutId = R.layout.base_info_item_3;
                        form.viewTypeId = 2;
                        break;

                }
            }
            mTicketType.setText(mTypes.get(mSelect).ticketName);

            mAdapter = new PreTicketAdapter();
            mList.setOnItemClickListener(mPreTicketItemListener);
            mList.setAdapter(mAdapter);
            ViewUtil.setListViewHeightBasedOnChildren(mList);
        }

    }

    /**
     * 判断是否还有必填项
     *
     * @return
     */
    private boolean hadRequired() {

        for (Form form : mItems) {

            if (form.require == Constants.CheckInFormKey.IS_REQUIRE) {

                if (TextUtils.isEmpty(form.desc)) {

                    return true;
                }
            }

        }
        return false;
    }

    private void setCommBtnStatus() {
        if (hadRequired()) {
            mCommitBtn.setEnabled(false);
            mCommitBtn.setTextColor(mRes.getColor(R.color.text_color));
        } else {
            mCommitBtn.setEnabled(true);
            mCommitBtn.setTextColor(mRes.getColor(R.color.white));
        }
    }

    private int brandIndex = -1;
    private AdapterView.OnItemClickListener mPreTicketItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent,
                                View view,
                                int position,
                                long id) {

            Form form = (Form) parent
                    .getAdapter()
                    .getItem(position);
            setCommBtnStatus();
            switch (form.field) {
                case Constants.CheckInFormKey.AGE:
                    //                                                                                   onAgePicker(
                    //                                                                                       position);

                    onOptionPicker(
                            position);
                    break;
                case Constants.CheckInFormKey.LOCATION:
                    onAddressPicker(
                            position);
                    break;

                case Constants.CheckInFormKey.INDUSTRY:
                    onOptionIndustryPicker(position);
                    break;
                case Constants.CheckInFormKey.BRAND:
                case Constants.CheckInFormKey.SERIES:
                case Constants.CheckInFormKey.CAR:
                    brandIndex = position;
                    KPlayCarApp.putValue(Constants.KeyParams.PRE_REG_SELECT_MODEL, form.field);
                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                    args.put(
                            Constants.IntentParams.MODULE_TYPE,
                            Constants.ModuleTypes.PRE_REG_TICKET);
                    CommonUtils
                            .startNewActivity(
                                    PreRegistrationActivity.this,
                                    args,
                                    SelectBrandActivity.class);
                    break;
                case Constants.CheckInFormKey.PRICE:

                    onOptionBudgetPicker(position);
                    break;
            }

        }
    };

    /**
     * 年龄选择菜单项
     *
     * @param positon
     */
    private void onAgePicker(final int positon) {
        NumberPicker picker = new NumberPicker(this);
        picker.setWidth(picker.getScreenWidthPixels() / 2);
        picker.setCycleDisable(false);
        picker.setLineVisible(false);
        picker.setOffset(2);//偏移量
        picker.setRange(18, 100, 1);//数字范围
        picker.setSelectedItem(18);
        picker.setLabel("岁");
        picker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
            @Override
            public void onNumberPicked(int index, Number item) {
                mItems.get(positon).desc = item.intValue() + "";

                mAdapter.notifyDataSetChanged();
            }
        });
        picker.show();
    }

    private void onOptionPicker(final int position) {
        OptionPicker picker = new OptionPicker(this, ageGroups);
        picker.setCycleDisable(true);
        picker.setLineVisible(false);
        picker.setShadowVisible(true);
        picker.setTextSize(11);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                mItems.get(position).desc = item + "";

                mAdapter.notifyDataSetChanged();
            }
        });
        picker.show();
    }


    private void onOptionIndustryPicker(final int position) {
        OptionPicker picker = new OptionPicker(this, industrys);
        picker.setCycleDisable(true);
        picker.setLineVisible(false);
        picker.setShadowVisible(true);
        picker.setTextSize(11);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                mItems.get(position).desc = item + "";

                mAdapter.notifyDataSetChanged();
            }
        });
        picker.show();
    }


    private void onOptionBudgetPicker(final int position) {
        OptionPicker picker = new OptionPicker(this, budgets);
        picker.setCycleDisable(true);
        picker.setLineVisible(false);
        picker.setShadowVisible(true);
        picker.setTextSize(11);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                mItems.get(position).desc = item + "";

                mAdapter.notifyDataSetChanged();
            }
        });
        picker.show();
    }

    /**
     * 三级位置菜单项
     *
     * @param position
     */
    private void onAddressPicker(final int position) {
        AddressPickTask task = new AddressPickTask(this);
        task.setHideProvince(false);
        task.setHideCounty(false);
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {
                CommonUtils.showToast(mContext, "数据初始化失败");
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                StringBuilder stringBuilder = new StringBuilder();
                if (county == null) {
                    CommonUtils.showToast(mContext, province.getAreaName() + city.getAreaName());
                } else {
                    stringBuilder.append(province.getAreaName());
                    if (province.getAreaName().indexOf("市") < 0) {
                        stringBuilder.append(",");
                        stringBuilder.append(city.getAreaName());
                    }
                    stringBuilder.append(",");
                    stringBuilder.append(county.getAreaName());
                    mItems.get(position).desc = stringBuilder.toString();

                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        task.execute("重庆市", "重庆市", "九龙坡区");
    }

    private ArrayList<PreTicketModel.TicketTypeBean> mTypes = null;


    private void wrapperAutoScrollItems(ApplyTicketModel applyModel) {

        int count = applyModel.count;
        List<ApplyTicketModel.User> users = applyModel.list;

        if (count < INIT_ITEMS) {

            for (int i = 0; i < INIT_ITEMS - count; i++) {

                users.add(new ApplyTicketModel.User());
            }
        }
        AutoScrollAdapter autoScrollAdapter = new AutoScrollAdapter(users,mContext);
        applyList.setAdapter(autoScrollAdapter);
        applyList.startScroll();
    }

    private static final int INIT_ITEMS = 5;//默认初始列表显示5条数据


    private class PreTicketAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public int getItemViewType(int position) {
            return mItems.get(position).viewTypeId;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final Form form = mItems.get(position);
            int id = form.layoutId;
            WrapAdapter.ViewHolder holder = null;
            switch (id) {
                case R.layout.base_info_item_1:

                    holder = WrapAdapter.ViewHolder.get(mContext, convertView, parent,
                            R.layout.base_info_item_1, position);
                    break;

                case R.layout.base_info_item_2:
                    holder = WrapAdapter.ViewHolder.get(mContext, convertView, parent,
                            R.layout.base_info_item_2, position);
                    break;

                case R.layout.base_info_item_3:
                    holder = WrapAdapter.ViewHolder.get(mContext, convertView, parent,
                            R.layout.base_info_item_3, position);
                    break;
            }

            switch (id) {
                case R.layout.base_info_item_1:

                    final EditText editText = holder.getView(R.id.content);
                    editText.clearFocus();
                    if (Constants.CheckInFormKey.PRICE.equals(form.field)) {

                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    } else {
                        editText.setInputType(InputType.TYPE_CLASS_TEXT);
                    }
                    if (editText.getTag() instanceof TextWatcher) {

                        editText.removeTextChangedListener((TextWatcher) editText.getTag());
                    }
                    TextWatcher watcher = new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                      int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before,
                                                  int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            mItems.get(position).desc = s.toString();
                            setCommBtnStatus();
                        }
                    };
                    editText.addTextChangedListener(watcher);
                    editText.setTag(watcher);
                    editText.setText(mItems.get(position).desc);
                    break;

                case R.layout.base_info_item_2:

                    RadioGroup radioGroup = holder.getView(R.id.radioGroup);
                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                            if (R.id.male == checkedId) {
                                form.desc = "男";
                            } else {
                                form.desc = "女";
                            }
                            setCommBtnStatus();
                        }
                    });

                    break;
                case R.layout.base_info_item_3:

                    TextView textView = holder.getView(R.id.desc);
                    textView.setText(mItems.get(position).desc);
                    break;
            }
            TextView tvName = holder.getView(R.id.title);
            tvName.setText(mItems.get(position).name);
            ImageView imageView = holder.getView(R.id.img);

            if (Constants.CheckInFormKey.IS_REQUIRE == form.require) {
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }

            return holder.getConvertView();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2 && RESULT_OK == resultCode) {

            int id = data.getIntExtra(Constants.IntentParams.ID, -1);
            if (id != -1) {
                mSelect = id;
            }
            showFormData();
        }


        if ((requestCode == Constants.InteractionCode.QQ_SHARE_REQUEST_CODE) || (requestCode == Constants.InteractionCode.QQ_ZONE_SHARE_REQUEST_CODE)) {

            Tencent.onActivityResultData(requestCode, resultCode, data, mBaseUIListener);
        }

    }


}
