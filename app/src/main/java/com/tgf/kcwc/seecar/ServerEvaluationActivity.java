package com.tgf.kcwc.seecar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hedgehog.ratingbar.RatingBar;
import com.tgf.kcwc.R;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.EvaluationModel;
import com.tgf.kcwc.mvp.model.YuyueBuyModel;
import com.tgf.kcwc.mvp.presenter.BuyMotoEvaluationPresenter;
import com.tgf.kcwc.mvp.view.MotoEvaluationView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.NumFormatUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FlowLayout;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/2/27 0027
 * E-mail:hekescott@qq.com
 */

public class ServerEvaluationActivity extends BaseActivity implements MotoEvaluationView {

    private FlowLayout evaluationTagLayout;
    private BuyMotoEvaluationPresenter buyMotoEvaluationPresent;
    private StringBuilder evaluateTag;
    private EditText descEd;
    private RatingBar ratingBar;
    private RatingBar myCommitratingBar;
    private int starCount;
    private YuyueBuyModel.OrgItem mToChatUser;
    private String mToken;
    private  int mOrderId;
    private String[] starDesc ={"非常不满意，各方面都很差","不满意，比较差","一般，需要改善","比较满意，但仍可改善","非常满意，无可挑剔"};
    private ArrayList<EvaluationModel.Tag> mTags;
    private TextView starDescTv;
    private KPlayCarApp kPlayCarApp;
    private TextView limitTv;
    private CheckBox ansycCheckbox;
    private boolean isAnsycName;
    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("服务评价");
    }

    @Override
    protected void setUpViews() {
        Intent fromIntent = getIntent();
        mToken = IOUtils.getToken(getContext());
        mToChatUser = fromIntent.getParcelableExtra(Constants.IntentParams.DATA);
        mOrderId = fromIntent.getIntExtra(Constants.IntentParams.ID,-1);
        buyMotoEvaluationPresent = new BuyMotoEvaluationPresenter();
        buyMotoEvaluationPresent.attachView(this);
        buyMotoEvaluationPresent.getTags(mToken,mOrderId, mToChatUser.id);
        findViewById(R.id.evaluate_commit).setOnClickListener(this);
        descEd = findViewById(R.id.evaluate_desc);
        ansycCheckbox = findViewById(R.id.evaluate_ansyccb);
        starDescTv =  findViewById(R.id.evstar_desctv);
        limitTv =  findViewById(R.id.evaluate_limmittv);
        descEd.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                this.temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                limitTv.setText(temp.length() + "/150");
            }
        });
        myCommitratingBar = (RatingBar) findViewById(R.id.evaluate_commitrating);
        myCommitratingBar.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float RatingCount) {
                starCount = (int) RatingCount;
                if(starCount>0&&starCount<6){
                    starDescTv.setText(starDesc[starCount-1]);
                }

            }
        });
        ansycCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAnsycName = isChecked;
            }
        });
        showHead();
    }

    private void showHead() {
        SimpleDraweeView avatarIv = (SimpleDraweeView) findViewById(R.id.evaluate_avatar);
        String avatarUrl = URLUtil.builderImgUrl(mToChatUser.avatar, 144, 144);
        avatarIv.setImageURI(Uri.parse(avatarUrl));
        TextView nickNameTv = (TextView) findViewById(R.id.evaluate_nickname);
        nickNameTv.setText(mToChatUser.nickname);
        TextView orgNameTv = (TextView) findViewById(R.id.evaluate_orgname);
        orgNameTv.setText(mToChatUser.org_name);
        RatingBar orgRatingBar = (RatingBar) findViewById(R.id.evaluate_rating);
        orgRatingBar.setStar(NumFormatUtil.getFmtString(mToChatUser.star));
        TextView orgLevalTv = (TextView) findViewById(R.id.evaluate_leveal);
        orgLevalTv.setText(mToChatUser.star);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        evaluationTagLayout = (FlowLayout) findViewById(R.id.evaluation_tag);
        evaluationTagLayout.setHorizontalSpacing(10);
        evaluationTagLayout.setVerticalSpacing(10);

    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.evaluate_commit:
                if(starCount==0){
                    CommonUtils.showToast(getContext(),"请填写评价星级");
                    return;
                }
                evaluateTag= new StringBuilder();
                for(int i=0;i<mTags.size();i++){
                    EvaluationModel.Tag tag = mTags.get(i);
                    if(tag.iSchecked){
                        if(i==mTags.size()-1){
                            evaluateTag.append(tag.tagName);
                        }else {
                            evaluateTag.append(tag.tagName+",");
                        }
                    }
                }
                int isAnscy =isAnsycName?1:0 ;
                buyMotoEvaluationPresent.postEvaluation(mToken, mOrderId, mToChatUser.id, starCount, evaluateTag.toString(), descEd.getText() + "",isAnscy);
                break;
            default:
                break;
        }
    }


    @Override
    public void showCommitSuccess() {
        CommonUtils.showToast(getContext(),"评价成功");
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void showTags(ArrayList<EvaluationModel.Tag> tags) {
        mTags = tags;
        addTagItems(evaluationTagLayout,tags);
    }
    /**
     * 添加标签
     * @param datas
     */
    private void addTagItems(final FlowLayout tagLayout, List<EvaluationModel.Tag> datas) {
        tagLayout.removeAllViews();
        int size = datas.size();
        for (int i = 0; i < size; i++) {
            final EvaluationModel.Tag hobbyTag = datas.get(i);
            View v = LayoutInflater.from(mContext).inflate(R.layout.text_tag_item4, tagLayout,
                    false);
            v.setTag(hobbyTag.id);
            tagLayout.addView(v);
            final TextView tv = (TextView) v.findViewById(R.id.name);
            tv.setText(hobbyTag.tagName + "");
            tv.setTag(hobbyTag.id);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    hobbyTag.iSchecked = !hobbyTag.iSchecked;
                    if (hobbyTag.iSchecked) {
                        v.setBackgroundResource(R.drawable.shapeborder_radiusgreenrect_bg3);
                        tv.setTextColor(mRes.getColor(R.color.white));
                    } else {
                        v.setBackgroundResource(R.drawable.shapeborder_radiusgrayrect_bg);
                        tv.setTextColor(mRes.getColor(R.color.text_color17));
                    }
                }
            });
        }
    }
    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {
        CommonUtils.showToast(getContext(),"评价失败");
    }

    @Override
    public Context getContext() {
        return this;
    }
}
