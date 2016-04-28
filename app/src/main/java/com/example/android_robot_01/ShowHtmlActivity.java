package com.example.android_robot_01;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lu.financemanager.R;
import com.lu.momeymanager.model.HtmlModel;
import com.lu.momeymanager.view.activity.BaseFragmentActivity;
import com.lu.momeymanager.view.widget.ProgressView;

/**
 * Created by lenovo on 2016/4/9.
 */
@ContentView(R.layout.activity_show_html)
public class ShowHtmlActivity extends BaseFragmentActivity implements HtmlModel.IGetHtmlTitle{

    @ViewInject(R.id.webView)
    private WebView webView;

    public static final String URL="URL";
    public static final String TEXT="TEXT";

    @ViewInject(R.id.tvTitle)
    private TextView tvTitle;

    @ViewInject(R.id.ivMore)
    private ImageView ivMore;
    private ProgressView progressView;

    private HtmlModel htmlModel;
    @Override
    protected void initWidget() {
        progressView=new ProgressView(this);
        WebSettings wSet = webView.getSettings();
//        wSet.setJavaScriptEnabled(true);

        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        ivMore.setVisibility(View.GONE);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(false);

        progressView.apptoTarget(webView);

        htmlModel=new HtmlModel();
        htmlModel.setiGetHtmlTitle(this);
    }

    @OnClick({R.id.ivBack})
    protected void viewClick(View view) {
       switch (view.getId()){
           case R.id.ivBack:
               finish();
               break;

       }
    }

    @Override
    protected void initData() {
        Bundle binder=getIntent().getExtras();
        if(binder==null){
            finish();
        }
        tvTitle.setText(binder.getString(TEXT));
        String url=binder.getString(URL);
        d("url:"+url);
        webView.loadUrl(url);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                d("shouldOverrideUrlLoading url:"+url);
                view.loadUrl(url);
                htmlModel.getTitle(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressView.setPrencent(newProgress);
            }

        });

    }

    @Override
    public void getHtmlTitle(String title) {
        tvTitle.setText(title);
        tvTitle.requestFocus();
    }
}
