package com.tgf.kcwc.see.sale;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.RichEditorEntity;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.richeditor.SEditorData;
import com.tgf.kcwc.view.richeditor.SortRichEditor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Author：Jenny
 * Date:2017/2/21 19:48
 * E-mail：fishloveqin@gmail.com
 * 宝贝描述页面
 */

public class GoodsDescActivity extends BaseActivity {

    protected ImageButton  mTitleBarBack;
    protected TextView     mTitleBarText;
    protected TextView     mTitleFunctionBtn;

    private TextView       tvSort;

    private SortRichEditor editor;

    private ImageView      ivCamera;
    private TextView       ivGallery;
    private Button         btnPosts;

    private File           mCurrentPhotoFile; // 照相机拍照得到的图片

    public Intent getTakePickIntent(File f) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        return intent;
    }

    @Override
    protected void titleBarCallback(ImageButton back, final FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {

        initView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTitleBar = false;
        super.setContentView(R.layout.activity_goods_desc);

    }

    private static final int IMAGE_PICKER = 1001;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data
                    .getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                for (ImageItem item : images) {
                    editor.addImage(item.path);
                }
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_gallery:

                ImagePicker.getInstance().setMultiMode(true);
                ImagePicker.getInstance().setFocusWidth(BitmapUtils.dp2px(mContext, 280));
                ImagePicker.getInstance().setFocusHeight(BitmapUtils.dp2px(mContext, 280));
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);

                break;
            case R.id.iv_camera:

                break;

        }
    }

    /**
     * 负责处理编辑数据提交等事宜，请自行实现
     */
    private void dealEditData(List<SEditorData> editList) {
        System.out.println("dealEditData:" + editList.size());
        for (SEditorData itemData : editList) {
            if (itemData.getInputStr() != null) {
                System.out.println("str:" + itemData.getInputStr());
            } else if (itemData.getImagePath() != null) {
                System.out.println("path:" + itemData.getImagePath());
            }
        }
    }

    private void initView() {
        mTitleBarBack = (ImageButton) findViewById(R.id.title_bar_back);
        mTitleBarText = (TextView) findViewById(R.id.title_bar_text);
        mTitleFunctionBtn = (TextView) findViewById(R.id.title_function_btn);
        ivGallery = (TextView) findViewById(R.id.iv_gallery);
        ivCamera = (ImageView) findViewById(R.id.iv_camera);
        editor = (SortRichEditor) findViewById(R.id.richEditor);
        ivCamera = (ImageView) findViewById(R.id.iv_camera);
        ivGallery.setOnClickListener(this);
        ivCamera.setOnClickListener(this);
        initTitleBarData();
    }

    private void initTitleBarData() {

        mTitleBarText.setText("宝贝描述");
        final RichEditorEntity entity = IOUtils.getObject(mContext,
            Constants.KeyParams.RICH_E_DATA);
        String editStr = "保存";

        if (entity != null) {
            final List<SEditorData> datas = entity.mEditorDatas;
            final String title = entity.title;
          // editor.getEtTitle().setText(title + "");
            editor.drawSaveData(datas);
        } else {
            editor.addHeaderEditView();
        }

        mTitleFunctionBtn.setText(editStr);
        backEvent(mTitleBarBack);
        mTitleFunctionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<SEditorData> editList = editor.buildEditData();
                RichEditorEntity entity = new RichEditorEntity();
                entity.mEditorDatas = editList;
                entity.title = editor.getTitleData();
                //dealEditData(editList);
                IOUtils.writeObject(mContext, entity, Constants.KeyParams.RICH_E_DATA);

                finish();
            }
        });
    }
}
