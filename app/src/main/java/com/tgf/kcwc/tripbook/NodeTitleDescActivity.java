package com.tgf.kcwc.tripbook;

import java.util.ArrayList;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.richeditor.SEditorData;
import com.tgf.kcwc.view.richeditor.TripSortRichEditor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Auther: Scott
 * Date: 2017/5/5 0005
 * E-mail:hekescott@qq.com
 */

public class NodeTitleDescActivity extends BaseActivity {
    private static final int   IMAGE_PICKER = 1001;
    private TripSortRichEditor editor;
    private ArrayList<SEditorData> headArticle;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
    }

    @Override
    protected void setUpViews() {
        headArticle  = getIntent().getParcelableArrayListExtra(Constants.IntentParams.DATA);
        findViewById(R.id.btnGallery_lv).setOnClickListener(this);
        findViewById(R.id.save_btn).setOnClickListener(this);
        findViewById(R.id.backiv).setOnClickListener(this);
        editor = (TripSortRichEditor) findViewById(R.id.richEditor);

        if(headArticle!=null){
            editor.drawSaveData(headArticle);
            editor.addHeaderEditView(false);
        }else {
            editor.addHeaderEditView(true);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nodetitledesc);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btnGallery_lv:
                ImagePicker.getInstance().setMultiMode(true);
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);
                break;
            case R.id.save_btn:
//                if(TextUtils.isEmpty(titleEd.getText().toString())){
//                    CommonUtils.showToast(getContext(),"请填写标题");
//                    return;
//                }
                headArticle = editor.buildEditData();
//                attrationEntity.title =titleEd.getText().toString().trim();
                Intent result = new Intent();
                result.putParcelableArrayListExtra(Constants.IntentParams.DATA, headArticle);
                setResult(RESULT_OK, result);
                finish();
                break;
            case R.id.backiv:
                finish();
                break;
            default:
                break;
        }
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

    public Context getContext() {
        return this;
    }
}
