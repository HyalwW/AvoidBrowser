package com.hyaline.avoidbrowser.ui.fragments.webhunt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.hyaline.avoidbrowser.BR;
import com.hyaline.avoidbrowser.R;
import com.hyaline.avoidbrowser.base.BaseFragment;
import com.hyaline.avoidbrowser.data.AppDatabase;
import com.hyaline.avoidbrowser.data.beans.BrowseHistoryBean;
import com.hyaline.avoidbrowser.data.daos.BrowseHistoryDao;
import com.hyaline.avoidbrowser.databinding.FragmentWebHuntBinding;
import com.hyaline.avoidbrowser.ui.activities.imageviewer.ImageViewActivity;
import com.hyaline.avoidbrowser.ui.activities.main.MainActivity;
import com.hyaline.avoidbrowser.ui.customviews.LoadingView;
import com.hyaline.avoidbrowser.ui.customviews.NestedWebView;
import com.hyaline.avoidbrowser.ui.fragments.webhunt.option.WebOptionPopup;
import com.hyaline.avoidbrowser.utils.ClipboardUtils;
import com.hyaline.avoidbrowser.utils.ThreadPool;
import com.qmuiteam.qmui.util.QMUIDirection;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension;
import com.tencent.smtt.sdk.URLUtil;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by Wang.Wenhui
 * Date: 2020/7/16
 * Description: blablabla
 */
public class WebHuntFragment extends BaseFragment<WebHuntViewModel, FragmentWebHuntBinding> {
    private NestedWebView webView;
    private WebViewClient client;
    private WebChromeClient chromeClient;
    private boolean clearHistory, isRedirect;
    private OnWebStuffListner loadlistner;
    private PageInfo pageInfo;
    private BrowseHistoryDao browseHistoryDao;
    private Runnable saveHistoryRun = () -> {
        BrowseHistoryBean exist = browseHistoryDao.exist(pageInfo.getUrl());
        if (exist == null) {
            BrowseHistoryBean bean = new BrowseHistoryBean();
            bean.setUrl(pageInfo.getUrl());
            bean.setTitle(pageInfo.getTitleStr());
            browseHistoryDao.insert(bean);
        } else {
            exist.setTime(System.currentTimeMillis());
            browseHistoryDao.update(exist);
        }
    };

    private int px, py;

