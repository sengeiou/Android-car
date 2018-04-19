package com.tgf.kcwc.certificate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
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
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.AutoScrollAdapter;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.FactoryBrandActivity;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.ApplyTicketModel;
import com.tgf.kcwc.mvp.model.CertResultModel;
import com.tgf.kcwc.mvp.model.CheckinTypeModel;
import com.tgf.kcwc.mvp.model.Form;
import com.tgf.kcwc.mvp.model.TicketDescModel;
import com.tgf.kcwc.mvp.presenter.ApplyTicketPresenter;
import com.tgf.kcwc.mvp.presenter.CertDataPresenter;
import com.tgf.kcwc.mvp.presenter.CheckinPresenter;
import com.tgf.kcwc.mvp.view.CertDataView;
import com.tgf.kcwc.mvp.view.CheckinTypeView;
import com.tgf.kcwc.mvp.view.WrapApplyTicketView;
import com.tgf.kcwc.task.AddressPickTask;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.util.WebviewUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.autoscrolltext.AutoCircleScrollListView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.NumberPicker;
import cn.qqtheme.framework.picker.OptionPicker;

/**
 * Author：Jenny
 * Date:2017/1/12 10:01
 * E-mail：fishloveqin@gmail.com
 */

public class CheckinActivity extends BaseActivity implements CheckinTypeView {

    private Button mCommitBtn;
    private ListView mList;

    private int mIndex = -1;
    private String mTitle = "";
    private TextView mTitleTv;

    private String[] mTitles = null;

    private BaseAdapter mAdapter;

    private List<Form> mItems = null;

    private CheckinPresenter mPresenter;

    private TextView mCertTypeTv;

    private RelativeLayout mTypeLayout, mTitleLayout;

    private LinearLayout mContentLayout;

    private String mId, mId2;
    private String mCover;

    private SimpleDraweeView mCoverImg;

    private CertDataPresenter mStatusPresenter;

