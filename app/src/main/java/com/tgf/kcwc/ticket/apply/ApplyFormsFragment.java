package com.tgf.kcwc.ticket.apply;

import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.ApplyTicketModel;
import com.tgf.kcwc.mvp.model.Form;
import com.tgf.kcwc.mvp.model.PreTicketModel;
import com.tgf.kcwc.mvp.model.TicketDescModel;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.util.WebviewUtil;
import com.tgf.kcwc.view.UPMarqueeView;
import com.tgf.kcwc.view.autoscrolltext.AutoCircleScrollListView;
import com.tgf.kcwc.view.autoscrolltext.VerticalScrollTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Jenny
 * Date:2017/11/6
 * E-mail:fishloveqin@gmail.com
 */

public class ApplyFormsFragment extends BaseFragment implements DataCallback {
    protected TextView adTv;
    protected TextView exhibitionNameTv;
    protected TextView typeTv;
    protected TextView applyTotalTv;
    protected View rootView;
    protected TextView title, contentTv;
    protected TextView applyTypeTv;
    protected TextView ticketNameTv;
    protected RelativeLayout typeLayout;
    protected WebView webView;
    private ListView mList;
    private AutoCircleScrollListView mApplyList;

    private ArrayList<PreTicketModel.TicketTypeBean> mTypes = null;
    private List<Form> mItems = null;
    private PreTicketAdapter mAdapter = null;
    private int mSelect = 0;
    private Handler mHandler = new Handler();
    private WebView mWebView;

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_apply_ticket_forms;
    }

    @Override
    protected void initView() {
        adTv = findView(R.id.adTv);
        exhibitionNameTv = findView(R.id.exhibitionNameTv);
        exhibitionNameTv.setText(name);
        typeTv = findView(R.id.typeTv);
        applyTotalTv = findView(R.id.applyTotalTv);
        mList = findView(R.id.list);
        mApplyList = findView(R.id.applyList);
        upMarqueeView = findView(R.id.marqueeView);
        scrollTextView = findView(R.id.scrollTextView);
        mWebView = findView(R.id.webView);
        title = findView(R.id.title);
        contentTv = findView(R.id.content);
        applyTypeTv = findView(R.id.applyTypeTv);
        ticketNameTv = findView(R.id.ticketNameTv);
        typeLayout = findView(R.id.typeLayout);
    }

    UPMarqueeView upMarqueeView = null;
    VerticalScrollTextView scrollTextView;

    @Override
    public void loadForms(PreTicketModel formsModel) {

        mTypes = formsModel.list;

        int size = mTypes.size();
        if (size > 1) {
            typeLayout.setVisibility(View.VISIBLE);
        } else {
            typeLayout.setVisibility(View.GONE);
        }
        showFormData();
    }

    @Override
    public void loadApplyList(ApplyTicketModel applyModel) {

        applyTotalTv.setText(applyModel.count + "人已报名");
        contentTv.setText("已报名: " + applyModel.count + "人");
        AutoScrollAdapter adapter = new AutoScrollAdapter(applyModel.list);
        mApplyList.setAdapter(adapter);
        mApplyList.startScroll();

    }

    @Override
    public void loadAD(String adText) {

        adTv.setText(adText);
    }

    @Override
    public void loadTicketDesc(TicketDescModel model) {

        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.loadDataWithBaseURL("", WebviewUtil.getHtmlData(model.information), "text/html", "UTF-8", "");
    }


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

            ticketNameTv.setText(mTypes.get(mSelect).ticketName);
            mAdapter = new PreTicketAdapter();
            mList.setAdapter(mAdapter);
            ViewUtil.setListViewHeightBasedOnChildren(mList);
        }

    }

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
                            // setCommBtnStatus();
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
                            //setCommBtnStatus();
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


    private class AutoScrollAdapter extends BaseAdapter {

        List<ApplyTicketModel.User> mUsers;

        /**
         * 循环展示次数,最理想的值为3
         */

        public AutoScrollAdapter(List<ApplyTicketModel.User> users) {
            this.mUsers = users;
        }

        @Override
        public int getCount() {
            if (mUsers.size() > 0) {

                return mUsers.size() * AutoCircleScrollListView.REPEAT_TIMES;
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return mUsers.get(position % mUsers.size());
        }

        @Override
        public long getItemId(int position) {
            return position % mUsers.size();
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            WrapAdapter.ViewHolder holder = WrapAdapter.ViewHolder.get(mContext, view, viewGroup, R.layout.auto_text_item_view, position);

            ApplyTicketModel.User user = mUsers.get(position % mUsers.size());
            TextView nicknameTv = holder.getView(R.id.nicknameTv);
            TextView contentTv = holder.getView(R.id.contentTv);
            TextView brandTv = holder.getView(R.id.brandTv);
            RelativeLayout rl = holder.getView(R.id.rl);
            nicknameTv.setText(user.nickname);
            contentTv.setText("刚刚预约观展");
            if (TextUtils.isEmpty(user.brand)) {
                brandTv.setVisibility(View.GONE);
            } else {
                brandTv.setVisibility(View.VISIBLE);
            }
            CommonUtils.customDisplayAttentionText(",   关注" + "   " + user.brand + user.series, brandTv, mRes);
            // brandTv.setText(user.brand + "" + user.series);
            return holder.getConvertView();
        }


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mApplyList.onStop();
    }

}
