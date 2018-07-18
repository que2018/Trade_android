package com.coin.trade.main;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.widget.ProgressBar;

import com.coin.trade.constant.ADDR;
import com.coin.trade.R;

public class AboutActivity extends AppCompatActivity {

    private WebView aboutWebView;
    private ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        loadingBar = findViewById(R.id.loading);
        aboutWebView = findViewById(R.id.about);

        aboutWebView.getSettings().setJavaScriptEnabled(true);
        aboutWebView.setWebViewClient(new AboutWebclient());
        aboutWebView.loadUrl(ADDR.ABOUT);
	}

    class AboutWebclient extends WebViewClient
    {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            loadingBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}
