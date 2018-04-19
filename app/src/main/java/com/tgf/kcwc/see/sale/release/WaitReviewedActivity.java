package com.tgf.kcwc.see.sale.release;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.callback.OnSingleClickListener;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.me.sale.SaleMgrListActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.view.FunctionView;

/**
 * @anthor noti
 * @time 2017/9/13
 * @describle  等待审核
 */
public class WaitReviewedActivity extends BaseActivity{
    //状态 提交审核材料+展位申请+等待审核
    ImageView commitIv, applyIv, waitIv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_reviewed);
    }

    @Override
    protected void setUpViews() {
        commitIv = (ImageView) findViewById(R.id.commit_iv);
        applyIv = (ImageView) findViewById(R.id.apply_iv);
        waitIv = (ImageView) findViewById(R.id.wait_iv);
        findViewById(R.id.apply_btn).setOnClickListener(new OnSingleClickListener() {
            @Override
            protected void onSingleClick(View view) {
                // TODO: 2017/9/14 查看申请详情
                CommonUtils.startNewActivity(mContext, SaleMgrListActivity.class);
            }
        });
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("等待审核");
        function.setImageResource(R.drawable.cover_default);
        function.setOnClickListener(new OnSingleClickListener() {
            @Override
            protected void onSingleClick(View view) {
                // TODO: 2017/9/13 参展申请须知
//                CommonUtils.startNewActivity(WaitReviewedActivity.this, ApplyHintActivity.class);
                Intent intent = new Intent(WaitReviewedActivity.this, ApplyHintActivity.class);
//                intent.putExtra(Constants.IntentParams.ID, eventId);
                intent.putExtra(Constants.IntentParams.INDEX, 2);
                startActivity(intent);
            }
        });
    }
}
