package com.tgf.kcwc.me;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.mvp.presenter.UserDataPresenter;
import com.tgf.kcwc.mvp.view.UserDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.Validator;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;

/**
 * Author：Jenny
 * Date:2016/9/12 21:26
 * E-mail：fishloveqin@gmail.com
 * 手机联系人列表
 */
public class ContactsListActivity extends BaseActivity implements UserDataView<Object> {

    private UserDataPresenter mPresenter;

    private User              mUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);

        boolean flag = CommonUtils.checkSpPermission(this,
            new String[] { Manifest.permission.READ_CONTACTS }, 1);
        if (flag) {
            showLoadingIndicator(true);
            new Thread(runnable).start();
        }
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("通讯录");
        function.setText(R.string.confirm);

        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mUser == null) {

                    CommonUtils.showToast(mContext, "请选择联系人!");
                    return;
                }
                mPresenter.sendSMS("add_friend", mUser.phoneNum, IOUtils.getToken(mContext));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showLoadingDialog();
            new Thread(runnable).start();//异步查询数据库
        }

    }

    @Override
    protected void setUpViews() {
        mList = (ListView) findViewById(R.id.list);
        mSearchLayout = (RelativeLayout) findViewById(R.id.search_title_layout);
        mSearchLayout.setVisibility(View.GONE);
        mEditText = (EditText) findViewById(R.id.etSearch);
        mEditText.addTextChangedListener(mTextWatcherListener);
        mList.setOnItemClickListener(onItemClickListener);
        mPresenter = new UserDataPresenter();
        mPresenter.attachView(this);

    }

    private TextWatcher mTextWatcherListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            matchesSearchData(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void matchesSearchData(String key) {

        ArrayList<User> users = new ArrayList<User>();
        for (User u : cacheData) {
            String mobile = u.phoneNum;
            String name = u.name;

            if (mobile.indexOf(key) >= 0 || name.indexOf(key) >= 0) {
                users.add(u);
            }
        }

        allData.clear();
        allData.addAll(users);
        adapter.notifyDataSetChanged();
    }

    private User                            mCurrentCheckUser;
    private ListView                        mList;
    private RelativeLayout                  mSearchLayout;
    private EditText                        mEditText           = null;
    private WrapAdapter<User>               adapter             = null;

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                                                                    @Override
                                                                    public void onItemClick(AdapterView<?> adapterView,
                                                                                            View view,
                                                                                            int position,
                                                                                            long id) {

                                                                        User u = (User) adapterView
                                                                            .getAdapter()
                                                                            .getItem(position);
                                                                        u.state = true;
                                                                        mCurrentCheckUser = u;
                                                                        singleChecked(u);
                                                                        mUser = u;
                                                                        adapter
                                                                            .notifyDataSetChanged();
                                                                    }
                                                                };

    private void readContacts() {
        allData.clear();
        cacheData.clear();
        //得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        //向下移动光标
        while (cursor.moveToNext()) {
            //取得联系人名字
            int nameFieldColumnIndex = cursor
                .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            String contact = cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String contactId = cursor
                .getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
            while (phone.moveToNext()) {
                String phoneNumber = phone
                    .getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                //格式化手机号
                phoneNumber = phoneNumber.replace("-", "");
                phoneNumber = phoneNumber.replace(" ", "");
                if (phoneNumber.startsWith("+86")) {
                    phoneNumber = phoneNumber.substring(3, phoneNumber.length());
                }
                if (phoneNumber.startsWith("17951")) {
                    phoneNumber = phoneNumber.substring(5, phoneNumber.length());
                }
                boolean isMobile = Validator.isMobile(phoneNumber);
                if (isMobile) {
                    User u = new User();
                    u.name = contact;
                    u.userId = contactId;
                    u.phoneNum = phoneNumber;
                    //u.headerImg = getContactPhoto(mContext, contactId, R.drawable.def_header_img);
                    allData.add(u);
                    cacheData.add(u);
                }

            }
        }

    }

    Runnable       runnable = new Runnable() {
                                @Override
                                public void run() {

                                    readContacts();
                                    mHandler.sendEmptyMessage(0);
                                }
                            };

    public Handler mHandler = new Handler() {

                                @Override
                                public void handleMessage(Message msg) {

                                    showLoadingIndicator(false);
                                    if (allData.size() > 0) {
                                        mSearchLayout.setVisibility(View.VISIBLE);
                                    }
                                    adapter = new WrapAdapter<User>(mContext, allData,
                                        R.layout.check_contact_item) {
                                                                @Override
                                                                public void convert(ViewHolder helper,
                                                                                    User item) {

                                                                    TextView nameText = helper
                                                                        .getView(R.id.name);
                                                                    nameText.setText(item.name);
                                                                    TextView statusText = helper
                                                                        .getView(R.id.status_text);
                                                                    statusText
                                                                        .setText(item.phoneNum);
                                                                    SimpleDraweeView img = helper
                                                                        .getView(R.id.img);
                                                                    img.setImageURI(Uri.parse(""));
                                                                    ImageView selectedImg = helper
                                                                        .getView(
                                                                            R.id.select_status_img);
                                                                    if (item.state) {
                                                                        selectedImg.setVisibility(
                                                                            View.VISIBLE);
                                                                    } else {
                                                                        selectedImg.setVisibility(
                                                                            View.GONE);
                                                                    }

                                                                }
                                                            };
                                    mList.setAdapter(adapter);
                                }
                            };

    @Override
    public void showDatas(Object o) {

        CommonUtils.showToast(mContext,"邀请好友成功！");
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
    public Context getContext() {
        return mContext;
    }

    public static class User {

        public String  userId;
        public String  phoneNum;
        public String  name;
        public boolean state;
        public Bitmap  headerImg;
        public String  mobile;
    }

    private ArrayList<User> allData   = new ArrayList<User>();
    private ArrayList<User> cacheData = new ArrayList<User>();

    private void singleChecked(User user) {
        for (User u : cacheData) {
            if (u.phoneNum != user.phoneNum) {
                u.state = false;

            }
        }
    }

    /**
     * 获取联系人头像
     *
     * @param c
     * @param personId
     * @param defaultIco
     * @return
     */
    private static Bitmap getContactPhoto(Context c, String personId, int defaultIco) {
        byte[] data = new byte[0];
        Uri u = Uri.parse("content://com.android.contacts/data");
        String where = "raw_contact_id = " + personId
                       + " AND mimetype ='vnd.android.cursor.item/photo'";
        Cursor cursor = c.getContentResolver().query(u, null, where, null, null);
        if (cursor.moveToFirst()) {
            data = cursor.getBlob(cursor.getColumnIndex("data15"));
        }
        cursor.close();
        if (data == null || data.length == 0) {
            return BitmapFactory.decodeResource(c.getResources(), defaultIco);
        } else
            return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(runnable);

        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
