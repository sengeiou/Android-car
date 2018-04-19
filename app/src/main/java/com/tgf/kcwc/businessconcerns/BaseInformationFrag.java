package com.tgf.kcwc.businessconcerns;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.BaseInfoModel;
import com.tgf.kcwc.mvp.presenter.BaseInfoPresenter;
import com.tgf.kcwc.mvp.view.BaseInfoView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/8/1 10:38
 * 商务关注-基本信息
 */

public class BaseInformationFrag extends BaseFragment implements BaseInfoView {
    private TextView nameTv;
    private TextView phoneTv;
    private TextView companyTv;
    private TextView departmentTv;
    private TextView positionTv;
    private TextView companyAddressTv;
    private TextView familyAddressTv;
    private TextView birthdayTv;
    private TextView qqTv;
    private TextView wbTv;
    private TextView wxTv;
    private TextView tagTv;
    private TextView remarkTv;

    private ListView mListView;
    private WrapAdapter mAdapter;
    private ArrayList<BaseInfoModel.AuthItem> mList = new ArrayList<>();

    BaseInfoPresenter mPresenter;
    BaseInfoModel list;
    private int friendId;

    public BaseInformationFrag(int friendId) {
        super();
        this.friendId = friendId;
    }

    @Override
    protected void updateData() {
        mPresenter = new BaseInfoPresenter();
        mPresenter.attachView(this);
        mPresenter.getBaseInfo(IOUtils.getToken(getContext()), friendId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_base_information;
    }

    @Override
    protected void initView() {
        setUserVisibleHint(true);
        nameTv = findView(R.id.base_information_name_et);
        phoneTv = findView(R.id.base_information_phone_et);
        companyTv = findView(R.id.base_information_company_et);
        departmentTv = findView(R.id.base_information_department_et);
        positionTv = findView(R.id.base_information_position_et);
        companyAddressTv = findView(R.id.base_information_company_address_et);
        familyAddressTv = findView(R.id.base_information_family_address_et);
        birthdayTv = findView(R.id.base_information_birthday_et);
        qqTv = findView(R.id.base_information_qq_et);
        wbTv = findView(R.id.base_information_wb_et);
        wxTv = findView(R.id.base_information_wx_et);

        tagTv = findView(R.id.base_information_tag_et);
        remarkTv = findView(R.id.base_information_remark_et);

        mListView = findView(R.id.frag_list_view);
        initAdapter();

    }

    @Override
    protected void initData() {
//        super.initData();
        mPresenter = new BaseInfoPresenter();
        mPresenter.attachView(this);
        mPresenter.getBaseInfo(IOUtils.getToken(getContext()), friendId);
    }

    @Override
    public void showBaseInfo(BaseInfoModel list) {
        this.list = list;
        if (list.name.equals("-")||list.name.equals("")){
            nameTv.setHint("-");
        }else {
            nameTv.setText(list.name);
        }
        if (list.tel.equals("-")||list.tel.equals("")){
            phoneTv.setHint("-");
        }else {
            phoneTv.setText(list.tel);
        }
        if (list.company.equals("-")||list.company.equals("")){
            companyTv.setHint("-");
        }else {
            companyTv.setText(list.company);
        }
        if (list.department.equals("-")||list.department.equals("")){
            departmentTv.setHint("-");
        }else {
            departmentTv.setText(list.department);
        }
        if (list.position.equals("-")||list.position.equals("")){
            positionTv.setHint("-");
        }else {
            positionTv.setText(list.position);
        }
        if (list.sAddress.equals("-")||list.sAddress.equals("")){
            companyAddressTv.setHint("-");
        }else {
            companyAddressTv.setText(list.sAddress);
        }
        if (list.homeAddress.equals("-")||list.homeAddress.equals("")){
            familyAddressTv.setHint("-");
        }else {
            familyAddressTv.setText(list.homeAddress);
        }
        if (list.birthday.equals("-")||list.birthday.equals("")){
            birthdayTv.setHint("-");
        }else {
            birthdayTv.setText(list.birthday);
        }
        if (list.qq.equals("-")||list.qq.equals("")){
            qqTv.setHint("-");
        }else {
            qqTv.setText(list.qq);
        }
        if (list.weibo.equals("-")||list.weibo.equals("")){
            wbTv.setHint("-");
        }else {
            wbTv.setText(list.weibo);
        }
        if (list.wechat.equals("-")||list.wechat.equals("")){
            wxTv.setHint("-");
        }else {
            wxTv.setText(list.wechat);
        }
        if (list.remark.equals("-")||list.remark.equals("")){
            remarkTv.setHint("-");
        }else {
            remarkTv.setText(list.remark);
        }
//        tagTv.setText(list.);
    }

    @Override
    public void showAuth(ArrayList<BaseInfoModel.AuthItem> authItem) {
        mList.clear();
        mList.addAll(authItem);
        if (null != mAdapter) {
            mListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } else {
            initAdapter();
            mListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initAdapter() {
        mAdapter = new WrapAdapter<BaseInfoModel.AuthItem>(mContext, mList, R.layout.frag_base_info_item) {
            @Override
            public void convert(ViewHolder helper, BaseInfoModel.AuthItem item) {
                ImageView imageView = helper.getView(R.id.base_information_car_iv);
                String avatarUrl = URLUtil.builderImgUrl(item.photo, 144, 144);
                imageView.setImageURI(Uri.parse(avatarUrl));
                helper.setText(R.id.base_information_brand, item.productName);
                helper.setText(R.id.base_information_time, item.createTime);
            }
        };
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }
}
