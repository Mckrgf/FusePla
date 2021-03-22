package com.supcon.mes.module_webview.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.app.annotation.apt.Router;
import com.supcon.common.BaseConstant;
import com.supcon.common.view.base.activity.BaseWebViewActivity;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.view.js.BridgeHandler;
import com.supcon.common.view.view.js.BridgeUtil;
import com.supcon.common.view.view.js.BridgeWebView;
import com.supcon.common.view.view.js.CallBackFunction;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.middleware.PLAApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_webview.IntentRouter;
import com.supcon.mes.module_webview.R;
import com.supcon.mes.module_webview.ui.listener.OrientationWatchDog;
import com.supcon.mes.module_webview.util.map.FixedParamKey;
import com.supcon.mes.module_webview.util.map.MapDefaultWebClientEx;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by wangshizhan on 2019/10/30
 * Email:wangshizhan@supcom.com
 * 通用移动视图打开容器，用于webwidget跳转
 */
@Router(Constant.Router.WEB)
public class WebActivity extends BaseWebViewActivity {
//    //1:默认的，固定竖屏；（正向竖屏，不支持感应）;
//    //2:固定横屏（正向横屏，不支持感应）；
//    //3:如果开启"旋转屏幕"，可以感应横竖屏；如果没有开启"旋转屏幕， 默认是竖屏;
//    //4:如果开启"旋转屏幕"，可以感应横竖屏；如果没有开启"旋转屏幕， 默认是横屏;
//    public final static int TYPE_SCREEN_ORIENTATION_PORTRAIT = 1;
//    public final static int TYPE_SCREEN_ORIENTATION_LANDSCAPE = 2;
//    public final static int TYPE_SCREEN_ORIENTATION_SENSOR_PORTRAIT = 3;
//    public final static int TYPE_SCREEN_ORIENTATION_SENSOR_LANDSCAPE = 4;

    private boolean isFirstIn = true;
    private boolean hasTitle = false;
    private String title = "";
    private String screenOrientType = Constant.SystemCode.SCREEN_TYPE_PORTRAIT;
    /**
     * 监听类
     */
    private OrientationWatchDog mOrientationWatchDog;


    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        super.initView();
        hasTitle = getIntent().getBooleanExtra(Constant.WebUrl.HAS_TITLE, false);
        screenOrientType = getIntent().getStringExtra(Constant.WebUrl.SCREEN_ORIENT);
        title = getIntent().getStringExtra(Constant.WebUrl.WEB_TITLE);

        if (hasTitle) {
            setTitle(title);
        }

        toggleTitle(hasTitle);
        if(isList) {
            rightBtn.setImageResource(R.drawable.sl_top_refresh);
            rightBtn.setVisibility(View.VISIBLE);
        }

        setOrientationByType();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        ((ViewGroup)webView.getParent()).removeView(webView);
        webView.removeAllViews();
        webView.destroy();

        if (mOrientationWatchDog != null) {
            mOrientationWatchDog.destroy();
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_web;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                LogUtil.d(""+url);
            }
        });

        //地图上面点击后跳转到 地图缓存的页面
        webView.registerHandler("goMapCache", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {

                IntentRouter.go(context, Constant.Router.MAP_CACHE_SETTING);
            }

        });

        //地图有的页面是 直接打开的，有的是H5中连接打开的，所以为了避免不必要的截获，我要添加一个检测
        String url = getIntent().getStringExtra(BaseConstant.WEB_URL);
        if (!StringUtil.isBlank(url) && StringUtil.contains(url, FixedParamKey.SESGISConfig)) {
            webView.setWebViewClient(new MapDefaultWebClientEx(webView, context));
        }
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isFirstIn) {
            //调用刷新方法
            webView.evaluateJavascript("vue.loadMore()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    LogUtil.d("onReceiveValue:",value);
                }
            });
        }
        isFirstIn = false;
    }

    @Override
    protected void onReload() {
        webView.reload();
    }

    @Override
    public void sendRefreshEvent() {
        super.sendRefreshEvent();
        if(!isList){
            EventBus.getDefault().post(new RefreshEvent());
        }
    }

    @Override
    public void onPageStart(WebView view, String url, Bitmap favicon) {
        super.onPageStart(view, url, favicon);
        LogUtil.d("onPageStart");
    }

    @Override
    public void onPageCommitVisible(WebView view, String url) {
        super.onPageCommitVisible(view, url);
        LogUtil.d("onPageCommitVisible");

    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        LogUtil.d("onPageFinished");
        BridgeUtil.webViewLoadLocalJs(view, BridgeWebView.MobileJs);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(RefreshEvent event) {

    }

    @Override
    protected void showDialog(String message, JsResult result) {
        super.showDialog(message, result);
        new CustomDialog(context)
                .twoButtonAlertDialog(message)
                .bindClickListener(R.id.redBtn, v -> {result.confirm();}, true)
                .bindClickListener(R.id.grayBtn, v -> {result.cancel();}, true)
                .show();
    }

    @Override
    public void goNext(String url) {

        webView.loadUrl(url);
    }

    @Override
    protected void createNewWeb(String data) {
        String url = null;
        try {
            JSONObject jsonObject = new JSONObject(data);
            url = jsonObject.getString("param");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e("new url:"+data);

        if(TextUtils.isEmpty(url)){
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(BaseConstant.WEB_COOKIE, PLAApplication.getCooki());
        bundle.putString(BaseConstant.WEB_AUTHORIZATION, PLAApplication.getToken());
        bundle.putString(BaseConstant.WEB_URL, PLAApplication.getHost()+url);
        bundle.putBoolean(Constant.WebUrl.HAS_TITLE, hasTitle);
        IntentRouter.go(context, Constant.Router.WEB, bundle);
    }

    @Override
    protected void uploadFile(File file, CallBackFunction callBackFunction) {


    }

    private void downloadByBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void goBrowser(String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    /**
     //1:默认的，固定竖屏；（正向竖屏，不支持感应）;
     //2:固定横屏（正向横屏，不支持感应）；
     //3:如果开启"旋转屏幕"，可以感应横竖屏；如果没有开启"旋转屏幕， 默认是竖屏;
     //4:如果开启"旋转屏幕"，可以感应横竖屏；如果没有开启"旋转屏幕， 默认是横屏;
     */
    private void setOrientationByType() {

        if (StringUtil.equalsIgnoreCase(screenOrientType, Constant.SystemCode.SCREEN_TYPE_LANDSCAPE)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (StringUtil.equalsIgnoreCase(screenOrientType, Constant.SystemCode.SCREEN_TYPE_PORTRAIT)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (StringUtil.equalsIgnoreCase(screenOrientType, Constant.SystemCode.SCREEN_TYPE_DEFAULT_LANDSCAPE)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            initOrientationWatchdog();
        } else if (StringUtil.equalsIgnoreCase(screenOrientType, Constant.SystemCode.SCREEN_TYPE_DEFAULT_PORTRAIT)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            initOrientationWatchdog();
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏

            // 延伸显示区域到刘海
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                getWindow().setAttributes(lp);
                // 设置页面全屏显示
                final View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }

        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //显示状态栏

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                // 设置页面全屏显示
                final View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE );
            }
        }
    }

    /**
     * 初始化屏幕方向监听器。用来监听屏幕方向。结果通过OrientationListener回调出去。
     */
    private void initOrientationWatchdog() {
        mOrientationWatchDog = new OrientationWatchDog(this);
        mOrientationWatchDog.startWatch();
    }
}
