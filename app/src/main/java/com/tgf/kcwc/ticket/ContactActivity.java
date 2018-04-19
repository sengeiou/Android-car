package com.tgf.kcwc.ticket;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.ContactAdapter;
import com.tgf.kcwc.adapter.OnItemClickListener;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.coupon.manage.CouponSendSeeActivity;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.ContactUser;
import com.tgf.kcwc.mvp.view.DividerItemDecoration;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.Validator;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Auther: Scott
 * Date: 2017/2/9 0009
 * E-mail:hekescott@qq.com
 */

public class ContactActivity extends BaseActivity {
    private final String KEY_INTENT_LISTCONTACT = "select_contact";
    private final String KEY_INTENT_TYPE = "type";
    private final int VAULE_INTENT_TICKET = 1;
    private final int VAULE_INTENT_COUPON = 2;
    private static final String TAG = "ContactActivity";
    private RecyclerView contanctRv;
    private ArrayList<ContactUser> contactUsers = new ArrayList<>();
    private ArrayList<ContactUser> mSelecUsers = new ArrayList<>();
    private final int MSG_SHOW_CONTACT = 100;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_CONTACT:
//
                    mContactAdapter.setDatas(contactUsers);
                    mContactAdapter.notifyDataSetChanged();
                    mIndexBar.setmSourceDatas(contactUsers)//设置数据
                            .invalidate();
                    mDecoration.setmDatas(contactUsers);


                    break;

            }

        }
    };
    private ContactAdapter mContactAdapter;
    private LinearLayoutManager linearLayoutManager;
    private SuspensionDecoration mDecoration;
    private TextView mTvSideBarHint;
    private IndexBar mIndexBar;
    private int mIntentType = -1;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("通讯录");
        function.setText("确认");
        function.setOnClickListener(mConfirmListener);
    }

    private View.OnClickListener mConfirmListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {


            Intent toIntent = new Intent();
            switch (mIntentType) {
                case VAULE_INTENT_COUPON:
//                    toIntent.setClass(ContactActivity.this, CouponSendSeeActivity.class);
                    break;
                case VAULE_INTENT_TICKET:
//                                 toIntent.setClass(ContactActivity.this, TicketSendSeeActivity.class);
                    break;
                default:
                    break;
            }
            toIntent.putExtra(KEY_INTENT_LISTCONTACT, (Serializable) mSelecUsers);
            setResult(RESULT_OK, toIntent);
            finish();
//            startActivity(toIntent);

        }
    };

    @Override
    protected void setUpViews() {
        Intent fromIntent = getIntent();
        Serializable contactSel = fromIntent.getSerializableExtra(KEY_INTENT_LISTCONTACT);
        if (contactSel != null) {
            mSelecUsers = (ArrayList<ContactUser>) contactSel;
        }
        contanctRv = (RecyclerView) findViewById(R.id.contanct_rv);
        mTvSideBarHint = (TextView) findViewById(R.id.tvSideBarHint);
        //IndexBar
        mIndexBar = (IndexBar) findViewById(R.id.indexBar);


        mIntentType = fromIntent.getIntExtra(KEY_INTENT_TYPE, -1);


        linearLayoutManager = new LinearLayoutManager(this);
        contanctRv.setLayoutManager(linearLayoutManager);

        mContactAdapter = new ContactAdapter(this, contactUsers);
        contanctRv.setAdapter(mContactAdapter);

        mDecoration = new SuspensionDecoration(this, contactUsers);
        contanctRv.addItemDecoration(mDecoration);
        //如果add两个，那么按照先后顺序，依次渲染。
        contanctRv.addItemDecoration(new DividerItemDecoration(ContactActivity.this,
                DividerItemDecoration.VERTICAL_LIST));

//        使用indexBar
//        HintTextView

        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(linearLayoutManager);//设置RecyclerView的LayoutManager


        mContactAdapter.setOnItemClicklisenter(new OnItemClickListener<ContactUser>() {

            @Override
            public void onItemClick(ViewGroup parent, View view, ContactUser contactUser, int position) {

                Iterator itr = mSelecUsers.iterator();
                while (itr.hasNext()) {
                    ContactUser itrValue = (ContactUser) itr.next();
                    if (contactUser.mobile.equals(itrValue.mobile)) {
                        if (!contactUser.isSelected) {
                            itr.remove();
                            return;
                        }
                    }
                }
                mSelecUsers.add(contactUser);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, ContactUser contactUser, int position) {
                return false;
            }
        });


        boolean flag = CommonUtils.checkSpPermission(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        if (flag) {
            readContacts();
            mContactAdapter.setDatas(contactUsers);
            mContactAdapter.notifyDataSetChanged();
            mIndexBar.setmSourceDatas(contactUsers)//设置数据
                    .invalidate();
            mDecoration.setmDatas(contactUsers);
//            readContacts();
//            Observable.create(new Observable.OnSubscribe<Object>() {
//                @Override
//                public void call(Subscriber<? super Object> subscriber) {
//                    readContacts();
//                    subscriber.onNext(2);
//                    subscriber.onCompleted();
//                }
//
//            }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
//                    .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
//                    .subscribe(new Observer<Object>() {
//
//
//                        @Override
//                        public void onCompleted() {
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                        }
//
//                        @Override
//                        public void onNext(Object o) {
//
//                        }
//                    });
//            new Thread(runnable).start();
        }
        //indexbar初始化


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            new Thread(runnable).start();//异步查询数据库
            readContacts();
        }
    }

    private void readContacts() {
        contactUsers.clear();
        //得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        //向下移动光标
        while (cursor.moveToNext()) {
            //取得联系人名字
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            String contact = cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
            while (phone.moveToNext()) {
                String phoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
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
                    ContactUser u = new ContactUser();
//                    ContactsListActivity.User u = new ContactsListActivity.User();
                    u.name = contact;
                    u.userId = Integer.parseInt(contactId);
                    u.mobile = phoneNumber;

                    //u.headerImg = getContactPhoto(mContext, contactId, R.drawable.def_header_img);
                    contactUsers.add(u);
                }

            }
        }
        if (mSelecUsers != null && contactUsers != null) {
            if (mSelecUsers.size() != 0 && contactUsers.size() != 0) {
                for (ContactUser selectUser : mSelecUsers) {
                    for (ContactUser contact : contactUsers) {
                        if (selectUser.mobile.equals(contact.mobile)) {
                            contact.isSelected = true;
                        }
                    }
                }
            }
        }

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

//            readContacts();
            for (int i = 0; i < 10; i++) {
                ContactUser u = new ContactUser();
//                    ContactsListActivity.User u = new ContactsListActivity.User();
                u.name = "hke" + i;
                u.userId = i;
                u.mobile = "1336824659" + i;
                contactUsers.add(u);
            }


            mHandler.sendEmptyMessageDelayed(MSG_SHOW_CONTACT, 500);
        }
    };
}
