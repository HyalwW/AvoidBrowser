package com.hyaline.avoidbrowser.ui.fragments.webhunt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.hyaline.avoidbrowser.BR;
import com.hyaline.avoidbrowser.R;
import com.hyaline.avoidbrowser.base.BaseFragment;
import com.hyaline.avoidbrowser.databinding.FragmentWebHuntBinding;
import com.hyaline.avoidbrowser.ui.customviews.LoadingView;
import com.hyaline.avoidbrowser.ui.fragments.OnLoadListner;
import com.qmuiteam.qmui.util.QMUIDirection;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/16
 * Description: blablabla
 */
public class WebHuntFragment extends BaseFragment<WebHuntViewModel, FragmentWebHuntBinding> {
    private String url;
    private WebView webView;
    private WebViewClient client;
    private boolean clearHistory, isRedirect;
    private OnLoadListner loadListner;

    @Override
    protected int layoutId() {
        return R.layout.fragment_web_hunt;
    }

    @Override
    protected int viewModelId() {
        return BR.viewModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof OnLoadListner) {
            loadListner = (OnLoadListner) getActivity();
        }
    }

    @Override
    protected void initData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            url = arguments.getString("url");
        }
    }

    private Animation.AnimationListener listener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            dataBinding.loading.setVisibility(View.GONE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    @Override
    protected void initView() {
        initWebView();
        initClient();
        initSetting();
        webView.loadUrl(url);
        dataBinding.loading.setListener(new LoadingView.OnLoadListener() {
            @Override
            public void onLoadAnimStart() {
                if (dataBinding.loading.getVisibility() != View.VISIBLE) {
                    dataBinding.loading.setVisibility(View.VISIBLE);
                    QMUIViewHelper.slideIn(dataBinding.loading, 150, null, true, QMUIDirection.TOP_TO_BOTTOM);
                }
            }

            @Override
            public void onLoadAnimDone() {
                QMUIViewHelper.slideOut(dataBinding.loading, 200, listener, true, QMUIDirection.BOTTOM_TO_TOP);
            }
        });
    }

    private void initWebView() {
        webView = new WebView(getActivity());
        ViewGroup.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dataBinding.base.addView(webView, 0, params);
    }

    private void initClient() {
        client = new WebViewClient() {
            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                isRedirect = false;
                Log.e("wwh", "WebHuntFragment --> onPageStarted: " + s);
                dataBinding.loading.startLoading();
                if (loadListner != null) {
                    loadListner.onLoadStart();
                }
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                if (isRedirect) {
                    return;
                }
                if (clearHistory) {
                    webView.clearHistory();
                    clearHistory = false;
                }
                dataBinding.loading.done();
                if (loadListner != null) {
                    loadListner.onLoadFinish();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                isRedirect = true;
                if (s.startsWith("http://") || s.startsWith("https://")) {
                    return false;
                } else {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            }
        };
        webView.setWebViewClient(client);
    }

    private void initSetting() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        //支持缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        //隐藏原生的缩放控件
        settings.setDisplayZoomControls(false);

        //支持内容重新布局
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        settings.supportMultipleWindows();
//        settings.setSupportMultipleWindows(true);
//        //设置缓存模式
//        settings.setDomStorageEnabled(true);
//        settings.setDatabaseEnabled(true);
//        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
//        settings.setAppCacheEnabled(true);
//        settings.setAppCachePath(webView.getContext().getCacheDir().getAbsolutePath());
//
//        //设置可访问文件
//        settings.setAllowFileAccess(true);
//        //当webview调用requestFocus时为webview设置节点
//        settings.setNeedInitialFocus(true);
//        //支持自动加载图片
//        if (Build.VERSION.SDK_INT >= 19) {
//            settings.setLoadsImagesAutomatically(true);
//        } else {
//            settings.setLoadsImagesAutomatically(false);
//        }
//        settings.setNeedInitialFocus(true);
//        //设置编码格式
//        settings.setDefaultTextEncodingName("UTF-8");
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dataBinding.loading.destroy();
        if (webView != null) {
            dataBinding.base.removeView(webView);
            webView.stopLoading();
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearHistory();
            webView.removeAllViews();
            webView.destroy();
        }
    }

    @Override
    public boolean onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return false;
    }

    public void goBack() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        }
    }

    public void goForward() {
        if (webView != null && webView.canGoForward()) {
            webView.goForward();
        }
    }

    public void go2Page(String url, boolean clearHistory) {
        if (webView != null) {
            this.clearHistory = clearHistory;
            webView.loadUrl(url);
        }
    }

    public void refresh() {
        if (webView != null) {
            webView.reload();
        }
    }

    public void stopLoad() {
        if (webView != null) {
            webView.stopLoading();
        }
    }
}