    private ScrollView mScrollView;
    private RelativeLayout mBottomLayout;
    private RelativeLayout mErrorLayout;
    private ApplyTicketPresenter mGetBasePresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mTitle = intent.getStringExtra(Constants.IntentParams.DATA);
        mId = intent.getStringExtra(Constants.IntentParams.ID);
        mId2 = intent.getStringExtra(Constants.IntentParams.ID2);
        mCover = intent.getStringExtra(Constants.IntentParams.DATA2);
        setContentView(R.layout.activity_certificate_checkin);
        mStatusPresenter = new CertDataPresenter();
        mStatusPresenter.attachView(mStatusView);
        mStatusPresenter.getCertStatus("", mId, "2", IOUtils.getToken(mContext));
        mPresenter = new CheckinPresenter();
        mPresenter.attachView(this);

    }

    private CertDataView<DataItem> mStatusView = new CertDataView<DataItem>() {
        @Override
        public Context getContext() {
            return mContext;
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
        public void showData(DataItem item) {
            int code = item.code;
            switch (code) {
                case Constants.NetworkStatusCode.SUCCESS:
                    displayNormalViews(true);
                    mPresenter.loadFormsDatas(mId + "", "2", "3", "");

                    break;
                case Constants.NetworkStatusCode.STATUS_CODE3:
                    displayNormalViews(false);
                    initErrorViewDatas(R.drawable.icon_pay_for_success_n, "您的预登记信息已提交成功", item.msg,
                            "去展会详情", 1);
                    break;
                case Constants.NetworkStatusCode.STATUS_CODE2:
                    displayNormalViews(false);
                    initErrorViewDatas(R.drawable.icon_pay_for_failure_n, "抱歉，您的预登记未通过审核!",
                            "未通过的原因:  " + item.msg, "重新预登记", 2);
                    break;
                default:

                    hideViews();
                    CommonUtils.showToast(mContext, item.msg + "");
                    break;
                case Constants.NetworkStatusCode.CERT_APPLY_SUCCESS:
                    Object obj = item.obj;
                    String json = obj.toString();

                    CommonUtils.showToast(mContext, "您已成功领取电子证件");

                    String arrays[] = json.split(",");

                    String isJump = arrays[0];
                    String id = arrays[1];
                    isJump = isJump.substring(isJump.indexOf("=") + 1);
                    id = id.substring(id.indexOf("=") + 1, id.length() - 1);

                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                    args.put(Constants.IntentParams.ID, Integer.parseInt(id));
                    CommonUtils
                            .startNewActivity(
                                    mContext, args,
                                    CertDetailActivity.class);
                    finish();
                    break;
            }

        }
    };

    private void initErrorViewDatas(int drawableId, String title, String errorMsg, String strId2,
                                    final int type) {

        ImageView imageView = (ImageView) findViewById(R.id.opStatus);
        imageView.setImageResource(drawableId);
        TextView desc1 = (TextView) findViewById(R.id.desc1);
        desc1.setText(title);
        TextView desc2 = (TextView) findViewById(R.id.desc2);
        desc2.setText(errorMsg);
        Button btn = (Button) findViewById(R.id.functionBtn);
        btn.setText(strId2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type == 1) {

                    finish();
                } else {

                    displayNormalViews(true);
                    mPresenter.loadFormsDatas(mId + "", "2", "3", "");
                }
            }
        });
    }

    /**
     * 正常布局与异常布局切换
     *
     * @param display
     */
    private void displayNormalViews(boolean display) {

        if (display) {
            mTypeLayout.setVisibility(View.VISIBLE);
            mContentLayout.setVisibility(View.VISIBLE);
            mBottomLayout.setVisibility(View.VISIBLE);
            mErrorLayout.setVisibility(View.GONE);
        } else {
            mContentLayout.setVisibility(View.GONE);
            mBottomLayout.setVisibility(View.GONE);
            mErrorLayout.setVisibility(View.VISIBLE);
            mTypeLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 正常布局与异常布局切换
     */
    private void hideViews() {

        mErrorLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.GONE);
        mBottomLayout.setVisibility(View.GONE);
        mErrorLayout.setVisibility(View.GONE);
        mTypeLayout.setVisibility(View.GONE);

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("" + mTitle);
    }

    private int ticketDescType = -1;
    protected TextView applyTotalTv;
    protected AutoCircleScrollListView applyList;
    protected WebView applyDescWebView;
    private String applyType = "";

    @Override
    protected void setUpViews() {

        mScrollView = (ScrollView) findViewById(R.id.scroll_view);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottomLayout);
        mErrorLayout = (RelativeLayout) findViewById(R.id.errorLayout);
        mCommitBtn = (Button) findViewById(R.id.commitBtn);
        mCoverImg = (SimpleDraweeView) findViewById(R.id.img);
        mCoverImg.setImageURI(Uri.parse(URLUtil.builderImgUrl(mCover, 540, 270)));
        mCommitBtn.setOnClickListener(this);
        mList = (ListView) findViewById(R.id.list);
        mCertTypeTv = (TextView) findViewById(R.id.certType);
        mTypeLayout = (RelativeLayout) findViewById(R.id.typeLayout);
        mContentLayout = findViewById(R.id.contentLayout);
        mTitleLayout = findViewById(R.id.titleLayout);
        mWV = (WebView) findViewById(R.id.webView);

        applyTotalTv = (TextView) findViewById(R.id.applyTotalTv);
        applyList = (AutoCircleScrollListView) findViewById(R.id.applyList);
        applyDescWebView = (WebView) findViewById(R.id.applyCertDescWebView);
        mGetBasePresenter = new ApplyTicketPresenter();
        mGetBasePresenter.attachView(wrapApplyTicketView);

        mGetBasePresenter.getApplyList(builderReqParams(false, null), false);
        ticketDescType = 1;
        mGetBasePresenter.getTicketDescInfo(builderReqParams(true, Constants.Types.APPLY_CERT + ""));
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
                applyDescWebView.getSettings().setDefaultTextEncodingName("UTF-8");
                applyDescWebView.loadDataWithBaseURL("", WebviewUtil.getHtmlData(model.information), "text/html", "UTF-8", "");
            } else if (ticketDescType == 2) {
                showApplySuccess(model);
            }
        }

    };


    private void showApplySuccess(TicketDescModel model) {


        mContentLayout.setVisibility(View.GONE);
        mTitleLayout.setVisibility(View.GONE);
        mTypeLayout.setVisibility(View.GONE);
        mBottomLayout.setVisibility(View.GONE);
        findViewById(R.id.splitView).setVisibility(View.GONE);
        LinearLayout successLayout = findViewById(R.id.successLayout);

        WebView ticketDescWebView = findViewById(R.id.ticketDescWebView);
        successLayout.setVisibility(View.VISIBLE);

        ticketDescWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        ticketDescWebView.loadDataWithBaseURL("", WebviewUtil.getHtmlData(model.information), "text/html", "UTF-8", "");
    }

    private static final int INIT_ITEMS = 5;//默认初始列表显示5条数据

    private void wrapperAutoScrollItems(ApplyTicketModel applyModel) {

        int count = applyModel.count;
        List<ApplyTicketModel.User> users = applyModel.list;

        if (count < INIT_ITEMS) {

            for (int i = 0; i < INIT_ITEMS - count; i++) {

                users.add(new ApplyTicketModel.User());
            }
        }
        AutoScrollAdapter autoScrollAdapter = new AutoScrollAdapter(users, mContext);
        applyList.setAdapter(autoScrollAdapter);
        applyList.startScroll();
    }

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

        params.put("event_id", mId + "");
        return params;
    }


    /**
     * 显示表单数据
     */
    private void showFormData() {
        mItems = mTypes.get(mSelect).forms;
        for (Form form : mItems) {

            //            switch (form.field) {
            //                case Constants.CheckInFormKey.NAME:
            //
            //                    form.layoutId = R.layout.base_info_item_1;
            //                    form.viewTypeId = 0;
            //                    break;
            //                case Constants.CheckInFormKey.SEX:
            //                    form.layoutId = R.layout.base_info_item_2;
            //                    form.viewTypeId = 1;
            //                    break;
            //                case Constants.CheckInFormKey.AGE:
            //                case Constants.CheckInFormKey.LOCATION:
            //                case Constants.CheckInFormKey.CAR:
            //                case Constants.CheckInFormKey.BRAND:
            //                    form.layoutId = R.layout.base_info_item_3;
            //                    form.viewTypeId = 2;
            //                    break;
            //
            //
            //            }
            switch (form.field) {
                case Constants.CheckInFormKey.NAME:
                case Constants.CheckInFormKey.ADDRESS:
                case Constants.CheckInFormKey.COMPANY:
                case Constants.CheckInFormKey.DEPT:
                case Constants.CheckInFormKey.POSITION:
                case Constants.CheckInFormKey.BRAND_SUB:
                    form.layoutId = R.layout.base_info_item_1;
                    form.viewTypeId = 0;
                    break;
                case Constants.CheckInFormKey.SEX:
                    form.layoutId = R.layout.base_info_item_2;
                    form.viewTypeId = 1;
                    break;
                case Constants.CheckInFormKey.AGE:
                case Constants.CheckInFormKey.LOCATION:
                case Constants.CheckInFormKey.BRAND:
                case Constants.CheckInFormKey.SERIES:
                case Constants.CheckInFormKey.CAR:
                case Constants.CheckInFormKey.VISIT_DATE:
                case Constants.CheckInFormKey.INDUSTRY:
                case Constants.CheckInFormKey.PRICE:
                    form.layoutId = R.layout.base_info_item_3;
                    form.viewTypeId = 2;
                    break;

            }
        }
        mAdapter = new CheckinBuyerAdapter();
        mList.setOnItemClickListener(mVisitorItemListener);

        mList.setAdapter(mAdapter);
        ViewUtil.setListViewHeightBasedOnChildren(mList);
        mPresenter.loadFormsDesc(mTypes.get(mSelect).id + "");
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

    /**
     * 日期控件
     *
     * @param position
     */
    private void onYearMonthDayPicker(final int position) {
        final DatePicker picker = new DatePicker(this);
        picker.setTopPadding(2);
        picker.setRangeStart(2017, 1, 1);
        picker.setRangeEnd(2111, 1, 11);
        picker.setSelectedItem(2017, 1, 1);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(year).append("-").append(month).append("-").append(day);
                mItems.get(position).desc = stringBuilder.toString();

                mAdapter.notifyDataSetChanged();
            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(
                        year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(
                        picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(
                        picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }

    private int brandIndex = -1;
    private AdapterView.OnItemClickListener mVisitorItemListener = new AdapterView.OnItemClickListener() {
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
                    //                                                                                    onAgePicker(
                    //                                                                                        position);

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
                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                    KPlayCarApp.putValue(Constants.KeyParams.PRE_REG_SELECT_MODEL, form.field);
                    args.put(Constants.IntentParams.MODULE_TYPE, Constants.ModuleTypes.PRE_REG_CERT);
                    CommonUtils
                            .startNewActivity(
                                    CheckinActivity.this,
                                    args, FactoryBrandActivity.class
                            );
                    break;
                case Constants.CheckInFormKey.VISIT_DATE:

                    onYearMonthDayPicker(
                            position);
                    break;

                case Constants.CheckInFormKey.PRICE:
                    onOptionBudgetPicker(position);
                    break;
            }

        }
    };

    private List<CheckinTypeModel.CheckinType> mTypes = null;

    @Override
    public void showDatas(CheckinTypeModel model) {
        mTypes = model.types;
        mTypeLayout.setOnClickListener(this);

        List<CheckinTypeModel.CheckinType> types = model.types;

        int size = types.size();
        if (size == 0) {

            CommonUtils.showToast(mContext, "暂无可领取的证件!");

            mTypeLayout.setVisibility(View.GONE);
            mBottomLayout.setVisibility(View.GONE);
            mContentLayout.setVisibility(View.GONE);
            return;
        }
        mTypeLayout.setVisibility(View.VISIBLE);
        mBottomLayout.setVisibility(View.VISIBLE);
        String[] items = new String[size];
        int index = 0;
        for (CheckinTypeModel.CheckinType type : types) {
            items[index] = type.name;
            index++;
        }
        mCertTypeTv.setText(items[mSelect]);
        mTypeLayout.setTag(items);

        if (size == 1) {
            mContentLayout.setVisibility(View.VISIBLE);
            mBottomLayout.setVisibility(View.VISIBLE);
            showFormData();
        } else {

            mContentLayout.setVisibility(View.GONE);
            mBottomLayout.setVisibility(View.GONE);
            mCertTypeTv.setText("");
        }


    }

    @Override
    public void showCommitResult(int code, String msg) {

        switch (code) {
            case Constants.NetworkStatusCode.SUCCESS:
                ticketDescType = 2;
                mGetBasePresenter.getTicketDescInfo(builderReqParams(true, Constants.Types.APPLY_CERT_SUCCESS + ""));
                break;
            case Constants.NetworkStatusCode.STATUS_CODE1:
                CommonUtils.showToast(mContext, msg);
                break;
            case Constants.NetworkStatusCode.STATUS_CODE2:
                CommonUtils.showToast(mContext, msg);
                break;
        }

    }

    private WebView mWV = null;

    @Override
    public void showCertDesc(CertResultModel model) {

        mWV.getSettings().setDefaultTextEncodingName("UTF-8");
        mWV.loadDataWithBaseURL("", WebviewUtil.getHtmlData(model.detail.remarks), "text/html", "UTF-8", "");

    }

    private int mSelect;

    private void builderTypeDialog(final String[] items) {

        AlertDialog alertDialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setSingleChoiceItems(items, mSelect, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSelect = which;
                dialog.dismiss();
                mCertTypeTv.setText(items[mSelect]);
                mContentLayout.setVisibility(View.VISIBLE);
                mBottomLayout.setVisibility(View.VISIBLE);
                showFormData();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
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

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {
            case R.id.typeLayout:

                Object tag = view.getTag();
                if (tag != null) {
                    String[] items = (String[]) tag;
                    builderTypeDialog(items);
                }

                break;
            case R.id.commitBtn:

                mPresenter.commitFormsDatas(builderFormsDatas());
                break;
        }

    }

    @Override
    public void setLoadingIndicator(boolean active) {

        // showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

        //dismissLoadingDialog();
    }

    @Override
    public Context getContext() {
        return mContext;
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

    private class CheckinBuyerAdapter extends BaseAdapter {

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

    private void setCommBtnStatus() {
        if (hadRequired()) {
            mCommitBtn.setEnabled(false);
            mCommitBtn.setTextColor(mRes.getColor(R.color.text_color));
        } else {
            mCommitBtn.setEnabled(true);
            mCommitBtn.setTextColor(mRes.getColor(R.color.white));
        }
    }

    private Map<String, String> builderFormsDatas() {

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.CheckInFormKey.TOKEN, IOUtils.getToken(mContext));
        params.put(Constants.CheckInFormKey.CID, mTypes.get(mSelect).id + "");
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
                case Constants.CheckInFormKey.SERIES:
                    params.put(Constants.CheckInFormKey.SERIES, form.desc);
                    break;
                case Constants.CheckInFormKey.ADDRESS:
                    params.put(Constants.CheckInFormKey.ADDRESS, form.desc);
                    break;
                case Constants.CheckInFormKey.AREA:
                    params.put(Constants.CheckInFormKey.AREA, form.desc);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


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
}
