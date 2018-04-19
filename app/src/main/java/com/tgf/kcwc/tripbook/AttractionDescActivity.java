package com.tgf.kcwc.tripbook;

import java.util.ArrayList;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.AttrationEntity;
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

public class AttractionDescActivity extends BaseActivity {
    private static final int IMAGE_PICKER = 1001;
    private final int REQUEST_CODE_CHOICE = 100;
    private TripSortRichEditor editor;
    private AttrationEntity attrationEntity;
    private EditText titleEd;
    private int type;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {
        type = getIntent().getIntExtra(Constants.IntentParams.INTENT_TYPE, 0);
        findViewById(R.id.btnGallery_lv).setOnClickListener(this);
        findViewById(R.id.save_btn).setOnClickListener(this);
        titleEd = (EditText) findViewById(R.id.attraction_titleed);
        editor = (TripSortRichEditor) findViewById(R.id.richEditor);
        findViewById(R.id.attraction_choicetv).setOnClickListener(this);
        findViewById(R.id.backiv).setOnClickListener(this);
        setType();
    }

    private void setType() {
       TextView titleTv = (TextView) findViewById(R.id.titletv);
        if(type ==3){
            titleEd.setHint("输入住宿名称或者住宿景点");
            titleTv.setText("酒店描述");
        }else if (type==2){
            titleEd.setHint("输入餐厅名称或者选择餐厅");
            titleTv.setText("餐厅描述");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attractiondesc);
        attrationEntity = new AttrationEntity();
        AttrationEntity fromAttraction = getIntent().getParcelableExtra(Constants.IntentParams.DATA);
        if (fromAttraction != null) {
            attrationEntity = fromAttraction;
            titleEd.setText(fromAttraction.title);
            editor.drawSaveData(attrationEntity.content);
        }
        editor.addHeaderEditView();
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
                if (TextUtils.isEmpty(titleEd.getText().toString())) {
                    CommonUtils.showToast(getContext(), "请填写标题");
                    return;
                }
                ArrayList<SEditorData> editList = editor.buildEditData();
                attrationEntity.content = editList;
                attrationEntity.title = titleEd.getText().toString().trim();
                Intent result = new Intent();
                result.putExtra(Constants.IntentParams.DATA, attrationEntity);
                setResult(RESULT_OK, result);
                finish();
                break;
            case R.id.attraction_choicetv:
                Intent toChoiceIntent = new Intent(getContext(), NearAttractionActivity.class);
                toChoiceIntent.putExtra(Constants.IntentParams.INTENT_TYPE, type);
                startActivityForResult(toChoiceIntent, REQUEST_CODE_CHOICE);
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
        } else if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CHOICE:
                    String titleStr = data.getStringExtra(Constants.IntentParams.TITLE);
                    if (!TextUtils.isEmpty(titleStr)) {
                        titleEd.setText(titleStr);
                        attrationEntity.org_id = data.getIntExtra(Constants.IntentParams.ID,0);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public Context getContext() {
        return this;
    }
}
