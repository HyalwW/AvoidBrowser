package com.hyaline.avoidbrowser.ui.activities.imageviewer;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.hyaline.avoidbrowser.BR;
import com.hyaline.avoidbrowser.R;
import com.hyaline.avoidbrowser.base.BaseActivity;
import com.hyaline.avoidbrowser.databinding.ActivityImageViewerBinding;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class ImageViewActivity extends BaseActivity<ImageViewModel, ActivityImageViewerBinding> {
    private WebView webView;
    private String imgUrl;

    @Override
    protected int layoutId() {
        return R.layout.activity_image_viewer;
    }

    @Override
    protected int viewModelId() {
        return BR.viewModel;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        initWebView();
        initEvents();
    }

    private void initWebView() {
        webView = new WebView(this);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dataBinding.base.addView(webView, 0, params);
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
        WebChromeClient chromeClient = new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {
                return super.onJsAlert(webView, s, s1, jsResult);
            }

            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                Log.e("wwh", "ImageViewActivity --> onProgressChanged: " + i);
            }
        };
        webView.setWebChromeClient(chromeClient);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JsCommand(), "Avoid");
    }

    private void initEvents() {
        viewModel.getBackEvent().observe(this, o -> finish());
        viewModel.getSaveEvent().observe(this, o -> viewModel.saveImage(imgUrl));
        viewModel.getShareEvent().observe(this, o -> {
            //todo 后期再做
        });
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

    @Override
    protected void onDestroy() {
        if (webView != null) {
            dataBinding.base.removeView(webView);
            webView.stopLoading();
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearHistory();
            webView.removeAllViews();
            webView.destroy();
        }
        super.onDestroy();
    }

    public class JsCommand {
        @JavascriptInterface
        public void showOption(boolean show) {
            viewModel.showOption(show);
            Log.e("wwh", "JsCommand --> showOption: " + show);
        }
    }
}
