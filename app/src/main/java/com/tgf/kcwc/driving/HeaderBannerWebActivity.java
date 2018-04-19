package com.tgf.kcwc.driving;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.view.FunctionView;

/**
 * Created by Administrator on 2017/6/5.
 */

public class HeaderBannerWebActivity extends BaseActivity {

    protected String title = "";
    protected String url = "";
    protected WebView mWebView;

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        if (TextUtils.isEmpty(title) || title.equals("0")) {

        } else {
            text.setText(title);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        url = getIntent().getStringExtra(Constants.IntentParams.ID);
        title = getIntent().getStringExtra(Constants.IntentParams.ID2);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headerweb);
        mWebView = (WebView) findViewById(R.id.web_view);

        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                // 默认的处理方式，WebView变成空白页
                //handler.cancel();
            }
        });

        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.getSettings().setUseWideViewPort(true);  //将图片调整到适合webview的大小
        mWebView.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        mWebView.getSettings().setSupportZoom(true);  //支持缩放，默认为true。是下面那个的前提。
        mWebView.getSettings().setBuiltInZoomControls(true); //设置内置的缩放控件。

        mWebView.getSettings().setDisplayZoomControls(false); //隐藏原生的缩放控件
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);  //提高渲染的优先级

        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        mWebView.getSettings().supportMultipleWindows();  //多窗口
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //关闭webview中缓存
        mWebView.getSettings().setAllowFileAccess(true);  //设置可以访问文件
        mWebView.getSettings().setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        mWebView.getSettings().setLoadsImagesAutomatically(true);  //支持自动加载图片
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");//设置编码格式
        mWebView.getSettings().setDomStorageEnabled(true);//设置编码格式

        //WebView加载web资源
        mWebView.loadUrl(url);

        mWebView.setWebViewClient(new WebViewClient() {
            //网页加载开始时调用，显示加载提示旋转进度条
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            //网页加载完成时调用，隐藏加载提示旋转进度条
            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
            }

            //网页加载失败时调用，隐藏加载提示旋转进度条
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                CommonUtils.showToast(mContext, "加载失败");
            }
        });

    }


    @Override
    public void onClick(View view) {

    }

    //改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();//返回上一页面
                return true;
            } else {
               // System.exit(0);//退出程序
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
