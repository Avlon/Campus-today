package com.example.zhao.top_school.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.zhao.top_school.R;

/**
 * Created by zhao on 2016/3/30.
 */
public class WebActivity extends Activity{
    private String target_url;
    private Bundle bundle;
    private Button back_but;
    private WebView webView;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);
        Intent intent=getIntent();
        bundle=intent.getExtras();
        target_url=bundle.getString("url");
        //Log.e("1", target_url);
        initView();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.loadUrl(target_url);
    }
    public void initView(){
        back_but=(Button)this.findViewById(R.id.web_back_but);
        webView=(WebView)this.findViewById(R.id.my_webview);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        back_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