    private WebOptionPopup popup;
    private String[] urls = new String[]{"复制地址", "新页面打开"};
    private String[] imgs = new String[]{"复制图片地址", "查看图片", "保存图片"};

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
        if (getActivity() instanceof OnWebStuffListner) {
            loadlistner = (OnWebStuffListner) getActivity();
        }
        int hashCode = hashCode();
        Log.e("wwh", "WebHuntFragment --> onCreate: " + hashCode);
        pageInfo = new PageInfo(hashCode);
    }

    @Override
    protected void initData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            pageInfo.setUrl(arguments.getString("url"));
        }
        browseHistoryDao = AppDatabase.getDatabase().browseHistoryDao();
    }

    @Override
    protected void initView() {
        initWebView();
        initClient();
        initChromeClient();
        initSetting();
        webView.loadUrl(pageInfo.getUrl());
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
                dataBinding.loading.setVisibility(View.GONE);
                QMUIViewHelper.slideOut(dataBinding.loading, 200, null, true, QMUIDirection.BOTTOM_TO_TOP);
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        pageInfo.setIsShow(!hidden);
        if (!hidden) {
            loadlistner.onReceiveUrl(pageInfo.getUrl());
            loadlistner.onTitleChanged(pageInfo.getTitleStr());
        }
    }

    private void initChromeClient() {
        chromeClient = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView webView, String s) {
                super.onReceivedTitle(webView, s);
                loadlistner.onTitleChanged(s);
                pageInfo.setTitle(s);
            }

            @Override
            public void onReceivedIcon(WebView webView, Bitmap bitmap) {
                super.onReceivedIcon(webView, bitmap);
                pageInfo.setIcon(bitmap);
            }
        };
        webView.setWebChromeClient(chromeClient);
    }

    private void initWebView() {
        webView = new NestedWebView(getActivity());
        ViewGroup.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, loadlistner.getScrollHeight());
        dataBinding.base.addView(webView, 0, params);
        webView.setOnLongClickListener(v -> {
            WebView.HitTestResult result = webView.getHitTestResult();
            if (result == null) {
                return false;
            }
            switch (result.getType()) {
                case WebView.HitTestResult.EDIT_TEXT_TYPE: // 选中的文字类型
                case WebView.HitTestResult.PHONE_TYPE: // 处理拨号
                case WebView.HitTestResult.EMAIL_TYPE: // 处理Email
                case WebView.HitTestResult.GEO_TYPE: // 　地图类型
                case WebView.HitTestResult.UNKNOWN_TYPE: //未知
                    break;
                case WebView.HitTestResult.SRC_ANCHOR_TYPE: // 超链接
                    checkPopup();
                    popup.setStrs(urls, result.getExtra()).showAtLocation(webView, px, py);
                    return true;
                case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE: // 带有链接的图片类型
                case WebView.HitTestResult.IMAGE_TYPE: // 处理长按图片的菜单项
                    String url = result.getExtra();
                    Log.e("wwh", "WebHuntFragment --> initWebView: " + url);
                    if (url != null && URLUtil.isValidUrl(url)) {
                        checkPopup();
                        popup.setStrs(imgs, result.getExtra()).showAtLocation(webView, px, py);
                    }
                    return true;
            }
            return false;
        });
        webView.setOnTouchListener((v, event) -> {
            px = (int) event.getRawX();
            py = (int) event.getRawY();
            return false;
        });
    }

    private void checkPopup() {
        if (popup == null) {
            popup = new WebOptionPopup(getContext())
                    .listener((str, position, url) -> {
                        popup.dismiss();
                        switch (str) {
                            case "复制地址":
                                ClipboardUtils.copyText(url);
                                ToastUtils.showShort("地址复制成功~");
                                break;
                            case "新页面打开":
                                ((MainActivity) getActivity()).go2Fragment(url, null);
                                break;
                            case "复制图片地址":
                                ClipboardUtils.copyText(url);
                                ToastUtils.showShort("图片地址复制成功~");
                                break;
                            case "查看图片":
                                Intent intent = new Intent(getContext(), ImageViewActivity.class);
                                intent.putExtra("imageUrl", url);
                                startActivity(intent);
                                break;
                            case "保存图片":
                                viewModel.saveImage(url);
                                break;
                        }
                    });
        }
    }

    private void initClient() {
        client = new WebViewClient() {
            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                isRedirect = false;
                Log.e("wwh", "WebHuntFragment --> onPageStarted: " + s);
                dataBinding.loading.startLoading();
                if (loadlistner != null) {
                    loadlistner.onLoadStart();
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
                pageInfo.setUrl(s);
                if (loadlistner != null) {
                    loadlistner.onLoadFinish();
                    loadlistner.onReceiveUrl(s);
                }
                ThreadPool.fixed().execute(saveHistoryRun);
                if (webView.getHeight() < loadlistner.getScrollHeight()) {
                    ViewGroup.LayoutParams params = webView.getLayoutParams();
                    params.height = loadlistner.getScrollHeight();
                    webView.setLayoutParams(params);
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

    public PageInfo getPageInfo(boolean needBitmap) {
        if (needBitmap) {
            int width = QMUIDisplayHelper.getUsefulScreenWidth(webView);
            int height = QMUIDisplayHelper.getUsefulScreenHeight(webView);
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            IX5WebViewExtension extension = webView.getX5WebViewExtension();
            if (extension != null) {
                extension.snapshotVisible(bitmap,
                        false, false, false, false, 1, 1, () -> pageInfo.setCacheBitmap(bitmap));
            } else {
                try {
                    webView.setDrawingCacheEnabled(true);
                    Bitmap cache = Bitmap.createBitmap(webView.getDrawingCache());
                    webView.setDrawingCacheEnabled(false);
                    pageInfo.setCacheBitmap(cache);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return pageInfo;
    }
}
