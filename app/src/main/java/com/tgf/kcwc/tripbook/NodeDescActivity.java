package com.tgf.kcwc.tripbook;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.NodeEntity;
import com.tgf.kcwc.mvp.model.AttrationEntity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.nestlistview.NestFullListView;
import com.tgf.kcwc.view.nestlistview.NestFullListViewAdapter;
import com.tgf.kcwc.view.nestlistview.NestFullViewHolder;
import com.tgf.kcwc.view.richeditor.SEditorData;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/5/6 0006
 * E-mail:hekescott@qq.com
 */

public class NodeDescActivity extends BaseActivity {

    private ListView mNodescLv;
    private LinearLayout headEditNode;
    private ArrayList<AttrationEntity> nodelist = new ArrayList<>();
    private Intent attractinIntentResult;
    private WrapAdapter<AttrationEntity> attrationAdapter;
    private TextView headDescTv;
    private ArrayList<SEditorData> headArticle;
    private NodeEntity mNodeEntity = new NodeEntity();
    private int modifyPos;
    private String title;
    private Intent fromIntent;
    private View editNotice;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {
        fromIntent = getIntent();
        mNodeEntity = fromIntent.getParcelableExtra(Constants.IntentParams.DATA);
        title = fromIntent.getStringExtra(Constants.IntentParams.TITLE);
        mNodescLv = (ListView) findViewById(R.id.nodedesc_lv);
        findViewById(R.id.attraction_btn).setOnClickListener(this);
        findViewById(R.id.canteen_btn).setOnClickListener(this);
        findViewById(R.id.hotel_btn).setOnClickListener(this);
        findViewById(R.id.save_btn).setOnClickListener(this);
        headEditNode = (LinearLayout) View.inflate(this, R.layout.layout_header_editnode, null);
        mNodescLv.addHeaderView(headEditNode);
        headDescTv = (TextView) headEditNode.findViewById(R.id.point_desctv);
        editNotice = findViewById(R.id.editnotice_layout);
        TextView titleTv = (TextView) findViewById(R.id.titletv);
        findViewById(R.id.backiv).setOnClickListener(this);
        if (!TextUtil.isEmpty(title)) {
            titleTv.setText(title);
        }
        headEditNode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toDescInten = new Intent(getContext(), NodeTitleDescActivity.class);
                toDescInten.putParcelableArrayListExtra(Constants.IntentParams.DATA, headArticle);
                startActivityForResult(toDescInten, REQUEST_CODE_DESC);
            }
        });
        if (mNodeEntity != null) {
            showData();
        }
        initAdapter();
    }

    private void showData() {
        if (mNodeEntity.book_line_content_list != null) {
            nodelist = mNodeEntity.book_line_content_list;
        }
        if (mNodeEntity.desc != null) {
            headArticle = mNodeEntity.desc;
            showHeadActicle(headArticle);
        }
        if(nodelist==null||nodelist.size()==0){
//            editnotice_layout
            editNotice.setVisibility(View.VISIBLE);
            TextView clickSpan = (TextView) findViewById(R.id.click_edittv);
            Drawable drawable = getResources().getDrawable(R.drawable.icon_pencil);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            String text ="点[smile]编辑结点概述";
            SpannableString spannable = new SpannableString(text);
            ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
            spannable.setSpan(span,1,1+"[smile]".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            clickSpan.setText(spannable);
        }else{
            editNotice.setVisibility(View.GONE);
        }

    }

    private void initAdapter() {
        attrationAdapter = new WrapAdapter<AttrationEntity>(getContext(), nodelist,
                R.layout.listitem_tripbook_editnode) {
            @Override
            public void convert(ViewHolder helper, AttrationEntity item) {
                helper.setText(R.id.node_desctv, item.title);
                SimpleDraweeView poinIconIv = helper.getView(R.id.point_iconiv);

                GenericDraweeHierarchy hierarchy = poinIconIv.getHierarchy();
                if (item.type == 1) {
                    hierarchy.setPlaceholderImage(R.drawable.btn_attraction);
                } else if (item.type == 2) {
                    hierarchy.setPlaceholderImage(R.drawable.btn_canteen);
                } else {
                    hierarchy.setPlaceholderImage(R.drawable.btn_hotel);
                }
                NestFullListView contentLv = helper.getView(R.id.nodedesc_contentlv);
                if (item.content != null) {
                    contentLv.setAdapter(new NestFullListViewAdapter<SEditorData>(
                            R.layout.listitem_nodedesc_content, item.content) {
                        @Override
                        public void onBind(int pos, SEditorData attrationEntity,
                                           NestFullViewHolder holder) {
                            TextView nodeContentTv = holder.getView(R.id.node_content_tv);
                            SimpleDraweeView nodeContentIv = holder.getView(R.id.node_content_iv);
                            if (TextUtils.isEmpty(attrationEntity.getInputStr())) {
                                nodeContentTv.setVisibility(View.GONE);
                                attrationEntity.setInputStr(null);
                            } else {
                                nodeContentTv.setVisibility(View.VISIBLE);
                                nodeContentTv.setText(attrationEntity.getInputStr());
                            }
                            if (TextUtils.isEmpty(attrationEntity.getImageUrl())) {
                                nodeContentIv.setVisibility(View.GONE);
                                attrationEntity.setImageUrl(null);
                            } else {
                                nodeContentIv.setVisibility(View.VISIBLE);
                                String url = URLUtil.builderImgUrl(attrationEntity.getImageUrl(),
                                        540, 270);
                                nodeContentIv.setImageURI(Uri.parse(url));
                            }
                        }
                    });
                }
            }
        };

        mNodescLv.setAdapter(attrationAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nodedesc);
        attractinIntentResult = new Intent(getContext(), AttractionDescActivity.class);
        mNodescLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                modifyPos = position-1;
                AttrationEntity attrationEntity = nodelist.get(position-1);
                attractinIntentResult.putExtra(Constants.IntentParams.INTENT_TYPE, attrationEntity.type);
                attractinIntentResult.putExtra(Constants.IntentParams.DATA, attrationEntity);
                startActivityForResult(attractinIntentResult, REQUEST_CODE_MODIFY);
            }
        });

    }

    @Override
    public void onClick(View view) {
        attractinIntentResult.removeExtra(Constants.IntentParams.DATA);
        switch (view.getId()) {
            case R.id.hotel_btn:
                attractinIntentResult.putExtra(Constants.IntentParams.INTENT_TYPE, 3);
                startActivityForResult(attractinIntentResult, REQUEST_CODE_HOTEL);
                break;
            case R.id.canteen_btn:
                attractinIntentResult.putExtra(Constants.IntentParams.INTENT_TYPE, 2);
                startActivityForResult(attractinIntentResult, REQUEST_CODE_CANTEEN);
                break;
            case R.id.backiv:
                finish();
                break;
            case R.id.attraction_btn:
                attractinIntentResult.putExtra(Constants.IntentParams.INTENT_TYPE, 1);
                startActivityForResult(attractinIntentResult, REQUEST_CODE_ATTRACTION);
                break;
            case R.id.save_btn:
                Intent fromEdittripIntent = new Intent(getContext(), EditTripbookActivity.class);
                if(headArticle==null){
                    CommonUtils.showToast(getContext(),"节点概述不能为空");
                    return;
                }
                mNodeEntity.desc = headArticle;
                mNodeEntity.book_line_content_list = nodelist;
                fromEdittripIntent.putExtra(Constants.IntentParams.DATA, mNodeEntity);
//                Gson gson = new Gson();
//                String jsonContent = null;
//                if (mNodeEntity != null) {
//                    jsonContent = gson.toJson(mNodeEntity);
//                }
                setResult(RESULT_OK, fromEdittripIntent);
                finish();
                break;
            default:
                break;
        }
    }

    public Context getContext() {
        return this;
    }

    private final int REQUEST_CODE_HOTEL = 100;
    private final int REQUEST_CODE_CANTEEN = 101;
    private final int REQUEST_CODE_ATTRACTION = 102;
    private final int REQUEST_CODE_DESC = 103;
    private final int REQUEST_CODE_MODIFY = 104;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_HOTEL:
                    AttrationEntity hotelEntity = data
                            .getParcelableExtra(Constants.IntentParams.DATA);
                    if (hotelEntity != null) {
                        hotelEntity.type = 3;
                        nodelist.add(hotelEntity);
                        attrationAdapter.notifyDataSetChanged();
                        editNotice.setVisibility(View.GONE);
                    }
                    break;
                case REQUEST_CODE_CANTEEN:
                    AttrationEntity canteenEntity = data
                            .getParcelableExtra(Constants.IntentParams.DATA);
                    if (canteenEntity != null) {
                        canteenEntity.type = 2;
                        nodelist.add(canteenEntity);
                        attrationAdapter.notifyDataSetChanged();
                        editNotice.setVisibility(View.GONE);
                    }
                    break;
                case REQUEST_CODE_ATTRACTION:
                    AttrationEntity attraEntity = data
                            .getParcelableExtra(Constants.IntentParams.DATA);
                    if (attraEntity != null) {
                        attraEntity.type = 1;
                        nodelist.add(attraEntity);
                        attrationAdapter.notifyDataSetChanged();
                        editNotice.setVisibility(View.GONE);
                    }
                    break;
                case REQUEST_CODE_DESC:
                    ArrayList<SEditorData> tempheadArticle = data
                            .getParcelableArrayListExtra(Constants.IntentParams.DATA);
                    headDescTv.setText("节点概述");
                    if (tempheadArticle != null) {
                        headArticle = tempheadArticle;
                        showHeadActicle(headArticle);
                    }
                    break;
                case REQUEST_CODE_MODIFY:
                    AttrationEntity modidfyArticle = data
                            .getParcelableExtra(Constants.IntentParams.DATA);
                    if (modidfyArticle != null) {
                        nodelist.set(modifyPos, modidfyArticle);
                        attrationAdapter.notifyDataSetChanged();
                    }
                    break;
                default:
                    break;
            }
        }

    }

    private void showHeadActicle(ArrayList<SEditorData> headArticle) {
        for (SEditorData sEditorData : headArticle) {
            if (!TextUtils.isEmpty(sEditorData.getInputStr())) {
                headDescTv.setText(sEditorData.getInputStr());
                break;
            }
        }
    }


}
