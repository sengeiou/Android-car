package com.tgf.kcwc.me;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.PersonHomeDataModel;
import com.tgf.kcwc.mvp.presenter.UserDataPresenter;
import com.tgf.kcwc.mvp.view.UserDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DeviceUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Jenny
 * Date:2017/5/17
 * E-mail:fishloveqin@gmail.com
 */

public class UserHomeFragment extends BaseFragment implements UserDataView<PersonHomeDataModel> {

    protected View             rootView;
    protected SimpleDraweeView avatar;
    protected TextView         nicknameTv;
    protected TextView         heightTv;
    protected TextView         bwhTv;
    protected TextView         attainmentTv;
    protected RelativeLayout   part1Layout;
    protected ListView         list;
    protected ListView         list2;
    protected TextView         attentionTv;
    protected TextView         secretMsgTv;
    protected RelativeLayout   relayOption;
    protected RelativeLayout   relayOption2;

    private RelativeLayout     mAttentionLayout, mSecretMsgLayout;
    private UserDataPresenter  mPresenter;

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_home;
    }

    @Override
    protected void initView() {
        avatar = findView(R.id.avatar);
        nicknameTv = findView(R.id.nicknameTv);
        heightTv = findView(R.id.heightTv);
        bwhTv = findView(R.id.bwhTv);
        attainmentTv = findView(R.id.attainmentTv);
        part1Layout = findView(R.id.part1Layout);
        part1Layout.setVisibility(View.GONE);
        list = findView(R.id.list);
        list2 = findView(R.id.list2);
        int userId = getActivity().getIntent().getIntExtra(Constants.IntentParams.ID, -1);

        mPresenter = new UserDataPresenter();
        mPresenter.attachView(this);
        mPresenter.getPersonalDetailData(userId + "", IOUtils.getToken(mContext));
    }

    @Override
    public void showDatas(PersonHomeDataModel model) {

        if (model.isDoyen == 1 && model.isModel == 1) {
            part1Layout.setVisibility(View.VISIBLE);
            showFirstPartInfo(model);
        }

        showSecondPartInfo(model);
        showThirdPartInfo(model);
    }

    /***
     * 显示第一部分数据
     * @param model
     */
    private void showFirstPartInfo(PersonHomeDataModel model) {

        avatar.setImageURI(Uri.parse(URLUtil.builderImgUrl(model.model.cover, 360, 360)));
        nicknameTv.setText(model.model.name + "");

        //身高
        String str1 = String.format(mRes.getString(R.string.height_tv), model.model.name + "");

        CommonUtils.customDisplayText(mRes, heightTv, str1, R.color.text_color3);
        //三围
        String str2 = String.format(mRes.getString(R.string.bwh_tv),
            model.model.bust + "/" + model.model.waist + "/" + model.model.hipline);

        CommonUtils.customDisplayText(mRes, bwhTv, str2, R.color.text_color3);

        attainmentTv.setText(model.model.prize + "");
    }

    @Override
    public void setLoadingIndicator(boolean active) {

        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

        showLoadingIndicator(false);
    }

    private void showSecondPartInfo(PersonHomeDataModel model) {

        List<DataItem> dataItems = new ArrayList<DataItem>();

        String[] arrs = mRes.getStringArray(R.array.user_info_items);
        for (String s : arrs) {
            DataItem item = new DataItem();
            item.title = s;
            dataItems.add(item);
        }
        dataItems.get(0).content = model.age + "";
        dataItems.get(1).content = model.signs + "";
        dataItems.get(2).content = model.signText + "";
        dataItems.get(3).content = model.hobby + "";
        //dataItems.get(4).content="";
        WrapAdapter<DataItem> adapter = new WrapAdapter<DataItem>(mContext, dataItems,
            R.layout.user_info_item1) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {

                TextView titleTv = helper.getView(R.id.title);
                titleTv.setText(item.title);
                TextView contentTv = helper.getView(R.id.content);
                contentTv.setText(item.content);
            }
        };
        list.setAdapter(adapter);

    }

    private void showThirdPartInfo(PersonHomeDataModel model) {

        List<DataItem> dataItems = new ArrayList<DataItem>();

        String[] arrs = mRes.getStringArray(R.array.user_info_items2);
        for (String s : arrs) {
            DataItem item = new DataItem();
            item.title = s;
            dataItems.add(item);
        }
        dataItems.get(0).content = model.createTime + "";
        dataItems.get(1).content = model.registerArea + "";
        //dataItems.get(2).content = DeviceUtil.getPhoneModel();
        //dataItems.get(4).content="";
        WrapAdapter<DataItem> adapter = new WrapAdapter<DataItem>(mContext, dataItems,
            R.layout.user_info_item1) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {

                TextView titleTv = helper.getView(R.id.title);
                titleTv.setText(item.title);
                TextView contentTv = helper.getView(R.id.content);
                contentTv.setText(item.content);
                contentTv.setTextColor(mRes.getColor(R.color.text_color17));
            }
        };
        list2.setAdapter(adapter);

    }

    @Override
    public Context getContext() {
        return mContext;
    }

}