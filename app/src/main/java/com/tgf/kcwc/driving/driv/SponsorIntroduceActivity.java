package com.tgf.kcwc.driving.driv;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.RichEditorEntity;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.richeditor.SEditorData;
import com.tgf.kcwc.view.richeditor.TripSortRichEditor;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/6.
 */

public class SponsorIntroduceActivity extends BaseActivity {

    private static final int IMAGE_PICKER = 1001;
    private TripSortRichEditor editor;
    private TextView mBtnGallery;
    private TextView mCancel;
    private TextView mTitleFunctionBtn;
    private ArrayList<SEditorData> mEditorDatas = new ArrayList<>();

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        isTitleBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sponsor_introduce);
        editor = (TripSortRichEditor) findViewById(R.id.richEditor);
        mBtnGallery = (TextView) findViewById(R.id.btnGallery);
        mCancel = (TextView) findViewById(R.id.cancel);
        mTitleFunctionBtn = (TextView) findViewById(R.id.title_function_btn);
        mBtnGallery.setOnClickListener(this);
        mEditorDatas = getIntent().getParcelableArrayListExtra(Constants.IntentParams.DATA);
        if (mEditorDatas != null) {
            if (mEditorDatas.size() != 0) {
                editor.drawSaveData(mEditorDatas);
            }
        }
        if (mEditorDatas.size() > 0) {
            editor.addHeaderEditViews(false);
        } else {
            editor.addHeaderEditViews(true);
        }


        mCancel.setOnClickListener(this);
        mTitleFunctionBtn.setOnClickListener(this);
    }

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
            case R.id.btnGallery:
                ImagePicker.getInstance().setMultiMode(true);
                ImagePicker.getInstance().setSelectLimit(9);
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);
                break;
            case R.id.cancel:
                finish();
                break;
            case R.id.title_function_btn:
                RichEditorEntity entity = new RichEditorEntity();
                entity.mEditorDatas = editor.buildEditData();
                Intent data = new Intent();
                data.putExtra(Constants.IntentParams.DATA, (Parcelable) entity);

                setResult(RESULT_OK, data);
                finish();
                break;
        }
    }
}
