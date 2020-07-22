package com.hyaline.avoidbrowser.ui.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hyaline.avoidbrowser.R;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class ImageViewActivity extends AppCompatActivity {
    private LinearLayout base;
    private WebView webView;
    private String imgUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        base = findViewById(R.id.base);
        webView = new WebView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        params.weight = 1;
        base.addView(webView, params);
        webView.loadUrl("file:///android_asset/imageViewer.html");
        imgUrl = getIntent().getStringExtra("imageUrl");
        WebViewClient client = new WebViewClient() {
            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                Log.e("wwh", "ImageViewActivity-->onPageStarted(): " + s);
                super.onPageStarted(webView, s, bitmap);
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                Log.e("wwh", "ImageViewActivity-->onPageFinished(): " + s);
                webView.loadUrl("javascript:showImage('" + imgUrl + "')");
                super.onPageFinished(webView, s);
            }

        };
        webView.setWebViewClient(client);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }
}
