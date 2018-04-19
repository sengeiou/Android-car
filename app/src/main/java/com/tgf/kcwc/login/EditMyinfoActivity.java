package com.tgf.kcwc.login;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.presenter.EditUserInfoPresenter;
import com.tgf.kcwc.mvp.presenter.FileUploadPresenter;
import com.tgf.kcwc.mvp.view.EditUserInfoView;
import com.tgf.kcwc.mvp.view.FileUploadView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Auther: Scott
 * Date: 2016/12/15 0015
 * E-mail:hekescott@qq.com
 */

public class EditMyinfoActivity extends BaseActivity
                                implements EditUserInfoView, RadioGroup.OnCheckedChangeListener, FileUploadView {
    //请求相机
    private static final int      REQUEST_CAPTURE                     = 100;
    //请求相册
    private static final int      REQUEST_PICK                        = 101;
    //请求截图
    private static final int      REQUEST_CROP_PHOTO                  = 102;
    //请求访问外部存储
    private static final int      READ_EXTERNAL_STORAGE_REQUEST_CODE  = 103;
    //请求写入外部存储
    private static final int      WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;
    //调用照相机返回图片临时文件
    private File                  tempFile;
    private EditText              nickNameEd;
    private SimpleDraweeView      avatarIv;
    private EditUserInfoPresenter mEditUserInfoPresenter;
    private Account.UserInfo mMyInfo;
    private RadioGroup            genderRadioGroup;
    private int                   gender                              = 1;
    private FileUploadPresenter mFileUploadPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editmyinfo);
        //创建拍照存储的临时文件
        createCameraTempFile(savedInstanceState);
        mEditUserInfoPresenter = new EditUserInfoPresenter();
        mFileUploadPresenter = new FileUploadPresenter();
        mFileUploadPresenter.attachView(this);
        mEditUserInfoPresenter.attachView(this);
        mMyInfo = Constants.myInfo;
    }

    @Override
    protected void setUpViews() {

        genderRadioGroup = (RadioGroup) findViewById(R.id.login_gemderrg);
        nickNameEd = (EditText) findViewById(R.id.editmyinfo_nickname_ed);
        avatarIv = (SimpleDraweeView) findViewById(R.id.editmyinfo_avatar);
        avatarIv.setOnClickListener(this);
        findViewById(R.id.submit_btn).setOnClickListener(this);
        genderRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("完善资料");
        text.setTextColor(mRes.getColor(R.color.app_title_color1));

        function.setTextResource("跳过", R.color.tab_text_s_color, 16);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_btn:
                mEditUserInfoPresenter.editUserInfo(Constants.myInfo.avatar, nickNameEd.getText() + "", gender,
                    mMyInfo.id+"");
                break;
            case R.id.editmyinfo_avatar:
                uploadHeadImage();
                break;
            default:
                break;
        }
    }

    /**
     * 创建调用系统照相机待存储的临时文件
     *
     * @param savedInstanceState
     */
    private void createCameraTempFile(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            tempFile = (File) savedInstanceState.getSerializable("tempFile");
        } else {
            tempFile = new File(
                checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"),
                System.currentTimeMillis() + ".jpg");
        }
    }

    /**
     * 检查文件是否存在
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    /**
     * 返回事件
     *
     * @param button
     */
    protected void backEvent(View button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 上传头像
     */
    private void uploadHeadImage() {
        View view = LayoutInflater.from(this).inflate(R.layout.popupwindow_choose_upload, null);
        TextView btnCarema = (TextView) view.findViewById(R.id.btn_camera);
        TextView btnPhoto = (TextView) view.findViewById(R.id.btn_photo);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });

        btnCarema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtils.checkSpPermission(EditMyinfoActivity.this,
                    new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE)) {
                    gotoCarema();
                }
                popupWindow.dismiss();
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtils.checkSpPermission(EditMyinfoActivity.this,
                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE)) {
                    gotoPhoto();
                }
                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 跳转到相册
     */
    private void gotoPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
    }

    /**
     * 跳转到照相机
     */
    private void gotoCarema() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        startActivityForResult(intent, REQUEST_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case REQUEST_PICK: //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    gotoClipActivity(uri);
                }
                break;
            case REQUEST_CROP_PHOTO: //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    //                    String cropImagePath = getRealFilePathFromUri(getApplicationContext(), uri);
                    //                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
                    avatarIv.setImageURI(uri);
                   String path = getRealFilePathFromUri(EditMyinfoActivity.this,uri);
//                    mEditUserInfoPresenter.uploadAvartar(new File(path));
                    uploadImage(path);
                    //                    cover.setImageBitmap(bitMap);
                    //此处后面可以将bitMap转为二进制上传后台网络
                    //......

                }
                break;
        }
    }

    /**
     * 打开截图界面
     *
     * @param uri
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipImageActivity.class);
        intent.putExtra("type", 1);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }
    private void uploadImage(String path) {
        File f = new File(path);
        mFileUploadPresenter.uploadAvatarFile(RequestBody.create(MediaType.parse("image/png"), f),
                RequestBody.create(MediaType.parse("text/plain"), "avatar"),
                RequestBody.create(MediaType.parse("text/plain"), ""),
                RequestBody.create(MediaType.parse("text/plain"), IOUtils.getToken(getContext()))
                );
    }
    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 book_line_content_list://开头的情况
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 外部存储权限申请返回
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                gotoCarema();
            }
        } else if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                gotoPhoto();
            }
        }
    }

    @Override
    public void showOnSuccess() {
        CommonUtils.showToast(mContext, "成功");
        finish();
    }

    @Override
    public void showOnFailure(String errormsg) {
        CommonUtils.showToast(mContext, "失败" + errormsg);
    }

    @Override
    public void setLoadingIndicator(boolean active) {


    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.female_login:
                gender = 2;
                break;
            case R.id.male_login:
                gender = 1;

                break;
            default:
                break;
        }
    }

    @Override
    public void resultData(Object o) {
        DataItem dataItem = (DataItem) o;
        CommonUtils.showToast(mContext, dataItem.msg + "");
        Constants.myInfo.avatar = dataItem.content;
    }
}
