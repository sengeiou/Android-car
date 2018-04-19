package com.tgf.kcwc.me;

import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.entity.PathItem;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.HobbyTag;
import com.tgf.kcwc.mvp.model.UserDetailDataModel;
import com.tgf.kcwc.mvp.presenter.FileUploadPresenter;
import com.tgf.kcwc.mvp.presenter.UserDataPresenter;
import com.tgf.kcwc.mvp.view.FileUploadView;
import com.tgf.kcwc.mvp.view.UserDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.GsonUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.richeditor.SEImageLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.OptionPicker;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Author:Jenny
 * Date:16/5/9 15:22
 * E-mail:fishloveqin@gmail.com
 * 个人信息
 **/
public class UserInfoActivity extends BaseActivity implements AdapterView.OnItemClickListener,
        View.OnClickListener, UserDataView<UserDetailDataModel> {

    protected TextView name;
    protected SimpleDraweeView headerImg;
    protected RelativeLayout headerLayout;
    protected ListView list1;
    protected ListView list2;
    private String mUserId;

    private List<DataItem> firstDatas = new ArrayList<DataItem>();

    private List<DataItem> secondDatas = new ArrayList<DataItem>();

    private WrapAdapter<DataItem> adapter1, adapter2;

    private UserDataPresenter mPresenter;

    private UserDataPresenter mUpdateUserInfoPresenter;
    private FileUploadPresenter mHeaderImgPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserId = getIntent().getStringExtra(Constants.IntentParams.ID);
        super.setContentView(R.layout.user_info_layout);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void setUpViews() {
        initView();
        initListData();

        mPresenter = new UserDataPresenter();
        mPresenter.attachView(this);
        mPresenter.getUserDetailInfo(mUserId, IOUtils.getToken(mContext));
        mUpdateUserInfoPresenter = new UserDataPresenter();
        mUpdateUserInfoPresenter.attachView(mUpdateUserDataView);
        mHeaderImgPresenter = new FileUploadPresenter();
        mHeaderImgPresenter.attachView(mHeaderImgView);
    }

    private String mHeaderImgUrl = "";

    private FileUploadView<DataItem> mHeaderImgView = new FileUploadView<DataItem>() {
        @Override
        public void resultData(DataItem dataItem) {

            PathItem item = dataItem.resp.data;
            mHeaderImgUrl = item.path;
        }

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
    };

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("基本信息");

        function.setTextResource("保存", R.color.white, 14);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mUpdateUserInfoPresenter.updateUserInfo(builderPostParams());
            }
        });
    }

    private void initListData() {

        String[] arrays = mRes.getStringArray(R.array.user_info_titles);

        String[] hints = mRes.getStringArray(R.array.user_info_hints);
        int index = 0;
        for (String s : arrays) {

            DataItem dataItem = new DataItem();
            dataItem.title = s;
            dataItem.hint = hints[index];
            index++;

            firstDatas.add(dataItem);
        }
        adapter1 = new WrapAdapter<DataItem>(mContext, R.layout.person_info_item, firstDatas) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {

                TextView titleTv = helper.getView(R.id.title);
                TextView contentTv = helper.getView(R.id.content);

                titleTv.setText(item.title);
                contentTv.setHint(item.hint);
                contentTv.setText(item.content);
                contentTv.setSelected(true);
            }
        };
        list1.setAdapter(adapter1);

        arrays = mRes.getStringArray(R.array.user_info_titles2);

        index = 0;
        for (String s : arrays) {

            DataItem dataItem = new DataItem();
            dataItem.title = s;
            index++;

            secondDatas.add(dataItem);
        }

        adapter2 = new WrapAdapter<DataItem>(mContext, R.layout.person_info_item, secondDatas) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {

                TextView titleTv = helper.getView(R.id.title);
                TextView contentTv = helper.getView(R.id.content);

                titleTv.setText(item.title);
                contentTv.setText(item.content);
                contentTv.setTextColor(mRes.getColor(R.color.text_color17));
            }
        };

        list2.setAdapter(adapter2);

        list1.setOnItemClickListener(mFirstItemClickListener);
    }

    private AdapterView.OnItemClickListener mFirstItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            DataItem dataItem = (DataItem) parent.getAdapter().getItem(position);
            switch (position) {

                case 0:
                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                    args.put(Constants.IntentParams.TITLE, "设置昵称");
                    args.put(Constants.IntentParams.INTENT_TYPE, 1);
                    args.put(Constants.IntentParams.DATA, dataItem.content);
                    CommonUtils.startResultNewActivity(UserInfoActivity.this, args,
                            UpdatePersonInfoActivity.class, Constants.IntentParams.REQUEST_CODE);
                    break;

                case 1:
                    onOptionPicker(1);

                    break;

                case 2:

                    onYearPicker(2);
                    break;

                case 3:

                    args = new HashMap<String, Serializable>();
                    args.put(Constants.IntentParams.TITLE, "设置签名");
                    args.put(Constants.IntentParams.INTENT_TYPE, 2);
                    args.put(Constants.IntentParams.DATA, dataItem.content);
                    CommonUtils.startResultNewActivity(UserInfoActivity.this, args,
                            UpdatePersonInfoActivity.class, Constants.IntentParams.REQUEST_CODE_2);
                    break;

                case 4:

                    args = new HashMap<String, Serializable>();
                    if (mHobbyTags != null) {
                        args.put("mHobbyTags", mHobbyTags);
                    }
                    CommonUtils.startResultNewActivity(UserInfoActivity.this, args,
                            HobbyTagMgrActivity.class, Constants.IntentParams.REQUEST_CODE_3);

                    break;
            }

        }
    };

    private void initView() {
        name = (TextView) findViewById(R.id.name);
        headerImg = (SimpleDraweeView) findViewById(R.id.headerImg);
        headerImg.setOnClickListener(this);
        headerLayout = (RelativeLayout) findViewById(R.id.headerLayout);
        //headerLayout.setOnClickListener(this);
        list1 = (ListView) findViewById(R.id.list1);
        list2 = (ListView) findViewById(R.id.list2);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {
            case R.id.headerImg:

                ImagePicker.getInstance().setMultiMode(false);
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);

                break;
        }

    }

    private static final int IMAGE_PICKER = 1001;

    private String nick = "";
    private String gender = "";
    private String birthday = "";
    private String sign = "";
    private String tag = "";
    private String mobile = "";
    private String createTime = "";
    private String regArea = "";

    private List<UserDetailDataModel.HobbyBean> hobbyBeanList;

    @Override
    public void showDatas(UserDetailDataModel userDetailDataModel) {

        //显示头像

        mHeaderImgUrl = userDetailDataModel.avatar;
        headerImg
                .setImageURI(Uri.parse(URLUtil.builderImgUrl(userDetailDataModel.avatar, 144, 144)));

        if (!TextUtils.isEmpty(userDetailDataModel.nickname)) {
            nick = userDetailDataModel.nickname;
        }

        int gengerType = userDetailDataModel.sex;
        if (gengerType == 1) {
            gender = "男";
        } else if (gengerType == 2) {
            gender = "女";
        }
        if (!TextUtils.isEmpty(userDetailDataModel.birth)) {
            if ("0000-00-00".equals(userDetailDataModel.birth)) {
                birthday = "";
            } else {
                birthday = userDetailDataModel.birth;
            }

        }

        if (!TextUtils.isEmpty(userDetailDataModel.signText)) {
            sign = userDetailDataModel.signText;
        }

        if (!TextUtils.isEmpty(userDetailDataModel.tel)) {
            mobile = userDetailDataModel.tel;
        }

        if (!TextUtils.isEmpty(userDetailDataModel.createTime)) {
            createTime = userDetailDataModel.createTime;
            createTime = DateFormatUtil.getCreateTime(createTime);
        }

        if (!TextUtils.isEmpty(userDetailDataModel.registerArea)) {
            regArea = userDetailDataModel.registerArea;
        }
        firstDatas.get(0).content = nick;
        firstDatas.get(1).content = gender;
        firstDatas.get(2).content = birthday;
        firstDatas.get(3).content = sign;

        //显示标签

        hobbyBeanList = userDetailDataModel.hobby;
        StringBuilder stringBuilder = new StringBuilder();
        for (UserDetailDataModel.HobbyBean hobbyBean : hobbyBeanList) {

            stringBuilder.append("#").append(hobbyBean.name).append(",");
        }

        String tagNames = stringBuilder.toString();
        if (tagNames.length() > 0) {
            tagNames = tagNames.substring(0, tagNames.length() - 1);
        }
        firstDatas.get(4).content = tagNames;
        adapter1.notifyDataSetChanged();

        secondDatas.get(0).content = mobile;
        secondDatas.get(1).content = createTime;
        secondDatas.get(2).content = regArea;
        adapter2.notifyDataSetChanged();
    }

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

    /**
     * 年选择菜单项
     *
     * @param position
     */
    private void onYearPicker(final int position) {
        final DatePicker picker = new DatePicker(this);
        picker.setTopPadding(2);
        picker.setRangeStart(1900, 1, 1);
        picker.setRangeEnd(2111, 1, 11);
        picker.setSelectedItem(1986, 12, 12);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                firstDatas.get(position).content = year + "-" + month + "-" + day;
                adapter1.notifyDataSetChanged();
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

    private void onOptionPicker(final int position) {
        OptionPicker picker = new OptionPicker(this, new String[]{"男", "女"});
        picker.setCycleDisable(true);
        picker.setLineVisible(false);
        picker.setShadowVisible(true);
        picker.setTextSize(11);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                firstDatas.get(position).content = item + "";

                adapter1.notifyDataSetChanged();
            }
        });
        picker.show();
    }

    private ArrayList<HobbyTag> mHobbyTags = null;

    /**
     * post提交数据参数
     * <p>
     * avatar	头像	string
     * birth	生日	string
     * category_id	标签分类（数组转json）	array<string>
     * nickname	昵称	string
     * sex	性别	number
     * sign_text	签名	string
     */
    private Map<String, String> builderPostParams() {

        Map<String, String> params = new HashMap<String, String>();

        String avatar = mHeaderImgUrl;
        String categoryIds = "";
        String nickname = firstDatas.get(0).content;
        String sex = firstDatas.get(1).content;

        String sexType = "";
        if (!TextUtils.isEmpty(sex)) {
            if ("男".equals(sex)) {
                sexType = "1";
            } else if ("女".equals(sex)) {
                sexType = "2";
            }
        }
        String birth = firstDatas.get(2).content;
        String signText = firstDatas.get(3).content;

        List<String> ids = new ArrayList<String>();

        if ((hobbyBeanList != null) && (mHobbyTags == null)) {

            for (UserDetailDataModel.HobbyBean hobbyTag : hobbyBeanList) {

                ids.add(hobbyTag.id + "");
            }

        }

        if (mHobbyTags != null) {

            for (HobbyTag hobbyTag : mHobbyTags) {

                for (HobbyTag secTag : hobbyTag.second) {
                    if (secTag.isSelected) {
                        ids.add(secTag.id + "");
                    }

                }
            }
        }

        if (ids.size() > 0) {
            categoryIds = GsonUtil.objToJson(ids);

        }
        Logger.e("categoryIds:" + categoryIds);
        params.put("avatar", avatar);
        params.put("birth", birth);
        params.put("category_id", categoryIds);
        params.put("nickname", nickname);
        params.put("sex", sexType);
        params.put("sign_text", signText);
        params.put("token", IOUtils.getToken(mContext));
        return params;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Constants.IntentParams.REQUEST_CODE == requestCode && RESULT_OK == resultCode) {

            firstDatas.get(0).content = data.getStringExtra(Constants.IntentParams.DATA);
            adapter1.notifyDataSetChanged();
        }

        if (Constants.IntentParams.REQUEST_CODE_2 == requestCode && RESULT_OK == resultCode) {

            firstDatas.get(3).content = data.getStringExtra(Constants.IntentParams.DATA);
            adapter1.notifyDataSetChanged();
        }

        if (Constants.IntentParams.REQUEST_CODE_3 == requestCode && RESULT_OK == resultCode) {

            //取出标签数据

            mHobbyTags = data.getParcelableArrayListExtra(Constants.IntentParams.DATA);

            int size = mHobbyTags.size();
            StringBuilder stringBuilder = new StringBuilder();
            if (size != 0) {

                for (HobbyTag tag : mHobbyTags) {

                    List<HobbyTag> secTags = tag.second;

                    for (HobbyTag secTag : secTags) {
                        if (secTag.isSelected) {
                            stringBuilder.append("#").append(secTag.name).append(",");
                        }
                    }

                }

                String tagNames = stringBuilder.toString();
                if (tagNames.length() > 0) {
                    tagNames = tagNames.substring(0, tagNames.length() - 1);
                }

                Logger.e("tags:" + tagNames);
                firstDatas.get(4).content = tagNames;
                adapter1.notifyDataSetChanged();
            }

        }

        if (data != null && requestCode == IMAGE_PICKER) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data
                    .getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);

            for (ImageItem item : images) {
                //editor.addImage(item.path);
                displayHeaderImg(item.path);
                mHeaderImgPresenter
                        .uploadFile(
                                RequestBody.create(MediaType.parse("image/png"), new File(item.path)),
                                RequestBody.create(MediaType.parse("text/plain"), "avatar"),
                                RequestBody.create(MediaType.parse("text/plain"), ""), RequestBody
                                        .create(MediaType.parse("text/plain"), IOUtils.getToken(getContext())),
                                headerImg);
            }
        }

    }

    /**
     * 显示头像
     *
     * @param path
     */
    private void displayHeaderImg(String path) {

        PointF pointF = new PointF();
        pointF.x = headerImg.getWidth();
        pointF.y = headerImg.getHeight();
        //  System.out.println("x:" + pointF.x + ",y:" + pointF.y);
        PathItem pathItem = new PathItem();
        pathItem.path = path;
        headerImg.setTag(pathItem);
        SEImageLoader.getInstance().loadImage(path, headerImg, pointF);
    }

    UserDataView<Account.UserInfo> mUpdateUserDataView = new UserDataView<Account.UserInfo>() {
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

            showLoadingIndicator(false);

        }

        @Override
        public void showDatas(Account.UserInfo userInfo) {
            Account account = IOUtils.getAccount(mContext);
            account.userInfo = userInfo;
            ObjectOutputStream objectOutputStream = null;
            FileOutputStream outputStream = null;
            try {
                outputStream = openFileOutput(Constants.KEY_ACCOUNT_FILE, Context.MODE_PRIVATE);
                objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(account);
                objectOutputStream.flush();

                IOUtils.clearData();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.close(objectOutputStream);
                IOUtils.close(outputStream);
            }
            CommonUtils.showToast(mContext, "更新资料成功!");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.detachView();
        }

        if (mUpdateUserInfoPresenter != null) {
            mUpdateUserInfoPresenter.detachView();
        }

        if (mHeaderImgPresenter != null) {
            mHeaderImgPresenter.detachView();
        }
    }
}
